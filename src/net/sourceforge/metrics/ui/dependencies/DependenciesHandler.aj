package net.sourceforge.metrics.ui.dependencies;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaModelException;

import classycle.graph.StrongComponent;
import classycle.graph.Vertex;

import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;

import exception.ExceptionHandler;
@ExceptionHandler
public privileged aspect DependenciesHandler {

	pointcut createDependenciesHandler(): call(public void DependencyGraphPanel.createDependencies(..)) && withincode(public DependencyGraphPanel.new(..));

	pointcut addKnotHandler(): execution(private void DependencyGraphPanel.addKnot(Node, Node, StrongComponent,int));

	pointcut actionPerformedHandler(): execution(private void DependencyGraphPanel.internalAdd(Node));

	pointcut internalRunHandler1(): execution(private void Knot.internalRun(Map, Map));

	pointcut internalFindShortestPathHandler(): execution(private Vertex[] PathFinder.internalFindShortestPath(Vertex, Vertex, Map,List));

	pointcut internalExportButtonClickedHandler(): execution(private void PathFinderDialog.internalExportButtonClicked(File));

	pointcut internalRunHandler2(): execution(private void TangleAnalyzer.internalRun());

	pointcut getDependenciesHandler(): execution(private void TangleAnalyzer.getDependencies(List));

	pointcut internalGetPackageFragmentsHandler(): execution(private List TangleAnalyzer.internalGetPackageFragments(..));

	declare soft: TGException :createDependenciesHandler()|| addKnotHandler()||actionPerformedHandler()||internalRunHandler1();
	declare soft: JavaModelException : getDependenciesHandler()||internalGetPackageFragmentsHandler();
	declare soft: Throwable :internalExportButtonClickedHandler();
	declare soft: InvocationTargetException :internalRunHandler2();
	declare soft: InterruptedException :internalRunHandler2();

	List around(): internalGetPackageFragmentsHandler() {
		try {
			return proceed();
		} catch (JavaModelException e) {
			e.printStackTrace();
			return new ArrayList();
		}
	}

	void around(): getDependenciesHandler() {
		try {
			proceed();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}

	void around(): internalRunHandler2() {
		try {
			proceed();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	void around(): internalExportButtonClickedHandler() {
		try {
			proceed();
		} catch (Throwable e) {

		}
	}

	Vertex[] around(): internalFindShortestPathHandler() {
		try {
			return proceed();
		} catch (IllegalArgumentException e) {
			return null; // no path found
		}
	}

	void around(): internalRunHandler1() {
		try {
			proceed();
		} catch (TGException e) {
			e.printStackTrace();
		}
	}

	void around(): actionPerformedHandler() {
		try {
			proceed();
		} catch (TGException e1) {
			e1.printStackTrace();
		}
	}

	void around(): addKnotHandler() {
		try {
			proceed();
		} catch (TGException e) {
			e.printStackTrace();
		}
	}

	void around(): createDependenciesHandler() {
		try {
			proceed();
		} catch (TGException e) {
			e.printStackTrace();
		}
	}

}
