/*******************************************************************************
 * Copyright (c) 2004, 2019 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Converted to e4 view
 *******************************************************************************/
package org.eclipse.swt.tools.views;

import javax.annotation.*;

import org.eclipse.e4.ui.di.*;
import org.eclipse.swt.*;
import org.eclipse.swt.tools.internal.*;
import org.eclipse.swt.widgets.*;

/**
 * Sleak view to trace native handler allocation
 */

public class SleakView {

	private Composite composite;

	@PostConstruct
	public void createPartControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		Sleak sleak = new Sleak ();
		sleak.create(composite);
	}

	@Focus
	public void setFocus() {
		composite.setFocus();
	}

}