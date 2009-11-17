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

package org.maze.eimp.im;

/**
 *
 * @author loya
 * @version $Id: BuddyGroup.java,v 1.1 2003/05/13 06:31:47 loya Exp $ 
 */
public class BuddyGroup
{
	private final BuddyList listForward;
	private final BuddyList listAllow;
	private final BuddyList listBlock;
	private final BuddyList listReverse;
	private final GroupList groupList;

	protected BuddyGroup()
	{
		listForward = new BuddyList("FL");
		listAllow = new BuddyList("AL");
		listBlock = new BuddyList("BL");
		listReverse = new BuddyList("RL");

		groupList = new GroupList();
	}

	public GroupList getGroupList()
	{
		return this.groupList;
	}

	public BuddyList getForwardList()
	{
		return this.listForward;
	}

	public BuddyList getAllowList()
	{
		return this.listAllow;
	}

	public BuddyList getBlockList()
	{
		return this.listBlock;	
	}

	public BuddyList getReverseList()
	{
		return this.listReverse;
	}

	public BuddyList getListAsCode( String code )
	{
		if( code.equals("FL") )
			return listForward;
		else
		if( code.equals("AL") )
			return listAllow;
		else
		if( code.equals("BL") )
			return listBlock;
		else
		if( code.equals("RL") )
			return listReverse;
		return null;
	}

	public void clear()
	{
		this.listAllow.clear();
		this.listBlock.clear();
		this.listForward.clear();
		this.listReverse.clear();
	}

	public static BuddyGroup getInstance()
	{
		return new BuddyGroup();
	}
}
