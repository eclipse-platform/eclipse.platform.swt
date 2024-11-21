package org.eclipse.swt.tests.gtk;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.gtk.GTK;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Test_Gtk3_Table_Remove_Focused_Row {

	private Shell shell;
	private Table table;

	@BeforeClass
	public static void setUpClass() {
		var isGtk3 = false;
		if ("gtk".equals(SWT.getPlatform())) {
			@SuppressWarnings("restriction")
			var gtkMajorVersion = GTK.GTK_VERSION >>> 16;
			isGtk3 = (gtkMajorVersion == 3);
		}
		Assume.assumeTrue("Test is only for Gtk3.", isGtk3);
	}

	@Before
	public void setUp() {
		shell = new Shell();
		shell.setMinimumSize(400, 400);
		shell.setLayout(new FillLayout());
	}

	@After
	public void tearDown() {
		if (table != null) {
			table.dispose();
		}
		if (shell != null) {
			shell.dispose();
		}
	}

	@Test
	public void test_remove_focused_row_remove_one() {
		test_remove_focused_row_remove_method_arg(() -> table.remove(0));
	}

	@Test
	public void test_remove_focused_row_remove_array() {
		test_remove_focused_row_remove_method_arg(() -> table.remove(new int[] { 0 }));
	}

	@Test
	public void test_remove_focused_row_remove_interval() {
		test_remove_focused_row_remove_method_arg(() -> table.remove(0, 0));
	}

	private void test_remove_focused_row_remove_method_arg(Runnable removeRow0) {
		table = new Table(shell, SWT.VIRTUAL);
		table.setItemCount(2);
		table.addListener(SWT.SetData, event -> {
			var item = (TableItem) event.item;
			item.setText("Item #" + System.identityHashCode(item) + " " + item.toString());
		});

		shell.pack();
		shell.open();

		processUiEvents();

		// set focus on row[0]
		table.setFocus();

		processUiEvents();

		table.clear(0);
		removeRow0.run();

		processUiEvents();
	}

	private void processUiEvents() {
		while (table.getDisplay().readAndDispatch()) {
			// continue to next event
		}
	}

}
