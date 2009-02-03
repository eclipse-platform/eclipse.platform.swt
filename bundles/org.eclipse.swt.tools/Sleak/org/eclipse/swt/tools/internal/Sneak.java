package org.eclipse.swt.tools.internal;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Sneak extends Synchronizer {
	Display display;
	List list;
	Text text;
	int itemCount;
	String [] items = new String [0];
	String [] stacks = new String [0];
	public static int TIME = 250;
	public static int EVENT_MIN = 0;
	public static int EVENT_MAX = 23;
	
public Sneak (Display display) {
	super (display);
	this.display = display;
	install ();
}

public static void main (String [] args) {
	Display display = new Display ();
	Sneak sneak = new Sneak (display);
	sneak.main (display, args);
	
	// Launch your application here
	// e.g.
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	final List list = new List(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	String[] names = new String[100];//1024 * 100 * 5];
	for (int i = 0; i < names.length; i++) {
		names[i] = "Click and wait "+i;
	}
	shell.setSize (200, 200);
	shell.open();
	long t0 = System.currentTimeMillis();
	list.setItems(names);
	long t1 = System.currentTimeMillis();
	System.out.println("Time: " + (t1-t0));
	list.addListener (SWT.MouseDown, new Listener () {
		public void handleEvent (Event event) {
			System.out.println("Sleeping in UI ...");
			try {
				Thread.sleep (300);
			} catch (Throwable th) {}
			System.out.println("done.");
		}
	});
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

Shell main (Display display, String [] args) {
	Shell shell = new Shell ();
	shell.setLayout(new FillLayout ());
	list = new List (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	list.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event e) {
			text.setText (stacks [list.getSelectionIndex()]);
		}
	});
	text = new Text (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	shell.open ();
	return shell;
}

void add (long time, String name, StackTraceElement elements[]) {
	if (items.length == itemCount) {
		String [] newItems = new String [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	items [itemCount] = time + " - " + name;
	if (stacks.length == itemCount) {
		String [] newStacks = new String [stacks.length + 4];
		System.arraycopy (stacks, 0, newStacks, 0, stacks.length);
		stacks = newStacks;
	}
	String stack = name + "\n";
	if (elements != null) {
		for (int i=0; i<elements.length; i++) {
			stack += "\t" + elements[i] + "\n";
		}
	}
	stacks [itemCount] = stack;
	list.add (items [itemCount]);
	itemCount++;
}

protected void asyncExec (final Runnable runnable) {
	super.asyncExec (new Runnable () {
		public void run () {
			exec (runnable, null);
		}
	});
}

void exec (Runnable runnable, Event event) {
	final boolean [] done = new boolean [1];
	final StackTraceElement [] [] elements = new StackTraceElement [1][0];
	final Thread t = Thread.currentThread ();
	Thread timer = new Thread () {
		public void run () {
			try {
				Thread.sleep(TIME);
			} catch (Throwable th) {}
			if (!done [0]) elements[0] = t.getStackTrace();
		}
	};
	timer.start();
	long t1 = System.currentTimeMillis ();
	if (runnable != null) runnable.run ();
	if (event != null) {
		event.widget.notifyListeners (event.type, event);
	}
	done [0] = true;
	long t2 = System.currentTimeMillis ();
	if ((t2 - t1) > TIME) {
		String name = "";
		if (runnable != null) name += runnable.toString ();
		if (event != null) name += event.toString ();
		add (t2 - t1, name, elements [0]);
//		System.err.println("*** Time: " + (t2 - t1));
//		if (elements != null) {
//			for (int i=0; i<elements[0].length; i++) {
//				System.err.println("\t" + elements[0][i]);
//			}
//		}
	}
}

void install () {
	display.setSynchronizer(this);
	Listener filter = new Listener () {
		boolean ignore = false;
		public void handleEvent (Event event) {
			if (ignore) return;
			if (event.widget != null) {
				ignore = true;
				exec (null, event);
				event.type = SWT.None;
				ignore = false;
			}
		}
	};
	for (int i=EVENT_MIN; i<EVENT_MAX; i++) {
		display.addFilter (i, filter);
	}
}

protected void syncExec (final Runnable runnable) {
	super.syncExec (new Runnable () {
		public void run () {
			exec (runnable, null);
		}
	});
}
}
