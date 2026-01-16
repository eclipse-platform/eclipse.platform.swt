/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal;

final class AutoScaleCalculation {
	private AutoScaleCalculation() {
	}

	interface AutoScale {
		int getAutoScaledZoom(int zoom);
		boolean isCompatibleToMonitorSpecificScaling();
	}

	private static class AutoScaleInteger implements AutoScale {
		private static final String AS_STRING = "integer";

		@Override
		public int getAutoScaledZoom(int zoom) {
			return Math.max ((zoom + 25) / 100 * 100, 100);
		}

		@Override
		public boolean isCompatibleToMonitorSpecificScaling() {
			return false;
		}

		@Override
		public String toString() {
			return AS_STRING;
		}

	}

	private static class AutoScaleQuarter implements AutoScale {
		private static final String AS_STRING = "quarter";

		@Override
		public int getAutoScaledZoom(int zoom) {
			return  Math.round(zoom / 25f) * 25;
		}

		@Override
		public boolean isCompatibleToMonitorSpecificScaling() {
			return true;
		}

		@Override
		public String toString() {
			return AS_STRING;
		}
	}

	private static class AutoScaleHalf implements AutoScale {
		private static final String AS_STRING = "half";

		@Override
		public int getAutoScaledZoom(int zoom) {
			// Math.round rounds 125->150 and 175->200,
			// Math.rint rounds 125->100 and 175->200 matching
			// "integer"
			return (int) Math.rint(zoom / 50d) * 50;
		}

		@Override
		public boolean isCompatibleToMonitorSpecificScaling() {
			return false;
		}

		@Override
		public String toString() {
			return AS_STRING;
		}
	}

	private static class AutoScaleExact implements AutoScale {
		private static final String AS_STRING = "exact";

		@Override
		public int getAutoScaledZoom(int zoom) {
			return zoom;
		}

		@Override
		public boolean isCompatibleToMonitorSpecificScaling() {
			return true;
		}

		@Override
		public String toString() {
			return AS_STRING;
		}
	}


	private static class AutoScaleNone implements AutoScale {
		private static final String AS_STRING = "false";

		@Override
		public int getAutoScaledZoom(int zoom) {
			return 100;
		}

		@Override
		public boolean isCompatibleToMonitorSpecificScaling() {
			return false;
		}

		@Override
		public String toString() {
			return AS_STRING;
		}
	}

	private static class AutoScaleValue implements AutoScale {

		private final int fixedZoom;

		AutoScaleValue(int zoom) {
			this.fixedZoom = zoom;
		}

		@Override
		public int getAutoScaledZoom(int zoom) {
			return fixedZoom;
		}

		@Override
		public boolean isCompatibleToMonitorSpecificScaling() {
			return false;
		}

		@Override
		public String toString() {
			return fixedZoom + "";
		}
	}

	static AutoScale parseFrom(String value) {
		if (value != null) {
			if (AutoScaleNone.AS_STRING.equalsIgnoreCase (value)) {
				return new AutoScaleNone();
			} else if (AutoScaleHalf.AS_STRING.equalsIgnoreCase (value)) {
				return new AutoScaleHalf();
			} else if (AutoScaleQuarter.AS_STRING.equalsIgnoreCase (value)) {
				return new AutoScaleQuarter();
			} else if (AutoScaleExact.AS_STRING.equalsIgnoreCase (value)) {
				return new AutoScaleExact();
			} else if (AutoScaleInteger.AS_STRING.equalsIgnoreCase(value)) {
				return new AutoScaleInteger();
			} else {
				try {
					int zoomValue = Integer.parseInt (value);
					return new AutoScaleValue(zoomValue);
				} catch (NumberFormatException e) {
					// unsupported value, use default
				}
			}
		}
		return getDefaultAutoScale();
	}

	private static AutoScale getDefaultAutoScale() {
		return DPIUtil.isMonitorSpecificScalingActive() ? new AutoScaleQuarter() : new AutoScaleInteger();
	}

}
