package bus;

import org.apache.spark.SparkConf;

import vn.vitk.tok.Tokenizer;

public class segmentationBUS {
	public void wordSegmentation(String fileName) {
		if(fileName != "" || fileName != null) {
			SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("Word Segmentation");
//			String dataFolder = "/export/dat/tok";
			String dataFolder = "/home/buinguyenhoanganh/Desktop/vn.vitk/dat/tok";
//			String master = "local[*]";
			String master = sparkConf.get("spark.master");	
			String inputFileName = fileName;
			String outputDirectory = "SegmentData";

			Tokenizer tokenizer = null;
			tokenizer = new Tokenizer(master, dataFolder + "/lexicon.xml", dataFolder + "/regexp.txt", dataFolder + "/whitespace.model", true);
			tokenizer.tokenize(inputFileName, outputDirectory);
		}
    }
}
