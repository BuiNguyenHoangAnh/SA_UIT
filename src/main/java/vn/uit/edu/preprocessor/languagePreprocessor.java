package vn.uit.edu.preprocessor;

import java.io.IOException;

import org.apache.spark.SparkContext;

import segmentation.segmentationBUS;
import standardize.standardizeBUS;
import stopword.removeStopWordsBUS;
import util.sparkConfigure;
import vn.uit.edu.sa.define.Constant;

public class languagePreprocessor {

	private String fileName = null;
	private standardizeBUS standardizer = null;
	private segmentationBUS segmentation = null;
	private removeStopWordsBUS removeStopWords = null;
	private sparkConfigure spark = null;
	private String handleString = null;
	
	public languagePreprocessor(sparkConfigure spark) {
		this.spark = spark;
		initialize();
	}
	
	public languagePreprocessor(sparkConfigure spark, String fileName) {
		this.fileName = fileName;
		this.spark = spark;
		initialize();
	}
	
	private void initialize() {
		standardizer = new standardizeBUS();
		segmentation = new segmentationBUS();
		removeStopWords = new removeStopWordsBUS();	
	}
		
	
	public void run(String _fileName) {
		
		if (_fileName == null)
			this.fileName = Constant.projectInputFolder + "/data"; //default input
		else this.fileName =  _fileName; //user input
		
		handleString = standardizer.standarizeData(spark, fileName);
		System.out.println(handleString);
		handleString = segmentation.wordSegmentation(spark, handleString);
		System.out.println(handleString);
		try {
			removeStopWords.correctData(this.spark, handleString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
