package dao;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.spark.api.java.JavaRDD;

import util.sparkConfigure;

public class correctionDAO {
	private JavaRDD<String> inputFile;

//	get input file
	public JavaRDD<String> getInputFile(sparkConfigure spark, String fileName) {
		inputFile = spark.getSparkContext().textFile(fileName);
		return inputFile;
	}
	
//	write a string to output file
	public JavaRDD<String> writeStringToOutputFile(sparkConfigure spark, String outputString) {
		List<String> list;

		list = Arrays.asList(outputString);
		return spark.getSparkContext().parallelize(list);
	}
	
//	read data from file and push it to a string
	public String pushDataFromFileToString(JavaRDD<String> inputFile) {
		String inputString = null;
		for(String line:inputFile.collect()){
//            System.out.println(line);
            inputString = inputString + " " + line;
        }
		return inputString;
	}
	
//	read each line from file and push them to a set
	@SuppressWarnings("rawtypes")
	public Set pushDataFromFileToSet(JavaRDD<String> inputFile) {
		Set<String> set = new HashSet<String>();
		for(String line:inputFile.collect()){
//            System.out.println(line);
			set.add(line);
		} 
		return set;
	}
}
