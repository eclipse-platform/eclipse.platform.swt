package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

/**
 * Instances of this class are used to ensure that an
 * application cannot interfere with the locking mechanism
 * used to implement asynchonous and synchronous communication
 * between widgets and background threads.
 */

class RunnableLock {
	Runnable runnable;
	Thread thread;
	
RunnableLock (Runnable runnable) {
	this.runnable = runnable;
}

boolean done () {
	return runnable == null;
}

void run () {
	if (runnable != null) runnable.run ();
	runnable = null;
}

}
