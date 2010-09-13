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
package org.maze.eimp.app.action;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.maze.eimp.Environment;
import org.maze.eimp.app.SimpleIMWindow;
/**
 * Perference action
 */
public class PerferenceAction extends Action {
	SimpleIMWindow w;
	public PerferenceAction(SimpleIMWindow w) {
		this.w = w;
		setText(Messages.getString("PerferenceAction.M_Preference")); //$NON-NLS-1$
		setToolTipText(Messages.getString("PerferenceAction.D_Preference")); //$NON-NLS-1$
		//setImageDescriptor(ImageManager.getImageDescriptor(ImageManager.IMG_LOGIN));
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		//		AppPerfDialog pd = new AppPerfDialog(w.getShell());
		//		pd.open();
		PreferenceManager pm = new PreferenceManager();
		PreferenceNode pnGeneral = new PreferenceNode("eimp.pref.general", //$NON-NLS-1$
				Messages.getString("PerferenceAction.General"), null, //$NON-NLS-1$
				"org.maze.eimp.perf.PrefGeneralPage"); //$NON-NLS-1$
		pm.addToRoot(pnGeneral);
		PreferenceNode pnAccount = new PreferenceNode("eimp.pref.accounts", //$NON-NLS-1$
				Messages.getString("PerferenceAction.Accounts"), null, //$NON-NLS-1$
				"org.maze.eimp.perf.PrefAccountPage"); //$NON-NLS-1$
		pm.addToRoot(pnAccount);
		PreferenceNode pnAppearance = new PreferenceNode(
				"eimp.pref.appearance", //$NON-NLS-1$ 
				Messages.getString("PerferenceAction.Appearance"), null, //$NON-NLS-1$
				"org.maze.eimp.perf.PrefAppearancePage"); //$NON-NLS-1$
		pm.addToRoot(pnAppearance);
		PreferenceNode pnSounds = new PreferenceNode("eimp.pref.sounds", //$NON-NLS-1$
				Messages.getString("PerferenceAction.Sounds"), null, //$NON-NLS-1$
				"org.maze.eimp.perf.PrefSoundsPage"); //$NON-NLS-1$
		pm.addToRoot(pnSounds);
		PreferenceDialog pd = new PreferenceDialog(w.getShell(), pm);
		pd.setPreferenceStore(Environment.getInstance().getPrefStore());
		pd.open();
	}
}