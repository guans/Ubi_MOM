package test;

import test.TestServerSend.People;
import ubimessage.client.MOMClient;
import ubimessage.message.Message;
import ubimessage.message.MessageListener;

/**
 * 主要功能：WEB端获取信息
 * 
 */
public class TestWebGet {

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

	// ------------------------------------------------------------------------------------------

	// 获得服务器定位信息
	public static void DoLocateWeb(Message m) throws Exception {

		byte[] newByte1 = new byte[24];
		System.arraycopy(m.getContentAsBytes(), 0, newByte1, 0, 24);
		double x[] = byteToDouble(m.getContentAsBytes());
		System.out.println((x[0]));
		System.out.println((x[1]));
		System.out.println((x[2]));

		byte[] newByte2 = new byte[16];
		System.arraycopy(m.getContentAsBytes(), 24, newByte2, 0, 16);
		String id = new String(newByte2).trim();
		System.out.println("id:" + id);

	}

	// 获得服务器单个人员查询信息
	public static void DoSearchSingle(Message m) throws Exception {

		byte[] newByte1 = new byte[16];
		byte[] newByte2 = new byte[16];
		byte[] newByte3 = new byte[16];
		byte[] newByte4 = new byte[16];
		System.arraycopy(m.getContentAsBytes(), 0, newByte1, 0, 16);
		System.arraycopy(m.getContentAsBytes(), 16, newByte2, 0, 16);
		System.arraycopy(m.getContentAsBytes(), 16 * 2, newByte3, 0, 16);
		System.arraycopy(m.getContentAsBytes(), 16 * 3, newByte4, 0, 16);
		String id = new String(newByte1).trim();
		String name = new String(newByte2, "GBK");// 终于转为中文、、
		String dise = new String(newByte3, "GBK");// 终于转为中文、、
		String phone = new String(newByte4).trim();

		System.out.println("id:" + id);
		System.out.println("name:" + name);
		System.out.println("dise:" + dise);
		System.out.println("phone:" + phone);

	}
	
	// 获得服务器多个人员查询信息
		public static void DoSearchAll(Message m) throws Exception {
			byte[] newByte1 = new byte[16];
			byte[] newByte2 = new byte[16];
			byte[] newByte3 = new byte[16];
			int num=Integer.valueOf(m.getHeader("num")).intValue();
			
			People p[]=new People[num];
			for(int i=0;i<num;i++)
			{
				System.arraycopy(m.getContentAsBytes(), 48*i+0, newByte1, 0, 16);
				System.arraycopy(m.getContentAsBytes(), 48*i+16, newByte2, 0, 16);
				System.arraycopy(m.getContentAsBytes(), 48*i+16 * 2, newByte3, 0, 16);
				
				String id=new String(newByte1).trim();
				String name = new String(newByte2, "GBK").trim();// 终于转为中文、、
				String phone=new String(newByte3).trim();
				p[i]=new People(id,name,"疾病",phone);
				System.out.println("id:" + p[i].id);
				System.out.println("name:" +p[i].name);
				System.out.println("phone:" + p[i].phone);
			}
			
		}
		
		// 处理获得mac和ID                                                    web端
				public static void DoGetMacid(Message m) throws Exception {
				
					byte[] newByte1 = new byte[32];
					byte[] newByte2 = new byte[16];
					int num=Integer.valueOf(m.getHeader("num")).intValue();
					MacAndId macandid[]=new MacAndId[num];
					for(int i=0;i<num;i++)
					{
						System.arraycopy(m.getContentAsBytes(), (32+16)*i+0, newByte1, 0, 32);
						System.arraycopy(m.getContentAsBytes(), (32+16)*i+32, newByte2, 0, 16);
						String mac=new String(newByte1).trim();
						String id=new String(newByte2).trim();
						macandid[i]=new MacAndId(mac,id);
						System.out.println("Mac:" + macandid[i].ma);
						System.out.println("Id:" + macandid[i].id);
					}
				}

	public static void main(String args[]) throws Exception {

		// create a client to receive messages asynchronously
		MOMClient rcpt = new MOMClient("127.0.0.1", 3009, Setting.WEB_ENGINE_ID);
		// then set the MessageListener for the client

		rcpt.setMessageListener(new MessageListener() {

			public void messageReceived(Message m) {
				try {
					// 读取信息头
					if (m.getHeader("type").equals("searchsingle")) {
						DoSearchSingle(m);
					}
					if (m.getHeader("type").equals("searchall")) {
						DoSearchAll(m);
					}
					if (m.getHeader("type").equals("locateweb")) {
						DoLocateWeb(m);
					}
					if (m.getHeader("type").equals("getmacid")) {
						DoGetMacid(m);
					}
					else
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
