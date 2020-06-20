/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
package org.eclipse.swt.internal;

import java.lang.reflect.*;
import java.util.function.*;

import org.eclipse.swt.*;

/**
 * Instances of this class represent entry points into Java
 * which can be invoked from operating system level callback
 * routines.
 * <p>
 * IMPORTANT: A callback is only valid when invoked on the
 * thread which created it. The results are undefined (and
 * typically bad) when a callback is passed out to the
 * operating system (or other code) in such a way that the
 * callback is called from a different thread.
 */

public class Callback {

	Object object;
	String method, signature;
	int argCount;
	long address, errorResult;
	boolean isStatic, isArrayBased;

	static final boolean is32Bit = C.PTR_SIZEOF == 4 ? true : false;
	static final String PTR_SIGNATURE = is32Bit ? "I" : "J"; //$NON-NLS-1$  //$NON-NLS-2$
	static final String SIGNATURE_0 = getSignature(0);
	static final String SIGNATURE_1 = getSignature(1);
	static final String SIGNATURE_2 = getSignature(2);
	static final String SIGNATURE_3 = getSignature(3);
	static final String SIGNATURE_4 = getSignature(4);
	static final String SIGNATURE_N = "(["+PTR_SIGNATURE+")"+PTR_SIGNATURE; //$NON-NLS-1$  //$NON-NLS-2$

/**
 * Constructs a new instance of this class given an object
 * to send the message to, a string naming the method to
 * invoke and an argument count. Note that, if the object
 * is an instance of <code>Class</code> it is assumed that
 * the method is a static method on that class.
 *
 * <p>Note, do not use this if the method arguments have a double, as arguments will be
 * shifted/corrupted. See Bug 510538. Instead use the following constructor: <br>
 * <code> Callback (Object, String, Type, Type [])</code></p>
 *
 * @param object the object to send the message to
 * @param method the name of the method to invoke
 * @param argCount the number of arguments that the method takes
 */
public Callback (Object object, String method, int argCount) {
	this (object, method, argCount, false);
}

/**
 * Constructs a new instance of this class given an object
 * to send the message to, a string naming the method to
 * invoke, an argument count and a flag indicating whether
 * or not the arguments will be passed in an array. Note
 * that, if the object is an instance of <code>Class</code>
 * it is assumed that the method is a static method on that
 * class.
 *
 * <p>Note, do not use this if the method arguments have a double, as arguments will be
 * shifted/corrupted. See Bug 510538. Instead use the following constructor: <br>
 * <code> Callback (Object, String, Type, Type [])</code></p>
 *
 * @param object the object to send the message to
 * @param method the name of the method to invoke
 * @param argCount the number of arguments that the method takes
 * @param isArrayBased <code>true</code> if the arguments should be passed in an array and false otherwise
 */
public Callback (Object object, String method, int argCount, boolean isArrayBased) {
	this (object, method, argCount, isArrayBased, 0);
}

/**
 * Constructs a new instance of this class given an object
 * to send the message to, a string naming the method to
 * invoke, an argument count, a flag indicating whether
 * or not the arguments will be passed in an array and a value
 * to return when an exception happens. Note that, if
 * the object is an instance of <code>Class</code>
 * it is assumed that the method is a static method on that
 * class.
 *
 * <p>Note, do not use this if the method arguments have a double, as arguments will be
 * shifted/corrupted. See Bug 510538. Instead use the following constructor: <br>
 * <code> Callback (Object, String, Type, Type [])</code></p>
 *
 * @param object the object to send the message to
 * @param method the name of the method to invoke
 * @param argCount the number of arguments that the method takes
 * @param isArrayBased <code>true</code> if the arguments should be passed in an array and false otherwise
 * @param errorResult the return value if the java code throws an exception
 */
public Callback (Object object, String method, int argCount, boolean isArrayBased, long errorResult) {

	/* Set the callback fields */
	this.object = object;
	this.method = method;
	this.argCount = argCount;
	this.isStatic = object instanceof Class;
	this.isArrayBased = isArrayBased;
	this.errorResult = errorResult;

	/* Inline the common cases */
	if (isArrayBased) {
		signature = SIGNATURE_N;
	} else {
		switch (argCount) {
			case 0: signature = SIGNATURE_0; break; //$NON-NLS-1$
			case 1: signature = SIGNATURE_1; break; //$NON-NLS-1$
			case 2: signature = SIGNATURE_2; break; //$NON-NLS-1$
			case 3: signature = SIGNATURE_3; break; //$NON-NLS-1$
			case 4: signature = SIGNATURE_4; break; //$NON-NLS-1$
			default:
				signature = getSignature(argCount);
		}
	}

	/* Bind the address */
	address = bind (this, object, method, signature, argCount, isStatic, isArrayBased, errorResult);
	if (address == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
}


/**
 * <p>Register the java method to be a C callback.
 * I.e, C will be able to make a call to this java method directly (through callback.c)</p>
 *
 * <p>The other constructors hard-code int/long into the method signature:<br>
 * <code> long method (long ...) </code><br>
 * Which is suitable for int/long and pointers.<br>
 * This constructor is used if you need to use a different return/argument type, e.g double. See Bug 510538 </p>
 *
 * <p> Note:
 * <ul>
 * <li> Array support is not implemented/supported by this constructor. Use other constructors.</li>
 * <li> If the object is an instance of <code>Class</code> it is assumed that
 * the method is a static method on that class. </li>
 * <li> Note, long types are converted to ints on 32 bit system automatically to account for smaller pointers.
 * This means if you use 'long', you need to cast int next to it. like: <code> long &#47;*int*&#47;</code> </li>
 * </ul></p>
 *
 * <p>The following types are supported: <br>
 * <ul>
 * <li>void (for return values only) </li>
 * <li>int</li>
 * <li>long</li>
 * <li>byte</li>
 * <li>char</li>
 * <li>double</li>
 * <li>float</li>
 * <li>short</li>
 * <li>boolean</li>
 * </ul>
 *
 * <p> For example if you want to link the following method: <br>
 * <code> void myMethod(long &#47;*int*&#47; arg1, double arg2) </code> <br>
 * Then you would call this callback like:<br>
 * <code> Callback (this, "myMethod", void.class, new Type []{long.class, double.class}); </code>
 * </p>
 *
 * @param object the object to send the message to
 * @param method method the name of the method to invoke
 * @param returnType specify the type like  <code>void.class, long.class, double.class </code>
 * @param arguments specify the list of arguments like <code> new Type [] {long.class, double.class } </code>
 */
public Callback (Object object, String method, Type returnType, Type [] arguments) {
	/* Set the callback fields */
	this.object = object;
	this.method = method;
	this.argCount = arguments != null ? arguments.length : 0;
	this.isStatic = object instanceof Class;
	this.isArrayBased = false;
	this.errorResult = 0;

	Function <Type, String> getTypeLetter = type -> {
		// https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.2
		if (int.class.equals(type)) {
			return "I";
		} else if (long.class.equals(type)) {
			return "J";
		} else if (void.class.equals(type)) { // for return type.
			return "V";
		} else if (byte.class.equals(type)) {
			return "B";
		} else if (char.class.equals(type)) {
			return "C";
		} else if (double.class.equals(type)) {
			return "D";
		} else if (float.class.equals(type)) {
			return "F";
		} else if (short.class.equals(type)) {
			return "S";
		} else if (boolean.class.equals(type)) {
			return "Z";
		}
		SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, type.toString() + "Not supported");
		return null; // not reachable. Suppress warning.
	};

	StringBuilder signature = new StringBuilder("(");
	if (this.argCount > 0) {
		for (Type t : arguments) {
			if (t.equals(void.class)) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, "void is not a valid argument");
			}
			signature.append(getTypeLetter.apply(t));
		}
	}
	signature.append(")");
	signature.append(getTypeLetter.apply(returnType));
	this.signature = signature.toString();
	if (is32Bit) {
		this.signature = this.signature.replace("J", "I");
	}

	/* Bind the address */
	address = bind (this, this.object, this.method, this.signature, this.argCount, this.isStatic, this.isArrayBased, this.errorResult);
	if (address == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
}

/**
 * Allocates the native level resources associated with the
 * callback. This method is only invoked from within the
 * constructor for the argument.
 *
 * @param callback the callback to bind
 * @param object the callback's object
 * @param method the callback's method
 * @param signature the callback's method signature
 * @param argCount the callback's method argument count
 * @param isStatic whether the callback's method is static
 * @param isArrayBased whether the callback's method is array based
 * @param errorResult the callback's error result
 */
static native synchronized long bind (Callback callback, Object object, String method, String signature, int argCount, boolean isStatic, boolean isArrayBased, long errorResult);

/**
 * Releases the native level resources associated with the callback,
 * and removes all references between the callback and
 * other objects. This helps to prevent (bad) application code
 * from accidentally holding onto extraneous garbage.
 */
public void dispose () {
	if (object == null) return;
	unbind (this);
	object = method = signature = null;
	address = 0;
}

/**
 * Returns the address of a block of machine code which will
 * invoke the callback represented by the receiver.
 *
 * @return the callback address
 */
public long getAddress () {
	return address;
}

/**
 * Returns the SWT platform name.
 *
 * @return the platform name of the currently running SWT
 */
public static native String getPlatform ();

/**
 * Returns the number of times the system has been recursively entered
 * through a callback.
 * <p>
 * Note: This should not be called by application code.
 * </p>
 *
 * @return the entry count
 *
 * @since 2.1
 */
public static native int getEntryCount ();

static String getSignature(int argCount) {
	String signature = "("; //$NON-NLS-1$
	for (int i = 0; i < argCount; i++) signature += PTR_SIGNATURE;
	signature += ")" + PTR_SIGNATURE; //$NON-NLS-1$
	return signature;
}

/**
 * Indicates whether or not callbacks which are triggered at the
 * native level should cause the messages described by the matching
 * <code>Callback</code> objects to be invoked. This method is used
 * to safely shut down SWT when it is run within environments
 * which can generate spurious events.
 * <p>
 * Note: This should not be called by application code.
 * </p>
 *
 * @param enable true if callbacks should be invoked
 */
public static final native synchronized void setEnabled (boolean enable);

/**
 * Returns whether or not callbacks which are triggered at the
 * native level should cause the messages described by the matching
 * <code>Callback</code> objects to be invoked. This method is used
 * to safely shut down SWT when it is run within environments
 * which can generate spurious events.
 * <p>
 * Note: This should not be called by application code.
 * </p>
 *
 * @return true if callbacks should not be invoked
 */
public static final native synchronized boolean getEnabled ();

/**
 * This might be called directly from native code in environments
 * which can generate spurious events. Check before removing it.
 *
 * @deprecated
 *
 * @param ignore true if callbacks should not be invoked
 */
@Deprecated
static final void ignoreCallbacks (boolean ignore) {
	setEnabled (!ignore);
}

/**
 * Immediately wipes out all native level state associated
 * with <em>all</em> callbacks.
 * <p>
 * <b>WARNING:</b> This operation is <em>extremely</em> dangerous,
 * and should never be performed by application code.
 * </p>
 */
public static final native synchronized void reset ();

/**
 * Releases the native level resources associated with the callback.
 *
 * @see #dispose
 */
static final native synchronized void unbind (Callback callback);

}
