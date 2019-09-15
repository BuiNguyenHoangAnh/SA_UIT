package com.journaldev.sparkdemo;

import java.io.IOException;

import bus.correctionBUS;
import bus.segmentationBUS;
import bus.taggingBUS;
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
    	
    	correctionBUS correction = new correctionBUS();
    	
    	taggingBUS tagging = new taggingBUS();
    	
//    	topicModelingBUS topicModelingBus = new topicModelingBUS();
    	
/*
 * 
 * GIAI DOAN: TIEN XU LI
 * 
 */
		/*
		 * STANDARDIZE DATA
		 */
    	correction.standardizeData(spark);
    	
		/*
		 * TOKENIZER/ SEGMENTATION
		 */
//    	segmentation.wordSegmentation(spark);
    	
		/*
		 * REMOVE STOP WORD
		 */
//    	try {
//			correction.correctData(spark);
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
