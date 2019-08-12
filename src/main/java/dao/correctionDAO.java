package dao;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class correctionDAO {
	private SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("Word Segmentation");
	private JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
	
	private JavaRDD<String> inputFile;

//	get input file
	public JavaRDD<String> getInputFile(String fileName) {
		inputFile = sparkContext.textFile(fileName);
		return inputFile;
	}
	
//	write a string to output file
	public JavaRDD<String> writeStringToOutputFile(String outputString) {
		List<String> list;

		list = Arrays.asList(outputString);
		return sparkContext.parallelize(list);
	}
}
