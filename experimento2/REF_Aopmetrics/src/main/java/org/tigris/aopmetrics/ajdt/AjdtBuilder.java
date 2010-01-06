package org.tigris.aopmetrics.ajdt;

import java.util.StringTokenizer;

import org.aspectj.ajde.Ajde;
import org.aspectj.ajdt.internal.core.builder.AjBuildManager;
import org.tigris.aopmetrics.TimerHelper;
import org.tigris.aopmetrics.source.Project;

/**
 * @author misto
 */
public class AjdtBuilder {
	private static final String ASPECTJRT = "aspectjrt";

	private TestBuildListener testerBuildListener = new TestBuildListener();

	private String workdir;
	private String projectName;
	private String classpath;
	private String rootClasspath;
	private String configfile;
	private String sourcelevel;
	private String charset;

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public void setConfigfile(String configfile) {
		this.configfile = configfile;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setWorkdir(String workdir) {
		this.workdir = workdir;
	}

	public void setSourceLevel(String level) {
		this.sourcelevel = level;
	}

	public void setRootClasspath(String rootClasspath) {
		this.rootClasspath = rootClasspath;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	private void configureAjde() {
		NullIdeManager.getIdeManager().init();
		configureBuildOptions();
		configureProjectProperties();

		Ajde.getDefault().getBuildManager().addListener(testerBuildListener);
	}

	private void configureBuildOptions() {
		NullIdeBuildOptions buildOptions = (NullIdeBuildOptions) Ajde
				.getDefault().getBuildManager().getBuildOptions();
		if (this.sourcelevel != null) {
			buildOptions.setSourceCompatibilityLevel(this.sourcelevel);
		}
		if (this.charset != null) {
			buildOptions.setCharacterEncoding(this.charset);
		}
	}

	private void configureProjectProperties() {
		NullIdeProperties props = (NullIdeProperties) Ajde.getDefault()
				.getProjectProperties();
		props.setClasspath(prepareClassPath());
		props.setProjectName(this.projectName);
		props.setAjcWorkingDir(this.workdir);
		props.setOutputPath(this.workdir);
	}

	private String prepareClassPath() {
		if (classpath.indexOf(ASPECTJRT) == -1 && this.rootClasspath != null) {
			StringTokenizer tokens = new StringTokenizer(rootClasspath, ":");

			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken();
				if (token.indexOf(ASPECTJRT) != -1)
					return this.classpath + ":" + token;
			}
		}

		return this.classpath;
	}

	public Project build() throws AjdtBuilderException {
		TimerHelper.checkpoint("before init");
		configureAjde();

		MetricsSourceModelBuilder sourceModelBuilder = new MetricsSourceModelBuilder(
				this.projectName);
		AjBuildManager.setAsmHierarchyBuilder(sourceModelBuilder);

		TimerHelper.checkpoint("after init");

		if (!doSynchronousBuild(this.configfile))
			throw new AjdtBuilderException("Build failed");

		TimerHelper.checkpoint("after build");

		Project project = sourceModelBuilder.getProject();
		project.traverse(new AspectRelationshipsBuilder());

		TimerHelper.checkpoint("after modeling");

		return project;
	}

	protected boolean doSynchronousBuild(String config) {
		testerBuildListener.reset();
		Ajde.getDefault().getBuildManager().build(config);
		while (!testerBuildListener.getBuildFinished()) {
			internalDoSynchronousBuild();
		}
		return testerBuildListener.getBuildSucceeded();
	}

	private void internalDoSynchronousBuild() {
		Thread.sleep(200);
	}
}
