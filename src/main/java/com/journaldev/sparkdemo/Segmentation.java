package com.journaldev.sparkdemo;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class Segmentation {
	@SuppressWarnings({ "resource", "rawtypes", "unchecked" })
	public static void wordSegmentation(String fileName) {
		SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("JD Word Counter");
		JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
		
		JavaRDD<String> inputFile = sparkContext.textFile(fileName);
		JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
		JavaPairRDD segmentData = wordsFromFile.mapToPair(t -> new Tuple2(t, 1)).reduceByKey((x, y) -> (int) x + (int) y);
		
		segmentData.saveAsTextFile("SegmentData");
    }
}
