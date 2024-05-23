/*******************************************************************************
 * Copyright (c) 2020 Paul Pazderski and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Paul Pazderski - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32.snippets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.ole.win32.IDataObject;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.tests.win32.SwtWin32TestUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * This one is primary a manual test because it is platform dependent, kind of
 * slow, a bit unreliable (can succeed even if bug exist; see dragDropCount) and
 * grabs the mouse while testing. Apart from that the test runs automatically.
 * Just start and see if the JVM crashes.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("restriction")
public class Bug567422_DNDCrash {

	enum TestMode {
		EXPECTED,
		UNCOMMON,
		EVIL,
	}

	public static void main(String[] args) throws InterruptedException {
		for (TestMode mode : TestMode.values()) {
			new Bug567422_DNDCrash().testBug567422(mode);
		}
	}

	/**
	 * Simulate drag and drop on a target with expected behaviour which is that the
	 * target release all references on the COM objects it acquires but not more and
	 * therefore is actually finished once the drop is performed.
	 */
	@Test
	public void testBug567422_1_expectedDropTarget() throws InterruptedException {
		testBug567422(TestMode.EXPECTED);
	}

	/**
	 * Simulate drag and drop on a target with uncommon behaviour which is that the
	 * target does not release all references on the COM objects once the drop is
	 * performed. Holding a reference to the IDataObject after the drop is done or
	 * canceled is not wrong but uncommon.
	 */
	@Test
	public void testBug567422_2_uncommonDropTarget() throws InterruptedException {
		testBug567422(TestMode.UNCOMMON);
	}

	/**
	 * Simulate drag and drop on a target with evil or faulty behaviour which is that the
	 * target releases more references on the COM objects as it has acquired previously.
	 */
	@Test
	public void testBug567422_3_evilDropTarget() throws InterruptedException {
		testBug567422(TestMode.EVIL);
	}

	public void testBug567422(TestMode mode) throws InterruptedException {
		// More DND operations increase the test time but also the chance to trigger a
		// crash.
		int dragDropCount = 5;
		// Decrease to potential speed up test or increase for a little higher chance to
		// trigger crash if bug exist.
		// Must be at least 2. This number of DND operations are performed before there
		// is a chance to find a crash.
		int testBatchSize = 5;
		String data = "Test data";

		Shell shell = new Shell();
		shell.setLayout(new RowLayout());
		shell.setSize(300, 300);
		shell.open();
		try {
			try {
				SwtWin32TestUtil.processEvents(shell.getDisplay(), 1000, shell::isVisible);
			} catch (InterruptedException e) {
				fail("Initialization interrupted");
			}
			assertTrue("Shell not visible.", shell.isVisible());

			Transfer transfer = TextTransfer.getInstance();
			AtomicBoolean dropped = new AtomicBoolean(false);
			AtomicReference<String> droppedData = new AtomicReference<>(null);
			List<IDataObject> dataObjects = new ArrayList<>();

			DragSource source = new DragSource(shell, DND.DROP_LINK | DND.DROP_COPY | DND.DROP_MOVE);
			source.setTransfer(transfer);
			source.addListener(DND.DragSetData, event -> event.data = data);

			DropTarget target = new DropTarget(shell, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_LINK | DND.DROP_MOVE);
			target.setTransfer(transfer);
			target.addDropListener(new DropTargetListener() {
				@Override
				public void drop(DropTargetEvent event) {
					try {
						String o = (String) event.data;
						droppedData.set(o);
					} catch (ClassCastException ex) {
					}
					dropped.set(true);

					if (mode == TestMode.UNCOMMON) {
						// keep reference on data object and release it eventually later
						IDataObject dataObject = new IDataObject(event.currentDataType.pIDataObject);
						dataObject.AddRef();
						dataObjects.add(dataObject);
					}
					if (mode == TestMode.EVIL) {
						// release more references on data object as acquired
						IDataObject dataObject = new IDataObject(event.currentDataType.pIDataObject);
						for (int i = 0; i < 3; i++) {
							dataObject.Release();
						}
					}
				}

				@Override
				public void dropAccept(DropTargetEvent event) {
				}

				@Override
				public void dragOver(DropTargetEvent event) {
				}

				@Override
				public void dragOperationChanged(DropTargetEvent event) {
				}

				@Override
				public void dragLeave(DropTargetEvent event) {
				}

				@Override
				public void dragEnter(DropTargetEvent event) {
				}
			});

			for (int i = 0; i < dragDropCount; i++) {
				dropped.set(false);
				droppedData.set(null);
				int dragDropTries = 3;
				do {
					shell.setText(i + "/" + dragDropCount);
					postDragAndDropEvents(shell);
					SwtWin32TestUtil.processEvents(shell.getDisplay(), 1000, dropped::get);
				} while (!dropped.get() && --dragDropTries > 0);
				assertTrue("No drop received.", dropped.get());
				assertNotNull("No data was dropped.", droppedData.get());

				if (mode == TestMode.UNCOMMON && (i % testBatchSize) == 0) {
					for (IDataObject dataObject : dataObjects) {
						dataObject.Release();
					}
					dataObjects.clear();
				}
			}
			if (mode == TestMode.UNCOMMON) {
				for (IDataObject dataObject : dataObjects) {
					dataObject.Release();
				}
			}
		} finally {
			Display display = shell.getDisplay();
			display.dispose();
			assertTrue(display.isDisposed());
		}
	}

	/**
	 * Posts the events required to do a drag and drop. (i.e. click and hold mouse
	 * button and move mouse)
	 * <p>
	 * The caller is responsible to ensure the generated events are processed by the
	 * event loop.
	 * </p>
	 */
	private static void postDragAndDropEvents(Shell shell) {
		shell.forceActive();
		assertTrue("Test shell requires input focus.", shell.forceFocus());
		Event event = new Event();
		Point pt = shell.toDisplay(50, 50);
		event.x = pt.x;
		event.y = pt.y;
		event.type = SWT.MouseMove;
		shell.getDisplay().post(event);
		event.button = 1;
		event.count = 1;
		event.type = SWT.MouseDown;
		shell.getDisplay().post(event);
		event.x += 30;
		event.type = SWT.MouseMove;
		shell.getDisplay().post(event);
		event.type = SWT.MouseUp;
		shell.getDisplay().post(event);
	}
}
