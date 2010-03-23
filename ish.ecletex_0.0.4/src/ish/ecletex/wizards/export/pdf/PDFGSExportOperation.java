/*
 * Created on Sep 30, 2004
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
 * @author Ian Hartney
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PDFGSExportOperation implements IRunnableWithProgress {

	private IPath MainFile;
	private IPath TargetFilename;
	private IPath OutputDir;
	private IPath OutputPDFFilename;
	private IPath OutputPSFilename;
	private IPath DVIFile;
	private IPath ProjectDir;
	private String TexBin;
	private String GSBin;
	String ps_command = "";
	String pdf_command = "";
	
	public PDFGSExportOperation(IPath MainFile,IPath TargetFile){
		this.MainFile = MainFile;
		
		this.TargetFilename = TargetFile;
		
		this.OutputDir = TargetFilename.removeLastSegments(1);
		
		this.OutputPDFFilename = MainFile.removeFileExtension();
		this.OutputPDFFilename = OutputPDFFilename.addFileExtension("pdf");
		
		this.OutputPSFilename = MainFile.removeFileExtension();
		this.OutputPSFilename = OutputPSFilename.addFileExtension("ps");
		
		this.DVIFile = MainFile.removeFileExtension();
		this.DVIFile = DVIFile.addFileExtension("dvi");
		
		ProjectDir = MainFile.removeLastSegments(1);
		TexBin =
			ecletexPlugin.getDefault().getPreferenceStore().getString(
					TexExternalToolsPreferencePage.TEX_BIN_DIR);
		
		GSBin = ecletexPlugin.getDefault().getPreferenceStore().getString(TexExternalToolsPreferencePage.GS_BIN_DIR);
		
		

		if(OSUtils.currentOS().matches("windows")){
			//IPath dvipspath  = new Path(TexBin);
			//dvipspath = dvipspath.append("dvips.exe");
			//ps_command +="\""+dvipspath.toOSString()+"\"";
			ps_command = "dvips.exe";
			IPath gspath = new Path(GSBin);
			gspath = gspath.append("gswin32c.exe");
			pdf_command = "\""+gspath.toOSString()+"\"";
			
		}else{
			ps_command = "dvips";
			pdf_command = "gs";
		}
		
		
	}

	
	
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		monitor.beginTask("Exporting Project as PDF using DVIPS -> PS GS -> PDF",5);
		
		ps_command+=" \""+DVIFile.toOSString()+"\"";
		String path = "PATH=\""+TexBin+"\"";
		System.out.println("PS Command: "+ps_command);
		
		SafeExec ps_exec = new SafeExec(ps_command,new String[]{path},new File(ProjectDir.toOSString()));
		ps_exec.start();
		monitor.worked(1);
		ps_exec.WaitFor();
		monitor.worked(1);
		
		if(OutputPSFilename.toFile().exists()){
			pdf_command += " -q -dSAFER -dNOPAUSE -sDEVICE=pdfwrite -dBATCH -sOutputFile=\""+TargetFilename.toOSString()+"\""+" \""+OutputPSFilename.toOSString()+"\"";
			System.out.println("PDF Command: "+pdf_command);
			SafeExec pdf_exec = new SafeExec(pdf_command,new String[0],new File(ProjectDir.toOSString()));
			pdf_exec.start();
			monitor.worked(1);
			pdf_exec.WaitFor();
			monitor.worked(1);
			
		}
		
		OutputPSFilename.toFile().delete();
		monitor.worked(1);
	}

}
