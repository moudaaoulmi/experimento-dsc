package org.maze.eimp.util;

import java.util.ArrayList;

/**
 * @author hliu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class StringEx {
	//StringBuffer fTmp;
	
	public static String replace(String src,char toplace,String replace){
		StringBuffer fTmp=new StringBuffer();
		int i=0;
		int l=src.length();
		int last=0;
		for(i=0;i<l;i++){
			if(src.charAt(i)==toplace){
				fTmp.append(src.substring(last,i));
				fTmp.append(replace);
				last=i+1;
			}
		}
		fTmp.append(src.substring(last,l));
		return fTmp.toString();
		
	}
	
	public static ArrayList split(String str,char sep,char rel){
		int l=str.length();
		StringBuffer buf=new StringBuffer();
		ArrayList al=new ArrayList();
		for(int i=0;i<l;i++){
			char t=str.charAt(i);
			if(t==sep){
				al.add(buf.toString());
				buf.setLength(0);
				//buf.setLength(5);
				continue;
			}
			if(t==rel){
				if(i+1<l){
					if(str.charAt(i+1)==sep || str.charAt(i+1)==rel){
						i=i+1;
						buf.append(str.charAt(i));
						}
				}
				continue;
			}
			buf.append(t);
			if(i==l-1){
				al.add(buf.toString());
				buf.setLength(0);
				//buf.setLength(5);
				continue;
			}
		}
		return al;
	}
	
}
