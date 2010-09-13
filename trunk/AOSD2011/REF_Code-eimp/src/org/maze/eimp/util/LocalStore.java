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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.BuddyGroup;
import org.maze.eimp.im.BuddyList;
import org.maze.eimp.im.Group;
import org.maze.eimp.im.GroupList;

/**
 * This class is from jmsn!
 * 
 * @author Jang-Ho Hwang, rath@linuxkorea.co.kr
 * @version $Id: LocalStore.java,v 1.2 2003/08/12 09:15:12 loya Exp $
 */
public class LocalStore {
	public static final String DEFAULT_HOME_DIR = System
			.getProperty("user.home");
	public static final String DEFAULT_NAME = "eimp";
	// public static final File DEFAULT_HOME_DIR = new
	// File(System.getProperty("user.home"), ".eimp");
	private final File homedir;
	private File userdir = null;
	private String loginName = null;

	private Properties setting = new Properties();

	public LocalStore() {
		homedir = new File(DEFAULT_HOME_DIR, "." + DEFAULT_NAME);
		homedir.mkdirs();
	}

	public LocalStore(String name) {
		homedir = new File(DEFAULT_HOME_DIR, "." + name);
		homedir.mkdirs();
	}

	public LocalStore(File dir) {
		if (dir.isFile())
			throw new IllegalArgumentException("dir is must be directory");
		homedir = dir;
		homedir.mkdirs();
	}

	public File getHomeDirectory() {
		return this.homedir;
	}

	/**
	 * 주어진 사용자에 대한 local copy 디렉토리를 생성하고, 준비한다.
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;

		userdir = new File(homedir, loginName.toLowerCase());
		userdir.mkdirs();
	}

	public String getLoginName() {
		return this.loginName;
	}

	/**
	 * 정보를 읽어 주어진 Properties 객체에 담아준다. 만약 파일이 존재하지
	 * 않는다면, 아무런 일도 수행하지 않을 것이다.
	 * <p>
	 * 다음과 같은 정보들이 읽혀질 것이다.
	 * <ul>
	 * <li><b>SerialNumber</b> - Buddy list의 최신버젼을 나타내는 값
	 * </ul>
	 */
	public void loadInformation() {
		if (userdir == null)
			throw new IllegalStateException("required setLoginName");

		File file = new File(userdir, "setting.prop");
		if (!file.exists())
			return;

		FileInputStream fis = null;
		internalLoadInformation(file, fis);
	}

	private void internalLoadInformation(File file, FileInputStream fis) {
		fis = new FileInputStream(file);
		setting.load(fis);
		fis.close();
	}

	public void setProperty(String key, String value) {
		setting.setProperty(key, value);
	}

	public String getProperty(String key) {
		return setting.getProperty(key);
	}

	public String getProperty(String key, String def) {
		return setting.getProperty(key, def);
	}

	/**
	 * If key is undefined, this will return false.
	 */
	public boolean getPropertyBoolean(String key) {
		String value = getProperty(key);
		if (value == null)
			return false;
		return Boolean.valueOf(value).booleanValue();
	}

	public boolean getPropertyBoolean(String key, boolean def) {
		String value = getProperty(key);
		if (value == null)
			return def;
		return Boolean.valueOf(value).booleanValue();
	}

	public void storeInformation() {
		FileOutputStream fos = null;
		internalStoreInformation(fos);
	}

	private void internalStoreInformation(FileOutputStream fos) {
		fos = new FileOutputStream(new File(userdir, "setting.prop")
				.getAbsolutePath());
		setting.store(fos, "JMSN setting");
		fos.flush();
		fos.close();
		fos = null;
	}

	/**
	 *
	 */
	public void storeBuddies(BuddyGroup bg) {
		writeGroups(bg.getGroupList(), new File(userdir, "Groups.prop"));
		writeList(bg.getForwardList(), new File(userdir, "Forward"));
		writeList(bg.getReverseList(), new File(userdir, "Reverse"));
		writeList(bg.getAllowList(), new File(userdir, "Allow"));
		writeList(bg.getBlockList(), new File(userdir, "Block"));
	}

	public void loadBuddies(BuddyGroup bg) {
		readGroups(bg.getGroupList(), new File(userdir, "Groups.prop"));
		readList(bg.getForwardList(), new File(userdir, "Forward"));
		readList(bg.getReverseList(), new File(userdir, "Reverse"));
		readList(bg.getAllowList(), new File(userdir, "Allow"));
		readList(bg.getBlockList(), new File(userdir, "Block"));
	}

	private void readList(BuddyList list, File file) throws IOException {
		list.clear();
		if (!file.exists())
			return;

		FileInputStream fis = null;
		internalReadList(list, file, fis);
	}

	private void internalReadList(BuddyList list, File file, FileInputStream fis)
			throws IOException {
		fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		while ((line = br.readLine()) != null) {
			int i0 = line.indexOf(',');
			if (i0 == -1)
				continue;
			int i1 = line.indexOf(',', i0 + 1);
			if (i1 == -1)
				continue;

			String loginName = line.substring(0, i0);
			int groupIndex = Integer.parseInt(line.substring(i0 + 1, i1));
			String friendlyName = line.substring(i1 + 1);

			Buddy friend = new Buddy(loginName, friendlyName);
			friend.setGroupIndex(groupIndex);

			list.add(friend);
		}
		br.close();
		fis.close();
		fis = null;
	}

	private void writeList(BuddyList list, File file) throws IOException {
		FileOutputStream fos = null;
		internalWriteList(list, file, fos);
	}

	private void internalWriteList(BuddyList list, File file,
			FileOutputStream fos) throws FileNotFoundException, IOException {
		fos = new FileOutputStream(file);
		PrintWriter out = new PrintWriter(fos);
		for (Iterator i = list.iterator(); i.hasNext();) {
			Buddy friend = (Buddy) i.next();
			out.println(friend.getLoginName() + "," + friend.getGroupIndex()
					+ "," + friend.getFriendlyName());
		}
		out.flush();
		out.close();
		fos.close();
		fos = null;
	}

	private void writeGroups(GroupList list, File file) throws IOException {
		FileOutputStream fos = null;
		Properties prop = new Properties();
		for (Iterator i = list.iterator(); i.hasNext();) {
			Group group = (Group) i.next();
			prop.setProperty("group." + group.getIndex(), group.getName());
		}
		internalWriteGroups(file, prop, fos);
	}

	private void internalWriteGroups(File file, Properties prop,
			FileOutputStream fos) throws IOException {
		fos = new FileOutputStream(file);
		prop.store(fos, "JMSN Group cache file");
		fos.flush();
		fos.close();
		fos = null;
	}

	private void readGroups(GroupList list, File file) throws IOException {
		list.clear();
		if (!file.exists())
			return;

		FileInputStream fis = null;
		Properties prop = new Properties();

		internalReadGroups(list, file, prop, fis);
	}

	private void internalReadGroups(GroupList list, File file, Properties prop,
			FileInputStream fis) throws IOException {
		fis = new FileInputStream(file);
		prop.load(fis);

		for (Enumeration e = prop.propertyNames(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String value = MimeUtility.getURLDecodedString(prop
					.getProperty(key), "UTF-8");

			if (key.indexOf('.') == -1)
				continue;

			int index = Integer.parseInt(key.substring(key.indexOf('.') + 1));
			Group group = new Group(value, index);

			list.addGroup(group);
		}
	}
}
