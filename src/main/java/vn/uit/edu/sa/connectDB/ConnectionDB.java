package vn.uit.edu.sa.connectDB;

import org.apache.spark.api.java.JavaSparkContext;

import vn.uit.edu.sa.define.*;

public class ConnectionDB {

	public ConnectionDB() {
		
	}
	
	public static void initConnection(/*SparkSession sparkSession*/) {
		/*
		 * sparkSession = SparkSession.builder().master("local")
		 * .appName(Constant.appName)
		 * .config("spark.mongodb.input.uri",Constant.mongoURI)
		 * .config("spark.mongodb.out.uri",Constant.mongoURI) .getOrCreate(); return new
		 * JavaSparkContext(sparkSession.sparkContext());
		 */
	}
	
	public static Boolean closeConnection(JavaSparkContext jSparkContext) {
		try {
			jSparkContext.close();
			return true;
		}catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
}
