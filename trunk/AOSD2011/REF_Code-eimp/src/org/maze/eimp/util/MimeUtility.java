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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
/**
 * This class is from jmsn!
 *
 * @author Jang-Ho Hwang, rath@linuxkorea.co.kr
 * @version $Id: MimeUtility.java,v 1.1 2003/06/29 04:00:03 loya Exp $
 */
public class MimeUtility
{
	public static String getURLEncodedString( String str )
		throws UnsupportedEncodingException
	{
		return getURLEncodedString( str, System.getProperty("file.encoding") );
	}

	public static String getURLEncodedString( String str, String encoding )
		throws UnsupportedEncodingException
	{
		StringBuffer sb = new StringBuffer();
		byte[] b = str.getBytes(encoding);
		for(int i=0; i<b.length; i++)
		{
			sb.append( "%" );
			int value = (int)(b[i] < 0 ? b[i]+0x100 : b[i]);
			String hexa = Integer.toHexString(value);
			if( hexa.length()==1 )
				sb.append("0");
			sb.append( hexa.toUpperCase() );
		}
		return sb.toString();
	}

	public static String getURLDecodedString( String str )
		throws UnsupportedEncodingException
	{
		return getURLDecodedString( str, System.getProperty("file.encoding") );
	}

	public static String getURLDecodedString( String str, String encoding )
		throws UnsupportedEncodingException
	{
		StringBuffer sb = new StringBuffer();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(64);
		boolean isURLEncode = false;
		for(int i=0, len=str.length(); i<len; i++)
		{
		    if( str.charAt(i)=='%' )
			{
				int v = Integer.valueOf(str.substring(i+1, i+3), 0x10).intValue();
				bos.write( v );
				isURLEncode = true;
				i+=2;
			}
			else
			{
				if( isURLEncode )
				{
					isURLEncode = false;
					sb.append( new String(bos.toByteArray(), encoding) );
					bos.reset();
				}
				sb.append( str.charAt(i) );
			}
		}
		if( isURLEncode )
			sb.append( new String(bos.toByteArray(), encoding) );
		return sb.toString();
	}
};
