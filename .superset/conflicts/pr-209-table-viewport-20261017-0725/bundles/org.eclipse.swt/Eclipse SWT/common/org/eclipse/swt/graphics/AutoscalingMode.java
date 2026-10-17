package org.eclipse.swt.graphics;

/**
 * Defines the autoscaling behavior used when rendering or computing layout for
 * controls that support DPI‑aware scaling.
 * <p>
 * This mode determines whether SWT takes care of the scaling of widgets with
 * respect to the zoom level of the monitor.
 * </p>
 *
 * <ul>
 * <li><b>{@link #ENABLED}</b> – Autoscaling is explicitly enabled. Values (such
 * as coordinates, dimensions, or layout metrics) will be adjusted according to
 * the effective scaling factor.</li>
 *
 * <li><b>{@link #DISABLED}</b> – Autoscaling is explicitly disabled. Values
 * will be used as‑is without applying any scaling transformations.</li>
 *
 * <li><b>{@link #DISABLED_INHERITED}</b> – Autoscaling is disabled for this
 * control and this is inherited by its children. Child controls will also be
 * initialized with DISABLED_INHERITED AutoscalingMode.</li>
 * </ul>
 *
 * @since 3.133
 */
public enum AutoscalingMode {
	ENABLED, DISABLED, DISABLED_INHERITED
}
