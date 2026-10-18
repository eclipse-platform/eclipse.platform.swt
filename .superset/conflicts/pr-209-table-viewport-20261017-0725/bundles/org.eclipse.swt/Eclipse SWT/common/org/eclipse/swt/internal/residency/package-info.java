/*
 * Copyright 2026 Hitesh Soliwal and contributors
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
/**
 * Toolkit-neutral M3 item-residency engine (design: docs/m3/M3_ITEM_RESIDENCY.md).
 *
 * <p><b>Licence note.</b> By the rights holder's decision of 2026-10-13, every file this package
 * contains, together with {@code org.eclipse.swt.internal.PackedHeapGrowth},
 * {@code org.eclipse.swt.internal.swap.FileSwapStore} and
 * {@code org.eclipse.swt.internal.swap.SwapPageBinaryOpsSelector}, is dual-licensed
 * {@code EPL-2.0 OR Apache-2.0} (Copyright 2026 Hitesh Soliwal and contributors). The files ported
 * from Synexia in {@code org.eclipse.swt.internal.swap} stay Apache-2.0 with their original
 * notices. No pre-existing SWT file changes licence. The Swing reuse in M3jdk21
 * {@code m3/ports/swing-viewport} takes the Apache-2.0 option; copies into {@code java.desktop}
 * internals are an explicit relicence to GPL-2.0 with the Classpath Exception, recorded in M3jdk21
 * {@code m3/docs/m3-item-residency-explicit-relicense.md}.</p>
 */
package org.eclipse.swt.internal.residency;
