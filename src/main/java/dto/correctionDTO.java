package dto;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Dataset;

import dao.correctionDAO;

import util.sparkConfigure;
import util.util.Bean;

public class correctionDTO {
	private correctionDAO correctionDao = new correctionDAO();
	
	public JavaRDD<String> getDictionary(sparkConfigure spark) {
		return this.correctionDao.dictionaryFile(spark);
	}
	
	public int getInputLength() {
		return this.correctionDao.inputFiles().length;
	}
	
	public String[] getInputFiles() {
		return this.correctionDao.inputFiles();
	}
	
	public String[] getInput() {
		return this.correctionDao.input();
	}

	public DataFrame getSocialLanguageDictionary(sparkConfigure spark) {
		return this.correctionDao.readSocialDictionary(spark);
	}
}
