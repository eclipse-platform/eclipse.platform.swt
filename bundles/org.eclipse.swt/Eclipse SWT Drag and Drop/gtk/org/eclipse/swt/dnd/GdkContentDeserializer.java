/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk4.*;

/**
 * Wrapper for GDK class GdkContentDeserializer with convenience methods to aid in
 * writing {@link Transfer}s for GTK4
 */
class GdkContentDeserializer {

	private long deserializer;

	public GdkContentDeserializer(long deserializer) {
		this.deserializer = deserializer;
	}

	public long gtype() {
		return GTK4.gdk_content_deserializer_get_gtype(deserializer);
	}

	public String mime_type() {
		return Converter.cCharPtrToJavaString(GTK4.gdk_content_deserializer_get_mime_type(deserializer), false);
	}

	public long input_stream() {
		return GTK4.gdk_content_deserializer_get_input_stream(deserializer);

	}

	public int priority() {
		return GTK4.gdk_content_deserializer_get_priority(deserializer);
	}

	public long cancellable() {
		return GTK4.gdk_content_deserializer_get_cancellable(deserializer);
	}

	public void return_success() {
		GTK4.gdk_content_deserializer_return_success(deserializer);
	}

	public void return_error(long error) {
		GTK4.gdk_content_deserializer_return_error(deserializer, error);
	}
}
