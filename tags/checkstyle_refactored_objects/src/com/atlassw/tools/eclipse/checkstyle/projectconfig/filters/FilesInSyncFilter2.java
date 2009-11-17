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

package com.atlassw.tools.eclipse.checkstyle.projectconfig.filters;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.synchronize.SyncInfo;
import org.osgi.framework.Bundle;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

/**
 * Filters all files that are in sync with the source repository.
 * 
 * @author Lars K�dderitzsch
 */
public class FilesInSyncFilter2 extends AbstractFilter
{

    /**
     * {@inheritDoc}
     */
    public boolean accept(Object element)
    {
        boolean passes = true;

        if (!isCorrectEclipseVersion())
        {
            Status status = new Status(Status.ERROR, CheckstylePlugin.PLUGIN_ID, Status.OK,
                    "FilesInSyncFilter2 only supported since Eclipse 3.2 and greater", null); //$NON-NLS-1$
            CheckstylePlugin.getDefault().getLog().log(status);
            return true;
        }

        if (element instanceof IFile)
        {

            IFile file = (IFile) element;
            IProject project = file.getProject();

            if (RepositoryProvider.isShared(project))
            {

                RepositoryProvider provider = RepositoryProvider.getProvider(project);

                if (provider != null)
                {

                    Subscriber subscriber = provider.getSubscriber();

                    try
                    {
                        SyncInfo synchInfo = subscriber.getSyncInfo(file);

                        if (synchInfo != null)
                        {
                            int kind = synchInfo.getKind();
                            passes = (SyncInfo.getDirection(kind) & SyncInfo.OUTGOING) == SyncInfo.OUTGOING;
                        }
                    }
                    catch (TeamException e)
                    {
                        generalException.checkstyleLog(e);
                    }
                }
            }
        }
        return passes;
    }

    /**
     * {@inheritDoc}
     */
    public void setEnabled(boolean selected)
    {
        if (isCorrectEclipseVersion())
        {
            super.setEnabled(selected);
        }
        else if (selected)
        {
            CheckstyleLog.errorDialog(Display.getDefault().getActiveShell(),
                    "This filter is only supported since Eclipse 3.2 and greater", null, false); //$NON-NLS-1$
        }
    }

    private boolean isCorrectEclipseVersion()
    {
        // Check for the team plugin version
        // since the used API is only since 3.2.0
        Bundle teamCorePlugin = Platform.getBundle("org.eclipse.team.core"); //$NON-NLS-1$
        String version = (String) teamCorePlugin.getHeaders().get(
                org.osgi.framework.Constants.BUNDLE_VERSION);

        return "3.2.0".compareTo(version) < 1; //$NON-NLS-1$
    }

}