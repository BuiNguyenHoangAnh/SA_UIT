package segmentation;

import java.io.File;
import java.util.ArrayList;

import vn.uit.edu.sa.define.Constant;

public class segmentationDAO {
	private ArrayList<String> fileName = null;
	
	public ArrayList<String> inputFiles() {
		this.fileName = new ArrayList<>();
		String inputDir = Constant.projectOutputDir + "/Standardize";

		File folder = new File(inputDir);
		File[] listOfFiles = folder.listFiles();
		
		for (File file : listOfFiles){
			if (!file.isHidden()) {
				fileName.add(file.getName());				
			}
		}				
		return this.fileName;
	}
}
