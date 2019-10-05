package segmentation;

import java.util.ArrayList;

import util.helpFunction;
import util.sparkConfigure;
import vn.uit.edu.sa.define.Constant;
import vn.vitk.tok.Tokenizer;

public class segmentationBUS {
	segmentationDTO segmentationDto = new segmentationDTO();
	ArrayList<String> fileName;

	public void wordSegmentation(sparkConfigure spark) {
		String dataFolder = "/export/dat/tok";
//		String master = "local[*]";
		String master = spark.getSparkConf().get("spark.master");	
		
		Tokenizer tokenizer = null;
//		tokenizer = new Tokenizer(master, dataFolder + "/lexicon.xml", dataFolder + "/regexp.txt", dataFolder + "/whitespace.model", true);
		tokenizer = new Tokenizer(master, dataFolder + "/lexicon.xml", dataFolder + "/regexp.txt", dataFolder + "/syllables2M.arpa");

		String outputDirectory = Constant.projectOutputDir;
		String inputFileName = Constant.projectOutputDir + "/Standardize";
		this.fileName = this.segmentationDto.getInputFiles();

		/*
		 * for (int i = 0; i < this.fileName.length; i++) { if(this.fileName[i] != "" ||
		 * this.fileName[i] != null) { String inputFileName = this.fileName[i];
		 * 
		 * tokenizer.tokenize(inputFileName, outputDirectory + (i + 1)); } }
		 */
		
		tokenizer.tokenize(inputFileName,  outputDirectory + "/Segmentation");
    }

	public String wordSegmentation(sparkConfigure spark, String handleString) {
		String dataFolder = "/export/dat/tok";
		String master = spark.getSparkConf().get("spark.master");	
		Tokenizer tokenizer = null;
		tokenizer = new Tokenizer(master, dataFolder + "/lexicon.xml", dataFolder + "/regexp.txt", dataFolder + "/syllables2M.arpa");
		
		return  helpFunction.pushDataFromFileToString(tokenizer.tokenize(handleString, true));
	}
}
