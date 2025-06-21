/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

/**
 * Instances of this class allow the user to select a color from a predefined
 * set of available colors.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ColorDialog extends Dialog {
	private RGB selectedRGB;
	private RGB rgb;
	private RGB[] rgbs;

	/**
	 * Constructs a new instance of this class given only its parent.
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public ColorDialog(Shell parent) {
		this(parent, SWT.APPLICATION_MODAL);
	}

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public ColorDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Makes the receiver visible and brings it to the front of the display.
	 *
	 * @return the selected color, or null if the dialog was cancelled, no color was
	 *         selected, or an error occurred
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public RGB open() {
		Shell parent = getParent();
		Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Color");
		Display display = shell.getDisplay();

		GridLayout shellLayout = new GridLayout(2, false);
		shellLayout.marginWidth = 10;
		shellLayout.marginHeight = 10;
		shell.setLayout(shellLayout);

		// Simple Color selector
		ColorComposite colorComposite = new ColorComposite(shell, SWT.BORDER);
		colorComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		if (rgb != null) {
			colorComposite.setInitialColor(rgb);
		}
		if (rgbs != null) {
			colorComposite.setCustomColors(rgbs);
		}

		final Composite[] advancedHolder = new Composite[1];
		final ColorPickerComposite[] pickerRef = new ColorPickerComposite[1];

		Composite buttons = new Composite(shell, SWT.NONE);
		GridData buttonsData = new GridData(SWT.END, SWT.CENTER, true, false);
		buttonsData.horizontalSpan = 2;
		buttons.setLayoutData(buttonsData);
		buttons.setLayout(new RowLayout());

		Button ok = new Button(buttons, SWT.PUSH);
		ok.setText("Select");

		Button cancel = new Button(buttons, SWT.PUSH);
		cancel.setText("Cancel");

		Button toggle = new Button(buttons, SWT.PUSH);
		toggle.setText("Advanced >>");

		ok.addListener(SWT.Selection, e -> {
			if (advancedHolder[0] != null && !advancedHolder[0].isDisposed()) {
				selectedRGB = pickerRef[0].getSelectedRGB();
			} else if (colorComposite.selectedColor != null) {
				selectedRGB = colorComposite.selectedColor.getRGB();
			}
			rgbs = colorComposite.getCustomColors();
			shell.close();
		});

		cancel.addListener(SWT.Selection, e -> {
			selectedRGB = null;
			shell.close();
		});

		toggle.addListener(SWT.Selection, e -> {
			boolean showingAdvanced = advancedHolder[0] != null && !advancedHolder[0].isDisposed();
			if (showingAdvanced) {
				advancedHolder[0].dispose();
				advancedHolder[0] = null;
				toggle.setText("Advanced >>");
			} else {
				Composite advanced = new Composite(shell, SWT.NONE);
				advanced.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
				advanced.setLayout(new FillLayout());

				ColorPickerComposite picker = new ColorPickerComposite(advanced, SWT.BORDER);

				if (colorComposite.selectedColor != null) {
					RGB current = colorComposite.selectedColor.getRGB();
					picker.selectedRGB = current;
					picker.selectedColor = new Color(display, current.red, current.green, current.blue);
				}

				advancedHolder[0] = advanced;
				pickerRef[0] = picker;

				advanced.moveAbove(buttons);

				toggle.setText("Simple <<");
			}

			shell.layout(true, true);
			shell.pack();
		});


		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return selectedRGB;
	}

	/**
	 * Sets the receiver's selected color to be the argument.
	 *
	 * @param rgb the new RGB value for the selected color, may be null to let the
	 *            platform select a default when open() is called
	 * @see PaletteData#getRGBs
	 */
	public void setRGB(RGB rgb) {
		this.rgb = rgb;
	}

	/**
	 * Returns the currently selected color in the receiver.
	 *
	 * @return the RGB value for the selected color, may be null
	 *
	 * @see PaletteData#getRGBs
	 */
	public RGB getRGB() {
		return rgb;
	}

	/**
	 * Sets the receiver's list of custom colors to be the given array of
	 * <code>RGB</code>s, which may be null to let the platform select a default
	 * when open() is called.
	 *
	 * @param rgbs the array of RGBs, which may be null
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if an RGB in
	 *                                     the rgbs array is null</li>
	 *                                     </ul>
	 *
	 * @since 3.8
	 */
	public void setRGBs(RGB[] rgbs) {
		if (rgbs != null) {
			for (RGB c : rgbs) {
				if (c == null)
					throw new IllegalArgumentException("RGB array contains null");
			}
		}
		this.rgbs = rgbs;
	}

	/**
	 * Returns an array of <code>RGB</code>s which are the list of custom colors
	 * selected by the user in the receiver, or null if no custom colors were
	 * selected.
	 *
	 * @return the array of RGBs, which may be null
	 *
	 * @since 3.8
	 */
	public RGB[] getRGBs() {
		return rgbs;
	}

	// For compatibility with macOS Display#dialogProc
	long changeColor(long id, long sel, long arg0) {
		// No-op since custom Skija dialog handles selection
		return 0;
	}

	void windowWillClose(long id, long sel, long arg0) {
		// No-op for compatibility
	}
}
