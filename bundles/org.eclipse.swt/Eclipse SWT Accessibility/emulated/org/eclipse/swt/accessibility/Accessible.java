package org.eclipse.swt.accessibility;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.widgets.Control;

/**
 * NOTE: The API in the accessibility package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The methods in AccessibleListener are more stable than those
 * in AccessibleControlListener, however please take nothing for
 * granted. The only reason this API is being released at this
 * time is so that other teams can try it out.
 * 
 * @since 2.0
 */
public class Accessible {

	Accessible(Control control) {
	}
	
	public static Accessible internal_new_accessible(Control control) {
		return new Accessible(control);
	}

	public void addAccessibleListener(AccessibleListener listener) {
	}
	
	public void removeAccessibleListener(AccessibleListener listener) {
	}
	
	public void addAccessibleControlListener(AccessibleControlListener listener) {
	}

	public void removeAccessibleControlListener(AccessibleControlListener listener) {
	}
}
