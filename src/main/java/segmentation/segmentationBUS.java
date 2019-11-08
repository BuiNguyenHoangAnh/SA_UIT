package segmentation;

import java.util.ArrayList;

import org.apache.spark.api.java.JavaRDD;

import util.helpFunction;
import util.sparkConfigure;
import vn.uit.edu.sa.define.Constant;
import vn.vitk.tok.Tokenizer;

public class segmentationBUS {
	segmentationDTO segmentationDto = new segmentationDTO();
	ArrayList<String> fileName;
	
	private helpFunction helpFunc = new helpFunction();

	public String wordSegmentation(sparkConfigure spark, String handleString) {
		String dataFolder = "/export/dat/tok";
		String master = "local[*]";	
		Tokenizer tokenizer = null;
		tokenizer = new Tokenizer(master, dataFolder + "/lexicon.xml", dataFolder + "/regexp.txt", dataFolder + "/syllables2M.arpa");
		
		return  helpFunc.pushDataFromFileToString(tokenizer.tokenize(handleString, true));
	}
}
