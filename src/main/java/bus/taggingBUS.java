package bus;

import dto.taggingDTO;
import util.sparkConfigure;
import vn.vitk.tag.Tagger;

public class taggingBUS {
	private String[] fileName;
	private taggingDTO taggingDto = new taggingDTO();
	
	public void wordTagging(sparkConfigure spark) {
		String dataFolder = "/export/dat/tag";
		String master = spark.getSparkConf().get("spark.master");
		String cmm = dataFolder + "/vi/cmm";
		
		Tagger tagger = new Tagger(spark.getSparkContext());
		tagger.load(cmm);
		
		this.fileName = this.taggingDto.getInputFiles();
		String outputFileName = "Tag";
		
		for (int i = 0; i < this.fileName.length; i++) {
			if(this.fileName[i] != "" || this.fileName[i] != null) {	
				String inputFileName = this.fileName[i];
				
				tagger.tag(inputFileName, outputFileName + (i+1), Tagger.OutputFormat.TEXT);
			}
		}
	}
}
