package com.journaldev.sparkdemo;

import bus.correctionBUS;
import bus.segmentationBUS;

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
    	
/*
 * 
 * GIAI DOAN: TIEN XU LI
 * 
 */
		/*
		 * TOKENIZER/ SEGMENTATION
		 */
//    	segmentation.wordSegmentation(spark);
    	
		/*
		 * REMOVE STOP WORD
		 */
    	correction.correctInputFile(spark);
    }
}
