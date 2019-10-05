package segmentation;

import java.util.ArrayList;

public class segmentationDTO {
	segmentationDAO segmentationDao = new segmentationDAO();

	public int getInputLength() {
		return this.segmentationDao.inputFiles().size();
	}
	
	public ArrayList<String> getInputFiles() {
		return this.segmentationDao.inputFiles();
	}
}
