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
public class Test_ViewportPaintGraphEmptyClip {
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
        assertEquals (System.getProperty ("m3.expected.swt.platform", SWT.getPlatform ()),
                SWT.getPlatform (), "the locally pinned native platform must match");
        assertEquals (System.getProperty ("m3.expected.swt.version", Library.getVersionString ()),
                Library.getVersionString (), "the locally pinned SWT library version must match");
        assertFalse (gc.isDisposed ());
        assertSame (display, gc.getDevice ());
        System.out.println ("NATIVE_PROVIDER=" + SWT.getPlatform () + "; SWT_VERSION=" + SWT.getVersion ()
                + "; LIBRARY=" + Library.getVersionString ()
                + "; TEMP=" + System.getProperty ("java.io.tmpdir"));
    }

    @AfterEach
    public void tearDown () {
        if (gc != null && !gc.isDisposed ()) gc.dispose ();
        if (image != null && !image.isDisposed ()) image.dispose ();
    }

    @Test
    public void emptyTextClipDoesNotVisitOrDraw () {
        ViewportPaintGraph graph = new ViewportPaintGraph ();
        graph.text (graph.root (), "invisible", 4, 4, true);
        assertEmptyReplay (graph, Affine.IDENTITY, new Rectangle (0, 0, 0, 64));
    }

    @Test
    public void emptyAffineClipDoesNotVisitOrDraw () {
        ViewportPaintGraph graph = new ViewportPaintGraph ();
        graph.fillRectangle (graph.root (), 2, 2, 10, 10);
        assertEmptyReplay (graph, Affine.scale (1.5f, 1.5f), new Rectangle (0, 0, 64, 0));
    }

    @Test
    public void negativeAndOverflowOriginEmptyClipsRemainEmpty () {
        ViewportPaintGraph graph = new ViewportPaintGraph ();
        graph.text (graph.root (), "invisible", 4, 4, true);
        assertEmptyReplay (graph, Affine.IDENTITY, new Rectangle (0, 0, -1, 64));
        assertEmptyReplay (graph, Affine.IDENTITY,
                new Rectangle (Integer.MAX_VALUE, Integer.MIN_VALUE, 64, -1));
    }

    @Test
    public void templateAndLayerEmptyReplaySkipCommands () {
        ViewportPaintGraph graph = new ViewportPaintGraph ();
        int template = graph.template ();
        graph.text (template, "invisible", 4, 4, true);
        int pane = graph.group (graph.root ());
        graph.setLayer (pane, 10);
        graph.instance (pane, template, Affine.IDENTITY);
        Rectangle empty = new Rectangle (0, 0, 0, 64);
        assertEquals (new ViewportPaintGraph.ReplayStats (0, 0, 0, 0),
                graph.replayTemplate (gc, template, Affine.IDENTITY, empty));
        assertEquals (new ViewportPaintGraph.ReplayStats (0, 0, 0, 0),
                graph.replayLayer (gc, 10, Affine.IDENTITY, empty));
        assertEquals (new ViewportPaintGraph.ReplayStats (0, 0, 0, 0),
                graph.replayLayers (gc, new int [] {10, 20}, Affine.IDENTITY, empty));
    }

    @Test
    public void emptyReplayPreservesArgumentAndDisposedGcFailures () {
        ViewportPaintGraph graph = new ViewportPaintGraph ();
        Rectangle empty = new Rectangle (0, 0, 0, 64);
        assertThrows (NullPointerException.class, () -> graph.replay (null, Affine.IDENTITY, empty));
        assertThrows (NullPointerException.class, () -> graph.replay (gc, null, empty));
        assertThrows (IllegalArgumentException.class,
                () -> graph.replayTemplate (gc, graph.root (), Affine.IDENTITY, empty));
        gc.dispose ();
        assertThrows (IllegalArgumentException.class, () -> graph.replay (gc, Affine.IDENTITY, empty));
    }

    @Test
    public void nonemptyControlStillPaintsRealPixels () {
        ViewportPaintGraph graph = new ViewportPaintGraph ();
        graph.fillRectangle (graph.root (), 2, 2, 10, 10);
        var stats = graph.replay (gc, Affine.IDENTITY, new Rectangle (0, 0, 64, 64));
        assertEquals (1, stats.drawnCommands ());
        ImageData pixels = image.getImageData ();
        assertEquals (display.getSystemColor (SWT.COLOR_BLACK).getRGB (),
                pixels.palette.getRGB (pixels.getPixel (3, 3)));
        assertEquals (display.getSystemColor (SWT.COLOR_WHITE).getRGB (),
                pixels.palette.getRGB (pixels.getPixel (40, 40)));
    }

    @Test
    public void emptyReplaySkipsLargeRetainedGraphWithoutChangingState () {
        ViewportPaintGraph graph = new ViewportPaintGraph ();
        for (int i = 0; i < 10_000; i++) graph.text (graph.root (), "invisible", 4, 4, true);
        int nodes = graph.nodeCount ();
        assertEmptyReplay (graph, Affine.IDENTITY, new Rectangle (0, 0, 0, 64));
        assertEquals (nodes, graph.nodeCount ());
        assertEquals (10_000, graph.geometryNodeCount ());
    }

    private void assertEmptyReplay (ViewportPaintGraph graph, Affine transform, Rectangle empty) {
        ImageData before = image.getImageData ();
        Rectangle clip = gc.getClipping ();
        int width = gc.getLineWidth ();
        Color foreground = gc.getForeground ();
        Color background = gc.getBackground ();
        Transform state = new Transform (display);
        Transform after = new Transform (display);
        try {
            gc.getTransform (state);
            float [] expected = new float [6];
            state.getElements (expected);
            var stats = graph.replay (gc, transform, empty);
            ImageData pixels = image.getImageData ();
            System.out.println ("EMPTY_CLIP=" + empty + "; STATS=" + stats
                    + "; IMAGE_UNCHANGED=" + Arrays.equals (before.data, pixels.data));
            assertEquals (new ViewportPaintGraph.ReplayStats (0, 0, 0, 0), stats,
                    "empty viewport must not traverse, draw or create a transform path");
            assertArrayEquals (before.data, pixels.data, "empty replay must leave real native pixels unchanged");
            assertEquals (clip, gc.getClipping ());
            assertEquals (width, gc.getLineWidth ());
            assertEquals (foreground, gc.getForeground ());
            assertEquals (background, gc.getBackground ());
            gc.getTransform (after);
            float [] actual = new float [6];
            after.getElements (actual);
            assertArrayEquals (expected, actual);
        } finally {
            after.dispose ();
            state.dispose ();
        }
    }
}
