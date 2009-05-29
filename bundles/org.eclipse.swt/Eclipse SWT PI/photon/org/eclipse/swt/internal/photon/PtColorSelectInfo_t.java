/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.photon;


public class PtColorSelectInfo_t {
	public short flags;
	public byte nselectors;
	public byte ncolor_models;
	/** @field cast=(PgColorModel_t **) */
	public int color_models;
	/** @field cast=(PtColorSelectorSpec_t *) */
	public int selectors;
	/** @field accessor=pos.x */
	public short pos_x;
	/** @field accessor=pos.y */
	public short pos_y;
	/** @field accessor=size.w */
	public short size_w;
	/** @field accessor=size.h */
	public short size_h;
	/** @field accessor=palette.instance,cast=(void *) */
	public int palette;
	/** @field cast=(char *) */
	public int accept_text;
	/** @field cast=(char *) */
	public int dismiss_text;
	/** @field cast=(char *) */
	public int accept_dismiss_text;
	/** @field cast=(void *) */
	public int apply_f;
	/** @field cast=(void *) */
	public int data;
	public int rgb;
	/** @field cast=(PtWidget_t *) */
	public int dialog;
	public static final int sizeof = 52;
}
