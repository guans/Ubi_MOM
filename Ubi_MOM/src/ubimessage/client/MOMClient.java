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
package ubimessage.client;

import java.net.*;
import java.util.*;
import java.io.*;

import ubimessage.message.*;
import ubimessage.*;
import ubimessage.message.MessageListener;

/** This class represents a MessageIT client. A Client instance sends and receives messages and subscribes to topics, 
 * and has a client ID which must be unique in the dispatcher. 
 * Messages can be received in two ways: synchronously through the receive() method, 
 * or asynchronously by registering a messageit.message.MessageListener instance with the Client instance. 
 * The MessageListener interface defines the onMessage(Message) method, which the Client calls every time that a message is received. 
 * Asynchronous receive is recommended: the receive() method is blocking, and if a Client both sends and receives messages
 * deadlocks can occur. Since it is always possible to send a message to an arbitrary client through its client ID
 * it's best to use anync reecive. Moreover, don't use the two methods at the same time, 
 * or synschonous receives will block indefinitely
 * @author Luca Cristina
 */
public class MOMClient implements MessageITProtocolConstants{
	public static final int DEFAULT_PORT=3009;

	private String clientID=null;
	private String host=null;
	private int port=DEFAULT_PORT;
	private Socket sock=null;
	private ObjectInputStream ois=null;
	private ObjectOutputStream oos=null;
	private MessageReceiverThread mrt;

	private MessageListener ml=null;

	/** Sets up this Client to connect to a dispatcher on the local machine on the default port with 'dummy' as client ID. */
	public MOMClient(){
		this("localhost",DEFAULT_PORT,"nobody");
	}
	/** Sets up this Client for connection
	 * @param host the host name or IP to connect to
	 * @param port the port to connect to
	 * @param clientID the client ID to use
	 * @throws NullPointerException if the client ID is null
	 */
	public MOMClient(String host, int port, String clientID){
		if(clientID==null)throw new NullPointerException("Null client ID");
		this.host=host;
		this.port=port;
		this.clientID=clientID;
		mrt=new MessageReceiverThread();
	}
	/** Returns the client's ID
	 * @return the client ID
	 */
	public String getClientID(){return clientID;}
	/**
	 * @param clientID The clientID to set.
	 */
	public void setClientID(String clientID) {
		if(clientID==null)throw new NullPointerException("Null client ID");
		ensureOffline();
		this.clientID = clientID;
	}
	/**
	 * @return Returns the host.
	 */
	public String getHost(){return host;}
	/**
	 * @param host The host to set.
	 */
	public void setHost(String host){
		ensureOffline();
		this.host = host;
	}
	/**
	 * @return Returns the port.
	 */
	public int getPort(){return port;}
	/**
	 * @param port The port to set.
	 */
	public void setPort(int port) {
		ensureOffline();
		this.port = port;
	}
	/** Connects to the dispatcher. If the client is already connected, nothing happens
	 * @throws IOException if a communication error occurs
	 * @throws messageit.MessageITException if the client ID already exists in the dispatcher
	 */
	public void connect() throws IOException, MessageITException{
		if(online())return;
		sock=new Socket(host,port);
		oos();
		ois();
		//we are connected. Now register.
		oos().writeObject(MSGIT_CLIENT+getClientID());//I'm a regular client, my name is ... 
		
		int code=ois().readInt();//Is that good?
		
		
		oos().writeObject(null);
		if(code!=0){
			disconnect();
			throw new MessageITException("Conection failed: client ID already exists");
		}
		mrt=new MessageReceiverThread();
		new Thread(mrt).start();
	}
	/** Disconnects from the dispatcher. If the client is not connected nothing happens
	 * @throws IOException if a communication error occurs
	 */
	public void disconnect() throws IOException{
		if(!online())return;
		oos().writeObject(MSGIT_END);
		//the server closes the socket
		sock=null;
		ois=null;
		oos=null;
		mrt.active=false;
		mrt=null;
	}
	/**Returns the client's connection status
	 * @return true if the client is connected to a dispatcher, false otherwise
	 */
	public boolean online(){return sock!=null;}
	/** Sends a message. At least one between recipient and topic must be set. 
	 * If a recipient is set, the message is delivered only to the client with equal ID;
	 * otherwise it is sent to all clients who have subscribed to the message's topic. 
	 * @param msg the message to be sent
	 * @throws IOException if a communication error occurs
	 */
	public void sendMessage(ubimessage.message.Message msg) throws IOException{
		if(msg.getTopic()==null && msg.getRecipient()==null)
			throw new RuntimeException("No recipient and no topic set for message");
		oos.writeObject(msg);
	//	oos.flush();
		
	
	//	oos.writeObject(null);
	}
	
	/** Synchronously receives a message, or throws an exception from the dispatcher, 
	 * whichever comes. Not recommended. 
	 * @return the message received, if the dispatcher sent a message
	 * @throws the exception sent by the dispatcher
	 */
	public Message receiveMessage()throws Exception{
		Object o=mrt.getMessage();
		if(o instanceof Exception)throw (Exception)o;
		return (Message)o;
	}
	/** Ensures that the client is online. If it is, nothing happens; if not, a RuntimeException is thrown. 
	 * @throws RuntimeException if the client is offline
	 */
	public void ensureOnline(){
		if(!online())
			throw new RuntimeException("Operation impossible when offline, connect to publisher first");
	}
	/** Ensures that the client is offline. If it is, nothing happens; if not, a RuntimeException is thrown. 
	 * @throws RuntimeException if the client is online
	 */
	public void ensureOffline(){
		if(online())
			throw new RuntimeException("Operation not allowed when online, disconnect from publisher first");
	}
	/** Subscribes a topic on the dispatcher. From now on, 
	 * every message sent to the dispatcher by any client, with this topic and no recipient
	 * will be sent to this client. 
	 * @param topic the name of the topic to subscribe
	 * @throws IOException if a communication error occurs
	 */
	public void addTopic(String topic) throws IOException{
		ensureOnline();
		oos().writeObject(MSGIT_SUBSCRIBE+topic);
	}
	/** Unsubscribes a topic on the dispatcher. 
	 * @param topic the name of the topic to unsubscribe
	 * @throws IOException if a communication error occurs
	 */
	public void removeTopic(String topic) throws IOException{
		ensureOnline();
		oos().writeObject(MSGIT_UNSUBSCRIBE+topic);
	}
	/** Unsubscribes all topics on the dispatcher. 
	 * @throws IOException if a communication error occurs
	 */
	public void removeAllTopics() throws IOException{
		ensureOnline();
		oos().writeObject(MSGIT_UNSUBSCRIBE);
	}

	//object I/O streams lazy create
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

	/** Sets the MessageListener associated with this Client. From now on, the client will notify 
	 * received messages and exceptions to it. Every Client can have only one MessageListener at a time. 
	 * @param messageListener the new MessageListener (can be null)
	 */
	public void setMessageListener(MessageListener messageListener){ml=messageListener;}

	/** A Thread responsible for receiving messages and exceptions from the dispatcher's socket
	 * and passing them to the actual handlers. 
	 */
	class MessageReceiverThread implements Runnable{
		boolean active=true;
		ArrayList queue=new ArrayList();
		public void run(){
			Object o;
			while(active){
				try{
					o=ois.readObject();
					treat(o);
				}catch(EOFException ex){active=false;}
				catch(Exception ex){treat(ex);}
			}
		}
		public Object getMessage(){
			while(queue.isEmpty())Thread.yield();
			return queue.remove(0);
		}
		private void treat(Object o){
			if(ml!=null){
				if(o instanceof Message)ml.messageReceived((Message)o);
				else if(o instanceof Exception)ml.exceptionRaised((Exception)o);
			}else queue.add(o);
		}
	}
}
