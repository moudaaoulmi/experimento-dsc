/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * 	   AMC 01.20.2003 extended to support AspectJ 1.1 options
 * ******************************************************************/


package org.tigris.aopmetrics.ajdt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aspectj.ajde.ProjectPropertiesAdapter;

/**
 * @author	Mik Kersten
 */
public class NullIdeProperties implements ProjectPropertiesAdapter {

	private List buildConfigFiles = new ArrayList();
	private List projectSourceFiles = new ArrayList();

	private Set inJars = new HashSet();
	private Set inpath = new HashSet();
	private Set sourceRoots = new HashSet();
	private Set aspectPath = new HashSet();
	private String outJar;
	private String classpath;
	private String projectName;
	private String outputPath;
	private String ajcWorkingDir;
	private String rootProjectDir;
	private String projectSourcePath;
	private String lastActiveBuildConfigFile;
	private String defaultBuildConfigFile;


	public void addBuildConfigFile(String configFile) { this.buildConfigFiles.add(configFile); }
	public List getBuildConfigFiles() {
		return buildConfigFiles;
	}

    public void setRootProjectDir(String rootProjectDir) { this.rootProjectDir = rootProjectDir; }
    public String getRootProjectDir() {
    	return this.rootProjectDir;
    }

    public void addProjectSourceFile(File sourceFile) { this.projectSourceFiles.add(sourceFile); }
    public List getProjectSourceFiles() {
    	return this.projectSourceFiles;	
    }

    public void setProjectSourcePath(String projectSourcePath) { this.projectSourcePath = projectSourcePath; }
    public String getProjectSourcePath() {
    	return this.projectSourcePath;
    }

    public void setClasspath(String classpath) { this.classpath = classpath; }
    public String getClasspath() {
   		return 	System.getProperty("sun.boot.class.path")  
//   				 + File.pathSeparator + System.getProperty("java.class.path")
    			 + (this.classpath != null ? File.pathSeparator + this.classpath : "");
    }

    public void setOutputPath(String outputPath) { this.outputPath = outputPath; }
    public String getOutputPath() {
    	return this.outputPath; 
    }

    public void setAjcWorkingDir(String ajcWorkingDir) { this.ajcWorkingDir = ajcWorkingDir; }
    public String getAjcWorkingDir() {
    	return this.ajcWorkingDir;	
    }

    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getProjectName() {
    	return this.projectName;	
    }
 
	public String getDefaultBuildConfigFile() { return defaultBuildConfigFile; }
	public void setDefaultBuildConfigFile(String defaultBuildConfigFile) {
		this.defaultBuildConfigFile = defaultBuildConfigFile;
	}
	
	public String getLastActiveBuildConfigFile() {	return lastActiveBuildConfigFile; }
	public void setLastActiveBuildConfigFile(String lastActiveBuildConfigFile) {
		this.lastActiveBuildConfigFile = lastActiveBuildConfigFile;
	}

    public String getBootClasspath() { return null; }
    public String getClassToExecute() { return null; }
    public String getExecutionArgs() { return null; }
    public String getVmArgs() { return null;}
    
    public void addInJar( File jar ) { this.inJars.add(jar); }
    public Set getInJars( ) {
    	return inJars;
    }
    
    public void addInpath( File path) { this.inpath.add(path); }
    public Set getInpath( ) {
    	return inpath;
    }
    
	public Map getSourcePathResources() {
		return null;
/*		File srcBase = new File(getProjectSourcePath());
		File[] fromResources = FileUtil.listFiles(srcBase, new FileFilter() {
			public boolean accept(File pathname) {
				String name = pathname.getName().toLowerCase();
				return !name.endsWith(".class") && !name.endsWith(".java") && !name.endsWith(".aj");
			}
		});
		Map<String,File> map = new HashMap<String,File>();
		for (int i = 0; i < fromResources.length; i++) {
			String normPath = FileUtil.normalizedPath(fromResources[i] ,srcBase);
			map.put(normPath, fromResources[i]);

		}
		return map;*/
	}

	public void setOutJar( String jar ){ this.outJar = jar; }
    public String getOutJar() {
    	return outJar;
    }
    
    public void addSourceRoot(File sourceRoot) { this.sourceRoots.add(sourceRoot); }
    public Set getSourceRoots() {
    	return sourceRoots;
    }

	public void addAspectPath(File path) { this.aspectPath.add(path); }
    public Set getAspectPath() {
    	return aspectPath;
    }
}
