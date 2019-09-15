package bus;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.spark.api.java.JavaRDD;

import dto.removeStopWordsDTO;
import util.sparkConfigure;

public class removeStopWordsBUS {
/*
 * 
 * declare variables
 * 
 */
	private static Set<String> stopWordSet;
	
	private removeStopWordsDTO correctionDto = new removeStopWordsDTO();
	
/*
 * 
 * remove stop word
 * 
 */
	public void correctData(sparkConfigure spark) throws IOException {
		JavaRDD<String> inputFile;
		JavaRDD<String> result;
		
		String inputString = null;
		String outputString = null;
		
		stopWordSet = this.createListFromDictionary(spark);

		for (int i = 0; i < this.correctionDto.getInputLength(); i++) {
			inputFile = spark.getSparkContext().textFile(this.correctionDto.getInputFiles()[i]);
		
			inputString = this.pushDataFromFileToString(inputFile);
			
			outputString = this.removeStopWords(inputString);
			
			result = this.writeStringToFile(spark, outputString);
			
			result.saveAsTextFile("RemoveStopWord" + (i + 1));
		}
	}

	// read each line in file and push words into a set
	@SuppressWarnings("unchecked")
	private Set<String> createListFromDictionary(sparkConfigure spark) throws IOException {
		Set<String> bufferSet = new HashSet<String>();
		JavaRDD<String> inputFile = this.correctionDto.getDictionary(spark);
		
		bufferSet = this.pushDataFromFileToSet(inputFile);
		
		return bufferSet;
	}
	
	@SuppressWarnings("rawtypes")
	private Set pushDataFromFileToSet(JavaRDD<String> inputFile) throws IOException {
		Set<String> set = new HashSet<String>();
		for(String line:inputFile.collect()){
//            System.out.println(line);
			set.add(line);
		} 
		return set;
	}
	
	// check if word was a stop word
	private boolean isStopWord(String word) {
		if(word.length() < 2)
			return true;
		if(word.charAt(0) >= '0' && word.charAt(0) <= '9')
			return true;
		if(stopWordSet.contains(word))
			return true;
		return false;
	}
	
	private String removeStopWords(String string) {
		String result = "";
		String[] words = string.split("\\s+");
		for(String word : words) {
			if(word.isEmpty()) continue;
			if(this.isStopWord(word)) continue; //remove stopwords
			result += (word+" ");
		}
		return result;
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
