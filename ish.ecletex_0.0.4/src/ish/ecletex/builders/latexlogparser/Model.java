/*
 * Created on 13-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.builders.latexlogparser;

import java.util.LinkedList;

/**
 * @author ish
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Model {
	Model()
		{
			statistics = new int[102];
			list = new LinkedList();
		}

		void add(Entry entry)
		{
			list.add(entry);
			statistics[entry.type]++;
		}

		void addOnce(Entry entry)
		{
			if(list.indexOf(entry) == -1)
			{
				list.add(entry);
				statistics[entry.type]++;
			}
		}

		void clear()
		{
			list.clear();
			for(int i = 0; i < statistics.length; i++)
				statistics[i] = 0;

		}

		public LinkedList get()
		{
			return list;
		}

		public String toString()
		{
			String s = "";
			for(int i = 0; i < statistics.length; i++)
				s = s + i + " " + statistics[i] + "\n";

			return s;
		}

		int statistics[];
		LinkedList list;

}
