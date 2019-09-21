package tagging;

public class taggingDTO {
	taggingDAO taggingnDao = new taggingDAO();

	public int getInputLength() {
		return this.taggingnDao.inputFiles().length;
	}
	
	public String[] getInputFiles() {
		return this.taggingnDao.inputFiles();
	}
}
