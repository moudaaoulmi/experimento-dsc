/*
 * Created on 12-May-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.bibtex;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.editors.bibtex.dom.BibtexEntry;
import ish.ecletex.editors.bibtex.dom.BibtexFile;
import ish.ecletex.editors.bibtex.dom.BibtexString;

/**
 * @author ish
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class BibTexFormGen {
	private BibtexEntry entry;
	private FormToolkit toolkit;
	private Composite parent;
	BibTeXEditor editor;
	private Image removeFieldsImage;
	private BibtexFile bfile;
	Section section;

	private HashMap entries;

	public BibTexFormGen(BibtexEntry entry, BibtexFile bfile,
			FormToolkit toolkit, Composite parent, HashMap entries,
			BibTeXEditor editor) {
		this.entry = entry;
		this.toolkit = toolkit;
		this.parent = parent;
		this.entries = entries;
		this.editor = editor;
		this.bfile = bfile;
		internalBibTexFormGen();
	}

	private void internalBibTexFormGen() {
		this.removeFieldsImage = new Image(Display.getDefault(), ecletexPlugin
				.getDefault().openStream(new Path("icons/remove.gif")));
	}

	public void GenerateForm() {
		HashMap fieldsmap = new HashMap();
		section = toolkit.createSection(parent, Section.TWISTIE);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		section.setLayoutData(gd);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				parent.layout(true);
			}
		});

		section.setText(entry.getEntryKey());
		toolkit.createCompositeSeparator(section);

		Composite sectionClient = toolkit.createComposite(section);
		GridLayout clientLayout = new GridLayout();
		clientLayout.numColumns = 2;
		sectionClient.setLayout(clientLayout);

		Map fields = entry.getFields();
		Object[] oFields = fields.keySet().toArray();
		Button[] fieldButtons = new Button[oFields.length];
		Text[] fieldText = new Text[oFields.length];
		for (int index = 0; index < oFields.length; index++) {
			String fieldName = ((String) oFields[index]).trim();
			Button selectCheckButton = toolkit.createButton(sectionClient,
					fieldName, SWT.CHECK | SWT.BOLD);
			fieldButtons[index] = selectCheckButton;
			Text text = toolkit.createText(sectionClient, removeBrackets(entry
					.getFieldValue(fieldName).toString()));
			fieldText[index] = text;
			text.addKeyListener(new KeyListener() {
				public void keyReleased(KeyEvent e) {
					editor.setDirty();
				}

				public void keyPressed(KeyEvent e) {

				}

			});
			fieldsmap.put(fieldName, text);
		}
		Button removeSelected = toolkit.createButton(sectionClient, "",
				SWT.FLAT);
		removeSelected.setToolTipText("Remove All Selected Fields");
		removeSelected.setImage(removeFieldsImage);
		removeSelected.addSelectionListener(new RemoveSelectionListener(
				fieldButtons, fieldText, fieldsmap, entry));
		entries.put(entry.getEntryKey(), new Object[] { fieldsmap, entry });

		Composite addComposite = toolkit.createComposite(sectionClient);
		GridLayout addLayout = new GridLayout();
		addLayout.numColumns = 3;
		addComposite.setLayout(addLayout);
		Combo fieldCombo = new Combo(addComposite, SWT.DROP_DOWN);
		fillCombo(fieldCombo);
		Text addText = toolkit.createText(addComposite,
				"Field Value                                                ");
		Button addField = toolkit.createButton(addComposite, "Add", SWT.FLAT);
		addField.addSelectionListener(new AddFieldSelectionListener(fieldsmap,
				entry, fieldCombo, addText));

		section.setClient(sectionClient);

	}

	private void fillCombo(Combo c) {
		c.add("address");
		c.add("author");
		c.add("booktitle");
		c.add("chapter");
		c.add("edition");
		c.add("editor");
		c.add("howpublished");
		c.add("institution");
		c.add("journal");
		c.add("month");
		c.add("note");
		c.add("number");
		c.add("organization");
		c.add("pages");
		c.add("publisher");
		c.add("school");
		c.add("series");
		c.add("title");
		c.add("type");
		c.add("volume");
		c.add("year");
		c.add("annote");

	}

	private String removeBrackets(String s) {
		if (s.startsWith("{"))
			s = s.substring(1, s.length());
		if (s.endsWith("}"))
			s = s.substring(0, s.length() - 1);
		return s;
	}

	private class RemoveSelectionListener implements SelectionListener {

		private Button[] buttons;
		private HashMap fields;
		private Text[] texts;
		private BibtexEntry entry;

		public RemoveSelectionListener(Button[] buttons, Text[] texts,
				HashMap fields, BibtexEntry entry) {
			this.buttons = buttons;
			this.fields = fields;
			this.texts = texts;
			this.entry = entry;
		}

		public void widgetDefaultSelected(SelectionEvent e) {

		}

		public void widgetSelected(SelectionEvent e) {
			for (int i = 0; i < buttons.length; i++) {
				if (!buttons[i].isDisposed()) {
					if (buttons[i].getSelection()) {
						if (fields.containsKey(buttons[i].getText())) {
							fields.remove(buttons[i].getText());
						}
						entry.undefineField(buttons[i].getText());
						buttons[i].dispose();
						texts[i].dispose();
					}
				}
			}
			editor.setDirty();
			parent.layout(true);

		}
	}

	private class AddFieldSelectionListener implements SelectionListener {

		private HashMap fields;
		private BibtexEntry entry;
		Combo fieldName;
		Text fieldValue;

		public AddFieldSelectionListener(HashMap fields, BibtexEntry entry,
				Combo fieldname, Text fieldvalue) {
			this.fields = fields;
			this.entry = entry;
			this.fieldName = fieldname;
			this.fieldValue = fieldvalue;
		}

		public void widgetDefaultSelected(SelectionEvent e) {

		}

		public void widgetSelected(SelectionEvent e) {

			BibtexString bs = bfile.makeString(fieldValue.getText());
			entry.setField(fieldName.getText(), bs);
			fields.put(fieldValue.getText(), fieldName.getText());
			editor.setDirty();
			section.dispose();
			GenerateForm();
			parent.layout(true);
			section.setExpanded(true);
		}
	}

}
