/*

    MessageIT - Italian messaging middleware
    Copyright (C) 2005  Luca Cristina <lcristina at sourceforge dot net>

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

 * Created on 20-sep-2005
 * 21-oct-2005: added copyright notice
 */
package ubimessage.dispatcher;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ubimessage.message.Message;
import ubimessage.message.MessageSender;

/** This class is responsible for communication with a client connected to the dispatcher. 
 * It implements the server side of the communication protocol and interacts with the client, 
 * notifying actions to the Dispatcher and allows the Dispatcher to forward messages to the client. 
 * @author Luca Cristina
 */
public class ClientHandler implements MessageSender, ubimessage.MessageITProtocolConstants, Runnable{
	private Dispatcher dispatcher;
/*	private SocketChannel sc;
	private SelectionKey k;*/
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private MessageQueue queue;
	private String clientID;
	
	//改
	private Socket sock;
	private boolean flag=true;
	
	public ClientHandler(Dispatcher disp, Socket socket)throws IOException{
		dispatcher=disp;
		this.sock = socket;
		ois();
		oos();
		queue=new MessageQueue();
	}

	/** Initializes the ClientHandler with the Dispatcher and the client connection it is associated to. 
	 * @param disp the Dispatcher to which client requests must be forwarded
	 * @param key the SelectionKey representing the client's connection
	 */
	/*public ClientHandler(Dispatcher disp, SelectionKey key)throws IOException{
		k=key;
		sc=(SocketChannel)k.channel();
		dispatcher=disp;
		
		oos();
		queue=new MessageQueue();
	}*/
	/** Serves the client. This method assumes that some input from client is available; 
	 * otherwise it will lock waiting for it. 
	 * @throws IOException if a communication error occurs
	 */
	public void doService() throws IOException{
		Object o;		
		try{
			o=readObject();
			if(o instanceof String){
				String s=(String)o;
				System.out.println(clientID+": "+s);
				if(s.startsWith(MSGIT_CLIENT)){
					clientID=s.substring(MSGIT_CLIENT.length());
					int reply=0;
					if(getHandler(clientID)!=null){//already exists
						reply=1;
						clientID=null;
					}else addHandler(clientID, this);
					oos().writeInt(reply);
					
					oos().flush();
				}else if(s.startsWith(MSGIT_SUBSCRIBE))
					dispatcher.clientSubscribed(clientID,s.substring(MSGIT_SUBSCRIBE.length()));
				else if(s.equals(MSGIT_UNSUBSCRIBE))
					dispatcher.clientUnsubscribedAll(clientID);
				else if(s.startsWith(MSGIT_UNSUBSCRIBE))
					dispatcher.clientUnsubscribed(clientID,s.substring(MSGIT_UNSUBSCRIBE.length()));
				else if(s.equals("END")){
					dispatcher.clientDisconnected(clientID);
					removeHandler(clientID);
					/*k.cancel();
					sc.close();*/
					sock.close();
					flag=false;
					
				}
			}else if(o instanceof Message){
				if(clientID==null)return;//nobody
				System.out.println(clientID+": got message");
				dispatcher.messageArrived((Message)o);
				//return;
			}
			}catch(Exception e){
				//ois.reset();
				//ois.close();
				dispatcher.clientDisconnected(clientID);
				removeHandler(clientID);
			/*	k.cancel();
				sc.close();*/
				System.out.println(clientID+"强制关闭异常");
				flag=false;
				sock.close();
			}

		
		
	}
	/** Adds a Message to the queue of messages to be sent to the client
	 * @param msg the Message to send
	 */
	public void enqueueMessage(Message msg){
		queue.insert(msg);
	}
	/** Sends all messages in the 'outbox' queue to the client
	 * @throws IOException if a communication error occurs
	 */
	private void flushQueue() throws IOException{
		if(queue.isEmpty())return;
		while(!queue.isEmpty())
			oos().writeObject(queue.extract());
	}
	/** Read an Object from the client's communication channel
	 * @return the read object, or null if it could not be unserialized and instantiated
	 * @throws IOException if a communication error occurs
	 */
	private Object readObject()throws IOException{
		try{
			return ois().readObject();
		}catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();		
			return null;
		}catch(EOFException e){
			flag=false;
			System.out.println(clientID+"EOF 异常！！");		
			//ois().reset();
			//ois.close();
			return null;
		}

		
	}
	//object I/O streams lazy create
	/*private ObjectInputStream ois() throws IOException{
		if(sc==null)return null;
		
		if(ois==null)ois=new ObjectInputStream(new BlockingInputStream(sc));
		return ois;
	}
	private ObjectOutputStream oos() throws IOException{
		if(sc==null)return null;
		if(oos==null)oos=new ObjectOutputStream(new BlockingOutputStream(sc));
		return oos;
	}*/
	
	private ObjectInputStream ois() throws IOException{
		if(sock==null)return null;
		if(ois==null)ois=new ObjectInputStream(sock.getInputStream());
		return ois;
	}
	private ObjectOutputStream oos() throws IOException{
		if(sock==null)return null;
		if(oos==null){oos=new ObjectOutputStream(sock.getOutputStream());oos.flush();}
		return oos;
	}
	//client -> handler mapping, and queue flushing
	private static Map clientHandlers=new HashMap();
	/** Sends all messages in the 'outbox' queues of all ClientHandler instances in the VM to the client
	 * @throws IOException if a communication error occurs
	 */
	public static void flushAllQueues()throws IOException{
		ClientHandler ch;
		Iterator i=clientHandlers.values().iterator();
		while(i.hasNext()){
			ch=(ClientHandler)i.next();
			ch.flushQueue();
		}
	}
	private static void addHandler(String clientID, ClientHandler ch){
		clientHandlers.put(clientID, ch);
	}
	private static void removeHandler(String clientID){
		clientHandlers.remove(clientID);
	}
	/**
	 * Returns the ClientHandler associated with a given client ID
	 * @param clientID the client's ID
	 * @return the associated ClientHandler
	 */
	public static ClientHandler getHandler(String clientID){
		return (ClientHandler)clientHandlers.get(clientID);
	}

	@Override
	public void run() {
		while(flag){
			try {
				doService();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
