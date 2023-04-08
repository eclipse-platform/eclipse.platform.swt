/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CTabFolder
 *
 * @see org.eclipse.swt.custom.CTabFolder
 */
public class Test_org_eclipse_swt_custom_CTabFolder extends Test_org_eclipse_swt_widgets_Composite {

@Override
@Before
public void setUp() {
	super.setUp();
	shell.setLayout(new FillLayout());
	makeCleanEnvironment();
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
}

@Override
@Test
public void test_computeSizeIIZ() {
}

@Override
@Test
public void test_computeTrimIIII() {
}

@Override
@Test
public void test_getClientArea() {
}

@Override
@Test
public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
}

@Override
@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
}

/* custom */
protected CTabFolder ctabFolder;

/**
 * Dispose all widgets in shell and create a new empty {@link CTabFolder}.
 */
private void makeCleanEnvironment() {
	makeCleanEnvironment(SWT.NONE);
}

/**
 * Dispose all widgets in shell and create a new empty {@link CTabFolder}.
 *
 * @param style style bits for the {@link CTabFolder}
 */
private void makeCleanEnvironment(int style) {
	Control[] children = shell.getChildren();
	for (Control child : children) {
		child.dispose();
	}
	ctabFolder = new CTabFolder(shell, style);
	setWidget(ctabFolder);
}

private void createTabFolder(List<String> events) {
	createTabFolder(events, 3);
}

private void createTabFolder(List<String> events, int numItems) {
	makeCleanEnvironment();
	for (int i = 0; i < numItems; i++) {
		CTabItem item = new CTabItem(ctabFolder, SWT.NONE);
		item.setText("CTabItem &" + i);
		item.setToolTipText("CTabItem ToolTip" + i);
		Text itemText = new Text(ctabFolder, SWT.MULTI | SWT.BORDER);
		itemText.setText("\nText for CTabItem " + i + "\n\n\n");
		item.setControl(itemText);
		if (events != null) {
			hookExpectedEvents(item, getTestName(), events);
			hookExpectedEvents(itemText, getTestName(), events);
		}
	}
	ctabFolder.setSelection(ctabFolder.getItem(0));
}

@Test
public void test_ItemCountListeners() {
	int expectedTabsCount = 1;
	int expectedEventsCount = 0;
	createTabFolder(null, expectedTabsCount);
	AtomicLong itemCount = new AtomicLong();
	AtomicLong callCount = new AtomicLong();
	Consumer<CTabFolderEvent> tabCountUpdate = e -> {
		itemCount.set(ctabFolder.getItemCount());
		callCount.incrementAndGet();
	};
	ctabFolder.addCTabFolder2Listener(CTabFolder2Listener.itemsCountAdapter(tabCountUpdate));

	CTabItem item1 = new CTabItem(ctabFolder, SWT.NONE);
	expectedTabsCount ++;
	expectedEventsCount++;
	assertEquals(expectedTabsCount, ctabFolder.getItemCount());
	assertEquals(ctabFolder.getItemCount(), itemCount.get());
	assertEquals(expectedEventsCount, callCount.get());

	CTabItem item2 = new CTabItem(ctabFolder, SWT.NONE);
	expectedEventsCount++;
	expectedTabsCount ++;
	assertEquals(expectedTabsCount, ctabFolder.getItemCount());
	assertEquals(ctabFolder.getItemCount(), itemCount.get());
	assertEquals(expectedEventsCount, callCount.get());

	item1.dispose();
	expectedEventsCount++;
	expectedTabsCount --;
	assertEquals(expectedTabsCount, ctabFolder.getItemCount());
	assertEquals(ctabFolder.getItemCount(), itemCount.get());
	assertEquals(expectedEventsCount, callCount.get());

	item2.dispose();
	expectedEventsCount++;
	expectedTabsCount --;
	assertEquals(expectedTabsCount, ctabFolder.getItemCount());
	assertEquals(ctabFolder.getItemCount(), itemCount.get());
	assertEquals(expectedEventsCount, callCount.get());
}

@Test
public void test_consistency_KeySelection() {
	List<String> events = new ArrayList<>();
	createTabFolder(events);
	consistencyEvent(0, SWT.ARROW_RIGHT, 0, 0, ConsistencyUtility.KEY_PRESS, events, false);
}

@Test
public void test_consistency_MouseSelection() {
	List<String> events = new ArrayList<>();
	createTabFolder(events);
	consistencyPrePackShell();
	consistencyEvent(ctabFolder.getSize().x/2, 5, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_PgdwnSelection () {
	List<String> events = new ArrayList<>();
	createTabFolder(events);
	consistencyEvent(0, SWT.CTRL, 0, SWT.PAGE_DOWN, ConsistencyUtility.DOUBLE_KEY_PRESS, events, false);
}

@Test
public void test_consistency_PgupSelection () {
	List<String> events = new ArrayList<>();
	createTabFolder(events);
	ctabFolder.setSelection(2);
	consistencyEvent(0, SWT.CTRL, 0, SWT.PAGE_UP, ConsistencyUtility.DOUBLE_KEY_PRESS, events, false);
}

@Test
public void test_consistency_MenuDetect () {
	List<String> events = new ArrayList<>();
	createTabFolder(events);
	ctabFolder.setSelection(1);
	consistencyEvent(50, 5, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_DragDetect () {
	List<String> events = new ArrayList<>();
	createTabFolder(events);
	ctabFolder.setSelection(1);
	consistencyEvent(50, 5, 70, 10, ConsistencyUtility.MOUSE_DRAG, events);
}

@Test
public void test_setHighlightEnabled () {
	assertTrue("By default, highlighting should be enabled", ctabFolder.getHighlightEnabled());
	ctabFolder.setHighlightEnabled(false);
	assertFalse(ctabFolder.getHighlightEnabled());
	ctabFolder.setHighlightEnabled(true);
	assertTrue(ctabFolder.getHighlightEnabled());
}

@Ignore("Currently failing due to Bug 507611. E.g: Height is 50 instead of being at least 59")
@Test
public void test_checkSize() {
	shell.setLayout(new GridLayout(1, false));
	Text text2 = new Text(shell, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
	text2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	text2.setText("This control takes the initial focus.");

	Color red = shell.getDisplay().getSystemColor(SWT.COLOR_RED);
	Image systemImage = shell.getDisplay().getSystemImage(SWT.ICON_INFORMATION);

	CTabFolder folder = new CTabFolder(shell, SWT.BORDER);
	folder.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	folder.setSelectionBackground(red);
	for (int i = 0; i < 3; i++) {
		CTabItem item = new CTabItem(folder, SWT.CLOSE);
		item.setText("Item " + i);
		Text text = new Text(folder, SWT.MULTI);
		text.setText("Content for Item " + i);
		item.setImage(systemImage);
		item.setControl(text);
	}
	folder.setSelection(0);
	shell.pack();
	shell.open();
	int folderY = folder.getSize().y;
	int expectedminHeight = systemImage.getImageData().height + text2.getFont().getFontData()[0].getHeight();
	assertTrue("\nBug 507611 - CTabFolder is too thin for its actual content. \nCtabFolder height:"
								+folderY+"\nExpected min:"+expectedminHeight,  folderY > expectedminHeight);
}

/**
 * Test for bug 528251.
 *
 * We define two {@link CTabFolder tab folders}, of which one has a nested tab folder.
 * We validate that selecting the nested tab does not break selection highlight for the top-level tabs.
 *
 * @see org.eclipse.swt.tests.manual.Bug528251_CTabFolder_nested_highlighting
 */
@Test
public void test_nestedTabHighlighting () {
	CTabFolder partStackTabFolder = new CTabFolder(shell, SWT.NONE);

	CTabItem consoleViewTab = new CTabItem(partStackTabFolder, SWT.NONE);
	consoleViewTab.setText("Console View");

	SashForm anotherView = new SashForm(partStackTabFolder, SWT.NONE);
	CTabItem anotherViewTab = new CTabItem(partStackTabFolder, SWT.NONE);
	anotherViewTab.setText("Other View");
	anotherViewTab.setControl(anotherView);
	CTabFolder anotherViewNestedTabFolder = new CTabFolder(anotherView, SWT.NONE);
	CTabItem anotherViewNestedTab = new CTabItem(anotherViewNestedTabFolder, SWT.NONE);
	anotherViewNestedTab.setText("nested tab");

	shell.pack();
	shell.open();

	processEvents();

	// nothing is selected, expect no highlight
	boolean shouldHighlightConsoleViewTab = reflection_shouldHighlight(partStackTabFolder);
	assertFalse("expected CTabFolder to not need highlighting without any selection",
			shouldHighlightConsoleViewTab);

	// "click" on the Console View tab
	partStackTabFolder.notifyListeners(SWT.Activate, new Event());
	partStackTabFolder.setSelection(consoleViewTab);
	// "click" on the Other View tab, per default the first sub-tab is also highlighted
	partStackTabFolder.setSelection(anotherViewTab);
	anotherViewNestedTabFolder.notifyListeners(SWT.Activate, new Event());
	// "click" on the nested tab
	anotherViewNestedTabFolder.setSelection(anotherViewNestedTab);
	partStackTabFolder.setSelection(consoleViewTab);
	// "click" on the Console View tab, this hides and deactivates the nested CTabFolder
	anotherViewNestedTabFolder.notifyListeners(SWT.Deactivate, new Event());
	processEvents();

	// the Console View tab is selected, so it should still be highlighted
	shouldHighlightConsoleViewTab = reflection_shouldHighlight(partStackTabFolder);
	assertTrue("Bug 528251 - View tab not highlighted due to another view with a CTabFolder",
			shouldHighlightConsoleViewTab);
}

/** Test for Bug 559887: Chevron not updated on foreground color or font change. */
@Test
public void test_chevronAppearanceChanged() {
	Display display = shell.getDisplay();
	createTabFolder(null);
	shell.open();
	processEvents();
	ToolItem chevron = showChevron();

	Image oldChevronImg = new Image(display, chevron.getImage(), SWT.IMAGE_COPY);
	Font newFont = null;
	try {
		ctabFolder.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
		Image newChevronImg = chevron.getImage();
		ImageTestUtil.assertImagesNotEqual(oldChevronImg.getImageData(), newChevronImg.getImageData());

		oldChevronImg.dispose();
		oldChevronImg = new Image(display, newChevronImg, SWT.IMAGE_COPY);

		FontData[] existingFontData = ctabFolder.getFont().getFontData();
		existingFontData[0].setName(SwtTestUtil.testFontName);
		existingFontData[0].setStyle(SWT.BOLD | SWT.ITALIC);
		newFont = new Font(display, existingFontData);
		ctabFolder.setFont(newFont);
		newChevronImg = chevron.getImage();
		ImageTestUtil.assertImagesNotEqual(oldChevronImg.getImageData(), newChevronImg.getImageData());
	} finally {
		oldChevronImg.dispose();
		if (newFont != null) {
			newFont.dispose();
		}
	}
}

@Test
public void test_childControlOverlap() {
	BiConsumer<Control, Integer> setTopRightAndCheckOverlap = (control, style) -> {
		if (style == 0) {
			ctabFolder.setTopRight(control);
		} else {
			ctabFolder.setTopRight(control, style);
		}
		processEvents();
		checkElementOverlap(ctabFolder);
	};

	makeCleanEnvironment(SWT.CLOSE);
	shell.setLayout(new FillLayout());
	SwtTestUtil.openShell(shell);

	Label topRightControl = new Label(ctabFolder, SWT.BORDER);
	topRightControl.setText("TopRight");

	// Extra test for bug 384851. TopRight control set but no items.
	setTopRightAndCheckOverlap.accept(topRightControl, SWT.FILL);
	setTopRightAndCheckOverlap.accept(null, 0);

	for (int i = 1; i <= 5; i++) {
		CTabItem tabItem = new CTabItem(ctabFolder, SWT.NONE);
		tabItem.setText("Item" + i);
		Composite filler = new Composite(ctabFolder, SWT.NONE);
		filler.setBackground(filler.getDisplay().getSystemColor(SWT.COLOR_GREEN + i));
		tabItem.setControl(filler);
	}

	checkElementOverlap(ctabFolder);

	setTopRightAndCheckOverlap.accept(topRightControl, 0);
	setTopRightAndCheckOverlap.accept(topRightControl, SWT.RIGHT | SWT.WRAP);
	setTopRightAndCheckOverlap.accept(topRightControl, SWT.FILL);
	setTopRightAndCheckOverlap.accept(null, 0);

	ctabFolder.setMinimizeVisible(true);
	checkElementOverlap(ctabFolder);

	setTopRightAndCheckOverlap.accept(topRightControl, 0);
	setTopRightAndCheckOverlap.accept(topRightControl, SWT.RIGHT | SWT.WRAP);
	setTopRightAndCheckOverlap.accept(topRightControl, SWT.FILL);
	setTopRightAndCheckOverlap.accept(null, 0);

	ctabFolder.setMinimizeVisible(false);
	ctabFolder.setMaximizeVisible(true);
	checkElementOverlap(ctabFolder);

	setTopRightAndCheckOverlap.accept(topRightControl, 0);
	setTopRightAndCheckOverlap.accept(topRightControl, SWT.RIGHT | SWT.WRAP);
	setTopRightAndCheckOverlap.accept(topRightControl, SWT.FILL);
	setTopRightAndCheckOverlap.accept(null, 0);

	ctabFolder.setMinimizeVisible(true);
	checkElementOverlap(ctabFolder);

	setTopRightAndCheckOverlap.accept(topRightControl, 0);
	setTopRightAndCheckOverlap.accept(topRightControl, SWT.RIGHT | SWT.WRAP);
	setTopRightAndCheckOverlap.accept(topRightControl, SWT.FILL);
	setTopRightAndCheckOverlap.accept(null, 0);

	// some extra tests including chevron
	showChevron();
	checkElementOverlap(ctabFolder);
	ctabFolder.setMinimizeVisible(true);
	checkElementOverlap(ctabFolder);
	ctabFolder.setMaximizeVisible(true);
	checkElementOverlap(ctabFolder);
	setTopRightAndCheckOverlap.accept(topRightControl, 0);
	setTopRightAndCheckOverlap.accept(topRightControl, SWT.RIGHT | SWT.WRAP);
	setTopRightAndCheckOverlap.accept(topRightControl, SWT.FILL);
	setTopRightAndCheckOverlap.accept(null, 0);
}

/**
 * Min/max and chevron icon can appear below tab row.
 * Test for bug 499215, 533582.
 */
@Test
public void test_iconWrappedOnNextLine() {
	createTabFolder(null);

	FontData[] existingFontData = ctabFolder.getFont().getFontData();
	existingFontData[0].setName(SwtTestUtil.testFontName);
	existingFontData[0].setHeight(3);
	Font smallFont = new Font(ctabFolder.getDisplay(), existingFontData);
	try {
		SwtTestUtil.openShell(shell);
		ctabFolder.setFont(smallFont);

		ctabFolder.setMaximizeVisible(true);
		processEvents();
		assertTabElementsInLine();

		createTabFolder(null, 20);
		ctabFolder.setFont(smallFont);
		shell.layout(true, true);
		showChevron();
		processEvents();
		assertTabElementsInLine();
	} finally {
		smallFont.dispose();
	}
}

private void processEvents() {
	Display display = shell.getDisplay();

	while (display.readAndDispatch()) {
		//
	}
}

private static boolean reflection_shouldHighlight(CTabFolder partStackTabs) {
	String shouldHighlightMethodName = "shouldHighlight";
	Class<?> cTabFolderClass = CTabFolder.class;

	boolean shouldHighlightConsoleViewTab = false;
	try {
		Method method = cTabFolderClass.getDeclaredMethod(shouldHighlightMethodName);
		method.setAccessible(true);
		Object result = method.invoke(partStackTabs);
		Boolean shouldHighlight = (Boolean) result;
		shouldHighlightConsoleViewTab = shouldHighlight.booleanValue();
	} catch (Throwable t) {
		String message = "reflection call to " + cTabFolderClass.getName() + "." + shouldHighlightMethodName + "() failed";
		throw new AssertionError(message, t);
	}

	return shouldHighlightConsoleViewTab;
}

/**
 * Resize shell so that the ctabFolder must show the chevron.
 * <p>
 * Throws exception on failure or if chevron not found.
 * </p>
 *
 * @return the chevron widget
 */
private ToolItem showChevron() {
	int itemWidth = 0;
	for (CTabItem item : ctabFolder.getItems()) {
		itemWidth += item.getBounds().width;
	}
	// resize shell to force a chevron
	int newWidth = itemWidth*3/4;
	shell.setSize(newWidth, shell.getSize().y);
	boolean resizeFailed = Math.abs(newWidth - shell.getSize().x) > 10;
	ToolItem chevron = getChevron(ctabFolder);
	assertNotNull("Chevron not shown" + (resizeFailed
			? ". Shell could not be resized to the desired size. Tab row width might be smaller than the minimum shell width."
			: ""), chevron);
	return chevron;
}

/**
 * Find chevron item in CTabFolder. Does not use reflection but searches for a
 * control which seem to be the chevron based on button style and tooltip text.
 *
 * @param tabFolder CTabFolder to search in
 * @return the chevron item or <code>null</code> if not found or not shown
 */
private ToolItem getChevron(CTabFolder tabFolder) {
	for (Control child : tabFolder.getChildren()) {
		if (child instanceof ToolBar) {
			for (ToolItem toolItem : ((ToolBar)child).getItems()) {
				if ((toolItem.getStyle() & SWT.PUSH) != 0
						&& toolItem.getText().isEmpty()
						&& SWT.getMessage("SWT_ShowList").equals(toolItem.getToolTipText())
						&& toolItem.getImage() != null) {
					return toolItem;
				}
			}
		}
	}
	return null;
}

private void checkElementOverlap(CTabFolder tabFolder) {
	Rectangle folderBounds = tabFolder.getBounds();
	ArrayList<Widget> subControls = new ArrayList<>();
	subControls.addAll(Arrays.asList(reflection_getChildControls(tabFolder)));
	subControls.addAll(Arrays.asList(tabFolder.getItems()));
	for (int i = 0; i < subControls.size(); i++) {
		Rectangle boundsA = null;
		if (subControls.get(i) instanceof Control) {
			if (!((Control) subControls.get(i)).isVisible())
				continue;
			boundsA = ((Control) subControls.get(i)).getBounds();
		} else if (subControls.get(i) instanceof CTabItem) {
			if (!((CTabItem) subControls.get(i)).isShowing())
				continue;
			boundsA = ((CTabItem) subControls.get(i)).getBounds();
		}
		for (int j = i + 1; j < subControls.size(); j++) {
			Rectangle boundsB = null;
			if (subControls.get(j) instanceof Control) {
				if (!((Control) subControls.get(j)).isVisible())
					continue;
				boundsB = ((Control) subControls.get(j)).getBounds();
			} else if (subControls.get(j) instanceof CTabItem) {
				if (!((CTabItem) subControls.get(j)).isShowing())
					continue;
				boundsB = ((CTabItem) subControls.get(j)).getBounds();
			}
			assertFalse("Overlap between <" + subControls.get(i) + "> and <" + subControls.get(j) + ">\n" + boundsA
					+ " overlaps " + boundsB, boundsA.intersects(boundsB));
		}
		assertEquals("Element <" + subControls.get(i) + "> outside folder.", folderBounds.intersection(boundsA),
				boundsA);
	}
}

private static Control[] reflection_getChildControls(CTabFolder tabFolder) {
	String childControlArrayName = "controls";
	try {
		Field field = CTabFolder.class.getDeclaredField(childControlArrayName);
		field.setAccessible(true);
		return (Control[]) field.get(tabFolder);
	} catch (Exception e) {
		fail("Failed to access controls via reflections.");
		return null;
	}
}

/**
 * Check if all CTabItems and toolbar icons like min/max, chevron are all in one line and not wrapped.
 */
private void assertTabElementsInLine() {
	List<Rectangle> tabBarElementBounds = new ArrayList<>();
	Arrays.stream(ctabFolder.getItems()).filter(CTabItem::isShowing).map(this::getBoundsInShell).forEach(tabBarElementBounds::add);
	for (Control child : ctabFolder.getChildren()) {
		if (child instanceof ToolBar) {
			for (ToolItem toolItem : ((ToolBar)child).getItems()) {
				if (toolItem.getImage() != null) {
					tabBarElementBounds.add(getBoundsInShell(toolItem));
				}
			}
		}
	}
	Rectangle maxBound = tabBarElementBounds.get(0);
	for (Rectangle bound : tabBarElementBounds) {
		if (bound.height > maxBound.height) {
			assertTrue("Element at " + maxBound + " is not on line.",
					bound.y <= maxBound.y && (bound.y + bound.height) >= (maxBound.y + maxBound.height));
			maxBound = bound;
		} else {
			assertTrue("Element at " + bound + " is not on line.",
					bound.y >= maxBound.y && (bound.y + bound.height) <= (maxBound.y + maxBound.height));
		}
	}
}

private Rectangle getBoundsInShell(Widget control) {
	Control parent;
	Rectangle bounds;
	if (control instanceof Control) {
		parent = ((Control)control).getParent();
		bounds = ((Control)control).getBounds();
	} else if (control instanceof CTabItem) {
		parent = ((CTabItem)control).getParent();
		bounds = ((CTabItem)control).getBounds();
	} else if (control instanceof ToolItem) {
		parent = ((ToolItem)control).getParent();
		bounds = ((ToolItem)control).getBounds();
	} else {
		throw new UnsupportedOperationException("Widget must provide bounds and parent");
	}
	if (parent != null && !(parent instanceof Shell)) {
		Rectangle absParentBound = getBoundsInShell(parent);
		bounds.x += absParentBound.x;
		bounds.y += absParentBound.y;
	}
	return bounds;
}

}
