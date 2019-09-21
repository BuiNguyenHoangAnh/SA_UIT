package standardize;

import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

import util.sparkConfigure;

public class standardizeDAO {
	private String socialLanguageDictionaryName = null;

	private String[] inputFileName = null;
	
	//	get input file
	public String[] input() {
		int length = 2;
		this.inputFileName = new String[length];
		
		// checking if there is no input file then exit app
		if (this.inputFileName.length <= 0) {
			System.out.println("No files provided.");
			System.exit(0);
		}
		// set data for file name elements
		else {
			for (int i = 0; i < this.inputFileName.length; i++) {
				this.inputFileName[i] = "sample.txt";
			}
		}
		
		return this.inputFileName;
	}
	
	/*
	 * read Social Language Dictionary
	 */
	public DataFrame readSocialDictionary(sparkConfigure spark) {
		this.socialLanguageDictionaryName = "resource/SocialLanguageDictionary.json";
		
		SQLContext sqlContext = new SQLContext(spark.getSparkContext());
		
		DataFrame dictionary = sqlContext.read().json(this.socialLanguageDictionaryName);
		
		return dictionary;
	}
}
