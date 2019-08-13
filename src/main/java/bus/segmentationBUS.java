package bus;

import util.sparkConfigure;
import vn.vitk.tok.Tokenizer;

public class segmentationBUS {
	public void wordSegmentation(sparkConfigure spark, String fileName) {
		if(fileName != "" || fileName != null) {
			String dataFolder = "/export/dat/tok";
//			String master = "local[*]";
			String master = spark.getSparkConf().get("spark.master");	
			String inputFileName = fileName;
			String outputDirectory = "SegmentData";

			Tokenizer tokenizer = null;
			tokenizer = new Tokenizer(master, dataFolder + "/lexicon.xml", dataFolder + "/regexp.txt", dataFolder + "/whitespace.model", true);
			tokenizer.tokenize(inputFileName, outputDirectory);
		}
    }
}
