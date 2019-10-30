package com.journaldev.sparkdemo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Dataset;

import com.mongodb.spark.MongoSpark;

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
    public static final String VALIDATE_PATH1 = "/home/tranhamduong/project-sa-uit/workspace/project/data/validate/aspect-dt";
    public static final String VALIDATE_PATH2 = "/home/tranhamduong/project-sa-uit/workspace/project/data/validate/aspect-csvc";

    public static void main( String[] args ) throws IOException
    {
/*
 * 
 * declare variables
 * 
 * 
 */
    	
    	//MongoSparkHelper mongoHelper = new MongoSparkHelper(sparkConfig.getRemoteSparkContext()); // remote server uit
    	//MongoSparkHelper mongoHelper = new MongoSparkHelper(sparkConfig.getSparkContext()); // localhost
    	//mongoHelper.generateInputFile();
    	
	    sparkConfigure sparkConfig = new sparkConfigure();
	    
	    //languagePreprocessor preproccessor = new languagePreprocessor(sparkConfig);
	    //preproccessor.run(null); 

    	sentimentAnalyser model = new sentimentAnalyser();
    	//model.sentimentModel();
	    model.testData(VALIDATE_PATH1,VALIDATE_PATH2);
    	
    	
    	


/*
 * 
 * GIAI DOAN: TIEN XU LI (RUN MANUALLY)
 * 
 * 

    	
    	taggingBUS tagging = new taggingBUS();
 */ 	
     	//segmentationBUS segmentation = new segmentationBUS();
    	
    	//standardizeBUS standardize = new standardizeBUS();
    	
    	//removeStopWordsBUS removeStopWords = new removeStopWordsBUS();
    	
    	/*
		 * STANDARDIZE DATA
		 */
    	//standardize.standardizeData(sparkConfig);
	    //helpFunction.removeUnusedFile("Standardize");
  
		/*
		 * TOKENIZER/ SEGMENTATION
		 */
    	//segmentation.wordSegmentation(sparkConfig);
    	//helpFunction.removeUnusedFile("Segementation");
		/*
		 * REMOVE STOP WORD
		 */
		
		// try { removeStopWords.correctData(sparkConfig); } catch (IOException e) { 
		 // e.printStackTrace(); }
    			 
    	//helpFunction.removeEmptyLine("");		
		 /*
		 * WORD TAGGING
		 */
    	//tagging.wordTagging(sparkConfig);
    	

    }
}
