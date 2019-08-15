package dao;

public class segmentationDAO {
	private String[] fileName = null;
	
	public String[] inputFiles() {
		int length = 2;
		this.fileName = new String[length];
		
		// checking if there is no input file then exit app
		if (this.fileName.length <= 0) {
			System.out.println("No files provided.");
			System.exit(0);
		}
		// set data for file name elements
		else {
			for (int i = 0; i < this.fileName.length; i++) {
				this.fileName[i] = "input.txt";
			}
		}
		
		return this.fileName;
	}
}
