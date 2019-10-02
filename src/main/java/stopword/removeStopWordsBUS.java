package stopword;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.spark.api.java.JavaRDD;

import util.helpFunction;
import util.sparkConfigure;
import vn.uit.edu.sa.define.Constant;

public class removeStopWordsBUS {
/*
 * 
 * declare variables
 * 
 */
	private static Set<String> stopWordSet;
	
	private removeStopWordsDTO correctionDto = new removeStopWordsDTO();
	private helpFunction helpFunc = new helpFunction();
	
/*
 * 
 * remove stop word
 * 
 */
	public void correctData(sparkConfigure spark) throws IOException {
		JavaRDD<String> inputFile;
		JavaRDD<String> result;
		JavaRDD<String> output;
		
		String inputString = null;
		String outputString = null;
		
		stopWordSet = this.createListFromDictionary(spark);

		for (int i = 0; i < this.correctionDto.getInputLength(); i++) {
			inputFile = spark.getSparkContext().textFile(Constant.projectOutputDir + "/Segmentation/" + this.correctionDto.getInputFiles().get(i));
		
			inputString = this.helpFunc.pushDataFromFileToString(inputFile);
			
			outputString = this.removeStopWords(inputString);
			
			result = this.helpFunc.writeStringToFile(spark, outputString);
			output = result.filter(x -> x.length() > 1 || !(x.equals("") && x.length() == 0));
			output.saveAsTextFile(Constant.projectOutputDir + "/StopWords");
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
		//System.out.println(string);
		String[] words = string.split(" ");
		//System.out.println(Arrays.toString(words));
		
		
		  for(String word : words) { 
			  if(word.isEmpty()) continue;
			  if (word.equals("\n")) {
			  }
			  else if(this.isStopWord(word)) continue; //remove stopwords 
			 
			  result += (word+" "); 
		  }
		 
		System.out.println(result);
		return result;
	}
}
