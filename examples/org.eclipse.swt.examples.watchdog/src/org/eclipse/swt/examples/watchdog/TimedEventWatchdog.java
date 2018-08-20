/*******************************************************************************
 * Copyright (c) 2013, Google Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Google Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.watchdog;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Synchronizer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This watchdog uses a {@link Timer} to take a stack trace during long events, trying to catch a
 * single long-running event in action.
 */
class TimedEventWatchdog implements Listener {
	/**
	 * Information captured about a set of events.
	 * <p>
	 * When a set contains more than 1 event, it means that additional events were processed
	 * recursively before the first event finished. If events are invoked recursively and any event
	 * is longer than a watchdog's threshold, each enclosing event will also be longer than the
	 * threshold and will be reported as the stack unwinds.
	 *
	 * @noextend This class is not intended to be subclassed by clients.
	 */
	private static class LongEventInfo {
		/**
		 * The sequence number of the first event in this set
		 */
		public final int startingSequenceNumber;

		/**
		 * The sequence number of the last event in this set
		 */
		public final int endingSequenceNumber;

		/**
		 * The start time of the first event, in milliseconds since 00:00 of 1 January 1970 Z.
		 * @see System#currentTimeMillis
		 */
		public final long start;

		/**
		 * The total duration of all events, in milliseconds
		 */
		public final long duration;

		/**
		 * The recursive depth of calls to events when this LongEventInfo was created.
		 */
		public final int depth;

		/**
		 * The maximum recursive depth of calls to events during this set of events
		 */
		public final int maxDepth;

		/**
		 * Constructs an event snapshot object from a contiguous range of events.
		 *
		 * @param startSeqNo the first event in this snapshot
		 * @param endSeqNo the last event in this snapshot
		 * @param start the start timestamp in milliseconds since 00:00 of 1 Jan 1970
		 * @param duration the duration of the captured events, in milliseconds
		 * @param depth the depth at which this snapshot started and ended
		 * @param maxDepth the maximum depth reached by any event captured by this snapshot
		 */
		public LongEventInfo(int startSeqNo, int endSeqNo, long start, long duration, int depth,
				int maxDepth) {
			this.startingSequenceNumber = startSeqNo;
			this.endingSequenceNumber = endSeqNo;
			this.start = start;
			this.duration = duration;
			this.depth = depth;
			this.maxDepth = maxDepth;
		}
	}

	private static final class StackTrace {
		public final Date captureTime;
		public final StackTraceElement[] stack;

		public StackTrace(Thread thread, long time_ms) {
			captureTime = new Date(time_ms);
			stack = thread.getStackTrace();
		}
	}

	private class StackNode {
		public int startingSequenceNumber = -1;
		public long startTime = -1;
	}

	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");

	private static final String EVENT_STR_FORMAT =
			"Event #%1$d-#%2$d: %3$dms from %4$s [depth = %5$d, max = %6$d]";

	private final long dataCollectionDelay; // in milliseconds
	private final long threshold_ms; // in milliseconds

	private StackNode[] stack = new StackNode[16];

	// Count of how many nested {@link Dispatch#readAndDisplay} calls are on the stack
	private int recursiveDepth = 0;

	// Sequence numbers should always be >0 so the logged sequence numbers progress in an intuitive
	// way and -1 can be used as a sentinel value
	private int dispatchSequenceNumber = 1;

	private int maxRecursiveDepth = 0;

	private final Timer timer;
	private final TimerTask onTickTask;
	private final Thread uiThread;

	private volatile StackTrace stackTrace;
	private volatile long grabStackAt;

	public TimedEventWatchdog(Thread uiThread, long threshold_ms) {
		if (uiThread == null) {
			throw new NullPointerException();
		}

		this.threshold_ms = threshold_ms;
		this.dataCollectionDelay = threshold_ms / 2;

		this.uiThread = uiThread;
		this.timer = new Timer("Monitoring data collection timer", true);
		this.onTickTask = new TimerTask() {
			@Override
			public void run() {
				poll();
			}
		};

		grabStackAt = Long.MAX_VALUE;
		timer.scheduleAtFixedRate(onTickTask, 0, Math.max(dataCollectionDelay / 2, 1));
	}

	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.PreEvent) {
			beginEvent();
		} else if (event.type == SWT.PostEvent) {
			endEvent();
		}
	}

	/**
	 * Called at the start of every event, just before invoking the event handler. This function is
	 * called on the UI thread very frequently and can have a significant impact on performance, so
	 * it should be as fast as possible.
	 */
	public void beginEvent() {
		int depth = recursiveDepth++;
		int seqNo = dispatchSequenceNumber;
		seqNo = (seqNo < Integer.MAX_VALUE ? seqNo + 1 : 1);
		dispatchSequenceNumber = seqNo;

		if (depth > maxRecursiveDepth) {
			maxRecursiveDepth = depth;
		}

		StackNode s;
		if (depth < stack.length) {
			s = stack[depth];
		} else {
			s = null;
			StackNode[] newStack = new StackNode[2 * depth];
			System.arraycopy(stack, 0, newStack, 0, stack.length);
			stack = newStack;
		}

		if (s == null) {
			stack[depth] = s = new StackNode();
		}

		s.startTime = getTimestamp();
		s.startingSequenceNumber = seqNo;

		// This function gets called very often, so it needs to be fast!
		stackTrace = null;
		grabStackAt = System.currentTimeMillis() + dataCollectionDelay; // Linearization point
	}

	/**
	 * Called at the end of every event, after that event has returned. This function is called on
	 * the UI thread very frequently and can have a significant impact on performance, so it
	 * should be as fast as possible.
	 */
	public void endEvent() {
		if (recursiveDepth == 0) return;
		int depth = --recursiveDepth;

		// If a subscriber is added during an asyncExec event (the typical way to subscribe),
		// then we don't have any start information collected and stack[depth] will still be
		// null, so skip reporting that event.
		StackNode s = depth < stack.length ? stack[depth] : null;
		if (s != null) {
			int duration = (int) (getTimestamp() - s.startTime);
			LongEventInfo info = null;

			if (duration >= threshold_ms) {
				if (info == null) {
					info = new LongEventInfo(s.startingSequenceNumber,
							dispatchSequenceNumber, s.startTime, duration, depth,
							maxRecursiveDepth);
				}

				onLongEvent(info);
			}
		}

		if (depth == 0) {
			maxRecursiveDepth = 0;
		}

		// This function gets called very often, so it needs to be fast!
		grabStackAt = Long.MAX_VALUE; // Linearization point
	}

	private long getTimestamp() {
		return System.currentTimeMillis();
	}

	/**
	 * If the duration of any single event was longer than a client's duration threshold, this
	 * callback is invoked after the event completes and before {@link #endEvent}. If the event
	 * completed faster than the threshold, this callback is not invoked.
	 * <p>
	 * When an event at depth > 0 is longer than a client's threshold, the enclosing event will
	 * also be longer and will be reported separately as the call stack unwinds.
	 * <p>
	 * This example simply logs stack traces for off-line analysis.
	 * <p>
	 * Example output:
	 * <pre>
	 * !ENTRY org.eclipse.swt.examples.watchdog 1 0 2013-05-20 11:43:59.253
	 * !MESSAGE Event #217-#217: 250ms from 11:43:59.002 [depth = 1, max = 1]
	 * 	Trace 11:43:59.150 (+148.0ms)
	 * 		org.eclipse.swt.examples.watchdog.WatchdogPlugin.longExample(WatchdogPlugin.java:68)
	 * 		org.eclipse.swt.examples.watchdog.WatchdogPlugin$1.run(WatchdogPlugin.java:62)
	 * 		org.eclipse.swt.widgets.Synchronizer.instrumentedRun(Synchronizer.java:247)
	 * 		org.eclipse.swt.widgets.Synchronizer.syncExec(Synchronizer.java:223)
	 * 		org.eclipse.ui.internal.UISynchronizer.syncExec(UISynchronizer.java:150)
	 * 		org.eclipse.swt.widgets.Display.syncExec(Display.java:4491)
	 * 		org.eclipse.swt.examples.watchdog.WatchdogPlugin$2.run(WatchdogPlugin.java:78)
	 * 		... 27 more
	 * </pre>
	 * @param event captured information about the long event
	 */
	public void onLongEvent(LongEventInfo event) {
		grabStackAt = Long.MAX_VALUE; // Linearization point
		StackTrace trace = stackTrace;
		stackTrace = null;

		if (trace != null) {
			String msg = String.format(EVENT_STR_FORMAT, event.startingSequenceNumber,
					event.endingSequenceNumber, event.duration,
					TIME_FORMAT.format(new Date(event.start)), event.depth, event.maxDepth);

			StringBuilder str = new StringBuilder(msg);

			str.append('\n');
			str.append('\t').append("Trace ").append(TIME_FORMAT.format(trace.captureTime));

			// Calculate when the stack trace happened relative to the start of the dispatch.
			double deltaTimeFromEventStart = trace.captureTime.getTime() - event.start;
			String unit = "ms";
			if (deltaTimeFromEventStart > 1000.0) {
				deltaTimeFromEventStart /= 1000.0;
				unit = "s";
			}
			deltaTimeFromEventStart = Math.round(deltaTimeFromEventStart * 10.0) / 10.0;
			str.append(" (+").append(deltaTimeFromEventStart).append(unit).append(')').append('\n');

			final String displayClassName = Display.class.getName();
			final String syncClassName = Synchronizer.class.getName();

			int numPrinted = 0;
			int maxToPrint = -1;
			for (StackTraceElement e : trace.stack) {
				str.append('\t').append('\t').append(e.toString()).append('\n');
				++numPrinted;

				// Limit number of stack elements printed to reasonable size
				if (traceElementIs(e, displayClassName, "readAndDispatch")) {
					maxToPrint = 0;
				} else if (traceElementIs(e, displayClassName, "syncExec")) {
					maxToPrint = 3;
				} else if (traceElementIs(e, syncClassName, "syncExec")) {
					maxToPrint = 3;
				}

				if (maxToPrint == 0) {
					str.append('\t').append('\t').append("... ")
						.append(trace.stack.length - numPrinted).append(" more").append('\n');
					break;
				} else if (maxToPrint > 0) {
					maxToPrint--;
				}
			}

			WatchdogPlugin.getDefault().getLog().log(new Status(IStatus.INFO,
					WatchdogPlugin.getDefault().getBundle().getSymbolicName(), str.toString()));
		}
	}

	private static boolean traceElementIs(StackTraceElement e, String className, String method) {
		return className.equals(e.getClassName()) && method.equals(e.getMethodName());
	}

	/**
	 * The data-collection is kept very simple for this example to focus on how to use the API
	 * without adding too much multithreading complexity.
	 * <p>
	 * For additional data collection, you could collect multiple stack traces, then log some or
	 * all of them in {@link #onLongEvent}. The timer can also log if too much time elapses between
	 * {@link #beginEvent} and {@link #endEvent} to help diagnose UI freezes and potential
	 * deadlocks.
	 * <p>
	 * This example uses {@link Timer} for simplicity. Using Timer does introduce an upper-bound on
	 * how much data can be collected. More in-depth or precise collection may require replacing
	 * the {@link Timer}-based polling with a dedicated thread.
	 */
	private void poll() {
		if (stackTrace == null) {
			long currTime = System.currentTimeMillis();

			if (currTime - grabStackAt > 0) { // Linearization point
				stackTrace = new StackTrace(uiThread, currTime);
			}
		}
	}
}
