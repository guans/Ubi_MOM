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


import ubimessage.dispatcher.Dispatcher;


/**
 * The second example. This demonstrates point-to-point delivery and asynchronous receive
 * This class starts the dispatcher. 
 * @author Luca Cristina
 */
public class Ex02Dispatcher {
	public static void main(String args[]) throws Exception{
		//first, create a message dispatcher and start it
		Dispatcher disp=new Dispatcher();
		
		//修改
		disp.clientSubscribed(Setting.WEB_ENGINE_ID, "web");  //定位中端订阅
		
		disp.start();
		//then create a new Thread to execute it
		new Thread(disp,"dispatcher").start();

		//wait for user input
		System.in.read(new byte[10]);
		//and stop the dispatcher
		disp.shutdown(true);
	}
}
