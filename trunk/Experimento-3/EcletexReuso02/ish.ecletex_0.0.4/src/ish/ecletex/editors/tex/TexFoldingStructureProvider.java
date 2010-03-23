/*
 * Created on 03-Oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.editors.tex;

import ish.ecletex.editors.tex.sections.AbstractSection;
import ish.ecletex.editors.tex.sections.Begin;
import ish.ecletex.editors.tex.sections.Chapter;
import ish.ecletex.editors.tex.sections.Citation;
import ish.ecletex.editors.tex.sections.End;
import ish.ecletex.editors.tex.sections.ISection;
import ish.ecletex.editors.tex.sections.Label;
import ish.ecletex.editors.tex.sections.Section;
import ish.ecletex.editors.tex.sections.SubSection;
import ish.ecletex.editors.tex.sections.SubSubSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.projection.IProjectionListener;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * @author Ian Hartney
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TexFoldingStructureProvider implements IProjectionListener {

	private static class TexProjectionAnnotation extends ProjectionAnnotation {
		public String ID;
		public Class type;

		public TexProjectionAnnotation(String ID, Class type) {
			this.ID = ID;
			this.type = type;
		}

		public boolean equals(Object arg0) {
			if (arg0 instanceof TexProjectionAnnotation) {
				TexProjectionAnnotation ann = (TexProjectionAnnotation) arg0;
				if (ann.ID.equals(this.ID) && ann.type.equals(this.type))
					return true;
			}
			return false;
		}

		public int hashCode() {
			String s = type.getName() + ID;
			return s.hashCode();
		}
	}

	private IDocument fCachedDocument;

	private ITextEditor fEditor;
	private ProjectionViewer fViewer;
	private Map lastAnnotation;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jface.text.source.projection.IProjectionListener#
	 * projectionEnabled()
	 */
	public void projectionEnabled() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jface.text.source.projection.IProjectionListener#
	 * projectionDisabled()
	 */
	public void projectionDisabled() {
		// TODO Auto-generated method stub

	}

	public void install(ITextEditor editor, ProjectionViewer viewer) {
		if (editor instanceof TeXEditor) {
			fEditor = editor;
			fViewer = viewer;
			TeXOutline outline = (TeXOutline) fEditor
					.getAdapter(IContentOutlinePage.class);
			outline.SetFoldingStructureProvider(this);
			fViewer.addProjectionListener(this);
		}
	}

	public void uninstall() {
		if (isInstalled()) {
			projectionDisabled();
			fViewer.removeProjectionListener(this);
			fViewer = null;
			fEditor = null;
		}
	}

	protected boolean isInstalled() {
		return fEditor != null;
	}

	private boolean ExistsInPrevious(Class type, String ID) {
		if (lastAnnotation == null)
			return false;
		TexProjectionAnnotation ann = new TexProjectionAnnotation(ID, type);
		if (lastAnnotation.containsKey(ann))
			return true;
		return false;
	}

	private TexProjectionAnnotation GetExisting(Class type, String ID) {
		String hash = type.getName() + ID;
		Iterator e = lastAnnotation.keySet().iterator();
		while (e.hasNext()) {
			Object next = e.next();
			if (next.hashCode() == hash.hashCode()) {
				return (TexProjectionAnnotation) next;
			}
		}
		return null;
	}

	private Position GetPosition(ISection s, List contents) {
		ISection next = FindNextOverridingSection(s, contents);
		return internalGetPosition(s, next);

	}

	private Position internalGetPosition(ISection s, ISection next) {
		int startLine = fCachedDocument.getLineOfOffset(s.getPosition()
				.getOffset());
		int offset = fCachedDocument.getLineOffset(startLine);
		if (next == null) {
			int endLine = fCachedDocument.getLineOfOffset(fCachedDocument
					.getLength() - 1);
			int endoffset = fCachedDocument.getLineOffset(endLine);
			return new Position(offset, endoffset - offset);
		}
		if (s instanceof Begin) {
			int endLine = fCachedDocument.getLineOfOffset(next.getPosition()
					.getOffset());
			int endoffset = fCachedDocument.getLineOffset(endLine + 1);
			return new Position(offset, endoffset - offset);

		} else {
			int endLine = fCachedDocument.getLineOfOffset(next.getPosition()
					.getOffset());
			int endoffset = fCachedDocument.getLineOffset(endLine);
			return new Position(offset, endoffset - offset);
		}
	}

	ISection FindNextOverridingSection(ISection s, List contents) {
		ISection next = null;
		if (s instanceof Chapter) {
			int index = contents.indexOf(s);
			for (int i = index + 1; i < contents.size(); i++) {
				ISection s2 = (ISection) contents.get(i);
				if (s2 instanceof Chapter) {
					next = s2;
					break;
				}
			}
		} else if (s instanceof Section) {
			int index = contents.indexOf(s);
			for (int i = index + 1; i < contents.size(); i++) {
				ISection s2 = (ISection) contents.get(i);
				if (s2 instanceof Section || s2 instanceof Chapter) {
					next = s2;
					break;
				}
			}
		} else if (s instanceof SubSection) {
			int index = contents.indexOf(s);
			for (int i = index + 1; i < contents.size(); i++) {
				ISection s2 = (ISection) contents.get(i);
				if (s2 instanceof SubSection || s2 instanceof Section
						|| s2 instanceof Chapter) {
					next = s2;
					break;
				}
			}
		} else if (s instanceof SubSubSection) {
			int index = contents.indexOf(s);
			for (int i = index + 1; i < contents.size(); i++) {
				ISection s2 = (ISection) contents.get(i);
				if (s2 instanceof SubSubSection || s2 instanceof SubSection
						|| s2 instanceof Section || s2 instanceof Chapter) {
					next = s2;
					break;
				}
			}
		} else if (s instanceof Begin) {
			int index = contents.indexOf(s);
			int sep_index = s.getTitle().indexOf(AbstractSection.ID_SEPERATOR);
			String title = "";
			if (sep_index == -1)
				title = s.getTitle();
			else
				title = s.getTitle().substring(0, sep_index);
			for (int i = index + 1; i < contents.size(); i++) {
				ISection s2 = (ISection) contents.get(i);

				if (s2 instanceof End && s2.getTitle().startsWith(title)) {
					next = s2;
					break;
				}
			}
		}
		return next;
	}

	private Map computeAdditions(List contents) {
		Map additions = new HashMap();

		Iterator e = contents.iterator();
		while (e.hasNext()) {
			Object next = e.next();
			if (next instanceof ISection && !(next instanceof Citation)
					&& !(next instanceof Label) && !(next instanceof End)) {
				ISection c = (ISection) next;
				if (!ExistsInPrevious(c.getClass(), c.getTreeString())) {
					TexProjectionAnnotation ann = new TexProjectionAnnotation(c
							.getTreeString(), c.getClass());
					Position p = GetPosition(c, contents);
					additions.put(ann, p);

				}
			}

		}
		return additions;
	}

	public void update(List contents) {

		if (!isInstalled())
			return;

		ProjectionAnnotationModel model = (ProjectionAnnotationModel) fEditor
				.getAdapter(ProjectionAnnotationModel.class);
		if (model == null)
			return;

		try {

			IDocumentProvider provider = fEditor.getDocumentProvider();
			fCachedDocument = provider.getDocument(fEditor.getEditorInput());
			// fAllowCollapsing= false;

			List deletions = new ArrayList();
			// List updates= new ArrayList();

			Map additions = computeAdditions(contents);

			List updates = new ArrayList();

			// if(old!=null){
			// deletions = old;
			// }

			Iterator e = contents.iterator();

			while (e.hasNext()) {
				Object next = e.next();
				if (next instanceof ISection && !(next instanceof Citation)
						&& !(next instanceof Label) && !(next instanceof End)) {
					ISection c = (ISection) next;
					if (ExistsInPrevious(c.getClass(), c.getTreeString())) {
						TexProjectionAnnotation ann = GetExisting(c.getClass(),
								c.getTreeString());
						Position p = model.getPosition(ann);
						Position cp = GetPosition(c, contents);
						if (p != null && !p.equals(cp)) {
							System.out.println("Updating:" + ann.ID);
							p.setOffset(cp.getOffset());
							p.setLength(cp.getLength());
							updates.add(ann);
						}
					}
				}

			}

			Map currentAnnotations = new HashMap();
			e = contents.iterator();
			while (e.hasNext()) {
				ISection next = (ISection) e.next();
				TexProjectionAnnotation ann = new TexProjectionAnnotation(next
						.getTreeString(), next.getClass());
				currentAnnotations.put(ann, new Object());
			}

			if (lastAnnotation != null) {
				e = lastAnnotation.keySet().iterator();
				while (e.hasNext()) {
					TexProjectionAnnotation ann = (TexProjectionAnnotation) e
							.next();
					if (!currentAnnotations.containsKey(ann)) {
						deletions.add(ann);
					}
				}
			}

			// Map updated= computeAdditions((IParent) fInput);
			// Map previous= createAnnotationMap(model);
			//			
			//			
			// Iterator e= updated.keySet().iterator();
			// while (e.hasNext()) {
			// JavaProjectionAnnotation annotation= (JavaProjectionAnnotation)
			// e.next();
			// IJavaElement element= annotation.getElement();
			// Position position= (Position) updated.get(annotation);
			//				
			// List annotations= (List) previous.get(element);
			// if (annotations == null) {
			//					
			// additions.put(annotation, position);
			//					
			// } else {
			//					
			// Iterator x= annotations.iterator();
			// while (x.hasNext()) {
			// JavaProjectionAnnotation a= (JavaProjectionAnnotation) x.next();
			// if (annotation.isComment() == a.isComment()) {
			// Position p= model.getPosition(a);
			// if (p != null && !position.equals(p)) {
			// p.setOffset(position.getOffset());
			// p.setLength(position.getLength());
			// updates.add(a);
			// }
			// x.remove();
			// break;
			// }
			// }
			//										
			// if (annotations.isEmpty())
			// previous.remove(element);
			// }
			// }
			//			
			// e= previous.values().iterator();
			// while (e.hasNext()) {
			// List list= (List) e.next();
			// int size= list.size();
			// for (int i= 0; i < size; i++)
			// deletions.add(list.get(i));
			// }
			//			
			// match(model, deletions, additions, updates);
			//			
			Annotation[] removals = new Annotation[deletions.size()];
			deletions.toArray(removals);
			Annotation[] changes = new Annotation[updates.size()];
			updates.toArray(changes);
			// if(old==null)
			// old = new Annotation[0];
			model.modifyAnnotations(removals, additions, changes);

			lastAnnotation = currentAnnotations;
			// old = new Annotation[additions.size()];
			// old = (Annotation[])additions.keySet().toArray(old);

		} finally {
			fCachedDocument = null;
			// fAllowCollapsing= true;
		}
	}
}
