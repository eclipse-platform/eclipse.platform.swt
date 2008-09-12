package org.eclipse.swt.tools.builders;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class Check64EnableAction implements IObjectActionDelegate {

	public Check64EnableAction() {
		super();
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	public void run(IAction action) {
		Check64CompilationParticipant.setEnabled(action.isChecked());
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
