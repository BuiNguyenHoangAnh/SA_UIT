package tagging;

import util.sparkConfigure;

import vn.vitk.tag.Tagger;
import vn.vitk.tag.Tagger.OutputFormat;	

public class taggingBUS {
	private String[] fileName;
	private taggingDTO taggingDto = new taggingDTO();
	
	public void wordTagging(sparkConfigure spark) {
		String dataFolder = "/export/dat/tag";
		String cmm = dataFolder + "/vi/cmm";
		
		Tagger tagger = new Tagger(spark.getSparkContext());
		tagger.load(cmm);
		
		this.fileName = this.taggingDto.getInputFiles();
		String outputFileName = "Tag";
		OutputFormat outputFormat = Tagger.OutputFormat.TEXT;
		
		for (int i = 0; i < this.fileName.length; i++) {
			if(this.fileName[i] != "" || this.fileName[i] != null) {	
				String inputFileName = this.fileName[i];
				
				tagger.tag(inputFileName, outputFileName + (i+1), outputFormat);
			}
		}
	}
}
