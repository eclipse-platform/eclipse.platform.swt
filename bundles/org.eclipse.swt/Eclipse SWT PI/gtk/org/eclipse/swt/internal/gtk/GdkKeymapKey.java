package org.eclipse.swt.internal.gtk;

public class GdkKeymapKey {
	/** @field cast=(guint) */
	public long keycode;
	/** @field cast=(gint) */
	public int group;
	/** @field cast=(gint) */
	public int level;
	public static final int sizeof = OS.GdkKeymapKey_sizeof();
}
