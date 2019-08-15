package bus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.spark.api.java.JavaRDD;

import dto.correctionDTO;
import util.sparkConfigure;

public class correctionBUS {
/*
 * 
 * declare variables
 * 
 */
	private static Set<String> stopWordSet;;

	private correctionDTO correctionDto = new correctionDTO();
	
/*
 * 
 * remove stop word
 * 
 */
	public void correctInputFile(sparkConfigure spark) {
		JavaRDD<String> inputFile;
		JavaRDD<String> result;
		
		String inputString = null;
		String outputString = null;
		
		stopWordSet = this.createListFromDictionary(spark);

		for (int i = 0; i < this.correctionDto.getInputLength(); i++) {
			inputFile = spark.getSparkContext().textFile(this.correctionDto.getInputFiles()[i]);
		
			inputString = this.pushDataFromFileToString(inputFile);
			
			outputString = removeStopWords(inputString);
			
			result = this.writeStringToFile(spark, outputString);
			
			result.saveAsTextFile("RemoveStopWord");
		}
	}

	// read each line in file and push words into a set
	@SuppressWarnings("unchecked")
	public Set<String> createListFromDictionary(sparkConfigure spark) {
		Set<String> bufferSet = new HashSet<String>();
		JavaRDD<String> inputFile = this.correctionDto.getDictionary(spark);
		
		bufferSet = this.pushDataFromFileToSet(inputFile);
		
		return bufferSet;
	}
	
	@SuppressWarnings("rawtypes")
	public Set pushDataFromFileToSet(JavaRDD<String> inputFile) {
		Set<String> set = new HashSet<String>();
		for(String line:inputFile.collect()){
//            System.out.println(line);
			set.add(line);
		} 
		return set;
	}
	
	// check if word was a stop word
	public static boolean isStopWord(String word) {
		if(word.length() < 2)
			return true;
		if(word.charAt(0) >= '0' && word.charAt(0) <= '9')
			return true;
		if(stopWordSet.contains(word))
			return true;
		return false;
	}
	
	public static String removeStopWords(String string) {
		String result = "";
		String[] words = string.split("\\s+");
		for(String word : words) {
			if(word.isEmpty()) continue;
			if(isStopWord(word)) continue; //remove stopwords
			result += (word+" ");
		}
		return result;
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
	
//	write a string to output file
	public JavaRDD<String> writeStringToFile(sparkConfigure spark, String outputString) {
		List<String> list;

		list = Arrays.asList(outputString);
		return spark.getSparkContext().parallelize(list);
	}
	
/*
 * 
 * xu li tu viet tat
 * 
 */
	
/*
 * 
 * xu li tu sai chinh ta
 * 
 */
	 
/*
 * 
 * xu li tieng long/ vung mien
 * 
 */

}
