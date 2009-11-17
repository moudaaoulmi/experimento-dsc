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

package org.maze.eimp.app;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.UserStatus;

class BuddySorter extends ViewerSorter {

		/* (non-Javadoc)
		* @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		*/
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (e1 instanceof Buddy && e2 instanceof Buddy) {
				int e =
					getStatusPri(((Buddy) e2).getStatus())
						- getStatusPri(((Buddy) e1).getStatus());
				if (e != 0)
					return e;
			}
			return super.compare(viewer, e1, e2);
		}

		private int getStatusPri(String st) {
			if (st.equals(UserStatus.ONLINE))
				return 8;
			if (st.equals(UserStatus.OFFLINE))
				return 1;
			if (st.equals(UserStatus.INVISIBLE))
				return 0;
			if (st.equals(UserStatus.BUSY))
				return 6;
			if (st.equals(UserStatus.IDLE))
				return 6;
			if (st.equals(UserStatus.BE_RIGHT_BACK))
				return 4;
			if (st.equals(UserStatus.AWAY_FROM_COMPUTER))
				return 4;
			if (st.equals(UserStatus.ON_THE_PHONE))
				return 4;
			if (st.equals(UserStatus.ON_THE_LUNCH))
				return 4;
			return 0;
		}

	}
