package ubimessage.dispatcher;

import java.io.IOException;
import java.util.ArrayList;

import ubimessage.message.Message;

public class MessageParser {
	private Message msg;
	private static String SP = "|";
	private static String SP1 = ":";
	public MessageParser(Message msg) {
		this.msg = msg;
	}
	
	//topic="monitor"，监控客户端发出监控列表时，解析出监控的目标客户端列表
	public  ArrayList<String>  parserMonitoredClient(){
		String content;
		//客户端列表
		ArrayList<String> clientList = new ArrayList<String>();
		try {
			//消息体内容
			content = (String)msg.getContent();
			//客户端列表
			String []clients = content.split("\\"+SP);
			//逐一添加客户端
			for(int i=1;i<clients.length;i++) {
				clientList.add(clients[i].trim());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientList;
	}
	
	/*//topic="coordinate"，当是坐标类型的消息时，解析出客户端ID
	public String parserClient(){
		String content = null;
		String clientID = null;
		//content = (String)msg.getContent();
		 clientID = msg.getRecipient();
		//获取mac
		//String mac = content.split(SP)[1];
		return clientID;
	}*/
}
