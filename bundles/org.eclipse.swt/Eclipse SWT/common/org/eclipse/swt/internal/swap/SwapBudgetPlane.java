// SPDX-License-Identifier: Apache-2.0
// Ported from hsoliwal/com.synexia 46e296ad537c6ecbd62b9783874e8f1714e278a9 synexia-mindex/runtime/src/main/java/com/synexia/mindex/swap/SwapBudgetPlane.java; package renamed only. Notice: docs/m3/item-residency/THIRD_PARTY_NOTICES.md
package org.eclipse.swt.internal.swap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * One continuous control plane for optional swap residency.
 *
 * <p>The hard {@link TieredSwapCache.Budget} remains the safety ceiling. A normalized ratio
 * derives the current byte/entry/HOT budgets and target physical-shard concurrency. Identity,
 * durability and correctness never depend on this ratio.</p>
 */
public final class SwapBudgetPlane {

	public record Snapshot(
			double ratio,
			long bytes,
			int entries,
			long hotBytes,
			int physicalShards) {
		public Snapshot {
			requireRatio(ratio);
			if (bytes < 0L
					|| entries < 0
					|| hotBytes < 0L
					|| hotBytes > bytes
					|| physicalShards <= 0) {
				throw new IllegalArgumentException("invalid active swap budget");
			}
		}

		public boolean retainsOptionalResidency() {
			return bytes > 0L && entries > 0;
		}
	}

	private final TieredSwapCache.Budget hard;
	private final int maxPhysicalShards;
	private final double softStart;
	private final double hardStop;

	public SwapBudgetPlane(
			TieredSwapCache.Budget hard,
			int maxPhysicalShards,
			double softStart,
			double hardStop) {
		this.hard = Objects.requireNonNull(hard, "hard");
		if (maxPhysicalShards <= 0
				|| (maxPhysicalShards & (maxPhysicalShards - 1)) != 0) {
			throw new IllegalArgumentException("maxPhysicalShards must be a positive power of two");
		}
		if (!(softStart >= 0.0
				&& softStart < hardStop
				&& hardStop <= 1.0)) {
			throw new IllegalArgumentException("require 0 <= softStart < hardStop <= 1");
		}
		this.maxPhysicalShards = maxPhysicalShards;
		this.softStart = softStart;
		this.hardStop = hardStop;
	}

	public TieredSwapCache.Budget hardBudget() {
		return hard;
	}

	public int maxPhysicalShards() {
		return maxPhysicalShards;
	}

	public double softStart() {
		return softStart;
	}

	public double hardStop() {
		return hardStop;
	}

	/**
	 * Convert used/max heap evidence to available optional-residency capacity.
	 *
	 * <p>At or below {@code softStart} the full plane is available. At or above
	 * {@code hardStop}, no optional residency is retained. Between those points the mapping is
	 * linear.</p>
	 */
	public double ratio(long usedBytes, long maxBytes) {
		if (usedBytes < 0L || maxBytes <= 0L || usedBytes > maxBytes) {
			throw new IllegalArgumentException("invalid heap usage");
		}
		double used = (double) usedBytes / (double) maxBytes;
		if (used <= softStart) return 1.0;
		if (used >= hardStop) return 0.0;
		return (hardStop - used) / (hardStop - softStart);
	}

	public Snapshot snapshot(double ratio) {
		requireRatio(ratio);
		long bytes = scaleBytes(hard.bytes(), ratio);
		int entries = scaleCount(hard.entries(), ratio);
		long hotBytes = Math.min(bytes, scaleBytes(hard.hotBytes(), ratio));
		return new Snapshot(
				ratio,
				bytes,
				entries,
				hotBytes,
				physicalShardCount(maxPhysicalShards, ratio));
	}

	public static long scaleBytes(long hardLimit, double ratio) {
		if (hardLimit < 0L) throw new IllegalArgumentException("negative hardLimit");
		requireRatio(ratio);
		if (hardLimit == 0L || ratio == 0.0) return 0L;
		if (ratio == 1.0) return hardLimit;
		return BigDecimal.valueOf(hardLimit)
				.multiply(BigDecimal.valueOf(ratio))
				.setScale(0, RoundingMode.FLOOR)
				.longValueExact();
	}

	public static int scaleCount(int hardLimit, double ratio) {
		if (hardLimit < 0) throw new IllegalArgumentException("negative hardLimit");
		long scaled = scaleBytes(hardLimit, ratio);
		return Math.toIntExact(scaled);
	}

	static int physicalShardCount(int maxPhysicalShards, double ratio) {
		requireRatio(ratio);
		if (maxPhysicalShards <= 0
				|| (maxPhysicalShards & (maxPhysicalShards - 1)) != 0) {
			throw new IllegalArgumentException("maxPhysicalShards must be a positive power of two");
		}
		if (ratio <= 0.0) return 1;
		int desired =
				Math.max(
						1,
						Math.min(
								maxPhysicalShards,
								(int) Math.ceil((double) maxPhysicalShards * ratio)));
		return Integer.highestOneBit(desired);
	}

	private static void requireRatio(double ratio) {
		if (!Double.isFinite(ratio) || ratio < 0.0 || ratio > 1.0) {
			throw new IllegalArgumentException("ratio must be finite in [0,1]");
		}
	}
}
