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

/**
 *
 * @author loya
 * @version $Id: GroupList.java,v 1.1 2003/05/13 06:31:47 loya Exp $ 
 */
public class GroupList
{
	private final List list;
	private final Map map;

	GroupList()
	{
		list = Collections.synchronizedList( new ArrayList() );
		map = Collections.synchronizedMap( new HashMap() );
	}

	public void addGroup( Group group )
	{
		Object o = map.put( group.getIndex(), group );
		if( o==null )
			list.add( group );
		else
		{
			Group grp = (Group)o;
			grp.setName( group.getName() );
		}
	}

	public Group getGroup( int index )
	{
		return getGroup( new Integer(index) );
	}

	public Group getGroup( Integer index )
	{
		return (Group)map.get(index);
	}

	public void removeGroup( int index )
	{
		removeGroup( new Integer(index) );
	}

	public void removeGroup( Integer index )
	{
		Object o = map.remove( index );
		if( o==null )
			throw new IllegalArgumentException( "no group in " + index );
		list.remove( o );
	}

	public Iterator iterator()
	{
		return list.iterator();
	}	

	public synchronized void sort()
	{
		Collections.sort( list, new Comparator() {
			public int compare( Object o1, Object o2 )
			{
				Group g0 = (Group)o1;
				Group g1 = (Group)o2;
				return g0.getFormattedName().compareTo(
					g1.getFormattedName() );
			}
			public boolean equals( Object o ) { return false; }
		});	
	}

	public void clear()
	{
		list.clear();
		map.clear();
	}
}
