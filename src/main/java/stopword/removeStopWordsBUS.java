package stopword;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
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
			outputString = helpFunction.removeEmptyLine(outputString);
			result = this.helpFunc.writeStringToFile(spark, outputString);			
			result.saveAsTextFile(Constant.projectOutputDir + "/StopWords");
			helpFunction.removeUnusedFile("StopWords");
			try {
				System.out.println("checkout");
				new File(Constant.projectOutputDir + "/GibbsLDA").mkdirs();
				FileUtils.copyFile(new File(Constant.projectOutputDir + "/StopWords/" + helpFunction.getFileName("StopWords")), new File(Constant.projectOutputDir + "/GibbsLDA/GibbsLDA"));
			}catch (Exception e) {e.printStackTrace();}
			
			helpFunction.addNumberToDocument(Long.toString(helpFunction.convertToListAndCount(outputString)), Constant.projectOutputDir + "/GibbsLDA/" + helpFunction.getFileName("GibbsLDA"));
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
		 
		//System.out.println(result);
		return result;
	}

	public void correctData(sparkConfigure spark, String handleString) throws IOException{
		JavaRDD<String> inputFile;
		JavaRDD<String> result;
		JavaRDD<String> output;
		
		String outputString = null;
		
		stopWordSet = this.createListFromDictionary(spark);
		
		outputString = this.removeStopWords(handleString);
		outputString = helpFunction.removeEmptyLine(outputString);
		
		result = this.helpFunc.writeStringToFile(spark, outputString);			
		result.saveAsTextFile(Constant.projectOutputDir + "/StopWords");
		helpFunction.removeUnusedFile("StopWords");
		try {
			new File(Constant.projectOutputDir + "/GibbsLDA").mkdirs();
			FileUtils.copyFile(new File(Constant.projectOutputDir + "/StopWords/" + helpFunction.getFileName("StopWords")), new File(Constant.projectOutputDir + "/GibbsLDA/GibbsLDA"));
		}catch (Exception e) {e.printStackTrace();}
			
			helpFunction.addNumberToDocument(Long.toString(helpFunction.convertToListAndCount(outputString)), Constant.projectOutputDir + "/GibbsLDA/" + helpFunction.getFileName("GibbsLDA"));		
	}
}
