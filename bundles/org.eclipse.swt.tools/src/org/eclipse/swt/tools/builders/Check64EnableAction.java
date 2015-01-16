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

import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;

public class Check64EnableAction implements IObjectActionDelegate {

	public Check64EnableAction() {
		super();
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		action.setChecked(Check64CompilationParticipant.getEnabled());
	}

	@Override
	public void run(IAction action) {
		Check64CompilationParticipant.setEnabled(action.isChecked());
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
