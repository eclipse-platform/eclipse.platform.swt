/*******************************************************************************
 * Copyright (c) 2009, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.qt;

import com.trolltech.qt.QSignalEmitter.AbstractSignal;

/**
 * A {@code SignalConnector} is the representation of a certain signal,
 * receiver, method tuple. This tuple allows connecting and disconnecting from
 * the signal - without the need of constantly repeating (error-prone) the
 * necessary parameters.
 */
public class SignalConnector {

	private final AbstractSignal signal;
	private final Object receiver;
	private final String method;

	/**
	 * Create a {@code SignalConnector} from the given parameters.
	 * 
	 * @param signal
	 *            the signal
	 * @param receiver
	 *            the receiver of the signal
	 * @param method
	 *            the reveiver's method
	 */
	public SignalConnector(final AbstractSignal signal, final Object receiver, final String method) {
		this.signal = signal;
		this.receiver = receiver;
		this.method = method;
	}

	/**
	 * Connect the signal.
	 * 
	 * @return this object.
	 */
	public SignalConnector connect() {
		signal.connect(receiver, method);
		return this;
	}

	/**
	 * Disconnect the signal.
	 */
	public void disconnect() {
		signal.disconnect(receiver, method);
	}

	/**
	 * Run the given {@code Runnable} with disconnecting before and
	 * re-connection afterwards.
	 * 
	 * @param runnable
	 */
	public void runDisconnected(final Runnable runnable) {
		try {
			disconnect();
			runnable.run();
		} finally {
			connect();
		}
	}
}
