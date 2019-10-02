package com.journaldev.sparkdemo;

import java.io.IOException;

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
    	
	    sparkConfigure sparkConfig = new sparkConfigure();
    	
    	segmentationBUS segmentation = new segmentationBUS();
    	
    	standardizeBUS standardize = new standardizeBUS();
    	
    	removeStopWordsBUS removeStopWords = new removeStopWordsBUS();
    	
    	//taggingBUS tagging = new taggingBUS();
    	
    	//sentimentAnalyser model = new sentimentAnalyser();
    	
    	//MongoSparkHelper mongoHelper = new MongoSparkHelper(sparkConfig.getRemoteSparkContext()); // remote server uit
    	//MongoSparkHelper mongoHelper = new MongoSparkHelper(sparkConfig.getSparkContext()); // localhost
    	//mongoHelper.generateInputFile();

/*
 * 
 * GIAI DOAN: TIEN XU LI
 * 
 */ 	
    	/*
		 * STANDARDIZE DATA
		 */
    	standardize.standardizeData(sparkConfig);
	    helpFunction.removeUnusedFile("Standardize");
  
		/*
		 * TOKENIZER/ SEGMENTATION
		 */
    	segmentation.wordSegmentation(sparkConfig);
    	helpFunction.removeUnusedFile("Segementation");
		/*
		 * REMOVE STOP WORD
		 */
		
		  try { removeStopWords.correctData(sparkConfig); } catch (IOException e) { 
		  e.printStackTrace(); }
		  helpFunction.removeUnusedFile("StopWords");
		 
		  //helpFunction.splitString();
		/*
		 * WORD TAGGING
		 */
    	//tagging.wordTagging(sparkConfig);
    	
    	//Trich xuat du lieu cho mo hinh GibbsLDA
       	//GibbsLDAService gibbsLDA = new GibbsLDAService(sparkConfig);
    	//gibbsLDA.generateInputFile();
	    
	    //mongoHelper.readFileAsDataFrame(System.getProperty("user.dir") + "/output/part-00003");
    }
}
