/*
 * (c) Copyright IBM Corp. 2002.
 * All Rights Reserved
 *
 * Andre Weinand, OTI - Initial version
 */
package org.eclipse.swt.internal.carbon;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

public class MacUtil {

	public static boolean DEBUG;
	public static boolean REVERSE;	// if true 
	
	static {
		DEBUG= false;
		REVERSE= true;
	}
	
	public static void embedControl(int controlHandle, int parentControlHandle) {
		if (REVERSE) {
			int count= OS.CountSubControls(parentControlHandle);
			OS.EmbedControl(controlHandle, parentControlHandle);
			int[] outControl= new int[1];
			for (int i= 0; i < count; i++) {
				if (OS.GetIndexedSubControl(parentControlHandle, (short)(1), outControl) == 0)
					OS.EmbedControl(outControl[0], parentControlHandle);
				else
					throw new SWTError();
			}
		} else {
			OS.EmbedControl(controlHandle, parentControlHandle);
		}
	}
	
	public static int createSeparator(int parentHandle, int style) {
  		int handle= MacUtil.newControl(parentHandle, (short)0, (short)0, (short)100, OS.kControlSeparatorLineProc);
		if ((style & SWT.HORIZONTAL) != 0)
			OS.SizeControl(handle, (short) 20, (short) 1);
		else
			OS.SizeControl(handle, (short) 1, (short) 20);	
		return handle;
	}
	
	public static Point toControl(int cHandle, Point point) {
		MacPoint mp= new MacPoint(point);
		
		int savedPort= OS.GetPort();
		try {
			OS.SetPortWindowPort(OS.GetControlOwner(cHandle));
			OS.GlobalToLocal(mp.getData());
		} finally {
			OS.SetPort(savedPort);
		}
	
		MacRect bounds= new MacRect();
		OS.GetControlBounds(cHandle, bounds.getData());
		
		Point p= mp.toPoint();
		p.x-= bounds.getX();
		p.y-= bounds.getY();
		return p;
	}
	
	public static Point toDisplay(int cHandle, Point point) {
		MacRect bounds= new MacRect();
		OS.GetControlBounds(cHandle, bounds.getData());
		MacPoint mp= new MacPoint(point.x+bounds.getX(), point.y+bounds.getY());
		int port= OS.GetPort();
		try {
			OS.SetPortWindowPort(OS.GetControlOwner(cHandle));
			OS.LocalToGlobal(mp.getData());
		} finally {
			OS.SetPort(port);
		}
		return mp.toPoint();
	}
	
	// clipping regions
	
	public static void getVisibleRegion(int cHandle, int result, boolean includingTop) {
		int tmpRgn= OS.NewRgn();
		
		getControlRegion(cHandle, OS.kControlEntireControl, result);
		
		int parent= cHandle;
		while ((parent= MacUtil.getSuperControl(parent)) != 0) {
			getControlRegion(parent, OS.kControlContentMetaPart, tmpRgn);
			OS.SectRgn(result, tmpRgn, result);
		}
		
		if (includingTop) {
			int n= OS.CountSubControls(cHandle);
			if (n > 0) {
				//System.out.println("have children on top");
				int[] outHandle= new int[1];
				for (int i= 0; i < n; i++) {
					int index= REVERSE ? (n-i) : (i+1);
					if (OS.GetIndexedSubControl(cHandle, (short)index, outHandle) == 0) {
						if (OS.IsControlVisible(outHandle[0])) {
							getControlRegion(outHandle[0], OS.kControlStructureMetaPart, tmpRgn);
							OS.DiffRgn(result, tmpRgn, result);
						}
					} else
						throw new SWTError();
				}
			}
		}
				
		OS.DisposeRgn(tmpRgn);
	}
	
	private static void getControlRegion(int cHandle, short part, int rgn) {
		if (true) {
			short[] bounds= new short[4];
			OS.GetControlBounds(cHandle, bounds);
			OS.RectRgn(rgn, bounds);
		} else {
			OS.GetControlRegion(cHandle, OS.kControlContentMetaPart, rgn);
		}
	}

	// Hit detection on the Mac is reversed and doesn't consider clipping,
	// so we have to do it ourselves

	public static int findControlUnderMouse(MacPoint where, int wHandle, short[] cpart) {
		Point w= where.toPoint();
		int cHandle= find(OS.GetRootControl(wHandle), null, new MacRect(), w);
		if (cHandle != 0) {
			cpart[0]= OS.TestControl(cHandle, where.getData());
			//System.out.println("findControlUnderMouse: " + cpart[0]);
		}
		return cHandle;
	}
	
	private static int find(int cHandle, Rectangle parentBounds, MacRect tmp, Point where) {
	
		if (! OS.IsControlVisible(cHandle))
			return 0;
		if (! OS.IsControlActive(cHandle))
			return 0;

		OS.GetControlBounds(cHandle, tmp.getData());
		Rectangle rr= tmp.toRectangle();
		if (parentBounds != null)
			rr= parentBounds.intersection(rr);

		int n= OS.CountSubControls(cHandle);
		int[] outHandle= new int[1];
		for (int i= 0; i < n; i++) {
			int index= REVERSE ? (n-i) : (i+1);
			if (OS.GetIndexedSubControl(cHandle, (short)index, outHandle) == 0) {
				int result= find(outHandle[0], rr, tmp, where);
				if (result != 0)
					return result;
			}
		}

		if (rr.contains(where))
			return cHandle;
		return 0;
	}

	public static String getStringAndRelease(int sHandle) {
		int length= OS.CFStringGetLength(sHandle);
		char[] buffer= new char[length];
		OS.CFStringGetCharacters(sHandle, 0, length, buffer);
		OS.CFRelease(sHandle);
		return new String(buffer);
	}
	
	public static byte[] Str255(String s) {
		int l= 0;
		if (s != null)
			l= s.length();
		if (l > 255) {
			throw new SWTError(SWT.ERROR_INVALID_RANGE);
			//System.out.println("MacUtil.Str255: string length > 255");
		}
		byte[] b= new byte[l+1];
		b[0]= (byte) l;
		for (int i= 0; i < l; i++)
			b[i+1]= (byte) s.charAt(i);
		return b;
	}
	
	public static String toString(byte[] str255) {
		int n= str255[0];
		char[] c= new char[n];
		for (int i= 0; i < n; i++)
			c[i]= (char) str255[i+1];
		return new String(c);
	}

	/**
	 * Create a new control and embed it in the given parent control.
	 */
	public static int newControl(int parentControlHandle, short init, short min, short max, short procID) {
		int windowHandle= OS.GetControlOwner(parentControlHandle);
		int controlHandle= OS.NewControl(windowHandle, false, init, min, max, procID);
		embedControl(controlHandle, parentControlHandle);
		initLocation(controlHandle);
		OS.ShowControl(controlHandle);
		return controlHandle;
	}
	
	public static int createDrawingArea(int parentControlHandle, boolean visible, int width, int height, int border) {
		int windowHandle= OS.GetControlOwner(parentControlHandle);
		int features= OS.kControlSupportsEmbedding | OS.kControlSupportsFocus | OS.kControlGetsFocusOnClick;
		int controlHandle= OS.NewControl(windowHandle, false, (short)features, (short)0, (short)0, OS.kControlUserPaneProc);
		OS.SizeControl(controlHandle, (short)width, (short)height);
		embedControl(controlHandle, parentControlHandle);
		initLocation(controlHandle);
		if (visible)
			OS.ShowControl(controlHandle);
		return controlHandle;
	}

	public static int createDrawingArea(int parentControlHandle, int width, int height, int border) {
		return createDrawingArea(parentControlHandle, true, width, height, border);
	}
	
	public static void initLocation(int cHandle) {
		int parent= getSuperControl(cHandle);
		short[] bounds= new short[4];
		OS.GetControlBounds(parent, bounds);
		short x= bounds[1];
		short y= bounds[0];
		if (x > 0 || y > 0)
			OS.MoveControl(cHandle, x, y);
	}
	
	/**
	 * Returns the parent of the given control.
	 */
	public static int getSuperControl(int cHandle) {
		int[] parentHandle= new int[1];
		OS.GetSuperControl(cHandle, parentHandle);
		return parentHandle[0];
	}

	public static void dump(int matchHandle) {
		int wHandle= OS.GetControlOwner(matchHandle);
		dump(OS.GetRootControl(wHandle), 0, matchHandle);
		System.out.println();
	}
	
	public static void dump(int cHandle, int level, int matchHandle) {
		for (int x= 0; x < level; x++)
			System.out.print("  ");
		Widget w= WidgetTable.get(cHandle);
		if (w != null)
			System.out.print(w);
		else
			System.out.print(cHandle);
			
		MacRect bounds= new MacRect();
		OS.GetControlBounds(cHandle, bounds.getData());
		System.out.print(" " + bounds.toRectangle());
		
		if (cHandle == matchHandle)
			System.out.println(" ******************");
		else
			System.out.println();
			
		int n= OS.CountSubControls(cHandle);
		int[] outHandle= new int[1];
		for (int i= 0; i < n; i++) {
			if (OS.GetIndexedSubControl(cHandle, (short)(i+1), outHandle) == 0)
				dump(outHandle[0], level+1, matchHandle);
		}
	}
	
	public static Point computeSize(int handle) {
		if (OS.IsValidControlHandle(handle)) {
			MacRect rect= new MacRect();
			short[] base= new short[1];
			OS.GetBestControlRect(handle, rect.getData(), base);
			if (rect.isEmpty())
				System.out.println("MacUtil.computeSize: 0 size");
			return rect.getSize();
		}
		System.out.println("MacUtil.computeSize: unknown handle type");
		return new Point(50, 50);
	}
	
	public static int getDisplayWidth() {
		MacRect bounds= new MacRect();
		OS.GetAvailableWindowPositioningBounds(OS.GetMainDevice(), bounds.getData());
		return bounds.getWidth();
	}
	
	public static int getDisplayHeight() {
		MacRect bounds= new MacRect();
		OS.GetAvailableWindowPositioningBounds(OS.GetMainDevice(), bounds.getData());
		return bounds.getHeight();
	}
	
	public static String toString(int i) {
		StringBuffer sb= new StringBuffer();
		sb.append((char)((i & 0xff000000) >> 24));
		sb.append((char)((i & 0x00ff0000) >> 16));
		sb.append((char)((i & 0x0000ff00) >> 8));
		sb.append((char)((i & 0x000000ff) >> 0));
		return sb.toString();
	}
}
