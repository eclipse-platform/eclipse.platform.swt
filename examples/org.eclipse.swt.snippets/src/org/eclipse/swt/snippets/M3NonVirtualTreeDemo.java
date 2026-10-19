/*******************************************************************************
 * Copyright (c) 2026 Hitesh Soliwal and contributors.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 (EPL-2.0).
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Executable demonstration: the SWT Tree itself is NON-virtual.
 * Only the small, visible part of a much larger logical model is materialized.
 * The controller explicitly mediates SetData for every real TreeItem.
 * This is a snippet, not a replacement for SWT's public SWT.VIRTUAL contract.
 */
public final class M3NonVirtualTreeDemo {
    private static final String NODE_KEY = "m3.node";
    private static final String PAGE_KEY = "m3.page";
    private static final String PLACEHOLDER_KEY = "m3.placeholder";

    private record Page(M3TreeDemandModel.Key parent, int start) {}

    private static final class Controller {
        private final Tree tree;
        private final Label stats;
        private final Text trace;
        private final M3TreeDemandModel.Mediator mediator = new M3TreeDemandModel.Mediator();
        private long pages;
        private int logs;

        Controller(Tree tree, Label stats, Text trace) {
            this.tree = Objects.requireNonNull(tree);
            this.stats = Objects.requireNonNull(stats);
            this.trace = Objects.requireNonNull(trace);
            if ((tree.getStyle() & SWT.VIRTUAL) != 0) {
                throw new IllegalStateException("Internal SWT Tree must be non-virtual");
            }
            tree.addListener(SWT.SetData, event -> {
                if (!(event.data instanceof M3TreeDemandModel.Key key)
                        || !(event.item instanceof TreeItem item)) {
                    throw new IllegalArgumentException("invalid mediated SetData request");
                }
                var description = M3TreeDemandModel.describe(key);
                item.setText(new String[] {
                        description.title(), description.kind(), description.address()
                });
            });
            tree.addListener(SWT.Expand, event -> {
                TreeItem item = (TreeItem) event.item;
                if (Boolean.TRUE.equals(item.getData(PLACEHOLDER_KEY))) {
                    item.setData(PLACEHOLDER_KEY, Boolean.FALSE);
                    var parent = (M3TreeDemandModel.Key) item.getData(NODE_KEY);
                    showPage(item, parent, 0);
                }
            });
            tree.addListener(SWT.Selection, event -> {
                TreeItem selected = (TreeItem) event.item;
                if (selected == null || selected.isDisposed()) return;
                Object request = selected.getData(PAGE_KEY);
                if (request instanceof Page page) {
                    TreeItem parent = selected.getParentItem();
                    if (parent != null && !parent.isDisposed()) {
                        showPage(parent, page.parent(), page.start());
                        parent.setExpanded(true);
                        if (parent.getItemCount() > 0) {
                            tree.setSelection(parent.getItem(0));
                        }
                    }
                    return;
                }
                Object logical = selected.getData(NODE_KEY);
                if (logical instanceof M3TreeDemandModel.Key key) {
                    trace("selected " + M3TreeDemandModel.describe(key).address());
                }
            });
        }

        void openFirst() {
            if (tree.getItemCount() != 0) {
                TreeItem first = tree.getItem(0);
                if (Boolean.TRUE.equals(first.getData(PLACEHOLDER_KEY))) {
                    first.setData(PLACEHOLDER_KEY, Boolean.FALSE);
                    showPage(first, (M3TreeDemandModel.Key) first.getData(NODE_KEY), 0);
                }
                first.setExpanded(true);
                tree.setSelection(first);
            }
        }

        void reset() {
            tree.setRedraw(false);
            try {
                tree.removeAll();
                for (int group = 0; group < M3TreeDemandModel.GROUPS; group++) {
                    createRoot(M3TreeDemandModel.root(group), group);
                }
                pages = 0;
            } finally {
                tree.setRedraw(true);
            }
            trace("non-virtual SWT Tree reset (root peers only)");
            updateStats();
        }

        private void createRoot(M3TreeDemandModel.Key key, int logicalIndex) {
            TreeItem item = new TreeItem(tree, SWT.NONE);
            connect(item, key, logicalIndex);
        }

        private void connect(TreeItem item, M3TreeDemandModel.Key key, int logicalIndex) {
            item.setData(NODE_KEY, key);
            try {
                mediator.deliver(key, logicalIndex, (node, index) -> {
                    Event event = new Event();
                    event.item = item;
                    event.index = index;
                    event.data = node;
                    // A normal SWT Tree never sends SetData automatically.
                    // This controller explicitly mediates it, including nested context.
                    tree.notifyListeners(SWT.SetData, event);
                    if (item.isDisposed()) {
                        throw new IllegalStateException("SetData disposed its TreeItem");
                    }
                });
            } catch (RuntimeException | Error failure) {
                if (!item.isDisposed()) item.dispose();
                throw failure;
            }
            if (M3TreeDemandModel.children(key) > 0) {
                TreeItem placeholder = new TreeItem(item, SWT.NONE);
                placeholder.setText("Expand to request logical children…");
                item.setData(PLACEHOLDER_KEY, Boolean.TRUE);
            }
        }

        private void showPage(TreeItem parent, M3TreeDemandModel.Key key, int start) {
            int total = M3TreeDemandModel.children(key);
            if (start < 0 || start >= total || start % M3TreeDemandModel.PAGE_SIZE != 0) {
                throw new IllegalArgumentException("unreviewed page coordinate");
            }
            int end = Math.min(total, start + M3TreeDemandModel.PAGE_SIZE);
            tree.setRedraw(false);
            try {
                for (TreeItem previous : parent.getItems()) previous.dispose();
                if (start > 0) {
                    pager(parent, key, Math.max(0, start - M3TreeDemandModel.PAGE_SIZE),
                            "◀ Previous " + M3TreeDemandModel.PAGE_SIZE + "…");
                }
                for (int index = start; index < end; index++) {
                    TreeItem item = new TreeItem(parent, SWT.NONE);
                    connect(item, M3TreeDemandModel.child(key, index), index);
                }
                if (end < total) {
                    pager(parent, key, end, "▶ Next " + M3TreeDemandModel.PAGE_SIZE + "…");
                }
                pages++;
            } finally {
                tree.setRedraw(true);
            }
            trace("mediated SetData for " + start + "…" + (end - 1)
                    + " of " + total + " children under " + M3TreeDemandModel.describe(key).address());
            updateStats();
        }

        private void pager(TreeItem parent, M3TreeDemandModel.Key key, int next, String text) {
            TreeItem item = new TreeItem(parent, SWT.NONE);
            item.setText(new String[] {text, "Paging command", "no logical item allocated"});
            item.setData(PAGE_KEY, new Page(key, next));
        }

        private void trace(String message) {
            if (++logs <= 150) trace.append(message + System.lineSeparator());
        }

        private static int count(TreeItem[] items) {
            int sum = 0;
            for (TreeItem item : items) {
                sum += 1 + count(item.getItems());
            }
            return sum;
        }

        private void updateStats() {
            stats.setText("Logical nodes: " + M3TreeDemandModel.LOGICAL_NODES
                    + "   |   Native TreeItems: " + count(tree.getItems())
                    + "   |   Explicit SetData calls: " + mediator.completed()
                    + "   |   Page binds: " + pages
                    + "   |   SWT.VIRTUAL: OFF");
        }
    }

    public static void main(String[] args) {
        Display display = new Display();
        try {
            Shell shell = new Shell(display);
            shell.setText("M3 Tree — Non-virtual SWT, mediated SetData");
            shell.setLayout(new GridLayout(1, false));
            Label heading = new Label(shell, SWT.WRAP);
            heading.setText("A normal SWT Tree holds only requested rows. Expand packages or select a page command. "
                    + "Each real row receives data through an explicit, controller-owned SWT.SetData event.");
            heading.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            Tree tree = new Tree(shell, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION
                    | SWT.H_SCROLL | SWT.V_SCROLL); // Deliberately NO SWT.VIRTUAL.
            tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            tree.setHeaderVisible(true);
            tree.setLinesVisible(true);
            String[] titles = {"Logical node", "Role", "Coordinate"};
            int[] widths = {330, 220, 220};
            for (int i = 0; i < titles.length; i++) {
                TreeColumn column = new TreeColumn(tree, SWT.LEFT);
                column.setText(titles[i]);
                column.setWidth(widths[i]);
            }
            Label stats = new Label(shell, SWT.NONE);
            stats.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            Text trace = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL);
            GridData traceLayout = new GridData(SWT.FILL, SWT.FILL, true, false);
            traceLayout.heightHint = 110;
            trace.setLayoutData(traceLayout);
            Controller controller = new Controller(tree, stats, trace);
            Button expand = new Button(shell, SWT.PUSH);
            expand.setText("Open first module");
            expand.addListener(SWT.Selection, event -> controller.openFirst());
            Button reset = new Button(shell, SWT.PUSH);
            reset.setText("Reset physical TreeItems");
            reset.addListener(SWT.Selection, event -> controller.reset());
            controller.reset();
            controller.openFirst();
            shell.setSize(900, 720);
            shell.open();
            for (String arg : args) {
                if (arg.startsWith("--close-after-ms=")) {
                    int timeout = Integer.parseInt(arg.substring("--close-after-ms=".length()));
                    if (timeout < 1 || timeout > 60_000) throw new IllegalArgumentException("timeout");
                    display.timerExec(timeout, () -> { if (!shell.isDisposed()) shell.dispose(); });
                }
            }
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
            }
        } finally {
            display.dispose();
        }
    }
}
