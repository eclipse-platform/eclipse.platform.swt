package org.eclipse.swt.internal;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * This interface is the cross-platform version of the
 * java.io.Serializable interface.
 * <p>
 * It is part of our effort to provide support for both J2SE
 * and J2ME platforms. Under this scheme, classes need to 
 * implement SerializableCompatibility instead of
 * java.io.Serializable.
 * </p>
 * <p>
 * Note: java.io.Serializable is not part of CLDC.
 * </p>
 * <p>
 * IMPORTANT: CDC mandates Serializable. Behavior on CLDC will have
 * to be discussed further.
 * </p>
 */
public interface SerializableCompatibility {
}