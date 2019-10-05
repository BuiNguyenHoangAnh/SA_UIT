package stopword;

import java.util.ArrayList;

import org.apache.spark.api.java.JavaRDD;

import util.sparkConfigure;

public class removeStopWordsDTO {
	private removeStopWordsDAO correctionDao = new removeStopWordsDAO();
	
	public JavaRDD<String> getDictionary(sparkConfigure spark) {
		return this.correctionDao.dictionaryFile(spark);
	}
	
	public int getInputLength() {
		return this.correctionDao.inputFiles().size();
	}
	
	public ArrayList<String> getInputFiles() {
		return this.correctionDao.inputFiles();
	}
}
