package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.deeplearning4j.datasets.iterator.AsyncDataSetIterator;
import org.deeplearning4j.datasets.iterator.DataSetIterator;
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
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class sentimentAnalyser {
	/**
     * Location to save and extract the training/testing data
     */
    public static final String DATA_PATH = "/path/to/data/train/";
    /**
     * Location (local file system) for the model word2vector. Set this manually.
     */
    public static final String WORD_VECTORS_PATH = "/path/to/data/vnw2v.bin";
    // Directory save checkpoint MultiLayerNetwork. 
    public static final String CHECKPOINT_PATH = "/path/to/checkpoint/";
    
    public void sentimentModel() throws IOException {
    	long start = System.currentTimeMillis();

        int batchSize = 50;     //Number of examples in each minibatch
        int vectorSize = 300;   //Size of the word vectors. 300 in the model.
        int nEpochs = 5;        //Number of epochs (full passes of training data) to train on
        int truncateReviewsToLength = 300;  //Truncate reviews with length (# words) greater than this

        //Set up network configuration
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).iterations(1)
                .updater(Updater.RMSPROP)
                .regularization(true).l2(1e-5)
                .weightInit(WeightInit.XAVIER)
                .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue).gradientNormalizationThreshold(1.0)
                .learningRate(0.0018)
                .list(2)
                .layer(0, new GravesLSTM.Builder().nIn(vectorSize).nOut(200)
                        .activation("softsign").build())
                .layer(1, new RnnOutputLayer.Builder().activation("softmax")
                        .lossFunction(LossFunctions.LossFunction.MCXENT).nIn(200).nOut(2).build())
                .pretrain(false).backprop(true).build();
        
        ScoreIterationListener listener = new ScoreIterationListener(1);
        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();
        net.setListeners(listener);

        //DataSetIterators for training and testing respectively
        //Using AsyncDataSetItersentimentIteratorator to do data loading in a separate thread; this may improve performance vs. waiting for data to load
        WordVectors wordVectors = WordVectorSerializer.loadGoogleModel(new File(WORD_VECTORS_PATH), true, false);
        DataSetIterator train = new AsyncDataSetIterator(new sentimentIterator(DATA_PATH,wordVectors,batchSize,truncateReviewsToLength,true),1);
        DataSetIterator test = new AsyncDataSetIterator(new sentimentIterator(DATA_PATH,wordVectors,100,truncateReviewsToLength,false),1);

        System.out.println("Starting training...");
        for( int i=0; i < nEpochs; i++ ){
            net.fit(train);
            train.reset();
            System.out.println("Epoch " + i + " complete. Starting evaluation:");

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

            System.out.println(evaluation.stats());
            
            System.out.println("Save checkpoint MultiLayerNetwork time " + i + "..............");
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

        //saveLatestModel
        System.out.println("Save saveLatestModel...");
        LocalFileModelSaver saver = new LocalFileModelSaver(CHECKPOINT_PATH);
        saver.saveLatestModel(net, 1);
        System.out.println("----- Example complete -----");
        System.out.println("===== Example complete Time: " + (System.currentTimeMillis() - start) + " ms");
    }
}
