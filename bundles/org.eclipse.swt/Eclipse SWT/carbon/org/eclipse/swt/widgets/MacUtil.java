/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Andre Weinand, OTI - Initial version
 */
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.CGPoint;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Point;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.RGBColor;

public class MacUtil {

	public final static boolean USE_MENU_ICONS;
	
	/** Prevent use of standard Mac shortcuts Cmd-Q, Cmd-H */
	public final static boolean KEEP_MAC_SHORTCUTS;
	public final static boolean FULL_KBD_NAV;
		
	static final char MNEMONIC = '&';
		
	static {
		USE_MENU_ICONS= true;
		KEEP_MAC_SHORTCUTS= true;
		FULL_KBD_NAV= true;
	}
		
	//---- HIView utilities
	
	/*
	static void dump(int control, int level) {
		for (int j= 0; j < level; j++)
			System.out.print(' ');
		
		int id= OS.HIObjectCopyClassID(control);
		String s= getStringAndRelease(id);
		System.out.print(control + "(" + s + "): ");
		
		if ("com.apple.hiwindowcontentview".equals(s)) {
			int[] id0= new int[2];
			id0[0]= OSType("wind");
			id0[1]= 1;
			OS.SetControlID(control, id0);
		}
					
		Rect b= new Rect();
		OS.GetControlBounds(control, b);
		System.out.print(new Rectangle(b.left, b.top, b.right-b.left, b.bottom-b.top));

		int[] id2= new int[2];
		OS.GetControlID(control, id2);
		System.out.println(" " + toString(id2[0]) + " " + id2[1]);
		
		int n= countSubControls(control);
		int[] outControl= new int[1];
		for (int i= 0; i < n; i++) {
			if (getChild(control, outControl, n, i) == OS.noErr) {
				dump(outControl[0], level+1);
			}
		}
	}
	*/

	/**
	 * Returns the HIView that represents the contents of the given window or
	 * the root HIView if no contents HIView could be found.
	 */
	private static int getContentView(int windowHandle) {
		int rootControl= OS.HIViewGetRoot(windowHandle);
		if (rootControl == 0) {
			System.out.println("getContentView: could not find root control");
			return 0;
		}
		int[] contentView= new int[1];
		int rc= OS.HIViewFindByID(rootControl, OS.kHIViewWindowContentID(), contentView);
		if (rc != OS.noErr) {
			if (rc != OS.errUnknownControl)
				System.out.println("getContentView: errNo: " + rc);
			return rootControl;
		}
		return contentView[0];
	}
	
	//---- control utilities
			
	static int getChild(int controlHandle, int[] tmp, int n, int i) {
		int index= (n-1 - i);
		int status= OS.GetIndexedSubControl(controlHandle, (short)(index+1), tmp);
		if (status != OS.noErr)
			System.out.println("MacUtil.getChild: error");
		return status;
	}
		
	static int indexOf(int parentHandle, int handle) {
		int n= countSubControls(parentHandle);
		int[] outControl= new int[1];
		for (int i= 0; i < n; i++) {
			if (getChild(parentHandle, outControl, n, i) == OS.noErr)
				if (outControl[0] == handle)
					return i;
		}
		return -1;
	}
	
	/**
	 * Inserts the given child at position in the parent.
	 * If pos is out of range the child is added at the end (below all other).
	 */
	static void insertControl(int controlHandle, int parent, int pos) {
		
		int parentHandle= parent;
		
		// make sure that parentHandle really refers to a Control
		if (OS.IsValidControlHandle(parent)) {
			// it is a Control
			parentHandle= parent;
		} else if (OS.IsValidWindowPtr(parentHandle)) {
			// it is a Window: get the Window's content Control
			parentHandle= getContentView(parent);
			if (parentHandle == 0)
				parentHandle= getContentView(parent);
		} else {
			System.out.println("MacUtil.insertControl: parentHandle is neither control nor window");
		}
		int n= countSubControls(parentHandle);
		
		int should= pos;
		if (should < 0 || should > n)
			should= n;
		
		boolean add= false;
		if (getSuperControl(controlHandle) != parentHandle) {
			add= true;
		} else {
			System.out.println("MacUtil.insertControl: already there");
			if (n == 1)
				return;
		}
		
		if (n == 0) {
			if (OS.HIViewAddSubview(parentHandle, controlHandle) != OS.noErr)
				System.out.println("MacUtil.insertControl: error in HIViewAddSubview");	
			pos= 0;
		} else {
			if (pos >= 0 && pos < n) {
				int[] where= new int[1];
				getChild(parentHandle, where, n, pos);
				if (add)
					OS.HIViewAddSubview(parentHandle, controlHandle);
				OS.HIViewSetZOrder(controlHandle, OS.kHIViewZOrderAbove, where[0]);
			} else {
				if (add)
					OS.HIViewAddSubview(parentHandle, controlHandle);
				if (OS.HIViewSetZOrder(controlHandle, OS.kHIViewZOrderBelow, 0) != OS.noErr)
					System.out.println("error 2");
				pos= n;
			}
		}
	
		// verify correct position
		int i= indexOf(parentHandle, controlHandle);
		if (i != should)
			System.out.println("MacUtil.insertControl: is: "+i+" should: "+ should  + " n:" + n + " add: " + add);
	}
	
	/**
	 * Returns the bounds of a view relative to its window's local coordinate system.
	 */
	public static void getControlBounds(int cHandle, Rect bounds) {
		OS.GetControlBounds(cHandle, bounds);
		org.eclipse.swt.graphics.Point offset= getLocation(cHandle);
		bounds.left+= offset.x;
		bounds.top+= offset.y;
		bounds.right+= offset.x;
		bounds.bottom+= offset.y;
	}

	/**
	 * Returns location of given Control relative to the window.	 */
	static org.eclipse.swt.graphics.Point getLocation(int cHandle) {
		
//		int windowHandle= OS.GetControlOwner(cHandle);
//		CGPoint p= new CGPoint();
//		OS.HIViewConvertPoint(p, cHandle, 0 /*getContentView(windowHandle)*/);
//		int xx= (int) p.x;
//		int yy= (int) p.y;
		
		int x= 0, y= 0;
		Rect tmp= new Rect();
		int parent= cHandle;
		while (true) {
			parent= getSuperControl(parent);
			if (parent == 0)
				break;
			if (getSuperControl(parent) == 0)
				break;
			OS.GetControlBounds(parent, tmp);
			x+= tmp.left;
			y+= tmp.top;
		}
		
//		if (x != xx || y != yy)
//			System.out.println("MacUtil.getLocation: differ " + x+"/"+y + " " + xx+"/"+yy);
		return new org.eclipse.swt.graphics.Point(x, y);
	}

	static void getControlBounds(int cHandle, short part, int rgn) {
		OS.GetControlRegion(cHandle, part, rgn);
		Rect tmp= new Rect();
		int parent= cHandle;
		while (true) {
			parent= getSuperControl(parent);
			if (parent == 0)
				break;
			if (getSuperControl(parent) == 0)
				break;
			OS.GetControlBounds(parent, tmp);
			OS.OffsetRgn(rgn, (short)tmp.left, (short)tmp.top);
		}
	}
	
	public static int getVisibleRegion(int cHandle, int result, boolean includingTop) {
		
		getControlRegion(cHandle, OS.kControlEntireControl, result);
		
		int tmpRgn= OS.NewRgn();
		int parent= cHandle;
		
		while (true) {
			parent= getSuperControl(parent);
			if (parent == 0)
				break;
			if (getSuperControl(parent) == 0)
				break;
			getControlRegion(parent, OS.kControlContentMetaPart, tmpRgn);
			OS.SectRgn(result, tmpRgn, result);
		}
		
		if (includingTop) {
			int n= countSubControls(cHandle);
			if (n > 0) {
				//System.out.println("have children on top");
				int[] outHandle= new int[1];
				for (int i= 0; i < n; i++) {
					int index= i; // was: n-1-i
					if (OS.GetIndexedSubControl(cHandle, (short)(index+1), outHandle) == 0) {	// indices are 1 based
						if (OS.IsControlVisible(outHandle[0])) {
							if (true)
								getControlRegion(outHandle[0], OS.kControlStructureMetaPart, tmpRgn);
							else
								OS.GetControlRegion(outHandle[0], (short)OS.kControlEntireControl, tmpRgn);
							OS.DiffRgn(result, tmpRgn, result);
						}
					} else
						throw new SWTError();
				}
			}
		}
		OS.DisposeRgn(tmpRgn);
		
		return OS.noErr;
	}
	
	static org.eclipse.swt.graphics.Point toControl(int cHandle, org.eclipse.swt.graphics.Point point) {
		Point mp= new Point();
		OS.SetPt(mp, (short)point.x, (short)point.y);
		// convert from screen to window coordinates
		int wHandle= OS.GetControlOwner(cHandle);
		int port= OS.GetWindowPort(wHandle);
		OS.QDGlobalToLocalPoint(port, mp);
		CGPoint p= new CGPoint();
		p.x= mp.h;
		p.y= mp.v;
		OS.HIViewConvertPoint(p, getContentView(wHandle), cHandle);
		return new org.eclipse.swt.graphics.Point((int)p.x, (int)p.y);
	}
	
	static org.eclipse.swt.graphics.Point toDisplay(int cHandle, org.eclipse.swt.graphics.Point point) {
		int wHandle= OS.GetControlOwner(cHandle);
		// convert from control to window coordinates
		CGPoint p= new CGPoint();
		p.x= point.x;
		p.y= point.y;
		OS.HIViewConvertPoint(p, cHandle, getContentView(wHandle));
		Point mp= new Point();
		OS.SetPt(mp, (short)p.x, (short)p.y);
		// convert from window to screen coordinates
		int port= OS.GetWindowPort(wHandle);
		OS.QDLocalToGlobalPoint(port, mp);
		return new org.eclipse.swt.graphics.Point(mp.h, mp.v);
	}
		
	private static void getControlRegion(int cHandle, int part, int rgn) {
		Rect bounds= new Rect();
		getControlBounds(cHandle, bounds);
		OS.RectRgn(rgn, bounds);
	}
	
	// Hit detection on the Mac is reversed and doesn't consider clipping,
	// so we have to do it ourselves

	static int findControlUnderMouse(int wHandle, MacEvent me, short[] cpart) {
		
		int root= OS.HIViewGetRoot(wHandle);
		int[] ov= new int[1];
		
		if (me.getKind() == OS.kEventMouseMoved) {
			org.eclipse.swt.internal.carbon.Point pp= me.getWhere();
			org.eclipse.swt.graphics.Point w= toControl(root, new org.eclipse.swt.graphics.Point(pp.h, pp.v));
			OS.HIViewGetSubviewHit(root, new float[] { w.x, w.y }, true, ov);
		} else {
			OS.HIViewGetViewForMouseEvent(root, me.getEventRef(), ov);
		}

		int control= ov[0];
		
		if (control != 0) {
			Widget ww= WidgetTable.get(control);					
			if (cpart != null && ww != null) {
				org.eclipse.swt.internal.carbon.Point where= me.getWhere();
				org.eclipse.swt.graphics.Point w= toControl(control, new org.eclipse.swt.graphics.Point(where.h, where.v));
				OS.HIViewGetPartHit(control, new float[] { w.x, w.y }, cpart);
			}
		}

		return control;
	}

	private static int countSubControls(int cHandle) {
		short[] cnt= new short[1];
		int status= OS.CountSubControls(cHandle, cnt);
		switch (status) {
		case OS.noErr:
			return cnt[0];			
		case OS.errControlIsNotEmbedder:
			//System.out.println("MacUtil.countSubControls: errControlIsNotEmbedder");
			break;
		case -30599: // OS.controlHandleInvalidErr
			System.out.println("MacUtil.countSubControls: controlHandleInvalidErr");
			break;
		default:
			System.out.println("MacUtil.countSubControls: " + status);
			break;
		}
		return 0;
	}
	
	/**
	 * Returns the parent of the given control or null if the control is the window's content control.
	 */
	static int getSuperControl(int cHandle) {
		
		int wHandle= OS.GetControlOwner(cHandle);
		if (wHandle == 0) {
			//System.out.println("MacUtil.getSuperControl: GetControlOwner error");
			return 0;
		}
		if (cHandle == getContentView(wHandle))
			return 0;
                
		int[] parentHandle= new int[1];
		int rc= OS.GetSuperControl(cHandle, parentHandle);
		if (rc != OS.noErr)
			System.out.println("MacUtil.getSuperControl: " + rc);
		return parentHandle[0];
	}
	
	static org.eclipse.swt.graphics.Point computeSize(int handle) {
		if (OS.IsValidControlHandle(handle)) {
			Rect rect= new Rect();
			short[] base= new short[1];
			OS.GetBestControlRect(handle, rect, base);
			if (OS.EmptyRect(rect))
				System.out.println("MacUtil.computeSize: 0 size");
			return new org.eclipse.swt.graphics.Point(rect.right - rect.left, rect.bottom - rect.top);
		}
		System.out.println("MacUtil.computeSize: unknown handle type");
		return new org.eclipse.swt.graphics.Point(50, 50);
	}
	
	//---- Strings utilities
	
	static String getStringAndRelease(int sHandle) {
		int length= OS.CFStringGetLength(sHandle);
		char[] buffer= new char[length];
		CFRange range = new CFRange();
		range.length = length;
		OS.CFStringGetCharacters(sHandle, range, buffer);
		OS.CFRelease(sHandle);
		return new String(buffer);
	}
	
	/**
	 * Converts the given String into a Pascal Str255 type.	 */
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
	
	/**
	 * Converts the given Pascal Str255 type into a String.
	 */
	public static String toString(byte[] str255) {
		int n= str255[0];
		char[] c= new char[n];
		for (int i= 0; i < n; i++)
			c[i]= (char) str255[i+1];
		return new String(c);
	}

	/**
	 * Converts the given String into an OSType.
	 */
	static int OSType(String s) {
		return ((s.charAt(0) & 0xff) << 24) | ((s.charAt(1) & 0xff) << 16) | ((s.charAt(2) & 0xff) << 8) | (s.charAt(3) & 0xff);
	}
	
	/**
	 * Converts the given OSType into a String.
	 */
	public static String toString(int i) {
		StringBuffer sb= new StringBuffer();
		sb.append((char)((i & 0xff000000) >> 24));
		sb.append((char)((i & 0x00ff0000) >> 16));
		sb.append((char)((i & 0x0000ff00) >> 8));
		sb.append((char)((i & 0x000000ff) >> 0));
		return sb.toString();
	}
	
	static String removeMnemonics(String s) {
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
	
	//---- color utilities 
	
	public static void RGBBackColor(int packed) {
		if ((packed & 0xff000000) == 0) {
			RGBColor color = new RGBColor();
			color.red = (short)(((packed >> 16) & 0xFF) * 257);
			color.green = (short)(((packed >> 8) & 0xFF) * 257);
			color.blue = (short)(((packed) & 0xFF) * 257);
			OS.RGBBackColor(color);			
		} else {
			RGBColor color = new RGBColor();
			color.red = (short)0xFFFF;
			color.green = (short)0xFFFF;
			color.blue = (short)0xFFFF;
			OS.RGBBackColor(color);
		}
	}
	
	public static void RGBForeColor(int packed) {
		if ((packed & 0xff000000) == 0) {
			RGBColor color = new RGBColor();
			color.red = (short)(((packed >> 16) & 0xFF) * 257);
			color.green = (short)(((packed >> 8) & 0xFF) * 257);
			color.blue = (short)(((packed) & 0xFF) * 257);
			OS.RGBForeColor(color);
		} else {
			RGBColor color = new RGBColor();
			color.red = (short)0xFFFF;
			color.green = (short)0xFFFF;
			color.blue = (short)0xFFFF;
			OS.RGBForeColor(color);
		}
	}
	
	//---- data browser utilities 
	
	static int[] getDataBrowserItems(int dataBrowserHandle, int containerID, int state, boolean recurse) {
		int[] resultIDs= new int[0];
		int resultHandle= OS.NewHandle(0);
		if (OS.GetDataBrowserItems(dataBrowserHandle, containerID, recurse, state, resultHandle) == OS.noErr) {
			int itemCount= OS.GetHandleSize(resultHandle) / 4;	// sizeof(int)
			if (itemCount > 0) {
				resultIDs= new int[itemCount];
				OS.HLock(resultHandle);
				int[] ptr= new int[1];
				OS.memcpy(ptr, resultHandle, 4);
				OS.memcpy(resultIDs, ptr[0], itemCount*4);
				OS.HUnlock(resultHandle);
			}
		}
		OS.DisposeHandle(resultHandle);
		return resultIDs;
	}

	static int[] getSelectionIDs(int dataBrowserHandle, int containerID, boolean recurse) {
		return getDataBrowserItems(dataBrowserHandle, containerID, OS.kDataBrowserItemIsSelected, recurse);
	}
}
