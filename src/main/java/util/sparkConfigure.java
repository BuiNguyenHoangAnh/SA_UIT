package util;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import vn.uit.edu.sa.connectDB.MongoSparkHelper;
import vn.uit.edu.sa.define.Constant;
import vn.vitk.util.SparkContextFactory;

public class sparkConfigure {
	private static SparkConf sparkConf = new SparkConf().setMaster("local").set("spark.driver.allowMultipleContexts", "true").setAppName("SA-UIT").set(Constant.inputURI, Constant.mongoURI).set(Constant.outputURI, Constant.mongoURI);
	private static JavaSparkContext sparkContext = null;

	public SparkConf getSparkConf() {
		return this.sparkConf;
	}

	public JavaSparkContext getSparkContext() {
//		if (this.sparkContext == null) {
//			synchronized (SparkContextFactory.class) {
//				if (this.sparkContext == null) {
//					this.sparkContext = new JavaSparkContext(this.sparkConf);
//				}
//			}
//		}
		this.sparkContext = SparkContextFactory.create();
		
		return this.sparkContext;
	}
	
	public JavaSparkContext getRemoteSparkContext() {
//		if (this.sparkContext == null) {
//			synchronized (SparkContextFactory.class) {
//				if (this.sparkContext == null) {
//					this.sparkContext = new JavaSparkContext(this.sparkConf);
//				}
//			}
//		}
		this.sparkContext = SparkContextFactory.createRemoteConnection();
		
		return this.sparkContext;
	}
	
	public void closeJavaSparkContext() {
		this.sparkContext.close();
	}
	
}
