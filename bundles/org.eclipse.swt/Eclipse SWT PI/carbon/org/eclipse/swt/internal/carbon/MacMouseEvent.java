package org.eclipse.swt.internal.carbon;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.SWT;

public class MacMouseEvent {
	
	private int fWhen;
	private Point fWhere;
	private int fState;
	private int fButton;
	private MacEvent fMacEvent;
	
	public MacMouseEvent() {
	}

	public MacMouseEvent(int button, Point where) {
		fButton= button;
		fWhere= where;
		fState= SWT.BUTTON1;
	}
	
	public MacMouseEvent(MacEvent me) {
		fMacEvent= me;
		fWhen= me.getWhen();
		fWhere= me.getWhere2();
		fState= me.getStateMask();
		fButton= me.getButton();
	}
	
	public int getWhen() {
		return fWhen;
	}
	
	public Point getWhere() {
		return fWhere;
	}

	public int getState() {
		return fState;
	}
	
	public int getButton() {
		return fButton;
	}
	
	public int[] toOldMacEvent() {
		if (fMacEvent != null)
			return fMacEvent.toOldMacEvent();
		System.err.println("MacMouseEvent.toOldMacEvent: nyi");
		return null;
	}
}
