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
package org.eclipse.swt.internal;

import java.util.*;

import org.eclipse.swt.graphics.*;

/**
 * Internal retained paint graph for viewport-driven SWT rendering.
 *
 * <p>The graph does not proxy or retain {@link GC}. SWT's public GC remains the
 * execution surface and keeps its normal lifetime. Geometry, hierarchy and
 * instance links live in primitive columnar arrays. Detached templates may be
 * referenced by many instance nodes, allowing repeated row/header strokes to be
 * retained once and replayed under different affine transforms.</p>
 *
 * <p>Translation-only instances use coordinate offsets and allocate no native
 * {@link Transform}. General affine instances use at most two temporary
 * transforms for an entire replay and restore the caller's GC transform before
 * returning.</p>
 *
 * @noreference This class is not intended to be referenced by clients.
 */
public final class ViewportPaintGraph {
	private static final byte GROUP = 0;
	private static final byte LINE = 1;
	private static final byte DRAW_RECT = 2;
	private static final byte FILL_RECT = 3;
	private static final byte TEXT = 4;
	private static final byte INSTANCE = 5;
	private static final byte DRAW_PATH = 6;
	private static final byte FILL_PATH = 7;

	private static final int ROOT = 0;
	private static final int DETACHED = -2;
	private static final int NONE = -1;
	private static final byte TEXT_TRANSPARENT = 1;
	private static final byte HAS_CLIP = 1 << 1;
	private static final byte HAS_STROKE = 1 << 2;
	private static final byte HAS_LAYER = 1 << 3;
	private static final byte HAS_CULL_BOUNDS = 1 << 4;

	private byte [] kinds = new byte [16];
	private int [] firstChild = new int [16];
	private int [] lastChild = new int [16];
	private int [] nextSibling = new int [16];
	private int [] previousSibling = new int [16];
	private int [] parents = new int [16];
	private int [] targets = new int [16];
	private int [] transformIds = new int [16];
	private int [] layers = new int [16];
	private int [] zOrders = new int [16];
	private float [] clipX = new float [16];
	private float [] clipY = new float [16];
	private float [] clipWidth = new float [16];
	private float [] clipHeight = new float [16];
	private float [] cullX = new float [16];
	private float [] cullY = new float [16];
	private float [] cullWidth = new float [16];
	private float [] cullHeight = new float [16];
	private int [] lineWidth = new int [16];
	private int [] lineStyle = new int [16];
	private int [] lineCap = new int [16];
	private int [] lineJoin = new int [16];
	private int [] pathTypeOffsets = new int [16];
	private int [] pathTypeCounts = new int [16];
	private int [] pathPointOffsets = new int [16];
	private int [] pathPointCounts = new int [16];
	private float [] pathMinX = new float [16];
	private float [] pathMinY = new float [16];
	private float [] pathMaxX = new float [16];
	private float [] pathMaxY = new float [16];
	private int [] a = new int [16];
	private int [] b = new int [16];
	private int [] c = new int [16];
	private int [] d = new int [16];
	private byte [] flags = new byte [16];
	private Object [] payloads = new Object [16];
	private int nodeCount = 1;
	private int geometryNodeCount;
	private int pathNodeCount;

	private byte [] pathTypes = new byte [64];
	private int pathTypeSize;
	private float [] pathPoints = new float [128];
	private int pathPointSize;

	private float [] transforms = new float [6 * 8];
	private int transformCount = 1;

	/*
	 * Viewport paint graphs are UI-thread confined. These reusable lanes keep
	 * coordinate/hit-test/clip queries allocation-free on paint and pointer paths.
	 */
	private final float [] affineScratch = new float [6];
	private final float [] boundsScratch = new float [4];

	public ViewportPaintGraph () {
		Arrays.fill (firstChild, NONE);
		Arrays.fill (lastChild, NONE);
		Arrays.fill (nextSibling, NONE);
		Arrays.fill (previousSibling, NONE);
		Arrays.fill (parents, NONE);
		Arrays.fill (targets, NONE);
		kinds [ROOT] = GROUP;
		setTransformElements (0, Affine.IDENTITY);
	}

	/**
	 * Affine transform in SWT element order
	 * {@code {m11, m12, m21, m22, dx, dy}}.
	 *
	 * @noreference This type is not intended to be referenced by clients.
	 */
	public record Affine (
			float m11, float m12, float m21, float m22, float dx, float dy) {
		public static final Affine IDENTITY = new Affine (1, 0, 0, 1, 0, 0);

		public Affine {
			if (!Float.isFinite (m11) || !Float.isFinite (m12)
					|| !Float.isFinite (m21) || !Float.isFinite (m22)
					|| !Float.isFinite (dx) || !Float.isFinite (dy)) {
				throw new IllegalArgumentException ("non-finite affine element");
			}
		}

		public static Affine translation (float x, float y) {
			if (x == 0f && y == 0f) return IDENTITY;
			return new Affine (1, 0, 0, 1, x, y);
		}

		public static Affine scale (float x, float y) {
			if (x == 1f && y == 1f) return IDENTITY;
			return new Affine (x, 0, 0, y, 0, 0);
		}

		public static Affine rotation (float radians) {
			if (radians == 0f) return IDENTITY;
			/*
			 * Preserve exact integer geometry for canonical float quadrant angles.
			 * Tiny residual cosine/sine values at +/-PI/2 and PI otherwise expand
			 * conservative bounds and can incorrectly retain or cull edge strokes.
			 * Adjacent representable angles keep their ordinary trig result.
			 */
			double quadrant = Math.rint (radians / (Math.PI / 2d));
			if (Math.abs (quadrant) <= 4d
					&& radians == (float)(quadrant * (Math.PI / 2d))) {
				return switch ((int)quadrant & 3) {
					case 0 -> IDENTITY;
					case 1 -> new Affine (0, 1, -1, 0, 0, 0);
					case 2 -> new Affine (-1, 0, 0, -1, 0, 0);
					default -> new Affine (0, -1, 1, 0, 0, 0);
				};
			}
			float sin = (float)Math.sin (radians);
			float cos = (float)Math.cos (radians);
			return new Affine (cos, sin, -sin, cos, 0, 0);
		}

		public static Affine shear (float x, float y) {
			if (x == 0f && y == 0f) return IDENTITY;
			return new Affine (1, y, x, 1, 0, 0);
		}

		/** Returns {@code this * local}; {@code local} is applied first. */
		public Affine compose (Affine local) {
			Objects.requireNonNull (local, "local");
			if (local.isIdentity ()) return this;
			if (isIdentity ()) return local;
			return new Affine (
					m11 * local.m11 + m21 * local.m12,
					m12 * local.m11 + m22 * local.m12,
					m11 * local.m21 + m21 * local.m22,
					m12 * local.m21 + m22 * local.m22,
					m11 * local.dx + m21 * local.dy + dx,
					m12 * local.dx + m22 * local.dy + dy);
		}

		public float determinant () {
			return m11 * m22 - m21 * m12;
		}

		public Affine inverse () {
			float determinant = determinant ();
			if (!Float.isFinite (determinant) || determinant == 0f) {
				throw new IllegalStateException ("non-invertible affine transform");
			}
			float inverseDeterminant = 1f / determinant;
			float i11 = m22 * inverseDeterminant;
			float i12 = -m12 * inverseDeterminant;
			float i21 = -m21 * inverseDeterminant;
			float i22 = m11 * inverseDeterminant;
			float idx = -(i11 * dx + i21 * dy);
			float idy = -(i12 * dx + i22 * dy);
			return new Affine (i11, i12, i21, i22, idx, idy);
		}

		public boolean isIdentity () {
			return this == IDENTITY
					|| (same (m11, 1) && same (m12, 0)
					&& same (m21, 0) && same (m22, 1)
					&& same (dx, 0) && same (dy, 0));
		}

		public boolean isTranslationOnly () {
			return same (m11, 1) && same (m12, 0)
					&& same (m21, 0) && same (m22, 1);
		}

		public boolean isIntegralTranslation () {
			return isTranslationOnly ()
					&& dx == Math.rint (dx) && dy == Math.rint (dy);
		}

		public void map (float x, float y, float [] out) {
			if (out == null || out.length < 2) {
				throw new IllegalArgumentException ("affine output too small");
			}
			out [0] = m11 * x + m21 * y + dx;
			out [1] = m12 * x + m22 * y + dy;
		}

		public boolean inverseMap (float x, float y, float [] out) {
			if (out == null || out.length < 2) {
				throw new IllegalArgumentException ("affine output too small");
			}
			float determinant = determinant ();
			if (determinant == 0f) return false;
			float px = x - dx;
			float py = y - dy;
			out [0] = (m22 * px - m21 * py) / determinant;
			out [1] = (-m12 * px + m11 * py) / determinant;
			return true;
		}

		private static boolean same (float left, float right) {
			return Float.floatToIntBits (left) == Float.floatToIntBits (right);
		}
	}

	/**
	 * Replay counters used by diagnostics and regression tests.
	 *
	 * @noreference This type is not intended to be referenced by clients.
	 */
	public record ReplayStats (
			int visitedNodes, int drawnCommands, int culledCommands, int transformSwitches) {
	}

	public int root () {
		return ROOT;
	}

	/** Creates a detached reusable group. */
	public int template () {
		return newNode (GROUP, DETACHED);
	}

	public int group (int parent) {
		return group (parent, Affine.IDENTITY);
	}

	public int group (int parent, Affine transform) {
		requireGroup (parent);
		int node = newNode (GROUP, parent);
		transformIds [node] = transformId (Objects.requireNonNull (transform, "transform"));
		return node;
	}

	public void setTransform (int group, Affine transform) {
		requireGroup (group);
		Objects.requireNonNull (transform, "transform");
		int id = transformIds [group];
		if (id == 0) {
			transformIds [group] = transformId (transform);
		} else {
			// Non-identity slots have one owner. Reuse its lane across scroll frames,
			// including identity resets, instead of retaining every past transform.
			setTransformElements (id, transform);
		}
	}

	public void setLayer (int group, int layer) {
		requireGroup (group);
		layers [group] = layer;
		flags [group] |= HAS_LAYER;
	}

	public void clearLayer (int group) {
		requireGroup (group);
		flags [group] &= ~HAS_LAYER;
	}

	/**
	 * Orders one retained node among siblings in the same parent/viewport plane.
	 *
	 * <p>Lower values paint first. Equal values retain creation order. The sibling
	 * links are reordered when this method is called, so replay performs no
	 * sorting or allocation.</p>
	 */
	public void setZOrder (int node, int zOrder) {
		requireNode (node);
		int parent = parents [node];
        if (parent < 0) {
            throw new IllegalArgumentException("z-order requires an attached sibling");
        }
        if (zOrders [node] == zOrder) {
            return;
        }
		unlinkChild (parent, node);
		zOrders [node] = zOrder;
		linkChild (parent, node);
	}

	public int zOrder (int node) {
		requireNode (node);
		return zOrders [node];
	}

	public boolean effectiveLayer (int node, int [] out) {
		requireNode (node);
        if (out == null || out.length == 0) {
            throw new IllegalArgumentException("layer output too small");
        }
		for (int current = node; current >= 0; current = parents [current]) {
			if ((flags [current] & HAS_LAYER) != 0) {
				out [0] = layers [current];
				return true;
			}
		}
		return false;
	}

	public void setClip (int group, float x, float y, float width, float height) {
		requireGroup (group);
		if (!Float.isFinite (x) || !Float.isFinite (y)
				|| !Float.isFinite (width) || !Float.isFinite (height)
				|| width < 0 || height < 0) {
			throw new IllegalArgumentException ("invalid viewport clip");
		}
		clipX [group] = x;
		clipY [group] = y;
		clipWidth [group] = width;
		clipHeight [group] = height;
		flags [group] |= HAS_CLIP;
	}

	public void clearClip (int group) {
		requireGroup (group);
		flags [group] &= ~HAS_CLIP;
	}

	/**
	 * Sets conservative local-space bounds for coarse subtree culling.
	 *
	 * <p>This is deliberately separate from clipping.  A clip changes what may
	 * be drawn inside a group; cull bounds only let replay skip the whole
	 * retained subtree when none of it can intersect the requested replay
	 * region.  Callers should use the cheapest stable row/pane bounds they
	 * already know.</p>
	 */
	public void setCullBounds (int group, float x, float y, float width, float height) {
		requireGroup (group);
		if (!Float.isFinite (x) || !Float.isFinite (y)
				|| !Float.isFinite (width) || !Float.isFinite (height)
				|| width < 0 || height < 0) {
			throw new IllegalArgumentException ("invalid viewport cull bounds");
		}
		cullX [group] = x;
		cullY [group] = y;
		cullWidth [group] = width;
		cullHeight [group] = height;
		flags [group] |= HAS_CULL_BOUNDS;
	}

	public void clearCullBounds (int group) {
		requireGroup (group);
		flags [group] &= ~HAS_CULL_BOUNDS;
	}

	/**
	 * Computes the conservative root-space bounds of all clips inherited by
	 * one attached graph node. Detached templates have no unique root mapping.
	 */
	public boolean rootClip (int node, float [] out) {
		requireAttachedNode (node);
        if (out == null || out.length < 4) {
            throw new IllegalArgumentException("clip output too small");
        }
		boolean clipped = false;
		float left = Float.NEGATIVE_INFINITY;
		float top = Float.NEGATIVE_INFINITY;
		float right = Float.POSITIVE_INFINITY;
		float bottom = Float.POSITIVE_INFINITY;
		for (int current = node; current >= 0; current = parents [current]) {
            if ((flags [current] & HAS_CLIP) == 0) {
                continue;
            }
			mapRectToRoot (
					current,
					clipX [current], clipY [current],
					clipWidth [current], clipHeight [current],
					boundsScratch);
			if (!clipped) {
				left = boundsScratch [0];
				top = boundsScratch [1];
				right = boundsScratch [0] + boundsScratch [2];
				bottom = boundsScratch [1] + boundsScratch [3];
				clipped = true;
			} else {
				left = Math.max (left, boundsScratch [0]);
				top = Math.max (top, boundsScratch [1]);
				right = Math.min (right, boundsScratch [0] + boundsScratch [2]);
				bottom = Math.min (bottom, boundsScratch [1] + boundsScratch [3]);
			}
		}
        if (!clipped) {
            return false;
        }
		out [0] = left;
		out [1] = top;
		out [2] = Math.max (0, right - left);
		out [3] = Math.max (0, bottom - top);
		return true;
	}

	public void setStroke (int group, int width, int style, int cap, int join) {
		requireGroup (group);
        if (width < 0) {
            throw new IllegalArgumentException("negative line width");
        }
		lineWidth [group] = width;
		lineStyle [group] = style;
		lineCap [group] = cap;
		lineJoin [group] = join;
		flags [group] |= HAS_STROKE;
	}

	public void clearStroke (int group) {
		requireGroup (group);
		flags [group] &= ~HAS_STROKE;
	}

	public boolean effectiveStroke (int node, int [] out) {
		requireNode (node);
        if (out == null || out.length < 4) {
            throw new IllegalArgumentException("stroke output too small");
        }
		for (int current = node; current >= 0; current = parents [current]) {
			if ((flags [current] & HAS_STROKE) != 0) {
				out [0] = lineWidth [current];
				out [1] = lineStyle [current];
				out [2] = lineCap [current];
				out [3] = lineJoin [current];
				return true;
			}
		}
		return false;
	}

	public void mapToRoot (int node, float x, float y, float [] out) {
		requireAttachedNode (node);
        if (out == null || out.length < 2) {
            throw new IllegalArgumentException("coordinate output too small");
        }
		rootTransform (node, affineScratch);
		out [0] = affineScratch [0] * x + affineScratch [2] * y + affineScratch [4];
		out [1] = affineScratch [1] * x + affineScratch [3] * y + affineScratch [5];
	}

	public boolean mapFromRoot (int node, float x, float y, float [] out) {
		requireAttachedNode (node);
        if (out == null || out.length < 2) {
            throw new IllegalArgumentException("coordinate output too small");
        }
		rootTransform (node, affineScratch);
		float determinant = affineScratch [0] * affineScratch [3]
				- affineScratch [2] * affineScratch [1];
        if (determinant == 0) {
            return false;
        }
		float px = x - affineScratch [4];
		float py = y - affineScratch [5];
		out [0] = (affineScratch [3] * px - affineScratch [2] * py) / determinant;
		out [1] = (-affineScratch [1] * px + affineScratch [0] * py) / determinant;
		return true;
	}

	public int line (int parent, int x1, int y1, int x2, int y2) {
		requireGroup (parent);
		int node = newNode (LINE, parent);
		a [node] = x1;
		b [node] = y1;
		c [node] = x2;
		d [node] = y2;
		geometryNodeCount++;
		return node;
	}

	public int drawRectangle (int parent, int x, int y, int width, int height) {
		requireGroup (parent);
		int node = newNode (DRAW_RECT, parent);
		a [node] = x;
		b [node] = y;
		c [node] = width;
		d [node] = height;
		geometryNodeCount++;
		return node;
	}

	public int fillRectangle (int parent, int x, int y, int width, int height) {
		requireGroup (parent);
		int node = newNode (FILL_RECT, parent);
		a [node] = x;
		b [node] = y;
		c [node] = width;
		d [node] = height;
		geometryNodeCount++;
		return node;
	}

	public int text (int parent, String text, int x, int y, boolean transparent) {
		requireGroup (parent);
		Objects.requireNonNull (text, "text");
		int node = newNode (TEXT, parent);
		a [node] = x;
		b [node] = y;
		payloads [node] = text;
        if (transparent) {
            flags [node] |= TEXT_TRANSPARENT;
        }
		geometryNodeCount++;
		return node;
	}

	public int drawPath (int parent, PathData data) {
		return addPath (parent, data, DRAW_PATH);
	}

	public int fillPath (int parent, PathData data) {
		return addPath (parent, data, FILL_PATH);
	}

	private int addPath (int parent, PathData data, byte kind) {
		requireGroup (parent);
		Objects.requireNonNull (data, "data");
		byte [] types = Objects.requireNonNull (data.types, "data.types");
		float [] points = Objects.requireNonNull (data.points, "data.points");
		int expectedPoints = 0;
		for (byte type : types) {
			expectedPoints += switch (type) {
				case org.eclipse.swt.SWT.PATH_MOVE_TO, org.eclipse.swt.SWT.PATH_LINE_TO -> 2;
				case org.eclipse.swt.SWT.PATH_CUBIC_TO -> 6;
				case org.eclipse.swt.SWT.PATH_QUAD_TO -> 4;
				case org.eclipse.swt.SWT.PATH_CLOSE -> 0;
				default -> throw new IllegalArgumentException ("invalid retained path type");
			};
		}
		if (expectedPoints != points.length) {
			throw new IllegalArgumentException ("retained path point count does not match types");
		}
		float minX = Float.POSITIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;
		for (int index = 0; index < points.length; index += 2) {
			float x = points [index];
			float y = points [index + 1];
			if (!Float.isFinite (x) || !Float.isFinite (y)) {
				throw new IllegalArgumentException ("non-finite retained path point");
			}
			minX = Math.min (minX, x);
			minY = Math.min (minY, y);
			maxX = Math.max (maxX, x);
			maxY = Math.max (maxY, y);
		}
        if (points.length == 0) {
            minX = minY = maxX = maxY = 0;
        }

		int node = newNode (kind, parent);
		ensurePathTypeCapacity (pathTypeSize + types.length);
		ensurePathPointCapacity (pathPointSize + points.length);
		pathTypeOffsets [node] = pathTypeSize;
		pathTypeCounts [node] = types.length;
		System.arraycopy (types, 0, pathTypes, pathTypeSize, types.length);
		pathTypeSize += types.length;
		pathPointOffsets [node] = pathPointSize;
		pathPointCounts [node] = points.length;
		System.arraycopy (points, 0, pathPoints, pathPointSize, points.length);
		pathPointSize += points.length;
		pathMinX [node] = minX;
		pathMinY [node] = minY;
		pathMaxX [node] = maxX;
		pathMaxY [node] = maxY;
		pathNodeCount++;
		geometryNodeCount++;
		return node;
	}

	/**
	 * Adds one DAG edge from {@code parent} to a detached template.
	 */
	public int instance (int parent, int template, Affine transform) {
		requireGroup (parent);
		if (template <= ROOT || template >= nodeCount || kinds [template] != GROUP
				|| parents [template] != DETACHED) {
			throw new IllegalArgumentException ("instance target is not a detached template");
		}
		if (wouldReach (template, parent)) {
			throw new IllegalArgumentException ("paint graph cycle");
		}
		int node = newNode (INSTANCE, parent);
		targets [node] = template;
		transformIds [node] = transformId (Objects.requireNonNull (transform, "transform"));
		return node;
	}

	public int nodeCount () {
		return nodeCount;
	}

	public int geometryNodeCount () {
		return geometryNodeCount;
	}

	public int transformCount () {
		return transformCount;
	}

	public ReplayStats replay (GC gc) {
		return replay (gc, ROOT, Affine.IDENTITY, null, false, 0);
	}

	public ReplayStats replay (GC gc, Affine transform, Rectangle clip) {
		return replay (gc, ROOT, transform, clip, false, 0);
	}

	/**
	 * Replays only commands in the requested effective viewport layer.
	 * Commands without a layer, and commands inherited into other layers, are skipped.
	 */
	public ReplayStats replayLayer (GC gc, int layer, Affine transform, Rectangle clip) {
		return replay (gc, ROOT, transform, clip, true, layer);
	}

	/**
	 * Replays viewport layers in caller-supplied back-to-front order.
	 *
	 * <p>This mirrors draw-channel/layer scheduling used by immediate-mode and GPU
	 * renderers without changing ordinary {@link #replay(GC)} semantics.</p>
	 */
	public ReplayStats replayLayers (GC gc, int [] orderedLayers, Affine transform, Rectangle clip) {
		Objects.requireNonNull (orderedLayers, "orderedLayers");
		int visited = 0, drawn = 0, culled = 0, switches = 0;
		for (int layer : orderedLayers) {
			ReplayStats stats = replayLayer (gc, layer, transform, clip);
			visited += stats.visitedNodes ();
			drawn += stats.drawnCommands ();
			culled += stats.culledCommands ();
			switches += stats.transformSwitches ();
		}
		return new ReplayStats (visited, drawn, culled, switches);
	}

	public ReplayStats replayTemplate (GC gc, int template, Affine transform, Rectangle clip) {
		if (template <= ROOT || template >= nodeCount || kinds [template] != GROUP
				|| parents [template] != DETACHED) {
			throw new IllegalArgumentException ("not a detached template");
		}
		return replay (gc, template, transform, clip, false, 0);
	}

	private ReplayStats replay (
			GC gc, int start, Affine initialTransform, Rectangle clip,
			boolean filterLayer, int requestedLayer) {
		Objects.requireNonNull (gc, "gc");
		Objects.requireNonNull (initialTransform, "initialTransform");
        if (gc.isDisposed()) {
            throw new IllegalArgumentException("disposed GC");
        }
		if (clip != null && clip.isEmpty ()) {
			return new ReplayStats (0, 0, 0, 0);
		}

		int initialCapacity = Math.max (16, nodeCount);
		int [] nodeStack = new int [initialCapacity];
		Affine [] transformStack = new Affine [initialCapacity];
		int [] layerStack = new int [initialCapacity];
		boolean [] hasLayerStack = new boolean [initialCapacity];
		int stackSize = 0;

		nodeStack [stackSize] = start;
		transformStack [stackSize] = initialTransform;
		layerStack [stackSize] = 0;
		hasLayerStack [stackSize++] = false;

		Transform saved = null;
		Transform work = null;
		Path [] replayPaths = pathNodeCount == 0 ? null : new Path [nodeCount];
		Affine savedAffine = null;
		boolean transformedGc = false;
		int visited = 0;
		int drawn = 0;
		int culled = 0;
		int transformSwitches = 0;
		/*
		 * One conservative envelope per replay.  It intentionally overestimates
		 * fractional widths, caps/joins and antialiasing so clipping can never
		 * remove a native GC pixel that would otherwise be visible.
		 */
		long strokeOutset = Math.max (1L, gc.getLineWidth ()) + 1;

		try {
			while (stackSize != 0) {
				int node = nodeStack [--stackSize];
				Affine transform = transformStack [stackSize];
				int inheritedLayer = layerStack [stackSize];
				boolean hasInheritedLayer = hasLayerStack [stackSize];
				visited++;

				switch (kinds [node]) {
					case GROUP -> {
						Affine next = transform;
                        if (transformIds [node] != 0) {
                            next = transform.compose(transform(transformIds [node]));
                        }
						if (clip != null && (flags [node] & HAS_CULL_BOUNDS) != 0
								&& outsideBounds (
										next,
										cullX [node], cullY [node],
										cullWidth [node], cullHeight [node],
										clip, strokeOutset)) {
							continue;
						}
						boolean hasNextLayer = hasInheritedLayer;
						int nextLayer = inheritedLayer;
						if ((flags [node] & HAS_LAYER) != 0) {
							hasNextLayer = true;
							nextLayer = layers [node];
						}
						for (int child = lastChild [node]; child != NONE; child = previousSibling [child]) {
							if (stackSize == nodeStack.length) {
								nodeStack = Arrays.copyOf (nodeStack, nodeStack.length * 2);
								transformStack = Arrays.copyOf (transformStack, transformStack.length * 2);
								layerStack = Arrays.copyOf (layerStack, layerStack.length * 2);
								hasLayerStack = Arrays.copyOf (hasLayerStack, hasLayerStack.length * 2);
							}
							nodeStack [stackSize] = child;
							transformStack [stackSize] = next;
							layerStack [stackSize] = nextLayer;
							hasLayerStack [stackSize++] = hasNextLayer;
						}
					}
					case INSTANCE -> {
						Affine next = transform.compose (transform (transformIds [node]));
						if (stackSize == nodeStack.length) {
							nodeStack = Arrays.copyOf (nodeStack, nodeStack.length * 2);
							transformStack = Arrays.copyOf (transformStack, transformStack.length * 2);
							layerStack = Arrays.copyOf (layerStack, layerStack.length * 2);
							hasLayerStack = Arrays.copyOf (hasLayerStack, hasLayerStack.length * 2);
						}
						nodeStack [stackSize] = targets [node];
						transformStack [stackSize] = next;
						layerStack [stackSize] = inheritedLayer;
						hasLayerStack [stackSize++] = hasInheritedLayer;
					}
					default -> {
						if (filterLayer && (!hasInheritedLayer || inheritedLayer != requestedLayer)) {
							continue;
						}
						if (clip != null && transform.isIntegralTranslation ()
								&& outsideClip (node, transform, clip, strokeOutset)) {
							culled++;
							continue;
						}
						boolean pathCommand = kinds [node] == DRAW_PATH || kinds [node] == FILL_PATH;
						boolean directPath = pathCommand && transform.isIdentity ();
						boolean fast = transform.isIntegralTranslation () && !pathCommand;
						if (directPath) {
							if (transformedGc) {
								gc.setTransform (saved);
								transformedGc = false;
								transformSwitches++;
							}
							drawRaw (gc, node, 0, 0, replayPaths);
						} else if (!fast) {
							if (saved == null) {
								saved = new Transform (gc.getDevice ());
								work = new Transform (gc.getDevice ());
								gc.getTransform (saved);
								float [] elements = new float [6];
								saved.getElements (elements);
								savedAffine = new Affine (
										elements [0], elements [1], elements [2],
										elements [3], elements [4], elements [5]);
							}
							Affine combined = savedAffine.compose (transform);
							work.setElements (
									combined.m11, combined.m12, combined.m21,
									combined.m22, combined.dx, combined.dy);
							gc.setTransform (work);
							transformedGc = true;
							transformSwitches++;
							drawRaw (gc, node, 0, 0, replayPaths);
						} else {
							if (transformedGc) {
								gc.setTransform (saved);
								transformedGc = false;
								transformSwitches++;
							}
							drawRaw (gc, node, Math.round (transform.dx), Math.round (transform.dy), replayPaths);
						}
						drawn++;
					}
				}
			}
		} finally {
			if (saved != null) {
                if (transformedGc) {
                    gc.setTransform(saved);
                }
				work.dispose ();
				saved.dispose ();
			}
			if (replayPaths != null) {
				for (Path replayPath : replayPaths) {
                    if (replayPath != null && !replayPath.isDisposed()) {
                        replayPath.dispose();
                    }
				}
			}
		}
		return new ReplayStats (visited, drawn, culled, transformSwitches);
	}

	private void drawRaw (GC gc, int node, int dx, int dy, Path [] replayPaths) {
		switch (kinds [node]) {
			case LINE -> gc.drawLine (a [node] + dx, b [node] + dy, c [node] + dx, d [node] + dy);
			case DRAW_RECT -> gc.drawRectangle (a [node] + dx, b [node] + dy, c [node], d [node]);
			case FILL_RECT -> gc.fillRectangle (a [node] + dx, b [node] + dy, c [node], d [node]);
			case TEXT -> gc.drawText (
					(String)payloads [node], a [node] + dx, b [node] + dy,
					(flags [node] & TEXT_TRANSPARENT) != 0);
			case DRAW_PATH -> gc.drawPath (replayPath (gc, node, replayPaths));
			case FILL_PATH -> gc.fillPath (replayPath (gc, node, replayPaths));
			default -> throw new IllegalStateException ("not a paint command");
		}
	}

	private Path replayPath (GC gc, int node, Path [] replayPaths) {
		Path path = replayPaths [node];
        if (path != null) {
            return path;
        }
		PathData data = new PathData ();
		int typeOffset = pathTypeOffsets [node];
		int typeCount = pathTypeCounts [node];
		data.types = Arrays.copyOfRange (pathTypes, typeOffset, typeOffset + typeCount);
		int pointOffset = pathPointOffsets [node];
		int pointCount = pathPointCounts [node];
		data.points = Arrays.copyOfRange (pathPoints, pointOffset, pointOffset + pointCount);
		return replayPaths [node] = new Path (gc.getDevice (), data);
	}

	private boolean outsideBounds (
			Affine transform, float x, float y, float width, float height,
			Rectangle clip, long strokeOutset) {
		mapRect (transform, x, y, width, height, boundsScratch);
		/*
		 * Cull bounds describe retained geometry, not necessarily its rasterized
		 * stroke.  Expand them by a conservative transform-scaled envelope so a
		 * child stroke that crosses the logical group edge remains eligible.
		 */
		double m11 = transform.m11;
		double m12 = transform.m12;
		double m21 = transform.m21;
		double m22 = transform.m22;
		double scaleUpperBound = Math.max (
				1d, Math.sqrt (m11 * m11 + m12 * m12 + m21 * m21 + m22 * m22));
		double envelope = strokeOutset * scaleUpperBound;
		double left = boundsScratch [0] - envelope;
		double top = boundsScratch [1] - envelope;
		double right = boundsScratch [0] + boundsScratch [2] + envelope;
		double bottom = boundsScratch [1] + boundsScratch [3] + envelope;
		long clipRight = (long)clip.x + clip.width;
		long clipBottom = (long)clip.y + clip.height;
		return right <= clip.x || bottom <= clip.y
				|| left >= clipRight || top >= clipBottom;
	}

	private boolean outsideClip (
			int node, Affine transform, Rectangle clip, long strokeOutset) {
		int dx = Math.round (transform.dx);
		int dy = Math.round (transform.dy);
		long left;
		long top;
		long right;
		long bottom;
		switch (kinds [node]) {
			case LINE -> {
				left = Math.min (a [node], c [node]) + (long)dx;
				top = Math.min (b [node], d [node]) + (long)dy;
				right = Math.max (a [node], c [node]) + (long)dx + 1;
				bottom = Math.max (b [node], d [node]) + (long)dy + 1;
			}
			case DRAW_RECT, FILL_RECT -> {
				left = Math.min (a [node], (long)a [node] + c [node]) + dx;
				top = Math.min (b [node], (long)b [node] + d [node]) + dy;
				right = Math.max (a [node], (long)a [node] + c [node]) + dx + 1;
				bottom = Math.max (b [node], (long)b [node] + d [node]) + dy + 1;
			}
			case DRAW_PATH, FILL_PATH -> {
				double envelope = kinds [node] == DRAW_PATH ? strokeOutset : 1d;
				double pathLeft = pathMinX [node] + dx - envelope;
				double pathTop = pathMinY [node] + dy - envelope;
				double pathRight = pathMaxX [node] + dx + envelope;
				double pathBottom = pathMaxY [node] + dy + envelope;
				long clipRight = (long)clip.x + clip.width;
				long clipBottom = (long)clip.y + clip.height;
				return pathRight <= clip.x || pathBottom <= clip.y
						|| pathLeft >= clipRight || pathTop >= clipBottom;
			}
			case TEXT -> {
				// Font metrics are intentionally not retained in the graph.
				return false;
			}
			default -> {
				return false;
			}
		}
		if (kinds [node] == LINE || kinds [node] == DRAW_RECT) {
			left -= strokeOutset;
			top -= strokeOutset;
			right += strokeOutset;
			bottom += strokeOutset;
		}
		return right <= clip.x || bottom <= clip.y
				|| left >= (long)clip.x + clip.width
				|| top >= (long)clip.y + clip.height;
	}

	private int newNode (byte kind, int parent) {
		ensureNodeCapacity (nodeCount + 1);
		int node = nodeCount++;
		kinds [node] = kind;
		firstChild [node] = lastChild [node] = NONE;
		nextSibling [node] = previousSibling [node] = NONE;
		parents [node] = parent;
		targets [node] = NONE;
        if (parent >= 0) {
            linkChild(parent, node);
        }
		return node;
	}

	private void linkChild (int parent, int child) {
		int cursor = firstChild [parent];
		while (cursor != NONE && comesBefore (cursor, child)) {
			cursor = nextSibling [cursor];
		}
		if (cursor == NONE) {
			int tail = lastChild [parent];
			if (tail == NONE) {
				firstChild [parent] = lastChild [parent] = child;
				return;
			}
			nextSibling [tail] = child;
			previousSibling [child] = tail;
			lastChild [parent] = child;
			return;
		}
		int previous = previousSibling [cursor];
		nextSibling [child] = cursor;
		previousSibling [child] = previous;
		previousSibling [cursor] = child;
        if (previous == NONE) {
            firstChild [parent] = child;
        } else {
            nextSibling [previous] = child;
        }
	}

	private void unlinkChild (int parent, int child) {
		int previous = previousSibling [child];
		int next = nextSibling [child];
        if (previous == NONE) {
            firstChild [parent] = next;
        } else {
            nextSibling [previous] = next;
        }
        if (next == NONE) {
            lastChild [parent] = previous;
        } else {
            previousSibling [next] = previous;
        }
		previousSibling [child] = NONE;
		nextSibling [child] = NONE;
	}

	private boolean comesBefore (int left, int right) {
		int order = Integer.compare (zOrders [left], zOrders [right]);
		return order < 0 || (order == 0 && left < right);
	}

	private boolean wouldReach (int start, int wanted) {
        if (start == wanted) {
            return true;
        }
		boolean [] seen = new boolean [nodeCount];
		int [] stack = new int [Math.max (16, nodeCount)];
		int size = 0;
		stack [size++] = start;
		while (size != 0) {
			int node = stack [--size];
            if (node == wanted) {
                return true;
            }
            if (seen [node]) {
                continue;
            }
			seen [node] = true;
			for (int child = firstChild [node]; child != NONE; child = nextSibling [child]) {
                if (size == stack.length) {
                    stack = Arrays.copyOf(stack, stack.length * 2);
                }
				stack [size++] = child;
				if (kinds [child] == INSTANCE) {
                    if (size == stack.length) {
                        stack = Arrays.copyOf(stack, stack.length * 2);
                    }
					stack [size++] = targets [child];
				}
			}
		}
		return false;
	}

	private int transformId (Affine transform) {
		return transform.isIdentity () ? 0 : storeTransform (transform);
	}

	private int storeTransform (Affine transform) {
		ensureTransformCapacity (transformCount + 1);
		int id = transformCount++;
		setTransformElements (id, transform);
		return id;
	}

	private Affine transform (int id) {
		int offset = id * 6;
		return new Affine (
				transforms [offset], transforms [offset + 1], transforms [offset + 2],
				transforms [offset + 3], transforms [offset + 4], transforms [offset + 5]);
	}

	private void setTransformElements (int id, Affine transform) {
		int offset = id * 6;
		transforms [offset] = transform.m11;
		transforms [offset + 1] = transform.m12;
		transforms [offset + 2] = transform.m21;
		transforms [offset + 3] = transform.m22;
		transforms [offset + 4] = transform.dx;
		transforms [offset + 5] = transform.dy;
	}

	private void requireGroup (int node) {
		if (node < 0 || node >= nodeCount || kinds [node] != GROUP) {
			throw new IllegalArgumentException ("parent is not a group");
		}
	}

	private void requireNode (int node) {
        if (node < 0 || node >= nodeCount) {
            throw new IllegalArgumentException("invalid paint node");
        }
	}

	private void requireAttachedNode (int node) {
		requireNode (node);
		for (int current = node; current >= 0; current = parents [current]) {
			if (parents [current] == DETACHED) {
				throw new IllegalArgumentException ("detached paint node has no unique root mapping");
			}
		}
	}

	private void rootTransform (int node, float [] out) {
		float r11 = 1, r12 = 0, r21 = 0, r22 = 1, rdx = 0, rdy = 0;
		for (int current = node; current >= 0; current = parents [current]) {
			if (parents [current] == DETACHED) {
				throw new IllegalArgumentException ("detached paint node has no unique root mapping");
			}
			int transformId = (kinds [current] == GROUP || kinds [current] == INSTANCE)
					? transformIds [current] : 0;
            if (transformId == 0) {
                continue;
            }
			int offset = transformId * 6;
			float l11 = transforms [offset];
			float l12 = transforms [offset + 1];
			float l21 = transforms [offset + 2];
			float l22 = transforms [offset + 3];
			float ldx = transforms [offset + 4];
			float ldy = transforms [offset + 5];

			float next11 = l11 * r11 + l21 * r12;
			float next12 = l12 * r11 + l22 * r12;
			float next21 = l11 * r21 + l21 * r22;
			float next22 = l12 * r21 + l22 * r22;
			float nextDx = l11 * rdx + l21 * rdy + ldx;
			float nextDy = l12 * rdx + l22 * rdy + ldy;
			r11 = next11;
			r12 = next12;
			r21 = next21;
			r22 = next22;
			rdx = nextDx;
			rdy = nextDy;
		}
		out [0] = r11;
		out [1] = r12;
		out [2] = r21;
		out [3] = r22;
		out [4] = rdx;
		out [5] = rdy;
	}

	private void mapRectToRoot (
			int node, float x, float y, float width, float height, float [] out) {
		rootTransform (node, affineScratch);
		mapRect (
				affineScratch [0], affineScratch [1], affineScratch [2],
				affineScratch [3], affineScratch [4], affineScratch [5],
				x, y, width, height, out);
	}

	private static void mapRect (
			Affine transform, float x, float y, float width, float height, float [] out) {
		mapRect (
				transform.m11, transform.m12, transform.m21,
				transform.m22, transform.dx, transform.dy,
				x, y, width, height, out);
	}

	private static void mapRect (
			float m11, float m12, float m21, float m22, float dx, float dy,
			float x, float y, float width, float height, float [] out) {
		float x1 = m11 * x + m21 * y + dx;
		float y1 = m12 * x + m22 * y + dy;
		float x2 = m11 * (x + width) + m21 * y + dx;
		float y2 = m12 * (x + width) + m22 * y + dy;
		float x3 = m11 * x + m21 * (y + height) + dx;
		float y3 = m12 * x + m22 * (y + height) + dy;
		float x4 = m11 * (x + width) + m21 * (y + height) + dx;
		float y4 = m12 * (x + width) + m22 * (y + height) + dy;
		float left = Math.min (Math.min (x1, x2), Math.min (x3, x4));
		float top = Math.min (Math.min (y1, y2), Math.min (y3, y4));
		float right = Math.max (Math.max (x1, x2), Math.max (x3, x4));
		float bottom = Math.max (Math.max (y1, y2), Math.max (y3, y4));
		out [0] = left;
		out [1] = top;
		out [2] = right - left;
		out [3] = bottom - top;
	}

	private void ensureNodeCapacity (int required) {
        if (required <= kinds.length) {
            return;
        }
		int old = kinds.length;
		int next = Math.max (required, old * 2);
		kinds = Arrays.copyOf (kinds, next);
		firstChild = grow (firstChild, old, next, NONE);
		lastChild = grow (lastChild, old, next, NONE);
		nextSibling = grow (nextSibling, old, next, NONE);
		previousSibling = grow (previousSibling, old, next, NONE);
		parents = grow (parents, old, next, NONE);
		targets = grow (targets, old, next, NONE);
		transformIds = Arrays.copyOf (transformIds, next);
		layers = Arrays.copyOf (layers, next);
		zOrders = Arrays.copyOf (zOrders, next);
		clipX = Arrays.copyOf (clipX, next);
		clipY = Arrays.copyOf (clipY, next);
		clipWidth = Arrays.copyOf (clipWidth, next);
		clipHeight = Arrays.copyOf (clipHeight, next);
		cullX = Arrays.copyOf (cullX, next);
		cullY = Arrays.copyOf (cullY, next);
		cullWidth = Arrays.copyOf (cullWidth, next);
		cullHeight = Arrays.copyOf (cullHeight, next);
		lineWidth = Arrays.copyOf (lineWidth, next);
		lineStyle = Arrays.copyOf (lineStyle, next);
		lineCap = Arrays.copyOf (lineCap, next);
		lineJoin = Arrays.copyOf (lineJoin, next);
		pathTypeOffsets = Arrays.copyOf (pathTypeOffsets, next);
		pathTypeCounts = Arrays.copyOf (pathTypeCounts, next);
		pathPointOffsets = Arrays.copyOf (pathPointOffsets, next);
		pathPointCounts = Arrays.copyOf (pathPointCounts, next);
		pathMinX = Arrays.copyOf (pathMinX, next);
		pathMinY = Arrays.copyOf (pathMinY, next);
		pathMaxX = Arrays.copyOf (pathMaxX, next);
		pathMaxY = Arrays.copyOf (pathMaxY, next);
		a = Arrays.copyOf (a, next);
		b = Arrays.copyOf (b, next);
		c = Arrays.copyOf (c, next);
		d = Arrays.copyOf (d, next);
		flags = Arrays.copyOf (flags, next);
		payloads = Arrays.copyOf (payloads, next);
	}

	private static int [] grow (int [] source, int oldLength, int newLength, int fill) {
		int [] result = Arrays.copyOf (source, newLength);
		Arrays.fill (result, oldLength, newLength, fill);
		return result;
	}

	private void ensurePathTypeCapacity (int required) {
        if (required <= pathTypes.length) {
            return;
        }
		pathTypes = Arrays.copyOf (pathTypes, Math.max (required, pathTypes.length * 2));
	}

	private void ensurePathPointCapacity (int required) {
        if (required <= pathPoints.length) {
            return;
        }
		pathPoints = Arrays.copyOf (pathPoints, Math.max (required, pathPoints.length * 2));
	}

	private void ensureTransformCapacity (int required) {
		int current = transforms.length / 6;
        if (required <= current) {
            return;
        }
		transforms = Arrays.copyOf (transforms, Math.max (required, current * 2) * 6);
	}
}
