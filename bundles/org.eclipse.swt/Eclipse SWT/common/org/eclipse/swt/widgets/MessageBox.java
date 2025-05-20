/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
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
 *     Raghunandana Murthappa (Advantest) - Platform independent implementation.
 *******************************************************************************/
package org.eclipse.swt.widgets;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

/**
 * Instances of this class are used to inform or warn the user.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ICON_ERROR, ICON_INFORMATION, ICON_QUESTION, ICON_WARNING, ICON_WORKING</dd>
 * <dd>OK, OK | CANCEL</dd>
 * <dd>YES | NO, YES | NO | CANCEL</dd>
 * <dd>RETRY | CANCEL</dd>
 * <dd>ABORT | RETRY | IGNORE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ICON_ERROR, ICON_INFORMATION, ICON_QUESTION,
 * ICON_WARNING and ICON_WORKING may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class MessageBox extends Dialog {

	private String message = "";
	private Map<Integer, String> labels;

/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public MessageBox(Shell parent) {
	this(parent, SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 *
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of dialog to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#ICON_ERROR
 * @see SWT#ICON_INFORMATION
 * @see SWT#ICON_QUESTION
 * @see SWT#ICON_WARNING
 * @see SWT#ICON_WORKING
 * @see SWT#OK
 * @see SWT#CANCEL
 * @see SWT#YES
 * @see SWT#NO
 * @see SWT#ABORT
 * @see SWT#RETRY
 * @see SWT#IGNORE
 */
public MessageBox(Shell parent, int style) {
	super(parent, checkStyle(parent, checkStyle(style)));
	checkSubclass();
}

/**
 * Returns the dialog's message, or an empty string if it does not have one.
 * The message is a description of the purpose for which the dialog was opened.
 * This message will be visible in the dialog while it is open.
 *
 * @return the message
 */
public String getMessage() {
	return message;
}

/**
 * Sets the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 *
 * @param string the message
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 */
public void setMessage(String string) {
	if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	message = string;
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return the ID of the button that was selected to dismiss the
 *         message box (e.g. SWT.OK, SWT.CANCEL, etc.)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public int open() {
	Shell dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	if (title != null) {
		dialog.setText(title);
	}

	Image iconImage = getIcon(dialog.getDisplay(), style);
	int numOfColumns = iconImage == null ? 1 : 2;
	dialog.setLayout(new GridLayout(numOfColumns, false));

	if (iconImage != null) {
		Label iconLabel = new Label(dialog, SWT.NONE);
		iconLabel.setImage(iconImage);
		iconLabel.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, false));
	}
	Label messageLabel = new Label(dialog, SWT.WRAP);
	messageLabel.setText(message);
	messageLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

	String[] buttonLabels = getButtonLabels(style);
	Composite buttonComp = new Composite(dialog, SWT.NONE);
	buttonComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, numOfColumns, 1));
	numOfColumns = buttonLabels.length;
	buttonComp.setLayout(new GridLayout(numOfColumns, true));

	Color labelBackground = messageLabel.getBackground();
	dialog.setBackground(labelBackground);
	buttonComp.setBackground(labelBackground);

	final int[] result = { SWT.CANCEL };

	for (String label : buttonLabels) {
		Button button = new Button(buttonComp, SWT.PUSH);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		button.setText(label);
		button.addListener(SWT.Selection, e -> {
			result[0] = mapButtonResult(label);
			dialog.close();
		});
	}

	dialog.pack();
	dialog.open();

	Display display = dialog.getDisplay();
	while (!dialog.isDisposed()) {
		if (!display.readAndDispatch()) {
			display.sleep();
		}
	}

	return result[0];
}

private Image getIcon(Display display, int style) {
	int iconID = -1;
	if ((style & SWT.ICON_ERROR) != 0) {
		iconID = SWT.ICON_ERROR;
	} else if ((style & SWT.ICON_INFORMATION) != 0) {
		iconID = SWT.ICON_INFORMATION;
	} else if ((style & SWT.ICON_QUESTION) != 0) {
		iconID = SWT.ICON_QUESTION;
	} else if ((style & SWT.ICON_WARNING) != 0) {
		iconID = SWT.ICON_WARNING;
	} else if ((style & SWT.ICON_WORKING) != 0) {
		iconID = SWT.ICON_INFORMATION;
	}

	if (iconID != -1) {
		return display.getSystemImage(iconID);
	}
	return null;
}

private String[] getButtonLabels(int style) {
	if ((style & SWT.OK) != 0 && (style & SWT.CANCEL) != 0) {
		return new String[] { getLabel(SWT.OK, "OK"), getLabel(SWT.CANCEL, "Cancel") };
	}
	if ((style & SWT.YES) != 0 && (style & SWT.NO) != 0 && (style & SWT.CANCEL) != 0) {
		return new String[] { getLabel(SWT.YES, "Yes"), getLabel(SWT.NO, "No"), getLabel(SWT.CANCEL, "Cancel") };
	}
	if ((style & SWT.YES) != 0 && (style & SWT.NO) != 0) {
		return new String[] { getLabel(SWT.YES, "Yes"), getLabel(SWT.NO, "No") };
	}
	if ((style & SWT.RETRY) != 0 && (style & SWT.CANCEL) != 0) {
		return new String[] { getLabel(SWT.RETRY, "Retry"), getLabel(SWT.CANCEL, "Cancel") };
	}
	if ((style & SWT.ABORT) != 0 && (style & SWT.RETRY) != 0 && (style & SWT.IGNORE) != 0) {
		return new String[] { getLabel(SWT.ABORT, "Abort"), getLabel(SWT.RETRY, "Retry"),
				getLabel(SWT.IGNORE, "Ignore") };
	}
	return new String[] { getLabel(SWT.OK, "OK") };
}

private String getLabel(int buttonId, String defaultLabel) {
	if (labels != null && labels.containsKey(buttonId) && labels.get(buttonId) != null) {
		return labels.get(buttonId);
	}
	return defaultLabel;
}

private int mapButtonResult(String label) {
	if (labels != null && !labels.isEmpty()) {
		for (Map.Entry<Integer, String> entry : labels.entrySet()) {
			String customLabel = entry.getValue();
			if (customLabel != null && customLabel.equals(label)) {
				return entry.getKey();
			}
		}
	}

	return switch (label) {
		case "OK" -> SWT.OK;
		case "Cancel" -> SWT.CANCEL;
		case "Yes" -> SWT.YES;
		case "No" -> SWT.NO;
		case "Retry" -> SWT.RETRY;
		case "Abort" -> SWT.ABORT;
		case "Ignore" -> SWT.IGNORE;
		default -> SWT.CANCEL;
	};
}

private static int checkStyle(int style) {
	int mask = (SWT.YES | SWT.NO | SWT.OK | SWT.CANCEL | SWT.ABORT | SWT.RETRY | SWT.IGNORE);
	int bits = style & mask;
	if (bits == SWT.OK || bits == SWT.CANCEL || bits == (SWT.OK | SWT.CANCEL)) return style;
	if (bits == SWT.YES || bits == SWT.NO || bits == (SWT.YES | SWT.NO) || bits == (SWT.YES | SWT.NO | SWT.CANCEL)) return style;
	if (bits == (SWT.RETRY | SWT.CANCEL) || bits == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) return style;
	style = (style & ~mask) | SWT.OK;
	return style;
}

/**
 * Set custom text for <code>MessageDialog</code>'s buttons:
 *
 * @param labels a <code>Map</code> where a valid 'key' is from below listed
 *               styles:<ul>
 * <li>SWT#OK</li>
 * <li>SWT#CANCEL</li>
 * <li>SWT#YES</li>
 * <li>SWT#NO</li>
 * <li>SWT#ABORT</li>
 * <li>SWT#RETRY</li>
 * <li>SWT#IGNORE</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if labels is null</li>
 * </ul>
 * @since 3.121
 */
public void setButtonLabels(Map<Integer, String> labels) {
	if (labels == null) error(SWT.ERROR_NULL_ARGUMENT);
	this.labels = labels;
}
}
