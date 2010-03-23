/*
 * Created on 13-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.builders.latexlogparser;

/**
 * @author ish
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Entry {
	Entry(int i, String s, String s1, int j)
		{
			type = i;
			msg = s;
			filename = s1;
			lineno = j;
		}

		Entry(int i, String s, String s1)
		{
			type = i;
			msg = s;
			filename = s1;
			lineno = 1;
		}

		public boolean equals(Object obj)
		{
			if(obj instanceof Entry)
			{
				Entry entry = (Entry)obj;
				return type == entry.type && lineno == entry.lineno && filename.equals(entry.filename) && msg.equals(entry.msg);
			} else
			{
				return false;
			}
		}

public String toString()
		{
			return "Type: "+type+" Message: "+msg+" Line: "+lineno+" Location: "+filename;
}

		public static final int FONT_ERROR = 0;
		public static final int FONT_WARNING = 1;
		public static final int UNDERFULL_HBOX = 2;
		public static final int OVERFULL_HBOX = 3;
		public static final int VBOX = 4;
		public static final int FLOAT_WARNING = 5;
		public static final int NOFILE_ERROR = 6;
		public static final int REFERENCE_ERROR = 7;
		public static final int LATEX_WARNING = 8;
		public static final int FIXME_NOTE = 30;
		public static final int FIXME_WARNING = 31;
		public static final int FIXME_ERROR = 32;
		public static final int SEPARATOR = 100;
		public static final int ERRORMESSAGE = 101;
		public int type;
		public String msg;
		public String filename;
		public int lineno;

}
