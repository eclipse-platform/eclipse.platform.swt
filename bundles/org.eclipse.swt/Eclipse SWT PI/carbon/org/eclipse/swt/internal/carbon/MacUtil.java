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
import org.eclipse.swt.internal.Callback;

public class MacUtil {

	public final static boolean DEBUG;
	public final static boolean USE_MENU_ICONS;
	/** Prevent use of standard Mac shortcuts Cmd-Q, Cmd-H */
	public final static boolean KEEP_MAC_SHORTCUTS;
	public final static boolean USE_FRAME= false;
	
	public final static boolean FULL_KBD_NAV= true;
	
	public final static boolean HIVIEW= false;
	
	static final char MNEMONIC = '&';
		
	static {
		DEBUG= false;
		USE_MENU_ICONS= true;
		KEEP_MAC_SHORTCUTS= true;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	private static int fViewClassID= 0;
	
	static int createCallback(String method, int argCount) {
		Callback cb= new Callback(MacUtil.class, method, argCount);
		int proc= cb.getAddress();
		return proc;
	}
	
	static int hiobProc(int a, int b, int c) {
		System.out.println("hiobProc");
		return OS.kNoErr;
	}
	
	static int createHIView() {
		int rc;
		
		if (fViewClassID == 0) {
				
			fViewClassID= OS.CFStringCreateWithCharacters("org.eclipse.swt.hiview");
			int baseClassID= OS.CFStringCreateWithCharacters("com.apple.hiview");
		
			int[] events= new int[] {
				OS.kEventClassHIObject, OS.kEventHIObjectConstruct,
				//OS.kEventClassHIObject, OS.kEventHIObjectInitialize,
				OS.kEventClassHIObject, OS.kEventHIObjectDestruct,
				
				OS.kEventClassControl,	OS.kEventControlDraw,
				OS.kEventClassControl,	OS.kEventControlAddedSubControl,
				OS.kEventClassControl,	OS.kEventControlRemovingSubControl,
			};
		
			int hiobProc= createCallback("hiobProc", 3);
		
			int[] tmp= new int[1];
			rc= OS.HIObjectRegisterSubclass(fViewClassID, baseClassID, 0, hiobProc, events, 0, tmp);
			System.out.println("HIObjectRegisterSubclass: " + rc);
		
			OS.CFRelease(baseClassID);
		}
		
		int[] oref= new int[1];
		//rc= OS.HIObjectCreate(fViewClassID, 0, oref);
		rc= OS.HIObjectCreate(OS.CFStringCreateWithCharacters("com.apple.hiview"), 0, oref);
		System.out.println("HIObjectCreate: " + rc + " " + oref[0]);
		return oref[0];
	}

	//////////////////////////////////////////////////////////////////////
		
	public static int getChild(int handle, int[] t, int n, int i) {
		int index= (n-1 - i);
		int status= OS.GetIndexedSubControl(handle, (short)(index+1), t);
		if (status != OS.kNoErr)
			System.out.println("MacUtil.getChild: error");
		return status;
	}
		
	public static int indexOf(int parentHandle, int handle) {
		int n= countSubControls(parentHandle);
		int[] outControl= new int[1];
		for (int i= 0; i < n; i++) {
			if (getChild(parentHandle, outControl, n, i) == OS.kNoErr)
				if (outControl[0] == handle)
					return i;
		}
		return -1;
	}
	
	/**
	 * Inserts the given child at position in the parent.
	 * If pos is out of range the child is added at the end (below all other).
	 */
	private static void insertControl(int controlHandle, int parentControlHandle, int pos) {
		
		
		int n= countSubControls(parentControlHandle);
		
		int should= pos;
		if (should < 0 || should > n)
			should= n;
		
		boolean add= false;
		if (getSuperControl(controlHandle) != parentControlHandle) {
			add= true;
		} else {
			/*
			String w1= getHIObjectClassID(parentControlHandle);
			String w2= getHIObjectClassID(controlHandle);
			System.out.println("MacUtil.insertControl: already there: " + w1 + " " + w2);
			*/
			if (n == 1)
				return;
		}
		
		if (n == 0) {
			OS.HIViewAddSubview(parentControlHandle, controlHandle);
			pos= 0;
		} else {
			if (pos >= 0 && pos < n) {
				int[] where= new int[1];
				getChild(parentControlHandle, where, n, pos);
				if (add)
					OS.HIViewAddSubview(parentControlHandle, controlHandle);
				OS.HIViewSetZOrder(controlHandle, OS.kHIViewZOrderAbove, where[0]);
			} else {
				if (add)
					OS.HIViewAddSubview(parentControlHandle, controlHandle);
				if (OS.HIViewSetZOrder(controlHandle, OS.kHIViewZOrderBelow, 0) != OS.kNoErr)
					System.out.println("eroor 2");
				pos= n;
			}
		}
	
		// verify correct position
		int i= indexOf(parentControlHandle, controlHandle);
		if (i != should)
			System.out.println("MacUtil.insertControl: is: "+i+" should: "+ should  + " n:" + n + " add: " + add);
	}
	
	/**
	 * Adds the given child at the end.
	 */
	public static void addControl(int controlHandle, int parentControlHandle) {
		insertControl(controlHandle, parentControlHandle, -1);
	}
		
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
				for (int i= 0; i < n; i++) {
					int index= i; // was: n-1-i
					if (OS.GetIndexedSubControl(cHandle, (short)(index+1), outHandle) == 0) {	// indices are 1 based
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
			for (int i= 0; i < n; i++) {
				int index= (n-1-i);
				if (OS.GetIndexedSubControl(cHandle, (short)(index+1), outHandle) == 0) {	// indices are 1 based
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

	//////////////////////////////////////////////////////////////////////
	
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
		
		if (HIVIEW)
			return 0;
			
		int root;
		if (true) {
			int[] rootHandle= new int[1];
			int rc= OS.GetRootControl(wHandle, rootHandle);
			if (rc != OS.kNoErr) {
				System.out.println("MacUtil.findControlUnderMouse: " + rc);
				return 0;
			}
			root= rootHandle[0];
		} else {
			root= OS.HIViewGetRoot(wHandle);
		}
		Point w= where.toPoint();
		int cHandle= find(root, null, new MacRect(), w);
		if (cHandle != 0 && cpart != null && cpart.length > 0) {
			cpart[0]= OS.TestControl(cHandle, where.getData());
			//System.out.println("findControlUnderMouse: " + cpart[0]);
		}
		return cHandle;
	}

	private static int countSubControls(int cHandle) {
		short[] cnt= new short[1];
		int status= OS.CountSubControls(cHandle, cnt);
		switch (status) {
		case OS.kNoErr:
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

	public static String getHIObjectClassID(int handle) {
		int sh= OS.HIObjectCopyClassID(handle);
		return getStringAndRelease(sh);
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
		int controlHandle;
		if (HIVIEW) {
			controlHandle= OS.NewControl(0, false, init, min, max, procID);
			insertControl(controlHandle, parentControlHandle, pos);
			OS.HIViewSetVisible(controlHandle, true);
			OS.HIViewSetNeedsDisplay(controlHandle, true);
		} else {
			int windowHandle= OS.GetControlOwner(parentControlHandle);
			controlHandle= OS.NewControl(windowHandle, false, init, min, max, procID);
			insertControl(controlHandle, parentControlHandle, pos);
			initLocation(controlHandle);
			OS.HIViewSetVisible(controlHandle, true);
		}

		return controlHandle;
	}
	
	public static int createDrawingArea(int parentControlHandle, int pos, boolean visible, int width, int height, int border) {
		int features= OS.kControlSupportsEmbedding | OS.kControlSupportsFocus | OS.kControlGetsFocusOnClick;
		int controlHandle;
		if (HIVIEW) {
			controlHandle= OS.NewControl(0, false, (short)features, (short)0, (short)0, OS.kControlUserPaneProc);
			OS.SizeControl(controlHandle, (short)width, (short)height);
			insertControl(controlHandle, parentControlHandle, pos);
			OS.HIViewSetVisible(controlHandle, visible);
			OS.HIViewSetNeedsDisplay(controlHandle, true);
		} else {
			int windowHandle= OS.GetControlOwner(parentControlHandle);
			controlHandle= OS.NewControl(windowHandle, false, (short)features, (short)0, (short)0, OS.kControlUserPaneProc);
			OS.SizeControl(controlHandle, (short)width, (short)height);
			insertControl(controlHandle, parentControlHandle, pos);
			initLocation(controlHandle);
			OS.HIViewSetVisible(controlHandle, visible);
		}
		return controlHandle;
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
