package org.eclipse.swt.internal; 

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

/**
 * This interface is the cross-platform version of the
 * java.lang.Cloneable interface.
 * <p>
 * It is part of our effort to provide support for both J2SE
 * and J2ME platforms. Under this scheme, classes need to 
 * implement CloneableCompatibility instead of java.lang.Cloneable.
 * </p>
 * <p>
 * Note: java.lang.Cloneable is not part of CLDC.
 * </p>
 */
public interface CloneableCompatibility extends Cloneable {
}