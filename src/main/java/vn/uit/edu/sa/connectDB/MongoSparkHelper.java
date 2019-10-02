package vn.uit.edu.sa.connectDB;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.bson.Document;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.StructType;

import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.ReadConfig;

import util.sparkConfigure;
import vn.uit.edu.sa.connectDB.ConnectionDB;
import vn.uit.edu.sa.define.Constant;

public class MongoSparkHelper<T> {
	
	private JavaSparkContext sparkContext;
	
	public MongoSparkHelper() {
		
	}
	
	public MongoSparkHelper(JavaSparkContext _sparkContext) {
		this.sparkContext = _sparkContext;
	}
	
	public JavaSparkContext getSparkContext() {
		return this.sparkContext;
	}
	
	public Dataset<T> read (String collectionName, Class<T> entityClass){
		Map<String, String> readOverrides = new HashMap<String, String>();
		readOverrides.put("collection", collectionName);
		readOverrides.put("readPreference.name", "secondaryPreferred");		
		ReadConfig readConfig = ReadConfig.create(sparkContext).withOptions(readOverrides);
		return MongoSpark.load(sparkContext, readConfig).toDS(entityClass);
	}
	
	public DataFrame read(String collectionName){
		Map<String, String> readOverrides = new HashMap<String, String>();
		readOverrides.put("collection", collectionName);
		readOverrides.put("readPreference.name", "secondaryPreferred");

		ReadConfig readConfig = ReadConfig.create(sparkContext).withOptions(readOverrides);
        return MongoSpark.load(sparkContext, readConfig).toDF();
	}
	
	public Dataset<T> read(String collectionName,List<Document> filterPipeline, Class<T> entityClass){
        Map<String, String> readOverrides = new HashMap<String, String>();
        readOverrides.put("collection", collectionName);
		readOverrides.put("readPreference.name", "secondaryPreferred");

        ReadConfig readConfig = ReadConfig.create(sparkContext).withOptions(readOverrides);
        
        return MongoSpark.load(sparkContext, readConfig).withPipeline(filterPipeline).toDS(entityClass);
	}

	public void generateInputFile() {
		DataFrame df1 = this.read(Constant.collectionComment);
		DataFrame _df1 = df1.filter("message != ''").filter("message != '.'").select("message");
		DataFrame df2 = this.read(Constant.collectionPost);
		DataFrame _df2 = df2.filter("message != ''" ).select("message");
		
		DataFrame mergedDF = _df1.unionAll(_df2);
		
		String temp = "";
		
	    List<String> listOne = mergedDF.as(Encoders.STRING()).collectAsList();
		List<String> listTwo = new ArrayList();

		for (String row : listOne) {
			temp = row.replace("\n", " ").replace("\r", " ").replace(System.getProperty("line.separator"), " ").replace("\r\n", " ");
			listTwo.add(temp);
		}
		
		SQLContext sqc = new SQLContext(sparkContext);
		DataFrame messageDF = sqc.createDataset(listTwo, Encoders.STRING()).toDF();
		
		messageDF.repartition(1).write().text(Constant.projecInputDir);
		
		if (rename(this.getFileName()))
			System.out.println(Constant.generateInputLDAFileSuccess);
		else
			System.out.println(Constant.generateInputLDAFileFail);
	}
	
	
	//May ham tam thoi, se bo vo ulti sau
	private String getFileName() {
		String fileName = "";
		File folder = new File(Constant.projecInputDir);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (!getExtensionByStringHandling(file.getName()).equals(".crc")) {
				if (file.getName() != "_SUCCESS")
				{
					fileName = file.getName();
				}
			}

		}
		return fileName;
	}
	
	private Optional<String> getExtensionByStringHandling(String filename) {
	    return Optional.ofNullable(filename)
	      .filter(f -> f.contains("."))
	      .map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
	
	
	private boolean rename(String oldName) {
		File oldFile = new File(Constant.projecInputDir + "/" + oldName);
		File newFile = new File(Constant.projecInputDir + "/" + Constant.inputFileName);
		if (oldFile.renameTo(newFile))
			return true;		
		return false;
	}
	
	public void readFileAsDataFrame(String fileName) {
		SQLContext sqlContext = new SQLContext(sparkContext);

		DataFrame df = sqlContext.sql("SELECT * FROM " +  fileName );

		df.show();
	}
}
