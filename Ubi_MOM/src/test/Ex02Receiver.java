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

import ubimessage.client.MOMClient;
import ubimessage.message.Message;
import ubimessage.message.MessageListener;

/**
 * The second example. This demonstrates point-to-point delivery and asynchronous receive
 * This class receives messages from the dispatcher. 
 * @author Luca Cristina
 */
public class Ex02Receiver {
	public static void main(String args[]) throws Exception{
		//create a client to receive messages asynchronously
		MOMClient rcpt=new MOMClient("localhost",3009,"receiver");
		//then set the MessageListener for the client
		rcpt.setMessageListener(new MessageListener(){
			public void messageReceived(Message m){
				try{System.out.println(m.getContent());}
				catch(Exception ex){}
			}
			public void exceptionRaised(Exception ex){}
		});
		//connect to the dispatcher
		try{
			rcpt.connect();
		}catch(IOException e){
			System.out.println("io"+e.getMessage());
		}
		//
		//wait for user input
		System.in.read(new byte[10]);
		//disconnect
		//rcpt.disconnect();
	}
}
