/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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

public abstract class GCHandle extends Resource implements IGraphicsContext {

	abstract void checkGC(int mask);

	abstract void fillPath(Path path);

	abstract boolean isClipped();

	abstract int getFillRule();

	abstract void getClipping(Region region);

	abstract void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle);

	protected abstract int getAdvanceWidth(char ch);

	protected abstract boolean getAdvanced();

	protected abstract Pattern getBackgroundPattern();

	protected abstract int getCharWidth(char ch);

	protected abstract Pattern getForegroundPattern();

	abstract GCData getGCData();

	protected abstract int getInterpolation();

	protected abstract int[] getLineDash();

	protected abstract int getLineJoin();

	abstract int getStyle();

	protected abstract int getTextAntialias();

	protected abstract void getTransform(Transform transform);

	protected abstract boolean getXORMode();

	protected abstract void setBackgroundPattern(Pattern pattern);

	protected abstract void setClipping(Path path);

	protected abstract void setClipping(Rectangle rect);

	protected abstract void setClipping(Region region);

	abstract void setFillRule(int rule);

	protected abstract void setForegroundPattern(Pattern pattern);

	protected abstract void setInterpolation(int interpolation);

	protected abstract void setLineAttributes(LineAttributes attributes);

	protected abstract void setLineCap(int cap);

	protected abstract void setLineDash(int[] dashes);

	protected abstract void setLineJoin(int join);

	protected abstract void setXORMode(boolean xor);

	protected abstract void setTextAntialias(int antialias);
}

