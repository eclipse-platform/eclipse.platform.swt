/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFontDatabase;
import com.trolltech.qt.gui.QPaintDeviceInterface;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPalette.ColorGroup;
import com.trolltech.qt.gui.QPalette.ColorRole;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;

/**
 * This class is the abstract superclass of all device objects, such as the
 * Display device and the Printer device. Devices can have a graphics context
 * (GC) created for them, and they can be drawn on by sending messages to the
 * associated GC.
 * 
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */
public abstract class Device implements Drawable {

	/* Debugging */
	public static boolean DEBUG;
	private boolean debug = DEBUG;
	boolean tracking = DEBUG;
	private Error[] errors;
	private Object[] objects;
	private Object trackingLock;

	/* System Font */
	Font systemFont;
	private QColor COLOR_LIST_BACKGROUND;
	private QColor COLOR_LIST_FOREGROUND;
	private QColor COLOR_LIST_SELECTION;
	private QColor COLOR_LIST_SELECTION_TEXT;
	private QColor COLOR_TITLE_BACKGROUND;
	private QColor COLOR_TITLE_BACKGROUND_GRADIENT;
	private QColor COLOR_TITLE_FOREGROUND;
	private QColor COLOR_TITLE_INACTIVE_BACKGROUND;
	private QColor COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT;
	private QColor COLOR_TITLE_INACTIVE_FOREGROUND;
	private QColor COLOR_WIDGET_BACKGROUND;
	private QColor COLOR_WIDGET_BORDER;
	private QColor COLOR_WIDGET_DARK_SHADOW;
	private QColor COLOR_WIDGET_FOREGROUND;
	private QColor COLOR_WIDGET_HIGHLIGHT_SHADOW;
	private QColor COLOR_WIDGET_LIGHT_SHADOW;
	private QColor COLOR_WIDGET_NORMAL_SHADOW;
	private QColor COLOR_INFO_FOREGROUND;
	private QColor COLOR_INFO_BACKGROUND;

	private boolean disposed;
	private QPaintDeviceInterface paintDevice;

	/*
	 * TEMPORARY CODE. When a graphics object is created and the device
	 * parameter is null, the current Display is used. This presents a problem
	 * because SWT graphics does not reference classes in SWT widgets. The
	 * correct fix is to remove this feature. Unfortunately, too many
	 * application programs rely on this feature.
	 */
	protected static Device CurrentDevice;
	protected static Runnable DeviceFinder;
	static {
		try {
			Class.forName("org.eclipse.swt.widgets.Display"); //$NON-NLS-1$
		} catch (ClassNotFoundException e) {
		}
	}

	/*
	 * TEMPORARY CODE.
	 */
	static synchronized Device getDevice() {
		if (DeviceFinder != null) {
			DeviceFinder.run();
		}
		Device device = CurrentDevice;
		CurrentDevice = null;
		return device;
	}

	/**
	 * Constructs a new instance of this class.
	 * <p>
	 * You must dispose the device when it is no longer required.
	 * </p>
	 * 
	 * @see #create
	 * @see #init
	 * 
	 * @since 3.1
	 */
	public Device() {
		this(null);
	}

	/**
	 * Constructs a new instance of this class.
	 * <p>
	 * You must dispose the device when it is no longer required.
	 * </p>
	 * 
	 * @param data
	 *            the DeviceData which describes the receiver
	 * 
	 * @see #create
	 * @see #init
	 * @see DeviceData
	 */
	public Device(DeviceData data) {
		synchronized (Device.class) {
			if (data != null) {
				debug = data.debug;
				tracking = data.tracking;
			}
			if (tracking) {
				errors = new Error[128];
				objects = new Object[128];
				trackingLock = new Object();
			}
			create(data);
			init();
		}
	}

	protected void setPaintDevice(QPaintDeviceInterface paintDevice) {
		this.paintDevice = paintDevice;
	}

	/**
	 * Throws an <code>SWTException</code> if the receiver can not be accessed
	 * by the caller. This may include both checks on the state of the receiver
	 * and more generally on the entire execution context. This method
	 * <em>should</em> be called by device implementors to enforce the standard
	 * SWT invariants.
	 * <p>
	 * Currently, it is an error to invoke any method (other than
	 * <code>isDisposed()</code> and <code>dispose()</code>) on a device that
	 * has had its <code>dispose()</code> method called.
	 * </p>
	 * <p>
	 * In future releases of SWT, there may be more or fewer error checks and
	 * exceptions may be thrown for different reasons.
	 * <p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	protected void checkDevice() {
		if (disposed) {
			SWT.error(SWT.ERROR_DEVICE_DISPOSED);
		}
	}

	/**
	 * Creates the device in the operating system. If the device does not have a
	 * handle, this method may do nothing depending on the device.
	 * <p>
	 * This method is called before <code>init</code>.
	 * </p>
	 * <p>
	 * Subclasses are supposed to reimplement this method and not call the
	 * <code>super</code> implementation.
	 * </p>
	 * 
	 * @param data
	 *            the DeviceData which describes the receiver
	 * 
	 * @see #init
	 */
	protected void create(DeviceData data) {
	}

	/**
	 * Destroys the device in the operating system and releases the device's
	 * handle. If the device does not have a handle, this method may do nothing
	 * depending on the device.
	 * <p>
	 * This method is called after <code>release</code>.
	 * </p>
	 * <p>
	 * Subclasses are supposed to reimplement this method and not call the
	 * <code>super</code> implementation.
	 * </p>
	 * 
	 * @see #dispose
	 * @see #release
	 */
	protected void destroy() {
	}

	/**
	 * Disposes of the operating system resources associated with the receiver.
	 * After this method has been invoked, the receiver will answer
	 * <code>true</code> when sent the message <code>isDisposed()</code>.
	 * 
	 * @see #release
	 * @see #destroy
	 * @see #checkDevice
	 */
	public void dispose() {
		synchronized (Device.class) {
			if (isDisposed()) {
				return;
			}
			checkDevice();
			release();
			destroy();
			disposed = true;
			if (tracking) {
				synchronized (trackingLock) {
					printErrors();
					objects = null;
					errors = null;
					trackingLock = null;
				}
			}
		}
	}

	void dispose_Object(Object object) {
		synchronized (trackingLock) {
			for (int i = 0; i < objects.length; i++) {
				if (objects[i] == object) {
					objects[i] = null;
					errors[i] = null;
					return;
				}
			}
		}
	}

	/**
	 * Returns a rectangle describing the receiver's size and location.
	 * 
	 * @return the bounding rectangle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Rectangle getBounds() {
		checkDevice();
		return new Rectangle(0, 0, paintDevice.width(), paintDevice.height());
	}

	/**
	 * Returns a <code>DeviceData</code> based on the receiver. Modifications
	 * made to this <code>DeviceData</code> will not affect the receiver.
	 * 
	 * @return a <code>DeviceData</code> containing the device's data and
	 *         attributes
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see DeviceData
	 */
	public DeviceData getDeviceData() {
		checkDevice();
		DeviceData data = new DeviceData();
		data.debug = debug;
		data.tracking = tracking;
		if (tracking) {
			synchronized (trackingLock) {
				int count = 0, length = objects.length;
				for (int i = 0; i < length; i++) {
					if (objects[i] != null) {
						count++;
					}
				}
				int index = 0;
				data.objects = new Object[count];
				data.errors = new Error[count];
				for (int i = 0; i < length; i++) {
					if (objects[i] != null) {
						data.objects[index] = objects[i];
						data.errors[index] = errors[i];
						index++;
					}
				}
			}
		} else {
			data.objects = new Object[0];
			data.errors = new Error[0];
		}
		return data;
	}

	/**
	 * Returns a rectangle which describes the area of the receiver which is
	 * capable of displaying data.
	 * 
	 * @return the client area
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #getBounds
	 */
	public Rectangle getClientArea() {
		return getBounds();
	}

	/**
	 * Returns the bit depth of the screen, which is the number of bits it takes
	 * to represent the number of unique colors that the screen is currently
	 * capable of displaying. This number will typically be one of 1, 8, 15, 16,
	 * 24 or 32.
	 * 
	 * @return the depth of the screen
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public int getDepth() {
		checkDevice();
		return paintDevice.depth();
	}

	/**
	 * Returns a point whose x coordinate is the horizontal dots per inch of the
	 * display, and whose y coordinate is the vertical dots per inch of the
	 * display.
	 * 
	 * @return the horizontal and vertical DPI
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Point getDPI() {
		checkDevice();
		return new Point(paintDevice.physicalDpiX(), paintDevice.physicalDpiY());
	}

	/**
	 * Returns <code>FontData</code> objects which describe the fonts that match
	 * the given arguments. If the <code>faceName</code> is null, all fonts will
	 * be returned.
	 * 
	 * @param faceName
	 *            the name of the font to look for, or null
	 * @param scalable
	 *            if true only scalable fonts are returned, otherwise only
	 *            non-scalable fonts are returned.
	 * @return the matching font data
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public FontData[] getFontList(String faceName, boolean scalable) {
		checkDevice();
		QFontDatabase qFontDatabase = null;
		qFontDatabase = new QFontDatabase();
		String fontFamilies[] = qFontDatabase.families().toArray(new String[0]);
		// for storing FontData arrays
		List<FontData> fontData = new ArrayList<FontData>();

		for (int i = 0; i < fontFamilies.length; i++) {
			String family = null;
			if (faceName == null) {
				family = fontFamilies[i];
			} else {
				if (faceName.equals(fontFamilies[i])) {
					family = fontFamilies[i];
				} else {
					continue;
				}
			}
			getFontList(fontData, qFontDatabase, family, scalable);
			if (faceName != null) {
				break;
			}
		}

		qFontDatabase.dispose(); //TODO_DISPOSE (called many times)
		qFontDatabase = null;

		if (fontData.size() > 0) {
			FontData[] result = new FontData[fontData.size()];
			int size = fontData.size();
			for (int i = 0; i < size; ++i) {
				result[i] = fontData.get(i);
			}
			return result;
		} else {
			return new FontData[0];
		}
	}

	private void getFontList(List<FontData> fontData, QFontDatabase qFontDatabase, String family, boolean onlyScalable) {
		String nativeStyles[] = qFontDatabase.styles(family).toArray(new String[0]);
		int prevStyle = -1;
		int i = 0;
		// Go trough each native style of the font family, map them to SWT
		// styles
		// and create a new FontData for each newly found SWT style of the
		// family.
		// Still, it may be possible that there are no native styles for a
		// family,
		// in which case we add a single FontData with default SWT style:
		// NORMAL.
		// In addition we go trough all heights of non scalable font families,
		// creating a new FontData for all heights of all native styles.
		do {
			String nativeStyle = nativeStyles.length > 0 ? nativeStyles[i] : null;
			boolean isScalable = qFontDatabase.isScalable(family, nativeStyle);
			int style = SWT.NORMAL;
			if (qFontDatabase.bold(family, nativeStyle)) {
				style |= SWT.BOLD;
			}
			if (qFontDatabase.italic(family, nativeStyle)) {
				style |= SWT.ITALIC;
			}
			if (style != prevStyle) {
				prevStyle = style;
				int[] heights = null;
				int j = 0;
				if (onlyScalable && isScalable) {
					heights = new int[1];
					heights[0] = FontData.FONT_DEF_HEIGHT;
				} else if (!onlyScalable && !isScalable) {
					List<Integer> pointSizes = null;
					if (nativeStyle == null) {
						pointSizes = qFontDatabase.pointSizes(family);
					} else {
						pointSizes = qFontDatabase.pointSizes(family, nativeStyle);
					}
					heights = new int[pointSizes.size()];
					for (int x = 0; x < pointSizes.size(); x++) {
						heights[x] = pointSizes.get(x);
					}
				}
				if (heights != null) {
					do {
						FontData fd = new FontData(family, heights[j], style);
						fontData.add(fd);
						j++;
					} while (!onlyScalable && !isScalable && j < heights.length);
				}
			}
			i++;
		} while (i < nativeStyles.length);
	}

	/**
	 * Returns the matching standard color for the given constant, which should
	 * be one of the color constants specified in class <code>SWT</code>. Any
	 * value other than one of the SWT color constants which is passed in will
	 * result in the color black. This color should not be freed because it was
	 * allocated by the system, not the application.
	 * 
	 * @param id
	 *            the color constant
	 * @return the matching color
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see SWT
	 */
	public Color getSystemColor(int id) {
		checkDevice();
		int pixel = 0x00000000;
		switch (id) {
		case SWT.COLOR_WHITE:
			return Color.qt_new(this, QColor.white);
		case SWT.COLOR_BLACK:
			return Color.qt_new(this, QColor.black);
		case SWT.COLOR_RED:
			return Color.qt_new(this, QColor.red);
		case SWT.COLOR_DARK_RED:
			return Color.qt_new(this, QColor.darkRed);
		case SWT.COLOR_GREEN:
			return Color.qt_new(this, QColor.green);
		case SWT.COLOR_DARK_GREEN:
			return Color.qt_new(this, QColor.darkGreen);
		case SWT.COLOR_YELLOW:
			return Color.qt_new(this, QColor.yellow);
		case SWT.COLOR_DARK_YELLOW:
			return Color.qt_new(this, QColor.darkYellow);
		case SWT.COLOR_BLUE:
			return Color.qt_new(this, QColor.blue);
		case SWT.COLOR_DARK_BLUE:
			return Color.qt_new(this, QColor.darkBlue);
		case SWT.COLOR_MAGENTA:
			return Color.qt_new(this, QColor.magenta);
		case SWT.COLOR_DARK_MAGENTA:
			return Color.qt_new(this, QColor.magenta);
		case SWT.COLOR_CYAN:
			return Color.qt_new(this, QColor.cyan);
		case SWT.COLOR_DARK_CYAN:
			return Color.qt_new(this, QColor.darkCyan);
		case SWT.COLOR_GRAY:
			return Color.qt_new(this, QColor.gray);
		case SWT.COLOR_DARK_GRAY:
			return Color.qt_new(this, QColor.darkGray);
		case SWT.COLOR_LIST_BACKGROUND:
			return Color.qt_new(this, COLOR_LIST_BACKGROUND);
		case SWT.COLOR_LIST_FOREGROUND:
			return Color.qt_new(this, COLOR_LIST_FOREGROUND);
		case SWT.COLOR_LIST_SELECTION:
			return Color.qt_new(this, COLOR_LIST_SELECTION);
		case SWT.COLOR_LIST_SELECTION_TEXT:
			return Color.qt_new(this, COLOR_LIST_SELECTION_TEXT);
		case SWT.COLOR_TITLE_BACKGROUND:
			return Color.qt_new(this, COLOR_TITLE_BACKGROUND);
		case SWT.COLOR_TITLE_BACKGROUND_GRADIENT:
			return Color.qt_new(this, COLOR_TITLE_BACKGROUND_GRADIENT);
		case SWT.COLOR_TITLE_FOREGROUND:
			return Color.qt_new(this, COLOR_TITLE_FOREGROUND);
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND:
			return Color.qt_new(this, COLOR_TITLE_INACTIVE_BACKGROUND);
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT:
			return Color.qt_new(this, COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT);
		case SWT.COLOR_TITLE_INACTIVE_FOREGROUND:
			return Color.qt_new(this, COLOR_TITLE_INACTIVE_FOREGROUND);
		case SWT.COLOR_WIDGET_BACKGROUND:
			return Color.qt_new(this, COLOR_WIDGET_BACKGROUND);
		case SWT.COLOR_WIDGET_BORDER:
			return Color.qt_new(this, COLOR_WIDGET_BORDER);
		case SWT.COLOR_WIDGET_DARK_SHADOW:
			return Color.qt_new(this, COLOR_WIDGET_DARK_SHADOW);
		case SWT.COLOR_WIDGET_FOREGROUND:
			return Color.qt_new(this, COLOR_WIDGET_FOREGROUND);
		case SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW:
			return Color.qt_new(this, COLOR_WIDGET_HIGHLIGHT_SHADOW);
		case SWT.COLOR_WIDGET_LIGHT_SHADOW:
			return Color.qt_new(this, COLOR_WIDGET_LIGHT_SHADOW);
		case SWT.COLOR_WIDGET_NORMAL_SHADOW:
			return Color.qt_new(this, COLOR_WIDGET_NORMAL_SHADOW);
		case SWT.COLOR_INFO_FOREGROUND:
			return Color.qt_new(this, COLOR_INFO_FOREGROUND);
		case SWT.COLOR_INFO_BACKGROUND:
			return Color.qt_new(this, COLOR_INFO_BACKGROUND);
		}
		return Color.qt_new(this, pixel);
	}

	/**
	 * Returns a reasonable font for applications to use. On some platforms,
	 * this will match the "default font" or "system font" if such can be found.
	 * This font should not be freed because it was allocated by the system, not
	 * the application.
	 * <p>
	 * Typically, applications which want the default look should simply not set
	 * the font on the widgets they create. Widgets are always created with the
	 * correct default font for the class of user-interface component they
	 * represent.
	 * </p>
	 * 
	 * @return a font
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Font getSystemFont() {
		checkDevice();
		return Font.qt_new(this, QApplication.font());
	}

	/**
	 * Returns <code>true</code> if the underlying window system prints out
	 * warning messages on the console, and <code>setWarnings</code> had
	 * previously been called with <code>true</code>.
	 * 
	 * @return <code>true</code>if warnings are being handled, and
	 *         <code>false</code> otherwise
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public boolean getWarnings() {
		checkDevice();
		return false;
	}

	/**
	 * Initializes any internal resources needed by the device.
	 * <p>
	 * This method is called after <code>create</code>.
	 * </p>
	 * <p>
	 * If subclasses reimplement this method, they must call the
	 * <code>super</code> implementation.
	 * </p>
	 * 
	 * @see #create
	 */
	protected void init() {
		/* Initialize the system font slot */
		systemFont = getSystemFont();
		QPalette palette = QApplication.style().standardPalette();
		try {
			// Mapping colors from the QPalette to SWT colors. Not all 
			// colors are 100% right. Use snippet 235 and 235_1 to get 
			// all colors displayed.

			// Info
			COLOR_INFO_FOREGROUND = palette.color(ColorGroup.Active, ColorRole.ToolTipText);
			COLOR_INFO_BACKGROUND = palette.color(ColorGroup.Active, ColorRole.ToolTipBase);

			// List
			COLOR_LIST_BACKGROUND = palette.color(ColorGroup.Active, ColorRole.Base);
			COLOR_LIST_FOREGROUND = palette.color(ColorGroup.Active, ColorRole.Text);
			COLOR_LIST_SELECTION = palette.color(ColorGroup.Active, ColorRole.Highlight);
			COLOR_LIST_SELECTION_TEXT = palette.color(ColorGroup.Active, ColorRole.HighlightedText);

			// Title
			COLOR_TITLE_BACKGROUND = new QColor(0, 84, 227); // palette.color(ColorGroup.Active, ColorRole.Highlight);
			// TODO The required blue/grey does not exist in QPalette. Using grey as a substitute.
			COLOR_TITLE_BACKGROUND_GRADIENT = new QColor(61, 149, 255);// palette.color(ColorGroup.Active, ColorRole.Light);
			COLOR_TITLE_FOREGROUND = palette.color(ColorGroup.Active, ColorRole.HighlightedText);

			// Inactive highlight
			COLOR_TITLE_INACTIVE_BACKGROUND = new QColor(122, 150, 223);// palette.color(ColorGroup.Inactive, ColorRole.Dark);
			// TODO The required shade of grey does not exist in QPalette. Should be a bit lighter.
			COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT = new QColor(157, 185, 235); //palette.color(ColorGroup.Inactive, ColorRole.Light);
			// TODO The required shade of grey does not exist in QPalette. Should be a bit lighter.
			// Even lighter still than background gradient. It's the same at the moment.
			COLOR_TITLE_INACTIVE_FOREGROUND = new QColor(216, 228, 248); //palette.color(ColorGroup.Inactive, ColorRole.BrightText);

			// Window
			//QApplication.style().
			COLOR_WIDGET_BACKGROUND = palette.color(ColorGroup.Active, ColorRole.Window);//new QColor(239, 235, 231); 
			COLOR_WIDGET_DARK_SHADOW = palette.color(ColorGroup.Active, ColorRole.Shadow);
			COLOR_WIDGET_FOREGROUND = COLOR_WIDGET_BORDER = palette.color(ColorGroup.Active, ColorRole.WindowText);
			COLOR_WIDGET_HIGHLIGHT_SHADOW = palette.color(ColorGroup.Active, ColorRole.Light);

			COLOR_WIDGET_LIGHT_SHADOW = new QColor(236, 233, 216); //palette.color(ColorGroup.Active, ColorRole.Button);
			COLOR_WIDGET_NORMAL_SHADOW = new QColor(157, 155, 144); //palette.color(ColorGroup.Active, ColorRole.Mid);

		} finally {
			palette.dispose();
		}
	}

	/**
	 * Invokes platform specific functionality to allocate a new GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Device</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms, and should never be called from application code.
	 * </p>
	 * 
	 * @param data
	 *            the platform specific GC data
	 * @return the platform specific GC handle
	 */
	public abstract QPaintDeviceInterface internal_new_GC(GCData data);

	/**
	 * Invokes platform specific functionality to dispose a GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Device</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms, and should never be called from application code.
	 * </p>
	 * 
	 * @param hDC
	 *            the platform specific GC handle
	 * @param data
	 *            the platform specific GC data
	 */
	public abstract void internal_dispose_GC(QPaintDeviceInterface paintDevice, GCData data);

	/**
	 * Returns <code>true</code> if the device has been disposed, and
	 * <code>false</code> otherwise.
	 * <p>
	 * This method gets the dispose state for the device. When a device has been
	 * disposed, it is an error to invoke any other method using the device.
	 * 
	 * @return <code>true</code> when the device is disposed and
	 *         <code>false</code> otherwise
	 */
	public boolean isDisposed() {
		synchronized (Device.class) {
			return disposed;
		}
	}

	/**
	 * Loads the font specified by a file. The font will be present in the list
	 * of fonts available to the application.
	 * 
	 * @param path
	 *            the font file path
	 * @return whether the font was successfully loaded
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if path is null</li>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see Font
	 * 
	 * @since 3.3
	 */
	public boolean loadFont(String path) {
		checkDevice();
		if (path == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		int retCode = QFontDatabase.addApplicationFont(path);
		return retCode != -1; // -1 == font load failed
	}

	void new_Object(Object object) {
		synchronized (trackingLock) {
			for (int i = 0; i < objects.length; i++) {
				if (objects[i] == null) {
					objects[i] = object;
					errors[i] = new Error();
					return;
				}
			}
			Object[] newObjects = new Object[objects.length + 128];
			System.arraycopy(objects, 0, newObjects, 0, objects.length);
			newObjects[objects.length] = object;
			objects = newObjects;
			Error[] newErrors = new Error[errors.length + 128];
			System.arraycopy(errors, 0, newErrors, 0, errors.length);
			newErrors[errors.length] = new Error();
			errors = newErrors;
		}
	}

	void printErrors() {
		if (!DEBUG) {
			return;
		}
		if (tracking) {
			synchronized (trackingLock) {
				if (objects == null || errors == null) {
					return;
				}
				int objectCount = 0;
				int colors = 0, cursors = 0, fonts = 0, gcs = 0, images = 0;
				int paths = 0, patterns = 0, regions = 0, textLayouts = 0, transforms = 0;
				for (int i = 0; i < objects.length; i++) {
					Object object = objects[i];
					if (object != null) {
						objectCount++;
						if (object instanceof Color) {
							colors++;
						}
						if (object instanceof Cursor) {
							cursors++;
						}
						if (object instanceof Font) {
							fonts++;
						}
						if (object instanceof GC) {
							gcs++;
						}
						if (object instanceof Image) {
							images++;
						}
						if (object instanceof Path) {
							paths++;
						}
						if (object instanceof Pattern) {
							patterns++;
						}
						if (object instanceof Region) {
							regions++;
						}
						if (object instanceof TextLayout) {
							textLayouts++;
						}
						if (object instanceof Transform) {
							transforms++;
						}
					}
				}
				if (objectCount != 0) {
					String string = "Summary: "; //$NON-NLS-1$
					if (colors != 0) {
						string += colors + " Color(s), ";//$NON-NLS-1$
					}
					if (cursors != 0) {
						string += cursors + " Cursor(s), ";//$NON-NLS-1$
					}
					if (fonts != 0) {
						string += fonts + " Font(s), ";//$NON-NLS-1$
					}
					if (gcs != 0) {
						string += gcs + " GC(s), ";//$NON-NLS-1$
					}
					if (images != 0) {
						string += images + " Image(s), ";//$NON-NLS-1$
					}
					if (paths != 0) {
						string += paths + " Path(s), ";//$NON-NLS-1$
					}
					if (patterns != 0) {
						string += patterns + " Pattern(s), ";//$NON-NLS-1$
					}
					if (regions != 0) {
						string += regions + " Region(s), ";//$NON-NLS-1$
					}
					if (textLayouts != 0) {
						string += textLayouts + " TextLayout(s), ";//$NON-NLS-1$
					}
					if (transforms != 0) {
						string += transforms + " Transforms(s), ";//$NON-NLS-1$
					}
					if (string.length() != 0) {
						string = string.substring(0, string.length() - 2);
						System.err.println(string);
					}
					for (int i = 0; i < errors.length; i++) {
						if (errors[i] != null) {
							errors[i].printStackTrace(System.err);
						}
					}
				}
			}
		}
	}

	/**
	 * Releases any internal resources back to the operating system and clears
	 * all fields except the device handle.
	 * <p>
	 * When a device is destroyed, resources that were acquired on behalf of the
	 * programmer need to be returned to the operating system. For example, if
	 * the device allocated a font to be used as the system font, this font
	 * would be freed in <code>release</code>. Also,to assist the garbage
	 * collector and minimize the amount of memory that is not reclaimed when
	 * the programmer keeps a reference to a disposed device, all fields except
	 * the handle are zero'd. The handle is needed by <code>destroy</code>.
	 * </p>
	 * This method is called before <code>destroy</code>. </p>
	 * <p>
	 * If subclasses reimplement this method, they must call the
	 * <code>super</code> implementation.
	 * </p>
	 * 
	 * @see #dispose
	 * @see #destroy
	 */
	protected void release() {
		paintDevice = null;
	}

	/**
	 * If the underlying window system supports printing warning messages to the
	 * console, setting warnings to <code>false</code> prevents these messages
	 * from being printed. If the argument is <code>true</code> then message
	 * printing is not blocked.
	 * 
	 * @param warnings
	 *            <code>true</code>if warnings should be printed, and
	 *            <code>false</code> otherwise
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void setWarnings(boolean warnings) {
		checkDevice();
	}

}
