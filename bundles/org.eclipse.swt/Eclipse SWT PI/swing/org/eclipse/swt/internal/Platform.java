package org.eclipse.swt.internal;

public class Platform {

	public static final String PLATFORM = "swing";

	public static boolean isLoadable() {
		return Library.isLoadable();
	}

}
