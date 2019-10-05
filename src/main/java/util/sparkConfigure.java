package util;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import vn.uit.edu.sa.connectDB.MongoSparkHelper;
import vn.uit.edu.sa.define.Constant;
import vn.vitk.util.SparkContextFactory;

public class sparkConfigure {
	private static JavaSparkContext sparkContext = null;

	public JavaSparkContext getSparkContext() {
		sparkContext = SparkContextFactory.create();
		
		return sparkContext;
	}
	
//	public JavaSparkContext getRemoteSparkContext() {
//		this.sparkContext = SparkContextFactory.createRemoteConnection();
//		
//		return this.sparkContext;
//	}
	
	
}
