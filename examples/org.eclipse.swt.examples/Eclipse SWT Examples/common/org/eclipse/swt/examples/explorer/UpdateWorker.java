package org.eclipse.swt.examples.explorer;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.widgets.*;
/**
 * Provides facilities for running worker threads that can be aborted and
 * restarted on the presence of new data.
 * 
 * Cooperates appropriately with the SWT event queue.
 */
public abstract class UpdateWorker implements Runnable {
	private Display display;

	private volatile Thread  thread;
	private volatile boolean stopped;
	private volatile boolean cancelled;
	private volatile Object  newData, activeData;
	
	/**
	 * Constructs an UpdateWorker
	 * 
	 * @param display the SWT Display running the event queue
	 */
	public UpdateWorker(Display display) {
		this.display = display;
		this.newData = null;
		thread = new Thread(this);
	}
	
	/**
	 * Stops the UpdateWorker and waits for it to terminate
	 */
	final public void syncStop() {
		synchronized(this) {
			cancelled = true;
			stopped = true;
			notifyAll();
		}
		while (thread != null && display.readAndDispatch()) display.sleep();
	}

	/**
	 * Notifies the UpdateWorker that it should update itself with new data.
	 * Cancels any previous operation and begins a new one.
	 * 
	 * @param data the new data argument
	 */
	final public void asyncUpdate(Object newData) {
		synchronized(this) {
			this.newData = newData;
			stopped = false;
			cancelled = true;
			notifyAll();
		}
		if (! thread.isAlive()) {
			stopped = false;
			thread.start();
		}
	}
	
	/**
	 * Notifies the UpdateWorker that it should quit what it is doing and start over
	 * with the same data argument as before.
	 */
	final public void asyncRestart() {
		asyncUpdate(newData);
	}
	
	/**
	 * Internal implementation Detail.
	 */
	final public void run() {
		while (! stopped) {
			try {
				synchronized(this) {
					cancelled = false;
					activeData = newData;
				}
				execute(activeData);
				synchronized(this) {
					if (! cancelled) wait();
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		thread = null;
	}
	
	/**
	 * Determines if the current session has been cancelled due to new data or a syncStop().
	 * <p>
	 * Subclass implementors should poll this and terminate processing as soon as possible.
	 * </p>
	 * @return true iff the current session has been cancelled
	 */
	final protected boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * Gets the data argument for the current session
	 * <p>
	 * Subclass implementors should check this value if they don't want to remember the value
	 * they received at execute() time.
	 * </p>
	 */
	final protected Object getData() {
		return activeData;
	}
	
	/**
	 * Executes an operation.
	 * <p>
	 * Subclass implementors must supply a definition for this method that performs the
	 * desired cancellable operation.
	 * </p>
	 * @param data the data argument for the current session
	 */
	protected abstract void execute(Object data);
}
