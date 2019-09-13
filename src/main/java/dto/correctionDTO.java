package dto;

import org.apache.spark.api.java.JavaRDD;

import dao.correctionDAO;
import util.sparkConfigure;

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

	public Object getExcelFile() {
		return this.correctionDao.readSocialDictionary();
	}
}
