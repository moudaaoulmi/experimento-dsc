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

package org.maze.eimp.yahoo;

/**
 * Util class for the yahoo protocol
 */
public class Util {

	/**
	 * Trim the html tag form a string.
	 * Ex. 
	 *  input - "<dd>sample</dd>"
	 *  output - "sample" 
	 * @param s
	 * @return
	 */
	public static String trimHtmlTag(String s) {
		StringBuffer buf = new StringBuffer();
		StringBuffer buf2 = new StringBuffer();
		int is = -1;
		int ie = 0;
		int i = 0;
		int l = s.length();
		for (i = 0; i < l; i++) {
			char c = s.charAt(i);
			if (c == '<') {
				is = i;
				buf.append(buf2);
			} else {

				if (c == '>') {
					if (is > -1) {
						//ignore the buf2
						buf2.setLength(0);
					}
				} else {
					buf2.append(c);
				}
			}
		}
		buf.append(buf2);
		return buf.toString();
	}

	//Borrow from jymsg9
	synchronized static void dump(byte[] array) {
		String s, c = "";
		for (int i = 0; i < array.length; i++) {
			s = "0" + Integer.toHexString((int) array[i]);
			System.out.print(s.substring(s.length() - 2) + " ");
			if ((int) array[i] >= ' ' && (int) array[i] <= '~')
				c = c + (char) array[i];
			else
				c = c + ".";
			if ((i + 1) == array.length) {
				while ((i % 20) != 19) {
					System.out.print("   ");
					i++;
				}
			}
			if ((((i + 1) % 20) == 0) || ((i + 1) >= array.length)) {
				System.out.print(" " + c + "\n");
				c = "";
			}
		}
	}
	//	Borrow from jymsg9
	static void dump(byte[] array, String s) {
		System.out.println(
			s
				+ "\n01-02-03-04-05-06-07-08-09-10-11-12-13-14-15-16-17-18-19-20");
		dump(array);
	}
}
