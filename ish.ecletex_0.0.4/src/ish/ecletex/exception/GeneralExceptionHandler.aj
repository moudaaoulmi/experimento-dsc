package ish.ecletex.exception;

import ish.ecletex.WordNetProvider;
import ish.ecletex.templateProvider;
import ish.ecletex.builders.ecletexBuildVisitor;
import ish.ecletex.builders.ecletexBuilder;
import ish.ecletex.builders.latexlogparser.Entry;
import ish.ecletex.builders.latexlogparser.Model;
import ish.ecletex.editors.bibtex.BibTeXEditor;
import ish.ecletex.editors.bibtex.BibTexFormGen;
import ish.ecletex.editors.bibtex.parser.BibtexParser;
import ish.ecletex.editors.tex.Flow2DocumentListener;
import ish.ecletex.editors.tex.SpellingRule;
import ish.ecletex.editors.tex.TeXCompletionEngine;
import ish.ecletex.editors.tex.TeXDocumentProvider;
import ish.ecletex.editors.tex.TeXDoubleClickStrategy;
import ish.ecletex.editors.tex.TeXEditor;
import ish.ecletex.editors.tex.TeXOutline;
import ish.ecletex.editors.tex.TexFoldingStructureProvider;
import ish.ecletex.editors.tex.TextWords;
import ish.ecletex.editors.tex.actions.AddWordAction;
import ish.ecletex.editors.tex.hover.TexHover;
import ish.ecletex.editors.tex.hover.TexInformationControl;
import ish.ecletex.editors.tex.sections.ISection;
import ish.ecletex.editors.tex.spelling.engine.GenericSpellDictionary;
import ish.ecletex.editors.tex.spelling.engine.PropertyConfiguration;
import ish.ecletex.editors.tex.spelling.engine.SpellDictionaryHashMap;
import ish.ecletex.properties.ecletexProjectProperties;
import ish.ecletex.properties.texFileProperties;
import ish.ecletex.utils.SafeExec;
import ish.ecletex.wizards.export.pdf.PDFExportWizard;
import ish.ecletex.wizards.export.pdf.PDFWizardPageOne;
import ish.ecletex.wordnet.jwnl.JWNL;
import ish.ecletex.wordnet.jwnl.JWNLException;
import ish.ecletex.wordnet.jwnl.data.POS;
import ish.ecletex.wordnet.jwnl.dictionary.FileBackedDictionary;
import ish.ecletex.wordnet.jwnl.dictionary.database.Query;
import ish.ecletex.wordnet.jwnl.dictionary.file.DictionaryFileType;
import ish.ecletex.xml.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Vector;

import org.aspectj.lang.SoftException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;

import ish.ecletex.editors.bibtex.parser.ParseException;

public privileged aspect GeneralExceptionHandler {

	String[] msg = { "Exception", "Error writing to dictionary file",
			"Error writing to dictionary file",
			"Project cannot be identified as an ecleTex nature.",
			"WordNet 2.0 data is not avaliable.",
			"default",
			""
	};

	pointcut internalReflowDocumentHandler(): execution(private void TeXDocumentProvider.internalReflowDocument(IDocument, int));

	pointcut internalAddWordHandler2(): execution(private void SpellDictionaryHashMap.internalAddWord(String));

	pointcut internalAddWordHandler(): execution(private void GenericSpellDictionary.internalAddWord(String));

	pointcut internalInitHandler() : execution(private void PDFExportWizard.internalInit());
	
	pointcut internalGetWordHandler(): execution(private String WordNetProvider.internalGetWord(String));
	pointcut getLanguageHandler(): execution(public String SpellingRule.GetLanguage());
	pointcut getHoverInfoHandler(): execution(public String TexHover.getHoverInfo(ITextViewer, IRegion));
	
	public void metodo(){		}
	
	// Mensagens
	// --------------------------------------------------------------------------

	pointcut LoadTemplatesHandler(): execution(private void templateProvider.LoadTemplates());

	pointcut fullBuildHandler(): execution(protected void ecletexBuilder.fullBuild(IProgressMonitor));

	pointcut internalSaveFormPageHandler(): execution(private void BibTeXEditor.internalSaveFormPage());

	pointcut internalBibTexFormGenHandler(): execution(private void BibTexFormGen.internalBibTexFormGen());

	pointcut internalCreateFormPageHandler(): execution(private void BibTeXEditor.internalCreateFormPage(BibtexParser, IPath));

	pointcut CheckPropertiesHandler(): execution(private void TeXEditor.CheckProperties(IFile));

	pointcut internalInputChangedHandler(): execution(private void TeXOutline$TexContentProvider.internalInputChanged(IDocument));

	pointcut saveHandler(): execution(public void PropertyConfiguration.save());

	pointcut internalWidgetSelectedHandler() : execution(private void PDFWizardPageOne.BrowseSelectionListener.internalWidgetSelected(IPath));

	pointcut internalInitialize1Handler(): execution(private static void JWNL.internalInitialize1(InputStream));

	pointcut internalFileLookaheadIteratorHandler(): execution(private void FileBackedDictionary.FileLookaheadIterator.internalFileLookaheadIterator(POS,	DictionaryFileType ));

	pointcut internalClose2Handler(): execution(private void Query.internalClose2());

	pointcut internalClose1Handler(): execution(private void Query.internalClose1());

	pointcut internalCloseHandler(): execution(private void Query.internalClose());

	pointcut ProcessResultsHandler(): execution(private void ecletexBuildVisitor.ProcessResults(Model , IResource));

	pointcut internalRunHandler(): execution(private void ecletexBuildVisitor.DoMarkers.internalRun(IProject, LinkedList, int));

	pointcut MarkerExistsHandler(): execution(private boolean ecletexBuildVisitor.DoMarkers.MarkerExists(IResource, Entry));

	pointcut documentChangedHandler(): execution(public void Flow2DocumentListener.documentChanged(DocumentEvent));

	pointcut internalLoadBibtexFilesHandler(): execution(private void TexInformationControl.internalLoadBibtexFiles(int, IPath,BibtexParser));

	pointcut runHandler(): execution(public void SafeExec.StreamConsumer.run());
	pointcut internalNextLineHandler(): execution(private void FileBackedDictionary.FileLookaheadIterator.internalNextLine());
	pointcut internalParseHandler1() : execution(private static void Parser.internalParse());
	pointcut internalGetCurrentWordHandler(): execution(private String AddWordAction.internalGetCurrentWord(IDocument, TextWords,
			int) );
	pointcut internalSelectCommentHandler(): execution(private boolean TeXDoubleClickStrategy.internalSelectComment(int, IDocument, int , int));
	pointcut internalSelectWordHandler(): execution(private boolean TeXDoubleClickStrategy.internalSelectWord(int, IDocument,int, int));
	pointcut internalGetPositionHandler(): execution(private Position TexFoldingStructureProvider.internalGetPosition(ISection , ISection));
	pointcut performOkHandler2(): execution(public boolean texFileProperties.performOk());
	pointcut performOkHandler(): execution(public boolean ecletexProjectProperties.performOk());
	pointcut internalWhileHandlerSoft(): execution(private void TeXCompletionEngine.internalWhile(Vector, Enumeration));
	
	
	declare soft: IOException : LoadTemplatesHandler() || internalCreateFormPageHandler() || internalSaveFormPageHandler() || internalBibTexFormGenHandler() || saveHandler() || internalInitialize1Handler() || internalFileLookaheadIteratorHandler() || internalAddWordHandler2() || internalAddWordHandler() || runHandler();
	declare soft: CoreException: fullBuildHandler() || CheckPropertiesHandler()|| internalWidgetSelectedHandler() || ProcessResultsHandler()||internalRunHandler() || MarkerExistsHandler() || internalInitHandler() || performOkHandler2() || performOkHandler();
	declare soft: ParseException: internalCreateFormPageHandler();
	declare soft: BadPositionCategoryException: internalInputChangedHandler();
	declare soft: FileNotFoundException : saveHandler();
	declare soft: SQLException : internalCloseHandler() || internalClose1Handler() || internalClose2Handler();
	declare soft: BadLocationException :documentChangedHandler() || internalReflowDocumentHandler() || getLanguageHandler() || internalGetCurrentWordHandler() || internalSelectCommentHandler() || internalSelectWordHandler() || internalGetPositionHandler() || getHoverInfoHandler();
	declare soft: Exception: internalLoadBibtexFilesHandler() || internalNextLineHandler() || internalParseHandler1();
	declare soft: JWNLException : internalGetWordHandler();
	
	
	void around(): internalWhileHandlerSoft(){
		try {
			proceed();
		} catch (SoftException e) {
			// continue;
		}
	}
	
	Object around(): internalGetCurrentWordHandler() || internalSelectCommentHandler() || internalSelectWordHandler() || internalGetPositionHandler() || performOkHandler2() || performOkHandler() {
		try {
			return proceed();
		} catch(RuntimeException e){
			throw e;
		} catch (Exception blx) {
			return null;
		}
	}	
	
	String around(): internalGetWordHandler() || getLanguageHandler()|| getHoverInfoHandler()  {
		try {
			return proceed();
		} catch(RuntimeException e){
			throw e;
		} catch (Exception ex) {
			return this.msg[thisEnclosingJoinPointStaticPart.getId()];
		}
	}
	
	void around(): internalReflowDocumentHandler() || internalAddWordHandler2() || internalAddWordHandler() || internalInitHandler(){
		try {
			proceed();			
		}catch(RuntimeException e){	
			throw e;
		} catch (Exception ex) {
			System.out.println(this.msg[thisEnclosingJoinPointStaticPart
					.getId()]);
		}
	}

	void around(): documentChangedHandler() {
		try {
			proceed();
		} catch (BadLocationException ex) {
			System.out.println(ex.getMessage());
		}
	}

	Object around(): ProcessResultsHandler() || MarkerExistsHandler() || internalRunHandler() ||  runHandler() {
		try {
			return proceed();
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	void around(): internalLoadBibtexFilesHandler() || internalNextLineHandler() || internalParseHandler1() {
		try {
			proceed();
		} catch (Exception ex) {
		}
	}

	void around(): CheckPropertiesHandler() || internalInputChangedHandler() || internalCreateFormPageHandler() 
					|| internalSaveFormPageHandler() ||  internalBibTexFormGenHandler() || fullBuildHandler()
					|| LoadTemplatesHandler() || saveHandler() || internalWidgetSelectedHandler() || internalInitialize1Handler()
					|| internalFileLookaheadIteratorHandler() || internalClose2Handler() || internalClose1Handler() || internalCloseHandler(){
		try {
			proceed();
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception ex) {
		}
	}

}
