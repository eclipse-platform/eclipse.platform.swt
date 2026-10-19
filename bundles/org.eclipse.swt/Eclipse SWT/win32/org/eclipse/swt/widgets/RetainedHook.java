/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gpu.*;

/**
 * Win32 implementation of {@link RetainedPlanes.Hook}. Plane selection is one
 * int store on the GC's data; overlay invalidation of a widget carrying
 * {@link Widget#GPU_RETAINED} (in {@code retainedBits}) composites without touching its tiles.
 */
final class RetainedHook implements RetainedPlanes.Hook {
	static final RetainedHook INSTANCE = new RetainedHook ();

	@Override
	public void setPlane (GC gc, int plane) {
		GCData data = gc.getGCData ();
		data.plane = plane;
		if ((plane & RetainedPlanes.OVERLAY) != 0) data.retainedMask |= GCData.RETAIN_PLANES_USED;
	}

	@Override
	public int drawnPlanes (GC gc) {
		int planes = gc.getGCData ().planeDraw;
		return planes == -1 ? RetainedPlanes.ALL_PLANES : planes;
	}

	@Override
	public void invalidatePlanes (Control control, int planes, int x, int y, int width, int height) {
		RetainedFrame frame = control.retainedFrame;
		if ((control.retainedBits & Widget.GPU_RETAINED) == 0 || frame == null || (planes & RetainedPlanes.RETAINED) != 0
				|| (frame.flags & RetainedFrame.HAS_OVERLAY) == 0) {
			control.redraw (x, y, width, height, false);
			return;
		}
		int zoom = control.getAutoscalingZoom ();
		int px = DPIUtil.pointToPixel (x, zoom), py = DPIUtil.pointToPixel (y, zoom);
		frame.invalidateOverlay (px, py, DPIUtil.pointToPixel (x + width, zoom) - px, DPIUtil.pointToPixel (y + height, zoom) - py);
	}
}
