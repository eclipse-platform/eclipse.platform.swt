package org.eclipse.swt.examples.launcher;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.ui.IViewPart;import org.eclipse.ui.IWorkbenchPage;import org.eclipse.ui.IWorkbenchPart;import org.eclipse.ui.IWorkbenchPartSite;import java.io.PrintWriter;import java.io.StringWriter;
/** * A launch delegate for starting an Eclipse view */
public class ViewLaunchDelegate implements LaunchDelegate {
	private String pluginViewId;

	public ViewLaunchDelegate(String pluginViewId) {
		this.pluginViewId = pluginViewId;
	}
	public boolean launch(IViewPart hostView, PrintWriter logWriter) {		logWriter.println(LauncherPlugin.getResourceString("run.InvocationSummaryHeader"));		logWriter.println(LauncherPlugin.getResourceString("run.InvokingView",			new Object[] { pluginViewId }));		boolean result = launchHelper(hostView, logWriter);		logWriter.println(LauncherPlugin.getResourceString("run.InvocationSummaryFooter"));		return result;	}
	public boolean launchHelper(IViewPart hostView, PrintWriter logWriter) {
		final IWorkbenchPart workbenchPart = hostView;
		final IWorkbenchPartSite workbenchPartSite = workbenchPart.getSite();
		final IWorkbenchPage workbenchPage = workbenchPartSite.getPage();
		
		try {
			workbenchPage.showView(pluginViewId);
		} catch (Throwable exception) {			logWriter.println(LauncherPlugin.getResourceString("run.error.Invocation"));			logWriter.println(exception.toString());			exception.printStackTrace(logWriter);			return false;		}		return true;	}
}
