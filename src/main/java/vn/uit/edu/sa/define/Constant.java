package vn.uit.edu.sa.define;

public class Constant {
	
	/*Start of MongoDB define*/
	
	//localhost
	public static final String mongoServerAddr = "mongodb://127.0.0.1"; //localhost
	public static final String databaseName = "uit";
	public static final String collectionComment = "comment-from-selected-page";
	public static final String collectionPost = "post";
	public static final String mongoURI = "mongodb://127.0.0.1/uit.comment-from-selected-page";
	public static final String mongoLocalHost = "127.0.0.1";
	public static final String mongoDefaultPort = "27017";
	public static final String appName = "project-sa-uit";
	
	//remote server
	public static final String UITServerAddr = "mongodb://192.168.24.17";
	public static final String UITServerPort = "27017";
	public static final String UITDatabaseName = "uit-crawler";
	public static final String UITServerHost = "192.168.24.17";
	public static final String UITCollectionPost = "post";
	public static final String UITCollectionComment = "comment";
	
	//this is not safe!!!
	public static final String UITMongoDBUser = "sysadmin";
	public static final String UITMongoDBPassword = "U!t-sLs_@dmin";
	public static final String UITAuthSource = "admin";
	public static final String UITServerInputURI = "mongodb://" + UITMongoDBUser + ":" + UITMongoDBPassword + "@" + UITServerHost + ":" + UITServerPort + "/" + UITDatabaseName + "."  + UITCollectionPost + "?authSource=" + UITAuthSource;
	public static final String UITServerOutputURI = "mongodb://" + UITMongoDBUser + ":" + UITMongoDBPassword + "@" + UITServerHost + ":" + UITServerPort + "/" + UITDatabaseName + "."  + UITCollectionPost + "?authSource=" + UITAuthSource;;
	
	public static final String inputURI = "spark.mongodb.input.uri";
	public static final String outputURI = "spark.mongodb.output.uri";
	
	/*End of MongoDB define*/
	
	/*Start of LDA Model & Gibbs Sampling define*/
	public static final String GibbsLDA_optionDir = System.getProperty("user.dir") + "/data/jgibbslda/LDAModelDir";
	public static final String LDAmodelName = "LDAModel";
	public static final int GibbsNiters = 100;
	public static final String GibbsLDA_dFile = "input-data.txt";
	
	public static final String GibbsLDA_input_dataFileName = "part-00000";

	
	/*End of LDA Model & Gibbs Sampling define*/
	
	/*Start of common define*/
	
	public static final String inputFileName = "input";
	public static final String projecInputDir = System.getProperty("user.dir") + "/" + inputFileName;
	public static final String projectInputFile = Constant.projecInputDir + "/" + Constant.inputFileName;
	public static final String outputStandardizedDir = System.getProperty("user.dir") + "/output/Standardize";
	public static final String projectOutputDir = System.getProperty("user.dir") + "/output";
	public static final String projectInputFolder = System.getProperty("user.dir") + "/input";
	
	/*End of MongoDB define*/
	
	/*
	 * Start of Log
	 */
	
	public static final String generateInputLDAFileSuccess = "Successfully generated Input LDA Model File!";
	public static final String generateInputLDAFileFail = "Unsuccessfully generated Input LDA Model File!";
	public static final int numOfProjectInputFile = 1;
	/*
	 * End of Log
	 */
	//mongodb config

	
	//export
	//public static final String optionDir = System.getProperty("user.dir") + "/data/LDAModelDir";
	//public static final String dFile = "input-data.txt";
}
