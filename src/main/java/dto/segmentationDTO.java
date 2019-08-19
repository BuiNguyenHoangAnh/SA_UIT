package dto;

import dao.segmentationDAO;

public class segmentationDTO {
	segmentationDAO segmentationDao = new segmentationDAO();

	public int getInputLength() {
		return this.segmentationDao.inputFiles().length;
	}
	
	public String[] getInputFiles() {
		return this.segmentationDao.inputFiles();
	}
}
