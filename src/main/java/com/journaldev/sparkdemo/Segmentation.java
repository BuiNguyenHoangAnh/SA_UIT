package com.journaldev.sparkdemo;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.Options;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

import vn.vitk.tok.*;

public class Segmentation {
	@SuppressWarnings({ "resource", "rawtypes", "unchecked" })
	public void wordSegmentation(String fileName) {
		SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("Word Segmentation");
//		String dataFolder = "/export/dat/tok";
		String dataFolder = "/home/buinguyenhoanganh/Desktop/vn.vitk/dat/tok";
//		String master = "local[*]";
		String master = sparkConf.get("spark.master");	
		String inputFileName = fileName;
		String outputDirectory = "SegmentData";

		Tokenizer tokenizer = null;
		tokenizer = new Tokenizer(master, dataFolder + "/lexicon.xml", dataFolder + "/regexp.txt", dataFolder + "/whitespace.model", true);
		tokenizer.tokenize(inputFileName, outputDirectory);
    }
}
