package exception;

import java.net.MalformedURLException;
import java.net.URL;

import net.sourceforge.metrics.builder.MetricsBuilder;
import net.sourceforge.metrics.builder.ProgressQueue;
import net.sourceforge.metrics.calculators.LinesOfCode;
import net.sourceforge.metrics.calculators.RMartinCouplings;
import net.sourceforge.metrics.core.CalculatorDescriptor;
import net.sourceforge.metrics.core.ExportDescriptor;
import net.sourceforge.metrics.core.ICalculator;
import net.sourceforge.metrics.core.IExporter;
import net.sourceforge.metrics.core.Log;
import net.sourceforge.metrics.core.Metric;
import net.sourceforge.metrics.core.MetricDescriptor;
import net.sourceforge.metrics.core.MetricsPlugin;
import net.sourceforge.metrics.core.sources.AbstractMetricSource;
import net.sourceforge.metrics.core.sources.Cache;
import net.sourceforge.metrics.core.sources.CompilationUnitMetrics;
import net.sourceforge.metrics.core.sources.PackageFragmentMetrics;
import net.sourceforge.metrics.core.sources.PackageFragmentRootMetrics;
import net.sourceforge.metrics.properties.MetricsPropertyPage;
import net.sourceforge.metrics.ui.DependencyGraphView;
import net.sourceforge.metrics.ui.MetricsTable;
import net.sourceforge.metrics.ui.OpenMetricsViewAction;
import net.sourceforge.metrics.ui.dependencies.DependencyGraphPanel;
import net.sourceforge.metrics.ui.dependencies.EclipseNode;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.ISearchPattern;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.touchgraph.graphlayout.TGException;

public privileged aspect GeneralExceptionHandler {

	//0
	pointcut MetricsBuilder_hasErrorsHandler(): execution(private boolean MetricsBuilder.hasErrors(IProject));
	//1
	pointcut ProgressQueue_internalHandler(): execution(private void ProgressQueue.NotifierThread.internal(net.sourceforge.metrics.builder.ProgressQueue.Command));
	//2 
	pointcut CompilationUnitMetrics_internalInitializeChildrenHandler(): execution(private void CompilationUnitMetrics.internalInitializeChildren(ICompilationUnit));
	//3 
	pointcut PackageFragmentMetrics_internalInitializeChildrenHandler1(): execution(private void PackageFragmentMetrics.internalInitializeChildren(IPackageFragment));
	//4 
	pointcut PackageFragmentRootMetrics_internalInitializeChildrenHandler2(): execution(private void PackageFragmentRootMetrics.internalInitializeChildren(IPackageFragmentRoot));
	//5 
	pointcut Cache_closeHandler(): execution(public void Cache.close());
	//6 
	pointcut Cache_clearHandler1(): execution(public void Cache.clear());
	//7 
	pointcut Cache_commitHandler(): execution(public void Cache.commit());
	//8 
	pointcut AbstractMetricSource_internalCheckRangeHandler(): execution(private void AbstractMetricSource.internalCheckRange(Metric, MetricDescriptor));
	//9 
	pointcut Cache_initRecordManagerHandler(): execution(private void Cache.initRecordManager());
	//10
	pointcut EclipseNode_internalRunHandler(): execution(private void EclipseNode.internalRun());
	//11 
	pointcut DependencyGraphPanel_showMessageHandler(): execution(public void DependencyGraphPanel.showMessage(String));
	//12 
	pointcut RMartinCouplings_internalCalculateEfferentCouplingHandler(): execution(private Metric RMartinCouplings.internalCalculateEfferentCoupling(PackageFragmentMetrics, ISearchPattern,
			IJavaSearchScope, SearchEngine));
	//13
	pointcut RMartinCouplings_internalCalculateAfferentCouplingHandler(): execution(private Metric RMartinCouplings.internalCalculateAfferentCoupling(PackageFragmentMetrics));
	//14
	pointcut RMartinCouplings_calculateAbstractnessHandler(): execution(private Metric RMartinCouplings.calculateAbstractness(PackageFragmentMetrics));
	//15
	pointcut LinesOfCode_internalGetSourceHandler(): execution(private String LinesOfCode.internalGetSource(int, int,ICompilationUnit));
	//16 
	pointcut ExportDescriptor_createExporterHandler(): execution(public IExporter ExportDescriptor.createExporter());
	//17 
	pointcut ExportDescriptor_makeImageURLHandler(): execution(private static URL MetricsPlugin.makeImageURL(String));
	//18
	pointcut CalculatorDescriptor_createCalculatorHandler(): execution(public ICalculator CalculatorDescriptor.createCalculator());
	//19
	pointcut MetricsTable_setMetricsHandler(): execution(public void MetricsTable.setMetrics(AbstractMetricSource));
	//20 
	pointcut IWorkbenchPage_internalRunHandler2(): call(public IViewPart IWorkbenchPage.showView(..)) && withincode(public void OpenMetricsViewAction.run());
	//21
	pointcut DependencyGraphView_createAWTFrameHandler(): execution(private java.awt.Frame DependencyGraphView.createAWTFrame(Composite));
	//22 
	pointcut IResource_deleteMarkersHandler(): call(public void IResource.deleteMarkers(String, boolean, int)) && withincode(public void SelectionListener.widgetSelected(SelectionEvent));
	//23 "Could not persist property" CoreException 24 "Error changing project nature." Throwable
	pointcut MetricsPropertyPage_internalPersistStateHandler(): execution(private void MetricsPropertyPage.EnableMetricsTable.internalPersistState(TableTreeItem[], int,
			String, String, String));
	//24 "Error changing project nature." Throwable
	pointcut MetricsPropertyPage_performOkHandler(): execution(public boolean MetricsPropertyPage.performOk());
	//25 "Error gettng project nature." 
	pointcut MetricsPropertyPage_internalCreateContentsHandler(): execution(private void MetricsPropertyPage.internalCreateContents(IProject));
	
	declare soft: CoreException: CalculatorDescriptor_createCalculatorHandler()
		|| IResource_deleteMarkersHandler()
		|| MetricsPropertyPage_internalPersistStateHandler();
	
	declare soft: MalformedURLException: ExportDescriptor_makeImageURLHandler();
	
	declare soft: CoreException: MetricsBuilder_hasErrorsHandler();
	
	declare soft: Throwable: ProgressQueue_internalHandler()
		|| CompilationUnitMetrics_internalInitializeChildrenHandler()
		|| Cache_initRecordManagerHandler()
		|| Cache_clearHandler1()
		|| Cache_commitHandler()
		|| AbstractMetricSource_internalCheckRangeHandler()
		|| Cache_closeHandler()
		|| ExportDescriptor_createExporterHandler()
		|| MetricsTable_setMetricsHandler()
		|| DependencyGraphView_createAWTFrameHandler()
		|| MetricsPropertyPage_performOkHandler()
		|| MetricsPropertyPage_internalCreateContentsHandler();
	
	declare soft: PartInitException: EclipseNode_internalRunHandler()
		|| IWorkbenchPage_internalRunHandler2();
	declare soft: TGException: DependencyGraphPanel_showMessageHandler();
	
	declare soft: JavaModelException : PackageFragmentMetrics_internalInitializeChildrenHandler1()
		|| PackageFragmentRootMetrics_internalInitializeChildrenHandler2()
		|| EclipseNode_internalRunHandler()
		|| RMartinCouplings_internalCalculateEfferentCouplingHandler()
		|| RMartinCouplings_internalCalculateAfferentCouplingHandler()
		|| RMartinCouplings_calculateAbstractnessHandler()
		|| LinesOfCode_internalGetSourceHandler();
	
	Object around(): ProgressQueue_internalHandler()
			|| CompilationUnitMetrics_internalInitializeChildrenHandler()
			|| Cache_initRecordManagerHandler()
			|| Cache_clearHandler1()
			|| Cache_commitHandler()
			|| AbstractMetricSource_internalCheckRangeHandler()
			|| Cache_closeHandler()
			|| MetricsTable_setMetricsHandler()
			|| DependencyGraphView_createAWTFrameHandler()
			|| MetricsPropertyPage_performOkHandler()
			|| MetricsPropertyPage_internalCreateContentsHandler()
	{
		Object result = null;
		try {
			result = proceed();
		} catch (Throwable e) {
			String messageText = getMessageErro(thisEnclosingJoinPointStaticPart.getId());
			Log.logError(messageText, e);
		}
		return result;
	}
	
	Object around(): MetricsBuilder_hasErrorsHandler()
		|| PackageFragmentMetrics_internalInitializeChildrenHandler1()
		|| PackageFragmentRootMetrics_internalInitializeChildrenHandler2() 
		|| EclipseNode_internalRunHandler()
		|| DependencyGraphPanel_showMessageHandler()
		|| RMartinCouplings_internalCalculateEfferentCouplingHandler()
		|| RMartinCouplings_internalCalculateAfferentCouplingHandler()
		|| RMartinCouplings_calculateAbstractnessHandler()
		|| LinesOfCode_internalGetSourceHandler()
		|| IWorkbenchPage_internalRunHandler2()
		|| MetricsPropertyPage_internalPersistStateHandler()
	{
		Object result = null;
		try {
			result = proceed();
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			String messageText = getMessageErro(thisEnclosingJoinPointStaticPart.getId());
			Log.logError(messageText, e);
		}
		return result;
	}
	
	private String getMessageErro(int pointcutIndex){
		String result = "";
		
		switch (pointcutIndex) {
		case 0:
			result = "CoreException getting build errors: ";
			break;
		case 1:
			result = "uncaught exception in metrics notifier";
			break;
		case 2: 
			result = "Could not delete markers";
			break;
		case 3: 
			result = "PackageFragmentMetrics.initializeChildren";
			break;
		case 4: 
			result = "PackageFragmentRoot.initializeChildren:";
			break;
		case 5: 
			result = "Could not close jdbm database";
			break;
		case 6: 
			result = "Error deleting database";
			break;
		case 7: 
			result = "Could not commit latest changes.";
			break;
		case 8: 
			result = "could not get resource to add marker";
			break;
		case 9: 
			result = "Could not open/create jdbm database";
			break;
		case 10: 
			result = "Node.openInEditor";
			break;
		case 11: 
			result = "DepencyGraphPanel.showMessage:";
			break;
		case 12: 
			result = "Error calculating Efferent Coupling";
			break;
		case 13: 
			result = "Error calculating Afferent Coupling";
			break;
		case 14: 
			result = "Error calculating Abstractness";
			break;
		case 15: 
			result = "LinesOfCode:getSource";
			break;
		case 16: 
			result = "ExportDescriptor::createExporter";
			break;
		case 17: 
			result = "Image not found";
			break;
		case 18: 
			result = "CalculatorDescriptor::createCalculator";
			break;
		case 19: 
			result = "MetricsTable::setMetrics" ;
			break;
		case 20: 
			result = "Could not create metrics view";
			break;
		case 21: 
			result = "Could not embed awt panel using reflection" ;
			break;
		case 22:
			result = "Could not delete markers";
			break;
		case 23:
			result = "Could not persist property";
			break; 
		case 24:
			result = "Error changing project nature." ;
			break;
		case 25:
			result = "Error gettng project nature.";
			break;
		}
		return result;
	}
	
}
