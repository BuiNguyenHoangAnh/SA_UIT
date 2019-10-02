package standardize;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import util.helpFunction;
import util.sparkConfigure;

public class standardizeBUS {
	private static DataFrame socialWordSet;
	
	private DataFrame correctWords;
	private Row[] correctRows;
	
	private standardizeDTO correctionDto = new standardizeDTO();
	private helpFunction helpFunc = new helpFunction();
	
	private DataFrame incorrectWords;
	private Row[] incorrectRows;
	
	public void standardizeData(sparkConfigure spark) {
		JavaRDD<String> input;
		JavaRDD<String> result;
		
		String inputString = null;
		String outputString = null;
		
		for (int i = 0; i < this.correctionDto.getInputLength(); i++) {
			input = spark.getSparkContext().textFile(this.correctionDto.getInput()[i]);
		
			inputString = this.helpFunc.pushDataFromFileToString(input);
			
			outputString = this.standardize(inputString, spark);
			
			result = this.helpFunc.writeStringToFile(spark, outputString);
			
			result.saveAsTextFile("Standardize" + (i + 1));
		}
	}
	
	private String standardize(String string, sparkConfigure spark) {
		socialWordSet = correctionDto.getSocialLanguageDictionary(spark);
		
		this.correctWords =  socialWordSet.select("correct");
		this.correctRows = correctWords.collect();
		
		this.incorrectWords =  socialWordSet.select("incorrect");
		this.incorrectRows = incorrectWords.collect();
		
		String result = "";
		String[] words = string.split("\\s+");
		for(String word : words) {
			if(word.isEmpty()) continue;
			int count = this.isSocialLanguage(word, spark);
			if(count > 0) {
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
			if(row.toString().substring(1, row.toString().length() - 1).equals(word)) 
				return count;
		}

		return 0;
	}
}
