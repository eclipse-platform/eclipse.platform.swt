package org.eclipse.swt.internal;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

/**
 * This interface is the cross-platform version of the
 * java.util.EventListener interface.
 * <p>
 * It is part of our effort to provide support for both J2SE
 * and J2ME platforms. Under this scheme, classes need to 
 * implement EventListenerCompatibility instead of
 * java.util.EventListener.
 * </p>
 * <p>
 * Note: java.util.EventListener is not part of CDC and CLDC.
 * </p>
 */
public interface EventListenerCompatibility {
}