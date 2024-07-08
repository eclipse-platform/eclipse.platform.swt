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
package org.eclipse.swt.printing;


import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.RenderingHints.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.print.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.swing.*;

/**
 * Instances of this class are used to print to a printer.
 * Applications create a GC on a printer using <code>new GC(printer)</code>
 * and then draw on the printer GC using the usual graphics calls.
 * <p>
 * A <code>Printer</code> object may be constructed by providing
 * a <code>PrinterData</code> object which identifies the printer.
 * A <code>PrintDialog</code> presents a print dialog to the user
 * and returns an initialized instance of <code>PrinterData</code>.
 * Alternatively, calling <code>new Printer()</code> will construct a
 * printer object for the user's default printer.
 * </p><p>
 * Application code must explicitly invoke the <code>Printer.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see PrinterData
 * @see PrintDialog
 */
public final class Printer extends Device {
	/**
	 * the handle to the printer DC
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public PrinterJob handle;

	/**
	 * the printer data describing this printer
	 */
	PrinterData data;

	/**
	 * whether or not a GC was created for this printer
	 */
	boolean isGCCreated = false;

/**
 * Returns an array of <code>PrinterData</code> objects
 * representing all available printers.
 *
 * @return the list of available printers
 */
public static PrinterData[] getPrinterList() {
  PrintService[] printServices = PrinterJob.lookupPrintServices();
  PrinterData[] result = new PrinterData[printServices.length];
  for(int i=0; i<printServices.length; i++) {
    PrintService printService = printServices[i];
    result[i] = new PrinterData(printService.getClass().getName(), printService.getName());
  }
	return result;
}

/**
 * Returns a <code>PrinterData</code> object representing
 * the default printer or <code>null</code> if there is no
 * printer available on the System.
 *
 * @return the default printer data or null
 *
 * @since 2.1
 */
public static PrinterData getDefaultPrinterData() {
  PrintService printService = PrinterJob.getPrinterJob().getPrintService();
  if(printService == null) return null;
  return new PrinterData(printService.getClass().getName(), printService.getName());
}

static DeviceData checkNull (PrinterData data) {
	if (data == null) data = new PrinterData();
	if (data.driver == null || data.name == null) {
		PrinterData defaultPrinter = getDefaultPrinterData();
		if (defaultPrinter == null) SWT.error(SWT.ERROR_NO_HANDLES);
		data.driver = defaultPrinter.driver;
		data.name = defaultPrinter.name;
	}
	return data;
}

/**
 * Constructs a new printer representing the default printer.
 * <p>
 * You must dispose the printer when it is no longer required.
 * </p>
 *
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if there are no valid printers
 * </ul>
 *
 * @see Device#dispose
 */
public Printer() {
	this(null);
}

/**
 * Constructs a new printer given a <code>PrinterData</code>
 * object representing the desired printer.
 * <p>
 * You must dispose the printer when it is no longer required.
 * </p>
 *
 * @param data the printer data for the specified printer
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the specified printer data does not represent a valid printer
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if there are no valid printers
 * </ul>
 *
 * @see Device#dispose
 */
public Printer(PrinterData data) {
	super(checkNull(data));
  initPrinterData();
}

protected void initPrinterData() {
  if(referencePageFormat != null) {
    return;
  }
  dpiX = 72;
  dpiY = 72;
  HashPrintRequestAttributeSet hashPrintRequestAttributeSet = getHashPrintRequestAttributeSet();
  PageFormat pageFormat;
  if(Compatibility.IS_JAVA_6_OR_GREATER) {
    pageFormat = handle.getPageFormat(hashPrintRequestAttributeSet);
  } else {
    pageFormat = handle.defaultPage();
  }
  Paper paper = pageFormat.getPaper();
  paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
  pageFormat.setPaper(paper);
  referencePageFormat = handle.validatePage(pageFormat);
  PrinterJob initPrinterJob = PrinterJob.getPrinterJob();
  try {
    // Should not happen since the handle has it.
    initPrinterJob.setPrintService(handle.getPrintService());
  } catch(Exception e) {
    e.printStackTrace();
  }
  initPrinterJob.setPageable(new Pageable() {
    @Override
	public int getNumberOfPages() {
      return UNKNOWN_NUMBER_OF_PAGES;
    }
    @Override
	public Printable getPrintable(int pageIndex) {
      return (g, pageFormat1, pageIndex1) -> {
	  Graphics2D g2D = (Graphics2D)g;
	  AffineTransform transform = g2D.getTransform();
	  dpiX = (int)Math.round(72 * transform.getScaleX());
	  dpiY = (int)Math.round(72 * transform.getScaleY());
//          int iX = (int)Math.round(pageFormat.getImageableX());
//          int iY = (int)Math.round(pageFormat.getImageableY());
//          g.translate(iX, iY);
//          referencePageFormat = pageFormat;
	  initialState = new State();
	  Font font = g2D.getFont();
	  font = font.deriveFont(font.getSize2D() * getDPI_().y / 72);
//          g2D.setFont(font);
	  initialState.transform = g2D.getTransform();
	  initialState.transform.setToScale(1, 1);
	  g2D.setTransform(initialState.transform);
	  initialState.font = font;
	  initialState.background = g2D.getBackground();
	  initialState.color = g2D.getColor();
	  initialState.userClip = null;
	  initialState.composite = g2D.getComposite();
	  initialState.renderingHints = g2D.getRenderingHints();
	  initialState.stroke = g2D.getStroke();
	  initialState.fontRenderContext = g2D.getFontRenderContext();
	  handle.cancel();
	  return Printable.NO_SUCH_PAGE;
	};
    }
    @Override
	public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
      return referencePageFormat;
    }
  });
  try {
    initPrinterJob.print(hashPrintRequestAttributeSet);
  } catch(Exception e) {
//    e.printStackTrace();
  }
}

protected int dpiX;
protected int dpiY;

/**
 * Creates the printer handle.
 * This method is called internally by the instance creation
 * mechanism of the <code>Device</code> class.
 * @param deviceData the device data
 */
@Override
protected void create(DeviceData deviceData) {
	data = (PrinterData)deviceData;
	PrintService selectedPrintService = null;
  PrintService[] printServices = PrinterJob.lookupPrintServices();
  for(int i=0; i<printServices.length; i++) {
    PrintService printService = printServices[i];
    if(printService.getClass().getName().equals(data.driver) && printService.getName().equals(data.name)) {
      selectedPrintService = printService;
      break;
    }
  }
  handle = PrinterJob.getPrinterJob();
  if(selectedPrintService != null) {
    try {
      handle.setPrintService(selectedPrintService);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
	if (handle == null) SWT.error(SWT.ERROR_NO_HANDLES);
}

abstract class CGCCommand {
  public abstract void run(CGC cgc);
}

volatile int pageCount;
volatile List pageCommandList;
volatile State initialState;
CGCRecorder lastRecorder;

void addCommand(CGCRecorder recorder, CGCCommand command) {
  if(pageCount > 0) {
    if(lastRecorder != recorder) {
      lastRecorder = recorder;
      addCommand(recorder, recorder.getStateCommand());
    }
    ((List)pageCommandList.get(pageCount - 1)).add(command);
  }
}

class State {
  Font font;
  Color background;
  Color color;
  Shape userClip;
  Composite composite;
  RenderingHints renderingHints;
  Stroke stroke;
  AffineTransform transform;
  FontRenderContext fontRenderContext;
}

class CGCRecorder extends CGC.CGCGraphics2D {
  protected Graphics2D graphics2D = new NullGraphics2D();
  @Override
public Graphics2D getGraphics() {
    return graphics2D;
  }
  public void setState(State state) {
    super.setFont(state.font);
    super.setBackground(state.background);
    super.setColor(state.color);
    super.setUserClip(state.userClip);
    super.setComposite(state.composite);
    super.setRenderingHints(state.renderingHints);
    super.setStroke(state.stroke);
    super.setTransform(state.transform);
    setFontRenderContext(state.fontRenderContext);
  }
  public State getState() {
    State state = new State();
    state.font = super.getFont();
    state.background = super.getBackground();
    state.color = super.getColor();
    state.userClip = super.getUserClip();
    state.composite = super.getComposite();
    state.renderingHints = super.getRenderingHints();
    state.stroke = super.getStroke();
    state.fontRenderContext = super.getFontRenderContext();
    state.transform = super.getTransform();
    return state;
  }
  public CGCCommand getStateCommand() {
    final State state = getState();
    return new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setFont(state.font);
        cgc.setColor(state.color);
        cgc.setBackground(state.background);
        cgc.setUserClip(state.userClip);
        cgc.setComposite(state.composite);
        cgc.setRenderingHints(state.renderingHints);
        cgc.setStroke(state.stroke);
        if(cgc instanceof CGCRecorder) {
          ((CGCRecorder)cgc).setFontRenderContext(state.fontRenderContext);
        }
        cgc.setTransform(state.transform);
      }
    };
  }
  @Override
public void copyArea(final int x, final int y, final int width, final int height, final int dx, final int dy) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        copyArea(x, y, width, height, dx, dy);
      }
    });
    super.copyArea(x, y, width, height, dx, dy);
  }
  @Override
public void dispose() {
//    addCommand(this, new CGCCommand() {
//      public void run(CGC cgc) {
//        cgc.dispose();
//      }
//    });
    super.dispose();
  }
  @Override
public void draw(final Shape s) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.draw(s);
      }
    });
    super.draw(s);
  }
  @Override
public void drawArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.drawArc(x, y, width, height, startAngle, arcAngle);
      }
    });
    super.drawArc(x, y, width, height, startAngle, arcAngle);
  }
  @Override
public boolean drawImage(Image img, final int dx1, final int dy1, final int dx2, final int dy2, final int sx1, final int sy1, final int sx2, final int sy2, final ImageObserver observer) {
    final BufferedImage image = new BufferedImage(sx2 - sx1, sy2 - sy1, BufferedImage.TYPE_INT_ARGB);
    image.getGraphics().drawImage(img, 0, 0, image.getWidth(), image.getHeight(), sx1, sy1, sx2, sy2, observer);
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, image.getWidth(), image.getHeight(), observer);
      }
    });
    return super.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
  }
  @Override
public void drawLine(final int x1, final int y1, final int x2, final int y2) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.drawLine(x1, y1, x2, y2);
      }
    });
    super.drawLine(x1, y1, x2, y2);
  }
  @Override
public void drawOval(final int x, final int y, final int width, final int height) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.drawOval(x, y, width, height);
      }
    });
    super.drawOval(x, y, width, height);
  }
  @Override
public void drawPolygon(final int[] points, final int[] points2, final int points3) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.drawPolygon(points, points2, points3);
      }
    });
    super.drawPolygon(points, points2, points3);
  }
  @Override
public void drawPolyline(final int[] points, final int[] points2, final int points3) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.drawPolyline(points, points2, points3);
      }
    });
    super.drawPolyline(points, points2, points3);
  }
  @Override
public void drawRect(final int x, final int y, final int width, final int height) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.drawRect(x, y, width, height);
      }
    });
    super.drawRect(x, y, width, height);
  }
  @Override
public void drawRoundRect(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
      }
    });
    super.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
  }
  @Override
public void drawString(final String str, final int x, final int y) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.drawString(str, x, y);
      }
    });
    super.drawString(str, x, y);
  }
  @Override
public void fill(final Shape s) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.fill(s);
      }
    });
    super.fill(s);
  }
  @Override
public void fillArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.fillArc(x, y, width, height, startAngle, arcAngle);
      }
    });
    super.fillArc(x, y, width, height, startAngle, arcAngle);
  }
  @Override
public void fillOval(final int x, final int y, final int width, final int height) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.fillOval(x, y, width, height);
      }
    });
    super.fillOval(x, y, width, height);
  }
  @Override
public void fillPolygon(final int[] xPoints, final int[] yPoints, final int nPoints) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.fillPolygon(xPoints, yPoints, nPoints);
      }
    });
    super.fillPolygon(xPoints, yPoints, nPoints);
  }
  @Override
public void fillRect(final int x, final int y, final int width, final int height) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.fillRect(x, y, width, height);
      }
    });
    super.fillRect(x, y, width, height);
  }
  @Override
public void fillRoundRect(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
      }
    });
    super.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
  }
  @Override
public Color getBackground() {
    return super.getBackground();
  }
  @Override
public Color getColor() {
    return super.getColor();
  }
  @Override
public Composite getComposite() {
    return super.getComposite();
  }
  @Override
public Font getFont() {
    return super.getFont();
  }
  @Override
public FontMetrics getFontMetrics() {
    return super.getFontMetrics();
  }
  protected FontRenderContext fontRenderContext;
  public void setFontRenderContext(FontRenderContext fontRenderContext) {
    this.fontRenderContext = fontRenderContext;
  }
  @Override
public FontRenderContext getFontRenderContext() {
    if(fontRenderContext != null) {
      return fontRenderContext;
    }
    return super.getFontRenderContext();
  }
  @Override
public Paint getPaint() {
    return super.getPaint();
  }
  @Override
public Object getRenderingHint(Key hintKey) {
    return super.getRenderingHint(hintKey);
  }
  @Override
public RenderingHints getRenderingHints() {
    return super.getRenderingHints();
  }
  @Override
public Stroke getStroke() {
    return super.getStroke();
  }
  @Override
public AffineTransform getTransform() {
    return super.getTransform();
  }
  @Override
public void setBackground(final Color background) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setBackground(background);
      }
    });
    super.setBackground(background);
  }
  @Override
public void setColor(final Color color) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setColor(color);
      }
    });
    super.setColor(color);
  }
  @Override
public void setComposite(final Composite comp) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setComposite(comp);
      }
    });
    super.setComposite(comp);
  }
  @Override
public void setFont(final Font font) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setFont(font);
      }
    });
    super.setFont(font);
  }
  @Override
public void setPaint(final Paint paint) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setPaint(paint);
      }
    });
    super.setPaint(paint);
  }
  @Override
public void setPaintMode() {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setPaintMode();
      }
    });
    super.setPaintMode();
  }
  @Override
public void setRenderingHint(final Key hintKey, final Object hintValue) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setRenderingHint(hintKey, hintValue);
      }
    });
    super.setRenderingHint(hintKey, hintValue);
  }
  @Override
public void setRenderingHints(final Map hints) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setRenderingHints(hints);
      }
    });
    super.setRenderingHints(hints);
  }
  @Override
public void setStroke(final Stroke s) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setStroke(s);
      }
    });
    super.setStroke(s);
  }
  @Override
public void setTransform(final AffineTransform tx) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setTransform(tx);
      }
    });
    super.setTransform(tx);
  }
  @Override
public void setXORMode(final Color c1) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setXORMode(c1);
      }
    });
    super.setXORMode(c1);
  }
  @Override
public void transform(final AffineTransform tx) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.transform(tx);
      }
    });
    super.transform(tx);
  }
  @Override
public void setUserClip(final Shape userClip) {
    addCommand(this, new CGCCommand() {
      @Override
	public void run(CGC cgc) {
        cgc.setUserClip(userClip);
      }
    });
    super.setUserClip(userClip);
  }
  @Override
public Shape getUserClip() {
    return super.getUserClip();
  }
  @Override
public Dimension getDeviceSize() {
    Rectangle bounds = getBounds();
    return new Dimension(bounds.width, bounds.height);
  }
}

/**
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Printer</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param data the platform specific GC data
 * @return the platform specific GC handle
 */
@Override
public CGC internal_new_GC (GCData data) {
	if (handle == null) SWT.error(SWT.ERROR_NO_HANDLES);
	CGCRecorder recorder = new CGCRecorder();
	recorder.setState(initialState);
  return recorder;
//	if (data != null) {
//		if (isGCCreated) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
//		if ((data.style & mask) != 0) {
//			data.layout = (data.style & SWT.RIGHT_TO_LEFT) != 0 ? OS.LAYOUT_RTL : 0;
//		} else {
//			data.style |= SWT.LEFT_TO_RIGHT;
//		}
//		data.device = this;
//		data.hFont = OS.GetCurrentObject(handle, OS.OBJ_FONT);
//
//		isGCCreated = true;
//	}
//	return handle;
}

/**
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Printer</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param hDC the platform specific GC handle
 * @param data the platform specific GC data
 */
@Override
public void internal_dispose_GC (CGC hDC, GCData data) {
	if (data != null) hDC.dispose();
}

HashPrintRequestAttributeSet getHashPrintRequestAttributeSet() {
  java.util.List printRequestAttributeList = new ArrayList();
  if(data.printToFile) {
    printRequestAttributeList.add(new Destination(new File(data.fileName).toURI()));
  }
  if(data.scope == PrinterData.PAGE_RANGE) {
    printRequestAttributeList.add(new PageRanges(data.startPage, data.endPage));
  }
  HashPrintRequestAttributeSet hashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
  // TODO (visjee): SWING
//  if(data.otherData != null) {
//    hashPrintRequestAttributeSet.addAll(data.otherData);
//  }
  hashPrintRequestAttributeSet.addAll(new HashPrintRequestAttributeSet((PrintRequestAttribute[])printRequestAttributeList.toArray(new PrintRequestAttribute[0])));
  return hashPrintRequestAttributeSet;
}

/**
 * Starts a print job and returns true if the job started successfully
 * and false otherwise.
 * <p>
 * This must be the first method called to initiate a print job,
 * followed by any number of startPage/endPage calls, followed by
 * endJob. Calling startPage, endPage, or endJob before startJob
 * will result in undefined behavior.
 * </p>
 *
 * @param jobName the name of the print job to start
 * @return true if the job started successfully and false otherwise.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #startPage
 * @see #endPage
 * @see #endJob
 */
public boolean startJob(String jobName) {
	checkDevice();
	handle.setJobName(jobName);
//	handle.setCopies(data.copyCount);
  pageCount = 0;
  pageCommandList = new ArrayList();
  // TODO: Check conditions that return false;
  return true;
//	DOCINFO di = new DOCINFO();
//	di.cbSize = DOCINFO.sizeof;
//	int hHeap = OS.GetProcessHeap();
//	int lpszDocName = 0;
//	if (jobName != null && jobName.length() != 0) {
//		/* Use the character encoding for the default locale */
//		TCHAR buffer = new TCHAR(0, jobName, true);
//		int byteCount = buffer.length() * TCHAR.sizeof;
//		lpszDocName = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
//		OS.MoveMemory(lpszDocName, buffer, byteCount);
//		di.lpszDocName = lpszDocName;
//	}
//	int lpszOutput = 0;
//	if (data.printToFile && data.fileName != null) {
//		/* Use the character encoding for the default locale */
//		TCHAR buffer = new TCHAR(0, data.fileName, true);
//		int byteCount = buffer.length() * TCHAR.sizeof;
//		lpszOutput = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
//		OS.MoveMemory(lpszOutput, buffer, byteCount);
//		di.lpszOutput = lpszOutput;
//	}
//	int rc = OS.StartDoc(handle, di);
//	if (lpszDocName != 0) OS.HeapFree(hHeap, 0, lpszDocName);
//	if (lpszOutput != 0) OS.HeapFree(hHeap, 0, lpszOutput);
//	return rc > 0;
}

/**
 * Ends the current print job.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #startJob
 * @see #startPage
 * @see #endPage
 */
public void endJob() {
	checkDevice();
  handle.setPageable(new Pageable() {
    @Override
	public int getNumberOfPages() {
      return pageCount;
    }
    @Override
	public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
      return (g, pageFormat, pageIndex1) -> {
	  if(pageIndex1 < 0 || pageIndex1 >= pageCount) {
	    return Printable.NO_SUCH_PAGE;
	  }
	  final Graphics2D g2D = ((Graphics2D)g);
	  CGC cgc = new CGC.CGCGraphics2D() {
	    @Override
		public Graphics2D getGraphics() {
	      return g2D;
	    }
	    @Override
		public Dimension getDeviceSize() {
	      Rectangle bounds = getBounds();
	      return new Dimension(bounds.width, bounds.height);
	    }
	  };
	  List commandList = (List)pageCommandList.get(pageIndex1);
//      int iX = (int)Math.round(referencePageFormat.getImageableX());
//      int iY = (int)Math.round(referencePageFormat.getImageableY());
//      g.translate(iX, iY);
	  for(int i=0; i<commandList.size(); i++) {
	    CGCCommand command = (CGCCommand)commandList.get(i);
	    command.run(cgc);
	  }
//      g.translate(-iX, -iY);
	  return Printable.PAGE_EXISTS;
	};
    }
    @Override
	public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
      return referencePageFormat;
    }
  });
  try {
    handle.print(getHashPrintRequestAttributeSet());
  } catch(Exception e) {
    e.printStackTrace();
  }
  pageCommandList = null;
  lastRecorder = null;
//	OS.EndDoc(handle);
}

/**
 * Cancels a print job in progress.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void cancelJob() {
	checkDevice();
  handle.cancel();
  pageCommandList = null;
//	OS.AbortDoc(handle);
}

/**
 * Starts a page and returns true if the page started successfully
 * and false otherwise.
 * <p>
 * After calling startJob, this method may be called any number of times
 * along with a matching endPage.
 * </p>
 *
 * @return true if the page started successfully and false otherwise.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #endPage
 * @see #startJob
 * @see #endJob
 */
public boolean startPage() {
	checkDevice();
  pageCount++;
  pageCommandList.add(new ArrayList());
  lastRecorder = null;
  // TODO: find if false can happen
  return true;
}

/**
 * Ends the current page.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #startPage
 * @see #startJob
 * @see #endJob
 */
public void endPage() {
	checkDevice();
	//TODO: set the default attributes for the next page?
}

/**
 * Returns a point whose x coordinate is the horizontal
 * dots per inch of the printer, and whose y coordinate
 * is the vertical dots per inch of the printer.
 *
 * @return the horizontal and vertical DPI
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
@Override
protected Point getDPI_() {
	checkDevice();
	initPrinterData();
//  int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
//  int dpi = (int)Math.round(Math.pow(72, 2) / resolution);
//  return new Point(dpi, dpi);
  return new Point(dpiX, dpiY);
//	int dpiX = OS.GetDeviceCaps(handle, OS.LOGPIXELSX);
//	int dpiY = OS.GetDeviceCaps(handle, OS.LOGPIXELSY);
//	return new Point(dpiX, dpiY);
}

/**
 * Returns a rectangle describing the receiver's size and location.
 * For a printer, this is the size of a page, in pixels.
 *
 * @return the bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getClientArea
 * @see #computeTrim
 */
@Override
public Rectangle getBounds() {
	checkDevice();
//	return getClientArea();
	Point dpi = getDPI_();
  int width = (int)Math.round(referencePageFormat.getImageableWidth() * dpi.x / 72.0);
  int height = (int)Math.round(referencePageFormat.getImageableHeight() * dpi.y / 72.0);
  return new Rectangle(0, 0, width, height);
//	int width = OS.GetDeviceCaps(handle, OS.PHYSICALWIDTH);
//	int height = OS.GetDeviceCaps(handle, OS.PHYSICALHEIGHT);
//	return new Rectangle(0, 0, width, height);
}

PageFormat referencePageFormat;

/**
 * Returns a rectangle which describes the area of the
 * receiver which is capable of displaying data.
 * For a printer, this is the size of the printable area
 * of a page, in pixels.
 *
 * @return the client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getBounds
 * @see #computeTrim
 */
@Override
public Rectangle getClientArea() {
	checkDevice();
	return getBounds();
//	int width = (int)Math.round(referencePageFormat.getImageableWidth());
//	int height = (int)Math.round(referencePageFormat.getImageableHeight());
//  return new Rectangle(0, 0, width, height);
}

/**
 * Given a desired <em>client area</em> for the receiver
 * (as described by the arguments), returns the bounding
 * rectangle which would be required to produce that client
 * area.
 * <p>
 * In other words, it returns a rectangle such that, if the
 * receiver's bounds were set to that rectangle, the area
 * of the receiver which is capable of displaying data
 * (that is, not covered by the "trimmings") would be the
 * rectangle described by the arguments (relative to the
 * receiver's parent).
 * </p>
 * Note that there is no setBounds for a printer. This method
 * is usually used by passing in the client area (the 'printable
 * area') of the printer. It can also be useful to pass in 0, 0, 0, 0.
 *
 * @param x the desired x coordinate of the client area
 * @param y the desired y coordinate of the client area
 * @param width the desired width of the client area
 * @param height the desired height of the client area
 * @return the required bounds to produce the given client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getBounds
 * @see #getClientArea
 */
public Rectangle computeTrim(int x, int y, int width, int height) {
	checkDevice();
  return new Rectangle(x, y, width, height);
}

/**
 * Returns a <code>PrinterData</code> object representing the
 * target printer for this print job.
 *
 * @return a PrinterData object describing the receiver
 */
public PrinterData getPrinterData() {
	return data;
}

/**
 * Checks the validity of this device.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
@Override
protected void checkDevice() {
	if (handle == null) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
}

/**
 * Releases any internal state prior to destroying this printer.
 * This method is called internally by the dispose
 * mechanism of the <code>Device</code> class.
 */
@Override
protected void release() {
	super.release();
	data = null;
}

/**
 * Destroys the printer handle.
 * This method is called internally by the dispose
 * mechanism of the <code>Device</code> class.
 */
@Override
protected void destroy() {
	handle = null;
}

}
