/*
 * Created on 10-Aug-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.wizards.export.pdf;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.preferences.TeXPreferencePage;
import ish.ecletex.preferences.TexExternalToolsPreferencePage;
import ish.ecletex.utils.OSUtils;
import ish.ecletex.utils.SafeExec;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * @author ish
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PDFLatexExportOperation implements IRunnableWithProgress {

	private IPath MainFile;
	private IPath TargetFilename;
	private String Arguments = "-interaction=nonstopmode";
	private String WindowsCommand = "pdflatex.exe";
	private String LinuxCommand = "pdflatex";
	private IPath OutputDir;
	private IPath OutputFilename;
	private IPath ProjectDir;
	private String TexBin;
	
	public PDFLatexExportOperation(IPath MainFile,IPath TargetFile){
		this.MainFile = MainFile;
		this.TargetFilename = TargetFile;
		this.OutputDir = TargetFilename.removeLastSegments(1);
		IPath name = new Path(MainFile.lastSegment());
		name = name.removeFileExtension();
		name = name.addFileExtension("pdf");
		//this.OutputFilename = OutputDir.append(name);
		this.OutputFilename = MainFile.removeFileExtension();
		this.OutputFilename = OutputFilename.addFileExtension("pdf");
		ProjectDir = MainFile.removeLastSegments(1);
		TexBin =
			ecletexPlugin.getDefault().getPreferenceStore().getString(
					TexExternalToolsPreferencePage.TEX_BIN_DIR);
		
	}

	
	
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		monitor.beginTask("Exporting Project as PDF using PDFLatex",3);
		String command ="";
		if(OSUtils.currentOS().matches("windows")){
			IPath pdflatexPath  = new Path(TexBin);
			pdflatexPath = pdflatexPath.append(WindowsCommand);
			command +="\""+pdflatexPath.toOSString()+"\"";
		}else{
			command = "pdflatex";
		}
		
		
		//command += " "+Arguments+" \""+MainFile.toOSString()+"\""+" -output-directory="+"\""+OutputDir.toOSString()+"\"";
		command += " "+Arguments+" \""+MainFile.toOSString()+"\"";
		
		System.out.println("Exporting PDF: "+command);
		SafeExec exec = new SafeExec(command,new String[0],new File(ProjectDir.toOSString()));
		exec.start();
		monitor.worked(1);
		exec.WaitFor();
		monitor.worked(1);
		if(OutputFilename.toFile().exists()){
			OutputFilename.toFile().renameTo(TargetFilename.toFile());
		}
		monitor.worked(1);
		monitor.done();

	}

}
