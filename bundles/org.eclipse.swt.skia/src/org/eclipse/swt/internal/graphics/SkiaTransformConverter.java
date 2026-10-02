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
package org.eclipse.swt.internal.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.internal.skia.DpiScalerUtil;

import io.github.humbleui.skija.Matrix33;

public class SkiaTransformConverter {

	public static Matrix33 toSkiaMatrix(Transform transform, DpiScalerUtil sc) {

		if (transform == null) {
			return Matrix33.IDENTITY;
		}
		if (transform.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		final float[] elements = new float[6];
		transform.getElements(elements);
		// SWT Transform: [m11, m12, m21, m22, dx, dy]
		// Skia Matrix33: [scaleX, skewX, transX, skewY, scaleY, transY, persp0,
		// persp1, persp2]
		// Correct mapping: SWT [0,1,2,3,4,5] -> Skija [0,2,4,1,3,5,0,0,1]
		final float[] skijaMat = new float[] { elements[0], // m11 -> scaleX
				elements[2], // m21 -> skewX
				sc.autoScaleUp(elements[4]), // dx -> transX
				elements[1], // m12 -> skewY
				elements[3], // m22 -> scaleY
				sc.autoScaleUp(elements[5]), // dy -> transY
				0, 0, 1 // perspective elements
		};
		return new Matrix33(skijaMat);

	}

	public static void fromSkiaMatrix(Matrix33 skiaMatrix, Transform transform, DpiScalerUtil sc) {

		final float[] m = skiaMatrix.getMat();
		// Skija Matrix33: [scaleX, skewX, transX, skewY, scaleY, transY, persp0,
		// persp1, persp2]
		// SWT Transform: [m11, m12, m21, m22, dx, dy]
		// Correct inverse mapping: Skija [0,1,2,3,4,5] -> SWT [0,3,1,4,2,5]
		transform.setElements(m[0], m[3], sc.autoScaleDown(m[1]), m[4], m[2], sc.autoScaleDown(m[5]));

	}

}
