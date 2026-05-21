/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors:
 *     SAP SE and others - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

/**
 * @noreference This class is not intended to be referenced by clients.
 */
public class ImageVersion {

	private int version;

	public ImageVersion(int version) {
		this.version = version;
	}

	public static ImageVersion getVersion(Image image) {
		return image.getImageVersion();
	}

	public int getVersion() {
		return version;
	}

}
