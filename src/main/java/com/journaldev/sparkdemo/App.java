package com.journaldev.sparkdemo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * 
 * SENTIMENT ANALYSIS
 * Application will read a file then execute word segmentation
 * 
 */
public class App 
{
	@SuppressWarnings({ "resource", "rawtypes", "unchecked" })
	private static void wordCount(String fileName) {
		SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("JD Word Counter");
		JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
		
		JavaRDD<String> inputFile = sparkContext.textFile(fileName);
		JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
		JavaPairRDD segmentData = wordsFromFile.mapToPair(t -> new Tuple2(t, 1)).reduceByKey((x, y) -> (int) x + (int) y);
		
		segmentData.saveAsTextFile("SegmentData");
    }
	
    public static void main( String[] args )
    {
//    	number of input files
    	int inputFilesLenght = 10;
  
//    	checking if there is no input file then exit app
    	if (inputFilesLenght <= 0) {
    		System.out.println("No files provided.");
    		System.exit(0);
    	}
   
//    	create an array to get file name
    	String[] fileName = new String[inputFilesLenght];
 
//    	set data for file name elements
    	fileName[0] = "input.txt";
  
//    	execute calculation
    	wordCount(fileName[0]);
    }
}
