//============================================================================
//
// Copyright (C) 2002-2007  David Schneider, Lars K�dderitzsch
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//============================================================================

package com.atlassw.tools.eclipse.checkstyle.quickfixes;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

/**
 * This action tries to run all quickfixes for markers on a selected compilation
 * unit.
 * 
 * @author Lars K�dderitzsch
 */
public class FixCheckstyleMarkersAction implements IObjectActionDelegate
{

    private GeneralException generalException = new GeneralException();
    
    /** the selection that occured in the workspace. */
    private ISelection mSelection;

    /** the active workbench part. */
    private IWorkbenchPart mWorkBenchPart;

    /**
     * {@inheritDoc}
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        mSelection = selection;
    }

    /**
     * {@inheritDoc}
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart)
    {
        mWorkBenchPart = targetPart;
    }

    /**
     * {@inheritDoc}
     */
    public void run(IAction action)
    {

        IStructuredSelection selection = null;
        if (mSelection instanceof IStructuredSelection)
        {
            selection = (IStructuredSelection) mSelection;
        }

        // no valid selection
        if (selection == null || selection.size() != 1)
        {
            return;
        }

        Object element = selection.getFirstElement();

        IFile file = (IFile) ((IAdaptable) element).getAdapter(IFile.class);
        if (file != null)
        {

            // open the file the editor
            try
            {
                IJavaElement javaElement = JavaCore.create(file);
                JavaUI.openInEditor(javaElement);
            }
            catch (CoreException e)
            {
                generalException.errorDialogCheckstyleLog(e, mWorkBenchPart.getSite().getShell(), true);
            }

            // call the fixing job
            Job job = new FixCheckstyleMarkersJob(file);
            job.setUser(true);
            job.schedule();
        }
    }
}