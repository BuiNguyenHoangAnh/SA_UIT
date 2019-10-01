package com.journaldev.sparkdemo;

import java.io.IOException;

import model.sentimentAnalyser;
import segmentation.segmentationBUS;
import standardize.standardizeBUS;
import stopword.removeStopWordsBUS;
import tagging.taggingBUS;
import util.sparkConfigure;

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
 */
    	sparkConfigure spark = new sparkConfigure();
    	
    	segmentationBUS segmentation = new segmentationBUS();
    	
    	standardizeBUS standardize = new standardizeBUS();
    	
    	removeStopWordsBUS removeStopWords = new removeStopWordsBUS();
    	
    	taggingBUS tagging = new taggingBUS();
    	
//    	sentimentAnalyser model = new sentimentAnalyser();
    	
/*
 * 
 * GIAI DOAN: TIEN XU LI
 * 
 */ 	
    	/*
		 * STANDARDIZE DATA
		 */
    	standardize.standardizeData(spark);
  
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
    }
}
