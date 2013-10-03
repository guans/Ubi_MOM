package test;

import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Date;

import test.TestUtilityGet.Node;

import ubimessage.client.MOMClient;
import ubimessage.message.Message;

/**
 * 主要功能 ： 服务器端发出信息
 * 
 */

public class TestServerSend {
	public static void main(String args[]) throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		// create a client to send messages
		// Client sender=new Client("192.168.1.135",3009,"sender");

		MOMClient sender = new MOMClient("192.168.1.101", 3009,
				Setting.SERVER_ENGINE_ID);
		// then connect it to the dispatcher
		sender.connect();
		Thread.sleep(3000);
		// now send multiple messages to receiver with different contents

		Message m;

		/*
		 * sender.sendMessage(DoEnroll(true));
		 * sender.sendMessage(DoLogin(true));
		 * sender.sendMessage(DoLocateSelf(23.3445324, 46.8956234, 13.4565234));
		 * sender.sendMessage(DoDocterInfo("陈两", "16548798465"));
		 * sender.sendMessage(DoSearchPoint("教二楼"));
		 * 
		 * 
		 * sender.sendMessage(DoLocateSelf(23.3445324, 46.8956234, 13.4565234));
		 * Node nodes[] = new Node[3]; nodes[0] = new Node(324321.234234234234,
		 * 2.222, 3.333); nodes[1] = new Node(3.546, 2.222, 3.333); nodes[2] =
		 * new Node(0.004, 2.222, 3.333);
		 * 
		 * sender.sendMessage(DoNavigation(3, nodes));
		 */
		/*sender.sendMessage(DoLogin(true));
		People aa[] = new People[3];
		aa[0] = new People("id2348324", "星星", "疾病", "1239234");
		aa[1] = new People("id45435", "的说法", "的风格的风格", "45345345");
		aa[2] = new People("3455435", "好机会法", "的鬼地方搞得很", "56233");
		// sender.sendMessage(DoSearchSingle(aa[0]));
		sender.sendMessage(DoSearchAll(aa));
		sender.sendMessage(DoLocateWeb(324.23, 23.234, 324.234, "sdhfjkd"));
*/
		People aa[] = new People[3];
		aa[0] = new People("id2348324", "星星", "疾病", "1239234");
		aa[1] = new People("id45435", "的说法", "的风格的风格", "45345345");
		aa[2] = new People("3455435", "好机会法", "的鬼地方搞得很", "56233");
		
		Place p[]=new Place[2];
		p[0]=new Place("教二楼",2.34,3.45,5.67);
		p[1]=new Place("北宗楼",8.67,4.88,7.86);
		//sender.sendMessage(DoSearchPoint(p.length, p));
		sender.sendMessage(DoTest(aa));
		
		
		MacAndId mm[]=new MacAndId[3];
		mm[0]=new MacAndId("78-2B-CB-DF-93-BE","admin");
		mm[1]=new MacAndId("57-7C-DG-ET-34-SD","XIAOMING");
		mm[2]=new MacAndId("FG-56-HJ-DA-23-HJ","CHENLI");
		sender.sendMessage(DoGetMacid(mm));
		// disconnect from the publisher
		sender.disconnect();
	}

	// ----------------------------------------------------------------------------------------------------
	// 处理移动端登陆验证消息
	public static Message DoLogin(boolean islogin) throws Exception { /* 移动端 */
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Message t = new Message();
		t.setRecipient(Setting.MAC);
		t.setSender(Setting.SERVER_ENGINE_ID);
		// 消息头
		t.setHeader("length", "1");
		t.setHeader("timeid", df.format(new Date()));
		t.setHeader("type", "login");
		t.setHeader("from", "server");
		t.setHeader("to", "utility");
		// 消息
		byte[] content = new byte[1];
		content[0] = (byte) (islogin ? 0x01 : 0x00);
		t.setContentAsBytes(content);
		return t;
	}

	// 处理移动端注册状态消息
	public static Message DoEnroll(boolean isenroll) throws Exception { /* 移动端 */
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Message t = new Message();
		t.setRecipient(Setting.MAC);
		t.setSender(Setting.SERVER_ENGINE_ID);
		// 消息头
		t.setHeader("length", "1");
		t.setHeader("timeid", df.format(new Date()));
		t.setHeader("type", "enroll");
		t.setHeader("from", "server");
		t.setHeader("to", "utility");
		// 消息
		byte[] content = new byte[1];
		content[0] = (byte) (isenroll ? 0x01 : 0x00);
		t.setContentAsBytes(content);
		return t;
	}

	// 处理移动端自身定位消息
	public static Message DoLocateSelf(double x, double y, double z) /* 移动端 */
	throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Message t = new Message();
		t.setRecipient(Setting.MAC);
		t.setSender(Setting.SERVER_ENGINE_ID);
		// 消息头
		t.setHeader("length", "24");
		t.setHeader("timeid", df.format(new Date()));
		t.setHeader("type", "locateself");
		t.setHeader("from", "server");
		t.setHeader("to", "utility");
		// 消息
		byte[] content = new byte[24];
		content = doubleToByte(x, y, z);
		t.setContentAsBytes(content);

		return t;
	}

	// 处理火灾点位置消息
	public static Message DoFirePoint(double x, double y, double z) /* 移动端 */
	throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Message t = new Message();
		t.setRecipient(Setting.MAC);
		t.setSender(Setting.SERVER_ENGINE_ID);
		// 消息头
		t.setHeader("length", "16");
		t.setHeader("timeid", df.format(new Date()));
		t.setHeader("type", "firepoint");
		t.setHeader("from", "server");
		t.setHeader("to", "utility");
		// 消息
		byte[] content = new byte[24];
		content = doubleToByte(x, y, z);
		t.setContentAsBytes(content);

		return t;
	}

	// 处理医生基本消息
	public static Message DoDocterInfo(String name, String phone) /* 移动端 */
	throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Message t = new Message();
		t.setRecipient(Setting.MAC);
		t.setSender(Setting.SERVER_ENGINE_ID);
		// 消息头
		t.setHeader("length", "24");
		t.setHeader("timeid", df.format(new Date()));
		t.setHeader("type", "docterinfo");
		t.setHeader("from", "server");
		t.setHeader("to", "utility");
		// 消息
		byte[] content = new byte[24];
		// name.getBytes(0, name.length(), content, 0);
		for (int i = 0; i < name.getBytes().length; i++) {
			content[i] = name.getBytes()[i];
		}
		// content[0]=name.getBytes();
		phone.getBytes(0, phone.length(), content, 8);
		t.setContentAsBytes(content);
		return t;
	}

	// 处理节点名称
	public static Message DoSearchPoint(int num, Place p[]) throws Exception { /* 移动端 */
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Message t = new Message();
		t.setRecipient(Setting.MAC);
		t.setSender(Setting.SERVER_ENGINE_ID);
		// 消息头
		t.setHeader("length", "16");
		t.setHeader("timeid", df.format(new Date()));
		t.setHeader("type", "searchpoint");
		t.setHeader("from", "server");
		t.setHeader("to", "utility");
		t.setHeader("num", String.valueOf(num)); // 返回的节点个数
		// 消息 （节点名+(x,y,z))
		byte[] content = new byte[(16 + 24) * num];
		for (int i = 0; i < num; i++) {
			for (int j = 0; j < p[i].name.getBytes().length; j++) {
				content[i*(24+16)+j] = p[i].name.getBytes()[j];
			}
			
			
			
			byte[] b = new byte[24];
			long l = Double.doubleToLongBits(p[i].x);
			for (int k = 0; k < 8; k++) {
				b[k] = new Long(l).byteValue();
				l = l >> 8;

			}
			l = Double.doubleToLongBits(p[i].y);
			for (int k = 8; k < 16; k++) {
				b[k] = new Long(l).byteValue();
				l = l >> 8;

			}
			l = Double.doubleToLongBits(p[i].z);
			for (int k = 16; k < b.length; k++) {
				b[k] = new Long(l).byteValue();
				l = l >> 8;

			}
			
			for(int m=0;m<24;m++)
			{
				content[i*(24+16)+16+m]=b[m];
			}
			
			
		}
		t.setContentAsBytes(content);
		return t;
	}

	// 处理导航节点数组
	public static Message DoNavigation(int num, Node[] points) throws Exception { /* 移动端 */
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Message t = new Message();
		t.setRecipient(Setting.MAC);
		t.setSender(Setting.SERVER_ENGINE_ID);
		// 消息头
		t.setHeader("length", String.valueOf(24 * num)); // 一个点24 byte
		t.setHeader("timeid", df.format(new Date()));
		t.setHeader("type", "navigate");
		t.setHeader("from", "server");
		t.setHeader("to", "utility");
		t.setHeader("point", String.valueOf(num)); // 返回的节点个数
		// 消息
		byte[] content = new byte[24 * num];
		// content = doubleToByte(x, y, z);
		content = doubleToByteSome(points, num);
		t.setContentAsBytes(content);
		return t;

	}

	// 处理紧急呼叫路线
	public static Message DoEmergencyRequest(int num, Node[] points)
			throws Exception { /* 移动端 */
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Message t = new Message();
		t.setRecipient(Setting.MAC);
		t.setSender(Setting.SERVER_ENGINE_ID);
		// 消息头
		t.setHeader("length", String.valueOf(24 * num)); // 一个点24 byte
		t.setHeader("timeid", df.format(new Date()));
		t.setHeader("type", "emergency");
		t.setHeader("from", "server");
		t.setHeader("to", "utility");
		t.setHeader("point", String.valueOf(num)); // 返回的节点个数
		// 消息
		byte[] content = new byte[24 * num];
		// content = doubleToByte(x, y, z);
		content = doubleToByteSome(points, num);
		t.setContentAsBytes(content);
		return t;

	}

	// 处理定位消息
	public static Message DoLocateWeb(double x, double y, double z, String id) /* web端 */
	throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Message t = new Message();
		t.setRecipient(Setting.WEB_ENGINE_ID);
		t.setSender(Setting.SERVER_ENGINE_ID);
		// 消息头
		t.setHeader("length", "40");
		t.setHeader("timeid", df.format(new Date()));
		t.setHeader("type", "locateweb");
		t.setHeader("from", "server");
		t.setHeader("to", "web");

		// 消息
		byte[] content = new byte[40];
		int length = doubleToByte(x, y, z).length;
		byte[] byt = doubleToByte(x, y, z);
		for (int i = 0; i < length; i++) {
			content[i] = byt[i];
		}
		id.getBytes(0, id.length(), content, 24);
		t.setContentAsBytes(content);

		return t;
	}

	// 处理单个人员查询消息
	public static Message DoSearchSingle(People p) /* web端 */
	throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Message t = new Message();
		t.setRecipient(Setting.WEB_ENGINE_ID);
		t.setSender(Setting.SERVER_ENGINE_ID);
		// 消息头
		t.setHeader("length", "64");
		t.setHeader("timeid", df.format(new Date()));
		t.setHeader("type", "searchsingle");
		t.setHeader("from", "server");
		t.setHeader("to", "web");
		// 消息
		byte[] content = new byte[16 * 4];
		p.id.getBytes(0, p.id.length(), content, 0);
		for (int i = 0; i < p.name.getBytes().length; i++) {
			content[16 * 1 + i] = p.name.getBytes()[i];
		}
		for (int i = 0; i < p.dise.getBytes().length; i++) {
			content[16 * 2 + i] = p.dise.getBytes()[i];
		}
		p.phone.getBytes(0, p.phone.length(), content, 48);
		t.setContentAsBytes(content);
		return t;
	}

	// 处理房间所有人消息
	public static Message DoSearchAll(People p[]) /* web端 */
	throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Message t = new Message();
		t.setRecipient(Setting.WEB_ENGINE_ID);
		t.setSender(Setting.SERVER_ENGINE_ID);

		// 消息头
		t.setHeader("length", "16");
		t.setHeader("timeid", df.format(new Date()));
		t.setHeader("type", "searchall");
		t.setHeader("num", String.valueOf(p.length));
		t.setHeader("from", "server");
		t.setHeader("to", "web");
		// 消息
		byte[] content = new byte[16 * 3 * p.length];

		for (int i = 0; i < p.length; i++) {
			p[i].id.getBytes(0, p[i].id.length(), content, 16 * 3 * i);
			for (int j = 0; j < p[i].name.getBytes().length; j++) {
				content[16 * 3 * i + j + 16] = p[i].name.getBytes()[j];
			}
			p[i].phone.getBytes(0, p[i].phone.length(), content, 16 * 3 * i
					+ 16 * 2);
		}
		t.setContentAsBytes(content);

		return t;
	}
	
	
	// 测试
		public static Message DoTest(People p[]) /* web端 */
		throws Exception {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			Message t = new Message();
			t.setTopic("web");
		//	t.setRecipient(Setting.WEB_ENGINE_ID);
			t.setSender(Setting.SERVER_ENGINE_ID);

			// 消息头
			t.setHeader("length", "16");
			t.setHeader("timeid", df.format(new Date()));
			t.setHeader("type", "searchall");
			t.setHeader("num", String.valueOf(p.length));
			t.setHeader("from", "server");
			t.setHeader("to", "web");
			// 消息
			byte[] content = new byte[16 * 3 * p.length];

			for (int i = 0; i < p.length; i++) {
				p[i].id.getBytes(0, p[i].id.length(), content, 16 * 3 * i);
				for (int j = 0; j < p[i].name.getBytes().length; j++) {
					content[16 * 3 * i + j + 16] = p[i].name.getBytes()[j];
				}
				p[i].phone.getBytes(0, p[i].phone.length(), content, 16 * 3 * i
						+ 16 * 2);
			}
			t.setContentAsBytes(content);
			return t;
		}
		
		// 发送MAC和id          32 + 16    
				public static Message DoGetMacid(MacAndId m[]) /* web端 */
				throws Exception {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
					Message t = new Message();
					
					t.setRecipient(Setting.WEB_ENGINE_ID);
					t.setSender(Setting.SERVER_ENGINE_ID);

					// 消息头
					//t.setHeader("length", "16");
					t.setHeader("timeid", df.format(new Date()));
					t.setHeader("type", "getmacid");
					t.setHeader("num", String.valueOf(m.length));       //mac和id对的个数
					t.setHeader("from", "server");
					t.setHeader("to", "web");
					// 消息
					byte[] content = new byte[(32+16) * m.length];

					for (int i = 0; i < m.length; i++) {
						
						m[i].ma.getBytes(0,m[i].ma.length(),content,(32+16)*i);
						m[i].id.getBytes(0,m[i].id.length(),content,(32+16)*i+32);					
					}
					t.setContentAsBytes(content);
					return t;
				}
		
		

	// ----------------------------------------------------------------------------------------------------

	// 人员类
	public static class People {
		String id;
		String name;

		String dise;
		String phone;

		public People(String a, String b, String d, String p) {

			id = a;
			name = b;

			dise = d;
			phone = p;
		}

		People(People p) {
			id = p.id;
			name = p.name;

			dise = p.dise;
			phone = p.phone;
		}

	}

	
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
		for (int i = 0; i < 3; i++) {
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
		}

		double d[] = new double[3];
		d[0] = Double.longBitsToDouble(l[0]);
		d[1] = Double.longBitsToDouble(l[1]);
		d[2] = Double.longBitsToDouble(l[2]);
		return d;
	}

	// 浮点到字节转换(多点)
	public static byte[] doubleToByteSome(Node points[], int num) {

		byte b[] = new byte[24 * num];
		for (int i = 0; i < num; i++) {

			// byte[] b = new byte[24];
			long l = Double.doubleToLongBits(points[i].coor_x);
			for (int j = 0; j < 8; j++) {
				b[24 * i + j] = new Long(l).byteValue();
				l = l >> 8;
			}
			l = Double.doubleToLongBits(points[i].coor_y);
			for (int j = 8; j < 16; j++) {
				b[24 * i + j] = new Long(l).byteValue();
				l = l >> 8;

			}
			l = Double.doubleToLongBits(points[i].coor_z);
			for (int j = 16; j < 24; j++) {
				b[24 * i + j] = new Long(l).byteValue();
				l = l >> 8;
			}

		}
		return b;
	}

	// 字节到浮点转换（多点）
	public static Node[] byteToDoubleSome(byte[] b, int num) {
		Node nodes[] = new Node[num];
		for (int i = 0; i < num; i++) {

			long l[] = new long[3];
			for (int j = 0; j < 3; j++) {
				l[0] = b[24 * i + 0];
				l[0] &= 0xff;
				l[0] |= ((long) b[24 * i + 1] << 8);
				l[0] &= 0xffff;
				l[0] |= ((long) b[24 * i + 2] << 16);
				l[0] &= 0xffffff;
				l[0] |= ((long) b[24 * i + 3] << 24);
				l[0] &= 0xffffffffl;
				l[0] |= ((long) b[24 * i + 4] << 32);
				l[0] &= 0xffffffffffl;

				l[0] |= ((long) b[24 * i + 5] << 40);
				l[0] &= 0xffffffffffffl;
				l[0] |= ((long) b[24 * i + 6] << 48);
				l[0]&=0xffffffffffffffl;
				l[0] |= ((long) b[24 * i + 7] << 56);

				l[1] = b[24 * i + 8];
				l[1] &= 0xff;
				l[1] |= ((long) b[24 * i + 9] << 8);
				l[1] &= 0xffff;
				l[1] |= ((long) b[24 * i + 10] << 16);
				l[1] &= 0xffffff;
				l[1] |= ((long) b[24 * i + 11] << 24);
				l[1] &= 0xffffffffl;
				l[1] |= ((long) b[24 * i + 12] << 32);
				l[1] &= 0xffffffffffl;

				l[1] |= ((long) b[24 * i + 13] << 40);
				l[1] &= 0xffffffffffffl;
				l[1] |= ((long) b[24 * i + 14] << 48);
				l[1]&=0xffffffffffffffl;
				l[1] |= ((long) b[24 * i + 15] << 56);

				l[2] = b[24 * i + 16];
				l[2] &= 0xff;
				l[2] |= ((long) b[24 * i + 17] << 8);
				l[2] &= 0xffff;
				l[2] |= ((long) b[24 * i + 18] << 16);
				l[2] &= 0xffffff;
				l[2] |= ((long) b[24 * i + 19] << 24);
				l[2] &= 0xffffffffl;
				l[2] |= ((long) b[24 * i + 20] << 32);
				l[2] &= 0xffffffffffl;

				l[2] |= ((long) b[24 * i + 21] << 40);
				l[2] &= 0xffffffffffffl;
				l[2] |= ((long) b[24 * i + 22] << 48);
				l[2]&=0xffffffffffffffl;
				l[2] |= ((long) b[24 * i + 23] << 56);
			}

			nodes[i] = new Node(Double.longBitsToDouble(l[0]),
					Double.longBitsToDouble(l[1]),
					Double.longBitsToDouble(l[2]));
			/*
			 * nodes[i].coor_x=Double.longBitsToDouble(l[0]);
			 * nodes[i].coor_y=Double.longBitsToDouble(l[1]);
			 * nodes[i].coor_z=Double.longBitsToDouble(l[2]);
			 */

			/*
			 * double d[] = new double[3]; d[0] = Double.longBitsToDouble(l[0]);
			 * d[1] = Double.longBitsToDouble(l[1]); d[2] =
			 * Double.longBitsToDouble(l[2]);
			 */

		}
		return nodes;
	}

}
