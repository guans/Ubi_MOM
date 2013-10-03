package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 该类保存了程序运行时的全局设置信息，应用程序启动时先从配置文件加载，设置该类的静态变量；
 * 运行时，根据用户的设置，修改类的静态变量；退出时将静态成员变量保存到配置文件中。
 * 
 * @author 胡旭科
 * @version 0.1
 */
public class Setting {

	public Setting() {
		readTxtFile("1.txt");
	}

	/** 服务引擎标识 */
	public static String SERVER_ENGINE_ID = "server_engine";

	/** WEB端引擎标识 */
	public static String WEB_ENGINE_ID = "web_engine";

	/** 定位引擎标识 */
	public static String LOCATION_ENGINE_ID = "location_engine";
	public static String MAC=readTxtFile("1.txt");
	/** 记录用户的登录状态 */
	public static String LOGIN_STATUS = "fail";
	/** 记录用户的登录ID */
	public static String USERNAME = " ";
	static	String lineTxt = null;
	public static String readTxtFile(String filePath) {
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				
				while ((lineTxt = bufferedReader.readLine()) != null) {
					Setting.MAC = lineTxt;
					System.out.println(lineTxt);
					break;
				}
				//read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return lineTxt;

	}
}
