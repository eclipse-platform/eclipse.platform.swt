/*******************************************************************************
 * Copyright (c) 2023, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Vasili Gulevich - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.performance;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.tests.junit.SwtTestUtil;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class Test_org_eclipse_swt_widgets_Tree {
	enum Shape {
		BINARY {
			@Override
			void buildTree(Tree tree, int size, Consumer<TreeItem> initializeItem) {
				tree.setItemCount(2);
				{
					int leftChildCount = size / 2;
					tree.getItem(0).setData("childCount", leftChildCount - 1);
					tree.getItem(1).setData("childCount", size - leftChildCount - 1);
				}
				boolean virtual = (tree.getStyle() & SWT.VIRTUAL) != 0;
				if (virtual) {
					tree.addListener(SWT.SetData, event -> {
						TreeItem item = (TreeItem) event.item;
						initializeItem.accept(item);
						createBinaryChildren(item, initializeItem);
					});
				} else {
					createBinaryBranch(tree.getItem(0), initializeItem);
					createBinaryBranch(tree.getItem(1), initializeItem);
				}
			}

			private void createBinaryBranch(TreeItem item, Consumer<TreeItem> initializeItem) {
				initializeItem.accept(item);
				for (TreeItem child : createBinaryChildren(item, initializeItem)) {
					createBinaryBranch(child, initializeItem);
				}
			}

			@Override
			protected TreeItem lastItem(Tree tree) {
				return lastItem(tree.getItem(1));
			}

			private List<TreeItem> createBinaryChildren(TreeItem item, Consumer<TreeItem> initializeItem) {
				int childCount = (int) item.getData("childCount");
				int leftChildCount = childCount / 2;
				int rightChildCount = childCount - leftChildCount;
				ArrayList<TreeItem> result = new ArrayList<>();
				if (leftChildCount > 0) {
					TreeItem left = new TreeItem(item, SWT.NONE);
					left.setData("childCount", leftChildCount - 1);
					result.add(left);
				}
				if (rightChildCount > 0) {
					TreeItem right = new TreeItem(item, SWT.NONE);
					right.setData("childCount", rightChildCount - 1);
					result.add(right);
				}
				return result;
			}

			private TreeItem lastItem(TreeItem item) {
				TreeItem[] children = item.getItems();
				if (children.length == 0) {
					return item;
				} else {
					return lastItem(children[children.length - 1]);
				}
			}

		},
		STAR {
			@Override
			void buildTree(Tree tree, int size, Consumer<TreeItem> initializeItem) {
				if ((tree.getStyle() & SWT.VIRTUAL) != 0) {
					tree.addListener(SWT.SetData, event -> {
						TreeItem item = (TreeItem) event.item;
						initializeItem.accept(item);
						if (item.getParentItem() == null)
							item.setItemCount(size - 1);
					});
					tree.setItemCount(1);
				} else {
					tree.setItemCount(1);
					TreeItem root = tree.getItem(0);
					initializeItem.accept(root);
					root.setItemCount(size - 1);
					for (TreeItem item : root.getItems()) {
						initializeItem.accept(item);
					}
				}
			}

			@Override
			protected TreeItem lastItem(Tree tree) {
				TreeItem root = tree.getItem(0);
				return root.getItem(tree.getItemCount() - 1);
			}
		};

		abstract void buildTree(Tree parent, int size, Consumer<TreeItem> initializeItem);

		protected abstract TreeItem lastItem(Tree tree);
	}

	@Rule
	public final TestName name = new TestName();
	private final boolean virtual;
	private final Shape shape;
	private final Shell shell = new Shell();
	private final Font font = new Font(shell.getDisplay(), "Arial", 5, 5);
	private final Color foreground = shell.getDisplay().getSystemColor(SWT.COLOR_GREEN);
	private final Color background = shell.getDisplay().getSystemColor(SWT.COLOR_BLACK);

	@Parameters(name = "Shape: {0}, virtual: {1}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] { { Shape.STAR, false }, { Shape.STAR, true }, { Shape.BINARY, false },
				{ Shape.BINARY, true }, });
	}

	public Test_org_eclipse_swt_widgets_Tree(Shape shape, boolean virtual) {
		this.shape = Objects.requireNonNull(shape);
		this.virtual = virtual;
	}

	@Before
	public void setUp() {
		shell.setSize(500, 500);
		shell.setLayout(new FillLayout());
		// Make tree visible to make GTK request updates
		SwtTestUtil.openShell(shell);
	}

	@After
	public void teardown() {
		font.dispose();
		shell.dispose();
	}

	@Test
	public void build() {
		assertMaximumDegree(1.1, n -> {
			return measureNanos(() -> buildSubject(n, this::initializeItem));
		});
	}

	@Test
	public void traverse() {
		assertMaximumDegree(2.1, n -> {
			Tree tree = buildSubject(n, this::initializeItem);
			return measureNanos(() -> {
				int[] count = new int[] { 0 };
				breadthFirstTraverse(tree, item -> count[0]++);
				Assert.assertEquals(n, count[0]);
			});
		});
	}

	@Test
	public void secondTraverse() {
		assertMaximumDegree(1.2, n -> {
			Tree tree = buildSubject(n, this::initializeItem);
			breadthFirstTraverse(tree, item -> {
				item.setExpanded(true);
			});
			return measureNanos(() -> {
				breadthFirstTraverse(tree, item -> {
				});
			});
		});
	}

	@Test
	public void dispose() {
		assertMaximumDegree(1.2, n -> {
			Tree tree = buildSubject(n, this::initializeItem);
			breadthFirstTraverse(tree, item -> {
				item.setExpanded(true);
			});
			return measureNanos(() -> tree.dispose());
		});
	}


	@Test
	public void getForeground() {
		assertMaximumDegree(1.2, n -> {
			Tree tree = buildSubject(n, this::initializeItem);
			breadthFirstTraverse(tree, item -> {
				item.setExpanded(true);
			});
			return measureNanos(() -> {
				breadthFirstTraverse(tree, TreeItem::getForeground);
			});
		});
	}

	@Test
	public void setForeground() {
		assertMaximumDegree(1.3, n -> {
			Tree tree = buildSubject(n, this::initializeItem);
			breadthFirstTraverse(tree, item -> {
				item.setExpanded(true);
			});
			Color cyan = shell.getDisplay().getSystemColor(SWT.COLOR_CYAN);
			return measureNanos(() -> {
				breadthFirstTraverse(tree, item -> item.setForeground(cyan));
			});
		});
	}

	@Test
	public void setText() {
		assertMaximumDegree(1.3, n -> {
			Tree tree = buildSubject(n, this::initializeItem);
			breadthFirstTraverse(tree, item -> {
				item.setExpanded(true);
			});
			return measureNanos(() -> {
				breadthFirstTraverse(tree, item -> item.setText("test"));
			});
		});
	}

	@Test
	public void showItem() {
		assertMaximumDegree(virtual ? 1.2 : 1.9, n -> {
			Tree tree = buildSubject(n, this::initializeItem);
			return measureNanos(() -> tree.showItem(shape.lastItem(tree)));
		});
	}

	@Test
	public void jfaceReveal() {
		// Still demonstrates different characteristics
		assertMaximumDegree(virtual ? 1.1 : 2, n -> {
			Tree tree = buildSubject(n, this::initializeItem);
			return measureNanos(() -> {
				// JFace creates all children and updates each by its index

				TreeItem item = tree.getItem(tree.getItemCount() - 1);

				for (;;) {
					item.setExpanded(true);
					int count = item.getItemCount();
					if (virtual) {
						item.getItems();
						for (int i = 0; i < count; i++) {
							item.getItem(i); // JFace updates children using their indices
						}
					}
					if (count <= 0) {
						tree.showItem(item);
						break;
					}
					item = item.getItem(count - 1);
				}
			});
		});
	}

	private Tree buildSubject(int size, Consumer<TreeItem> initialize) {
		Tree result = new Tree(shell, virtual ? SWT.VIRTUAL : SWT.NONE);
		shell.layout();
		result.setRedraw(false);
		shape.buildTree(result, size, initialize);
		result.setRedraw(true);
		return result;
	}

	/** Ensure that given function grows within acceptable polynomial degree */
	private void assertMaximumDegree(double maximumDegree, IntFunction<Double> function) {
		shell.setText(name.getMethodName());
		clearShell();
		int elementCount[] = new int[] { 10000, 100000 };
		function.apply(elementCount[0]); // warmup
		clearShell();
		double elapsed[] = new double[] { function.apply(elementCount[0]), 0 };
		clearShell();
		elapsed[1] = function.apply(elementCount[1]);
		double ratio = elapsed[1] / elementCount[1] / elapsed[0] * elementCount[0];
		double degree = Math.log(elapsed[1] / elapsed[0]) / Math.log(elementCount[1] / elementCount[0]);
		String error = String.format(
				"Execution time should grow as %f degree polynom. \nTime for %d elements: %f ns\nTime for %d elements: %f ns\nRatio: %f\nDegree: %f\n",
				maximumDegree, elementCount[0], elapsed[0], elementCount[1], elapsed[1], ratio, degree);
		System.out.println(name.getMethodName() + "\n" + error);
		assertTrue(error, (elapsed[1] <= 100 && elapsed[0] <= 100) || degree < maximumDegree);
	}

	private double measureNanos(Runnable runnable) {
		SwtTestUtil.processEvents();
		long start = System.nanoTime();
		runnable.run();
		SwtTestUtil.processEvents();
		long stop = System.nanoTime();
		return stop - start;
	}

	private final AtomicLong itemCount = new AtomicLong(0);

	private void initializeItem(TreeItem item) {
		item.setText(itemCount.getAndIncrement() + "");
		item.setForeground(foreground);
		item.setBackground(background);
		item.setFont(font);
	}

	private void clearShell() {
		for (Control child : shell.getChildren()) {
			child.dispose();
		}
		assert shell.getChildren().length == 0;
		itemCount.set(0);
		SwtTestUtil.processEvents();
	}

	private void breadthFirstTraverse(Tree tree, Consumer<TreeItem> visitor) {
		tree.setRedraw(false);
		try {
			Deque<TreeItem> queue = new LinkedList<>();
			queue.addAll(Arrays.asList(tree.getItems()));
			while (!queue.isEmpty()) {
				TreeItem parent = queue.removeFirst();
				visitor.accept(parent);
				queue.addAll(Arrays.asList(parent.getItems()));
			}
		} finally {
			tree.setRedraw(true);
		}
	}
}
