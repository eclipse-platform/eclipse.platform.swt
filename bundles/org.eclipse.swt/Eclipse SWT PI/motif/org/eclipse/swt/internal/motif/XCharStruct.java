/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.motif;

 
public class XCharStruct {
	public short lbearing;
	public short rbearing;
	public short width;
	public short ascent;
	public short descent;
	public short attributes;
	public static final int sizeof = 12;
}
