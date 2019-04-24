/*******************************************************************************
 * Copyright (c) 2007, 2012 IBM Corporation and others.
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
package org.eclipse.swt.internal.cocoa;

/**
 * @jniclass flags=no_gen
 */
public class id {

public long id;

public id() {
}

public id(long id) {
	this.id = id;
}

public id(id id) {
	this.id = id != null ? id.id : 0;
}

@Override
public int hashCode() {
	return (int) this.id;
}

@Override
public boolean equals(Object other) {
	return (this.id == ((id)other).id);
}

public long objc_getClass() {
	String name = getClass().getName();
	int index = name.lastIndexOf('.');
	if (index != -1) name = name.substring(index + 1);
	return OS.objc_getClass(name);
}

@Override
public String toString() {
	return getClass().getName() + "{" + id +  "}";
}
}
