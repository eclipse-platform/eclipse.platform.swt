/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.swap;

import java.nio.ByteBuffer;

/**
 * Fail-closed page-ops selection for SWT (replaces the Synexia selector, which depends on
 * {@code com.synexia.nativeinterop}). SWT ships no native swap library: with no candidate the Java
 * oracle is used. An embedder may pass a native candidate (for example the Synexia JNI backend);
 * it is admitted only behind {@link ParityCheckedSwapPageBinaryOps} and only if a self-test agrees
 * with the Java oracle, otherwise the Java oracle is returned.
 */
public final class SwapPageBinaryOpsSelector {
	private SwapPageBinaryOpsSelector () {
	}

	public static SwapPageBinaryOps java () {
		return JavaSwapPageBinaryOps.INSTANCE;
	}

	public static SwapPageBinaryOps select (SwapPageBinaryOps nativeCandidate) {
		if (nativeCandidate == null) return JavaSwapPageBinaryOps.INSTANCE;
		SwapPageBinaryOps checked = new ParityCheckedSwapPageBinaryOps (nativeCandidate);
		try {
			byte [] probe = {1, 2, 3, 4, 5, 6, 7, 8, 9};
			ByteBuffer target = ByteBuffer.allocate (probe.length);
			checked.copy (ByteBuffer.wrap (probe), target);
			ByteBuffer longs = ByteBuffer.allocate (24).order (java.nio.ByteOrder.LITTLE_ENDIAN);
			longs.putLong (0, 3).putLong (8, 70).putLong (16, 70_000);
			checked.deltaEncodeMonotonicLongs (longs, ByteBuffer.allocate (32));
			return checked;
		} catch (RuntimeException parityFailure) {
			return JavaSwapPageBinaryOps.INSTANCE;
		}
	}
}
