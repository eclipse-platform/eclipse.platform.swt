/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
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
		action.setChecked(Check64CompilationParticipant.getEnabled());
	}

	public void run(IAction action) {
		Check64CompilationParticipant.setEnabled(action.isChecked());
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
