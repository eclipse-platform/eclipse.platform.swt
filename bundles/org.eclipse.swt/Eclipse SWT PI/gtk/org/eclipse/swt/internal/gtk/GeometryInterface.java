/*******************************************************************************
 * Copyright (c) 2021 IBM Corporation and others.
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
package org.eclipse.swt.internal.gtk;

public interface GeometryInterface {

	public int getMinWidth();
	public int getMinHeight();
	public int getMaxWidth();
	public int getMaxHeight();
	public boolean getResize();
	public boolean getMinSizeRequested();
	public int getRequestedWidth();
	public int getRequestedHeight();

	public void setMinWidth(int value);
	public void setMinHeight(int value);
	public void setMaxWidth(int value);
	public void setMaxHeight(int value);
	public void setResize(boolean value);
	public void setMinSizeRequested(boolean value);
	public void setRequestedWidth(int value);
	public void setRequestedHeight(int value);
}