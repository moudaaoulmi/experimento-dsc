package org.tigris.aopmetrics;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.tools.ant.BuildException;
import org.aspectj.lang.SoftException;

public class AopMetricsCLI {
	private static final String OPT_WORKDIR = "workdir";
	private static final String OPT_RESULTSFILE = "resultsfile";
	private static final String OPT_CONFIGFILE = "configfile";
	private static final String OPT_CLASSPATH = "classpath";
	private static final String OPT_EXPORT = "export";
	private static final String OPT_SOURCELEVEL = "sourcelevel";
	private static final String OPT_VERBOSE = "verbose";
	private static final String OPT_CHARSET = "charset";
	private static final String OPT_HELP = "help";

	private AopMetricsOptions options = new AopMetricsOptions();
	private Options cliOpts = new Options();

	static public void main(String[] args) {
		new AopMetricsCLI().start(args);
	}

	public AopMetricsCLI() {
		Option help = new Option(OPT_HELP, "Prints this message.");
		cliOpts.addOption(help);

		Option workdir = new Option(
				OPT_WORKDIR,
				true,
				"Specifies a directory, where aopmetrics stores temporary files "
						+ "(compiled sources by AspectJ compiler). If the directory doesn't exist, it will be created it.");
		workdir.setArgName("directory");
		cliOpts.addOption(workdir);

		Option resultsFile = new Option(
				OPT_RESULTSFILE,
				true,
				"Specifies a file, where aopmetrics writes results of all "
						+ "measurements. If the file is not specified, then results will be printed to standard output.");
		resultsFile.setArgName("file");
		cliOpts.addOption(resultsFile);

		Option configfile = new Option(
				OPT_CONFIGFILE,
				true,
				"Specifies a config file (.lst), which is commonly used in "
						+ "AspectJ projects. The config file contains paths of all source files (separated by newline).");
		configfile.setArgName("file");
		cliOpts.addOption(configfile);

		Option classpath = new Option(OPT_CLASSPATH, true,
				"Specifies user class path.");
		classpath.setArgName("classpath");
		cliOpts.addOption(classpath);

		Option export = new Option(OPT_EXPORT, true,
				"Specifies an export type. Currently only 'xml' and 'xls' types are supported.");
		export.setArgName("type");
		cliOpts.addOption(export);

		Option sourcelevel = new Option(
				OPT_SOURCELEVEL,
				true,
				"Specifies source compiliance level. The value is passed to the AspectJ "
						+ "compiler. Currently, the compiler supports '1.3', '1.4' and '1.5' levels. Default: '1.4'");
		sourcelevel.setArgName("level");
		cliOpts.addOption(sourcelevel);

		Option verbose = new Option(
				OPT_VERBOSE,
				true,
				"Specifies verbosity level. The attribute is useful for debugging."
						+ " Currently, 'normal', 'info' and 'debug' levels are supported. Default: 'normal'");
		verbose.setArgName("level");
		cliOpts.addOption(verbose);

		Option charset = new Option(OPT_CHARSET, true,
				"Specifies character encoding of given sources.");
		charset.setArgName("charset");
		cliOpts.addOption(charset);
	}

	public void start(String[] args) {
		internalStart(args);
		AopMetricsRunner runner = new AopMetricsRunner();
		internalStart2(runner);
	}

	private void internalStart2(AopMetricsRunner runner) {
		runner.execute(this.options);
	}

	private void internalStart(String[] args) {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(cliOpts, args);
		if (cmd.hasOption(OPT_HELP)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(80, "aopmetrics [options]", "\nwhere:",
					cliOpts, "\n");
			// return;
			throw new SoftException(null);
		}

		readOptions(cmd);
	}

	static public String getClassPathFromClassLoader(ClassLoader cl) {
		if (cl instanceof URLClassLoader) {
			URLClassLoader ucl = (URLClassLoader) cl;
			URL[] urls = ucl.getURLs();
			StringBuffer buf = new StringBuffer();
			String PS = System.getProperty("path.separator");
			for (int i = 0; i < urls.length; i++) {
				buf.append(urls[i].getFile());
				if (i + 1 < urls.length)
					buf.append(PS);
			}

			if (cl.getParent() != null) {
				String parentClassPath = getClassPathFromClassLoader(cl
						.getParent());
				if (parentClassPath != null) {
					buf.append(PS).append(parentClassPath);
				}
			}
			return buf.toString();
		}
		return null;
	}

	private void readOptions(CommandLine cmd) throws BuildException {
		this.options.setRootClasspath(getClassPathFromClassLoader(this
				.getClass().getClassLoader()));

		if (cmd.hasOption(OPT_CONFIGFILE))
			this.options.setConfigfile(new File(cmd
					.getOptionValue(OPT_CONFIGFILE)));
		if (cmd.hasOption(OPT_RESULTSFILE))
			this.options.setResultsfile(new File(cmd
					.getOptionValue(OPT_RESULTSFILE)));
		if (cmd.hasOption(OPT_WORKDIR))
			this.options.setWorkdir(new File(cmd.getOptionValue(OPT_WORKDIR)));

		this.options.setCharset(cmd.getOptionValue(OPT_CHARSET));
		this.options.setClasspath(cmd.getOptionValue(OPT_CLASSPATH));
		this.options.setExport(cmd.getOptionValue(OPT_EXPORT));
		this.options.setSourcelevel(cmd.getOptionValue(OPT_SOURCELEVEL));
		this.options.setVerbose(cmd.getOptionValue(OPT_VERBOSE));

		if (this.options.getConfigfile() != null)
			this.options.setProjectName(this.options.getConfigfile().getName());
		else
			this.options.setProjectName("unknown");
	}

}
