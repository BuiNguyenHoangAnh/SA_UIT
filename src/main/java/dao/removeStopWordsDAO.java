package dao;

import org.apache.spark.api.java.JavaRDD;

import util.sparkConfigure;

public class removeStopWordsDAO {
	private String stopWordsDictionaryFileName = null;
	private String[] inputFileName = null;
	
//	get stop words dictionary
	public JavaRDD<String> dictionaryFile(sparkConfigure spark) {
		this.stopWordsDictionaryFileName = "stopword_dictionary.txt";

		if (this.stopWordsDictionaryFileName != "" || this.stopWordsDictionaryFileName != null) {
			JavaRDD<String> dictionaryFile = spark.getSparkContext().textFile(this.stopWordsDictionaryFileName);
			return dictionaryFile;
		}
		return null;
	}
	
//	get input
	public String[] inputFiles() {
		int length = 2;
		this.inputFileName = new String[length];
		
		// checking if there is no input file then exit app
		if (this.inputFileName.length <= 0) {
			System.out.println("No files provided.");
			System.exit(0);
		}
		// set data for file name elements
		else {
			for (int i = 0; i < this.inputFileName.length; i++) {
				this.inputFileName[i] = "stopword_file.txt";
			}
		}
		
		return this.inputFileName;
	}
}
