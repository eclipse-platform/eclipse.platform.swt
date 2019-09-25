/*******************************************************************************
 * Copyright (c) 2004, 2013 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.views;

import org.eclipse.swt.*;
import org.eclipse.swt.tools.internal.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.*;

/**
 * Sleak view to trace native handler allocation
 */

public class SleakView extends ViewPart {

	private Composite composite;

	@Override
	public void createPartControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		Sleak sleak = new Sleak ();
		sleak.create(composite);
	}

	@Override
	public void setFocus() {
		composite.setFocus();
	}

}