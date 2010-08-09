/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.graphics;

public class DeviceData {
	/*
	 * Debug fields - may not be honoured on some SWT platforms.
	 */
	public boolean debug;
	public boolean tracking;
	public Error[] errors;
	public Object[] objects;
}
