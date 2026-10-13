package org.eclipse.swt.widgets;

import com.google.gwt.core.client.EntryPoint;

/** Gate-only entry point: runs the shared workload so GWT must compile every atom. */
public class GateEntry implements EntryPoint {
	@Override public void onModuleLoad () {
		for (int seed = 1; seed <= 8; seed++) log (GateWorkload.run (seed, 400));
	}

	private static native void log (String text) /*-{
		$wnd.console.log (text);
	}-*/;
}
