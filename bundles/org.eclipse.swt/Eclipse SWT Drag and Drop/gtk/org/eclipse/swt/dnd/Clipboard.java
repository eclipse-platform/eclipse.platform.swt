/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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
package org.eclipse.swt.dnd;


import java.time.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;
import org.eclipse.swt.widgets.*;

/**
 * The <code>Clipboard</code> provides a mechanism for transferring data from one
 * application to another or within an application.
 *
 * <p>IMPORTANT: This class is <em>not</em> intended to be subclassed.</p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#clipboard">Clipboard snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ClipboardExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Clipboard {

	private Display display;

	static long GTKCLIPBOARD;
	static long GTKPRIMARYCLIPBOARD;
	/**
	 * GTK3 only
	 */
	private static long TARGET;

	static {
		if (GTK.GTK4) {
			GTKCLIPBOARD = GDK.gdk_display_get_clipboard(GDK.gdk_display_get_default());
			GTKPRIMARYCLIPBOARD = GDK.gdk_display_get_primary_clipboard(GDK.gdk_display_get_default());
			TARGET = 0;
		} else {
			GTKCLIPBOARD = GTK3.gtk_clipboard_get(GDK.GDK_NONE);
			byte[] buffer = Converter.wcsToMbcs("PRIMARY", true);
			long primary = GDK.gdk_atom_intern(buffer, false);
			GTKPRIMARYCLIPBOARD = GTK3.gtk_clipboard_get(primary);
			buffer = Converter.wcsToMbcs("TARGETS", true);
			TARGET = GDK.gdk_atom_intern(buffer, false);
		}
	}

/**
 * Constructs a new instance of this class.  Creating an instance of a Clipboard
 * may cause system resources to be allocated depending on the platform.  It is therefore
 * mandatory that the Clipboard instance be disposed when no longer required.
 *
 * @param display the display on which to allocate the clipboard
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see Clipboard#dispose
 * @see Clipboard#checkSubclass
 */
public Clipboard(Display display) {
	checkSubclass ();
	if (display == null) {
		display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
	}
	if (display.getThread() != Thread.currentThread()) {
		DND.error(SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.display = display;
}

/**
 * Checks that this class can be subclassed.
 * <p>
 * The SWT class library is intended to be subclassed
 * only at specific, controlled points. This method enforces this
 * rule unless it is overridden.
 * </p><p>
 * <em>IMPORTANT:</em> By providing an implementation of this
 * method that allows a subclass of a class which does not
 * normally allow subclassing to be created, the implementer
 * agrees to be fully responsible for the fact that any such
 * subclass will likely fail between SWT releases and will be
 * strongly platform specific. No support is provided for
 * user-written classes which are implemented in this fashion.
 * </p><p>
 * The ability to subclass outside of the allowed SWT classes
 * is intended purely to enable those not on the SWT development
 * team to implement patches in order to get around specific
 * limitations in advance of when those limitations can be
 * addressed by the team. Subclassing should not be attempted
 * without an intimate and detailed understanding of the hierarchy.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = Clipboard.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}
/**
 * Throws an <code>SWTException</code> if the receiver can not
 * be accessed by the caller. This may include both checks on
 * the state of the receiver and more generally on the entire
 * execution context. This method <em>should</em> be called by
 * widget implementors to enforce the standard SWT invariants.
 * <p>
 * Currently, it is an error to invoke any method (other than
 * <code>isDisposed()</code>) on a widget that has had its
 * <code>dispose()</code> method called. It is also an error
 * to call widget methods from any thread that is different
 * from the thread that created the widget.
 * </p><p>
 * In future releases of SWT, there may be more or fewer error
 * checks and exceptions may be thrown for different reasons.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
protected void checkWidget () {
	Display display = this.display;
	if (display == null) DND.error (SWT.ERROR_WIDGET_DISPOSED);
	if (display.getThread() != Thread.currentThread ()) DND.error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (display.isDisposed()) DND.error(SWT.ERROR_WIDGET_DISPOSED);
}

/**
 * If this clipboard is currently the owner of the data on the system clipboard,
 * clear the contents.  If this clipboard is not the owner, then nothing is done.
 * Note that there are clipboard assistant applications that take ownership of
 * data or make copies of data when it is placed on the clipboard.  In these
 * cases, it may not be possible to clear the clipboard.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void clearContents() {
	clearContents(DND.CLIPBOARD);
}

/**
 * If this clipboard is currently the owner of the data on the specified
 * clipboard, clear the contents.  If this clipboard is not the owner, then
 * nothing is done.
 *
 * <p>Note that there are clipboard assistant applications that take ownership
 * of data or make copies of data when it is placed on the clipboard.  In these
 * cases, it may not be possible to clear the clipboard.</p>
 *
 * <p>The clipboards value is either one of the clipboard constants defined in
 * class <code>DND</code>, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> clipboard constants.</p>
 *
 * @param clipboards to be cleared
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see DND#CLIPBOARD
 * @see DND#SELECTION_CLIPBOARD
 *
 * @since 3.1
 */
public void clearContents(int clipboards) {
	checkWidget();
	if (GTK.GTK4) {
		ClipboardProxyGTK4 proxy = ClipboardProxyGTK4._getInstance(display);
		proxy.clear(this, clipboards, false);
	} else {
		ClipboardProxy proxy = ClipboardProxy._getInstance(display);
		proxy.clear(this, clipboards);
	}
}

/**
 * Disposes of the operating system resources associated with the clipboard.
 * The data will still be available on the system clipboard after the dispose
 * method is called.
 *
 * <p>NOTE: On some platforms the data will not be available once the application
 * has exited or the display has been disposed.</p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 */
public void dispose () {
	if (isDisposed()) return;
	if (display.getThread() != Thread.currentThread()) DND.error(SWT.ERROR_THREAD_INVALID_ACCESS);
	display = null;
}

/**
 * Retrieve the data of the specified type currently available on the system
 * clipboard.  Refer to the specific subclass of <code>Transfer</code> to
 * determine the type of object returned.
 *
 * <p>This method may iterate the event loop to be able to complete. Use
 * {@link #getContentsAsync(Transfer)} to have control over when the event
 * loop runs.</p>
 *
 * <p>The following snippet shows text and RTF text being retrieved from the
 * clipboard:</p>
 *
 *    <pre><code>
 *    Clipboard clipboard = new Clipboard(display);
 *    TextTransfer textTransfer = TextTransfer.getInstance();
 *    String textData = (String)clipboard.getContents(textTransfer);
 *    if (textData != null) System.out.println("Text is "+textData);
 *    RTFTransfer rtfTransfer = RTFTransfer.getInstance();
 *    String rtfData = (String)clipboard.getContents(rtfTransfer);
 *    if (rtfData != null) System.out.println("RTF Text is "+rtfData);
 *    clipboard.dispose();
 *    </code></pre>
 *
 * @param transfer the transfer agent for the type of data being requested
 * @return the data obtained from the clipboard or null if no data of this type is available
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if transfer is null</li>
 * </ul>
 *
 * @see Transfer
 * @see #getContentsAsync(Transfer)
 */
public Object getContents(Transfer transfer) {
	return getContents(transfer, DND.CLIPBOARD);
}

/**
 * Retrieve the data of the specified type currently available on the specified
 * clipboard.  Refer to the specific subclass of <code>Transfer</code> to
 * determine the type of object returned.
 *
 * <p>This method may iterate the event loop to be able to complete. Use
 * {@link #getContentsAsync(Transfer, int)} to have control over when the event
 * loop runs.</p>
 *
 * <p>The following snippet shows text and RTF text being retrieved from the
 * clipboard:</p>
 *
 *    <pre><code>
 *    Clipboard clipboard = new Clipboard(display);
 *    TextTransfer textTransfer = TextTransfer.getInstance();
 *    String textData = (String)clipboard.getContents(textTransfer);
 *    if (textData != null) System.out.println("Text is "+textData);
 *    RTFTransfer rtfTransfer = RTFTransfer.getInstance();
 *    String rtfData = (String)clipboard.getContents(rtfTransfer, DND.CLIPBOARD);
 *    if (rtfData != null) System.out.println("RTF Text is "+rtfData);
 *    clipboard.dispose();
 *    </code></pre>
 *
 * <p>The clipboards value is either one of the clipboard constants defined in
 * class <code>DND</code>, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> clipboard constants.</p>
 *
 * @param transfer the transfer agent for the type of data being requested
 * @param clipboards on which to look for data
 *
 * @return the data obtained from the clipboard or null if no data of this type is available
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if transfer is null</li>
 * </ul>
 *
 * @see Transfer
 * @see DND#CLIPBOARD
 * @see DND#SELECTION_CLIPBOARD
 * @see #getContentsAsync(Transfer, int)
 *
 * @since 3.1
 */
public Object getContents(Transfer transfer, int clipboards) {
	if (GTK.GTK4) {
		CompletableFuture<Object> contentsAsync = gtk4_getContentsAsync(transfer, clipboards);
		try {
			// We limit how long the clipboard has to respond to 5 seconds
			// based on similar decisions in GTK3, see
			// https://github.com/eclipse-platform/eclipse.platform.swt/blob/d2de8e31f846d655949e81fa006ad2ebcc542b2f/bundles/org.eclipse.swt/Eclipse%20SWT%20Drag%20and%20Drop/gtk/org/eclipse/swt/dnd/Clipboard.java#L719
			return GTK4GlibFuture.get(display, contentsAsync, Duration.ofSeconds(5));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return null;
		} catch (ExecutionException | TimeoutException e) {
			// Bug 241957: In case of timeout take clipboard ownership to unblock future calls
			ClipboardProxyGTK4 proxy = ClipboardProxyGTK4._getInstance(display);
			proxy.clear(this, clipboards, true);
			return null;
		}
	}

	return gtk3_getContents(transfer, clipboards);
}

private Object gtk3_getContents(Transfer transfer, int clipboards) {
	checkWidget();
	if (transfer == null) DND.error(SWT.ERROR_NULL_ARGUMENT);
	long selection_data = 0;
	int[] typeIds = transfer.getTypeIds();
	boolean textTransfer = transfer.getTypeNames()[0].equals("UTF8_STRING");
	Object result = null;
	for (int i = 0; i < typeIds.length; i++) {
		if ((clipboards & DND.CLIPBOARD) != 0) {
			selection_data = gtk3_clipboard_wait_for_contents(GTKCLIPBOARD, typeIds[i]);
		}
		if (selection_data == 0 && (clipboards & DND.SELECTION_CLIPBOARD) != 0) {
			selection_data = gtk3_clipboard_wait_for_contents(GTKPRIMARYCLIPBOARD, typeIds[i]);
		}
		if (selection_data != 0) {
			TransferData tdata = new TransferData();
			tdata.type = GTK3.gtk_selection_data_get_data_type(selection_data);
			tdata.pValue = GTK3.gtk_selection_data_get_data(selection_data);
			tdata.length = GTK3.gtk_selection_data_get_length(selection_data);
			tdata.format = GTK3.gtk_selection_data_get_format(selection_data);
			result = transfer.nativeToJava(tdata);
			GTK3.gtk_selection_data_free(selection_data);
			selection_data = 0;
			if (result != null || !textTransfer) {
				break;
			}
		}
	}
	return result;
}

/**
 * Retrieve the data of the specified type currently available on the system
 * clipboard.  Refer to the specific subclass of <code>Transfer</code> to
 * determine the type of object returned.
 *
 * <p>This method is asynchronous and may require the SWT event loop to
 * iterate before the future will complete.</p>
 *
 * <p>The following snippet shows text being retrieved from the clipboard:</p>
 *
 *    <pre><code>
 *    Clipboard clipboard = new Clipboard(display);
 *    TextTransfer textTransfer = TextTransfer.getInstance();
 *    CompletableFuture&lt;Object&gt; future = clipboard.getContentsAsync(textTransfer);
 *    future.thenAccept(textData -&gt; System.out.println("Text is "+textData));
 *    clipboard.dispose();
 *    </code></pre>
 *
 * @param transfer the transfer agent for the type of data being requested
 * @return the future whose value will resolve to the data obtained from the
 *     clipboard or will resolve to null if no data of this type is available
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if transfer is null</li>
 * </ul>
 *
 * @see Transfer
 * @since 3.132
 */
public CompletableFuture<Object> getContentsAsync(Transfer transfer) {
	return getContentsAsync(transfer, DND.CLIPBOARD);
}

/**
 * Retrieve the data of the specified type currently available on the specified
 * clipboard.  Refer to the specific subclass of <code>Transfer</code> to
 * determine the type of object returned.
 *
 * <p>This method is asynchronous and may require the SWT event loop to
 * iterate before the future will complete.</p>
 *
 * <p>The following snippet shows text being retrieved from the clipboard:</p>
 *
 *    <pre><code>
 *    Clipboard clipboard = new Clipboard(display);
 *    TextTransfer textTransfer = TextTransfer.getInstance();
 *    CompletableFuture&lt;Object&gt; future = clipboard.getContentsAsync(textTransfer, DND.CLIPBOARD);
 *    future.thenAccept(textData -&gt; System.out.println("Text is "+textData));
 *    clipboard.dispose();
 *    </code></pre>
 *
 * <p>The clipboards value is either one of the clipboard constants defined in
 * class <code>DND</code>, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> clipboard constants.</p>
 *
 * @param transfer the transfer agent for the type of data being requested
 * @param clipboards on which to look for data
 *
 * @return the future whose value will resolve to the data obtained from the
 *     clipboard or will resolve to null if no data of this type is available
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if transfer is null</li>
 * </ul>
 *
 * @see Transfer
 * @see DND#CLIPBOARD
 * @see DND#SELECTION_CLIPBOARD
 *
 * @since 3.132
 */
public CompletableFuture<Object> getContentsAsync(Transfer transfer, int clipboards) {
	if (GTK.GTK4) {
		return gtk4_getContentsAsync(transfer, clipboards);
	}

	return CompletableFuture.completedFuture(gtk3_getContents(transfer, clipboards));
}

private CompletableFuture<Object> gtk4_getContentsAsync(Transfer transfer, int clipboards) {
	checkWidget();
	if (transfer == null)
		DND.error(SWT.ERROR_NULL_ARGUMENT);

	ClipboardProxyGTK4 proxy = ClipboardProxyGTK4._getInstance(display);
	return proxy.getData(this, transfer, clipboards);
}

/**
 * Returns <code>true</code> if the clipboard has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the clipboard.
 * When a clipboard has been disposed, it is an error to
 * invoke any other method using the clipboard.
 * </p>
 *
 * @return <code>true</code> when the widget is disposed and <code>false</code> otherwise
 *
 * @since 3.0
 */
public boolean isDisposed () {
	return (display == null);
}

/**
 * Place data of the specified type on the system clipboard.  More than one type
 * of data can be placed on the system clipboard at the same time.  Setting the
 * data clears any previous data from the system clipboard, regardless of type.
 *
 * <p>NOTE: On some platforms, the data is immediately copied to the system
 * clipboard but on other platforms it is provided upon request.  As a result,
 * if the application modifies the data object it has set on the clipboard, that
 * modification may or may not be available when the data is subsequently
 * requested.</p>
 *
 * <p>The following snippet shows text and RTF text being set on the copy/paste
 * clipboard:
 * </p>
 *
 * <pre><code>
 * 	Clipboard clipboard = new Clipboard(display);
 *	String textData = "Hello World";
 *	String rtfData = "{\\rtf1\\b\\i Hello World}";
 *	TextTransfer textTransfer = TextTransfer.getInstance();
 *	RTFTransfer rtfTransfer = RTFTransfer.getInstance();
 *	Transfer[] transfers = new Transfer[]{textTransfer, rtfTransfer};
 *	Object[] data = new Object[]{textData, rtfData};
 *	clipboard.setContents(data, transfers);
 *	clipboard.dispose();
 * </code></pre>
 *
 * @param data the data to be set in the clipboard
 * @param dataTypes the transfer agents that will convert the data to its
 * platform specific format; each entry in the data array must have a
 * corresponding dataType
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if data is null or datatypes is null
 *          or the length of data is not the same as the length of dataTypes</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *  @exception SWTError <ul>
 *    <li>ERROR_CANNOT_SET_CLIPBOARD - if the clipboard is locked or otherwise unavailable</li>
 * </ul>
 *
 * <p>NOTE: ERROR_CANNOT_SET_CLIPBOARD should be an SWTException, since it is a
 * recoverable error, but can not be changed due to backward compatibility.</p>
 */
public void setContents(Object[] data, Transfer[] dataTypes) {
	setContents(data, dataTypes, DND.CLIPBOARD);
}

/**
 * Place data of the specified type on the specified clipboard.  More than one
 * type of data can be placed on the specified clipboard at the same time.
 * Setting the data clears any previous data from the specified
 * clipboard, regardless of type.
 *
 * <p>NOTE: On some platforms, the data is immediately copied to the specified
 * clipboard but on other platforms it is provided upon request.  As a result,
 * if the application modifies the data object it has set on the clipboard, that
 * modification may or may not be available when the data is subsequently
 * requested.</p>
 *
 * <p>The clipboards value is either one of the clipboard constants defined in
 * class <code>DND</code>, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> clipboard constants.</p>
 *
 * <p>The following snippet shows text and RTF text being set on the copy/paste
 * clipboard:
 * </p>
 *
 * <pre><code>
 * 	Clipboard clipboard = new Clipboard(display);
 *	String textData = "Hello World";
 *	String rtfData = "{\\rtf1\\b\\i Hello World}";
 *	TextTransfer textTransfer = TextTransfer.getInstance();
 *	RTFTransfer rtfTransfer = RTFTransfer.getInstance();
 *	Transfer[] transfers = new Transfer[]{textTransfer, rtfTransfer};
 *	Object[] data = new Object[]{textData, rtfData};
 *	clipboard.setContents(data, transfers, DND.CLIPBOARD);
 *	clipboard.dispose();
 * </code></pre>
 *
 * @param data the data to be set in the clipboard
 * @param dataTypes the transfer agents that will convert the data to its
 * platform specific format; each entry in the data array must have a
 * corresponding dataType
 * @param clipboards on which to set the data
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if data is null or datatypes is null
 *          or the length of data is not the same as the length of dataTypes</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *  @exception SWTError <ul>
 *    <li>ERROR_CANNOT_SET_CLIPBOARD - if the clipboard is locked or otherwise unavailable</li>
 * </ul>
 *
 * <p>NOTE: ERROR_CANNOT_SET_CLIPBOARD should be an SWTException, since it is a
 * recoverable error, but can not be changed due to backward compatibility.</p>
 *
 * @see DND#CLIPBOARD
 * @see DND#SELECTION_CLIPBOARD
 *
 * @since 3.1
 */
public void setContents(Object[] data, Transfer[] dataTypes, int clipboards) {
	checkWidget();
	if (data == null || dataTypes == null || data.length != dataTypes.length || data.length == 0) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	for (int i = 0; i < data.length; i++) {
		if (data[i] == null || dataTypes[i] == null || !dataTypes[i].validate(data[i])) {
			DND.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	if (GTK.GTK4) {
		ClipboardProxyGTK4 proxy = ClipboardProxyGTK4._getInstance(display);
		if (!proxy.setData(this, data, dataTypes, clipboards)) {
			DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
		}
	} else {
		ClipboardProxy proxy = ClipboardProxy._getInstance(display);
		if (!proxy.setData(this, data, dataTypes, clipboards)) {
			DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
		}
	}
}

/**
 * Returns an array of the data types currently available on the system
 * clipboard. Use with Transfer.isSupportedType.
 *
 * @return array of data types currently available on the system clipboard
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Transfer#isSupportedType
 *
 * @since 3.0
 */
public TransferData[] getAvailableTypes() {
	return getAvailableTypes(DND.CLIPBOARD);
}

/**
 * Returns an array of the data types currently available on the specified
 * clipboard. Use with Transfer.isSupportedType.
 *
 * <p>The clipboards value is either one of the clipboard constants defined in
 * class <code>DND</code>, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> clipboard constants.</p>
 *
 * @param clipboards from which to get the data types
 * @return array of data types currently available on the specified clipboard
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Transfer#isSupportedType
 * @see DND#CLIPBOARD
 * @see DND#SELECTION_CLIPBOARD
 *
 * @since 3.1
 */
public TransferData[] getAvailableTypes(int clipboards) {
	checkWidget();
	if(GTK.GTK4) {
		return gtk4_getAvailableTypes(clipboards);
	}

	TransferData[] result = null;
	if ((clipboards & DND.CLIPBOARD) != 0) {
		int[] types = gtk3_getAvailableTypes(GTKCLIPBOARD);
		result = new TransferData[types.length];
		for (int i = 0; i < types.length; i++) {
			result[i] = new TransferData();
			result[i].type = types[i];
		}
	}
	if ((clipboards & DND.SELECTION_CLIPBOARD) != 0) {
		int[] types = gtk3_getAvailableTypes(GTKPRIMARYCLIPBOARD);
		int offset = 0;
		if (result != null) {
			TransferData[] newResult = new TransferData[result.length + types.length];
			System.arraycopy(result,0, newResult, 0, result.length);
			offset = result.length;
			result = newResult;
		} else {
			result = new TransferData[types.length];
		}
		for (int i = 0; i < types.length; i++) {
			result[offset+i] = new TransferData();
			result[offset+i].type = types[i];
		}
	}
	return result == null ? new TransferData[0] : result;
}

/**
 * Returns a platform specific list of the data types currently available on the
 * system clipboard.
 *
 * <p>Note: <code>getAvailableTypeNames</code> is a utility for writing a Transfer
 * sub-class.  It should NOT be used within an application because it provides
 * platform specific information.</p>
 *
 * @return a platform specific list of the data types currently available on the
 * system clipboard
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String[] getAvailableTypeNames() {
	checkWidget();
	if(GTK.GTK4) {
		return gtk4_getAvailableTypeNames();
	}
	int[] types1 = gtk3_getAvailableTypes(GTKCLIPBOARD);
	int[] types2 = gtk3_getAvailableTypes(GTKPRIMARYCLIPBOARD);
	String[] result = new String[types1.length + types2.length];
	int count = 0;
	for (int i = 0; i < types1.length; i++) {
		long pName = GDK.gdk_atom_name(types1[i]);
		if (pName == 0) {
			continue;
		}
		byte[] buffer = new byte [C.strlen(pName)];
		C.memmove (buffer, pName, buffer.length);
		OS.g_free (pName);
		result[count++] = "GTKCLIPBOARD "+new String (Converter.mbcsToWcs (buffer));
	}
	for (int i = 0; i < types2.length; i++) {
		long pName = GDK.gdk_atom_name(types2[i]);
		if (pName == 0) {
			continue;
		}
		byte[] buffer = new byte [C.strlen(pName)];
		C.memmove (buffer, pName, buffer.length);
		OS.g_free (pName);
		result[count++] = "GTKPRIMARYCLIPBOARD "+new String (Converter.mbcsToWcs (buffer));
	}
	if (count < result.length){
		String[] temp = new String[count];
		System.arraycopy(result, 0, temp, 0, count);
		result = temp;
	}
	return result;
}

private TransferData[] gtk4_getAvailableTypes(int clipboards) {
	/*
	 * The first time we see a type name (mime type) may be when asking the
	 * clipboard. The user may not load the specific Transfer class until after
	 * getting the available types.
	 *
	 * Therefore we need to register any type names we see with the transfer system.
	 */
	return Arrays.stream(gtk4_getAvailableTypeNames(clipboards)).map(Transfer::registerType).map((type) -> {
		TransferData transferData = new TransferData();
		transferData.type = type;
		return transferData;
	}).toArray(TransferData[]::new);
}

private String[] gtk4_getAvailableTypeNames() {
	Set<String> all = new LinkedHashSet<>();
	Arrays.stream(gtk4_getAvailableTypeNames(DND.CLIPBOARD)).map(n -> "GTKCLIPBOARD "+ n).forEachOrdered(all::add);
	Arrays.stream(gtk4_getAvailableTypeNames(DND.SELECTION_CLIPBOARD)).map(n -> "GTKPRIMARYCLIPBOARD "+ n).forEachOrdered(all::add);
	return all.toArray(String[]::new);
}

private String[] gtk4_getAvailableTypeNames(int clipboards) {
	long gtkClipboard = ((clipboards & DND.CLIPBOARD) != 0) ? Clipboard.GTKCLIPBOARD : Clipboard.GTKPRIMARYCLIPBOARD;

	List<String> names = new ArrayList<>();

	long formats = GTK4.gdk_clipboard_get_formats(gtkClipboard);

	{
		long[] n_gtypes = new long[1];
		long gtypes = GTK4.gdk_content_formats_get_gtypes(formats, n_gtypes);
		if (gtypes != 0) {
			int gtypes_length = (int) n_gtypes[0];

			for (int i = 0; i < gtypes_length; i++) {
				long[] ptr = new long[1];
				C.memmove(ptr, gtypes + i * C.PTR_SIZEOF, C.PTR_SIZEOF);
				long gtype = ptr[0];
				if (gtype == 0) {
					// End reached of null terminated array
					break;
				}
				if (gtype != OS.G_TYPE_INVALID()) {
					long g_type_name = OS.g_type_name(gtype);
					if (g_type_name == 0) {
						continue;
					}
					String gtypeName = Converter.cCharPtrToJavaString(g_type_name, false);
					names.add(gtypeName);
				}
			}
		}
	}

	{
		long[] n_mime_types = new long[1];
		long mime_types = GTK4.gdk_content_formats_get_mime_types(formats, n_mime_types);
		if (mime_types != 0) {
			int mime_types_length = (int) n_mime_types[0];

			for (int i = 0; i < mime_types_length; i++) {
				long[] ptr = new long[1];
				C.memmove(ptr, mime_types + i * C.PTR_SIZEOF, C.PTR_SIZEOF);
				long mime_type = ptr[0];
				if (mime_type == 0) {
					// End reached of null terminated array
					break;
				}

				String typeName = Converter.cCharPtrToJavaString(mime_type, false);
				if (!typeName.isBlank()) {
					names.add(typeName);
				}
			}
		}
	}

	return names.toArray(String[]::new);
}

private int[] gtk3_getAvailableTypes(long clipboard) {
	int[] types = new int[0];
	long selection_data = gtk3_clipboard_wait_for_contents(clipboard, TARGET);
	if (selection_data != 0) {
		try {
			int length = GTK3.gtk_selection_data_get_length(selection_data);
			int format = GTK3.gtk_selection_data_get_format(selection_data);
			long data = GTK3.gtk_selection_data_get_data(selection_data);
			if (length != 0) {
				types = new int[length * 8 / format];
				C.memmove(types, data, length);
			}
		} finally {
			GTK3.gtk_selection_data_free(selection_data);
		}
	}
	return types;
}

private long gtk3_clipboard_wait_for_contents(long clipboard, long target) {
	long startTime = System.currentTimeMillis();
	String key = "org.eclipse.swt.internal.gtk.dispatchEvent";
	Display display = this.display;
	display.setData(key, new int[]{GDK.GDK_PROPERTY_NOTIFY, GDK.GDK_SELECTION_CLEAR, GDK.GDK_SELECTION_REQUEST, GDK.GDK_SELECTION_NOTIFY});
	long selection_data = GTK3.gtk_clipboard_wait_for_contents(clipboard, target);
	display.setData(key, null);
	long duration = System.currentTimeMillis() - startTime;
	if (selection_data == 0 && duration > 5000) {
		// Bug 241957: In case of timeout take clipboard ownership to unblock future calls
		ClipboardProxy._getInstance(display).setData(this, new String[] {" "},
				new Transfer[] { TextTransfer.getInstance() },
				clipboard == GTKCLIPBOARD ? DND.CLIPBOARD : DND.SELECTION_CLIPBOARD);
	}
	return selection_data;
}
}
