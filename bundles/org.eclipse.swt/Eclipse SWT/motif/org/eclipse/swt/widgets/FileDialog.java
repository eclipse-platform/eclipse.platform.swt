package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

/**
 * Instances of this class allow the user to navigate
 * the file system and select or enter a file name.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SAVE, OPEN, MULTI</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public /*final*/ class FileDialog extends Dialog {
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String filterPath = "";
	String fullPath = "";
	String fileName = "";
	boolean cancel = true;
	static final String FILTER = "*";

/**
 * Constructs a new instance of this class given only its
 * parent.
 * <p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the dialog on the currently active
 * display if there is one. If there is no current display, the 
 * dialog is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
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
public FileDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
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
 * for all SWT dialog classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the dialog on the currently active
 * display if there is one. If there is no current display, the 
 * dialog is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
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
public FileDialog (Shell parent, int style) {
	super (parent, style);
}

int activate (int widget, int client, int call) {
	cancel = client == OS.XmDIALOG_CANCEL_BUTTON;
	OS.XtUnmanageChild (widget);
	return 0;
}

/**
 * Returns the path of the first file that was
 * selected in the dialog relative to the filter path,
 * or null if none is available.
 * 
 * @return the relative path of the file
 */
public String getFileName () {
	return fileName;
}

/**
 * Returns the paths of all files that were selected
 * in the dialog relative to the filter path, or null
 * if none are available.
 * 
 * @return the relative paths of the files
 */
public String [] getFileNames () {
	return new String [] {fileName};
}

/**
 * Returns the file extensions which the dialog will
 * use to filter the files it shows.
 *
 * @return the file extensions filter
 */
public String [] getFilterExtensions () {
	return filterExtensions;
}

/**
 * Returns the file names which the dialog will
 * use to filter the files it shows.
 *
 * @return the file name filter
 */
public String [] getFilterNames () {
	return filterNames;
}

/**
 * Returns the path which the dialog will use to filter
 * the files it shows.
 *
 * @return the filter path
 */
public String getFilterPath () {
	return filterPath;
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a string describing the absolute path of the first selected file,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public String open () {

	/* Get the parent */
	boolean destroyContext;
	Display appContext = Display.getCurrent ();
	if (destroyContext = (appContext == null)) appContext = new Display ();
	int display = appContext.xDisplay;
	int parentHandle = appContext.shellHandle;
	if ((parent != null) && (parent.getDisplay () == appContext))
		parentHandle = parent.shellHandle;

	/* Compute the dialog title */	
	/*
	* Feature in Motif.  It is not possible to set a shell
	* title to an empty string.  The fix is to set the title
	* to be a single space.
	*/
	String string = title;
	if (string.length () == 0) string = " ";
	/* Use the character encoding for the default locale */
	byte [] buffer1 = Converter.wcsToMbcs (null, string, true);
	int xmStringPtr1 = OS.XmStringParseText (
		buffer1,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	/* Compute the filter */
	String mask = FILTER;
	if (filterExtensions == null) filterExtensions = new String [0];
	if (filterNames == null) filterNames = new String [0];
	if (filterExtensions.length != 0) {
		/* Motif does not support multiple filters, so ignore them
		 * if there are more than one, or if there is a ; separator.
		 */
		if (filterExtensions.length == 1) {
			String filter = filterExtensions [0];
			if (filter.indexOf (';', 0) == -1) mask = filter;
		}
	}
	/* Use the character encoding for the default locale */
	byte [] buffer2 = Converter.wcsToMbcs (null, mask, true);
	int xmStringPtr2 = OS.XmStringParseText (
		buffer2,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	/* Compute the filter path */
	if (filterPath == null) filterPath = "";
	/* Use the character encoding for the default locale */
	byte [] buffer3 = Converter.wcsToMbcs (null, filterPath, true);
	int xmStringPtr3 = OS.XmStringParseText (
		buffer3,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	/* Create the dialog */
	int [] argList1 = {
		OS.XmNresizePolicy, OS.XmRESIZE_NONE,
		OS.XmNdialogStyle, OS.XmDIALOG_PRIMARY_APPLICATION_MODAL,
		OS.XmNwidth, OS.XDisplayWidth (display, OS.XDefaultScreen (display)) * 4 / 9,
		OS.XmNdialogTitle, xmStringPtr1,
		OS.XmNpattern, xmStringPtr2,
		OS.XmNdirectory, xmStringPtr3,
	};
	/*
	* Feature in Linux.  For some reason, the XmCreateFileSelectionDialog()
	* will not accept NULL for the widget name.  This works fine on the other
	* Motif platforms and in the other XmCreate calls on Linux.  The fix is
	* to pass in a NULL terminated string, not a NULL pointer.
	*/
	byte [] name = new byte [] {0};
	int dialog = OS.XmCreateFileSelectionDialog (parentHandle, name, argList1, argList1.length / 2);
	int child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_HELP_BUTTON);
	if (child != 0) OS.XtUnmanageChild (child);
	OS.XmStringFree (xmStringPtr1);
	OS.XmStringFree (xmStringPtr2);
	OS.XmStringFree (xmStringPtr3);
/*
	string := OSWidget xmStringAt: XmNdirectory handle: dialog.
	OSWidget xmStringAt: XmNdirSpec put: string, fileName handle: dialog.

	"Select the matching file in the list and scroll to show it."
	child := dialog xmFileSelectionBoxGetChild: XmDIALOGLIST.
	child isNull ifFalse: [
		string := OSWidget xmStringAt: XmNdirSpec handle: dialog.
		string := PlatformConverter wcsToMbcs: 0 buffer: string.
		xmString := OSXmString xmStringCreateLocalized: string asPSZ.
		child
			xmListSelectItem: xmString notify: false;
			xmListSetItem: xmString.
		xmString xmStringFree.

		"Bug in Solaris.  For some reason, the horizontal scroll bar in the dialog
		list refuses to be displayed.  This stops the dialog list from scrolling
		horizontally and displaying the file names.  This does not happen on other
		Motif platforms.  The fix is to force the horizontal scroll bar to be displayed
		by explicitly setting the scroll bar display policy."
		OSWidget resourceAt: XmNscrollBarDisplayPolicy put: XmSTATIC handle: child].
*/

	/* Hook the callbacks. */
	Callback callback = new Callback (this, "activate", 3);
	int address = callback.getAddress ();
	OS.XtAddCallback (dialog, OS.XmNokCallback, address, OS.XmDIALOG_OK_BUTTON);
	OS.XtAddCallback (dialog, OS.XmNcancelCallback, address, OS.XmDIALOG_CANCEL_BUTTON);

	/* Open the dialog and dispatch events. */
	cancel = true;
/*
	shell == nil ifFalse: [
		shell minimized ifTrue: [shell minimized: false]].
*/
	OS.XtManageChild (dialog);
	
//BOGUS - should be a pure OS message loop (no SWT AppContext)
	while (OS.XtIsRealized (dialog) && OS.XtIsManaged (dialog))
		if (!appContext.readAndDispatch ()) appContext.sleep ();

	/* Set the new path, file name and filter. */
	fullPath = "";
	if (!cancel) {
		filterPath = fullPath = "";
		int [] argList2 = {OS.XmNdirectory, 0, OS.XmNdirSpec, 0};
		OS.XtGetValues (dialog, argList2, argList2.length / 2);
		int xmString3 = argList2 [1];
		int ptr = OS.XmStringUnparse (
			xmString3,
			null,
			OS.XmCHARSET_TEXT,
			OS.XmCHARSET_TEXT,
			null,
			0,
			OS.XmOUTPUT_ALL);
		if (ptr != 0) {
			int length = OS.strlen (ptr);
			byte [] buffer = new byte [length];
			OS.memmove (buffer, ptr, length);
			OS.XtFree (ptr);
			/* Use the character encoding for the default locale */
			filterPath = new String (Converter.mbcsToWcs (null, buffer));
		}
		OS.XmStringFree (xmString3);
		int xmString4 = argList2 [3];
		ptr = OS.XmStringUnparse (
			xmString4,
			null,
			OS.XmCHARSET_TEXT,
			OS.XmCHARSET_TEXT,
			null,
			0,
			OS.XmOUTPUT_ALL);
		if (ptr != 0) {
			int length = OS.strlen (ptr);
			byte [] buffer = new byte [length];
			OS.memmove (buffer, ptr, length);
			OS.XtFree (ptr);
			/* Use the character encoding for the default locale */
			fullPath = new String (Converter.mbcsToWcs (null, buffer));
		}
		OS.XmStringFree (xmString4);
		int length = filterPath.length ();
		if (length != 0 && filterPath.charAt (length - 1) == '/') {
			filterPath = filterPath.substring (0, length - 1);
			int index = fullPath.lastIndexOf ('/');
			fileName = fullPath.substring (index + 1, fullPath.length ());
		}
	}

	/* Destroy the dialog and update the display. */
	if (OS.XtIsRealized (dialog)) OS.XtDestroyWidget (dialog);
	if (destroyContext) appContext.dispose ();
	callback.dispose ();
	
//	(shell == nil or: [shell isDestroyed not]) ifTrue: [dialog xtDestroyWidget].
//	OSWidget updateDisplay.
//	entryPoint unbind.

	if (cancel) return null;
	return fullPath;
}

/**
 * Set the initial filename which the dialog will
 * select by default when opened to the argument,
 * which may be null.  The name will be prefixed with
 * the filter path when one is supplied.
 * 
 * @param string the file name
 */
public void setFileName (String string) {
	fileName = string;
}

/**
 * Set the file extensions which the dialog will
 * use to filter the files it shows to the argument,
 * which may be null.
 *
 * @param extensions the file extension filter
 */
public void setFilterExtensions (String [] extensions) {
	filterExtensions = extensions;
}

/**
 * Sets the file names which the dialog will
 * use to filter the files it shows to the argument,
 * which may be null.
 *
 * @param names the file name filter
 */
public void setFilterNames (String [] names) {
	filterNames = names;
}

/**
 * Sets the path which the dialog will use to filter
 * the files it shows to the argument, which may be
 * null.
 *
 * @param string the filter path
 */
public void setFilterPath (String string) {
	filterPath = string;
}
}
