/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.skia.cache;

import org.eclipse.swt.graphics.ImageVersion;

public class ImageKey {

	private final int version;
	private final org.eclipse.swt.graphics.Image image;
	private final int zoom;

	public ImageKey(org.eclipse.swt.graphics.Image image , ImageVersion version, int zoom) {
		this.version = version.getVersion();
		this.image = image;
		this.zoom = zoom;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final ImageKey that = (ImageKey) obj;
		return version == that.version &&
				image == that.image &&
				zoom == that.zoom ;
	}

	@Override
	public int hashCode() {
		int result = Integer.hashCode(version);
		result = 31 * result + image.hashCode();
		result = 31 * result + Integer.hashCode(zoom);
		return result;
	}

	@Override
	public String toString() {
		return String.format("ImageCache{version=%d, image=%s, zoom=%d}", version, image, zoom); //$NON-NLS-1$
	}
}