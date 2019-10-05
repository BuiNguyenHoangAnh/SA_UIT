package util;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.spark.api.java.JavaRDD;

import vn.uit.edu.sa.define.Constant;

public class helpFunction {
	//read data from file and push it to a string
		public String pushDataFromFileToString(JavaRDD<String> inputFile) {
			String inputString = null;
			for(String line:inputFile.collect()){
		//        System.out.println(line);
				if (!line.isEmpty()) 
					inputString = inputString + " \n " + line;
		    }
			return inputString;
		}

	//write a string to output file
		public JavaRDD<String> writeStringToFile(sparkConfigure spark, String outputString) {
			List<String> list;
		
			list = Arrays.asList(outputString);
			JavaRDD<String> result = spark.getSparkContext().parallelize(list); 
			return result;
		}
		
		public static void removeUnusedFile(String location) {
			File folder = null;
			switch (location){
				case "Standardize":{
					folder = new File(Constant.projectOutputDir + "/Standardize");

					break;
				}
				case "Segementation": {
					folder = new File(Constant.projectOutputDir + "/Segmentation");

					break;
				}
				case "StopWords":{
					folder = new File(Constant.projectOutputDir + "/StopWords");

					break;
				}
				case "Tagging":{
					folder = new File(Constant.projectOutputDir + "/Tagging");

					break;
				}
			}
			
			File[] listOfFiles = folder.listFiles();
			
			for (File file : listOfFiles) {
				if (getExtensionByStringHandling(file.getName()).equals(".crc")) {
					file.delete();
				}else if (file.getName() == "SUCCESS") {
					file.delete();
				}else if (file.length() == 0) {
					file.delete();
				}else if (file.isHidden()) {
					file.delete();
				}
			}
		}
		
		public static String getFileName() {
			String fileName = "";
			File folder = new File(Constant.projecInputDir);
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
				if (!getExtensionByStringHandling(file.getName()).equals(".crc")) {
					if (file.getName() != "_SUCCESS")
					{
						fileName = file.getName();
					}
				}
			}
			return fileName;
		}
		
		public static String getFileName(String fileName) {
			File folder = new File(Constant.projectOutputDir + "/" + fileName);
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
				if (!getExtensionByStringHandling(file.getName()).equals(".crc")) {
					if (file.getName() != "_SUCCESS")
					{
						fileName = file.getName();
					}
				}
			}
			return fileName;
		}
		
		
		public static Optional<String> getExtensionByStringHandling(String filename) {
		    return Optional.ofNullable(filename)
		      .filter(f -> f.contains("."))
		      .map(f -> f.substring(filename.lastIndexOf(".") + 1));
		}
		
		
		private boolean rename(String oldName) {
			File oldFile = new File(Constant.projecInputDir + "/" + oldName);
			File newFile = new File(Constant.projecInputDir + "/" + Constant.inputFileName);
			if (oldFile.renameTo(newFile))
				return true;		
			return false;
		}

		public static String removeEmptyLine(String outputString) {
			String[] stringArray = outputString.split("\n");
			List<String> stringList = new ArrayList<>();
			Collections.addAll(stringList, stringArray);
			if (stringList.get(0).equals("null \n")) stringList.remove(0);
			if (stringList.get(1).equals(" null \n")) stringList.remove(1);
			for (int i = 0; i < stringList.size(); i ++) {
				if (stringList.get(i).equals(" ") || stringList.isEmpty()) {
					stringList.remove(i); 
				}else if (stringList.get(i).charAt(0) == ' ') {
					stringList.set(i, stringList.get(i).substring(1));
				}
			}
			String res = "";
			for (String string : stringList) {
				res += string;
				res += "\n";
			}
			return res;
		}
		
		public static void addNumberToDocument(String count, String fileName) {
			  try {
				  RandomAccessFile f = new RandomAccessFile(new File(fileName), "rw");
				  f.seek(0); 
				  f.write(count.getBytes());
				  f.close();		  
				  }catch (Exception e) {
				  	System.out.println(e);
			  }
		}
		
		public static long convertToListAndCount(String string) {
			List<String> stringList = new  ArrayList<>();
			Collections.addAll(stringList, string.split("\n"));
			return stringList.size();
		}
}
