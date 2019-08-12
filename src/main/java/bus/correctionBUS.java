package bus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import dao.correctionDAO;

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
 * loai bo stop word
 * 
 */

	// read each line in file and push words into an array
	public Set<String> createListFromDictionary(String fileName) {
		if(fileName != "" || fileName != null) {
			Set<String> bufferSet = new HashSet<String>();;
			JavaRDD<String> textFile = correctionDao.getInputFile(fileName);
			
			for(String line:textFile.collect()){
//		            System.out.println(line);
	            bufferSet.add(line);
	        } 
			
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
	
	public void checkInputFile(String inputFileName, String dictionaryFileName) {;
		JavaRDD<String> inputFile = correctionDao.getInputFile(inputFileName);
		
		JavaRDD<String> result;
		
		String inputString = null;
		String outputString = null;
		
		stopWordSet = createListFromDictionary(dictionaryFileName);
		
		for(String line:inputFile.collect()){
            System.out.println(line);
            inputString = inputString + " " + line;
        }
		
		outputString = removeStopWords(inputString);
		result = correctionDao.writeStringToOutputFile(outputString);

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
