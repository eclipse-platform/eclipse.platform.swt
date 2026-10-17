package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Comprehensive test snippet for getClipping() and setClipping() methods
 * Tests both Rectangle and Path clipping functionality using M3GC
 */
public class SnippetClippingTest {

	public static void main(String[] args) {
		// Enable Skija rendering
		SWT.USE_M3 = true;
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Clipping Test - Rectangle and Path (M3GC)");
		shell.setLayout(new FillLayout());

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		
		// Tab 1: Rectangle Clipping Test
		createRectangleClippingTab(tabFolder);
		
		// Tab 2: Path Clipping Test  
		createPathClippingTab(tabFolder);
		
		// Tab 3: Combined Clipping Test
		createCombinedClippingTab(tabFolder);

		shell.setSize(800, 600);
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void createRectangleClippingTab(TabFolder parent) {
		TabItem tabItem = new TabItem(parent, SWT.NONE);
		tabItem.setText("Rectangle Clipping");
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		tabItem.setControl(composite);
		
		Canvas canvas = new Canvas(composite, SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 400;
		gd.heightHint = 300;
		canvas.setLayoutData(gd);
		
		Text logText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		logText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Drawing.drawWithGC(canvas, e.gc, gc -> testRectangleClipping(gc, logText));
			}
		});
	}
	
	private static void createPathClippingTab(TabFolder parent) {
		TabItem tabItem = new TabItem(parent, SWT.NONE);
		tabItem.setText("Path Clipping");
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		tabItem.setControl(composite);
		
		Canvas canvas = new Canvas(composite, SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 400;
		gd.heightHint = 300;
		canvas.setLayoutData(gd);
		
		Text logText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		logText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Drawing.drawWithGC(canvas, e.gc, gc -> testPathClipping(gc, logText));
			}
		});
	}
	
	private static void createCombinedClippingTab(TabFolder parent) {
		TabItem tabItem = new TabItem(parent, SWT.NONE);
		tabItem.setText("Combined Test");
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		tabItem.setControl(composite);
		
		Canvas canvas = new Canvas(composite, SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 400;
		gd.heightHint = 300;
		canvas.setLayoutData(gd);
		
		Text logText = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		logText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Drawing.drawWithGC(canvas, e.gc, gc -> testCombinedClipping(gc, logText));
			}
		});
	}

	private static void testRectangleClipping(GC gc, Text logText) {
		StringBuilder log = new StringBuilder();
		log.append("=== Rectangle Clipping Test ===\n");
		
		// Get initial clipping bounds
		Rectangle initialClipping = gc.getClipping();
		log.append("Initial clipping: ").append(rectangleToString(initialClipping)).append("\n");
		
		// Fill background
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_GRAY));
		gc.fillRectangle(initialClipping);
		
		// Draw grid lines for reference
		drawReferenceGrid(gc, initialClipping);
		
		// Test 1: Set rectangle clipping
		Rectangle clipRect = new Rectangle(50, 50, 200, 150);
		
		// Draw expected clip bound outline BEFORE setting clipping
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_GREEN));
		gc.setLineWidth(3);
		gc.setLineStyle(SWT.LINE_DASH);
		gc.drawRectangle(clipRect);
		gc.drawText("Expected Clip 1", clipRect.x + 5, clipRect.y - 20, true);
		
		gc.setClipping(clipRect);
		
		Rectangle currentClipping = gc.getClipping();
		log.append("After setClipping(Rectangle): ").append(rectangleToString(currentClipping)).append("\n");
		log.append("Expected: ").append(rectangleToString(clipRect)).append("\n");
		log.append("Match: ").append(rectanglesEqual(currentClipping, clipRect)).append("\n\n");
		
		// Draw something in the clipped area
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_RED));
		gc.fillRectangle(0, 0, 400, 300);
		
		// Test 2: Set different rectangle clipping
		Rectangle clipRect2 = new Rectangle(100, 100, 150, 100);
		
		// Clear clipping temporarily to draw expected bounds
		gc.setClipping((Rectangle) null);
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_BLUE));
		gc.setLineWidth(3);
		gc.setLineStyle(SWT.LINE_DOT);
		gc.drawRectangle(clipRect2);
		gc.drawText("Expected Clip 2", clipRect2.x + 5, clipRect2.y - 20, true);
		
		gc.setClipping(clipRect2);
		
		currentClipping = gc.getClipping();
		log.append("After setClipping(Rectangle2): ").append(rectangleToString(currentClipping)).append("\n");
		log.append("Expected: ").append(rectangleToString(clipRect2)).append("\n");
		log.append("Match: ").append(rectanglesEqual(currentClipping, clipRect2)).append("\n\n");
		
		// Draw something in the new clipped area
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLUE));
		gc.fillRectangle(0, 0, 400, 300);
		
		// Test 3: Clear clipping
		gc.setClipping((Rectangle) null);
		
		currentClipping = gc.getClipping();
		log.append("After setClipping(null): ").append(rectangleToString(currentClipping)).append("\n");
		log.append("Expected: ").append(rectangleToString(initialClipping)).append("\n");
		log.append("Match: ").append(rectanglesEqual(currentClipping, initialClipping)).append("\n\n");
		
		// Draw final borders around the clipped areas to show actual vs expected
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.setLineWidth(2);
		gc.setLineStyle(SWT.LINE_SOLID);
		gc.drawRectangle(clipRect);
		gc.drawRectangle(clipRect2);
		
		// Add legend
		drawLegend(gc, 10, 250);
		
		// Update log
		logText.setText(log.toString());
	}

	private static void testPathClipping(GC gc, Text logText) {
		StringBuilder log = new StringBuilder();
		log.append("=== Path Clipping Test ===\n");
		
		// Get initial clipping bounds
		Rectangle initialClipping = gc.getClipping();
		log.append("Initial clipping: ").append(rectangleToString(initialClipping)).append("\n");
		
		// Fill background
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_GRAY));
		gc.fillRectangle(initialClipping);
		
		// Draw grid lines for reference
		drawReferenceGrid(gc, initialClipping);
		
		// Create a circular path
		Path circularPath = new Path(gc.getDevice());
		circularPath.addArc(100, 50, 150, 150, 0, 360);
		
		// Draw expected circular clip outline BEFORE setting clipping
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_GREEN));
		gc.setLineWidth(3);
		gc.setLineStyle(SWT.LINE_DASH);
		gc.drawPath(circularPath);
		gc.drawText("Expected Circle Clip", 105, 30, true);
		
		// Test 1: Set path clipping
		gc.setClipping(circularPath);
		
		Rectangle currentClipping = gc.getClipping();
		log.append("After setClipping(Circle Path): ").append(rectangleToString(currentClipping)).append("\n");
		log.append("Expected bounds approx: Rectangle {100, 50, 150, 150}\n");
		
		// Draw something in the clipped area
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_GREEN));
		gc.fillRectangle(0, 0, 400, 300);
		
		// Create a star-shaped path
		Path starPath = new Path(gc.getDevice());
		createStarPath(starPath, 200, 150, 60, 30, 5);
		
		// Clear clipping temporarily to draw expected star outline
		gc.setClipping((Path) null);
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_BLUE));
		gc.setLineWidth(3);
		gc.setLineStyle(SWT.LINE_DOT);
		gc.drawPath(starPath);
		gc.drawText("Expected Star Clip", 205, 120, true);
		
		// Test 2: Set different path clipping
		gc.setClipping(starPath);
		
		currentClipping = gc.getClipping();
		log.append("After setClipping(Star Path): ").append(rectangleToString(currentClipping)).append("\n");
		
		// Draw something in the new clipped area
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_YELLOW));
		gc.fillRectangle(0, 0, 400, 300);
		
		// Test 3: Clear clipping
		gc.setClipping((Path) null);
		
		currentClipping = gc.getClipping();
		log.append("After setClipping(null): ").append(rectangleToString(currentClipping)).append("\n");
		log.append("Expected: ").append(rectangleToString(initialClipping)).append("\n");
		log.append("Match: ").append(rectanglesEqual(currentClipping, initialClipping)).append("\n\n");
		
		// Draw final outlines to show the paths with actual clip bounds
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.setLineWidth(2);
		gc.setLineStyle(SWT.LINE_SOLID);
		gc.drawPath(circularPath);
		gc.drawPath(starPath);
		
		// Draw bounding rectangles for path clips
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_RED));
		gc.setLineWidth(1);
		gc.setLineStyle(SWT.LINE_DASHDOT);
		Rectangle circleBounds = new Rectangle(100, 50, 150, 150);
		gc.drawRectangle(circleBounds);
		gc.drawText("Circle Bounds", circleBounds.x + 5, circleBounds.y + circleBounds.height + 5, true);
		
		// Add legend for path clipping
		drawPathLegend(gc, 10, 220);
		
		// Cleanup
		circularPath.dispose();
		starPath.dispose();
		
		// Update log
		logText.setText(log.toString());
	}

	private static void testCombinedClipping(GC gc, Text logText) {
		StringBuilder log = new StringBuilder();
		log.append("=== Combined Clipping Test ===\n");
		
		// Get initial clipping bounds
		Rectangle initialClipping = gc.getClipping();
		log.append("Initial clipping: ").append(rectangleToString(initialClipping)).append("\n");
		
		// Fill background
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(initialClipping);
		
		// Draw grid lines for reference
		drawReferenceGrid(gc, initialClipping);
		
		// Test sequence: Rectangle -> Path -> Rectangle -> null
		
		// Step 1: Rectangle clipping
		Rectangle rect1 = new Rectangle(50, 50, 300, 200);
		
		// Draw expected bounds for step 1
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_RED));
		gc.setLineWidth(3);
		gc.setLineStyle(SWT.LINE_DASH);
		gc.drawRectangle(rect1);
		gc.drawText("Step 1: Rectangle", rect1.x + 5, rect1.y - 20, true);
		
		gc.setClipping(rect1);
		Rectangle clip1 = gc.getClipping();
		log.append("Step 1 - Rectangle: ").append(rectangleToString(clip1)).append("\n");
		
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_RED));
		gc.fillRectangle(0, 0, 400, 300);
		
		// Step 2: Path clipping
		Path ellipsePath = new Path(gc.getDevice());
		ellipsePath.addArc(100, 75, 200, 150, 0, 360);
		
		// Clear clipping temporarily to draw expected ellipse outline
		gc.setClipping((Rectangle) null);
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_GREEN));
		gc.setLineWidth(3);
		gc.setLineStyle(SWT.LINE_DOT);
		gc.drawPath(ellipsePath);
		gc.drawText("Step 2: Ellipse", 105, 55, true);
		
		gc.setClipping(ellipsePath);
		Rectangle clip2 = gc.getClipping();
		log.append("Step 2 - Ellipse Path: ").append(rectangleToString(clip2)).append("\n");
		
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_GREEN));
		gc.fillRectangle(0, 0, 400, 300);
		
		// Step 3: Different rectangle clipping
		Rectangle rect2 = new Rectangle(150, 100, 100, 100);
		
		// Clear clipping temporarily to draw expected bounds for step 3
		gc.setClipping((Path) null);
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_BLUE));
		gc.setLineWidth(3);
		gc.setLineStyle(SWT.LINE_DASHDOT);
		gc.drawRectangle(rect2);
		gc.drawText("Step 3: Small Rect", rect2.x + 5, rect2.y - 20, true);
		
		gc.setClipping(rect2);
		Rectangle clip3 = gc.getClipping();
		log.append("Step 3 - Small Rectangle: ").append(rectangleToString(clip3)).append("\n");
		
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLUE));
		gc.fillRectangle(0, 0, 400, 300);
		
		// Step 4: Clear clipping
		gc.setClipping((Rectangle) null);
		Rectangle clip4 = gc.getClipping();
		log.append("Step 4 - Cleared: ").append(rectangleToString(clip4)).append("\n");
		log.append("Matches initial: ").append(rectanglesEqual(clip4, initialClipping)).append("\n");
		
		// Draw final outlines with different styles to show the sequence
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.setLineWidth(2);
		gc.setLineStyle(SWT.LINE_SOLID);
		gc.drawRectangle(rect1);
		gc.drawPath(ellipsePath);
		gc.drawRectangle(rect2);
		
		// Add combined test legend
		drawCombinedLegend(gc, 10, 260);
		
		// Cleanup
		ellipsePath.dispose();
		
		// Update log
		logText.setText(log.toString());
	}

	private static void createStarPath(Path path, float centerX, float centerY, float outerRadius, float innerRadius, int points) {
		double angleStep = Math.PI / points;
		boolean outer = true;
		
		for (int i = 0; i <= points * 2; i++) {
			double angle = i * angleStep - Math.PI / 2; // Start from top
			float radius = outer ? outerRadius : innerRadius;
			float x = centerX + (float) (Math.cos(angle) * radius);
			float y = centerY + (float) (Math.sin(angle) * radius);
			
			if (i == 0) {
				path.moveTo(x, y);
			} else {
				path.lineTo(x, y);
			}
			outer = !outer;
		}
		path.close();
	}

	private static String rectangleToString(Rectangle rect) {
		if (rect == null) {
			return "null";
		}
		return String.format("Rectangle {%d, %d, %d, %d}", rect.x, rect.y, rect.width, rect.height);
	}

	private static boolean rectanglesEqual(Rectangle r1, Rectangle r2) {
		if (r1 == null && r2 == null) return true;
		if (r1 == null || r2 == null) return false;
		return r1.x == r2.x && r1.y == r2.y && r1.width == r2.width && r1.height == r2.height;
	}
	
	private static void drawReferenceGrid(GC gc, Rectangle bounds) {
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		gc.setLineStyle(SWT.LINE_DOT);
		
		// Vertical lines
		for (int x = bounds.x; x <= bounds.x + bounds.width; x += 10) {
			gc.drawLine(x, bounds.y, x, bounds.y + bounds.height);
		}
		// Horizontal lines
		for (int y = bounds.y; y <= bounds.y + bounds.height; y += 10) {
			gc.drawLine(bounds.x, y, bounds.x + bounds.width, y);
		}
	}
	
	private static void drawLegend(GC gc, int x, int y) {
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.setLineWidth(1);
		gc.setLineStyle(SWT.LINE_SOLID);
		
		gc.drawText("Legend:", x, y, true);
		gc.drawText("Dashed Green: Expected Clip 1", x, y + 15, true);
		gc.drawText("Dotted Blue: Expected Clip 2", x, y + 30, true);
		gc.drawText("Solid Black: Actual Clip Bounds", x, y + 45, true);
	}
	
	private static void drawPathLegend(GC gc, int x, int y) {
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.setLineWidth(1);
		gc.setLineStyle(SWT.LINE_SOLID);
		
		gc.drawText("Path Clipping Legend:", x, y, true);
		gc.drawText("Dashed Green: Expected Circle Clip", x, y + 15, true);
		gc.drawText("Dotted Blue: Expected Star Clip", x, y + 30, true);
		gc.drawText("Solid Black: Actual Clip Bounds", x, y + 45, true);
	}
	
	private static void drawCombinedLegend(GC gc, int x, int y) {
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.setLineWidth(1);
		gc.setLineStyle(SWT.LINE_SOLID);
		
		gc.drawText("Combined Clipping Test Legend:", x, y, true);
		gc.drawText("Dashed Red: Expected Clip Bounds (Step 1)", x, y + 15, true);
		gc.drawText("Dotted Green: Expected Clip Outline (Step 2)", x, y + 30, true);
		gc.drawText("Dash-Dot Blue: Expected Clip Bounds (Step 3)", x, y + 45, true);
		gc.drawText("Solid Black: Actual Clip Bounds", x, y + 60, true);
	}
}
