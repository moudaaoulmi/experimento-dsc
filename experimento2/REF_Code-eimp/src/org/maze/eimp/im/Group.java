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
 * Group
 * 
 * @author loya
 * @version $Id: Group.java,v 1.1 2003/05/13 06:31:47 loya Exp $ 
 */
public class Group
{
	private String name = null;
	private String fName = null;
	private Integer index;

	public Group( String name )
	{
		this.name = name;
	}

	public Group( String name, int index )
	{
		this( name, new Integer(index) );
	}

	public Group( String name, Integer index )
	{
		setName( name );
		setIndex( index );
	}

	public void setName( String name )
	{
		this.name = name;
		if( name!=null )		
			//fName = StringUtil.replaceString(this.name, "%20", " ");
			fName=name;
	}

	public String getName()
	{
		return this.name;	
	}

	public String getFormattedName()
	{
		return this.fName;
	}

	public void setIndex( int index )
	{
		this.index = new Integer(index);
	}

	public void setIndex( Integer index )
	{
		this.index = index;
	}

	public Integer getIndex()
	{
		return this.index;
	}

	public int getIndexInt()
	{
		if( index==null )
			return -1;
		return this.index.intValue();
	}

	public boolean equals( Object o )
	{
		if( this==o ) return true;
		if( o==null ) return false;

		if( o instanceof Group )
		{
			Group g = (Group)o;
			return g.index.equals(this.index);
		}
		return false;
	}
	
	public int hashCode()
	{
		return this.index.intValue();
	}

	public String toString()
	{
		return fName;	
	}
}
