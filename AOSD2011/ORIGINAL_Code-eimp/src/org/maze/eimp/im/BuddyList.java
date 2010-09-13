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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class BuddyList
{
	private final String name;
	private List list = null;
	private Map map = null;

	/**
	 * 
	 * @param name the name of this group
	 */
	public BuddyList( String name )
	{
		this.name = name;

		this.list = Collections.synchronizedList( new ArrayList() );
		this.map = Collections.synchronizedMap( new HashMap() );
	}

	public String getName()
	{
		return this.name;
	}

	/**
	 * add a buddy
	 * @param friend
	 */
	public Buddy add( Buddy friend )
	{
	Buddy buddy;
		String loginName = friend.getLoginName();
		Object o = null;
		if( (o=map.get(loginName))!=null )
		{
			buddy = (Buddy)o;
			buddy.setFriendlyName( friend.getFriendlyName() );
			buddy.setStatus( friend.getStatus() );
					}
		else
		{
			list.add( friend );
			map.put( loginName, friend );
			buddy = friend;
		}
		return buddy;
	}
	
	/**
	 * remove a buddy
	 * @param friend
	 */
	public void remove( Buddy friend )
	{
		list.remove( friend );
		map.remove( friend.getLoginName() );
	}

	public void remove( String loginName )
	{
		Object o = map.remove(loginName);
		if( o!=null )
			list.remove( o );
	}

	public Buddy get( int index )
	{
		return (Buddy)list.get(index);
	}

	public Buddy get( String loginName )
	{
		return (Buddy)map.get( loginName );
	}


	public void set( Buddy friend )
	{
		Buddy mf = get(friend.getLoginName());
		if( mf!=null )
		{
			mf.setFriendlyName( friend.getFriendlyName() );
			mf.setStatus( friend.getStatus() );
		}
		else
			throw new IllegalArgumentException( friend.getLoginName() + " not found on " + getName() );
	}

	/**
	 * change to offline.
	 */
	public void setOffline( String loginName )
	{
		Buddy mf = get(loginName);
		if( mf!=null )
			mf.setStatus( UserStatus.OFFLINE );
		else
			throw new IllegalArgumentException( loginName + " not found on " + getName() );
	}

	public Iterator iterator()
	{
		return list.iterator();
	}

	public int size()
	{
		return list.size();
	}

	public synchronized void sort( Comparator comp )
	{
	    Collections.sort( list, comp );
	}

	public void clear()
	{
		list.clear();
		map.clear();
	}
	
	public Object[] toArray(){
		return list.toArray();
	}
}
