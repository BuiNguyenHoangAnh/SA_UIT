package util;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class sparkConfigure {
	public SparkConf getSparkConf() {
		return new SparkConf().setMaster("local").set("spark.driver.allowMultipleContexts", "true").setAppName("SA-UIT");
	}
	
	public JavaSparkContext getSparkContext() {
		return new JavaSparkContext(this.getSparkConf());
	}
}
