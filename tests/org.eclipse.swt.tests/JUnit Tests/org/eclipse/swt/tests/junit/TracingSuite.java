/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.io.PrintStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.test.Screenshots;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * The {@code TracingSuite} runner behaves like a normal {@link Suite}, but additionally logs the
 * start of each atomic test contained in the suite to {@code System.out}, and it tries to collect
 * more information after a timeout.
 * <p>
 * For atomic tests that run longer than 10 minutes, it tries to take a stack trace and a screenshot,
 * and then it tries to stop the "main" thread with an IllegalStateException.
 * <p>
 * Usage: Modify an existing JUnit 4 suite class or create a new one like this:
 * <pre>
@RunWith(TracingSuite.class)
@SuiteClasses(YourTestClass.class)
public class JUnit4IsCrap { }
</pre>
 * Directly annotating an existing JUnit 4 class that contains tests doesn't work.
 */
public class TracingSuite extends Suite {

	private static final int MAX_SCREENSHOT_COUNT = 5;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Inherited
	public @interface TracingOptions {

		/**
		 * @return true iff start times of atomic tests should be logged to {@code System.out}
		 */
		public boolean logTestStart() default true;

		/**
		 * @return the number of seconds after which a thread dump is initiated,
		 *         or 0 if no timer should be started
		 */
		public long stackDumpTimeoutSeconds() default 10 * 60;

		/**
		 * @return true iff the main thread should get stopped by an
		 *         {@link IllegalStateException} (only happens after a
		 *         successful stack dump)
		 */
		public boolean stopMainThread() default true;
	}

	private TracingOptions fTracingOptions;

	private class LoggingRunNotifier extends RunNotifier {
		private RunNotifier fNotifier;
		private Timer fTimer = new Timer(true);
		private ConcurrentHashMap<Description, TimerTask> fRunningTests = new ConcurrentHashMap<>();

		public LoggingRunNotifier(RunNotifier notifier) {
			fNotifier = notifier;
		}

		@Override
		public void addListener(RunListener listener) {
			fNotifier.addListener(listener);
		}

		@Override
		public void removeListener(RunListener listener) {
			fNotifier.removeListener(listener);
		}

		@Override
		public void fireTestRunStarted(Description description) {
			fNotifier.fireTestRunStarted(description);
		}

		@Override
		public void fireTestRunFinished(Result result) {
			fNotifier.fireTestRunFinished(result);
		}

		@Override
		public void fireTestStarted(Description description) throws StoppedByUserException {
			Date start = new Date();
			if (fTracingOptions.logTestStart()) {
				String message = format(start, description);
				System.out.println(message);
			}

			long seconds = fTracingOptions.stackDumpTimeoutSeconds();
			if (seconds != 0) {
				DumpTask task = new DumpTask(description);
				fRunningTests.put(description, task);
				fTimer.schedule(task, seconds * 1000);
			}
			fNotifier.fireTestStarted(description);
		}

		@Override
		public void fireTestFailure(Failure failure) {
			fNotifier.fireTestFailure(failure);
		}

		@Override
		public void fireTestAssumptionFailed(Failure failure) {
			fNotifier.fireTestAssumptionFailed(failure);
		}

		@Override
		public void fireTestIgnored(Description description) {
			fNotifier.fireTestIgnored(description);
		}

		@Override
		public void fireTestFinished(Description description) {
			TimerTask task = fRunningTests.remove(description);
			if (task != null) {
				task.cancel();
			}
			fNotifier.fireTestFinished(description);
		}

		@Override
		public void pleaseStop() {
			fNotifier.pleaseStop();
		}

		@Override
		public void addFirstListener(RunListener listener) {
			fNotifier.addFirstListener(listener);
		}
	}

	private class DumpTask extends TimerTask {
		private volatile int fScreenshotCount;
		private Description fDescription;

		public DumpTask(Description description) {
			fDescription = description;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			// There are situation where a blocked main thread apparently also blocks output to
			// System.err. Try to dump to System.out first. If both dumps get through, the short
			// delay may help identify threads that are still running.
			dumpStackTraces(System.out);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e2) {
				// won't happen, continue
			}
			Thread main = dumpStackTraces(System.err);

			if (fScreenshotCount < MAX_SCREENSHOT_COUNT) {
				String screenshotFile = Screenshots.takeScreenshot(TracingSuite.class, Integer.toString(fScreenshotCount++));
				System.err.println("Timeout screenshot saved to " + screenshotFile);
			}

			if (main != null && fTracingOptions.stopMainThread()) {
				Throwable toThrow = new IllegalStateException("main thread killed by LoggingSuite timeout");
				toThrow.initCause(new RuntimeException(toThrow.getMessage()));
				// Set the stack trace to that of the target thread.
				toThrow.setStackTrace(main.getStackTrace());
				try {
					main.stop(toThrow);
				} catch (UnsupportedOperationException e) {
					// Thread#stop(Throwable) doesn't work any more in JDK 8. Try stop0:
					try {
						Method stop0 = Thread.class.getDeclaredMethod("stop0", Object.class);
						stop0.setAccessible(true);
						stop0.invoke(main, toThrow);
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		private Thread dumpStackTraces(PrintStream stream) {
			String message = format(new Date(), fDescription) + " ran for more than " + (long) (10 * 60) + " seconds";
			stream.println(message);

			stream.format("totalMemory:           %11d\n", Runtime.getRuntime().totalMemory());
			stream.format("freeMemory (before GC):%11d\n", Runtime.getRuntime().freeMemory());
			System.gc();
			stream.format("freeMemory (after GC): %11d\n", Runtime.getRuntime().freeMemory());

			Thread main = null;
			Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
			for (Entry<Thread, StackTraceElement[]> entry : stackTraces.entrySet()) {
				String name = entry.getKey().getName();
				if ("main".equals(name)) {
					main = entry.getKey();
				}
				StackTraceElement[] stack = entry.getValue();
				ThreadDump exception = new ThreadDump("for thread \"" + name + "\"");
				exception.setStackTrace(stack);
				exception.printStackTrace(stream);
			}
			return main;
		}
	}

	@TracingOptions // serves as default value provider -- has nothing to do with ThreadDump
	static class ThreadDump extends Exception {
		private static final long serialVersionUID = 1L;
		ThreadDump(String message) {
			super(message);
		}
	}

	private static String format(Date time, Description description) {
		String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US).format(time);
		String message = "[" + now + "] " + description.getClassName() + "#" + description.getMethodName() + "()";
		return message;
	}

	public TracingSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		super(klass, builder);
		fTracingOptions = Optional.ofNullable(klass.getAnnotation(TracingOptions.class)).orElseGet(
				() -> ThreadDump.class.getAnnotation(TracingOptions.class));
	}

	@Override
	protected void runChild(Runner runner, RunNotifier notifier) {
		super.runChild(runner, new LoggingRunNotifier(notifier));
	}
}