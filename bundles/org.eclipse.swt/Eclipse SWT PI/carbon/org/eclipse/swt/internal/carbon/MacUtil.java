/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Andre Weinand, OTI - Initial version
 */
package org.eclipse.swt.internal.carbon;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

public class MacUtil {

	public final static boolean DEBUG;
	public final static boolean JAGUAR;
	public final static boolean USE_MENU_ICONS;
	/** Prevent use of standard Mac shortcuts Cmd-Q, Cmd-H */
	public final static boolean KEEP_MAC_SHORTCUTS;
	
	static final char MNEMONIC = '&';
	
	static {
		DEBUG= false;
		USE_MENU_ICONS= true;
		KEEP_MAC_SHORTCUTS= true;
		JAGUAR= System.getProperty("os.version").startsWith("10.2");
	}
	
	public static int indexOf(int parentHandle, int handle) {
		int n= countSubControls(parentHandle);
		int[] outControl= new int[1];
		
		for (int i= 0; i < n; i++)
			if (OS.GetIndexedSubControl(parentHandle, (short)(n-i), outControl) == 0)
				if (outControl[0] == handle)
					return i;
		return -1;
	}
	
	public static int OSEmbedControl(int controlHandle, int parentControlHandle) {
		if (JAGUAR) {
			/* 
			 * in Jaguar it is no longer possible to use EmbedControl for rearranging children in a composite.
			 * The fix is to temporarily move a child to its root control fisrt and then to its
			 * new position.
			 */
			int[] root= new int[1];
			int wHandle= OS.GetControlOwner(parentControlHandle);
			OS.GetRootControl(wHandle, root);
			int rc= OS.EmbedControl(controlHandle, root[0]);
			if (rc != OS.kNoErr)
				return rc;
		}
		return OS.EmbedControl(controlHandle, parentControlHandle);
	}
	
	/**
	 * Inserts the given child at position in the parent.
	 * If pos is out of range the child is added at the end.
	 */
	public static void embedControl(int controlHandle, int parentControlHandle, int pos) {
		
		int n= countSubControls(parentControlHandle);
		
		// add at end
		if (OSEmbedControl(controlHandle, parentControlHandle) != OS.kNoErr)
			System.out.println("MacUtil.embedControl: could not embed control in parent");
			
		if (pos < 0 || pos > n)
			pos= n;
		
		int[] outControl= new int[1];
		for (int i= 0; i < pos; i++) {
			if (OS.GetIndexedSubControl(parentControlHandle, (short)(n-pos+1), outControl) == 0)
				if (OSEmbedControl(outControl[0], parentControlHandle) != OS.kNoErr)
					System.out.println("MacUtil.embedControl: couldn't move control to end");
		}
		
		// verify correct position
		n++;
		for (int i= 0; i < n; i++) {
			int index= (n-i);
			int[] outHandle= new int[1];
			if (OS.GetIndexedSubControl(parentControlHandle, (short)index, outHandle) == 0) {	// indices are 1 based
				if (outHandle[0] == controlHandle) {
					if (i != pos)
						System.out.println("MacUtil.embedControl: creation at position nyi (is: "+i+" should: "+ pos+")");
					return;
				}
			}
		}
		System.out.println("MacUtil.embedControl: new child not found");
	}
	
	public static void embedControl(int controlHandle, int parentControlHandle) {
		embedControl(controlHandle, parentControlHandle, -1);
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
		
		int wHandle= OS.GetControlOwner(cHandle);
		int port= OS.GetWindowPort(wHandle);
		OS.QDGlobalToLocalPoint(port, mp.getData());
	
		MacRect bounds= new MacRect();
		OS.GetControlBounds(cHandle, bounds.getData());
		
		Point p= mp.toPoint();
		p.x-= bounds.getX();
		p.y-= bounds.getY();
		
		/*
		float[] p2= new float[2];
		p2[0]= mp.getX();
		p2[1]= mp.getY();
		OS.HIViewConvertPoint(p2, 0, cHandle);
		System.out.println("MacUtil.toControl: " + p + "  " + p2[0] + " " + p2[1]);
		*/
		
		return p;
	}
	
	public static Point toDisplay(int cHandle, Point point) {
		MacRect bounds= new MacRect();
		OS.GetControlBounds(cHandle, bounds.getData());
		MacPoint mp= new MacPoint(point.x+bounds.getX(), point.y+bounds.getY());
		
		int wHandle= OS.GetControlOwner(cHandle);
		int port= OS.GetWindowPort(wHandle);
		OS.QDLocalToGlobalPoint(port, mp.getData());
		
		return mp.toPoint();
	}
	
	// clipping regions
	
	public static int getVisibleRegion(int cHandle, int result, boolean includingTop) {
		int tmpRgn= OS.NewRgn();
		
		getControlRegion(cHandle, OS.kControlEntireControl, result);

		int parent= cHandle;
		while ((parent= MacUtil.getSuperControl(parent)) != 0) {
			getControlRegion(parent, OS.kControlContentMetaPart, tmpRgn);
			OS.SectRgn(result, tmpRgn, result);
		}
		
		if (includingTop) {
			int n= countSubControls(cHandle);
			if (n > 0) {
				//System.out.println("have children on top");
				int[] outHandle= new int[1];
				for (int i= n; i > 0; i--) {
					if (OS.GetIndexedSubControl(cHandle, (short)i, outHandle) == 0) {
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
                
		return OS.kNoErr;
	}
	
	private static void getControlRegion(int cHandle, short part, int rgn) {
		if (true) {
			short[] bounds= new short[4];
			OS.GetControlBounds(cHandle, bounds);
			OS.RectRgn(rgn, bounds);
		} else {
			OS.GetControlRegion(cHandle, part, rgn);
		}
	}

	// Hit detection on the Mac is reversed and doesn't consider clipping,
	// so we have to do it ourselves

	public static int findControlUnderMouse(MacPoint where, int wHandle, short[] cpart) {
		Point w= where.toPoint();
		int[] rootHandle= new int[1];
		int rc= OS.GetRootControl(wHandle, rootHandle);
		if (rc != OS.kNoErr) {
			System.out.println("MacUtil.findControlUnderMouse: " + rc);
			return 0;
		}
		int cHandle= find(rootHandle[0], null, new MacRect(), w);
		if (cHandle != 0 && cpart != null && cpart.length > 0) {
			cpart[0]= OS.TestControl(cHandle, where.getData());
			//System.out.println("findControlUnderMouse: " + cpart[0]);
		}
		return cHandle;
	}
	
	private static int countSubControls(int cHandle) {
		short[] cnt= new short[1];
		switch (OS.CountSubControls(cHandle, cnt)) {
		case OS.kNoErr:
			return cnt[0];			
		case OS.errControlIsNotEmbedder:
			break;
		default:
			System.out.println("MacUtil.countSubControls");
			break;
		}
		return 0;
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

		int n= countSubControls(cHandle);
		if (n > 0) {
			int[] outHandle= new int[1];
			for (int i= n; i > 0; i--) {
				if (OS.GetIndexedSubControl(cHandle, (short)i, outHandle) == 0) {
					int result= find(outHandle[0], rr, tmp, where);
					if (result != 0)
						return result;
				}
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

	public static int OSType(String s) {
		return ((s.charAt(0) & 0xff) << 24) | ((s.charAt(1) & 0xff) << 16) | ((s.charAt(2) & 0xff) << 8) | (s.charAt(3) & 0xff);
	}

	/**
	 * Create a new control and embed it in the given parent control.
	 */
	public static int newControl(int parentControlHandle, short procID) {
		return newControl(parentControlHandle, -1, (short)0, (short)0, (short)0, procID);
	}
	
	/**
	 * Create a new control and embed it in the given parent control.
	 */
	public static int newControl(int parentControlHandle, short init, short min, short max, short procID) {
		return newControl(parentControlHandle, -1, init, min, max, procID);
	}
	
	/**
	 * Create a new control and embed it in the given parent control.
	 */
	public static int newControl(int parentControlHandle, int pos, short init, short min, short max, short procID) {
		int windowHandle= OS.GetControlOwner(parentControlHandle);
		int controlHandle= OS.NewControl(windowHandle, false, init, min, max, procID);
		embedControl(controlHandle, parentControlHandle, pos);
		initLocation(controlHandle);
		OS.ShowControl(controlHandle);
		return controlHandle;
	}
	
	public static int createDrawingArea(int parentControlHandle, int pos, boolean visible, int width, int height, int border) {
		int windowHandle= OS.GetControlOwner(parentControlHandle);
		int features= OS.kControlSupportsEmbedding | OS.kControlSupportsFocus | OS.kControlGetsFocusOnClick;
		int controlHandle= OS.NewControl(windowHandle, false, (short)features, (short)0, (short)0, OS.kControlUserPaneProc);
		OS.SizeControl(controlHandle, (short)width, (short)height);
		embedControl(controlHandle, parentControlHandle, pos);
		initLocation(controlHandle);
		if (visible)
			OS.ShowControl(controlHandle);
		return controlHandle;
	}
	
	public static int createDrawingArea(int parentControlHandle, boolean visible, int width, int height, int border) {
		return createDrawingArea(parentControlHandle, -1, visible, width, height, border);
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
	 * Returns the parent of the given control or null if the control is a root control.
	 */
	public static int getSuperControl(int cHandle) {
        
		int wHandle= OS.GetControlOwner(cHandle);
		if (wHandle == 0) {
			//System.out.println("MacUtil.getSuperControl: GetControlOwner error");
			return 0;
		}
		int[] rootHandle= new int[1];
		OS.GetRootControl(wHandle, rootHandle);
		if (cHandle == rootHandle[0])
			return 0;
                
		int[] parentHandle= new int[1];
		int rc= OS.GetSuperControl(cHandle, parentHandle);
		if (rc != OS.kNoErr)
			System.out.println("MacUtil.getSuperControl: " + rc);
		return parentHandle[0];
	}

	public static void dump(int matchHandle) {
		int wHandle= OS.GetControlOwner(matchHandle);
		int[] rootHandle= new int[1];
		OS.GetRootControl(wHandle, rootHandle);
		dump(rootHandle[0], 0, matchHandle);
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
                    
		int n= countSubControls(cHandle);
		if (n > 0) {
			int[] outHandle= new int[1];
			for (int i= 0; i < n; i++) {
				if (OS.GetIndexedSubControl(cHandle, (short)(i+1), outHandle) == 0)
					dump(outHandle[0], level+1, matchHandle);
			}
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
	
	public static String removeMnemonics(String s) {
		if (s != null) {
			int l= s.length();
			if (l > 0) {
				char[] buf= new char[l];
				int j= 0;
				for (int i= 0; i < l; i++) {
					char c= s.charAt(i);
					if (c != MNEMONIC)
						buf[j++]= c;
				}
				return new String(buf, 0, j);
			}
		}
		return s;
	}
	
	public static void RGBBackColor(int packed) {
		if ((packed & 0xff000000) == 0) {
			OS.RGBBackColor((short)(((packed >> 16) & 0xFF) * 257),
							(short)(((packed >> 8) & 0xFF) * 257),
							(short)(((packed) & 0xFF) * 257));
		} else {
			OS.RGBBackColor((short)0xFFFF, (short)0xFFFF, (short)0xFFFF);
		}
	}
	
	public static void RGBForeColor(int packed) {
		if ((packed & 0xff000000) == 0) {
			OS.RGBForeColor((short)(((packed >> 16) & 0xFF) * 257),
							(short)(((packed >> 8) & 0xFF) * 257),
							(short)(((packed) & 0xFF) * 257));
		} else {
			OS.RGBForeColor((short)0xFFFF, (short)0xFFFF, (short)0xFFFF);
		}
	}
	
	public static int[] getDataBrowserItems(int dataBrowserHandle, int containerID, int state, boolean recurse) {
		int resultHandle= 0;
		try {
			resultHandle= OS.NewHandle(0);
			if (OS.GetDataBrowserItems(dataBrowserHandle, containerID, recurse, state, resultHandle) == OS.kNoErr) {
				int itemCount= OS.GetHandleSize(resultHandle) / 4;	// sizeof(int)
				if (itemCount > 0) {	
					int resultIDs[]= new int[itemCount];
					OS.getHandleData(resultHandle, resultIDs);
					return resultIDs;
				}
			}
		} finally {
			OS.DisposeHandle(resultHandle);
		}
		return new int[0];
	}

	public static int[] getSelectionIDs(int dataBrowserHandle, int containerID, boolean recurse) {
		return getDataBrowserItems(dataBrowserHandle, containerID, OS.kDataBrowserItemIsSelected, recurse);
	}

}
