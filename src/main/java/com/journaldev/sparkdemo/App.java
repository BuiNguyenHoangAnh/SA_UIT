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
    public static final String VALIDATE_PATH1 = "/home/buinguyenhoanganh/Desktop/SA_UIT/data/validate/neg-training";
    public static final String VALIDATE_PATH2 = "/home/buinguyenhoanganh/Desktop/SA_UIT/data/validate/neg-training";

    public static void main( String[] args ) throws IOException
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
	    
	    //languagePreprocessor preproccessor = new languagePreprocessor(sparkConfig);
	    //preproccessor.run(null); 

//    	sentimentAnalyser model = new sentimentAnalyser();
    	//model.sentimentModel();
//	    model.testData(VALIDATE_PATH1,VALIDATE_PATH2);
    	
    	new sampleUI().startSampleUI();
    	
    }
}
