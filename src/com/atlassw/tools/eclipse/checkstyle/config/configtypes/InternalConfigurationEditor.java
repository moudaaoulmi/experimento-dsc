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

package com.atlassw.tools.eclipse.checkstyle.config.configtypes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.config.ConfigurationWriter;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.config.gui.CheckConfigurationPropertiesDialog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

/**
 * Implementation of a location editor to input a remote location. Contains just
 * a text field to input the URL.
 * 
 * @author Lars K�dderitzsch
 */
public class InternalConfigurationEditor implements ICheckConfigurationEditor
{

    //
    // attributes
    //

    /** The properties dialog. */
    private CheckConfigurationPropertiesDialog mDialog;

    /** the working copy this editor edits. */
    private CheckConfigurationWorkingCopy mWorkingCopy;

    /** the text field containing the config name. */
    private Text mConfigName;

    /** text field containing the location. */
    private Text mLocation;

    /** the text containing the description. */
    private Text mDescription;

    /** button to import an existing configuration. */
    private Button mBtnImport;

    //
    // methods
    //

    /**
     * {@inheritDoc}
     */
    public void initialize(CheckConfigurationWorkingCopy checkConfiguration,
            CheckConfigurationPropertiesDialog dialog)
    {
        mWorkingCopy = checkConfiguration;
        mDialog = dialog;
    }

    /**
     * {@inheritDoc}
     */
    public Control createEditorControl(Composite parent, final Shell shell)
    {

        Composite contents = new Composite(parent, SWT.NULL);
        contents.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        contents.setLayout(layout);

        Label lblConfigName = new Label(contents, SWT.NULL);
        lblConfigName.setText(Messages.CheckConfigurationPropertiesDialog_lblName);
        GridData gd = new GridData();
        lblConfigName.setLayoutData(gd);

        mConfigName = new Text(contents, SWT.LEFT | SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        mConfigName.setLayoutData(gd);

        Label lblConfigLocation = new Label(contents, SWT.NULL);
        lblConfigLocation.setText(Messages.CheckConfigurationPropertiesDialog_lblLocation);
        gd = new GridData();
        gd.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
        lblConfigLocation.setLayoutData(gd);

        mLocation = new Text(contents, SWT.LEFT | SWT.SINGLE | SWT.BORDER);
        mLocation.setEditable(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        mLocation.setLayoutData(gd);

        Label lblDescription = new Label(contents, SWT.NULL);
        lblDescription.setText(Messages.CheckConfigurationPropertiesDialog_lblDescription);
        gd = new GridData();
        gd.horizontalSpan = 2;
        lblDescription.setLayoutData(gd);

        mDescription = new Text(contents, SWT.LEFT | SWT.WRAP | SWT.MULTI | SWT.BORDER
                | SWT.VERTICAL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        gd.widthHint = 300;
        gd.heightHint = 100;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        mDescription.setLayoutData(gd);

        mBtnImport = new Button(contents, SWT.PUSH);
        mBtnImport.setText(Messages.InternalConfigurationEditor_btnImport);
        gd = new GridData();
        gd.horizontalSpan = 2;
        gd.horizontalAlignment = GridData.END;
        mBtnImport.setLayoutData(gd);

        mBtnImport.addSelectionListener(new SelectionListenerImplementation());

        if (mWorkingCopy.getName() != null)
        {
            mConfigName.setText(mWorkingCopy.getName());
        }
        if (mWorkingCopy.getLocation() != null)
        {
            mLocation.setText(mWorkingCopy.getLocation());
        }
        if (mWorkingCopy.getDescription() != null)
        {
            mDescription.setText(mWorkingCopy.getDescription());
        }

        return contents;
    }

    /**
     * {@inheritDoc}
     */
    public CheckConfigurationWorkingCopy getEditedWorkingCopy() throws CheckstylePluginException
    {
        mWorkingCopy.setName(mConfigName.getText());

        if (mWorkingCopy.getLocation() == null)
        {

            String location = "internal_config_" + "_" + System.currentTimeMillis() + ".xml"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            internalGetEditedWorkingCopy(location);
        }
        mWorkingCopy.setDescription(mDescription.getText());

        return mWorkingCopy;
    }

    private void internalGetEditedWorkingCopy(String location) throws CheckstylePluginException
    {
        mWorkingCopy.setLocation(location);

    }

    /**
     * Helper method trying to ensure that the file location provided by the
     * user exists. If that is not the case it prompts the user if an empty
     * configuration file should be created.
     * 
     * @param location the configuration file location
     * @throws CheckstylePluginException error when trying to ensure the
     *             location file existance
     */
    private boolean ensureFileExists(String location) throws CheckstylePluginException
    {

        String resolvedLocation = InternalConfigurationType.resolveLocationInWorkspace(location);

        File file = new File(resolvedLocation);
        if (!file.exists())
        {

            OutputStream out = null;
            internalEnsureFileExists(file, out);
            return true;
        }

        return true;
    }

    private void internalEnsureFileExists(File file, OutputStream out)
        throws CheckstylePluginException
    {

        if (file.getParentFile() != null)
        {
            file.getParentFile().mkdirs();
        }
        out = new BufferedOutputStream(new FileOutputStream(file));
        ConfigurationWriter.writeNewConfiguration(out, mWorkingCopy);

    }
    
    private class SelectionListenerImplementation implements SelectionListener{

        public void widgetSelected(SelectionEvent e)
        {
            ICheckConfiguration targetConfig = getEditedWorkingCopy();
            FileDialog fileDialog = new FileDialog(mConfigName.getShell());
            fileDialog.setText(Messages.InternalConfigurationEditor_titleImportDialog);
            fileDialog.setFilterExtensions(new String[] { "*.xml", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
            String configFileString = fileDialog.open();
            if (configFileString != null && new File(configFileString).exists())
            {
                ICheckConfiguration tmpSourceConfig = new CheckConfiguration("dummy", //$NON-NLS-1$
                        configFileString, null, new ExternalFileConfigurationType(), true, null, null);

                CheckConfigurationFactory.copyConfiguration(tmpSourceConfig, targetConfig);
            }
        }

        public void widgetDefaultSelected(SelectionEvent e)
        {
        // NOOP
        }
        
    }

}