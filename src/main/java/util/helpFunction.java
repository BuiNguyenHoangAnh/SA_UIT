package util;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

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
		
		public static void splitString() {
			String input = " null \n" + 
					" Em đk rồi chỉ sợ lên đó lạ_nước_lạ_cái không biết thích ở đâu rồi chạy vòng_vòng kiếm phòng thì mệt -.- \n" + 
					" Nguyên_Khánh_Trần_Duyên_Thanh \n" + 
					" Kim_Yến_Nguyễn_Thị \n" + 
					" Nguyễn_Văn_Âu Âu_là sao âu \n" + 
					" Em đọc mấy cái xem thêm thôi . hihi \n" + 
					" Nguyên_Phan nè người quản_trị : v \n" + 
					" nhạc hay đấy người quản_trị : V \n" + 
					" Haha . Tag vô rứa thôi . Chứ không có tham_gia được đâu . hihi \n" + 
					" Nguyễn_Truog người quản_trị vào xem ảnh đại_diện cái thế_nào \n" + 
					" Thái_Thị_Kim_Duyên \n" + 
					" Không nói nhiều , Việt_Nam_vô địch ! - Tục_ngữ Châu Á \n" + 
					" 😂 😂 \n" + 
					" Tinh_Linh : v \n" + 
					" thích này đỡ không bài : 3 \n" + 
					" đăng_kí xong rồi \n" + 
					" mày không cho tao được không ? \n" + 
					" ảnh đại_diện nhớ có gì đó về con ruồi bê đê : )))))) \n" + 
					" cái nào cũng được \n" + 
					" không nào_hay đó \n" + 
					" Trần_Vạn_Xuân nghe nè . Cntt hay Quốc_tế đều vợ hết \n" + 
					" Hiếu_Thanh \n" + 
					" Tinh_Linh kho ́ a ̀ kkkk \n" + 
					" không hiểu gì hêt \n" + 
					" Tam T_Bui : )))))))))))) \n" + 
					" Như nào giải_thích ảnh đại_diện với \n" + 
					" thì không cho ảnh đại_diện với \n" + 
					" không người quản_trị muốn không gì nè : v ngoài anh văn ra thì mấy cái kia rộng cực \n" + 
					" Lam_Giang nghe sơ qua nè người quản_trị \n" + 
					" Nguyễn_Hà_Thuyên ok \n" + 
					" Nguyễn_Tấn_Dân \n" + 
					" Đồng_Quang_Quyền \n" + 
					" người ảnh đại_diện đã nói_vợ ảnh đại_diện mà không hiểu \n" + 
					" Phạm_Thị_Ngọc_Hoà : )) I đã xem \n" + 
					" chắc chủ_yếu cũng như năm trước thôi , người quản_trị coi cách đăng_kí quá \n" + 
					" mày biết gì chỉ ảnh đại_diện cái đó \n" + 
					" Đức_Toàn người quản_trị hiểu ý ảnh đại_diện chứ \n" + 
					" M_Ic_Uc \n" + 
					" gán nhãn người quản_trị cho vui chứ ảnh đại_diện nghĩ cái này có tham_gia người quản_trị cx rớt , mà nó lại cho cái này % chỉ_tiêu cao vãi lìn \n" + 
					" Thái_Thị_Kim_Duyên ";
			
			String res[] = input.split(" ");
			String result = "";
			  for(String word : res) { if(word.isEmpty()) continue;
			  //if(this.isStopWord(word)) continue; //remove stopwords 
			  if (word.equals("\n")) {
			  System.out.println("test"); 
			  } result += (word+" "); }
			 System.out.println(result);
		}
}
