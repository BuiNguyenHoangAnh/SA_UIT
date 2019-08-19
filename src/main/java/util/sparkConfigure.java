package util;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import vn.vitk.util.SparkContextFactory;

public class sparkConfigure {
	private SparkConf sparkConf = new SparkConf().setMaster("local").set("spark.driver.allowMultipleContexts", "true").setAppName("SA-UIT");
	private JavaSparkContext sparkContext;

	public SparkConf getSparkConf() {
		return this.sparkConf;
	}

	public JavaSparkContext getSparkContext() {
		if (this.sparkContext == null) {
			synchronized (SparkContextFactory.class) {
				if (this.sparkContext == null) {
					this.sparkContext = new JavaSparkContext(this.sparkConf);
				}
			}
		}
		return this.sparkContext;
	}
	
	public void closeJavaSparkContext() {
		this.sparkContext.close();
	}
}
