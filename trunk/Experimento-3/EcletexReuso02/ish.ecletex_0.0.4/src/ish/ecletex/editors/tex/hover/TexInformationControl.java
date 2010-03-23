/*
 * Created on 10-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.editors.tex.hover;

import java.io.FileReader;
import java.util.List;
import ish.ecletex.ImageProvider;
import ish.ecletex.WordNetProvider;
import ish.ecletex.editors.bibtex.dom.BibtexEntry;
import ish.ecletex.editors.bibtex.dom.BibtexFile;
import ish.ecletex.editors.bibtex.parser.BibtexParser;
import ish.ecletex.editors.tex.TeXEditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlExtension;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.jface.text.IInformationControlExtension3;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.ScrolledFormText;

/**
 * @author ish
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TexInformationControl implements IInformationControl,
		IInformationControlExtension, IInformationControlExtension2,
		IInformationControlExtension3 {

	private IPath targetFile;
	private ImageData imagedata;
	private Image image;
	private TeXEditor editor;
	private Composite imageComposite;
	private Composite citationComposite;
	private Shell fShell;
	private Shell gShell;
	private int maxWidth;
	private int maxHeight;
	private Display display;
	private Display displayCitation;
	private boolean PSImage = false;
	private Rectangle boundingBox;
	private boolean isCitation = false;
	private ScrolledFormText citationText;
	private FormText text;
	private BibtexFile[] bibfiles;

	public TexInformationControl(TeXEditor editor, Shell container) {
		fShell = new Shell(container, SWT.NO_FOCUS | SWT.ON_TOP | SWT.MODELESS);
		gShell = new Shell(container, SWT.NO_FOCUS | SWT.ON_TOP | SWT.MODELESS);

		display = fShell.getDisplay();
		displayCitation = gShell.getDisplay();

		this.imageComposite = (Composite) fShell;
		this.citationComposite = (Composite) gShell;
		this.editor = editor;
		attachPaintListener();
		SetupCitationBox();
	}

	private void SetupCitationBox() {
		GridLayout layout = new GridLayout(1, true);
		citationComposite.setLayout(layout);
		citationText = new ScrolledFormText(citationComposite, false);
		FormText text = new FormText(citationText, SWT.NONE);
		citationText.setFormText(text);
		citationText.setLayout(layout);
		GridData gdata = new GridData(1, 1, true, true);
		citationText.setLayoutData(gdata);
		text.setLayoutData(gdata);
	}

	private void attachPaintListener() {
		imageComposite.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				if (image != null && !image.isDisposed()) {
					if (!PSImage) {
						gc.drawImage(image, 0, 0, imagedata.width,
								imagedata.height, 0, 0, imageComposite
										.getSize().x,
								imageComposite.getSize().y);
					} else {
						int Startx = boundingBox.x;
						int Starty = (imagedata.height - boundingBox.y)
								- (boundingBox.height - boundingBox.y);
						int Height = boundingBox.height - boundingBox.y;
						int Width = boundingBox.width - boundingBox.x;
						gc.drawImage(image, Startx, Starty, Width, Height, 0,
								0, imageComposite.getSize().x, imageComposite
										.getSize().y);
					}
				}
			}

		});
	}

	public void setInformation(String information) {
		SetData(information);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.IInformationControl#setSizeConstraints(int,
	 * int)
	 */
	public void setSizeConstraints(int maxWidth, int maxHeight) {
		this.maxHeight = maxHeight;
		this.maxWidth = maxWidth;
		System.out.println("Max Settings: " + maxWidth + " : " + maxHeight);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.IInformationControl#computeSizeHint()
	 */
	public Point computeSizeHint() {
		if (!isCitation) {
			if (imagedata != null) {
				if ((maxHeight == -1 && maxWidth == -1)
						|| (imagedata.width <= maxWidth && imagedata.height <= maxHeight))
					return new Point(imagedata.width, imagedata.height);

				float sf;

				int h;
				int w;

				if (PSImage) {
					h = boundingBox.height - boundingBox.y;
					w = boundingBox.width - boundingBox.x;
				} else {
					h = imagedata.height;
					w = imagedata.width;
				}
				int mh = maxHeight;
				int mw = maxWidth;

				float sf1 = (float) mw / (float) w;
				float sf2 = (float) mh / (float) h;
				if (sf1 > sf2) {
					sf = sf2;
				} else {
					sf = sf1;
				}

				System.out.println("Computed Size Hint: " + (int) (w * sf)
						+ " : " + (int) (h * sf));
				return new Point((int) (w * sf), (int) (h * sf));
			}
		} else {
			return gShell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.IInformationControl#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		if (!isCitation) {
			if (!imageComposite.isDisposed())
				imageComposite.setVisible(visible);
		} else {
			citationComposite.setVisible(visible);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.IInformationControl#setSize(int, int)
	 */
	public void setSize(int width, int height) {
		if (!isCitation) {
			if (!imageComposite.isDisposed())
				imageComposite.setSize(width, height);
		} else {
			citationComposite.setSize(width, height);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IInformationControl#setLocation(org.eclipse.swt
	 * .graphics.Point)
	 */
	public void setLocation(Point location) {
		if (!isCitation) {
			if (!imageComposite.isDisposed())
				imageComposite.setLocation(location);
		} else {
			citationComposite.setLocation(location);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.IInformationControl#dispose()
	 */
	public void dispose() {
		if (image != null && !image.isDisposed())
			image.dispose();
		citationText.dispose();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IInformationControl#addDisposeListener(org.eclipse
	 * .swt.events.DisposeListener)
	 */
	public void addDisposeListener(DisposeListener listener) {
		if (!isCitation) {
			if (!imageComposite.isDisposed())
				imageComposite.addDisposeListener(listener);
		} else {
			citationComposite.addDisposeListener(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IInformationControl#removeDisposeListener(org.
	 * eclipse.swt.events.DisposeListener)
	 */
	public void removeDisposeListener(DisposeListener listener) {
		if (!isCitation) {
			if (!imageComposite.isDisposed())
				imageComposite.removeDisposeListener(listener);
		} else {
			citationComposite.removeDisposeListener(listener);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IInformationControl#setForegroundColor(org.eclipse
	 * .swt.graphics.Color)
	 */
	public void setForegroundColor(Color foreground) {
		if (!isCitation) {
			if (!imageComposite.isDisposed())
				imageComposite.setForeground(foreground);
		} else {
			citationComposite.setForeground(foreground);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IInformationControl#setBackgroundColor(org.eclipse
	 * .swt.graphics.Color)
	 */
	public void setBackgroundColor(Color background) {
		if (!isCitation) {
			if (!imageComposite.isDisposed())
				imageComposite.setBackground(background);
		} else {
			citationComposite.setBackground(background);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.IInformationControl#isFocusControl()
	 */
	public boolean isFocusControl() {

		if (isCitation)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.IInformationControl#setFocus()
	 */
	public void setFocus() {
		if (isCitation) {
			gShell.forceFocus();
			citationText.setFocus();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IInformationControl#addFocusListener(org.eclipse
	 * .swt.events.FocusListener)
	 */
	public void addFocusListener(FocusListener listener) {
		if (!isCitation) {
			if (!imageComposite.isDisposed())
				imageComposite.addFocusListener(listener);
		} else {
			citationComposite.addFocusListener(listener);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IInformationControl#removeFocusListener(org.eclipse
	 * .swt.events.FocusListener)
	 */
	public void removeFocusListener(FocusListener listener) {
		if (!isCitation) {
			if (!imageComposite.isDisposed())
				imageComposite.removeFocusListener(listener);
		} else {
			citationComposite.removeFocusListener(listener);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.IInformationControlExtension#hasContents()
	 */
	public boolean hasContents() {
		if (!isCitation) {
			if (targetFile == null)
				return false;
			if (imagedata == null)
				return false;
			if (image == null)
				return false;
		} else {
			return true;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IInformationControlExtension2#setInput(java.lang
	 * .Object)
	 */
	public void setInput(Object input) {
		if (input instanceof String) {
			SetData((String) input);
		}
	}

	private void resetData() {
		targetFile = null;
		imagedata = null;
		if (image != null) {
			if (!image.isDisposed())
				image.dispose();
			image = null;
		}
		PSImage = false;
		boundingBox = null;
		isCitation = false;
	}

	private void HandleFile(IPath target) {
		System.out.println("Setting Hover Data to: "
				+ target.toFile().toString());
		if (target.toFile().exists()) {
			if (target.getFileExtension().equals("jpg")
					|| target.getFileExtension().equals("png")
					|| target.getFileExtension().equals("bmp")) {
				targetFile = target;
				imagedata = new ImageData(targetFile.toString());
				System.out
						.println("Setting Image to: " + targetFile.toString());
				if (image != null) {
					if (!image.isDisposed())
						image.dispose();
					image = null;
				}

				image = new Image(display, imagedata);
				isCitation = false;
			} else {
				if (target.getFileExtension().equals("eps")
						|| target.getFileExtension().equals("ps")) {
					targetFile = target;
					imagedata = ImageProvider.loadPSImage(target);
					if (imagedata != null) {
						image = new Image(display, imagedata);
						boundingBox = ImageProvider.getBoundingBox(target);
						PSImage = true;
						isCitation = false;
					} else {
						resetData();
					}
				} else {
					resetData();
				}
			}

		} else {
			resetData();
		}
	}

	private void HandleCitation(String information) {
		System.out.println("Handling Citation: " + information);
		BibtexEntry entry = getBibtexEntry(information);
		if (entry == null)
			citationText.setText("Entry Not Found.");
		else {
			String entrytext = entry.toString();
			entrytext = "<p>" + entrytext + "</p>";
			entrytext = entrytext.replaceAll("\n", "<br></br>");
			entrytext = entrytext.replaceAll("\t", "   ");
			citationText.setText(entrytext);
		}
		isCitation = true;
	}

	private boolean isBibtexEntry(String information) {
		if (bibfiles == null) {
			LoadBibtexFiles();
		}
		if (bibfiles != null) {
			for (int i = 0; i < bibfiles.length; i++) {
				List list = bibfiles[i].getEntries();
				for (int j = 0; j < list.size(); j++) {
					Object o = list.get(j);
					if (o instanceof BibtexEntry) {
						BibtexEntry entry = (BibtexEntry) o;
						String key = entry.getEntryKey();
						key = key.trim();
						if (information.equals(key))
							return true;
					}
				}
			}
		}
		return false;
	}

	private BibtexEntry getBibtexEntry(String ref) {
		if (bibfiles == null) {
			LoadBibtexFiles();
		}
		if (bibfiles != null) {
			for (int i = 0; i < bibfiles.length; i++) {
				List list = bibfiles[i].getEntries();
				for (int j = 0; j < list.size(); j++) {
					Object o = list.get(j);
					if (o instanceof BibtexEntry) {
						BibtexEntry entry = (BibtexEntry) o;
						String key = entry.getEntryKey();
						key = key.trim();
						if (ref.equals(key))
							return entry;
					}
				}
			}
		}
		return null;
	}

	private void LoadBibtexFiles() {
		String[] files = editor.getBibtexFilenames();
		bibfiles = new BibtexFile[files.length];
		for (int i = 0; i < files.length; i++) {
			IFile f = editor.getFile();
			IPath filePath = f.getRawLocation().removeLastSegments(1);
			filePath = filePath.append(files[i]);
			bibfiles[i] = new BibtexFile();
			BibtexParser parser = new BibtexParser(false);
			internalLoadBibtexFiles(i, filePath, parser);
		}

	}

	private void internalLoadBibtexFiles(int i, IPath filePath,
			BibtexParser parser) {
		parser.parse(bibfiles[i], new FileReader(filePath.toFile()));
	}

	private void SetData(String information) {
			IPath file = new Path(information);
			IPath folder = editor.getPath();
			IPath target = folder.append(file);
			target = target.makeAbsolute();
			if (target.toFile().exists()) {
				HandleFile(target);
			} else if (isBibtexEntry(information)) {
				HandleCitation(information);
			} else {

				String text = WordNetProvider.getWord(information);
				citationText.setText(text);
				isCitation = true;
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.IInformationControlExtension3#computeTrim()
	 */
	public Rectangle computeTrim() {
		if (!isCitation) {
			if (!imageComposite.isDisposed())
				return imageComposite.computeTrim(0, 0, imageComposite
						.getSize().x, imageComposite.getSize().y);
		} else {
			return citationComposite.computeTrim(0, 0, citationComposite
					.getSize().x, citationComposite.getSize().y);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.IInformationControlExtension3#getBounds()
	 */
	public Rectangle getBounds() {
		if (!isCitation) {
			if (!imageComposite.isDisposed())
				return imageComposite.getBounds();
		} else {
			return citationComposite.getBounds();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IInformationControlExtension3#restoresLocation()
	 */
	public boolean restoresLocation() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.IInformationControlExtension3#restoresSize()
	 */
	public boolean restoresSize() {
		return false;
	}
}
