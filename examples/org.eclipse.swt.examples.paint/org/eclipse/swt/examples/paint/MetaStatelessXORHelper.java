package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * Superclass for all Meta objects that do not need to store state about preview copies
 * and have self-complementary preview drawing and erasing operations
 * e.g. Those that use XOR drawing operations
 */
public abstract class MetaStatelessXORHelper extends Meta {
	public Object drawPreview(GC gc, Point offset) {
		gcDraw(gc, offset);
		return null;
	}
	public void erasePreview(GC gc, Point offset, Object rememberedData) {
		gcDraw(gc, offset);
	}
	protected abstract void gcDraw(GC gc, Point offset);
}
