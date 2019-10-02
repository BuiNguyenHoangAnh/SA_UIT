package vn.uit.edu.sa.gibbsLDA;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SQLContext;

import scala.collection.Seq;
import util.sparkConfigure;
import vn.uit.edu.sa.connectDB.MongoSparkHelper;
import vn.uit.edu.sa.define.Constant;
import vn.uit.edu.sa.gibbsLDA.src.Inferencer;
import vn.uit.edu.sa.gibbsLDA.src.LDACmdOption;
import vn.uit.edu.sa.gibbsLDA.src.Model;

public class GibbsLDAService {

	JavaSparkContext sparkContext = null;
	MongoSparkHelper mongoHelper = null;
	LDACmdOption option = null;
	
	public GibbsLDAService() {
		
	}
	
	public GibbsLDAService(sparkConfigure sparkConfig) {
		sparkContext = sparkConfig.getSparkContext();
		
		option = new LDACmdOption();
		option.inf = true;
		option.dir = Constant.GibbsLDA_optionDir + "/output";
		option.modelName = Constant.LDAmodelName;
		option.niters = Constant.GibbsNiters;
	}
	
	public void generateInputFile() {
		
		MongoSparkHelper mongoHelper = new MongoSparkHelper(sparkContext);
		  
		DataFrame commentDF = mongoHelper.read(Constant.collectionComment);
		DataFrame messageDF = commentDF.filter("message != ''").filter("message != '.'").select("message");
		
		DataFrame postDF = mongoHelper.read(Constant.collectionPost);
		DataFrame postMessageFrame = postDF.filter("message != ''" ).select("message");
		//postMessageFrame.show();
		DataFrame mergedDF = postMessageFrame.unionAll(messageDF);
		
		String temp = "";
		
	    List<String> listOne = mergedDF.as(Encoders.STRING()).collectAsList();
		List<String> listTwo = new ArrayList();

		for (String row : listOne) {
			temp = row.replace("\n", " ").replace("\r", " ").replace(System.getProperty("line.separator"), " ").replace("\r\n", " ");
			listTwo.add(temp);
		}
		
		SQLContext sqc = new SQLContext(sparkContext);
		DataFrame df = sqc.createDataset(listTwo, Encoders.STRING()).toDF();
		
		//mergedDF.show(4000, false);

		df.repartition(1).write().text(Constant.GibbsLDA_optionDir);

		addNumberToDocument(Long.toString(mergedDF.count()) + "\n", this.getFileName() );
		
		if (rename(this.getFileName()))
			System.out.println(Constant.generateInputLDAFileSuccess);
		else
			System.out.println(Constant.generateInputLDAFileFail);
		 
	}
	
	public void inference() {
		option.dfile = Constant.GibbsLDA_input_dataFileName;
		Inferencer inferencer = new Inferencer();
		inferencer.init(option);
		
		option.dfile = Constant.GibbsLDA_input_dataFileName;
		Model gibbsLDAModel = inferencer.inference();
	}
	
	public void inference(String[] array) {
		option.dfile = Constant.GibbsLDA_input_dataFileName;
		Inferencer inferencer = new Inferencer();
		inferencer.init(option);
		
		option.dfile = Constant.GibbsLDA_input_dataFileName;
		Model gibbsLDAModel = inferencer.inference(array);
	}
	
	private String getFileName() {
		String fileName = "";
		File folder = new File(Constant.GibbsLDA_optionDir);
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
	
	private void addNumberToDocument(String count, String fileName) {
		  try {
			  RandomAccessFile f = new RandomAccessFile(new File(Constant.GibbsLDA_optionDir + "/" + fileName), "rw");
			  f.seek(0); 
			  f.write(count.getBytes());
			  f.close();		  
			  }catch (Exception e) {
			  	System.out.println(e);
		  }
	}
	
	private boolean rename(String oldName) {
		File oldFile = new File(Constant.GibbsLDA_optionDir + "/" + oldName);
		File newFile = new File(Constant.GibbsLDA_optionDir + "/" + Constant.GibbsLDA_input_dataFileName);
		if (oldFile.renameTo(newFile))
			return true;		
		return false;
	}
}
