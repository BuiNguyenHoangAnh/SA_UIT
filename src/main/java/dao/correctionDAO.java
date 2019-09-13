package dao;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.SparkSession;

import util.sparkConfigure;

public class correctionDAO {
	private String dictionaryFileName = null;
	private String[] inputFileName = null;
	
//	get stop words dictionary
	public JavaRDD<String> dictionaryFile(sparkConfigure spark) {
		this.dictionaryFileName = "stopword_dictionary.txt";

		if (this.dictionaryFileName != "" || this.dictionaryFileName != null) {
			JavaRDD<String> dictionaryFile = spark.getSparkContext().textFile(this.dictionaryFileName);
			return dictionaryFile;
		}
		return null;
	}

//	get input file
	public String[] inputFiles() {
		int length = 2;
		this.inputFileName = new String[length];
		
		// checking if there is no input file then exit app
		if (this.inputFileName.length <= 0) {
			System.out.println("No files provided.");
			System.exit(0);
		}
		// set data for file name elements
		else {
			for (int i = 0; i < this.inputFileName.length; i++) {
				this.inputFileName[i] = "stopword_file.txt";
			}
		}
		
		return this.inputFileName;
	}
	
	/*
	 * read Social Language Dictionary
	 */
	//build session
    SparkSession sparkSession = SparkSession
            .builder()
            .appName("Java Spark SQL Example")
            .config("spark.master", "local")
            .getOrCreate();
	
	class Bean {
	     private String col1;
	     private String col2;   
//	     private Timestamp col3;
	}	

	public Dataset<Bean> readSocialDictionary() {
//		return null;
		
		StructType structType= new StructType(new StructField[] {
                new StructField("col1", DataTypes.StringType, true, Metadata.empty()),
                new StructField("col2", DataTypes.StringType, true, Metadata.empty()),
//                new StructField("col3", DataTypes.TimestampType, true, Metadata.empty())
        });

		Dataset<Bean> ds = this.sparkSession.read().
		                schema(structType).
		                format("com.crealytics.spark.excel").
		                option("useHeader", true). // If the xls file has headers
		                option("timestampFormat", "yyyy-MM-dd HH:mm:ss"). // If you want to convert timestamp to a specific format
		                option("treatEmptyValuesAsNulls", "false").
		                option("inferSchema", "false").
		                option("addColorColumns", "false").
		                load("/home/user/test/sample.xls"). //path to xls or xlsx
		                as(Encoders.bean(Bean.class)); 
		
		return ds;
	}
}
