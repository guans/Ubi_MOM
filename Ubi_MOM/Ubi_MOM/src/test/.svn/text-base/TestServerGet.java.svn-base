package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import ubimessage.client.MOMClient;
import ubimessage.message.Message;
import ubimessage.message.MessageListener;

/**
 * 主要功能：服务器端获取信息
 * 
 */

public class TestServerGet {
	// 处理移动端登陆消息(目前只做输出)

	// ---------------------------------------------------------------------------------------------
	// 浮点到字节转换 每八位一个浮点数
	public static byte[] doubleToByte(double x, double y, double z) {
		byte[] b = new byte[24];
		long l = Double.doubleToLongBits(x);
		for (int i = 0; i < 8; i++) {
			b[i] = new Long(l).byteValue();
			l = l >> 8;

		}
		l = Double.doubleToLongBits(y);
		for (int i = 8; i < 16; i++) {
			b[i] = new Long(l).byteValue();
			l = l >> 8;

		}
		l = Double.doubleToLongBits(z);
		for (int i = 16; i < b.length; i++) {
			b[i] = new Long(l).byteValue();
			l = l >> 8;

		}
		return b;
	}

	// 字节到浮点转换
	public static double[] byteToDouble(byte[] b) {
		long l[] = new long[3];
		//for (int i = 0; i < 3; i++) {
			l[0] = b[0];
			l[0] &= 0xff;
			l[0] |= ((long) b[1] << 8);
			l[0] &= 0xffff;
			l[0] |= ((long) b[2] << 16);
			l[0] &= 0xffffff;
			l[0] |= ((long) b[3] << 24);
			l[0] &= 0xffffffffl;
			l[0] |= ((long) b[4] << 32);
			l[0] &= 0xffffffffffl;
			l[0] |= ((long) b[5] << 40);
			l[0] &= 0xffffffffffffl;
			
			l[0] |= ((long) b[6] << 48);
			l[0]&=0xffffffffffffffl;
			l[0] |= ((long) b[7] << 56);

			l[1] = b[8];
			l[1] &= 0xff;
			l[1] |= ((long) b[9] << 8);
			l[1] &= 0xffff;
			l[1] |= ((long) b[10] << 16);
			l[1] &= 0xffffff;
			l[1] |= ((long) b[11] << 24);
			l[1] &= 0xffffffffl;
			l[1] |= ((long) b[12] << 32);
			l[1] &= 0xffffffffffl;
			l[1] |= ((long) b[13] << 40);
			l[1] &= 0xffffffffffffl;
			
			l[1] |= ((long) b[14] << 48);
			l[1]&=0xffffffffffffffl;
			l[1] |= ((long) b[15] << 56);

			l[2] = b[16];
			l[2] &= 0xff;
			l[2] |= ((long) b[17] << 8);
			l[2] &= 0xffff;
			l[2] |= ((long) b[18] << 16);
			l[2] &= 0xffffff;
			l[2] |= ((long) b[19] << 24);
			l[2] &= 0xffffffffl;
			l[2] |= ((long) b[20] << 32);
			l[2] &= 0xffffffffffl;
			l[2] |= ((long) b[21] << 40);
			l[2] &= 0xffffffffffffl;
			
			l[2] |= ((long) b[22] << 48);
			l[2]&=0xffffffffffffffl;
			l[2] |= ((long) b[23] << 56);
	//	}
		
		
		double d[] = new double[3];
		d[0] = Double.longBitsToDouble(l[0]);
		d[1] = Double.longBitsToDouble(l[1]);
		d[2] = Double.longBitsToDouble(l[2]);
		return d;
	}

	// ----------------------------------------------------------------------------------------------

	public static void DoLogin(Message m) throws Exception { /* 移动端 */
		// System.out.println("m"+m.getContent());
		byte[] newByte1 = new byte[16];
		byte[] newByte2 = new byte[16];
		// ** 把需要的数组复制到新数组中 *//*
		System.arraycopy(m.getContentAsBytes(), 0, newByte1, 0, 16);
		System.arraycopy(m.getContentAsBytes(), 16, newByte2, 0, 16);
		// ** 把数组添加到集合中 *//*
		String id = new String(newByte1).trim();
		String key = new String(newByte2).trim();

		System.out.println("id:" + id);
		System.out.println("password:" + key);

	}

	// 处理移动端注册消息
	public static void DoEnroll(Message m) throws Exception { /* 移动端 */
		// System.out.println("m"+m.getContent());
		byte[] newByte1 = new byte[16];
		byte[] newByte2 = new byte[16];
		byte[] newByte3 = new byte[32];
		// ** 把需要的数组复制到新数组中 *//*
		System.arraycopy(m.getContentAsBytes(), 0, newByte1, 0, 16);
		System.arraycopy(m.getContentAsBytes(), 16, newByte2, 0, 16);
		System.arraycopy(m.getContentAsBytes(), 32, newByte3, 0, 16);
		// ** 把数组添加到集合中 *//*
		String id = new String(newByte1).trim();
		String key = new String(newByte2).trim();
		String email = new String(newByte3).trim();

		System.out.println("id:" + id);
		System.out.println("password:" + key);
		System.out.println("email:" + email);
	}

	// 处理移动端自身定位请求消息
	public static void DoLocateSelf(Message m) throws Exception { /* 移动端 */
		// 接受到移动端信息，对其信息处理，发送给他当前坐标
	}

	// 处理移动端火灾点请求消息
	public static void DoFirePoint(Message m) throws Exception { /* 移动端 */
		// 接受到移动端信息，对其信息处理，发送给他火灾点坐标
	}

	// 处理移动端紧急情况医生请求消息
	public static void DoDocterInfo(Message m) throws Exception { /* 移动端 */
		// 接受到移动端信息，对其信息处理，发送给他医生信息（姓名+电话）
	}

	// 处理移动端查询地点请求消息
	public static void DoSearchPoint(Message m) throws Exception { /* 移动端 */
		byte[] newByte1 = new byte[16];
		System.arraycopy(m.getContentAsBytes(), 0, newByte1, 0, 16);
		String pname = new String(newByte1, "utf-8");// 终于转为中文、、
		System.out.println("要查询的点:" + pname);
	}

	// 处理移动端導航请求消息
	public static void DoNavigation(Message m) throws Exception { /* 移动端 */
		byte[] newByte1 = new byte[16];
		byte[] newByte2 = new byte[8];
		byte[] newByte3 = new byte[16];
		byte[] newByte4 = new byte[16];
		System.arraycopy(m.getContentAsBytes(), 0, newByte1, 0, 16);
		System.arraycopy(m.getContentAsBytes(), 16, newByte2, 0, 8);
		System.arraycopy(m.getContentAsBytes(), 24, newByte3, 0, 16);
		System.arraycopy(m.getContentAsBytes(), 40, newByte4, 0, 16);

		if (newByte2[0] == (1))
			System.out.println(("合法成功"));
		else
			System.out.println(("非法失败"));

		String splace = new String(newByte1, "utf-8");// 终于转为中文、、
		String eplace = new String(newByte3, "utf-8");// 终于转为中文、、
		String id = new String(newByte4).trim();
		System.out.println("要查询的起点:" + splace);
		System.out.println("要查询的重点:" + eplace);
		System.out.println("要查询的id:" + id);
	}

	// 处理移动端查询座標请求消息
	public static void DoSearchCoordinate(Message m) throws Exception { /* 移动端 */
		double x[] = byteToDouble(m.getContentAsBytes());
		System.out.println((x[0]));
		System.out.println((x[1]));
		System.out.println((x[2]));

	}

	/**
	 * 处理移动客户端的紧急导航路径请求
	 * 
	 * @param m
	 *            从消息中间件中获取到的消息
	 */
	public static void DoEmergencyRequest(Message m) {
		byte[] receive = new byte[32];
		System.arraycopy(m.getContentAsBytes(), 0, receive, 0, 32);
		String username = new String(receive).trim();
		if (m.getHeader("status").equals("success")) {
			System.out.println("该人员为特殊类型");
		} else {
			System.out.println("该人员为普通类型");
		}
		System.out.println("用户名为：" + username);
	}

	// 处理web端定位请求消息
	public static void DoLocateWeb(Message m) throws Exception { /* web端 */

	}

	// 处理web端单个人员查询消息
	public static void DoSearchSingle(Message m) throws Exception { /* web端 */
		byte[] newByte1 = new byte[16];
		System.arraycopy(m.getContentAsBytes(), 0, newByte1, 0, 16);
		String id = new String(newByte1).trim();
		System.out.println("人员编号:" + id);

	}

	// 处理web端房间内所有人员查询消息
	public static void SearchAll(Message m) throws Exception { /* web端 */

		byte[] newByte1 = new byte[16];
		System.arraycopy(m.getContentAsBytes(), 0, newByte1, 0, 16);
		String id = new String(newByte1).trim();
		System.out.println("房间编号:" + id);
	}
	
	// 接受WEB坐标
		public static void DoSendFireNode(Message m) throws Exception { /* web端 */

			double x[] = byteToDouble(m.getContentAsBytes());
			System.out.println((x[0]));
			System.out.println((x[1]));
			System.out.println((x[2]));
		}
		
		// 接受WEB消除火灾点
				public static void DoRemoveFireNode(Message m) throws Exception { /* web端 */

					System.out.println("消除火灾点");
				}
	
	
	
	//读写文件
	
		 public static void DOWriteTxt(String file, String txt) {
		  try {
		   FileOutputStream os = new FileOutputStream(new File(file), false);
		   os.write(txt.getBytes());
		   os.close();
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		 }

	public static void main(String args[]) throws Exception {

		// create a client to receive messages asynchronously
		// Client rcpt=new Client("0.0.0.0",1234,"receiver");
		MOMClient rcpt = new MOMClient("127.0.0.1", 3009,
				Setting.SERVER_ENGINE_ID);
		// then set the MessageListener for the client
		//

		rcpt.setMessageListener(new MessageListener() {

			public void messageReceived(Message m) {
				try {
					// 修改Setting的MAC
					if (m.getHeader("from").equals("utility")) {
						Setting.MAC = m.getSender();
						DOWriteTxt("1.txt",m.getSender());
					}
					
					// 读取信息头
					if (m.getHeader("type").equals("login")) {
						DoLogin(m);
					}
					if (m.getHeader("type").equals("enroll")) {
						DoEnroll(m);
					}
					if (m.getHeader("type").equals("navigate")) {
						DoNavigation(m);
					}
					if (m.getHeader("type").equals("locateself")) {
						System.out.println(("locate my place now"));
					}
					if (m.getHeader("type").equals("sendfirenode")) {
						DoSendFireNode(m);
					}
					if (m.getHeader("type").equals("removefirenode")) {
						DoRemoveFireNode(m);
					}

					if (m.getHeader("type").equals("searchcoordinate")) {
						DoSearchCoordinate(m);
					}
					else
						// System.out.println(m.getContentAsBytes().toString());
						System.out.println(m.getHeader("type"));
					System.out.println(m.getContent());
					System.out.println(m.getSender());
					System.out.println();

				} catch (Exception ex) {
				}
			}

			public void exceptionRaised(Exception ex) {
			}
		});
		// connect to the dispatcher
		rcpt.connect();
		//
		// wait for user input
		System.in.read(new byte[10]);
		// disconnect
		rcpt.disconnect();
	}
}