package test;

import java.text.SimpleDateFormat;
import java.util.Date;

import ubimessage.client.MOMClient;
import ubimessage.message.Message;

/**
 * 主要功能：WEB端发出信息
 * 
 */
public class TestWebSend {
	public static void main(String args[]) throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		// create a client to send messages
		MOMClient sender = new MOMClient("192.168.1.104", 3009, Setting.WEB_ENGINE_ID);
		// then connect it to the dispatcher
		sender.connect();
		Thread.sleep(3000);
		// now send multiple messages to receiver with different contents
		Message m;
		Setting.MAC = "abc123";
		sender.sendMessage(LocateWeb());
		sender.sendMessage(SearchSingle("2364"));
		sender.sendMessage(SearchAll("87539478"));
		sender.sendMessage(SendFireNode(4,5,6));
		sender.sendMessage(RemoveFireNode());
		// disconnect from the publisher
		sender.disconnect();
	}

	
	//定位请求
	public static  Message LocateWeb() throws Exception{  
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Message t=new Message();
		t.setRecipient(Setting.SERVER_ENGINE_ID);
		t.setSender(Setting.WEB_ENGINE_ID);
		//消息头
	    t.setHeader("length", "16");  
	    t.setHeader("timeid", df.format(new Date()));
	    t.setHeader("type", "locatweb");
	    t.setHeader("from", "web");
	    t.setHeader("to", "server");
	    //消息
	    byte []content=new byte[16];
	    
		  return t;
	}
	
	//单个人员查询请求
		public static  Message SearchSingle(String pid) throws Exception{  
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			Message t=new Message();
			t.setRecipient(Setting.SERVER_ENGINE_ID);
			t.setSender(Setting.WEB_ENGINE_ID);
			//消息头
		    t.setHeader("length", "16");  
		    t.setHeader("timeid", df.format(new Date()));
		    t.setHeader("type", "searchsingle");
		    t.setHeader("from", "web");
		    t.setHeader("to", "server");
		    //消息
		    byte []content=new byte[16];
		    pid.getBytes(0, pid.length(), content, 0);
		    t.setContentAsBytes(content);
			  return t;
		}
		
		//房间内所有人员查询请求
		public static  Message SearchAll(String room) throws Exception{  
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			Message t=new Message();
			t.setRecipient(Setting.SERVER_ENGINE_ID);
			t.setSender(Setting.WEB_ENGINE_ID);
			//消息头
		    t.setHeader("length", "16");  
		    t.setHeader("timeid", df.format(new Date()));
		    t.setHeader("type", "searchall");
		    t.setHeader("from", "web");
		    t.setHeader("to", "server");
		    //消息
		    byte []content=new byte[16];
		    room.getBytes(0, room.length(), content, 0);
		    t.setContentAsBytes(content);
		    
			  return t;
		}
		
		//发送火灾点 
				public static  Message SendFireNode(double x,double y,double z) throws Exception{  
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					Message t=new Message();
					t.setRecipient(Setting.SERVER_ENGINE_ID);
					t.setSender(Setting.WEB_ENGINE_ID);
					//消息头
				    t.setHeader("length", "24");  
				    t.setHeader("timeid", df.format(new Date()));
				    t.setHeader("type", "sendfirenode");
				    t.setHeader("from", "web");
				    t.setHeader("to", "server");
				 // 消息
					byte[] content = new byte[24];
					content = doubleToByte(x, y, z);
					t.setContentAsBytes(content);

				    
					  return t;
				}
				
				//消除火灾点
				public static  Message RemoveFireNode() throws Exception{  
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					Message t=new Message();
					t.setRecipient(Setting.SERVER_ENGINE_ID);
					t.setSender(Setting.WEB_ENGINE_ID);
					//消息头
				    t.setHeader("length", "16");  
				    t.setHeader("timeid", df.format(new Date()));
				    t.setHeader("type", "removefirenode");
				    t.setHeader("from", "web");
				    t.setHeader("to", "server");
				    //消息
				    byte []content=new byte[16];
				   
				    t.setContentAsBytes(content);
				    
					  return t;
				}
				
				// 获得所有mac和id
				public static  Message GetMacId() throws Exception{  
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					Message t=new Message();
					t.setRecipient(Setting.SERVER_ENGINE_ID);
					t.setSender(Setting.WEB_ENGINE_ID);
					//消息头
				    t.setHeader("length", "16");  
				    t.setHeader("timeid", df.format(new Date()));
				    t.setHeader("type", "getmacid");
				    t.setHeader("from", "web");
				    t.setHeader("to", "server");
				    //消息
				    byte []content=new byte[16];
				   
				    t.setContentAsBytes(content);
				    
					  return t;
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
	
}
