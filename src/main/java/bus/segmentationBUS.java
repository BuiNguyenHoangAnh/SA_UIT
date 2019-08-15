package bus;

import dto.segmentationDTO;
import util.sparkConfigure;
import vn.vitk.tok.Tokenizer;

public class segmentationBUS {
	segmentationDTO segmentationDto = new segmentationDTO();
	String[] fileName;

	public void wordSegmentation(sparkConfigure spark) {
		this.fileName = this.segmentationDto.getInputFiles();

		for (int i = 0; i < this.fileName.length; i++) {
			if(this.fileName[i] != "" || this.fileName[i] != null) {
				String dataFolder = "/export/dat/tok";
//				String master = "local[*]";
				String master = spark.getSparkConf().get("spark.master");	
				String inputFileName = this.fileName[i];
				String outputDirectory = "SegmentData";

				Tokenizer tokenizer = null;
				tokenizer = new Tokenizer(master, dataFolder + "/lexicon.xml", dataFolder + "/regexp.txt", dataFolder + "/whitespace.model", true);
				tokenizer.tokenize(inputFileName, outputDirectory + (i + 1));
			}
		}
    }
}
