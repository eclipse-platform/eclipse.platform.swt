/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.paint;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Manages a simple drawing surface.
 */
public class PaintSurface {
	private Point currentPosition = new Point(0, 0);
	private Canvas paintCanvas;

	private PaintSession paintSession;
	private Image image;
	private Image paintImage; // buffer for refresh blits
	private int   imageWidth, imageHeight;
	private int   visibleWidth, visibleHeight;

	private FigureDrawContext displayFDC = new FigureDrawContext();
	private FigureDrawContext imageFDC = new FigureDrawContext();
	private FigureDrawContext paintFDC = new FigureDrawContext();

	/* Rubberband */
	private ContainerFigure rubberband = new ContainerFigure();
		// the active rubberband selection
	private int rubberbandHiddenNestingCount = 0;
		// always >= 0, if > 0 rubberband has been hidden

	/* Status */
	private Text statusText;
	private String statusActionInfo, statusMessageInfo, statusCoordInfo;

	/**
	 * Constructs a PaintSurface.
	 * <p>
	 * paintCanvas must have SWT.NO_REDRAW_RESIZE and SWT.NO_BACKGROUND styles,
	 *     and may have SWT.V_SCROLL and/or SWT.H_SCROLL.
	 * </p>
	 * @param paintCanvas the Canvas object in which to render
	 * @param paintStatus the PaintStatus object to use for providing user feedback
	 * @param fillColor the color to fill the canvas with initially
	 */
	public PaintSurface(Canvas paintCanvas, Text statusText, Color fillColor) {
		this.paintCanvas = paintCanvas;
		this.statusText = statusText;
		clearStatus();

		/* Set up the drawing surface */
		Rectangle displayRect = paintCanvas.getDisplay().getClientArea();
		imageWidth = displayRect.width;
		imageHeight = displayRect.height;
		image = new Image(paintCanvas.getDisplay(), imageWidth, imageHeight);

		imageFDC.gc = new GC(image);
		imageFDC.gc.setBackground(fillColor);
		imageFDC.gc.fillRectangle(0, 0, imageWidth, imageHeight);
		displayFDC.gc = new GC(paintCanvas);

		/* Initialize the session */
		setPaintSession(null);

		/* Add our listeners */
		paintCanvas.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				displayFDC.gc.dispose();
			}			
		});
		paintCanvas.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent event) {
				processMouseEventCoordinates(event);
				if (paintSession != null) paintSession.mouseDown(event);
			}
			public void mouseUp(MouseEvent event) {
				processMouseEventCoordinates(event);
				if (paintSession != null) paintSession.mouseUp(event);
			}
			public void mouseDoubleClick(MouseEvent event) {
				processMouseEventCoordinates(event);
				if (paintSession != null) paintSession.mouseDoubleClick(event);
			}			
		});
		paintCanvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent event) {
				processMouseEventCoordinates(event);
				if (paintSession != null) paintSession.mouseMove(event);
			}
		});
		paintCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				if (rubberband.isEmpty()) {
					// Nothing to merge, so we just refresh
					event.gc.drawImage(image,
						displayFDC.xOffset + event.x, displayFDC.yOffset + event.y, event.width, event.height,
						event.x, event.y, event.width, event.height);
				} else {
					/*
					 * Avoid flicker when merging overlayed objects by constructing the image on
					 * a backbuffer first, then blitting it to the screen.
					 */
					// Check that the backbuffer is large enough
					if (paintImage != null) {
						Rectangle rect = paintImage.getBounds();
						if ((event.width + event.x > rect.width) ||
							(event.height + event.y > rect.height)) {
							paintFDC.gc.dispose();
							paintImage.dispose();
							paintImage = null;
						}
					}
					if (paintImage == null) {
						Display display = getDisplay();
						Rectangle rect = display.getClientArea();
						paintImage = new Image(display,
							Math.max(rect.width, event.width + event.x),
							Math.max(rect.height, event.height + event.y));
						paintFDC.gc = new GC(paintImage);
					}
					// Setup clipping and the FDC
					Region clipRegion = new Region();
					event.gc.getClipping(clipRegion);					
					paintFDC.gc.setClipping(clipRegion);
					clipRegion.dispose();

					paintFDC.xOffset = displayFDC.xOffset;
					paintFDC.yOffset = displayFDC.yOffset;
					paintFDC.xScale = displayFDC.xScale;
					paintFDC.yScale = displayFDC.yScale;
					
					// Merge the overlayed objects into the image, then blit
					paintFDC.gc.drawImage(image,
						displayFDC.xOffset + event.x, displayFDC.yOffset + event.y, event.width, event.height,
						event.x, event.y, event.width, event.height);
					rubberband.draw(paintFDC);
					event.gc.drawImage(paintImage,
						event.x, event.y, event.width, event.height,
						event.x, event.y, event.width, event.height);
				}
			}
		});
		paintCanvas.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent event) {
				handleResize();
			}			
		});

		/* Set up the paint canvas scroll bars */
		ScrollBar horizontal = paintCanvas.getHorizontalBar();
		horizontal.setVisible(true);
		horizontal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollHorizontally((ScrollBar)event.widget);
			}
		});
		ScrollBar vertical = paintCanvas.getVerticalBar();
		vertical.setVisible(true);
		vertical.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollVertically((ScrollBar)event.widget);
			}
		});
		handleResize();
	}
	
	/**
	 * Disposes of the PaintSurface's resources.
	 */
	public void dispose() {
		imageFDC.gc.dispose();
		image.dispose();
		if (paintImage != null) {
			paintImage.dispose();
			paintFDC.gc.dispose();
		}

		currentPosition = null;
		paintCanvas = null;
		paintSession = null;
		image = null;
		paintImage = null;
		displayFDC = null;
		imageFDC = null;
		paintFDC = null;
		rubberband = null;
		statusText = null;
		statusActionInfo = null;
		statusMessageInfo = null;
		statusCoordInfo = null;
	}

	/**
	 * Called when we must grab focus.
	 */
	public void setFocus()  {
		paintCanvas.setFocus();
	}

	/**
	 * Returns the Display on which the PaintSurface resides.
	 * @return the Display
	 */
	public Display getDisplay() {
		return paintCanvas.getDisplay();
	}

	/**
	 * Returns the Shell in which the PaintSurface resides.
	 * @return the Shell
	 */
	public Shell getShell() {
		return paintCanvas.getShell();
	}

	/**
	 * Sets the current paint session.
	 * <p>
	 * If oldPaintSession != paintSession calls oldPaintSession.end()
	 * and paintSession.begin()
	 * </p>
	 * 
	 * @param paintSession the paint session to activate; null to disable all sessions
	 */
	public void setPaintSession(PaintSession paintSession) {
		if (this.paintSession != null) {
			if (this.paintSession == paintSession) return;
			this.paintSession.endSession();
		}
		this.paintSession = paintSession;
		clearStatus();
		if (paintSession != null) {
			setStatusAction(paintSession.getDisplayName());
			paintSession.beginSession();
		} else {
			setStatusAction(PaintExample.getResourceString("tool.Null.label"));
			setStatusMessage(PaintExample.getResourceString("session.Null.message"));
		}
	}

	/**
	 * Returns the current paint session.
	 * 
	 * @return the current paint session, null if none is active
	 */
	public PaintSession getPaintSession() {
		return paintSession;
	}

	/**
	 * Returns the current paint tool.
	 * 
	 * @return the current paint tool, null if none is active (though some other session
	 *         might be)
	 */
	public PaintTool getPaintTool() {
		return (paintSession != null && paintSession instanceof PaintTool) ?
			(PaintTool)paintSession : null;
	}

	/**
	 * Returns the current position in an interactive operation.
	 *
	 * @return the last known position of the pointer
	 */
	public Point getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * Draws a Figure object to the screen and to the backing store permanently.
	 * 
	 * @param object the object to draw onscreen
	 */
	public void drawFigure(Figure object) {
		object.draw(imageFDC);
		object.draw(displayFDC);
	}

	/**
	 * Adds a Figure object to the active rubberband selection.
	 * <p>
	 * This object will be drawn to the screen as a preview and refreshed appropriately
	 * until the selection is either cleared or committed.
	 * </p>
	 * 
	 * @param object the object to add to the selection
	 */
	public void addRubberbandSelection(Figure object) {
		rubberband.add(object);
		if (! isRubberbandHidden()) object.draw(displayFDC);
	}

	/**
	 * Clears the active rubberband selection.
	 * <p>
	 * Erases any rubberband objects on the screen then clears the selection.
	 * </p>
	 */
	public void clearRubberbandSelection() {
		if (! isRubberbandHidden()) {
			Region region = new Region();
			rubberband.addDamagedRegion(displayFDC, region);
			Rectangle r = region.getBounds();
			paintCanvas.redraw(r.x, r.y, r.width, r.height, true);
			region.dispose();
		}
		rubberband.clear();

	}

	/**
	 * Commits the active rubberband selection.
	 * <p>
	 * Redraws any rubberband objects on the screen as permanent objects then clears the selection.
	 * </p>
	 */
	public void commitRubberbandSelection() {
		rubberband.draw(imageFDC);
		if (isRubberbandHidden()) rubberband.draw(displayFDC);
		rubberband.clear();
	}
	
	/**
	 * Hides the rubberband (but does not eliminate it).
	 * <p>
	 * Increments by one the rubberband "hide" nesting count.  The rubberband
	 * is hidden from view (but remains active) if it wasn't already hidden.
	 * </p>
	 */
	public void hideRubberband() {
		if (rubberbandHiddenNestingCount++ <= 0) {
			Region region = new Region();
			rubberband.addDamagedRegion(displayFDC, region);
			Rectangle r = region.getBounds();
			paintCanvas.redraw(r.x, r.y, r.width, r.height, true);
			region.dispose();
		}
	}		

	/**
	 * Shows (un-hides) the rubberband.
	 * <p>
	 * Decrements by one the rubberband "hide" nesting count.  The rubberband
	 * is only made visible when showRubberband() has been called once for each
	 * previous hideRubberband().  It is not permitted to call showRubberband() if
	 * the rubber band is not presently hidden.
	 * </p>
	 */
	public void showRubberband() {
		if (rubberbandHiddenNestingCount <= 0)
			throw new IllegalStateException("rubberbandHiddenNestingCount > 0");
		if (--rubberbandHiddenNestingCount == 0) {
			rubberband.draw(displayFDC);
		}
	}
	
	/**
	 * Determines if the rubberband is hidden.
	 * 
	 * @return true iff the rubber is hidden
	 */
	public boolean isRubberbandHidden() {
		return rubberbandHiddenNestingCount > 0;
	}

	/**
	 * Handles a horizontal scroll event
	 * 
	 * @param scrollbar the horizontal scroll bar that posted this event
	 */
	public void scrollHorizontally(ScrollBar scrollBar) {
		if (image == null) return;
		if (imageWidth > visibleWidth) {
			final int oldOffset = displayFDC.xOffset;
			final int newOffset = Math.min(scrollBar.getSelection(), imageWidth - visibleWidth);
			if (oldOffset != newOffset) {
				paintCanvas.update();
				displayFDC.xOffset = newOffset;
				paintCanvas.scroll(Math.max(oldOffset - newOffset, 0), 0, Math.max(newOffset - oldOffset, 0), 0,
					visibleWidth, visibleHeight, false);
			}
		}
	}

	/**
	 * Handles a vertical scroll event
	 * 
	 * @param scrollbar the vertical scroll bar that posted this event
	 */
	public void scrollVertically(ScrollBar scrollBar) {
		if (image == null) return;
		if (imageHeight > visibleHeight) {
			final int oldOffset = displayFDC.yOffset;
			final int newOffset = Math.min(scrollBar.getSelection(), imageHeight - visibleHeight);
			if (oldOffset != newOffset) {
				paintCanvas.update();
				displayFDC.yOffset = newOffset;
				paintCanvas.scroll(0, Math.max(oldOffset - newOffset, 0), 0, Math.max(newOffset - oldOffset, 0),
					visibleWidth, visibleHeight, false);
			}
		}
	}
	
	/**
	 * Handles resize events
	 */
	private void handleResize() {
		paintCanvas.update();

		Rectangle visibleRect = paintCanvas.getClientArea();
		visibleWidth = visibleRect.width;
		visibleHeight = visibleRect.height;

		ScrollBar horizontal = paintCanvas.getHorizontalBar();
		if (horizontal != null) {
			displayFDC.xOffset = Math.min(horizontal.getSelection(), imageWidth - visibleWidth);
			if (imageWidth <= visibleWidth) {
				horizontal.setEnabled(false);
				horizontal.setSelection(0);
			} else {
				horizontal.setEnabled(true);
				horizontal.setValues(displayFDC.xOffset, 0, imageWidth, visibleWidth,
					8, visibleWidth);
			}
		}

		ScrollBar vertical = paintCanvas.getVerticalBar();
		if (vertical != null) {
			displayFDC.yOffset = Math.min(vertical.getSelection(), imageHeight - visibleHeight);
			if (imageHeight <= visibleHeight) {
				vertical.setEnabled(false);
				vertical.setSelection(0);
			} else {
				vertical.setEnabled(true);
				vertical.setValues(displayFDC.yOffset, 0, imageHeight, visibleHeight,
					8, visibleHeight);
			}
		}
	}

	/**
	 * Virtualizes MouseEvent coordinates and stores the current position.
	 */
	private void processMouseEventCoordinates(MouseEvent event) {
		currentPosition.x = event.x =
			Math.min(Math.max(event.x, 0), visibleWidth - 1) + displayFDC.xOffset;
		currentPosition.y = event.y =
			Math.min(Math.max(event.y, 0), visibleHeight - 1) + displayFDC.yOffset;
	}
	
	/**
	 * Clears the status bar.
	 */
	public void clearStatus() {
		statusActionInfo = "";
		statusMessageInfo = "";
		statusCoordInfo = "";
		updateStatus();
	}

	/**
	 * Sets the status bar action text.
	 *
	 * @param action the action in progress, null to clear
	 */
	public void setStatusAction(String action) {
		statusActionInfo = (action != null) ? action : "";
		updateStatus();
	}
	
	/**
	 * Sets the status bar message text.
	 * 
	 * @param message the message to display, null to clear
	 */
	public void setStatusMessage(String message) {
		statusMessageInfo = (message != null) ? message : "";
		updateStatus();
	}

	/**
	 * Sets the coordinates in the status bar.
	 * 
	 * @param coord the coordinates to display, null to clear
	 */
	public void setStatusCoord(Point coord) {
		statusCoordInfo = (coord != null) ? PaintExample.getResourceString("status.Coord.format", new Object[]
			{ new Integer(coord.x), new Integer(coord.y)}) : "";
		updateStatus();
	}

	/**
	 * Sets the coordinate range in the status bar.
	 * 
	 * @param a the "from" coordinate, must not be null
	 * @param b the "to" coordinate, must not be null
	 */
	public void setStatusCoordRange(Point a, Point b) {
		statusCoordInfo = PaintExample.getResourceString("status.CoordRange.format", new Object[]
			{ new Integer(a.x), new Integer(a.y), new Integer(b.x), new Integer(b.y)});
		updateStatus();
	}

	/**
	 * Updates the display.
	 */
	private void updateStatus() {
		statusText.setText(
			PaintExample.getResourceString("status.Bar.format", new Object[]
			{ statusActionInfo, statusMessageInfo, statusCoordInfo }));
	}
}
