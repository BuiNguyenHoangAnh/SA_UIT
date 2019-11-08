package model;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import org.deeplearning4j.datasets.iterator.*;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;

public class sentimentIterator implements DataSetIterator {
	/**
	 * Declare variables
	 */
	private static final long serialVersionUID = 1L; // default serial Version
	private final WordVectors wordVectors;
    private final int batchSize;
    private final int vectorSize;
    private final int truncateLength;

    private int cursor = 0;
    private final File[] positiveFiles;
    private final File[] negativeFiles;
    private final TokenizerFactory tokenizerFactory;
    private Map mapIndexRandom;

    /**
     * @param dataDirectory the directory of the data set
     * @param wordVectors WordVectors object
     * @param batchSize Size of each minibatch for training
     * @param truncateLength If reviews exceed
     * @param train If true: return the training data, else return the testing data.
     */
    public sentimentIterator(String dataDirectory, WordVectors wordVectors, int batchSize, int truncateLength, boolean train) throws IOException {
        this.batchSize = batchSize;
        this.vectorSize = wordVectors.lookupTable().layerSize();
        
		/*
		 * data folder structure
		 * train/neg/...
		 * train/pos/...
		 * test/neg/...
		 * test/pos/...
		*/
        File positiveFile = new File(FilenameUtils.concat(dataDirectory, (train ? "train" : "test") + "/pos/") + "/");
        File negativeFile = new File(FilenameUtils.concat(dataDirectory, (train ? "train" : "test") + "/neg/") + "/");
        
        //File positiveFile = new File(FilenameUtils.concat(dataDirectory, (train ? "train" : "test") + "/dt/") + "/");
        //File negativeFile = new File(FilenameUtils.concat(dataDirectory, (train ? "train" : "test") + "/csvc/") + "/");
        
        positiveFiles = positiveFile.listFiles();
        negativeFiles = negativeFile.listFiles();

        this.wordVectors = wordVectors;
        this.truncateLength = truncateLength;

        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        
        int fileLength = positiveFiles.length + negativeFiles.length;
        mapIndexRandom = genRandomMapIndex(0, fileLength);
    }


    @Override
    public DataSet next(int num) {
        if (cursor >= positiveFiles.length + negativeFiles.length) throw new NoSuchElementException();
        try{
            return nextDataSet(num);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    //0=pos, 1=neg.
    private DataSet nextDataSet(int num) throws IOException {
        // Load reviews to String. Alternate positive and negative reviews
        List<String> reviews = new ArrayList<>(num);
        int[] positive = new int[num];
        int segment1 = positiveFiles.length;
        int segment2 = positiveFiles.length + negativeFiles.length;
        for( int i=0; i < num && cursor < totalExamples(); i++ ){
            int indexFile = (int) mapIndexRandom.get(cursor);
            if(0 <= indexFile && indexFile < segment1){
                //Load positive review
                int posReviewNumber = indexFile;
                String review = FileUtils.readFileToString(positiveFiles[posReviewNumber]);
                reviews.add(review);
                positive[i] = 0;
            } else if(segment1 <= indexFile && indexFile < segment2){
                //Load negative review
                int negReviewNumber = indexFile - segment1;
                String review = FileUtils.readFileToString(negativeFiles[negReviewNumber]);
                reviews.add(review);
                positive[i] = 1;
            }
            cursor++;
        }

        // Tokenize reviews and filter out unknown words
        List allTokens = new ArrayList<>(reviews.size());
        int maxLength = 0;
        for(String s : reviews){
            List<String> tokens = tokenizerFactory.create(s).getTokens();
            List tokensFiltered = new ArrayList<>();
            for(String t : tokens ){
                if(wordVectors.hasWord(t)) tokensFiltered.add(t);
            }
            allTokens.add(tokensFiltered);
            maxLength = Math.max(maxLength,tokensFiltered.size());
        }

        //If longest review exceeds 'truncateLength': only take the first 'truncateLength' words
        if(maxLength > truncateLength) maxLength = truncateLength;

        //Create data for training
        //Here: we have reviews.size() examples of varying lengths
        INDArray features = Nd4j.create(reviews.size(), vectorSize, maxLength); // N:300:300
        INDArray labels = Nd4j.create(reviews.size(), 2, maxLength);            // N:2:300 //Two labels: positive or negative
        //Because we are dealing with reviews of different lengths and only one output at the final time step: use padding arrays
        //Mask arrays contain 1 if data is present at that time step for that example, or 0 if data is just padding
        INDArray featuresMask = Nd4j.zeros(reviews.size(), maxLength);          // N:300
        INDArray labelsMask = Nd4j.zeros(reviews.size(), maxLength);            // N:300

        int[] temp = new int[2];
        for( int i=0; i < reviews.size(); i++ ){
            List tokens = (List) allTokens.get(i);
            temp[0] = i;
            //Get word vectors for each word in review, and put them in the training data
            for( int j=0; j < tokens.size() && j < maxLength; j++ ){
                String token = (String) tokens.get(j);
                INDArray vector = wordVectors.getWordVectorMatrix(token);
                features.put(new INDArrayIndex[]{NDArrayIndex.point(i), NDArrayIndex.all(), NDArrayIndex.point(j)}, vector);

                temp[1] = j;
                featuresMask.putScalar(temp, 1.0);  //Word is present (not padding) for this example + time step -> 1.0 in features mask
            }

            int idx = positive[i]; //(positive[i] ? 0 : 1);
            int lastIdx = Math.min(tokens.size(),maxLength);
            labels.putScalar(new int[]{i,idx,lastIdx-1},1.0);   //Set label:[1,0] for positive, [0,1] for negative
            labelsMask.putScalar(new int[]{i,lastIdx-1},1.0);   //Specify that an output exists at the final time step for this example
        }

        return new DataSet(features,labels,featuresMask,labelsMask);
    }
    
	/*
	 * random methods
	 */
    public Map genRandomMapIndex(int min, int max){
        Map mapIndex = new ConcurrentHashMap();
        for(int i=0; i < max; i++){
            mapIndex.put(i, i);
        }
        for(int i=0; i < max + 1000; i++){
            int a = randomRange(min, max);
            int b = randomRange(min, max);
            //swap value
            mapIndex.put(a, mapIndex.put(b, mapIndex.get(a)));
        }
        return mapIndex;
    }
    
    public int randomRange(int min, int max){
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }
    
	/*
	 * Override methods
	 */
    @Override
    public int totalExamples() {
        return positiveFiles.length + negativeFiles.length;
    }

    @Override
    public int inputColumns() {
        return vectorSize;
    }

    @Override
    public int totalOutcomes() {
        return 2;
    }

    @Override
    public void reset() {
        cursor = 0;
    }

    @Override
    public int batch() {
        return batchSize;
    }

    @Override
    public int cursor() {
        return cursor;
    }

    @Override
    public int numExamples() {
        return totalExamples();
    }

    @Override
    public void setPreProcessor(DataSetPreProcessor preProcessor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List getLabels() {
        return Arrays.asList("positive", "negative");
    }

    @Override
    public boolean hasNext() {
        return cursor < numExamples();
    }

    @Override
    public DataSet next() {
        return next(batchSize);
    }
    
	/*
	 * helper function
	 */
    // Convenience method for loading review to String
    public String loadReviewToString(int index) throws IOException{
        File f;
        if(index%2 == 0) f = positiveFiles[index/2];
        else f = negativeFiles[index/2];
        return FileUtils.readFileToString(f);
    }

    // Convenience method to get label for review
    public boolean isPositiveReview(int index){
        return index%2 == 0;
    }
    
    /**
     * Used post training to load a review from a file to a features INDArray that can be passed to the network output method
     *
     * @param file      File to load the review from
     * @param maxLength Maximum length (if review is longer than this: truncate to maxLength). Use Integer.MAX_VALUE to not nruncate
     * @return          Features array
     * @throws IOException If file cannot be read
     */
    public INDArray loadFeaturesFromFile(File file, int maxLength) throws IOException {
        String review = FileUtils.readFileToString(file);
        return loadFeaturesFromString(review, maxLength);
    }
    

    /**
     * Used post training to convert a String to a features INDArray that can be passed to the network output method
     *
     * @param reviewContents Contents of the review to vectorize
     * @param maxLength Maximum length (if review is longer than this: truncate to maxLength). Use Integer.MAX_VALUE to not nruncate
     * @return Features array for the given input String
     */
    public INDArray loadFeaturesFromString(String reviewContents, int maxLength){
        List<String> tokens = tokenizerFactory.create(reviewContents).getTokens();
        List<String> tokensFiltered = new ArrayList<>();
        for(String t : tokens ){
            if(wordVectors.hasWord(t)) tokensFiltered.add(t);
        }
        int outputLength = Math.min(maxLength,tokensFiltered.size());
        if (outputLength == 0 ) outputLength = 1;
        INDArray features = Nd4j.create(1, vectorSize, outputLength);

        int count = 0;
        for( int j=0; j<tokensFiltered.size() && count<maxLength; j++ ){
            String token = tokensFiltered.get(j);
            INDArray vector = wordVectors.getWordVectorMatrix(token);
            if(vector == null){
                continue;   //Word not in word vectors
            }
            features.put(new INDArrayIndex[]{NDArrayIndex.point(0), NDArrayIndex.all(), NDArrayIndex.point(j)}, vector);
            count++;
        }

        return features;
    }


	@Override
	public boolean resetSupported() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean asyncSupported() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public DataSetPreProcessor getPreProcessor() {
		// TODO Auto-generated method stub
		return null;
	}
}