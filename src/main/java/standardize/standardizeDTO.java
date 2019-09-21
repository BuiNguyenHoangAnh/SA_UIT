package standardize;

import org.apache.spark.sql.DataFrame;

import util.sparkConfigure;

public class standardizeDTO {
	private standardizeDAO correctionDao = new standardizeDAO();

	public String[] getInput() {
		return this.correctionDao.input();
	}
	
	public int getInputLength() {
		return this.correctionDao.input().length;
	}

	public DataFrame getSocialLanguageDictionary(sparkConfigure spark) {
		return this.correctionDao.readSocialDictionary(spark);
	}
}
