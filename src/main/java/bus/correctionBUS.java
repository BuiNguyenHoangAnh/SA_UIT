package bus;

import java.util.HashSet;
import java.util.Set;

import org.apache.spark.api.java.JavaRDD;

import dao.correctionDAO;
import util.sparkConfigure;

public class correctionBUS {
/*
 * 
 * declare variables
 * 
 */
	private static Set<String> stopWordSet;;
	
	private correctionDAO correctionDao = new correctionDAO();
	
/*
 * 
 * remove stop word
 * 
 */

	// read each line in file and push words into an array
	@SuppressWarnings("unchecked")
	public Set<String> createListFromDictionary(sparkConfigure spark, String fileName) {
		if(fileName != "" || fileName != null) {
			Set<String> bufferSet = new HashSet<String>();
			JavaRDD<String> inputFile = correctionDao.getInputFile(spark, fileName);
			
			bufferSet = correctionDao.pushDataFromFileToSet(inputFile);
			
			return bufferSet;
		}
		
		return null;
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
	
	public void checkInputFile(sparkConfigure spark, String inputFileName, String dictionaryFileName) {;
		JavaRDD<String> inputFile = correctionDao.getInputFile(spark, inputFileName);
		
		JavaRDD<String> result;
		
		String inputString = null;
		String outputString = null;
		
		stopWordSet = createListFromDictionary(spark, dictionaryFileName);
		
		inputString = correctionDao.pushDataFromFileToString(inputFile);
		
		outputString = removeStopWords(inputString);
		result = correctionDao.writeStringToOutputFile(spark, outputString);

		result.saveAsTextFile("RemoveStopWord");
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
