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

package com.atlassw.tools.eclipse.checkstyle.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.atlassw.tools.eclipse.checkstyle.builder.RunCheckstyleOnFilesJob;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

/**
 * Action to diable Checkstyle on one ore more projects.
 * 
 * @author Lars Koedderitzsch
 */
public class CheckSelectedFilesAction implements IObjectActionDelegate
{

    private IWorkbenchPart mPart;

    private IStructuredSelection mSelection;

    /**
     * {@inheritDoc}
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart)
    {
        mPart = targetPart;
    }

    /**
     * {@inheritDoc}
     */
    public void selectionChanged(IAction action, ISelection selection)
    {

        if (selection instanceof IStructuredSelection)
        {
            mSelection = (IStructuredSelection) selection;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void run(IAction action)
    {
        List filesToCheck = new ArrayList();

        try
        {
            addFileResources(mSelection.toList(), filesToCheck);

            RunCheckstyleOnFilesJob job = new RunCheckstyleOnFilesJob(filesToCheck);
            job.schedule();
        }
        catch (CoreException e)
        {
            CheckstyleLog.errorDialog(mPart.getSite().getShell(), e, true);
        }
    }

    /**
     * Recursivly add all files contained in the given resource collection to
     * the second list.
     * 
     * @param resources list of resource
     * @param files the list of files
     * @throws CoreException en unexpected exception
     */
    private void addFileResources(List resources, List files) throws CoreException
    {

        Iterator it = resources.iterator();
        while (it.hasNext())
        {

            IResource resource = (IResource) it.next();

            if (!resource.isAccessible())
            {
                continue;
            }

            if (resource instanceof IFile)
            {
                files.add(resource);
            }
            else if (resource instanceof IContainer)
            {
                addFileResources(Arrays.asList(((IContainer) resource).members()), files);
            }
        }
    }
}
