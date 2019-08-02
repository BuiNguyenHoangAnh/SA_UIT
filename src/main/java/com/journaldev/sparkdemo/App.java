package com.journaldev.sparkdemo;

import com.journaldev.sparkdemo.Segmentation;

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
//    	declare variables
    	Segmentation segmentation = new Segmentation();
    	int inputFilesLenght;
    	String[] fileName;
    	
//    	number of input files
    	inputFilesLenght = 10;
  
//    	checking if there is no input file then exit app
    	if (inputFilesLenght <= 0) {
    		System.out.println("No files provided.");
    		System.exit(0);
    	}
   
//    	create an array to get file name
    	fileName = new String[inputFilesLenght];
 
//    	set data for file name elements
    	fileName[0] = "input.txt";
  
//    	execute calculation
    	segmentation.wordSegmentation(fileName[0]);
    }
}
