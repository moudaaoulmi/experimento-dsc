/*******************************************************************************
 * Copyright (c) 2003, Loya Liu
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright notice, 
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice, 
 *     this list of conditions and the following disclaimer in the documentation 
 *     and/or other materials provided with the distribution.
 *   - Neither the name of the MAZE.ORG nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 *******************************************************************************/

package org.maze.eimp.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.DressMessage;
import org.maze.eimp.im.IMAdapter;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.Session;

/**
 * @author loya
 */
public class MessageArchiver extends IMAdapter {
	HashMap sessToList;

	/* (non-Javadoc)
	 * @see org.maze.eimp.im.IMListener#instantMessageReceived(org.maze.eimp.im.Session, org.maze.eimp.im.Buddy, org.maze.eimp.im.MimeMessage)
	 */
	public void instantMessageReceived(
		Session ss,
		Buddy friend,
		MimeMessage mime) {
		DressMessage m = new DressMessage(mime.getMessageString());
		m.setTime(mime.getTime());
		Buddy b = new Buddy(friend.getLoginName());
		b.setFriendlyName(friend.getFriendlyName());
		m.setBuddy(b);
		getList(ss.getId()).add(m);
	}

	public MessageArchiver() {
		sessToList = new HashMap();
	}

	public void remove(String sessid) {
		if (sessToList.containsKey(sessid)) {
			sessToList.remove(sessid);
		}
	}
	
	public Set getSessionIdList(){
		return sessToList.keySet();
	}
	public ArrayList getMessageList(String sessid){
		return (ArrayList)sessToList.get(sessid);
	}

	/**
	 * save as txt format
	 * @param sessID
	 * @param path
	 * @throws IOException
	 */
	public void saveTXT(String sessID, String path) throws IOException {
		FileWriter f = new FileWriter(path);
		ArrayList l = (ArrayList) sessToList.get(sessID);
		for (Iterator i = l.iterator(); i.hasNext();) {
			DressMessage m = (DressMessage) i.next();
			f.write(m.getBuddy().getLoginName());
			f.write("(");
			f.write(m.getBuddy().getFriendlyName());
			f.write(")");
			f.write("[");
			f.write(m.getTime().toString());
			f.write("]\n");
			f.write(m.getMessageString());
			f.write("\n");
		}
		f.close();
	}

	/**
	 * save as xml format
	 * @param sessID
	 * @param path
	 * @throws IOException
	 */
	public void save(String sessID, String path) throws IOException {
		FileWriter f = new FileWriter(path);
		ArrayList l = (ArrayList) sessToList.get(sessID);
		f.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		f.write("<session>\n");
		for (Iterator i = l.iterator(); i.hasNext();) {
			DressMessage m = (DressMessage) i.next();
			f.write("<msg time=\"");
			f.write(m.getTime().toString());
			f.write("\">");
			f.write("<buddy id=\"");
			f.write(m.getBuddy().getLoginName());
			f.write("\" friendlyname=\"");
			f.write(m.getBuddy().getFriendlyName());
			f.write("\"/>");
			f.write(m.getMessageString());
			f.write("</msg>\n");
		}
		f.write("</session>\n");
		f.close();
	}

	private ArrayList getList(String sessID) {
		if (sessToList.containsKey(sessID)) {
			return (ArrayList) sessToList.get(sessID);
		} else {
			ArrayList l = new ArrayList();
			sessToList.put(sessID, l);
			return l;
		}
	}
}
