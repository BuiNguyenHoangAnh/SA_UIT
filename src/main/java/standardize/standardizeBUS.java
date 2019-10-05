package standardize;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import util.helpFunction;
import util.sparkConfigure;
import vn.uit.edu.sa.define.Constant;

public class standardizeBUS {
	private static DataFrame socialWordSet;
	private static DataFrame incorrectWords;
	private static DataFrame correctWords;
	private static Row[] correctRows;
	private static Row[] incorrectRows;
	
	private standardizeDTO correctionDto = new standardizeDTO();
	private helpFunction helpFunc = new helpFunction();
	
	public void standardizeData(sparkConfigure spark) {
		JavaRDD<String> input;
		JavaRDD<String> result;
		
		String inputString = null;
		String outputString = null;
		
		/*
		 * for (int i = 0; i < this.correctionDto.getInputLength(); i++) { input =
		 * spark.getSparkContext().textFile(this.correctionDto.getInput()[i]);
		 * 
		 * inputString = this.helpFunc.pushDataFromFileToString(input);
		 * 
		 * outputString = this.standardize(inputString, spark);
		 * 
		 * result = this.helpFunc.writeStringToFile(spark, outputString);
		 * 
		 * result.saveAsTextFile("Standardize" + (i + 1)); }
		 */
		
		input = spark.getSparkContext().textFile(this.correctionDto.getInput()[0]);
		inputString = helpFunction.pushDataFromFileToString(input);
		outputString = this.standardize(inputString, spark);
		result = this.helpFunc.writeStringToFile(spark, outputString);
		result.repartition(1).saveAsTextFile(Constant.outputStandardizedDir);
	}
	
	public String standarizeData(sparkConfigure spark, String fileName){ 
		
		JavaRDD<String> input;
		
		String inputString = null;
		String result = null;
		
		input = spark.getSparkContext().textFile(fileName);
		inputString = helpFunction.pushDataFromFileToString(input);
		result = this.standardize(inputString, spark);
		
		return result;
	}
	

	private String standardize(String string, sparkConfigure spark) {
		socialWordSet = correctionDto.getSocialLanguageDictionary(spark);
		
		this.correctWords =  socialWordSet.select("correct");
		this.correctRows = correctWords.collect();
		
		this.incorrectWords =  socialWordSet.select("incorrect");
		this.incorrectRows = incorrectWords.collect();
		
		String result = "";
		String[] words = string.split(" ");
		
		correctWords = socialWordSet.select("correct");
		incorrectWords = socialWordSet.select("incorrect");
		
		correctRows = correctWords.collect();
		incorrectRows = incorrectWords.collect();

		
		for(String word : words) {
			if(word.isEmpty()) continue;
			int count = this.isSocialLanguage(word, spark);
			if (word == "\n") {
				result += "\n";
			}
			else if (count > 0) {
				//remove teen code, incorrect words, ...				
				result += (correctRows[count-1].toString().substring(1, correctRows[count-1].toString().length() - 1)+" ");
				continue;
			}
			result += (word+" ");
		}
		
		return result;
	}
	
	private int isSocialLanguage(String word, sparkConfigure spark) {
		int count = 0;
		for (Row row : incorrectRows) {
			count += 1;
			if(row.toString().substring(1, row.toString().length()-1).equals(word.toLowerCase())) 
				return count;
		}
		return 0;
	}	
}
