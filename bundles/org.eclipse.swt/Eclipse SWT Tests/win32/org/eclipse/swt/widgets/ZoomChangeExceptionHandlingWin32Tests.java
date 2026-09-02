/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

/**
 * Tests that a failure while processing a zoom change for a single widget does
 * not abort the zoom change processing of all other widgets, as reported in
 * <a href=
 * "https://github.com/eclipse-platform/eclipse.platform.swt/issues/2432">issue
 * #2432</a>.
 * <p>
 * Such failures are usually caused by resources provided by the application,
 * such as an image that has already been disposed when the widget is adapted to
 * the new zoom. They must be reported to the exception handlers of the
 * {@link Display}, but every other widget must still be adapted and the shell
 * must still be laid out afterwards.
 */
@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class ZoomChangeExceptionHandlingWin32Tests {

	private static final String FAILURE_MESSAGE = "Intentional zoom change failure";

	private Display display;
	private Shell shell;
	private int newZoom;
	// Not declared as java.util.List, as List is the SWT widget in this package
	private final ArrayList<RuntimeException> reportedExceptions = new ArrayList<>();

	@BeforeEach
	void setUp() {
		display = Display.getDefault();
		display.setRuntimeExceptionHandler(reportedExceptions::add);
		shell = new Shell(display);
		newZoom = shell.nativeZoom * 2;
	}

	@AfterEach
	void tearDown() {
		if (!shell.isDisposed()) {
			shell.dispose();
		}
	}

	@Test
	void testFailingControlDoesNotAbortRescalingOfSiblingsWhenProcessedSynchronously() {
		Composite composite = new Composite(shell, SWT.NONE);
		Button failingChild = createFailingButton(composite);
		Button subsequentChild = new Button(composite, SWT.PUSH);

		DPITestUtil.changeDPIZoom(shell, newZoom);

		assertEquals(newZoom, subsequentChild.getAutoscalingZoom(),
				"Sibling of a failing control must still be adapted to the new zoom");
		assertEquals(newZoom, failingChild.getAutoscalingZoom(),
				"Failing control must still be adapted to the new zoom");
		assertReportedFailures(1);
	}

	@Test
	void testFailingControlDoesNotAbortRescalingOfSiblingsWhenProcessedAsynchronously() {
		shell.setLayout(new CountingLayout());
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new CountingLayout());
		Button failingChild = createFailingButton(composite);
		Button subsequentChild = new Button(composite, SWT.PUSH);

		DPITestUtil.changeDPIZoom(shell, newZoom);

		assertEquals(newZoom, subsequentChild.getAutoscalingZoom(),
				"Sibling of a failing control must still be adapted to the new zoom");
		assertEquals(newZoom, failingChild.getAutoscalingZoom(),
				"Failing control must still be adapted to the new zoom");
		assertReportedFailures(1);
	}

	@Test
	void testFailingControlDoesNotAbortRescalingOfOtherComposites() {
		Composite failingComposite = new Composite(shell, SWT.NONE);
		createFailingButton(failingComposite);
		Composite subsequentComposite = new Composite(shell, SWT.NONE);
		Button subsequentChild = new Button(subsequentComposite, SWT.PUSH);

		DPITestUtil.changeDPIZoom(shell, newZoom);

		assertEquals(newZoom, subsequentChild.getAutoscalingZoom(),
				"Control in a composite following a failing one must still be adapted to the new zoom");
		assertReportedFailures(1);
	}

	@Test
	void testFailingCompositeDoesNotAbortRescalingOfItsChildren() {
		Composite failingComposite = new Composite(shell, SWT.NONE);
		// Adapting a control to a new zoom re-applies its region, which fails if the
		// application has disposed that region in the meantime
		Region region = new Region(display);
		region.add(new Rectangle(0, 0, 10, 10));
		failingComposite.setRegion(region);
		region.dispose();
		Button child = new Button(failingComposite, SWT.PUSH);

		DPITestUtil.changeDPIZoom(shell, newZoom);

		assertEquals(newZoom, child.getAutoscalingZoom(),
				"Children of a failing composite must still be adapted to the new zoom");
		assertEquals(1, reportedExceptions.size(),
				"Unexpected number of failures reported to the display: " + reportedExceptions);
	}

	@Test
	void testFailingControlDoesNotAbortRescalingOnMonitorChange() {
		// The processing on a monitor change is the path used in production, in which
		// the shell is not adapted as a task of the zoom change itself
		CountingLayout layout = new CountingLayout();
		shell.setLayout(layout);
		createFailingButton(shell);
		Button subsequentChild = new Button(shell, SWT.PUSH);
		layout.layoutCount = 0;

		DPITestUtil.changeDPIZoomOnMonitorChange(shell, newZoom);

		assertEquals(newZoom, subsequentChild.getAutoscalingZoom(),
				"Sibling of a failing control must still be adapted to the new zoom");
		// One layout is caused by resizing the shell for the new zoom, the other one
		// concludes the zoom change
		assertEquals(2, layout.layoutCount, "The shell must be laid out once per zoom change despite the failure");
		assertReportedFailures(1);
	}

	@Test
	void testFailingControlDoesNotPreventShellLayout() {
		CountingLayout layout = new CountingLayout();
		shell.setLayout(layout);
		createFailingButton(shell);
		layout.layoutCount = 0;

		DPITestUtil.changeDPIZoom(shell, newZoom);

		assertTrue(layout.layoutCount > 0, "The shell must be laid out even if a control failed to be adapted");
		assertReportedFailures(1);
	}

	@Test
	void testFailingItemDoesNotAbortRescalingOfRemainingItemsAndColumns() {
		Table table = new Table(shell, SWT.NONE);
		TableColumn column = new TableColumn(table, SWT.NONE);
		TableItem failingItem = new TableItem(table, SWT.NONE);
		failingItem.addListener(SWT.ZoomChanged, event -> {
			throw new IllegalStateException(FAILURE_MESSAGE);
		});
		TableItem subsequentItem = new TableItem(table, SWT.NONE);

		DPITestUtil.changeDPIZoom(shell, newZoom);

		assertEquals(newZoom, subsequentItem.getAutoscalingZoom(),
				"Item following a failing item must still be adapted to the new zoom");
		assertEquals(newZoom, column.getAutoscalingZoom(),
				"Columns must still be adapted to the new zoom if an item failed");
		assertReportedFailures(1);
	}

	@Test
	void testFailingItemDoesNotAbortRescalingOfItsChildItems() {
		Tree tree = new Tree(shell, SWT.NONE);
		new TreeColumn(tree, SWT.NONE);
		new TreeColumn(tree, SWT.NONE);
		TreeItem failingItem = new TreeItem(tree, SWT.NONE);
		// Adapting an item to a new zoom re-applies its images, which fails if the
		// application has disposed one of them in the meantime
		Image image = new Image(display, 16, 16);
		failingItem.setImage(1, image);
		image.dispose();
		TreeItem childItem = new TreeItem(failingItem, SWT.NONE);

		DPITestUtil.changeDPIZoom(shell, newZoom);

		assertEquals(newZoom, childItem.getAutoscalingZoom(),
				"Child items of a failing item must still be adapted to the new zoom");
		assertEquals(1, reportedExceptions.size(),
				"Unexpected number of failures reported to the display: " + reportedExceptions);
	}

	@Test
	void testFailingMenuItemDoesNotAbortRescalingOfRemainingMenuItems() {
		// A pop-up menu is used instead of a menu bar, as the latter is adapted twice:
		// explicitly as the menu bar and again as part of the decoration's menus
		Menu menu = new Menu(shell, SWT.POP_UP);
		shell.setMenu(menu);
		MenuItem failingItem = new MenuItem(menu, SWT.PUSH);
		failingItem.addListener(SWT.ZoomChanged, event -> {
			throw new IllegalStateException(FAILURE_MESSAGE);
		});
		MenuItem subsequentItem = new MenuItem(menu, SWT.PUSH);

		DPITestUtil.changeDPIZoom(shell, newZoom);

		assertEquals(newZoom, subsequentItem.getAutoscalingZoom(),
				"Menu item following a failing menu item must still be adapted to the new zoom");
		assertReportedFailures(1);
	}

	@Test
	void testFailureIsReportedToDisplayHandlerAndPropagatedIfItIsNotConsumed() {
		display.setRuntimeExceptionHandler(DefaultExceptionHandler.RUNTIME_EXCEPTION_HANDLER);
		Composite composite = new Composite(shell, SWT.NONE);
		createFailingButton(composite);
		Button subsequentChild = new Button(composite, SWT.PUSH);

		IllegalStateException exception = assertThrows(IllegalStateException.class,
				() -> DPITestUtil.changeDPIZoom(shell, newZoom));

		assertEquals(FAILURE_MESSAGE, exception.getMessage(),
				"A failure that is not consumed by the exception handler must still be propagated");
		assertEquals(newZoom, subsequentChild.getAutoscalingZoom(),
				"Sibling of a failing control must be adapted to the new zoom before the failure is propagated");
	}

	private Button createFailingButton(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.addListener(SWT.ZoomChanged, event -> {
			throw new IllegalStateException(FAILURE_MESSAGE);
		});
		return button;
	}

	private void assertReportedFailures(int expectedCount) {
		assertEquals(expectedCount, reportedExceptions.size(),
				"Unexpected number of failures reported to the display: " + reportedExceptions);
		for (RuntimeException reportedException : reportedExceptions) {
			assertEquals(FAILURE_MESSAGE, reportedException.getMessage());
		}
	}

	private static final class CountingLayout extends Layout {
		int layoutCount;

		@Override
		protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
			return new Point(wHint == SWT.DEFAULT ? 100 : wHint, hHint == SWT.DEFAULT ? 100 : hHint);
		}

		@Override
		protected void layout(Composite composite, boolean flushCache) {
			layoutCount++;
		}
	}
}
