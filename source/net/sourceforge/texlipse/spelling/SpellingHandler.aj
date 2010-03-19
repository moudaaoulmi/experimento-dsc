package net.sourceforge.texlipse.spelling;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.sourceforge.texlipse.TexlipsePlugin;
import net.sourceforge.texlipse.builder.BuilderRegistry;
import org.aspectj.lang.SoftException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.ISourceViewer;

public privileged aspect SpellingHandler {

	pointcut internalRunHandler(): execution(private void SpellCheckAction.internalRun(IFile) );

	pointcut internalAddWordToAspellHandler(): execution(private static void SpellChecker.internalAddWordToAspell(String, String,
			String[]));

	pointcut internalCheckProgramHandler(): execution(private int SpellChecker.internalCheckProgram(int));

	pointcut internalStartProgramHandler(): execution(private void SpellChecker.internalStartProgram());

	pointcut internalStartProgramHandlerSoft(): execution(private boolean SpellChecker.startProgram());

	pointcut internalStartProgram1Handler(): execution(private boolean SpellChecker.internalStartProgram1());

	pointcut internalCheckLineSpellingHandler(): execution(private void SpellChecker.internalCheckLineSpelling(int, String,
			List<String>) );
	
	pointcut internalGetSpellingProposalHandler(): execution(private static IMarker[] SpellChecker.internalGetSpellingProposal(IResource,IMarker[]));

	pointcut internalGetSpellingProposalHandlerSoft(): execution(public static ICompletionProposal[] SpellChecker.getSpellingProposal(int,ISourceViewer));

	pointcut internalGetResolutionsHandler(): execution(private Object SpellingResolutionGenerator.internalGetResolutions(IMarker));

	pointcut internalCheckLineSpellingHandler1(): execution(private void SpellChecker.internalCheckLineSpelling());

	declare soft: CoreException: internalRunHandler() || internalGetSpellingProposalHandler() || internalGetResolutionsHandler();
	
	declare soft: Exception: internalAddWordToAspellHandler()||internalCheckProgramHandler();
	
	declare soft: IOException: internalStartProgramHandler()||internalStartProgram1Handler()||internalCheckLineSpellingHandler();
	
	declare soft: InterruptedException: internalCheckLineSpellingHandler1();
	
	void around(): internalCheckLineSpellingHandler1() {
		try {
			proceed();
		} catch (InterruptedException e) {
			// No problem
		}
	}

	Object around(): internalGetResolutionsHandler() {
		try {
			return proceed();
		} catch (CoreException e) {
			return null;
		}
	}

	ICompletionProposal[] around(): internalGetSpellingProposalHandlerSoft() {
		try {
			return proceed();
		} catch (SoftException e) {
			return null;
		}
	}

	//XXX rethrow
	IMarker[] around(): internalGetSpellingProposalHandler(){
		try {
			return proceed();
		} catch (CoreException e) {
			// return null;
			throw new SoftException(e);
		}
	}

	

	void around(int lineNumber, String lineToPost):internalCheckLineSpellingHandler() && args(lineNumber, lineToPost,*) {
		try {
			proceed(lineNumber, lineToPost);
		} catch (IOException e) {
			BuilderRegistry.printToConsole(TexlipsePlugin
					.getResourceString("spellProgramStartError"));
			TexlipsePlugin.log("aspell error at line " + lineNumber + ": "
					+ lineToPost, e);
		}
	}

	boolean around(): internalStartProgram1Handler() {
		try {
			return proceed();
		} catch (IOException e) {
			TexlipsePlugin.log("Aspell died", e);
			BuilderRegistry.printToConsole(TexlipsePlugin
					.getResourceString("spellProgramStartError"));
			return false;
		}
	}

	boolean around(): internalStartProgramHandlerSoft(){
		try {
			return proceed();
		} catch (SoftException e) {
			return false;
		}
	}

	void around(SpellChecker spe): internalStartProgramHandler() && this(spe){
		try {
			proceed(spe);
		} catch (IOException e) {
			spe.spellProgram = null;
			spe.input = null;
			spe.output = null;
			BuilderRegistry.printToConsole(TexlipsePlugin
					.getResourceString("spellProgramStartError"));
			// return false;
			throw new SoftException(null);
		}
	}

	int around(int exitCode): internalCheckProgramHandler()&& args(exitCode) {
		try {
			proceed(exitCode);
		} catch (IllegalThreadStateException e) {
			// program is still running, good
		}
		return exitCode;
	}

	void around(String word): internalAddWordToAspellHandler() && args(word,..) {
		try {
			proceed(word);
		} catch (Exception e) {
			BuilderRegistry.printToConsole("Error adding word \"" + word
					+ "\" to Aspell user dict\n");
			TexlipsePlugin.log("Adding word \"" + word
					+ "\" to Aspell user dict", e);
		}
	}

	void around(): internalRunHandler() {
		try {
			proceed();
		} catch (CoreException e) {
		}
	}

}
