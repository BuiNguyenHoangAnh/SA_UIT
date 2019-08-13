package util;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class sparkConfigure {
	private SparkConf sparkConf;
	private JavaSparkContext sparkContext;

	public sparkConfigure() {
		this.sparkConf = new SparkConf().setMaster("local").setAppName("SA-UIT");
		this.sparkContext = new JavaSparkContext(sparkConf);
	}
	public SparkConf getSparkConf() {
		return this.sparkConf;
	}
	
	public JavaSparkContext getSparkContext() {
		return this.sparkContext;
	}
}
