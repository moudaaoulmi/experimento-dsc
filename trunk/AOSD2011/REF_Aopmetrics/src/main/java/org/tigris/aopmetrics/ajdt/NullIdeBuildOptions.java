package org.tigris.aopmetrics.ajdt;

import java.util.Map;
import java.util.Set;

import org.aspectj.ajde.BuildOptionsAdapter;

/**
 *
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: NullIdeBuildOptions.java,v 1.1 2006/04/20 23:37:20 misto Exp $
 */
public class NullIdeBuildOptions implements BuildOptionsAdapter {
	private Map javaOptionsMap;
	private boolean useJavacMode;
	private String workingOutputPath;
	private boolean preprocessMode;
	private boolean sourceOnePointFourMode;
	private boolean incrementalMode;
	private boolean lenientSpecMode;
	private boolean strictSpecMode;
	private boolean portingMode;
	private String nonStandardOptions;
	private String complianceLevel;
	private String sourceCompatibilityLevel;
	private Set warnings;
	private Set debugLevel;
	private boolean noImportError;
	private boolean preserveAllLocals;
	private String characterEncoding;


	public String getComplianceLevel() {
		return complianceLevel;
	}
	public void setComplianceLevel(String complianceLevel) {
		this.complianceLevel = complianceLevel;
	}
	public Set getDebugLevel() {
		return debugLevel;
	}
	public void setDebugLevel(Set debugLevel) {
		this.debugLevel = debugLevel;
	}
	public boolean getIncrementalMode() {
		return incrementalMode;
	}
	public void setIncrementalMode(boolean incrementalMode) {
		this.incrementalMode = incrementalMode;
	}
	public Map getJavaOptionsMap() {
		return javaOptionsMap;
	}
	public void setJavaOptionsMap(Map javaOptionsMap) {
		this.javaOptionsMap = javaOptionsMap;
	}
	public boolean getLenientSpecMode() {
		return lenientSpecMode;
	}
	public void setLenientSpecMode(boolean lenientSpecMode) {
		this.lenientSpecMode = lenientSpecMode;
	}
	public boolean getNoImportError() {
		return noImportError;
	}
	public void setNoImportError(boolean noImportError) {
		this.noImportError = noImportError;
	}
	public String getNonStandardOptions() {
		return nonStandardOptions;
	}
	public void setNonStandardOptions(String nonStandardOptions) {
		this.nonStandardOptions = nonStandardOptions;
	}
	public boolean getPortingMode() {
		return portingMode;
	}
	public void setPortingMode(boolean portingMode) {
		this.portingMode = portingMode;
	}
	public boolean getPreprocessMode() {
		return preprocessMode;
	}
	public void setPreprocessMode(boolean preprocessMode) {
		this.preprocessMode = preprocessMode;
	}
	public boolean getPreserveAllLocals() {
		return preserveAllLocals;
	}
	public void setPreserveAllLocals(boolean preserveAllLocals) {
		this.preserveAllLocals = preserveAllLocals;
	}
	public String getSourceCompatibilityLevel() {
		return sourceCompatibilityLevel;
	}
	public void setSourceCompatibilityLevel(String sourceCompatibilityLevel) {
		this.sourceCompatibilityLevel = sourceCompatibilityLevel;
	}
	public boolean getSourceOnePointFourMode() {
		return sourceOnePointFourMode;
	}
	public void setSourceOnePointFourMode(boolean sourceOnePointFourMode) {
		this.sourceOnePointFourMode = sourceOnePointFourMode;
	}
	public boolean getStrictSpecMode() {
		return strictSpecMode;
	}
	public void setStrictSpecMode(boolean strictSpecMode) {
		this.strictSpecMode = strictSpecMode;
	}
	public boolean getUseJavacMode() {
		return useJavacMode;
	}
	public void setUseJavacMode(boolean useJavacMode) {
		this.useJavacMode = useJavacMode;
	}
	public Set getWarnings() {
		return warnings;
	}
	public void setWarnings(Set warnings) {
		this.warnings = warnings;
	}
	public String getWorkingOutputPath() {
		return workingOutputPath;
	}
	public void setWorkingOutputPath(String workingOutputPath) {
		this.workingOutputPath = workingOutputPath;
	}
	public String getCharacterEncoding() {
		return characterEncoding;
	}
	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}
}
