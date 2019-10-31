package model;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.deeplearning4j.earlystopping.saver.LocalFileModelSaver;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import breeze.linalg.all;


public class sentimentAnalyser {
	/*
	 * // // Declare variables //
	 */
	// Location to save and extract the training/testing data
    public static final String DATA_PATH = "/home/tranhamduong/project-sa-uit/workspace/project/data/aspect"; // need to replace this path. Ex: /path/to/data/train/
    // Location for the model word2vector
    public static final String WORD_VECTORS_PATH = "/home/tranhamduong/project-sa-uit/workspace/project/word2vecModel/vector.txt"; //need to replace this path. Ex: /path/to/data/w2v.bin
    // Directory save checkpoint MultiLayerNetwork. 
    public static final String CHECKPOINT_PATH = "/home/tranhamduong/project-sa-uit/workspace/project/checkpoint/"; // ex: /path/to/checkpoint/<name>
    //
    public static final String VALIDATE_PATH = "/home/tranhamduong/project-sa-uit/workspace/project/data/validate/file";
    //
    //public static final String MODEL_PATH = "/home/tranhamduong/project-sa-uit/workspace/project/model/model-sentiment-csvc.zip";
    //
    public static final String MODEL_PATH = "/home/tranhamduong/project-sa-uit/workspace/project/model/model-sentiment-dt.zip";
    //
    //public static final String MODEL_PATH = "/home/tranhamduong/project-sa-uit/workspace/project/model/model-aspect.zip";

    
    public static sentimentIterator train;
    public static sentimentIterator test;
    public static MultiLayerNetwork net;
    
    private static final int batchSize = 50; //Number of examples in each minibatch
    private static final int vectorSize = 300; //Size of the word vectors. 300 in the model.
    private static final int nEpochs = 5; //Number of epochs (full passes of training data) to train on
    private static final int truncateReviewsToLength = 300; ////Truncate reviews with length (# words) greater than this
    
    public sentimentAnalyser() throws IOException {
        //DataSetIterators for training and testing respectively
        //Using AsyncDataSetItersentimentIteratorator to do data loading in a separate thread; this may improve performance vs. waiting for data to load
        WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(WORD_VECTORS_PATH));
        train = new sentimentIterator(DATA_PATH,wordVectors,batchSize,truncateReviewsToLength,true);
        test = new sentimentIterator(DATA_PATH,wordVectors,batchSize,truncateReviewsToLength,false);
    }
    
	/*
	 * training model
	 */
    public void sentimentModel() throws IOException {
    	
        //Set up network configuration
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).iterations(1)
                .updater(Updater.RMSPROP)
                .iterations(1)
                .regularization(true).l2(1e-5)
                .weightInit(WeightInit.XAVIER)
                .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue).gradientNormalizationThreshold(1.0)
                .learningRate(0.0018)
                .list()
                .layer(0, new GravesLSTM.Builder().nIn(vectorSize).nOut(200)
                        .activation(Activation.SOFTSIGN).build())
                .layer(1, new RnnOutputLayer.Builder().activation(Activation.SOFTMAX)
                        .lossFunction(LossFunctions.LossFunction.MCXENT).nIn(200).nOut(2).build())
                .pretrain(false).backprop(true).build();
        
        ScoreIterationListener listener = new ScoreIterationListener(1);
        net = new MultiLayerNetwork(conf);
        net.init();
        net.setListeners(listener);

        // Training: start here
        for( int i=0; i < nEpochs; i++ ){
            net.fit(train);
            train.reset();

            //Run evaluation. This is on 25k reviews, so can take some time
            Evaluation evaluation = new Evaluation();
            while(test.hasNext()){
                DataSet t = test.next();
                INDArray features = t.getFeatureMatrix();
                INDArray lables = t.getLabels();
                INDArray inMask = t.getFeaturesMaskArray();
                INDArray outMask = t.getLabelsMaskArray();
                INDArray predicted = net.output(features,false,inMask,outMask);

                evaluation.evalTimeSeries(lables,predicted,outMask);
            }
            test.reset();

            //System.out.println(evaluation.stats());
            
            // Save checkpoint
            String confPath = CHECKPOINT_PATH + "conf" + i + ".json";
            String netPath = CHECKPOINT_PATH + "sentimentNet" + i + ".bin";
            try {
                //Write the network configuration:
                FileUtils.write(new File(confPath), net.getLayerWiseConfigurations().toJson());
                System.out.println("Save file conf: " + confPath);
                
                //Write the network parameters:
                DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Paths.get(netPath)));
                Nd4j.write(net.params(), dos);
                System.out.println("Save sentimentNet: " + netPath);
            } catch (Exception e) {
            }
        }
        
        //save Latest Model
        //LocalFileModelSaver saver = new LocalFileModelSaver(CHECKPOINT_PATH);
        //saver.saveLatestModel(net, 1);
        ModelSerializer.writeModel(net, MODEL_PATH , true);
        //ModelSerializer.writeModel(net, "model.zip", true);
        
    }
    
    public void testData(String filePath1, String filePath2) throws IOException{
    	
        //After training: load a single example and generate predictions
    	
		try {
			int countPos  = 0;
			int countNeg  = 0;
			MultiLayerNetwork net = ModelSerializer.restoreMultiLayerNetwork(new File(MODEL_PATH));
			
			List<String> allLines = Files.readAllLines(Paths.get(filePath1));
			int total = allLines.size();
			for (String line : allLines) {				
				
		        INDArray features = test.loadFeaturesFromString(line, truncateReviewsToLength);
		        INDArray networkOutput = net.output(features);
		        long timeSeriesLength = networkOutput.size(2);
		        INDArray probabilitiesAtLastWord = networkOutput.get(NDArrayIndex.point(0), NDArrayIndex.all(), NDArrayIndex.point((int) (timeSeriesLength - 1)));


		        //System.out.println("\n\nProbabilities at last time step:");
		        
		        if (probabilitiesAtLastWord.getDouble(0) > probabilitiesAtLastWord.getDouble(1)) {
		        	countPos++;
		        }
		        else {
		        	countNeg++;
			        System.out.println("\n\n WRONG PREDICTION-------------------------------");
			        System.out.println("WRONG Short positive review: \n" + line);
			        System.out.println("p(positive): " + probabilitiesAtLastWord.getDouble(0));
			        System.out.println("p(negative): " + probabilitiesAtLastWord.getDouble(1));

		        }
			}
			
			System.out.println("CALCAULATE ");
			System.out.println("Positive: " + countPos + "/" + total + " => "+ ((float) countPos / (float) total));
			System.out.println("Negative: " + countNeg + "/" + total + " => "+ ((float) countNeg / (float) total));
			System.out.println("==================================================================");
			
			
			countPos  = 0;
			countNeg  = 0;
			net.clear();
			net = ModelSerializer.restoreMultiLayerNetwork(new File(MODEL_PATH));
			
			allLines.clear();
			allLines = Files.readAllLines(Paths.get(filePath2));
			total = allLines.size();
			for (String line : allLines) {				
				
		        INDArray features = test.loadFeaturesFromString(line, truncateReviewsToLength);
		        INDArray networkOutput = net.output(features);
		        long timeSeriesLength = networkOutput.size(2);
		        INDArray probabilitiesAtLastWord = networkOutput.get(NDArrayIndex.point(0), NDArrayIndex.all(), NDArrayIndex.point((int) (timeSeriesLength - 1)));

		        //System.out.println("\n\n-------------------------------");
		        //System.out.println("Short negative review: \n" + line);
		        //System.out.println("\n\nProbabilities at last time step:");
		       //System.out.println("p(positive): " + probabilitiesAtLastWord.getDouble(0));
		       // System.out.println("p(negative): " + probabilitiesAtLastWord.getDouble(1));
		        
		        if (probabilitiesAtLastWord.getDouble(0) > probabilitiesAtLastWord.getDouble(1)) {
		        	countPos++;
			        System.out.println("\n\n WRONG PREDICTION-------------------------------");
			        System.out.println("WRONG Short negative review: \n" + line);
			        System.out.println("p(positive): " + probabilitiesAtLastWord.getDouble(0));
			        System.out.println("p(negative): " + probabilitiesAtLastWord.getDouble(1));
		        }
		        else 
		        	countNeg++;
			}
			
			System.out.println("CALCAULATE ");
			System.out.println("Positive: " + countPos + "/" + total + " => "+ ((float) countPos / (float) total));
			System.out.println("Negative: " + countNeg + "/" + total + " => "+ ((float) countNeg / (float) total));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println("----- Example complete -----");
    }
    
}