/*
 * Created on 05-May-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.editors.tex.sections.Begin;
import ish.ecletex.editors.tex.sections.Chapter;
import ish.ecletex.editors.tex.sections.Citation;
import ish.ecletex.editors.tex.sections.End;
import ish.ecletex.editors.tex.sections.ISection;
import ish.ecletex.editors.tex.sections.Label;
import ish.ecletex.editors.tex.sections.Section;
import ish.ecletex.editors.tex.sections.SubSection;
import ish.ecletex.editors.tex.sections.SubSubSection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * @author ish
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TeXOutline extends ContentOutlinePage {

	public class OutlineLabelProvider extends LabelProvider {

		Image chapterImage;
		Image sectionImage;
		Image subSectionImage;
		Image subSubSectionImage;
		Image labelImage;
		Image citeImage;

		public OutlineLabelProvider() {
			super();
			this.chapterImage = new Image(Display.getCurrent(), ecletexPlugin
					.getDefault().openStream(new Path("icons/Chapter.gif")));
			this.sectionImage = new Image(Display.getCurrent(), ecletexPlugin
					.getDefault().openStream(new Path("icons/Section.gif")));
			this.subSectionImage = new Image(Display.getCurrent(),
					ecletexPlugin.getDefault().openStream(
							new Path("icons/SubSection.gif")));
			this.subSubSectionImage = new Image(Display.getCurrent(),
					ecletexPlugin.getDefault().openStream(
							new Path("icons/SubSubSection.gif")));
			this.labelImage = new Image(Display.getCurrent(), ecletexPlugin
					.getDefault().openStream(new Path("icons/Label.gif")));
			this.citeImage = new Image(Display.getCurrent(), ecletexPlugin
					.getDefault().openStream(new Path("icons/Cite.gif")));
		}

		public Image getImage(Object obj) {
			if (obj instanceof Chapter) {
				if (chapterImage != null)
					return chapterImage;
			}
			if (obj instanceof Section) {
				if (sectionImage != null)
					return sectionImage;
			}
			if (obj instanceof SubSection) {
				if (subSectionImage != null)
					return subSectionImage;
			}
			if (obj instanceof SubSubSection) {
				if (subSubSectionImage != null)
					return subSubSectionImage;
			}
			if (obj instanceof Citation) {
				if (citeImage != null)
					return citeImage;
			}
			if (obj instanceof Label) {
				if (labelImage != null)
					return labelImage;
			}
			return super.getImage(obj);
		}
	}

	protected class TexContentProvider implements ITreeContentProvider {

		private final static String SEGMENTS = "__tex_segments";
		private IPositionUpdater positionUpdater = new DefaultPositionUpdater(
				SEGMENTS);
		private List content = new ArrayList();
		private List allElements;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang
		 * .Object)
		 */
		public Object[] getChildren(Object o) {
			if (o instanceof ISection) {
				return ((ISection) o).getChildern();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang
		 * .Object)
		 */
		public Object getParent(Object o) {
			if (o instanceof ISection) {
				return ((ISection) o).getParent();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang
		 * .Object)
		 */
		public boolean hasChildren(Object o) {
			if (o instanceof ISection) {
				ISection e = (ISection) o;
				if (e.getChildern().length != 0)
					return true;
				else
					return false;
			}
			if (o == input)
				return true;
			else
				return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object)
		 */
		public Object[] getElements(Object arg0) {
			return content.toArray();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
			if (content != null) {
				content.clear();
				content = null;
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
		 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (oldInput != null) {
				IDocument document = provider.getDocument(oldInput);
				if (document != null) {
					internalInputChanged(document);
					document.removePositionUpdater(positionUpdater);
				}
			}

			content.clear();

			if (newInput != null) {
				IDocument document = provider.getDocument(newInput);
				if (document != null) {
					document.addPositionCategory(SEGMENTS);
					document.addPositionUpdater(positionUpdater);
					parse(document);
				}
			}

		}

		private void internalInputChanged(IDocument document) {
			document.removePositionCategory(SEGMENTS);
		}

		private String getSectionName(String command) {
			int openCurly = command.indexOf("{");
			int closeCurly = command.lastIndexOf("}");

			if (openCurly == -1 || closeCurly == -1)
				return command;
			return command.substring(openCurly + 1, closeCurly);
		}

		public void parse(IDocument document) {
			parse_root(document, true, null, null);
			if (folding != null)
				folding.update(allElements);
		}

		ISection parent = null;
		int WordCount = 0;
		int Citations = 0;

		public void parse_root(IDocument document, boolean root, String file,
				String includeString) {
			if (root) {
				System.out.println("Parsing Root Document");
				parent = null;
				WordCount = 0;
				Citations = 0;
				allElements = new ArrayList();
			} else {
				System.out.println("Parsing Include");

			}
			String currentWord = "";
			boolean inWord = false;
			boolean inComment = false;
			char currentChar;

			TextWords wDetector = new TextWords();
			boolean inTextWord = false;

			for (int i = 0; i < document.getLength(); i++) {
				// A extracao nao é possivel - Mais de uma variavel local
				// utilizada no trecho de codigo
				try {
					currentChar = document.getChar(i);
					if (inTextWord) {
						if (!wDetector.isWordPart(currentChar)) {
							inTextWord = false;
						}
					} else {
						if (wDetector.isWordStart(currentChar)) {
							WordCount++;
							inTextWord = true;
						}
					}

					if (currentChar == '\\') {
						inWord = true;
					} else {
						if (currentChar == '%') {
							inComment = true;
						}

						if (inWord) {
							if (currentChar != '\n' && currentChar != '}') {
								currentWord += currentChar;
							} else {
								inWord = false;
								WordCount--;
								// add the close bracket if needed.
								if (currentChar == '}')
									currentWord += currentChar;

								if (inComment) {
									currentWord = "";
								} else {

									if (currentWord.startsWith("bibliography{")) {
										String files = getSectionName(currentWord);
										if (files.indexOf(",") == -1) {
											bibtexfiles = new String[] { files
													+ ".bib" };
										} else {
											String[] oFiles = files.split(",");
											bibtexfiles = new String[oFiles.length];
											for (int j = 0; j < oFiles.length; j++) {
												bibtexfiles[j] = oFiles[j]
														.trim()
														+ ".bib";
											}
										}
									}

									if (currentWord.startsWith("chapter")) {
										Chapter c = null;
										if (root) {

											c = new Chapter(
													getSectionName(currentWord));
											Position p = new Position(i
													- currentWord.length());
											document.addPosition(SEGMENTS, p);
											c.setPosition(p);

										} else {
											c = new Chapter(
													getSectionName(currentWord)
															+ " ["
															+ includeString
															+ "]", file);
										}
										c.setParent(input);
										content.add(c);
										if (root)
											allElements.add(c);
										parent = c;

									} else if (currentWord
											.startsWith("section")) {
										Section s = null;
										if (root) {
											Position p = new Position(i
													- currentWord.length());
											s = new Section(
													getSectionName(currentWord),
													p);
											document.addPosition(SEGMENTS, p);
										} else {
											s = new Section(
													getSectionName(currentWord)
															+ " ["
															+ includeString
															+ "]", file);
										}

										if (parent == null) {
											s.setParent(input);
											content.add(s);
											parent = s;
										} else {
											if (parent instanceof Chapter) {
												((ISection) parent).addChild(s);
												s.setParent(parent);
												parent = s;
											} else {

												Object secondParent = parent;
												boolean isISection = true;
												for (;;) {
													if (secondParent instanceof ISection) {
														if (!(secondParent instanceof Section)
																&& !(secondParent instanceof SubSection)
																&& !(secondParent instanceof SubSubSection)) {
															break;
														} else {
															secondParent = ((ISection) secondParent)
																	.getParent();
														}
													} else {
														isISection = false;
														break;
													}
												}

												s.setParent(secondParent);
												if (isISection) {
													((ISection) secondParent)
															.addChild(s);
												} else {
													content.add(s);
												}
												parent = s;
											}
										}
										if (root)
											allElements.add(s);
									} else if (currentWord
											.startsWith("subsection")) {
										SubSection ss = null;
										if (root) {
											Position p = new Position(i
													- currentWord.length());
											document.addPosition(SEGMENTS, p);
											ss = new SubSection(
													getSectionName(currentWord),
													p);
										} else {
											ss = new SubSection(
													getSectionName(currentWord)
															+ " ["
															+ includeString
															+ "]", file);
										}

										if (parent == null) {
											ss.setParent(input);
											content.add(ss);
											parent = ss;
										} else {
											if (parent instanceof Chapter
													|| parent instanceof Section) {
												((ISection) parent)
														.addChild(ss);
												ss.setParent(parent);
												parent = ss;
											} else {

												Object secondParent = parent;
												boolean isISection = true;
												for (;;) {
													if (secondParent instanceof ISection) {
														if (!(secondParent instanceof SubSection)
																&& !(secondParent instanceof SubSubSection)) {
															break;
														} else {
															secondParent = ((ISection) secondParent)
																	.getParent();
														}
													} else {
														isISection = false;
														break;
													}
												}

												ss.setParent(secondParent);
												if (isISection) {
													((ISection) secondParent)
															.addChild(ss);
												} else {
													content.add(ss);
												}
												parent = ss;
											}
										}
										if (root)
											allElements.add(ss);
									} else if (currentWord
											.startsWith("subsubsection")) {
										SubSubSection sss = null;
										if (root) {
											Position p = new Position(i
													- currentWord.length());
											document.addPosition(SEGMENTS, p);
											sss = new SubSubSection(
													getSectionName(currentWord),
													p);
										} else {
											sss = new SubSubSection(
													getSectionName(currentWord)
															+ " ["
															+ includeString
															+ "]", file);
										}

										if (parent == null) {
											sss.setParent(input);
											content.add(sss);
											parent = sss;
										} else {
											if (parent instanceof Chapter
													|| parent instanceof Section
													|| parent instanceof SubSection) {
												((ISection) parent)
														.addChild(sss);
												sss.setParent(parent);
												parent = sss;
											} else {
												if (parent instanceof SubSubSection) {
													if (parent.getParent() instanceof ISection) {
														sss
																.setParent(((ISection) parent)
																		.getParent());
														((ISection) ((ISection) parent)
																.getParent())
																.addChild(sss);
													} else {
														sss.setParent(parent
																.getParent());
														content.add(sss);
													}
												}

												parent = sss;
											}
										}
										if (root)
											allElements.add(sss);
									} else if (currentWord.startsWith("cite")) {
										Citation c = null;
										if (root) {
											Position p = new Position(i
													- currentWord.length());
											document.addPosition(SEGMENTS, p);
											c = new Citation(
													getSectionName(currentWord),
													p);
										} else {
											c = new Citation(
													getSectionName(currentWord)
															+ " ["
															+ includeString
															+ " ]", file);
										}

										if (parent != null) {
											c.setParent(parent);
											parent.addChild(c);
										}
										Citations++;
									} else if (currentWord.startsWith("label")) {
										Label l = null;
										if (root) {
											Position p = new Position(i
													- currentWord.length());
											document.addPosition(SEGMENTS, p);
											l = new Label(
													getSectionName(currentWord),
													p);
										} else {
											l = new Label(
													getSectionName(currentWord)
															+ " ["
															+ includeString
															+ " ]", file);
										}

										if (parent != null) {
											l.setParent(parent);
											parent.addChild(l);
										}
									} else if (currentWord.startsWith("begin")) {
										Begin b = null;
										if (root) {
											Position p = new Position(i
													- currentWord.length());
											document.addPosition(SEGMENTS, p);
											b = new Begin(
													getSectionName(currentWord),
													p);
										} else {
											b = new Begin(
													getSectionName(currentWord)
															+ " ["
															+ includeString
															+ " ]", file);
										}

										if (parent != null) {
											b.setParent(parent);
											parent.addBeginEnd(b);
										}
										if (root)
											allElements.add(b);
									} else if (currentWord.startsWith("end")) {
										End e = null;
										if (root) {
											Position p = new Position(i
													- currentWord.length());
											document.addPosition(SEGMENTS, p);
											e = new End(
													getSectionName(currentWord),
													p);
										} else {
											e = new End(
													getSectionName(currentWord)
															+ " ["
															+ includeString
															+ " ]", file);
										}

										if (parent != null) {
											e.setParent(parent);
											parent.addBeginEnd(e);
										}
										if (root)
											allElements.add(e);
									} else if (currentWord
											.startsWith("include{")
											|| currentWord.startsWith("input{")) {
										String cfile = getSectionName(currentWord);

										IEditorPart editor = getSite()
												.getPage().getActiveEditor();
										if (editor instanceof TeXEditor) {
											TeXEditor te = (TeXEditor) editor;
											IFile f = te.getFile();
											IPath filepath = f.getRawLocation()
													.removeLastSegments(1);
											filepath = filepath.append(cfile);
											String file_extension = filepath
													.getFileExtension();
											if (!filepath.getFileExtension()
													.toLowerCase()
													.equals("tex"))
												filepath = filepath
														.addFileExtension("tex");

											if (filepath.toFile().exists()) {

												System.out
														.println("Found Include: "
																+ filepath
																		.toString());
												Document include = getDocument(filepath
														.toString());
												parse_root(include, false,
														filepath.toString(),
														cfile);
											}
										}

									}
									currentWord = "";

								}
								// if (currentChar == '\n') {
								// inComment = false;
								// }
							}
						}
					}

					if (currentChar == '\n') { // Jens Pedersen
						// [jennerdrengen@users.sourceforge.net]
						inComment = false;
					}

				} catch (BadLocationException blex) {
					blex.printStackTrace();
				} catch (BadPositionCategoryException bpcex) {
					bpcex.printStackTrace();
				}

			}
			if (root) {
				content.add(0, "Word Count: " + WordCount);
				content.add(0, "Citations : " + Citations);
			}
		}

	}

	private Document getDocument(String filename) {
		FileInputStream fis = new FileInputStream(filename);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String contents = "";
		String line = "";
		while ((line = br.readLine()) != null) {
			contents += line;
		}
		fis.close();
		return new Document(contents);
	}

	private String[] bibtexfiles = new String[0];

	protected Object input;
	protected IDocumentProvider provider;
	protected ITextEditor editor;
	private TexFoldingStructureProvider folding;

	public TeXOutline(IDocumentProvider provider, ITextEditor editor) {
		super();
		this.provider = provider;
		this.editor = editor;
	}

	public String[] getBibtexFilenames() {
		return bibtexfiles;
	}

	public void SetFoldingStructureProvider(TexFoldingStructureProvider folding) {
		this.folding = folding;

	}

	public void createControl(Composite parent) {

		super.createControl(parent);

		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new TexContentProvider());
		viewer.setLabelProvider(new OutlineLabelProvider());
		viewer.addSelectionChangedListener(this);

		if (input != null)
			viewer.setInput(input);
	}

	public void setInput(Object input) {
		this.input = input;
		update();
	}

	public void update() {
		TreeViewer viewer = getTreeViewer();

		if (viewer != null) {
			Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				control.setRedraw(false);
				viewer.setInput(input);
				viewer.expandAll();
				control.setRedraw(true);
			}
		}
	}

	public void selectionChanged(SelectionChangedEvent event) {

		super.selectionChanged(event);

		ISelection selection = event.getSelection();
		if (selection.isEmpty())
			editor.resetHighlightRange();
		else {
			Object SelectionElement = ((IStructuredSelection) selection)
					.getFirstElement();
			if (SelectionElement instanceof ISection) {

				ISection section = (ISection) SelectionElement;
				if (section.getPosition() != null) {
					int start = section.getPosition().getOffset();
					// int length= segment.position.getLength();
					internalSelectionChanged(start);
				}
			}
		}
	}

	private void internalSelectionChanged(int start) {
		editor.setHighlightRange(start, 0, true);
	}

}
