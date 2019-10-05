package standardize;

import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import util.sparkConfigure;
import vn.uit.edu.sa.define.Constant;

public class standardizeDAO {
	private static String socialLanguageDictionaryName = null;
	private String[] inputFileName = null;
	
	//	get input file
	public String[] input() {
		int length = Constant.numOfProjectInputFile;

		this.inputFileName = new String[length];
		String inputFile = Constant.inputFileName;
		
		// checking if there is no input file then exit app
		if (this.inputFileName.length <= 0) {
			System.out.println("No files provided.");
			System.exit(0);
		}
		// set data for file name elements
		else {
			for (int i = 0; i < this.inputFileName.length; i++) {
				this.inputFileName[i] = "input" + "/" + inputFile;
			}
		}
		
		return this.inputFileName;
	}
	
	/*
	 * read Social Language Dictionary
	 */
	public static DataFrame readSocialDictionary(sparkConfigure spark) {
		socialLanguageDictionaryName = "resource/SocialLanguageDictionary.json";
		
		SQLContext sqlContext = new SQLContext(spark.getSparkContext());
		
		DataFrame dictionary  = sqlContext.read().json(socialLanguageDictionaryName);
	
		return dictionary;
	}
}
