package org.eclipse.swt.examples.launcher;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.core.runtime.*;import org.eclipse.ui.*;
/** * A launch delegate for starting an Eclipse view. */
public class ViewLaunchDelegate implements LaunchDelegate {
	private String pluginViewId;

	public ViewLaunchDelegate(String pluginViewId) {
		this.pluginViewId = pluginViewId;
	}
	public boolean launch(IViewPart hostView) {		boolean result = launchHelper(hostView);		return result;	}
	public boolean launchHelper(IViewPart hostView) {
		final IWorkbenchPart workbenchPart = hostView;
		final IWorkbenchPartSite workbenchPartSite = workbenchPart.getSite();
		final IWorkbenchPage workbenchPage = workbenchPartSite.getPage();
		
		try {
			workbenchPage.showView(pluginViewId);
			return true;		} catch (PartInitException e) {			LauncherPlugin.logError(LauncherPlugin.getResourceString("run.error.Invocation"), e);		}		return false;	}
}
