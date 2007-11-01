package org.eclipse.swt.internal.cocoa;

import org.eclipse.swt.internal.*;

public class OS extends Platform {
	static {
		Library.loadLibrary("swt-pi"); //$NON-NLS-1$
	}

	/** Constants */
	public static final int NSBackingStoreRetained     = 0;
	public static final int NSBackingStoreNonretained  = 1;
	public static final int NSBackingStoreBuffered     = 2;	
	public static final int NSBorderlessWindowMask = 0;
	public static final int NSTitledWindowMask = 1 << 0;
	public static final int NSClosableWindowMask = 1 << 1;
	public static final int NSMiniaturizableWindowMask = 1 << 2;
	public static final int NSResizableWindowMask = 1 << 3;
	public static final int NSTexturedBackgroundWindowMask = 1 << 8;
	public static final int kProcessTransformToForegroundApplication = 1;
	public static final int noErr = 0;
	
	/** Classes */
	public static final int class_NSObject = OS.objc_getClass("NSObject");
	public static final int class_NSString = OS.objc_getClass("NSString");
	public static final int class_NSArray = OS.objc_getClass("NSString");
	public static final int class_NSApplication = OS.objc_getClass("NSApplication");
	public static final int class_NSWindow = OS.objc_getClass("NSWindow");
	public static final int class_NSScreen = OS.objc_getClass("NSScreen");
	public static final int class_NSAutoreleasePool = OS.objc_getClass("NSAutoreleasePool");
	
	/** Selectors */	
	public static final int sel_alphaValue = OS.sel_registerName("alphaValue");
	public static final int sel_alloc = OS.sel_registerName("alloc");
	public static final int sel_close = OS.sel_registerName("close");
	public static final int sel_contentView = OS.sel_registerName("contentView");
	public static final int sel_count = OS.sel_registerName("count");
	public static final int sel_depth = OS.sel_registerName("depth");
	public static final int sel_frame = OS.sel_registerName("frame");
	public static final int sel_init = OS.sel_registerName("init");
	public static final int sel_initWithContentRect_1styleMask_1backing_1defer_1 = OS.sel_registerName("initWithContentRect:styleMask:backing:defer:");
	public static final int sel_isVisible = OS.sel_registerName("isVisible");
	public static final int sel_mainScreen = OS.sel_registerName("mainScreen");
	public static final int sel_makeKeyAndOrderFront_1 = OS.sel_registerName("makeKeyAndOrderFront:");
	public static final int sel_new = OS.sel_registerName("new");
	public static final int sel_objectAtIndex_1 = OS.sel_registerName("objectAtIndex:");	
	public static final int sel_orderFront_1 = OS.sel_registerName("orderFront:");
	public static final int sel_orderOut_1 = OS.sel_registerName("orderOut:");
	public static final int sel_release = OS.sel_registerName("release");
	public static final int sel_respondsToSelector_1 = OS.sel_registerName("respondsToSelector:");
	public static final int sel_run = OS.sel_registerName("run");
	public static final int sel_screens = OS.sel_registerName("screens");
	public static final int sel_setAlphaValue_1 = OS.sel_registerName("setAlphaValue:");
	public static final int sel_setDelegate_1 = OS.sel_registerName("setDelegate:");
	public static final int sel_setTag_1 = OS.sel_registerName("setTag:");
	public static final int sel_setTitle_1 = OS.sel_registerName("setTitle:");
	public static final int sel_sharedApplication = OS.sel_registerName("sharedApplication");
	public static final int sel_stop_1 = OS.sel_registerName("stop:");
	public static final int sel_stringWithCharacters_1length_1 = OS.sel_registerName("stringWithCharacters:length:");	
	public static final int sel_tag = OS.sel_registerName("tag");
	public static final int sel_visibleFrame = OS.sel_registerName("visibleFrame");
	public static final int sel_windowWillClose_1 = OS.sel_registerName("windowWillClose:");
	public static final int sel_windowShouldClose_1 = OS.sel_registerName("windowShouldClose:");	
	

	//TODO - don't hard code
	public static final int VERSION = 0x1040;
	public static final int PTR_SIZEOF = 4;

/** JNI natives */
public static final native int NewGlobalRef(Object object);
public static final native void DeleteGlobalRef(int globalRef);
public static final native Object JNIGetObject(int globalRef);
	
public static final native boolean class_addIvar(int cls, String name, int size, byte alignment, String types);
public static final native boolean class_addMethod(int cls, int name, int imp, String types);
public static final native int objc_allocateClassPair(int superclass, String name, int extraBytes);
public static final native int objc_getClass(String className);
public static final native int objc_lookUpClass(String className);
public static final native void objc_msgSend_stret(NSRect rect, int object, int selector);
public static final native double objc_msgSend_fpret(int object, int selector);
public static final native int objc_msgSend(int object, int selector);
public static final native int objc_msgSend(int object, int selector, int arg0);
public static final native int objc_msgSend(int object, int selector, float arg0);
public static final native int objc_msgSend(int object, int selector, NSSize arg0);
public static final native int objc_msgSend(int object, int selector, NSRect arg0);
public static final native int objc_msgSend(int object, int selector, NSRect arg0, int arg1, int arg2, boolean arg3);
public static final native int objc_msgSend(int object, int selector, int arg0, NSRect arg1, int arg2);
public static final native int objc_msgSend(int object, int selector, NSRect arg0, int arg1);
public static final native int objc_msgSend(int object, int selector, char[] arg0, int arg1);
public static final native int objc_msgSend(int object, int selector, NSPoint arg0, int arg1);
public static final native int objc_msgSend(int object, int selector, int arg0, NSPoint arg1);
public static final native int objc_msgSend(int object, int selector, NSPoint arg0);
public static final native int objc_msgSend(int object, int selector, int arg0, int arg1);
public static final native int objc_msgSend(int object, int selector, int arg0, int arg1, int arg2);
public static final native int objc_msgSend(int object, int selector, int arg0, int arg1, int arg2, int arg3);
public static final native int objc_msgSend(int object, int selector, int[] arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10);
public static final native void objc_registerClassPair(int cls);
public static final native int object_getInstanceVariable(int obj, String name, int[] outValue);
public static final native int object_setInstanceVariable(int obj, String name, int value);
public static final native int sel_registerName(String selectorName);

public static final native int NSBitsPerPixelFromDepth(int depth);

public static final native int GetCurrentProcess(int[] psn);
public static final native int SetFrontProcess(int[] psn);
public static final native int TransformProcessType(int[] psn, int transformState);  
}
