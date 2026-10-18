/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ViewportPaintGraph.Affine;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

@SuppressWarnings("restriction")
public class Test_org_eclipse_swt_internal_ViewportPaintGraph {
	private Display display;
	private Image image;
	private GC gc;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		image = new Image (display, 64, 64);
		gc = new GC (image);
		gc.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
		gc.fillRectangle (image.getBounds ());
		gc.setBackground (display.getSystemColor (SWT.COLOR_BLACK));
		gc.setForeground (display.getSystemColor (SWT.COLOR_BLACK));
	}

	@AfterEach
	public void tearDown () {
        if (gc != null && !gc.isDisposed()) {
            gc.dispose();
        }
        if (image != null && !image.isDisposed()) {
            image.dispose();
        }
	}


	@Test
	public void test_thickLineOutsideClipStillPaintsItsVisibleStroke () {
		assertStrokeMatchesImmediate (false, 4, 8, 52, 8, 12, SWT.CAP_FLAT,
				new Rectangle (0, 12, 64, 16));
	}

	@Test
	public void test_squareCapOutsideClipStillPaintsItsVisibleCorner () {
		assertStrokeMatchesImmediate (false, 8, 8, 20, 20, 12, SWT.CAP_SQUARE,
				new Rectangle (22, 18, 20, 20));
	}

	@Test
	public void test_rectangleOutsideClipStillPaintsItsVisibleJoin () {
		assertStrokeMatchesImmediate (true, 8, 8, 32, 32, 12, SWT.CAP_FLAT,
				new Rectangle (43, 43, 12, 12));
	}

	@Test
	public void test_fractionalStrokeOutsideClipMatchesImmediateGc () {
		assertStrokeMatchesImmediate (false, 4, 9, 52, 9, 9.5f, SWT.CAP_ROUND,
				new Rectangle (0, 12, 64, 16));
	}

	@Test
	public void test_groupCullBoundsIncludeVisibleStrokeOutsideGeometryBounds () {
		Rectangle clip = new Rectangle (0, 33, 40, 10);
		Image reference = new Image (display, 64, 64);
		GC immediate = new GC (reference);
		try {
			immediate.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
			immediate.fillRectangle (reference.getBounds ());
			immediate.setForeground (display.getSystemColor (SWT.COLOR_BLACK));
			LineAttributes stroke = new LineAttributes (12, SWT.CAP_FLAT, SWT.JOIN_MITER);
			immediate.setLineAttributes (stroke);
			gc.setLineAttributes (stroke);
			immediate.setClipping (clip);
			gc.setClipping (clip);

			ViewportPaintGraph graph = new ViewportPaintGraph ();
			int group = graph.group (graph.root ());
			graph.setCullBounds (group, 0, 0, 32, 32);
			graph.line (group, 4, 31, 28, 31);

			immediate.drawLine (4, 31, 28, 31);
			var stats = graph.replay (gc, Affine.IDENTITY, clip);

			assertImagesEqual (reference, image,
					"coarse group bounds must include stroke pixels crossing the logical group edge");
			assertEquals (1, stats.drawnCommands ());
		} finally {
			immediate.dispose ();
			reference.dispose ();
		}
	}

	private void assertStrokeMatchesImmediate (boolean rectangle, int x, int y,
			int x2OrWidth, int y2OrHeight, float width, int cap, Rectangle clip) {
		Image reference = new Image (display, 64, 64);
		GC immediate = new GC (reference);
		try {
			immediate.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
			immediate.fillRectangle (reference.getBounds ());
			immediate.setForeground (display.getSystemColor (SWT.COLOR_BLACK));
			LineAttributes stroke = new LineAttributes (width, cap, SWT.JOIN_MITER);
			immediate.setLineAttributes (stroke);
			gc.setLineAttributes (stroke);
			immediate.setClipping (clip);
			gc.setClipping (clip);

			ViewportPaintGraph graph = new ViewportPaintGraph ();
			if (rectangle) {
				immediate.drawRectangle (x, y, x2OrWidth, y2OrHeight);
				graph.drawRectangle (graph.root (), x, y, x2OrWidth, y2OrHeight);
			} else {
				immediate.drawLine (x, y, x2OrWidth, y2OrHeight);
				graph.line (graph.root (), x, y, x2OrWidth, y2OrHeight);
			}
			var stats = graph.replay (gc, Affine.IDENTITY, clip);
			assertImagesEqual (reference, image,
					"retained stroke must match immediate SWT GC under the same clip");
			assertEquals (1, stats.drawnCommands ());
		} finally {
			immediate.dispose ();
			reference.dispose ();
		}
	}

	private void assertImagesEqual (Image expectedImage, Image actualImage, String message) {
		ImageData expected = expectedImage.getImageData ();
		ImageData actual = actualImage.getImageData ();
		int painted = 0;
		RGB white = rgb (SWT.COLOR_WHITE);
		for (int py = 0; py < 64; py++) {
			for (int px = 0; px < 64; px++) {
				RGB expectedRgb = expected.palette.getRGB (expected.getPixel (px, py));
                if (!expectedRgb.equals(white)) {
                    painted++;
                }
				assertEquals (
						expectedRgb,
						actual.palette.getRGB (actual.getPixel (px, py)),
						message + " at " + px + "," + py);
			}
		}
		assertTrue (painted > 0, "the native oracle must paint inside the clip");
	}

	@Test
	public void test_sharedTemplateInstancesDoNotDuplicateGeometry () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int row = graph.template ();
		graph.fillRectangle (row, 0, 0, 4, 4);
		graph.instance (graph.root (), row, Affine.translation (10, 10));
		graph.instance (graph.root (), row, Affine.translation (30, 10));

		assertEquals (1, graph.geometryNodeCount ());
		assertEquals (5, graph.nodeCount ());

		var stats = graph.replay (gc, Affine.IDENTITY, new Rectangle (0, 0, 64, 64));
		assertEquals (2, stats.drawnCommands ());
		assertEquals (rgb (SWT.COLOR_BLACK), pixelRgb (11, 11));
		assertEquals (rgb (SWT.COLOR_BLACK), pixelRgb (31, 11));
		assertEquals (rgb (SWT.COLOR_WHITE), pixelRgb (20, 11));
	}

	@Test
	public void test_translationClipCullsWithoutChangingGraph () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int row = graph.template ();
		graph.line (row, 0, 0, 20, 0);

		var hidden = graph.replayTemplate (
				gc, row, Affine.translation (0, 40), new Rectangle (0, 0, 32, 20));
		assertEquals (0, hidden.drawnCommands ());
		assertEquals (1, hidden.culledCommands ());

		var visible = graph.replayTemplate (
				gc, row, Affine.translation (0, 10), new Rectangle (0, 0, 32, 20));
		assertEquals (1, visible.drawnCommands ());
		assertEquals (0, visible.culledCommands ());
		assertEquals (1, graph.geometryNodeCount ());
	}


	@Test
	public void test_groupCullBoundsSkipOffscreenRetainedSubtreesBeforeCommands () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int row = graph.template ();
		graph.setCullBounds (row, 0, 0, 96, 20);
		for (int i = 0; i < 64; i++) {
			graph.line (row, 0, i % 20, 95, i % 20);
		}
		for (int i = 0; i < 200; i++) {
			graph.instance (graph.root (), row, Affine.translation (0, i * 24));
		}

		var stats = graph.replay (
				gc, Affine.IDENTITY, new Rectangle (0, 0, 96, 20));

		assertEquals (64, stats.drawnCommands (),
				"only the first row instance intersects the replay clip");
		assertTrue (stats.visitedNodes () < 500,
				"coarse retained bounds must reject offscreen row subtrees before their child commands");
		assertEquals (64, graph.geometryNodeCount (),
				"culling must not duplicate or mutate retained geometry");
	}

	@Test
	public void test_groupCullBoundsAreConservativeUnderAffineTransforms () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int group = graph.group (
				graph.root (),
				new Affine (0, 1, -1, 0, 40, 10));
		graph.setCullBounds (group, 0, 0, 20, 10);
		graph.fillRectangle (group, 0, 0, 20, 10);

		var visible = graph.replay (
				gc, Affine.IDENTITY, new Rectangle (28, 8, 16, 24));
		assertEquals (1, visible.drawnCommands (),
				"conservative affine AABB must retain a rotated subtree that intersects the clip");

		var hidden = graph.replay (
				gc, Affine.IDENTITY, new Rectangle (0, 40, 12, 12));
		assertEquals (0, hidden.drawnCommands (),
				"affine subtree bounds outside the clip must be rejected as a unit");
	}

	@Test
	public void test_generalAffineReplayRestoresCallerTransform () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int template = graph.template ();
		graph.drawRectangle (template, 1, 1, 8, 6);
		graph.instance (graph.root (), template, new Affine (1, 0.25f, 0.5f, 1, 7, 9));

		Transform caller = new Transform (display, 1, 0, 0, 1, 3, 4);
		Transform after = new Transform (display);
		try {
			gc.setTransform (caller);
			graph.replay (gc);
			gc.getTransform (after);

			float [] expected = new float [6];
			float [] actual = new float [6];
			caller.getElements (expected);
			after.getElements (actual);
			assertArrayEquals (expected, actual, 0.0001f,
					"retained replay must restore the caller's GC transform");
		} finally {
			after.dispose ();
			caller.dispose ();
		}
	}

	@Test
	public void test_instanceGraphRejectsCycles () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int row = graph.template ();
		int decoration = graph.template ();
		graph.instance (row, decoration, Affine.IDENTITY);
		assertThrows (IllegalArgumentException.class,
				() -> graph.instance (decoration, row, Affine.IDENTITY));
	}

	@Test
	public void test_affineCompositionMatchesNestedViewportTranslation () {
		Affine body = Affine.translation (-720, -440);
		Affine row = Affine.translation (0, 220);
		Affine combined = body.compose (row);

		assertEquals (-720f, combined.dx ());
		assertEquals (-220f, combined.dy ());
		assertTrue (combined.isIntegralTranslation ());
	}


	@Test
	public void test_viewportPlanesMapPaintAndEventsThroughSameAffineChain () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int body = graph.group (graph.root (), Affine.translation (-620, -1400));
		int frozen = graph.group (graph.root (), Affine.translation (0, -1400));
		int header = graph.group (graph.root (), Affine.translation (-620, 0));
		int editor = graph.group (body, new Affine (0, 1, -1, 0, 80, 40));

		graph.setLayer (body, 10);
		graph.setLayer (frozen, 20);
		graph.setLayer (header, 30);
		graph.setLayer (editor, 40);

		float [] point = new float [2];
		graph.mapToRoot (body, 700, 1500, point);
		assertArrayEquals (new float[] {80, 100}, point, 0.0001f,
				"body follows horizontal and vertical logical origin");

		graph.mapToRoot (header, 700, 15, point);
		assertArrayEquals (new float[] {80, 15}, point, 0.0001f,
				"header follows horizontal projection but remains fixed vertically");

		graph.mapToRoot (frozen, 40, 1500, point);
		assertArrayEquals (new float[] {40, 100}, point, 0.0001f,
				"frozen plane follows vertical projection without horizontal translation");

		float [] rootPoint = new float [2];
		graph.mapToRoot (editor, 5, 10, rootPoint);
		float [] local = new float [2];
		assertTrue (graph.mapFromRoot (editor, rootPoint [0], rootPoint [1], local));
		assertArrayEquals (new float[] {5, 10}, local, 0.0001f,
				"event coordinates must invert the same chain used for paint");

		int [] layer = new int [1];
		assertTrue (graph.effectiveLayer (editor, layer));
		assertEquals (40, layer [0]);
		graph.clearLayer (editor);
		assertTrue (graph.effectiveLayer (editor, layer));
		assertEquals (10, layer [0],
				"clearing editor layer inherits the body z-plane");

		int detached = graph.template ();
		assertThrows (IllegalArgumentException.class,
				() -> graph.mapToRoot (detached, 0, 0, new float [2]),
				"detached reusable templates have no unique root coordinate");
	}

	@Test
	public void test_viewportClipAndStrokeStateInheritWithoutRetainedGc () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int viewport = graph.group (graph.root ());
		int body = graph.group (viewport, Affine.translation (-100, -200));
		int child = graph.group (body, Affine.translation (50, 25));

		graph.setClip (viewport, 0, 0, 1000, 700);
		graph.setClip (body, 100, 200, 800, 600);
		graph.setClip (child, 0, 0, 300, 300);

		float [] clip = new float [4];
		assertTrue (graph.rootClip (child, clip));
		assertArrayEquals (new float[] {0, 0, 250, 125}, clip, 0.0001f,
				"nested local clips become one conservative root-space clip");

		graph.setStroke (body, 3, SWT.LINE_DASH, SWT.CAP_ROUND, SWT.JOIN_BEVEL);
		int [] stroke = new int [4];
		assertTrue (graph.effectiveStroke (child, stroke));
		assertArrayEquals (
				new int[] {3, SWT.LINE_DASH, SWT.CAP_ROUND, SWT.JOIN_BEVEL}, stroke);

		graph.setStroke (child, 1, SWT.LINE_SOLID, SWT.CAP_FLAT, SWT.JOIN_MITER);
		assertTrue (graph.effectiveStroke (child, stroke));
		assertArrayEquals (
				new int[] {1, SWT.LINE_SOLID, SWT.CAP_FLAT, SWT.JOIN_MITER}, stroke);
		graph.clearStroke (child);
		assertTrue (graph.effectiveStroke (child, stroke));
		assertEquals (3, stroke [0]);
	}

	@Test
	public void test_groupLocalTransformParticipatesInRetainedReplay () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int body = graph.group (graph.root (), Affine.translation (10, 10));
		graph.fillRectangle (body, 0, 0, 4, 4);

		var stats = graph.replay (gc, Affine.IDENTITY, new Rectangle (0, 0, 64, 64));
		assertEquals (1, stats.drawnCommands ());
		assertEquals (rgb (SWT.COLOR_BLACK), pixelRgb (11, 11));
		assertEquals (rgb (SWT.COLOR_WHITE), pixelRgb (5, 5));
	}

	@Test
	public void test_affineInverseRejectsSingularTransform () {
		Affine singular = new Affine (0, 0, 0, 0, 0, 0);
		assertFalse (singular.inverseMap (10, 10, new float [2]));

		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int node = graph.group (graph.root (), singular);
		assertFalse (graph.mapFromRoot (node, 10, 10, new float [2]));
	}


	@Test
	public void test_retainedZOrderReordersSiblingsAndPreservesEqualOrder () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int fill = graph.group (graph.root ());
		graph.fillRectangle (fill, 8, 8, 24, 12);
		int stroke = graph.group (graph.root ());
		graph.line (stroke, 8, 10, 31, 10);

		gc.setBackground (display.getSystemColor (SWT.COLOR_RED));
		gc.setForeground (display.getSystemColor (SWT.COLOR_BLUE));

		graph.setZOrder (fill, 10);
		graph.setZOrder (stroke, 20);
		assertEquals (10, graph.zOrder (fill));
		assertEquals (20, graph.zOrder (stroke));
		graph.replay (gc, Affine.IDENTITY, new Rectangle (0, 0, 64, 64));
		assertEquals (rgb (SWT.COLOR_BLUE), pixelRgb (12, 10),
				"higher z retained stroke must paint above the fill");

		gc.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
		gc.fillRectangle (image.getBounds ());
		gc.setBackground (display.getSystemColor (SWT.COLOR_RED));
		graph.setZOrder (stroke, 0);
		graph.replay (gc, Affine.IDENTITY, new Rectangle (0, 0, 64, 64));
		assertEquals (rgb (SWT.COLOR_RED), pixelRgb (12, 10),
				"lower z retained stroke must paint behind the fill");

		gc.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
		gc.fillRectangle (image.getBounds ());
		gc.setBackground (display.getSystemColor (SWT.COLOR_RED));
		graph.setZOrder (fill, 5);
		graph.setZOrder (stroke, 5);
		graph.replay (gc, Affine.IDENTITY, new Rectangle (0, 0, 64, 64));
		assertEquals (rgb (SWT.COLOR_BLUE), pixelRgb (12, 10),
				"equal z must preserve creation order, matching historical replay order");

		assertThrows (IllegalArgumentException.class,
				() -> graph.setZOrder (graph.root (), 1),
				"the root has no sibling stacking context");
	}

	@Test
	public void test_orderedLayerReplayFiltersAndPreservesBackToFrontSequence () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int body = graph.group (graph.root ());
		graph.setLayer (body, 10);
		graph.fillRectangle (body, 2, 2, 4, 4);

		int header = graph.group (graph.root ());
		graph.setLayer (header, 30);
		graph.fillRectangle (header, 20, 2, 4, 4);

		var headerOnly = graph.replayLayer (
				gc, 30, Affine.IDENTITY, new Rectangle (0, 0, 64, 64));
		assertEquals (1, headerOnly.drawnCommands ());
		assertEquals (rgb (SWT.COLOR_WHITE), pixelRgb (3, 3));
		assertEquals (rgb (SWT.COLOR_BLACK), pixelRgb (21, 3));

		gc.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
		gc.fillRectangle (image.getBounds ());
		gc.setBackground (display.getSystemColor (SWT.COLOR_BLACK));

		var layered = graph.replayLayers (
				gc, new int[] {10, 30}, Affine.IDENTITY, new Rectangle (0, 0, 64, 64));
		assertEquals (2, layered.drawnCommands ());
		assertEquals (rgb (SWT.COLOR_BLACK), pixelRgb (3, 3));
		assertEquals (rgb (SWT.COLOR_BLACK), pixelRgb (21, 3));
	}


	@Test
	public void test_retainedPathCopiesPrimitiveGeometryAndReplaysAcrossInstances () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int template = graph.template ();
		PathData data = new PathData ();
		data.types = new byte[] {
				SWT.PATH_MOVE_TO, SWT.PATH_LINE_TO, SWT.PATH_LINE_TO, SWT.PATH_CLOSE};
		data.points = new float[] {0, 0, 10, 0, 10, 10};
		graph.fillPath (template, data);
		graph.instance (graph.root (), template, Affine.translation (10, 10));
		graph.instance (graph.root (), template, Affine.translation (30, 10));

		data.types [0] = SWT.PATH_CLOSE;
		Arrays.fill (data.points, 50);

		var first = graph.replay (gc, Affine.IDENTITY, new Rectangle (0, 0, 64, 64));
		assertEquals (2, first.drawnCommands ());
		assertEquals (1, graph.geometryNodeCount ());
		assertEquals (rgb (SWT.COLOR_BLACK), pixelRgb (17, 13));
		assertEquals (rgb (SWT.COLOR_BLACK), pixelRgb (37, 13));
		assertEquals (rgb (SWT.COLOR_WHITE), pixelRgb (55, 55));

		var second = graph.replay (gc, Affine.IDENTITY, new Rectangle (0, 0, 64, 64));
		assertEquals (2, second.drawnCommands (),
				"replay-local native Path resources must be reconstructible after disposal");
	}

	@Test
	public void test_retainedPathRejectsMalformedOrNonFiniteGeometry () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();

		PathData mismatched = new PathData ();
		mismatched.types = new byte[] {SWT.PATH_MOVE_TO};
		mismatched.points = new float[] {1};
		assertThrows (IllegalArgumentException.class,
				() -> graph.drawPath (graph.root (), mismatched));

		PathData unknown = new PathData ();
		unknown.types = new byte[] {(byte)0x7f};
		unknown.points = new float[0];
		assertThrows (IllegalArgumentException.class,
				() -> graph.drawPath (graph.root (), unknown));

		PathData nonFinite = new PathData ();
		nonFinite.types = new byte[] {SWT.PATH_MOVE_TO};
		nonFinite.points = new float[] {Float.NaN, 0};
		assertThrows (IllegalArgumentException.class,
				() -> graph.fillPath (graph.root (), nonFinite));
	}

	@Test
	public void test_retainedDrawPathCullingIncludesStrokeEnvelope () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		PathData data = new PathData ();
		data.types = new byte[] {SWT.PATH_MOVE_TO, SWT.PATH_LINE_TO};
		data.points = new float[] {4, 8, 52, 8};
		graph.drawPath (graph.root (), data);

		gc.setLineWidth (12);
		Rectangle clip = new Rectangle (0, 12, 64, 16);
		gc.setClipping (clip);
		var stats = graph.replay (gc, Affine.IDENTITY, clip);
		assertEquals (1, stats.drawnCommands (),
				"path centreline outside the clip must survive when its stroke enters the viewport");
	}

	private RGB pixelRgb (int x, int y) {
		ImageData data = image.getImageData ();
		int pixel = data.getPixel (x, y);
		return data.palette.getRGB (pixel);
	}

	private RGB rgb (int swtColor) {
		return display.getSystemColor (swtColor).getRGB ();
	}
	@Test
	public void test_repeatedViewportTransformsReuseOneOwnedSlot () {
		ViewportPaintGraph graph = new ViewportPaintGraph ();
		int moving = graph.group (graph.root ());
		int stationary = graph.group (graph.root (), Affine.translation (40, 50));
		int baseline = graph.transformCount ();
		float [] point = new float [2];
		for (int frame = 0; frame < 10_000; frame++) {
			Affine transform = (frame & 1) == 0 ? Affine.translation (frame, -frame) : Affine.IDENTITY;
			graph.setTransform (moving, transform);
			graph.mapToRoot (moving, 2, 3, point);
			assertArrayEquals (new float [] {2 + transform.dx (), 3 + transform.dy ()}, point);
			assertTrue (graph.transformCount () <= baseline + 1,
					"scrolling must retain at most one transform slot per moving group");
		}
		graph.mapToRoot (stationary, 2, 3, point);
		assertArrayEquals (new float [] {42, 53}, point,
				"updating one group must not change another group's transform");
	}

}
