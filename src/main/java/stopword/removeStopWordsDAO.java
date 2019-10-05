package stopword;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.spark.api.java.JavaRDD;

import util.helpFunction;
import util.sparkConfigure;
import vn.uit.edu.sa.define.Constant;

public class removeStopWordsDAO {
	private String stopWordsDictionaryFileName = null;
	private ArrayList<String> inputFileName = null;
	
//	get stop words dictionary
	public JavaRDD<String> dictionaryFile(sparkConfigure spark) {
		this.stopWordsDictionaryFileName = "resource/stopword_dictionary.txt";

		if (this.stopWordsDictionaryFileName != "" || this.stopWordsDictionaryFileName != null) {
			JavaRDD<String> dictionaryFile = spark.getSparkContext().textFile(this.stopWordsDictionaryFileName);
			return dictionaryFile;
		}
		return null;
	}
	
//	get input
	public ArrayList<String> inputFiles() {
		this.inputFileName = new ArrayList<>();
		

		String inputDir = Constant.projectOutputDir + "/Segmentation";

		File folder = new File(inputDir);
		File[] listOfFiles = folder.listFiles();		
		for (File file : listOfFiles){
			if (!file.isHidden())
				inputFileName.add(file.getName());
		}		
		return this.inputFileName;
	}
}
