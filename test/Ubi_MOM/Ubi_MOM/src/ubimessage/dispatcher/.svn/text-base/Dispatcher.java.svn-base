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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import test.Setting;
import ubimessage.message.Message;

/** This class represents a message dispatcher, which is a server listening on a predefined port 
 * and serving clients. A dispatcher can be active or inactive. 
 * When it is created it is inactive, and must be started with the start() method. To be executed it must be
 * wrapped in a Thread object which must be started as well. 
 * The dispatcher can be stopped with the stop(boolean) method, and then started over again. 
 * Instead, shutdown is irreversible. After shutdown a dispatcher is unusable. 
 * This class contains only connection handling logic; message management and dispatching is delegated
 * to a MessageManager instance, and service to clients to ClientHandler instances. 
 * @author Luca Cristina
 */
public class Dispatcher implements Runnable{
	public static final int DEFAULT_PORT=3009;
	public static final int DEFAULT_CONNECTION_BACKLOG=10;
	
	/*private Selector selector;
	private ServerSocketChannel serverSocketChannel;*/
	//改
	private ServerSocket serverSocket;
	private ExecutorService service;
	private boolean accept,serve, shutdown;
	private MessageManager mm;
	/**Initializes the Dispatcher*/
	public Dispatcher(){
		accept=false;
		serve=false;
		shutdown=false;
		try{init();}catch(IOException ioe){
			System.out.println("Failed dispatcher init:");
			ioe.printStackTrace();
			System.exit(1);
		}
		mm=new MessageManager();
	}
	private void init() throws IOException{
		//初始化线程池
		service = Executors.newFixedThreadPool(10);
		//selector=Selector.open();
	}
	/**Starts the dispatcher. The dispatcher will begin accepting connections and serving clients. */
	public void start() throws IOException{
		if(accept)return;
		//改
		serverSocket = new  ServerSocket(DEFAULT_PORT);
		
		/*serverSocketChannel=ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(DEFAULT_PORT),DEFAULT_CONNECTION_BACKLOG);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);*/
		serve=true;
		accept=true;
	}
	/**Stops the dispatcher. If kickClients is true, will disconnect from all clients, 
	 * otherwise will wait for all of them to disconnect normally. 
	 * In the meantime the dispatcher can be started again 
	 * and no interruption of service will occur for connected clients
	 * @param kickClients true to disconnect from all clients, false to wait for them to disconnect (TODO)
	 */
	public void stop(boolean kickClients) throws IOException{
		accept=false;
		if(kickClients){
			serve=false;
			//TODO kick clients
		}
		//改
		serverSocket.close();
		
		/*if(serverSocketChannel.isOpen()){
			serverSocketChannel.keyFor(selector).cancel();
			serverSocketChannel.close();
		}
		serverSocketChannel=null;*/
	}
	/**Stops and shuts down the dispatcher. See stop()
	 * @param kickClients true to disconnect from all clients, false to wait for them to disconnect (TODO)
	 */
	public void shutdown(boolean kickClients) throws IOException{
		shutdown=true;
		stop(kickClients);
		mm.shutdown();
	}
	/**This method performs the actual job of the Dispatcher. It accepts new connections
	 * and delegates service of clients' requests to the appropriate ClientManager. 
	 * When inactive, it tries not to waste CPU through Thread.sleep(). 
	 */
	public void run(){
		while(!shutdown){
			while(accept||serve){
				try{
					service();
					Thread.sleep(1);//减轻CPU负担
				}catch(Exception ex){
					ex.printStackTrace();
					}
			}
			try{//while inactive, don't waste CPU
				Thread.sleep(1000);
			}catch(InterruptedException ie){
				//not a problem
				
			}
		}
	}
	/**This method performs the actual job of the Dispatcher. It accepts new connections
	 * and delegates service of clients' requests to the appropriate ClientManager. 
	 */
	private void service() throws Exception{
		/*SelectionKey k;
		selector.selectNow();
		Iterator i=selector.selectedKeys().iterator();
		while(i.hasNext()){
			k=(SelectionKey)i.next();
			i.remove();
			if(k.isAcceptable()){
				addClient(serverSocketChannel.accept());
				
			}else if(k.isReadable()){
				if(k.attachment()==null)System.out.println("No handler");else
				((ClientHandler)k.attachment()).doService();
			}
			
		}
		*/
		
		try {
			Socket client = serverSocket.accept();
			System.out.println("Connection from "+client.getInetAddress()+" OK");
			
			//service.execute(new ClientHandler(this,client));
			new Thread(new ClientHandler(this,client)).start();
			//addClient(client);
			//new ClientHandler(client).start();
		} catch (IOException e) {
		}
		
	}
	/**Adds a new connection to the connections managed by the dispatcher
	 * @param sc the SocketChannel representing the connection
	 */
	/*private void addClient(SocketChannel sc) throws ClosedChannelException, IOException{
		sc.configureBlocking(false);
		SelectionKey k=sc.register(selector,SelectionKey.OP_READ);
		k.attach(new ClientHandler(this,k));
		System.out.println("Connection from "+sc.socket().getInetAddress()+" OK");
	}
	*/

	/**Notifies the dispatcher that a message has arrived. Treatment is delegated to the MessageManager. 
	 * @param m the message
	 */
	public void messageArrived(Message m){
		mm.messageArrived(m);
		try {
			System.out.println((String)m.getContent());
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	/**Notifies the dispatcher that a client has subscribed for a topic. Treatment is delegated to the MessageManager. 
	 * @param clientID the subscriber's ID
	 * @param topic the topic
	 */
	public void clientSubscribed(String clientID, String topic){
		mm.addSubscription(clientID,topic);
	}
	/**Notifies the dispatcher that a client has unsubscribed from a topic. Treatment is delegated to the MessageManager. 
	 * @param clientID the subscriber's ID
	 * @param topic the topic
	 */
	public void clientUnsubscribed(String clientID, String topic){
		mm.removeSubscription(clientID,topic);
	}
	/**Notifies the dispatcher that a client has unsubscribed from all topics. Treatment is delegated to the MessageManager. 
	 * @param clientID the subscriber's ID
	 */
	public void clientUnsubscribedAll(String clientID){
		mm.removeAllSubscriptions(clientID);
	}
	/**Notifies the dispatcher that a client has left the dispatcher. No action taken for now. 
	 * @param clientID the client's ID
	 */
	public void clientDisconnected(String clientID){
		System.out.println("Disconnected: "+clientID);
	}
}
