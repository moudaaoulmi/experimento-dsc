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

package com.atlassw.tools.eclipse.checkstyle.properties;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.gui.CheckConfigurationLabelProvider;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.FileSet;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

/**
 * Property page.
 */
public class ComplexFileSetsEditor implements IFileSetsEditor
{
    // =================================================
    // Public static final variables.
    // =================================================

    // =================================================
    // Static class variables.
    // =================================================

    // =================================================
    // Instance member variables.
    // =================================================

    private IProject mProject;

    private Composite mComposite;

    private CheckboxTableViewer mViewer;

    private Button mAddButton;

    private Button mEditButton;

    private Button mRemoveButton;

    private List mFileSets;

    private CheckstylePropertyPage mPropertyPage;

    // =================================================
    // Constructors & finalizer.
    // =================================================

    /**
     * Creates the ComplexFileSetsEditor.
     * 
     * @param propsPage the property page
     */
    public ComplexFileSetsEditor(CheckstylePropertyPage propsPage)
    {
        mPropertyPage = propsPage;
        mProject = (IProject) propsPage.getElement();
    }

    // =================================================
    // Methods.
    // =================================================

    /**
     * {@inheritDoc}
     */
    public void setFileSets(List fileSets)
    {
        mFileSets = fileSets;

    }

    /**
     * {@inheritDoc}
     */
    public List getFileSets()
    {
        return mFileSets;
    }

    /**
     * {@inheritDoc}
     */
    public Control createContents(Composite parent) throws CheckstylePluginException
    {

        mComposite = parent;

        Group composite = new Group(parent, SWT.NONE);
        composite.setText(Messages.ComplexFileSetsEditor_titleAdvancedFilesetEditor);

        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);

        //
        // Create the table of file sets.
        //
        Table table = new Table(composite, SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION);

        GridData data = new GridData(GridData.FILL_BOTH);
        table.setLayoutData(data);

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableLayout tableLayout = new TableLayout();
        table.setLayout(tableLayout);

        TableColumn column1 = new TableColumn(table, SWT.NONE);
        column1.setText(Messages.ComplexFileSetsEditor_colEnabled);
        column1.setResizable(false);

        TableColumn column2 = new TableColumn(table, SWT.NONE);
        column2.setText(Messages.ComplexFileSetsEditor_colFilesetName);

        TableColumn column3 = new TableColumn(table, SWT.NONE);
        column3.setText(Messages.ComplexFileSetsEditor_colConfiguration);

        tableLayout.addColumnData(new ColumnWeightData(20));
        tableLayout.addColumnData(new ColumnWeightData(40));
        tableLayout.addColumnData(new ColumnWeightData(40));

        mViewer = new CheckboxTableViewer(table);
        mViewer.setLabelProvider(new FileSetLabelProvider());
        mViewer.setContentProvider(new ArrayContentProvider());
        mViewer.setSorter(new FileSetViewerSorter());
        mViewer.setInput(mFileSets);

        //
        // Set checked state
        //
        Iterator iter = mFileSets.iterator();
        while (iter.hasNext())
        {
            FileSet fileSet = (FileSet) iter.next();
            mViewer.setChecked(fileSet, fileSet.isEnabled());
        }

        mViewer.addDoubleClickListener(new IDoubleClickListener()
        {
            public void doubleClick(DoubleClickEvent e)
            {
                editFileSet();
            }
        });

        mViewer.addCheckStateListener(new ICheckStateListener()
        {
            public void checkStateChanged(CheckStateChangedEvent event)
            {
                changeEnabledState(event);
            }
        });

        //
        // Build the buttons.
        //
        Composite buttons = new Composite(composite, SWT.NULL);
        buttons.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        buttons.setLayout(layout);

        mAddButton = createPushButton(buttons, Messages.ComplexFileSetsEditor_btnAdd);
        mAddButton.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event evt)
            {
                addFileSet();
            }
        });

        mEditButton = createPushButton(buttons, Messages.ComplexFileSetsEditor_btnEdit);
        mEditButton.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event evt)
            {
                editFileSet();
            }
        });

        mRemoveButton = createPushButton(buttons, Messages.ComplexFileSetsEditor_btnRemove);
        mRemoveButton.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event evt)
            {
                removeFileSet();
            }
        });

        return composite;
    }

    /**
     * {@inheritDoc}
     */
    public void refresh()
    {
    // NOOP
    }

    /**
     * Utility method that creates a push button instance and sets the default
     * layout data.
     * 
     * @param parent the parent for the new button
     * @param label the label for the new button
     * @return the newly-created button
     */
    private Button createPushButton(Composite parent, String label)
    {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(label);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        button.setLayoutData(data);
        return button;
    }

    private void addFileSet()
    {
            FileSetEditDialog dialog = new FileSetEditDialog(mComposite.getShell(), null, mProject,
                    mPropertyPage);
            if (FileSetEditDialog.OK == dialog.open())
            {
                FileSet fileSet = dialog.getFileSet();
                mFileSets.add(fileSet);
                mViewer.refresh();
                mViewer.setChecked(fileSet, fileSet.isEnabled());

                mPropertyPage.getContainer().updateButtons();
            }
    }

    public IProject getMProject()
    {
        return mProject;
    }

    public void setMProject(IProject project)
    {
        mProject = project;
    }

    public Composite getMComposite()
    {
        return mComposite;
    }

    public void setMComposite(Composite composite)
    {
        mComposite = composite;
    }

    public CheckboxTableViewer getMViewer()
    {
        return mViewer;
    }

    public void setMViewer(CheckboxTableViewer viewer)
    {
        mViewer = viewer;
    }

    public Button getMEditButton()
    {
        return mEditButton;
    }

    public void setMEditButton(Button editButton)
    {
        mEditButton = editButton;
    }

    public Button getMRemoveButton()
    {
        return mRemoveButton;
    }

    public void setMRemoveButton(Button removeButton)
    {
        mRemoveButton = removeButton;
    }

    public List getMFileSets()
    {
        return mFileSets;
    }

    public void setMFileSets(List fileSets)
    {
        mFileSets = fileSets;
    }

    public CheckstylePropertyPage getMPropertyPage()
    {
        return mPropertyPage;
    }

    public void setMPropertyPage(CheckstylePropertyPage propertyPage)
    {
        mPropertyPage = propertyPage;
    }

    private void editFileSet()
    {
        IStructuredSelection selection = (IStructuredSelection) mViewer.getSelection();
        FileSet fileSet = (FileSet) selection.getFirstElement();
        if (fileSet == null)
        {
            //
            // Nothing is selected.
            //
            return;
        }

            FileSetEditDialog dialog = new FileSetEditDialog(mComposite.getShell(),
                    (FileSet) fileSet.clone(), mProject, mPropertyPage);
            if (FileSetEditDialog.OK == dialog.open())
            {
                FileSet newFileSet = dialog.getFileSet();
                mFileSets.remove(fileSet);
                mFileSets.add(newFileSet);
                mViewer.refresh();
                mViewer.setChecked(newFileSet, newFileSet.isEnabled());

                mPropertyPage.getContainer().updateButtons();
            }
    }

    private void removeFileSet()
    {
        IStructuredSelection selection = (IStructuredSelection) mViewer.getSelection();
        FileSet fileSet = (FileSet) selection.getFirstElement();
        if (fileSet == null)
        {
            //
            // Nothing is selected.
            //
            return;
        }

        mFileSets.remove(fileSet);
        mViewer.refresh();
        mPropertyPage.getContainer().updateButtons();
    }

    private void changeEnabledState(CheckStateChangedEvent event)
    {
        if (event.getElement() instanceof FileSet)
        {
            FileSet fileSet = (FileSet) event.getElement();
            fileSet.setEnabled(event.getChecked());
            mViewer.refresh();
        }
    }

    /**
     * Provides the labels for the FileSet list display.
     */
    class FileSetLabelProvider extends LabelProvider implements ITableLabelProvider
    {

        private CheckConfigurationLabelProvider mCheckConfigLabelProvider = new CheckConfigurationLabelProvider();

        /**
         * @see ITableLabelProvider#getColumnText(Object, int)
         */
        public String getColumnText(Object element, int columnIndex)
        {
            String result = element.toString();
            if (element instanceof FileSet)
            {
                FileSet fileSet = (FileSet) element;
                switch (columnIndex)
                {
                    case 0:
                        result = new String();
                        break;

                    case 1:
                        result = fileSet.getName();
                        break;

                    case 2:
                        result = fileSet.getCheckConfig() != null ? mCheckConfigLabelProvider
                                .getText(fileSet.getCheckConfig()) : ""; //$NON-NLS-1$
                        break;

                    default:
                        break;
                }

            }
            return result;
        }

        /**
         * @see ITableLabelProvider#getColumnImage(Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex)
        {
            return null;
        }
    }

    /**
     * Sorts CheckConfiguration objects into their display order.
     */
    public class FileSetViewerSorter extends ViewerSorter
    {

        /**
         * {@inheritDoc}
         */
        public int compare(Viewer viewer, Object e1, Object e2)
        {
            int result = 0;

            if ((e1 instanceof FileSet) && (e2 instanceof FileSet))
            {
                FileSet fileSet1 = (FileSet) e1;
                FileSet fileSet2 = (FileSet) e2;

                String name1 = fileSet1.getName();
                String name2 = fileSet2.getName();

                result = name1.compareTo(name2);
            }

            return result;
        }
    }
}