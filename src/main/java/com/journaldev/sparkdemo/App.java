package com.journaldev.sparkdemo;

import java.io.File;
import java.io.IOException;

<<<<<<< HEAD
=======
import org.apache.commons.io.FileUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Dataset;

import com.mongodb.spark.MongoSpark;

>>>>>>> languageProcessor
import model.sentimentAnalyser;
import segmentation.segmentationBUS;
import standardize.standardizeBUS;
import stopword.removeStopWordsBUS;
import tagging.taggingBUS;
import util.helpFunction;
import util.sparkConfigure;
import vn.uit.edu.preprocessor.languagePreprocessor;
import vn.uit.edu.sa.connectDB.MongoSparkHelper;
import vn.uit.edu.sa.define.Constant;
import vn.uit.edu.sa.dto.Comment;
import vn.uit.edu.sa.gibbsLDA.*;

/**
 * 
 * SENTIMENT ANALYSIS
 * Application will read a file then execute word segmentation
 * 
 */
public class App 	
{	

    public static void main( String[] args )
    {
/*
 * 
 * declare variables
 * 
 * 
 */
    	sparkConfigure spark = new sparkConfigure();
    	
    	segmentationBUS segmentation = new segmentationBUS();
    	
    	//MongoSparkHelper mongoHelper = new MongoSparkHelper(sparkConfig.getRemoteSparkContext()); // remote server uit
    	//MongoSparkHelper mongoHelper = new MongoSparkHelper(sparkConfig.getSparkContext()); // localhost
    	//mongoHelper.generateInputFile();
    	
    	removeStopWordsBUS removeStopWords = new removeStopWordsBUS();
    	
    	taggingBUS tagging = new taggingBUS();
    	
//    	sentimentAnalyser model = new sentimentAnalyser();
   
	    sparkConfigure sparkConfig = new sparkConfigure();
	    
	    languagePreprocessor preproccessor = new languagePreprocessor(sparkConfig);
	    preproccessor.run(null); 
    
    	//sentimentAnalyser model = new sentimentAnalyser();
    	


/*
 * 
 * GIAI DOAN: TIEN XU LI (RUN MANUALLY)
 * 
 * 
 *     	//segmentationBUS segmentation = new segmentationBUS();
    	
    	//standardizeBUS standardize = new standardizeBUS();
    	
    	//removeStopWordsBUS removeStopWords = new removeStopWordsBUS();
    	
    	//taggingBUS tagging = new taggingBUS();
 */ 	
    	/*
		 * STANDARDIZE DATA
		 */
    	//standardize.standardizeData(sparkConfig);
	    //helpFunction.removeUnusedFile("Standardize");
  
		/*
		 * TOKENIZER/ SEGMENTATION
		 */
//    	segmentation.wordSegmentation(spark);
    	
		/*
		 * REMOVE STOP WORD
		 */
//    	try {
//    		removeStopWords.correctData(spark);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
		/*
		 * WORD TAGGING
		 */
//    	tagging.wordTagging(spark);
    	//segmentation.wordSegmentation(sparkConfig);
    	//helpFunction.removeUnusedFile("Segementation");
		/*
		 * REMOVE STOP WORD
		 */
		
		 //try { removeStopWords.correctData(sparkConfig); } catch (IOException e) { 
		 // e.printStackTrace(); }
    			 
    	//helpFunction.removeEmptyLine("");		
		 /*
		 * WORD TAGGING
		 */
    	//tagging.wordTagging(sparkConfig);
    	
    }
}
