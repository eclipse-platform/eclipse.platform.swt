package org.eclipse.swt.internal;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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