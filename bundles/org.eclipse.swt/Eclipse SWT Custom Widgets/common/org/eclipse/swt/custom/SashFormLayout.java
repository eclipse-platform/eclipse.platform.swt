/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * This class provides the layout for SashForm
 * 
 * @see SashForm
 */
class SashFormLayout extends Layout {
protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
	SashForm sashForm = (SashForm)composite;
	Control[] cArray = sashForm.getControls(true);
	int width = 0;
	int height = 0;
	if (cArray.length == 0) {		
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		return new Point(width, height);
	}
	
	int sashwidth = sashForm.sashes.length > 0 ? sashForm.SASH_WIDTH + sashForm.sashes [0].getBorderWidth() * 2 : sashForm.SASH_WIDTH;
	boolean vertical = (sashForm.orientation == SWT.VERTICAL);
	if (vertical) {
		height += (cArray.length - 1) * sashwidth;
	} else {
		width += (cArray.length - 1) * sashwidth;
	}
	for (int i = 0; i < cArray.length; i++) {
		if (vertical) {
			Point size = cArray[i].computeSize(wHint, SWT.DEFAULT, flushCache);
			height += size.y;	
			width = Math.max(width, size.x);
		} else {
			Point size = cArray[i].computeSize(SWT.DEFAULT, hHint, flushCache);
			width += size.x;
			height = Math.max(height, size.y);
		}
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point(width, height);
}
protected void layout(Composite composite, boolean flushCache) {
	SashForm sashForm = (SashForm)composite;
	Rectangle area = sashForm.getClientArea();
	if (area.width == 0 || area.height == 0) return;
	
	Control[] newControls = sashForm.getControls(true);
	if (sashForm.controls.length == 0 && newControls.length == 0) return;
	sashForm.controls = newControls;
	
	Control[] controls = sashForm.controls;
	
	if (sashForm.maxControl != null && !sashForm.maxControl.isDisposed()) {
		for (int i= 0; i < controls.length; i++){
			if (controls[i] != sashForm.maxControl) {
				controls[i].setBounds(-200, -200, 0, 0);
			} else {
				controls[i].setBounds(area);
			}
		}
		return;
	}
	
	// keep just the right number of sashes
	if (sashForm.sashes.length < controls.length - 1) {
		Sash[] newSashes = new Sash[controls.length - 1];
		System.arraycopy(sashForm.sashes, 0, newSashes, 0, sashForm.sashes.length);
		int sashStyle = (sashForm.orientation == SWT.HORIZONTAL) ? SWT.VERTICAL : SWT.HORIZONTAL;
		if ((sashForm.getStyle() & SWT.BORDER) != 0) sashStyle |= SWT.BORDER;
		for (int i = sashForm.sashes.length; i < newSashes.length; i++) {
			newSashes[i] = new Sash(sashForm, sashStyle);
			newSashes[i].setBackground(sashForm.background);
			newSashes[i].setForeground(sashForm.foreground);
			newSashes[i].addListener(SWT.Selection, sashForm.sashListener);
		}
		sashForm.sashes = newSashes;
	}
	if (sashForm.sashes.length > controls.length - 1) {
		if (controls.length == 0) {
			for (int i = 0; i < sashForm.sashes.length; i++) {
				sashForm.sashes[i].dispose();
			}
			sashForm.sashes = new Sash[0];
		} else {
			Sash[] newSashes = new Sash[controls.length - 1];
			System.arraycopy(sashForm.sashes, 0, newSashes, 0, newSashes.length);
			for (int i = controls.length - 1; i < sashForm.sashes.length; i++) {
				sashForm.sashes[i].dispose();
			}
			sashForm.sashes = newSashes;
		}
	}
	if (controls.length == 0) return;
	Sash[] sashes = sashForm.sashes;
	int sashwidth = sashes.length > 0 ? sashForm.SASH_WIDTH + sashes [0].getBorderWidth() * 2 : sashForm.SASH_WIDTH;
	// get the ratios
	long[] ratios = new long[controls.length];
	long total = 0;
	for (int i = 0; i < controls.length; i++) {
		Long ratio = (Long)controls[i].getData(SashForm.LAYOUT_RATIO);
		if (ratio != null) {
			ratios[i] = ratio.longValue();
		} else {
			ratios[i] = ((200 << 16) + 999) / 1000;
		}
		total += ratios[i];
	}
	
	if (sashForm.orientation == SWT.HORIZONTAL) {
		total += (((long)(sashes.length * sashwidth) << 16) + area.width - 1) / area.width;
	} else {
		total += (((long)(sashes.length * sashwidth) << 16) + area.height - 1) / area.height;
	}

	if (sashForm.orientation == SWT.HORIZONTAL) {
		int width = (int)(ratios[0] * area.width / total);
		int x = area.x;
		controls[0].setBounds(x, area.y, width, area.height);
		x += width;
		for (int i = 1; i < controls.length - 1; i++) {
			sashes[i - 1].setBounds(x, area.y, sashwidth, area.height);
			x += sashwidth;
			width = (int)(ratios[i] * area.width / total);
			controls[i].setBounds(x, area.y, width, area.height);
			x += width;
		}
		if (controls.length > 1) {
			sashes[sashes.length - 1].setBounds(x, area.y, sashwidth, area.height);
			x += sashwidth;
			width = area.width - x;
			controls[controls.length - 1].setBounds(x, area.y, width, area.height);
		}
	} else {
		int height = (int)(ratios[0] * area.height / total);
		int y = area.y;
		controls[0].setBounds(area.x, y, area.width, height);
		y += height;
		for (int i = 1; i < controls.length - 1; i++) {
			sashes[i - 1].setBounds(area.x, y, area.width, sashwidth);
			y += sashwidth;
			height = (int)(ratios[i] * area.height / total);
			controls[i].setBounds(area.x, y, area.width, height);
			y += height;
		}
		if (controls.length > 1) {
			sashes[sashes.length - 1].setBounds(area.x, y, area.width, sashwidth);
			y += sashwidth;
			height = area.height - y;
			controls[controls.length - 1].setBounds(area.x, y, area.width, height);
		}

	}
}
}