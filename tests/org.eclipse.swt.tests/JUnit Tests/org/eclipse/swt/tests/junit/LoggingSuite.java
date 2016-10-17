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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * The {@code LoggingSuite} runner behaves like a normal {@link Suite}, but additionally
 * logs the start of each atomic test contained in the suite to {@code System.out}.
 */
public class LoggingSuite extends Suite {

	private RunListener loggingListener = new RunListener() {
		@Override
		public void testStarted(Description description) {
			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US).format(new Date());
			System.out.println("[" + now + "] " + description.getClassName() + "#" + description.getMethodName() + "()");
		}
	};

	public LoggingSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		super(klass, builder);
	}

	public LoggingSuite(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
		super(builder, classes);
	}

	public LoggingSuite(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
		super(klass, suiteClasses);
	}

	public LoggingSuite(Class<?> klass, List<Runner> runners) throws InitializationError {
		super(klass, runners);
	}

	public LoggingSuite(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
		super(builder, klass, suiteClasses);
	}

	@Override
	protected void runChild(Runner runner, RunNotifier notifier) {
		notifier.removeListener(loggingListener);
		notifier.addListener(loggingListener);
		super.runChild(runner, notifier);
	}
}
