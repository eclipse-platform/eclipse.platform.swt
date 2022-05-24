/*******************************************************************************
 * Copyright (c) 2022 Christoph Läubrich and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.layout;

import static org.eclipse.swt.SWT.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Controls the several aspects of a {@link BorderLayout}.
 *
 * @since 3.119
 */
public final class BorderData {

	private final Map<Control, Point> cachedSize = new IdentityHashMap<>(1);

	public int hHint = SWT.DEFAULT;
	public int wHint = SWT.DEFAULT;
	public int region = SWT.CENTER;

	/**
	 * creates a {@link BorderData} with default options
	 */
	public BorderData() {
	}

	/**
	 * creates a {@link BorderData} initialized with the given region, valid values
	 * are {@link SWT#TOP}, {@link SWT#CENTER}, {@link SWT#LEFT}, {@link SWT#RIGHT},
	 * {@link SWT#BOTTOM}
	 *
	 * @param region the region valid values are {@link SWT#TOP},
	 *               {@link SWT#CENTER}, {@link SWT#LEFT}, {@link SWT#RIGHT},
	 *               {@link SWT#BOTTOM}
	 */
	public BorderData(int region) {
		this.region = region;
	}

	/**
	 * creates a {@link BorderData} initialized with the given region and width and
	 * height hints
	 *
	 * @param region     the region valid values are {@link SWT#TOP},
	 *                   {@link SWT#CENTER}, {@link SWT#LEFT}, {@link SWT#RIGHT},
	 *                   {@link SWT#BOTTOM}
	 * @param widthHint  the default hint for the width
	 * @param heightHint he default hint for the height
	 */
	public BorderData(int region, int widthHint, int heightHint) {
		this.region = region;
		this.wHint = widthHint;
		this.hHint = heightHint;
	}

	Point getSize(Control control) {
		return cachedSize.computeIfAbsent(control, c -> c.computeSize(wHint, hHint, true));
	}

	Point computeSize(Control control, int wHint, int hHint, boolean changed) {
		if (wHint==SWT.DEFAULT) {
			wHint = this.wHint;
		}
		if (hHint == SWT.DEFAULT) {
			hHint = this.hHint;
		}
		return control.computeSize(wHint, hHint, changed);

	}

	void flushCache(Control control) {
		cachedSize.remove(control);
	}

	@Override
	public String toString() {
		return "BorderData [region=" + getRegionString(region) + ", hHint=" + hHint + ", wHint=" + wHint + "]";
	}

	static String getRegionString(int region) {
		switch (region) {
		case SWT.TOP:
			return "SWT.TOP";
		case SWT.RIGHT:
			return "SWT.RIGHT";
		case SWT.BOTTOM:
			return "SWT.BOTTOM";
		case SWT.LEFT:
			return "SWT.LEFT";
		case SWT.CENTER:
			return "SWT.CENTER";
		default:
			return "SWT.NONE";
		}
	}

	/**
	 *
	 * @return the region of this BorderData or {@link SWT#NONE} if it is out of
	 *         range
	 */
	int getRegion() {
		switch (region) {
		case TOP:
		case BOTTOM:
		case CENTER:
		case RIGHT:
		case LEFT:
			return region;
		case SWT.NONE:
		default:
			return SWT.NONE;
		}
	}
}