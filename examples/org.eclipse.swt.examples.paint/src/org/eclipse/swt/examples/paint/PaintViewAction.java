package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.core.runtime.IConfigurationElement;import org.eclipse.core.runtime.IExecutableExtension;import org.eclipse.jface.action.IAction;import org.eclipse.jface.viewers.ISelection;import org.eclipse.swt.widgets.Display;import org.eclipse.ui.IViewActionDelegate;import org.eclipse.ui.IViewPart;

/**
 * 
 * @see IViewActionDelegate
 */
public class PaintViewAction implements IViewActionDelegate, IExecutableExtension {
	private PaintView paintView;
	private String id;
	
	/**
	 * Initializes this action delegate with the view it will work in.
	 *
	 * @param view the view that provides the context for this delegate
	 */
	public void init(IViewPart view) {
		if (view != null && view instanceof PaintView) {
			paintView = (PaintView) view;
		} else throw new IllegalArgumentException("Expected an instance of PaintView");
	}
	
	/**
	 * Performs this action.
	 * <p>
	 * This method is called when the delegating action has been triggered.
	 * Implement this method to do the actual work.
	 * </p>
	 *
	 * @param action the action proxy that handles the presentation portion of the
	 *   action
	 */
	public void run(IAction action) {
		final Display display = paintView.getDisplay();
		action.setId(id); // temporary workaround since ID field not set by default
		display.syncExec(new ActionRunnable(action));
	}
	private class ActionRunnable implements Runnable {
		IAction action;
		public ActionRunnable(IAction action) {
			this.action = action;
		}
		public void run() {
			paintView.setPaintToolByAction(action);
		}
	}
			
		
	/**
	 * Notifies this action delegate that the selection in the workbench has changed.
	 * <p>
	 * Implementers can use this opportunity to change the availability of the
	 * action or to modify other presentation properties.
	 * </p>
	 *
	 * @param action the action proxy that handles presentation portion of the action
	 * @param selection the current selection in the workbench
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// We don't	care about the selection for now
	}
	
	/**
	 * Implementation of <code>IExecutableExtension</code> allows us to get access to the
	 * configuration data (i.e. plugin.xml attributes) that were specified in order to create
	 * the IAction we received
	 */
	public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data) {
		// Get the identifier
		id = cfig.getAttribute("id");
	}
}
