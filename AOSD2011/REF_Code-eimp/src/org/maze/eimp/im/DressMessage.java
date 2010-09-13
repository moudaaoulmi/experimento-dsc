/*
 * Created on 2003-8-29
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.maze.eimp.im;

/**
 * @author loya
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DressMessage extends MimeMessage {
	
	private Buddy buddy;

	/**
	 * @return
	 */
	public Buddy getBuddy() {
		return buddy;
	}

	/**
	 * @param buddy
	 */
	public void setBuddy(Buddy buddy) {
		this.buddy = buddy;
	}

	/**
	 * @param str
	 */
	public DressMessage(String str) {
		super(str);
		buddy=null;
	}
	
}
