package com.journaldev.sparkdemo;

import java.util.Arrays;

import org.apache.commons.cli.Options;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

import vn.vitk.tok.Tokenizer;

public class Segmentation {
	@SuppressWarnings({ "resource", "rawtypes", "unchecked" })
	public void wordSegmentation(String fileName) {
//		String dataFolder = "/export/dat/tok";
		String dataFolder = "/home/buinguyenhoanganh/Desktop/vn.vitk/dat/tok";
		String master = "local[*]";
		String inputFileName = fileName;
		String outputDirectory = "SegmentData";

//		SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("JD Word Counter");
//		JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
		
//		JavaRDD<String> inputFile = sparkContext.textFile(fileName);
//		JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
//		JavaPairRDD segmentData = wordsFromFile.mapToPair(t -> new Tuple2(t, 1)).reduceByKey((x, y) -> (int) x + (int) y);
		Tokenizer tokenizer = null;
		tokenizer = new Tokenizer(master, dataFolder + "/lexicon.xml", dataFolder + "/regexp.txt", dataFolder + "/whitespace.model", true);
//		JavaRDD<String> segmentData = tokenizer.tokenize(wordsFromFile);
		tokenizer.tokenize(inputFileName, outputDirectory);
		
//		segmentData.saveAsTextFile("SegmentData");
    }
}
