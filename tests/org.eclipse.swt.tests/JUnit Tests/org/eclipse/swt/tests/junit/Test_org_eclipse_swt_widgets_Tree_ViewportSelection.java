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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Product contract for a bounded native TreeView over a larger logical forest.
 * The current win32 master is expected RED for cold selection. This class
 * deliberately does not create a second model or touch production sources.
 */
public class Test_org_eclipse_swt_widgets_Tree_ViewportSelection {
    Display display;
    Shell shell;
    AtomicInteger setData;

    @BeforeEach
    public void setUp () {
        Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "win32 TreeViewport oracle");
        display = Display.getDefault ();
        shell = new Shell (display);
        shell.setBounds (20, 20, 320, 420);
        setData = new AtomicInteger ();
    }

    @AfterEach
    public void tearDown () {
        if (shell != null && !shell.isDisposed ()) shell.dispose ();
    }

    private Tree tree (int style, int count) {
        Tree tree = new Tree (shell, style | SWT.VIRTUAL | SWT.V_SCROLL | SWT.BORDER);
        tree.setBounds (5, 5, 300, 380);
        tree.addListener (SWT.SetData, event -> {
            setData.incrementAndGet ();
            ((TreeItem)event.item).setText ("row " + event.index);
        });
        tree.setItemCount (count);
        Assumptions.assumeTrue (tree.getVerticalBar () != null
                && tree.getVerticalBar ().getMaximum () == count - 1,
                "requires mediated Tree with a full logical vertical range");
        return tree;
    }

    @Test
    public void selectAllIncludesColdLogicalRowsWithoutMaterializingThem () {
        final int count = 10_000;
        Tree tree = tree (SWT.MULTI, count);
        AtomicInteger selectionEvents = new AtomicInteger ();
        tree.addListener (SWT.Selection, (Event event) -> selectionEvents.incrementAndGet ());
        shell.open ();
        tree.selectAll ();
        assertEquals (count, tree.getSelectionCount (),
                "selectAll must cover logical rows, not only resident native peers");
        assertEquals (0, selectionEvents.get (), "programmatic selectAll does not fire Selection");
        assertTrue (setData.get () < 512, "selectAll cannot force SetData for all cold rows");
    }

    @Test
    public void selectAllSurvivesDistantViewportScrollAndDeselectAll () {
        Tree tree = tree (SWT.MULTI, 10_000);
        shell.open ();
        tree.selectAll ();
        tree.setTopItem (tree.getItem (9_500));
        assertEquals (10_000, tree.getSelectionCount (),
                "scrolling must preserve cold logical selections");
        tree.deselectAll ();
        assertEquals (0, tree.getSelectionCount ());
        tree.setTopItem (tree.getItem (50));
        assertEquals (0, tree.getSelectionCount (), "off-window selection cannot reappear");
    }

    @Test
    public void shrinkClampsSelectionAndAppendDoesNotSelectNewRows () {
        Tree tree = tree (SWT.MULTI, 600);
        shell.open ();
        tree.selectAll ();
        assertEquals (600, tree.getSelectionCount ());
        tree.setItemCount (400);
        assertEquals (400, tree.getSelectionCount (), "removed rows lose selection");
        tree.setItemCount (900);
        assertEquals (400, tree.getSelectionCount (),
                "later additions cannot inherit a stale selectAll pattern");
    }

    @Test
    public void singleSelectionStyleDoesNotAcquireAnAllSelection () {
        Tree tree = tree (SWT.SINGLE, 600);
        shell.open ();
        int before = tree.getSelectionCount ();
        tree.selectAll ();
        assertEquals (before, tree.getSelectionCount (),
                "Tree.selectAll must remain a no-op for SWT.SINGLE");
    }
}
