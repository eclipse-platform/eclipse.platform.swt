/*******************************************************************************
 * Copyright (c) 2007, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

/**
 * @jniclass flags=no_gen
 */
public class id {

public int /*long*/ id;

public id() {
}

public id(int /*long*/ id) {
	this.id = id;
}

public id(id id) {
	this.id = id != null ? id.id : 0;
}

public int hashCode() {
	return (int) this.id;
}

public boolean equals(Object other) {
	return (this.id == ((id)other).id);
}

public int /*long*/ objc_getClass() {
	String name = getClass().getName();
	int index = name.lastIndexOf('.');
	if (index != -1) name = name.substring(index + 1);
	return OS.objc_getClass(name);
}

public String toString() {
	return getClass().getName() + "{" + id +  "}";
}
}
