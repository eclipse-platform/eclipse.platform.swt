package org.eclipse.swt.internal;

/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All Rights Reserved
 */
 
import java.util.EventObject;

/**
 * This class is the cross-platform version of the
 * java.util.EventObject class.
 * <p>
 * It is part of our effort to provide support for both J2SE
 * and J2ME platforms. Under this scheme, classes need to 
 * extend SWTEventObject instead of java.util.EventObject.
 * </p>
 * <p>
 * Note: java.util.EventObject is not part of CDC and CLDC.
 * </p>
 */
public class SWTEventObject extends EventObject {

/**
 * Constructs a new instance of this class.
 *
 * @param source the object which fired the event
 */
public SWTEventObject(Object source) {
	super(source);
}
}