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
package org.eclipse.swt.internal.residency;

import java.io.*;
import java.lang.management.*;
import java.util.*;

import javax.management.*;

import org.eclipse.swt.internal.swap.*;

/**
 * Desktop pressure source of {@link ItemResidency}: the ported Synexia {@link SwapPressureController}
 * (heap, cgroup and PSI; worst signal wins; shrink immediate, recovery rate-limited) drives both
 * the engine's slot budget and, through the same decision, the {@link TieredSwapCache} byte
 * budget of an {@link ItemSwapSpill}. A collection-usage threshold at 70% of each heap pool raises
 * {@link #urgent()} through the {@link MemoryMXBean} notification, so the next engine operation
 * samples immediately. Excluded from the web (GWT/RAP) build.
 */
public final class ItemSwapPressure implements ItemResidency.Pressure, NotificationListener {
	final SwapPressureController controller;
	private final ItemSwapSpill spill;
	private final MemoryMXBean memory = ManagementFactory.getMemoryMXBean ();
	private final List<MemoryPoolMXBean> armed = new ArrayList<> ();
	private volatile boolean urgent;
	volatile int notifications;

	public ItemSwapPressure (SwapPressureController controller, ItemSwapSpill spill) {
		this.controller = Objects.requireNonNull (controller, "controller");
		this.spill = spill;
	}

	/** System controller over the spill's hard cache budget (or a unit budget without a spill). */
	public static ItemSwapPressure system (ItemSwapSpill spill) {
		TieredSwapCache.Budget hard = spill != null ? spill.budget : new TieredSwapCache.Budget (1, 1, 0);
		SwapBudgetPlane plane = new SwapBudgetPlane (hard, 1, ItemResidency.SOFT_START / 1000.0, ItemResidency.HARD_STOP / 1000.0);
		ItemSwapPressure p = new ItemSwapPressure (SwapPressureController.system (plane), spill);
		p.arm ();
		return p;
	}

	private void arm () {
		for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans ()) {
			if (pool.getType () != MemoryType.HEAP || !pool.isCollectionUsageThresholdSupported ()) continue;
			long max = pool.getUsage ().getMax ();
			if (max > 0 && pool.getCollectionUsageThreshold () == 0) {
				pool.setCollectionUsageThreshold (max / 1000 * ItemResidency.SOFT_START);
				armed.add (pool);
			}
		}
		if (memory instanceof NotificationEmitter) ((NotificationEmitter) memory).addNotificationListener (this, null, null);
	}

	@Override
	public int availablePermille () {
		SwapPressureController.Decision d;
		try {
			d = controller.decide ();
		} catch (IOException e) {
			throw new UncheckedIOException (e);
		}
		if (spill != null) spill.cache.readjust (d.snapshot ());
		return (int) Math.round (d.effectiveRatio () * 1000.0);
	}

	@Override
	public boolean urgent () {
		boolean was = urgent;
		urgent = false;
		return was;
	}

	@Override
	public void handleNotification (Notification notification, Object handback) {
		if (MemoryNotificationInfo.MEMORY_COLLECTION_THRESHOLD_EXCEEDED.equals (notification.getType ())) {
			notifications++;
			urgent = true;
		}
	}

	/** Removes the listener and disarms the thresholds this source armed. */
	public void close () {
		if (memory instanceof NotificationEmitter) {
			try {
				((NotificationEmitter) memory).removeNotificationListener (this);
			} catch (ListenerNotFoundException e) {
				// already removed
			}
		}
		for (MemoryPoolMXBean pool : armed) pool.setCollectionUsageThreshold (0);
		armed.clear ();
	}
}
