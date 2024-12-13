package org.eclipse.swt.tests.gtk;

import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Test_Gtk_Tree_Virtual_setImage {

	private static final int WINDOW_WIDTH = 300;
	private static final int WINDOW_HEIGHT = 100;
	private static final int IMAGE_SIZE = 70;

	private Shell shell;
	private Tree tree;
	private Image image;

	@Before
	public void setUp() {
		shell = new Shell();
		shell.setLayout(new GridLayout());
	}

	@After
	public void tearDown() {
		if (tree != null) {
			tree.dispose();
			tree = null;
		}
		if (image != null) {
			image.dispose();
			image = null;
		}
		if (shell != null) {
			shell.dispose();
			shell = null;
		}
	}

	@Test
	public void test_initial_img_5_2() {
		do_test_initial_img(false, 5, 2);
	}

	@Test
	public void test_initial_img_5_2_wCheckbox() {
		do_test_initial_img(true, 5, 2);
	}

	@Test
	public void test_initial_img_50_22() {
		do_test_initial_img(false, 50, 22);
	}

	@Test
	public void test_initial_img_50_22_wCheckbox() {
		do_test_initial_img(true, 50, 22);
	}

	@Test
	public void test_set_img_50_22() {
		do_test_set_img(false, 50, 22);
	}

	@Test
	public void test_set_img_50_22_wCheckbox() {
		do_test_set_img(true, 50, 22);
	}

	private void do_test_initial_img(boolean withCheckbox, int totalRows, int imgRowIdx) {
		createImage();
		createTree(withCheckbox, totalRows, imgRowIdx, () -> image);
		shell.pack();
		shell.open();

		executePendingUiTasks();

		tree.showItem(tree.getItem(imgRowIdx));

		executePendingUiTasks();

		assertAllTreeRowsResized();
	}

	private void do_test_set_img(boolean withCheckbox, int totalRows, int imgRowIdx) {
		createTree(withCheckbox, totalRows, imgRowIdx, () -> image);
		shell.pack();
		shell.open();

		executePendingUiTasks();

		tree.showItem(tree.getItem(imgRowIdx));

		executePendingUiTasks();

		tree.showItem(tree.getItem(totalRows - 1));

		executePendingUiTasks();

		createImage();

		executePendingUiTasks();

		tree.clear(imgRowIdx, false);

		executePendingUiTasks();

		tree.showItem(tree.getItem(imgRowIdx));

		executePendingUiTasks();

		assertAllTreeRowsResized();
	}

	private void createImage() {
		this.image = new Image(shell.getDisplay(), IMAGE_SIZE, IMAGE_SIZE);
		var color = shell.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		var gc = new GC(image);
		gc.setForeground(color);
		gc.setBackground(color);
		gc.fillRectangle(0, 0, IMAGE_SIZE, IMAGE_SIZE);
		gc.dispose();
	}

	private void createTree(boolean withCheckbox, int totalRows, int imgRowIdx, Supplier<Image> getImage) {
		tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER | (withCheckbox ? SWT.CHECK : 0));
		var layoutData = new GridData(GridData.FILL_BOTH);
		layoutData.widthHint = WINDOW_WIDTH;
		layoutData.heightHint = WINDOW_HEIGHT;
		tree.setLayoutData(layoutData);
		tree.addListener(SWT.SetData, event -> {
			var item = (TreeItem) event.item;
			var idx = tree.indexOf(item);
			item.setText("node " + idx);
			if (idx == imgRowIdx) {
				var image = getImage.get();
				item.setImage(image);
			}
			item.setItemCount(0);
		});
		tree.setItemCount(totalRows);
	}

	private void executePendingUiTasks() {
		while (shell.getDisplay().readAndDispatch()) {
			// perform next pending task
		}
		try {
			// This sleep is required because
			// - gtk perform some updates/renderings on its own inner
			// timers (e.g. a timer for every frame drawing)
			// - readAndDispatch() doesn't wait for those timers
			// - we need to wait for this timers to get fully updated/rendered tree
			Thread.sleep(100);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
		while (shell.getDisplay().readAndDispatch()) {
			// perform next pending task
		}
	}

	private void assertAllTreeRowsResized() {
		var minHeight = IMAGE_SIZE;
		var maxHeight = 2 * IMAGE_SIZE - 1;
		for (var i = 0; i < tree.getItemCount(); i++) {
			var item = tree.getItem(i);
			var height = item.getBounds().height;
			Assert.assertTrue(
					String.format("Expected row[%d].height in [%d;%d], but was %d.", i, minHeight, maxHeight, height),
					minHeight <= height && height <= maxHeight);
		}
	}
}
