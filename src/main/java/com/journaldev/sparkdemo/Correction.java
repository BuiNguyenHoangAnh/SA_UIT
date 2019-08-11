package com.journaldev.sparkdemo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.lang.model.util.Elements;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

public class Correction {
/*
 * 
 * declare variables
 * 
 */
	private static Set<String> stopWordSet;
	
/*
 * 
 * loai bo stop word
 * 
 */

	// read each line in file and push words into an array
	public static Set<String> createListFromDictionary(String fileName) {
		if(fileName != "" || fileName != null) {
			Set<String> bufferSet = null;
			SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("Word Segmentation");
			JavaSparkContext context = new JavaSparkContext(sparkConf);
			JavaRDD<String> textFile = context.textFile(fileName);
			
			for(String line:textFile.collect()){
	            System.out.println(line);
	            bufferSet.add(line);
	        } 
			
			return bufferSet;
		}
		
		return null;
	}
	
	// check if word was a stop word
	public static boolean isStopWord(String word) {
		if(word.length() < 2)
			return true;
		if(word.charAt(0) >= '0' && word.charAt(0) <= '9')
			return true;
		if(stopWordSet.contains(word))
			return true;
		return false;
	}
	
	public static String removeStopWords(String string) {
		String result = "";
		String[] words = string.split("\\s+");
		for(String word : words) {
			if(word.isEmpty()) continue;
			if(isStopWord(string)) continue; //remove stopwords
			result += (word+" ");
		}
		return result;
	}
	
	public static void checkInputFile(String inputFileName, String dictionaryFileName) {
		SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("JD Word Counter");
		JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
		JavaRDD<String> inputFile = sparkContext.textFile(inputFileName);
		
		List<String> list;
		JavaRDD<String> result;
		
		String inputString = null;
		String outputString = null;
		
		stopWordSet = createListFromDictionary(dictionaryFileName);
		
		for(String line:inputFile.collect()){
            System.out.println(line);
            inputString = inputString + " " + line;
        }
		
		outputString = removeStopWords(inputString);
		list = Arrays.asList(outputString);
		result = sparkContext.parallelize(list);

		result.saveAsTextFile("CountData");
	}
	
/*
 * 
 * xu li tu viet tat
 * 
 */
	
/*
 * 
 * xu li tu sai chinh ta
 * 
 */
	 
/*
 * 
 * xu li tieng long/ vung mien
 * 
 */
	
}
