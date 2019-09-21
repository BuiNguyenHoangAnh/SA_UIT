package util;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

public class helpFunction {
	//read data from file and push it to a string
		public String pushDataFromFileToString(JavaRDD<String> inputFile) {
			String inputString = null;
			for(String line:inputFile.collect()){
		//        System.out.println(line);
		        inputString = inputString + " " + line;
		    }
			return inputString;
		}

	//write a string to output file
		public JavaRDD<String> writeStringToFile(sparkConfigure spark, String outputString) {
			List<String> list;
		
			list = Arrays.asList(outputString);
			JavaRDD<String> result = spark.getSparkContext().parallelize(list); 
			return result;
		}
}
