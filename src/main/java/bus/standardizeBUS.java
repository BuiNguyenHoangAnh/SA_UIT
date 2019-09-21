package bus;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import dto.standardizeDTO;
import util.sparkConfigure;

public class standardizeBUS {
	private static DataFrame socialWordSet;
	
	private standardizeDTO correctionDto = new standardizeDTO();
	
	public void standardizeData(sparkConfigure spark) {
		JavaRDD<String> input;
		JavaRDD<String> result;
		
		String inputString = null;
		String outputString = null;
		
		for (int i = 0; i < this.correctionDto.getInputLength(); i++) {
			input = spark.getSparkContext().textFile(this.correctionDto.getInput()[i]);
		
			inputString = this.pushDataFromFileToString(input);
			
			outputString = this.standardize(inputString, spark);
			
			result = this.writeStringToFile(spark, outputString);
			
			result.saveAsTextFile("Standardize" + (i + 1));
		}
		
		spark.getSparkContext();
	}
	
	private String standardize(String string, sparkConfigure spark) {
		socialWordSet = correctionDto.getSocialLanguageDictionary(spark);
		
		String result = "";
		String[] words = string.split("\\s+");
		for(String word : words) {
			if(word.isEmpty()) continue;
			if(this.isSocialLanguage(word, spark) > 0) {
				//remove teen code, incorrect words, ...
				DataFrame correctWords =  socialWordSet.select("correct");
				Row[] correctRows = correctWords.collect();
				
				int count = this.isSocialLanguage(word, spark);
				
				for (Row row : correctRows) {
					count -= 1;
					if(count == 0) 
						result += (row.toString().substring(1, row.toString().length() - 1)+" ");
				}
				continue;
			}
			result += (word+" ");
		}
		return result;
	}
	
	private int isSocialLanguage(String word, sparkConfigure spark) {
		int count = 0;

		DataFrame incorrectWords =  socialWordSet.select("incorrect");
		Row[] incorrectRows = incorrectWords.collect();
		for (Row row : incorrectRows) {
			count += 1;
			if(row.toString().contains(word)) 
				return count;
		}

		return 0;
	}
	
	/*
	 * helper function
	 */

	//read data from file and push it to a string
		private String pushDataFromFileToString(JavaRDD<String> inputFile) {
			String inputString = null;
			for(String line:inputFile.collect()){
		//        System.out.println(line);
		        inputString = inputString + " " + line;
		    }
			return inputString;
		}

	//write a string to output file
		private JavaRDD<String> writeStringToFile(sparkConfigure spark, String outputString) {
			List<String> list;
		
			list = Arrays.asList(outputString);
			JavaRDD<String> result = spark.getSparkContext().parallelize(list); 
			return result;
		}
}
