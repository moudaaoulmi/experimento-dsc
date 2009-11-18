/*
 * Copyright (c) 2003 Frank Sauer. All rights reserved.
 *
 * Licenced under CPL 1.0 (Common Public License Version 1.0).
 * The licence is available at http://www.eclipse.org/legal/cpl-v10.html.
 *
 *
 * DISCLAIMER OF WARRANTIES AND LIABILITY:
 *
 * THE SOFTWARE IS PROVIDED "AS IS".  THE AUTHOR MAKES  NO REPRESENTATIONS OR WARRANTIES,
 * EITHER EXPRESS OR IMPLIED.  TO THE EXTENT NOT PROHIBITED BY LAW, IN NO EVENT WILL THE
 * AUTHOR  BE LIABLE FOR ANY DAMAGES, INCLUDING WITHOUT LIMITATION, LOST REVENUE,  PROFITS
 * OR DATA, OR FOR SPECIAL, INDIRECT, CONSEQUENTIAL, INCIDENTAL  OR PUNITIVE DAMAGES,
 * HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF  LIABILITY, ARISING OUT OF OR RELATED TO
 * ANY FURNISHING, PRACTICING, MODIFYING OR ANY USE OF THE SOFTWARE, EVEN IF THE AUTHOR
 * HAVE BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 *
 * $id$
 */
package net.sourceforge.metrics.calculators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.metrics.core.Log;
import net.sourceforge.metrics.core.Metric;
import net.sourceforge.metrics.core.sources.AbstractMetricSource;
import net.sourceforge.metrics.core.sources.PackageFragmentMetrics;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchResultCollector;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.ISearchPattern;
import org.eclipse.jdt.core.search.SearchEngine;

/**
 * Calculates the Robert Martin Coupling metrics (Ca, Ce, I, A and Dn);
 * 
 * @author Frank Sauer
 */
public class RMartinCouplings extends Calculator {

	/**
	 * @param name
	 */
	public RMartinCouplings() {
		super(RMC);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.metrics.core.ICalculator#calculate(net.sourceforge.metrics.core.sources.AbstractMetricSource)
	 */
	public void calculate(AbstractMetricSource source) throws InvalidSourceException {
		if (source.getJavaElement().getElementType() != IJavaElement.PACKAGE_FRAGMENT)
			throw new InvalidSourceException("Martin Couplings need a package");
		PackageFragmentMetrics pkgSource = (PackageFragmentMetrics)source;
		Metric aff = calculateAfferentCoupling(pkgSource);
		Metric eff = calculateEfferentCoupling(pkgSource);
		Metric a = calculateAbstractness(pkgSource);
		if ((aff != null) && (eff != null)) {
			source.setValue(aff);
			source.setValue(eff);
			Metric i = calculateInstability(aff, eff);
			source.setValue(i);
			if (a != null) {
				Metric d = calculateDistance(a,i);
				source.setValue(a);
				source.setValue(d);
			}
		}
	}
	
	/**
	 * Calculates the normalized distance (Dn) of this package from the 
	 * main sequence, defined as |(A+I-1)|
	 * 
	 * @param source
	 */
	private Metric calculateDistance(Metric a, Metric i) {
		return new Metric(RMD, Math.abs(a.doubleValue() + i.doubleValue() - 1));
	}

	/**
	 * Calculates the number of abstract types (including interfaces) divided
	 * by the total number of types in this package
	 * 
	 * @param source
	 */
	private Metric calculateAbstractness(PackageFragmentMetrics source) {
		try {
			IPackageFragment p = (IPackageFragment)source.getJavaElement();
			ICompilationUnit[] units = p.getCompilationUnits();
			double allTypes = 0;
			double abstractTypes = 0;
			for (int u = 0; u < units.length; u++) {
				IType types[] = units[u].getAllTypes();
				allTypes += types.length;
				for (int t = 0; t < types.length; t++) {
					if (types[t].isInterface()) {
						abstractTypes++;
					} else {
						int flags = types[t].getFlags();
						if (Flags.isAbstract(flags) && Flags.isPublic(flags))
							abstractTypes++;
					}
				}
			}
			return new Metric(RMA, abstractTypes / allTypes);
		} catch (JavaModelException e) {
			Log.logError("Error calculating Abstractness", e);
			return null;
		}
	}

	/**
	 * Calculate ce/(ca+ce)
	 * Note that Ce+Ca can never be 0 because Ce >=1 by the decision based
	 * on the fact that all classes are at least dependent on java.lang.Object, 
	 * even though java.lang stuff is not being collected, the efferent collector
	 * adds one to its search result to deal with this situation.
	 * @param source
	 */
	private Metric calculateInstability(Metric ca, Metric ce) {
		double instability = ce.doubleValue() / (ca.doubleValue() + ce.doubleValue());
		return new Metric(RMI, instability);
	}

	/**
	 * Find all classes outside the given package that depend on things inside this package
	 * Note that nothing outside the default package can depend on things inside it, so the
	 * entire calculation is skipped. Not doing so causes a problem in 3.0M4; the search engine
	 * barfs on the search for some reason.
	 * 
	 * @param source
	 * @return Ca Metric
	 */
	private Metric calculateAfferentCoupling(PackageFragmentMetrics source) {
		IPackageFragment pf = (IPackageFragment) source.getJavaElement();
		if (!pf.isDefaultPackage()) {
			try {
				ISearchPattern pattern = SearchEngine.createSearchPattern(source.getJavaElement(), IJavaSearchConstants.REFERENCES);
				//IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
				IJavaSearchScope scope = createProjectSearchScope(source.getJavaElement());
				SearchEngine searchEngine = new SearchEngine();
				AfferentCollector c = new AfferentCollector(source);
				searchEngine.search(ResourcesPlugin.getWorkspace(), pattern, scope, c);
				return c.getResult();
			} catch (JavaModelException e) {
				Log.logError("Error calculating Afferent Coupling", e);
				return null;
			}
		} else { // BUG #931022 
			return new Metric(CA, 0);
		}
	}

	/**
	 * Create a search scope consisting of all source packages in the element's project
	 * and all referencing projects. Exclude the element itself. This results in a DRAMATIC
	 * performance increase over SearchEngine.createWorkspaceScope().,...
	 * @param element
	 * @return IJavaSearchScope
	 */
	private IJavaSearchScope createProjectSearchScope(IJavaElement element) throws JavaModelException {
		IJavaProject p = (IJavaProject) element.getAncestor(IJavaElement.JAVA_PROJECT);
		List scopeElements = new ArrayList();
		addPackagesInScope(p, scopeElements);
		// find referencing projects and add their packages if any
		IProject[] refProjects = p.getProject().getReferencingProjects();
		if ((refProjects != null)&&(refProjects.length>0)) {
			for (int i = 0; i < refProjects.length; i++) {
				IJavaProject next = JavaCore.create(refProjects[i]);
				if (next != null) {
					addPackagesInScope(next, scopeElements);
				}
			}
		}
		// don't include the package under investigation!
		scopeElements.remove(element); 		
		return SearchEngine.createJavaSearchScope((IJavaElement[])scopeElements.toArray(new IJavaElement[]{}));
	}
	
	private void addPackagesInScope(IJavaProject project, List scope)
		throws JavaModelException {
		IPackageFragment[] packages = project.getPackageFragments();
		for (int i = 0; i < packages.length;i++) {
			if (packages[i].getKind() != IPackageFragmentRoot.K_BINARY) scope.add(packages[i]);
		}
	}

	
	private Metric calculateEfferentCoupling(PackageFragmentMetrics source) {
		ISearchPattern pattern = SearchEngine.createSearchPattern("*", IJavaSearchConstants.PACKAGE, IJavaSearchConstants.REFERENCES, true);
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[]{source.getJavaElement()});
		SearchEngine searchEngine = new SearchEngine();
		try {
			EfferentCollector c = new EfferentCollector(source);
			searchEngine.search(ResourcesPlugin.getWorkspace(), pattern, scope, c);
			return c.getResult();
		} catch (JavaModelException e) {
			Log.logError("Error calculating Efferent Coupling", e);
			return null;
		}
	}
	
	/**
	 * Uses the jdt searchengine to collect all classes outside this package
	 * that depend on things inside this package, in other words, collect references
	 * to this package in the entire workspace, and do not count those found inside
	 * this package
	 * @author Frank Sauer
	 *
	 * To change this generated comment go to 
	 * Window>Preferences>Java>Code Generation>Code Template
	 */
	public static class AfferentCollector implements IJavaSearchResultCollector {

		private IJavaSearchScope packageScope;
		private PackageFragmentMetrics source = null;
		private Set results = null;
		private Metric result = null;
		private Set packages = null;
		
		public AfferentCollector(PackageFragmentMetrics source) {
			this.source = source;
			packageScope = SearchEngine.createJavaSearchScope(new IJavaElement[]{source.getJavaElement()});
		}
		
		/**
		 * 
		 */
		public Metric getResult() {
			return result;			
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jdt.core.search.IJavaSearchResultCollector#aboutToStart()
		 */
		public void aboutToStart() {
			results = new HashSet();
			packages = new HashSet();
		}

		/* count package references <em>outside</em>the current package
		 * @see org.eclipse.jdt.core.search.IJavaSearchResultCollector#accept(org.eclipse.core.resources.IResource, int, int, org.eclipse.jdt.core.IJavaElement, int)
		 */
		public void accept(IResource resource, int start, int end, IJavaElement enclosingElement, int accuracy) throws CoreException {
			if ((enclosingElement != null)&&(!packageScope.encloses(enclosingElement))) {
				IJavaElement pkg = enclosingElement.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
				results.add(resource.getFullPath().toString());
				packages.add(pkg.getElementName());
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jdt.core.search.IJavaSearchResultCollector#done()
		 */
		public void done() {
			result = new Metric(CA, results.size());
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jdt.core.search.IJavaSearchResultCollector#getProgressMonitor()
		 */
		public IProgressMonitor getProgressMonitor() {
			return null;
		}
	}
	
	public static class EfferentCollector implements IJavaSearchResultCollector {

		Metric result = null;
		Set results = null;
		Set packages = null;
		PackageFragmentMetrics source = null;
		
		public EfferentCollector(PackageFragmentMetrics source) {
			this.source = source;
		}
		
		/**
		 * @return
		 */
		public Metric getResult() {
			return result;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jdt.core.search.IJavaSearchResultCollector#aboutToStart()
		 */
		public void aboutToStart() {
			results = new HashSet();
			packages = new HashSet();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jdt.core.search.IJavaSearchResultCollector#accept(org.eclipse.core.resources.IResource, int, int, org.eclipse.jdt.core.IJavaElement, int)
		 */
		public void accept(IResource resource, int start, int end, IJavaElement enclosingElement, int accuracy) throws CoreException {
			if (enclosingElement != null) {
				// don't count references to standard java(x) API
				try {
					String name = getPackageName(enclosingElement, start, end);
					if (!name.startsWith("java")) {
						results.add(resource.getFullPath().toString());
						packages.add(name);
					}
				} catch (StringIndexOutOfBoundsException x) {
					Log.logError("Ce: Error getting package name.", x);
				}
			}
		}

		private String getPackageName(IJavaElement enclosingElement, int start, int end) {
			if (enclosingElement.getElementType() == IJavaElement.IMPORT_DECLARATION) {
				String name = enclosingElement.getElementName();
				int lastDot = name.lastIndexOf('.');
				return name.substring(0,lastDot);
			} else {
				ICompilationUnit unit = (ICompilationUnit) enclosingElement.getAncestor(IJavaElement.COMPILATION_UNIT);
				try {
					String source = unit.getSource();
					return source.substring(start, end);
				} catch (JavaModelException e) {
					return null;
				}
			}
		}

		/* return size of results + 1 (for java.* stuff)
		 * @see org.eclipse.jdt.core.search.IJavaSearchResultCollector#done()
		 */
		public void done() {
			result = new Metric(CE, results.size()+1);
			source.setEfferentDependencies(packages);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jdt.core.search.IJavaSearchResultCollector#getProgressMonitor()
		 */
		public IProgressMonitor getProgressMonitor() {
			return null;
		}
	}
	
}
