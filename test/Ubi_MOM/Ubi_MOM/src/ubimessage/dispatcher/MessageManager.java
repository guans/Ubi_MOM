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

 * Created on 27-sep-2005
 * 21-oct-2005: added copyright notice
 */
package ubimessage.dispatcher;

import java.util.*;

import ubimessage.message.Message;

/** This class is responsible for managing messages. Messages are accepted in an incoming queue, 
 * dispatched to the outgoing queues of the proper recipient clients, and then sent. 
 * The manager also manages topics and subscriptions to topics. 
 * A topic has an associated set of subscribers, initially empty. Later clients can subscribe 
 * and unsubscribe it. 
 * @author Luca Cristina
 */
public class MessageManager {
	private Map subscriptions;
	private MessageQueue incoming;
	private MessageManagerThread mmt;

	private Map  monitor ;//监控客户端和其需要监控的列表
	private Map  monitored;//某个客户端被哪个客户端监控
	/** 
	 * Initializes the MessageManager and the associated dispatching thread
	 */
	public MessageManager(){
		subscriptions=new HashMap();
		incoming=new MessageQueue();
		
		//------------------
		monitor = new HashMap();
		monitored = new HashMap();
		//-------------------
		
		mmt=new MessageManagerThread();
		new Thread(mmt,"message manager").start();
	}
	/** 
	 * Shuts down the manager and thread
	 */
	public void shutdown(){
		mmt.active=false;
	}
	/** Method used to notify the manager that a new Message has arrived. 
	 * The message is queued to be dispatched later. 
	 */
	public void messageArrived(Message msg){
		incoming.insert(msg);
	}
	/** Adds a topic to the manager. 
	 * @param topic the topic to add
	 */
	public void addTopic(String topic){
		if(topicExists(topic))return;
		subscriptions.put(topic,new ArrayList());
	}
	/** Removes a topic from the manager. 
	 * @param topic the topic to add
	 */
	public void removeTopic(String topic){
		if(!topicExists(topic))return;
		subscriptions.remove(topic);
	}
	/** Checks if a topic exists in the manager. 
	 * @param topic the topic to look for
	 * @return true if the topic exists
	 */
	public boolean topicExists(String topic){
		return subscriptions.containsKey(topic);
	}
	/** Adds a subscription to a given topic for a given client
	 * @param clientID the subscriber's ID
	 * @param topic the topic's name
	 */
	public void addSubscription(String clientID, String topic){
		addTopic(topic);
		ArrayList a=(ArrayList)subscriptions.get(topic);
		a.add(clientID);
	}
	/** Removes a subscription to a given topic for a given client
	 * @param clientID the subscriber's ID
	 * @param topic the topic's name
	 */
	public void removeSubscription(String clientID, String topic){
		ArrayList a=(ArrayList)subscriptions.get(topic);
		if(a==null)return;//unsubscribed an unknown topic
		a.remove(clientID);
		if(a.isEmpty())removeTopic(topic);
	}
	/** Removes all subscriptions for a given client
	 * @param clientID the subscriber's ID
	 */
	public void removeAllSubscriptions(String clientID){
		Iterator i=subscriptions.keySet().iterator();
		while(i.hasNext())removeSubscription(clientID, (String)i.next());
		//doesn't remove empty topics, not a problem for now
		//TODO check
	}
	/** This Runnable performs the actual job of dispatching messages. 
	 */
	private class MessageManagerThread implements Runnable{
		boolean active=true;
		/**
		 * Main dispatching loop. While it runs, it extracts messages from the incoming queue 
		 * and adds them to the appropriate clients' queues through the respective 
		 * ClientHandler instances. When no message is coming, it instructs
		 * all ClientHandlers to send all the messages they have in queue. 
		 * This method is to be called by the Thread running this Runnable. Do not call. 
		 */
		public void run(){
			while(active){
				while(!incoming.isEmpty()) {
					dispatchIncoming();
				}
				try{
					ClientHandler.flushAllQueues();
					
				}catch(Exception ex){
					ex.printStackTrace();
				}
				try{Thread.sleep(1000);}catch(InterruptedException ie){ie.printStackTrace();}
			}
		}
		private void dispatchIncoming(){
			Message m;
			m=incoming.extract();
			if(m.getRecipient()!=null)
				enqueue(m,m.getRecipient());
			//else{
			/*else if(m.getRecipient()==null && m.getTopic()==null){
				throw new RuntimeException("No recipient and no topic for message");				
			} */
			if(m.getTopic()!=null){
				//修改部分---------------------------------------------------------------
				//当传入待监控客户端列表时
				if(m.getTopic().equals("monitor")) {
					MessageParser mp = new MessageParser(m);
					//从消息中解析出待监控客户端列表
					ArrayList<String> clientList = mp.parserMonitoredClient();
					//获取监控客户端ID
					String monitorClient = m.getSender();
					//监控客户端 和 其监控的目标客户端列表
					List clients = (List) monitor.get(monitorClient);
					if(clients!=null)  {//若监控列表已存在,则在基础上继续添加
						for(String client:clientList) {
							clients.add(client);
						}
					} else {//若是第一次添加该监控
						monitor.put(monitorClient, clientList);
					}
				
					//设置指定客户端被哪个监控客户端监控
					for(String cl:clientList){
						monitored.put(cl, monitorClient);
					}
				}
				//当传入的是坐标信息时，判断分发,
				if(m.getTopic().equals("coordinate")) {
					//判断是否有监控客户端监控
					//MessageParser mp = new MessageParser(m);
					//获取与坐标对应的目标客户端，存放于Message中
					//String targetClient = mp.parserClient();
					//String targetClient = m.getRecipient();
					String targetClient = m.getHeader("target");
					//消息发给目标客户端
					if(targetClient!=null) {
						//先将坐标发送给该客户端
						//enqueue(m,targetClient);
						//获取该目标客户端是否有客户端监控
						String monitorClient = (String)monitored.get(targetClient);
						//若该目标客户端存在,则将该坐标信息发送给监控客户端
						if(monitorClient!=null) {
							enqueue(m,monitorClient);
						}
					}
					
				}
				//结束-------------------------------------------------------------
				
				ArrayList subscribers=(ArrayList)subscriptions.get(m.getTopic());
				if(subscribers==null){
					return;
				}
				Iterator i=subscribers.iterator();
				while(i.hasNext())
					enqueue(m,(String)i.next());
			}
		}
		private void enqueue(Message m, String clientID){
			if(ClientHandler.getHandler(clientID)!=null)
				ClientHandler.getHandler(clientID).enqueueMessage(m);
			else{
				//TODO no such recipient - no action for now - check
			}
		}
	}
}
