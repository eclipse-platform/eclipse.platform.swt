package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

/**	 
 * Instances of this class are <em>internal SWT implementation</em>
 * objects which provide a mapping between the typed and untyped 
 * listener mechanisms that SWT supports.
 * <p>
 * <b>IMPORTANT:</b> This class is <em>not</em> part of the SWT
 * public API. It is marked public only so that it can be shared
 * within the packages provided by SWT. It should never be
 * referenced from application code.
 * </p>
 *
 * @see Listener
 *
 * @private
 */
public class TypedListener implements Listener {
	
	/**
	 * The receiver's event listener
	 */
	protected SWTEventListener eventListener;

/**
 * Constructs a new instance of this class for the given event listener.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the SWT
 * public API. It is marked public only so that it can be shared
 * within the packages provided by SWT. It should never be
 * referenced from application code.
 * </p>
 *
 * @param listener the event listener to store in the receiver
 */
public TypedListener (SWTEventListener listener) {
	eventListener = listener;
}

/**
 * Returns the receiver's event listener.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the SWT
 * public API. It is marked public only so that it can be shared
 * within the packages provided by SWT. It should never be
 * referenced from application code.
 * </p>
 *
 * @return the receiver's event listener
 */
public SWTEventListener getEventListener () {
	return eventListener;
}

/**
 * Handles the given event.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the SWT
 * public API. It is marked public only so that it can be shared
 * within the packages provided by SWT. It should never be
 * referenced from application code.
 * </p>
 */
public void handleEvent (Event e) {
	switch (e.type) {
		case SWT.Paint: {
			/* Field set by Control */
			PaintEvent event = new PaintEvent (e);
			((PaintListener) eventListener).paintControl (event);
			e.gc = event.gc;
			break;
		}			
		case SWT.Selection: {
			/* Fields set by Sash */
			SelectionEvent event = new SelectionEvent (e);
			((SelectionListener) eventListener).widgetSelected (event);			
			e.x = event.x;
			e.y = event.y;
			e.doit = event.doit;
			break;
		}
		case SWT.DefaultSelection: {
			((SelectionListener)eventListener).widgetDefaultSelected(new SelectionEvent(e));
			break;
		}
		case SWT.Dispose: {
			((DisposeListener) eventListener).widgetDisposed(new DisposeEvent(e));
			break;
		}
		case SWT.FocusIn: {
			((FocusListener) eventListener).focusGained(new FocusEvent(e));
			break;
		}
		case SWT.FocusOut: {
			((FocusListener) eventListener).focusLost(new FocusEvent(e));
			break;
		}
		case SWT.Hide: {
			((MenuListener) eventListener).menuHidden(new MenuEvent(e));
			break;
		}
		case SWT.Show: {
			((MenuListener) eventListener).menuShown(new MenuEvent(e));
			break;
		}
		case SWT.KeyDown: {
			((KeyListener) eventListener).keyPressed(new KeyEvent(e));
			break;
		}
		case SWT.KeyUp: {
			((KeyListener) eventListener).keyReleased(new KeyEvent(e));
			break;
		}
		case SWT.MouseDown: {
			((MouseListener) eventListener).mouseDown(new MouseEvent(e));
			break;
		}
		case SWT.MouseUp: {
			((MouseListener) eventListener).mouseUp(new MouseEvent(e));
			break;
		}
		case SWT.MouseDoubleClick: {
			((MouseListener) eventListener).mouseDoubleClick(new MouseEvent(e));
			break;
		}
		case SWT.MouseMove: {
			((MouseMoveListener) eventListener).mouseMove(new MouseEvent(e));
			return;
		}
		case SWT.Resize: {
			((ControlListener) eventListener).controlResized(new ControlEvent(e));
			break;
		}
		case SWT.Move: {
			((ControlListener) eventListener).controlMoved(new ControlEvent(e));
			break;
		}
		case SWT.Close: {
			/* Fields set by Decorations */
			ShellEvent event = new ShellEvent (e);
			((ShellListener) eventListener).shellClosed(event);
			e.doit = event.doit;
			break;
		}
		case SWT.Activate: {
			((ShellListener) eventListener).shellActivated(new ShellEvent(e));
			break;
		}
		case SWT.Deactivate: {
			((ShellListener) eventListener).shellDeactivated(new ShellEvent(e));
			break;
		}
		case SWT.Iconify: {
			((ShellListener) eventListener).shellIconified(new ShellEvent(e));
			break;
		}
		case SWT.Deiconify: {
			((ShellListener) eventListener).shellDeiconified(new ShellEvent(e));
			break;
		}
		case SWT.Expand: {
			((TreeListener) eventListener).treeExpanded(new TreeEvent(e));
			break;
		}
		case SWT.Collapse: {
			((TreeListener) eventListener).treeCollapsed(new TreeEvent(e));
			break;
		}
		case SWT.Modify: {
			((ModifyListener) eventListener).modifyText(new ModifyEvent(e));
			break;
		}
		case SWT.Verify: {
			/* Fields set by Text, RichText */
			VerifyEvent event = new VerifyEvent (e);
			((VerifyListener) eventListener).verifyText (event);
			e.text = event.text;
			e.doit = event.doit;
			break;
		}
		case SWT.Help: {
			((HelpListener) eventListener).helpRequested (new HelpEvent (e));
			break;
		}
		case SWT.Arm: {
			((ArmListener) eventListener).widgetArmed (new ArmEvent (e));
			break;
		}
		case SWT.MouseExit: {
			((MouseTrackListener) eventListener).mouseExit (new MouseEvent (e));
			break;
		}
		case SWT.MouseEnter: {
			((MouseTrackListener) eventListener).mouseEnter (new MouseEvent (e));
			break;
		}
		case SWT.MouseHover: {
			((MouseTrackListener) eventListener).mouseHover (new MouseEvent (e));
			break;
		}
		case SWT.Traverse: {
			/* Fields set by Control */
			TraverseEvent event = new TraverseEvent (e);
			((TraverseListener) eventListener).keyTraversed (event);
			e.detail = event.detail;
			e.doit = event.doit;
			break;
		}
		
	}
}

}
