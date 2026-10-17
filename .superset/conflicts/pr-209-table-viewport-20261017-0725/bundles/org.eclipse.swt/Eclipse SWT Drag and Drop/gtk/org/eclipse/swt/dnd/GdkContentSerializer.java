/*******************************************************************************
 * Copyright (c) 2025 Kichwa Coders Canada, Inc.
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
 * Wrapper for GDK class GdkContentSerializer with convenience methods to aid in
 * writing {@link ContentProviders} for GTK4
 */
class GdkContentSerializer {
	private long serializer;

	public GdkContentSerializer(long serializer) {
		this.serializer = serializer;
	}

	public String mime_type() {
		long p = GTK4.gdk_content_serializer_get_mime_type(serializer);
		if (p == 0) {
			return null;
		}
		return Converter.cCharPtrToJavaString(p, false);
	}

	public long gtype() {
		return GTK4.gdk_content_serializer_get_gtype(serializer);
	}

	public long gvalue() {
		return GTK4.gdk_content_serializer_get_value(serializer);
	}

	public long output_stream() {
		return GTK4.gdk_content_serializer_get_output_stream(serializer);
	}

	public int priority() {
		return GTK4.gdk_content_serializer_get_priority(serializer);
	}

	public long cancellable() {
		return GTK4.gdk_content_serializer_get_cancellable(serializer);
	}

	public void return_success() {
		GTK4.gdk_content_serializer_return_success(serializer);
	}

	public void return_error(long error) {
		GTK4.gdk_content_serializer_return_error(serializer, error);
	}
}
