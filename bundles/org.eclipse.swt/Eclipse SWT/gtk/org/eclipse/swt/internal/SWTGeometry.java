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
package org.eclipse.swt.internal;

import org.eclipse.swt.internal.gtk.*;

public class SWTGeometry implements GeometryInterface {
	public int min_width;
	public int min_height;
	public int max_width;
	public int max_height;
	public boolean resizeToplevel;
	public boolean minSizeRequested;
	public int requestedWidth;
	public int requestedHeight;

	@Override
	public int getMinWidth() {
		return min_width;
	}
	@Override
	public int getMinHeight() {
		return min_height;
	}
	@Override
	public int getMaxWidth() {
		return max_width;
	}
	@Override
	public int getMaxHeight() {
		return max_height;
	}
	@Override
	public boolean getResize() {
		return resizeToplevel;
	}
	@Override
	public int getRequestedWidth() {
		return requestedWidth;
	}
	@Override
	public int getRequestedHeight() {
		return requestedHeight;
	}
	@Override
	public void setMinWidth(int value) {
		min_width = value;
	}
	@Override
	public void setMinHeight(int value) {
		min_height = value;
	}
	@Override
	public void setMaxWidth(int value) {
		max_width = value;
	}
	@Override
	public void setMaxHeight(int value) {
		max_height = value;
	}
	@Override
	public void setResize(boolean value) {
		resizeToplevel = value;
	}
	@Override
	public void setRequestedWidth(int value) {
		requestedWidth = value;
	}
	@Override
	public void setRequestedHeight(int value) {
		requestedHeight = value;
	}
	@Override
	public boolean getMinSizeRequested() {
		return minSizeRequested;
	}
	@Override
	public void setMinSizeRequested(boolean value) {
		minSizeRequested = value;
	}
}