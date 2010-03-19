package org.tigris.aopmetrics;

import java.io.File;



public class AopMetricsOptions {
	private File workdir;
	private File resultsfile;
	private File configfile;

	private String classpath;
	private String rootClasspath;

	private String export;
	private String sourcelevel;
	private String charset;
	private String verbose;
	private String projectName;


	public void setWorkdir(File workdir) {
		this.workdir = workdir;
	}

	public void setResultsfile(File resultsfile) {
		this.resultsfile = resultsfile;
	}

	public void setConfigfile(File configfile) {
		this.configfile = configfile;
	}

	public void setExport(String export) {
		this.export = export;
	}

	public void setSourcelevel(String sourcelevel) {
		this.sourcelevel = sourcelevel;
	}

	public void setVerbose(String verbose) {
		this.verbose = verbose;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getClasspath() {
		return classpath;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCharset() {
		return charset;
	}

	public File getConfigfile() {
		return configfile;
	}

	public String getExport() {
		return export;
	}

	public File getResultsfile() {
		return resultsfile;
	}

	public String getSourcelevel() {
		return sourcelevel;
	}

	public String getVerbose() {
		return verbose;
	}

	public File getWorkdir() {
		return workdir;
	}

	public String getRootClasspath() {
		return rootClasspath;
	}

	public void setRootClasspath(String rootClasspath) {
		this.rootClasspath = rootClasspath;
	}

	
}
