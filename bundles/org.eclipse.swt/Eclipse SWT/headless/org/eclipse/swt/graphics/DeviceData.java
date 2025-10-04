/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and others.
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
package org.eclipse.swt.graphics;

/**
 * Headless implementation of DeviceData for SWT.
 */
public class DeviceData {
	
	public String display_name;
	public String application_name;
	public String application_class;
	
	public boolean debug;
	public boolean tracking;
	public Error[] errors;
	public Object[] objects;
	
}
