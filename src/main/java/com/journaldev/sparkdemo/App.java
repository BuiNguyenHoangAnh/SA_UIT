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
/*
 * 
 * declare variables
 * 
 */
    	Segmentation segmentation = new Segmentation();
    	int inputFilesLenght;
    	String[] fileName;
    	
    	Correction correction = new Correction();
    	String dictionaryFileName;
    	String inputFileName;
    	
/*
 * 
 * GIAI DOAN: TIEN XU LI
 * 
 */
		/*
		 * TOKENIZER/ SEGMENTATION
		 */
//    	// number of input files
//    	inputFilesLenght = 10;
//    	// create an array to get file name
//    	fileName = new String[inputFilesLenght];
//    	// set data for file name elements
//    	for (int i = 0; i < inputFilesLenght; i++) {
//    		if(i == 0)
//    			fileName[i] = "input.txt";
//    		fileName[i] = "";
//    	}
//    	
//    	// checking if there is no input file then exit app
//    	if (inputFilesLenght <= 0) {
//    		System.out.println("No files provided.");
//    		System.exit(0);
//    	} 
//    	// tokenizer and write result to output file
//    	segmentation.wordSegmentation(fileName[0]);
    	
		/*
		 * REMOVE STOP WORD
		 */
    	dictionaryFileName = "stopword_dictionary.txt";
    	inputFileName = "stopword_file.txt";
    	correction.checkInputFile(inputFileName, dictionaryFileName);
    }
}
