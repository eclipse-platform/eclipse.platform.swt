package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.*;

/**
 *
 * Class DND contains all the constants used in defining a 
 * DragSource or a DropTarget.
 *
 */


public class DND {
	
	/**
	 * Drag and Drop Operation: no drag/drop operation is performed
	 */
	public final static int DROP_NONE = 0;
	/**
	 * Drag and Drop Operation: a copy of the data in the drag source is added to the drop target
	 */
	public final static int DROP_COPY = 1;
	/**
	 * Drag and Drop Operation: the data is added to the drop target and removed from the drag source
	 */
	public final static int DROP_MOVE = 2;
	/**
	 * Drag and Drop Operation: the drop target makes a link to the data in the drag source
	 */
	public final static int DROP_LINK = 4;
	
	public final static int DROP_TARGET_MOVE = 5;
	/**
	 * DragSource Event: the drop has successfully completed or has been terminated (such as hitting the ESC key);
	 *                   perform cleanup such as removing data on a move operation
	 */
	public static final int DragEnd		= 2000;
	/**
	 * DragSource Event: the data to be dropped is required from the drag source
	 */
	public static final int DragSetData = 2001;
	/**
	 * DropTarget Event: the cursor has entered the drop target boundaries
	 */
	public static final int DragEnter	= 2002;
	/**
	 * DropTarget Event: the cursor has left the drop target boundaries
	 */
	public static final int DragLeave	= 2003;
	/**
	 * DropTarget Event: the cursor is moving over the drop target
	 */
	public static final int	DragOver	= 2004;
	/**
	 * DropTarget Event: the operation being performed has changed 
	 *                   (usually due to the user changing the selected key while dragging)
	 */
	public static final int DragOperationChanged = 2005;
	/**
	 * DropTarget Event: the data is being dropped
	 */
	public static final int	Drop		= 2006;
	/**
	 * DropTarget Event: the drop target is given a last chance to modify the drop
	 */
	public static final int	DropAccept	= 2007;
	/**
	 * DragSource Event: a drag is about to begin
	 */
	public static final int	DragStart	= 2008;

	public static final int FEEDBACK_NONE = 0;
	public static final int FEEDBACK_SELECT = 1;
	public static final int FEEDBACK_INSERT_BEFORE = 2;
	public static final int FEEDBACK_INSERT_AFTER = 4;
	public static final int FEEDBACK_SCROLL = 8;
	public static final int FEEDBACK_EXPAND = 16;
	

	/**
	 * Error code for SWTError - drag source can not be initialized
	 */
	public static final int ERROR_CANNOT_INIT_DRAG = 2000;
	/**
	 * Error code for SWTError - drop target cannot be initialized
	 */
	public static final int ERROR_CANNOT_INIT_DROP = 2001;
	/**
	 * Error code for SWTError - Data can not be set on system clipboard
	 */
	public static final int ERROR_CANNOT_SET_CLIPBOARD = 2002;
	

	static final String INIT_DRAG_MESSAGE =  "Can not initialize Drag";
	static final String INIT_DROP_MESSAGE =  "Can not initialize Drop";
	static final String CANNOT_SET_CLIPBOARD_MESSAGE =  "Can not set data in clipboard";
	
public static void error (int code) {
	error (code, 0);
}

public static void error (int code, int hresult) {		
	switch (code) {		
		/* OS Failure/Limit (fatal, may occur only on some platforms) */
		case DND.ERROR_CANNOT_INIT_DRAG:{
			String msg = DND.INIT_DRAG_MESSAGE;
			if (hresult != 0) msg += "result = "+hresult;
			throw new SWTError (code, msg);
		}
		case DND.ERROR_CANNOT_INIT_DROP:{
			String msg = DND.INIT_DROP_MESSAGE;
			if (hresult != 0) msg += "result = "+hresult;
			throw new SWTError (code, msg);
		}
		case DND.ERROR_CANNOT_SET_CLIPBOARD:{
			String msg = DND.CANNOT_SET_CLIPBOARD_MESSAGE;
			if (hresult != 0) msg += "result = "+hresult;
			throw new SWTError (code, msg);
		}
	}
			
	/* Unknown/Undefined Error */
	SWT.error(code);
}

}
