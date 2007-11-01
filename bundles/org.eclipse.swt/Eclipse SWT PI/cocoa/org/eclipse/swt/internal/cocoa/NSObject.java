package org.eclipse.swt.internal.cocoa;

import org.eclipse.swt.internal.C;

public class NSObject {

public int id;

public NSObject alloc() {
	id = OS.objc_msgSend(get_class(), OS.sel_alloc);
	if (id == 0) return null;
	return this;
}

public int get_class() {
	return OS.class_NSObject;
}

public static boolean createClass(String name, String superclassName, int callback, int[] selectors, String[] argTypes) {
	objc_class meta_class = new objc_class();
	objc_class super_class = new objc_class();
	objc_class new_class = new objc_class();
	objc_class root_class = new objc_class();
 
	byte[] superclassNameBuffer = (superclassName + "\0").getBytes();
    int super_class_ptr = OS.objc_lookUpClass (superclassNameBuffer);
    if (super_class_ptr == 0) {
        return false;
    }
    OS.memmove(super_class, super_class_ptr, objc_class.sizeof);
 
    byte[] nameBuffer = (name + "\0").getBytes();
    if (OS.objc_lookUpClass (nameBuffer) != 0) {
        return false;
    }
 
    OS.memmove(root_class, super_class_ptr, objc_class.sizeof);
    while (root_class.super_class != 0) {
        OS.memmove(root_class, root_class.super_class, objc_class.sizeof);
    }
 
    int new_class_ptr = C.calloc(2, objc_class.sizeof);
    int meta_class_ptr = new_class_ptr + objc_class.sizeof;
 
    new_class.isa = meta_class_ptr;
    new_class.info = OS.CLS_CLASS;
    meta_class.info = OS.CLS_META;
 
    new_class.name = C.malloc (nameBuffer.length);
    C.memmove(new_class.name, nameBuffer, nameBuffer.length);
    meta_class.name = new_class.name;

    meta_class.methodLists = C.calloc(1, objc_method_list.sizeof + objc_method.sizeof);
    C.memmove(meta_class.methodLists, new int[]{-1}, 4);
    new_class.methodLists = C.calloc(1, objc_method_list.sizeof + objc_method.sizeof);
    C.memmove(new_class.methodLists, new int[]{-1}, 4);
    
    new_class.super_class  = super_class_ptr;
    meta_class.super_class = super_class.isa;
    meta_class.isa         = root_class.isa;
 
    new_class.instance_size = super_class.instance_size;
    OS.memmove(super_class, meta_class.super_class, objc_class.sizeof);
    meta_class.instance_size = super_class.instance_size;
 
    OS.memmove(new_class_ptr, new_class, objc_class.sizeof);
    OS.memmove(meta_class_ptr, meta_class, objc_class.sizeof);
    OS.objc_addClass(new_class_ptr);    

    objc_method_list method_list = new objc_method_list();
    method_list.method_count = selectors.length;
    int method_list_ptr = C.malloc(objc_method_list.sizeof + method_list.method_count * objc_method.sizeof);
    OS.memmove(method_list_ptr, method_list, objc_method_list.sizeof);
    objc_method method = new objc_method();
    for (int i = 0; i < selectors.length; i++) {
    	byte[] typesBuffer = (argTypes[i] + "\0").getBytes();
    	method.method_name = selectors[i];
    	method.method_imp = callback;
    	method.method_types = C.malloc(typesBuffer.length);
    	C.memmove(method.method_types, typesBuffer, typesBuffer.length);
    	OS.memmove(method_list_ptr + objc_method_list.sizeof + (i * objc_method.sizeof), method, objc_method.sizeof);
	}
    OS.class_addMethods(OS.objc_lookUpClass (nameBuffer), method_list_ptr);
    
    return true;
}

public NSObject(int id) {
	this.id = id;
}

public NSObject init() {
	OS.objc_msgSend(id, OS.sel_init);
	return this;
}

public void release() {
	OS.objc_msgSend(id, OS.sel_release);
	id = 0;
}

public boolean respondsToSelector(int sel) {
	return OS.objc_msgSend(id, OS.sel_respondsToSelector_1, sel) != 0;
}
}
