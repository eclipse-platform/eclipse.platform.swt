/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.awt;

import java.awt.Canvas;
import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWT_AWT {

public static Frame new_Frame (final Composite parent) {
	SWT.error (SWT.ERROR_NOT_IMPLEMENTED);
	return null;
}

public static Shell new_Shell (Display display, final Canvas parent) {
	SWT.error (SWT.ERROR_NOT_IMPLEMENTED);
	return null;
}
}
