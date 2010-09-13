package org.tigris.aopmetrics;

import java.io.File;import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

public class AopMetricsTask extends MatchingTask {
	private AopMetricsOptions options = new AopMetricsOptions();

	private List<FileSet> sources = new LinkedList<FileSet>();
	private Path classpath;

	/**
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {
		verifyOptions();
		AopMetricsRunner runner = new AopMetricsRunner();
		internalExecute(runner);
	}

	private void internalExecute(AopMetricsRunner runner) {
		runner.execute(this.options);
	}

	private String getTaskDefClasspath() {
		ClassLoader cloader = this.getClass().getClassLoader();
		if (cloader instanceof AntClassLoader) {
			AntClassLoader acloader = (AntClassLoader) cloader;
			return acloader.getClasspath();
		} else
			return "";
	}

	private void verifyOptions() throws BuildException {
		this.options.setProjectName(getProject().getName());

		if (options.getWorkdir() == null)
			throw new BuildException("Workdir must be specified.");
		if (options.getWorkdir() != null && !options.getWorkdir().isDirectory()) {
			if (options.getWorkdir().exists())
				throw new BuildException("Workdir is not a regular directory.");
			else
				options.getWorkdir().mkdirs();
		}

		if (this.classpath == null)
			this.classpath = new Path(getProject());

		this.options.setClasspath(this.classpath.toString());
		this.options.setRootClasspath(getTaskDefClasspath());

		if (this.sources.isEmpty() && this.options.getConfigfile() == null)
			throw new BuildException(
					"One of configfile argument and fileset nested element must be specified.");
		if (!this.sources.isEmpty() && this.options.getConfigfile() != null)
			throw new BuildException(
					"Only one of configfile argument and fileset nested element can be specified.");

		if (this.options.getConfigfile() == null) {
			this.options.setConfigfile(new File(this.options.getWorkdir()
					.getAbsolutePath()
					+ "/config.lst"));
			createConfig(this.options.getConfigfile(), sources);
		}
	}

	private void createConfig(File configFile, List<FileSet> filesetlist) {
		FileWriter writer = new FileWriter(configFile);
		for (FileSet fileset : filesetlist) {
			DirectoryScanner ds = fileset.getDirectoryScanner(getProject());
			String[] dsFiles = ds.getIncludedFiles();
			for (String file : dsFiles) {
				writer.write(ds.getBasedir().getAbsolutePath() + File.separator
						+ file + '\n');
			}
		}
		writer.close();
	}

	/* --- task's attributes --- */

	public void setWorkdir(File workdir) {
		this.options.setWorkdir(workdir);
	}

	public void setResultsfile(File resultsfile) {
		this.options.setResultsfile(resultsfile);
	}

	public void setConfigfile(File configfile) {
		this.options.setConfigfile(configfile);
	}

	public void setExport(String export) {
		this.options.setExport(export);
	}

	public void setSourcelevel(String sourcelevel) {
		this.options.setSourcelevel(sourcelevel);
	}

	public void setVerbose(String verbose) {
		this.options.setVerbose(verbose);
	}

	public void setCharset(String charset) {
		this.options.setCharset(charset);
	}

	/* --- task's nested elements --- */

	public void addConfiguredClasspath(Path classpath) {
		if (this.classpath == null) {
			this.classpath = classpath;
		} else {
			this.classpath.append(classpath);
		}
	}

	public void addFileSet(FileSet fileset) {
		this.sources.add(fileset);
	}

}
