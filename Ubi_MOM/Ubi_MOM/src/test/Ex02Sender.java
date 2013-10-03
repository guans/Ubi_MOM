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

 * Created on 18-oct-2005
 * 21-oct-2005: added copyright notice
 */

package test;

import java.io.IOException;
import java.util.Random;

import ubimessage.client.MOMClient;
import ubimessage.message.Message;

/**
 * The second example. This demonstrates point-to-point delivery and asynchronous receive
 * This class sends some messages to a single client. 
 * @author Luca Cristina
 */
public class Ex02Sender {
	public static void main(String args[]) throws Exception{
		//create a client to send messages
		MOMClient sender=new MOMClient("localhost",3009,"sender");
		//then connect it to the dispatcher
		sender.connect();
		Thread.sleep(3000);
		//now send multiple messages to receiver with different contents
		Message m;
		boolean flag = true;
		while(flag){
		//for(int x=0;x<300;x++){
			
			try {
				m=new Message();
				m.setRecipient("receiver");
				m.setSender("sender");
				m.setContent("New Message "+new Random().nextInt(300));
				sender.sendMessage(m);
			} catch (IOException e) {
				flag=false;
				return ;
				//e.printStackTrace();
			}
			Thread.sleep(800);
		}
		//disconnect from the publisher
		//sender.disconnect();
	}
}
