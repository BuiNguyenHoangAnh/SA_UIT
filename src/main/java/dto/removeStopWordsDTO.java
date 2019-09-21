package dto;

import org.apache.spark.api.java.JavaRDD;

import dao.removeStopWordsDAO;
import util.sparkConfigure;

public class removeStopWordsDTO {
	private removeStopWordsDAO correctionDao = new removeStopWordsDAO();
	
	public JavaRDD<String> getDictionary(sparkConfigure spark) {
		return this.correctionDao.dictionaryFile(spark);
	}
	
	public int getInputLength() {
		return this.correctionDao.inputFiles().length;
	}
	
	public String[] getInputFiles() {
		return this.correctionDao.inputFiles();
	}
}