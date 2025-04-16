/*******************************************************************************
 * Copyright (c) 2000, 2020 QNX Software Systems and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *     Martin Oberhuber (Wind River) - [303083] Split out the Spawner
 *     Red Hat Inc. - add flatpak support
 *     徐持恒 Xu Chiheng - refactor to help debugging
 *******************************************************************************/
package org.eclipse.swt.pty;

import java.io.*;

class ProcessFactory {

	static private ProcessFactory instance;
	private boolean hasSpawner;
	private Runtime runtime;

	private String[] modifyCmdArrayIfFlatpak(String[] cmdarray) {
		if (System.getenv("FLATPAK_SANDBOX_DIR") != null) { //$NON-NLS-1$
			String[] newArray = new String[cmdarray.length + 3];
			System.arraycopy(cmdarray, 0, newArray, 3, cmdarray.length);
			newArray[0] = "flatpak-spawn"; //$NON-NLS-1$
			newArray[1] = "--host"; //$NON-NLS-1$
			newArray[2] = "--watch-bus"; //$NON-NLS-1$
			cmdarray = newArray;
		}
		return cmdarray;
	}

	private class Builder {
		String[] cmdarray;
		String[] envp;
		File dir;
		boolean use_pty;
		PTY pty;
		boolean has_gracefulExitTimeMs;
		int gracefulExitTimeMs;

		public Builder(String[] cmdarray) throws IOException {
			if (cmdarray.length == 0 || cmdarray[0].isEmpty()) {
				throw new IllegalArgumentException("Empty command"); //$NON-NLS-1$
			}
			this.cmdarray = cmdarray;
			this.cmdarray = modifyCmdArrayIfFlatpak(this.cmdarray);
		}

		public Builder environment(String[] envp) {
			this.envp = envp;
			return this;
		}

		public Builder directory(File directory) {
			this.dir = directory;
			return this;
		}

		public Builder pty(PTY pty) {
			this.use_pty = true;
			this.pty = pty;
			return this;
		}

		public Builder gracefulExitTimeMs(int gracefulExitTimeMs) {
			this.has_gracefulExitTimeMs = true;
			this.gracefulExitTimeMs = gracefulExitTimeMs;
			return this;
		}

		public Process start() throws IOException {
			Process p;
			if (hasSpawner) {
				if (use_pty) {
					if (has_gracefulExitTimeMs) {
						p = new Spawner(cmdarray, envp, dir, pty, gracefulExitTimeMs);
					} else {
						p = new Spawner(cmdarray, envp, dir, pty);
					}
				} else {
					if (has_gracefulExitTimeMs) {
						p = new Spawner(cmdarray, envp, dir, gracefulExitTimeMs);
					} else {
						p = new Spawner(cmdarray, envp, dir);
					}
				}
			} else {
				if (use_pty || has_gracefulExitTimeMs) {
					throw new UnsupportedOperationException(Messages.Util_exception_cannotCreatePty);
				} else {
					p = runtime.exec(cmdarray, envp, dir);
				}
			}
			return p;
		}
	}

	private ProcessFactory() {
		hasSpawner = false;
		runtime = Runtime.getRuntime();
		try {
			System.loadLibrary("spawner"); //$NON-NLS-1$
			hasSpawner = true;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (UnsatisfiedLinkError e) {
			System.err.println(e.getMessage());
		}
	}

	public static ProcessFactory getFactory() {
		if (instance == null)
			instance = new ProcessFactory();
		return instance;
	}

	public Process exec(String[] cmdarray) throws IOException {
		Process p = new Builder(cmdarray).start();
		return p;
	}

	/**
	 * @since 6.2
	 */
	public Process exec(String[] cmdarray, int gracefulExitTimeMs) throws IOException {
		Process p = new Builder(cmdarray).gracefulExitTimeMs(gracefulExitTimeMs).start();
		return p;
	}

	public Process exec(String[] cmdarray, String[] envp) throws IOException {
		Process p = new Builder(cmdarray).environment(envp).start();
		return p;
	}

	/**
	 * @since 6.2
	 */
	public Process exec(String[] cmdarray, String[] envp, int gracefulExitTimeMs) throws IOException {
		Process p = new Builder(cmdarray).environment(envp).gracefulExitTimeMs(gracefulExitTimeMs).start();
		return p;
	}

	public Process exec(String cmdarray[], String[] envp, File dir) throws IOException {
		Process p = new Builder(cmdarray).environment(envp).directory(dir).start();
		return p;
	}

	/**
	 * @since 6.2
	 */
	public Process exec(String cmdarray[], String[] envp, File dir, int gracefulExitTimeMs) throws IOException {
		Process p = new Builder(cmdarray).environment(envp).directory(dir).gracefulExitTimeMs(gracefulExitTimeMs)
				.start();
		return p;
	}

	public Process exec(String cmdarray[], String[] envp, File dir, PTY pty) throws IOException {
		Process p = new Builder(cmdarray).environment(envp).directory(dir).pty(pty).start();
		return p;
	}

	/**
	 * @since 6.2
	 */
	public Process exec(String cmdarray[], String[] envp, File dir, PTY pty, int gracefulExitTimeMs)
			throws IOException {
		Process p = new Builder(cmdarray).environment(envp).directory(dir).pty(pty)
				.gracefulExitTimeMs(gracefulExitTimeMs).start();
		return p;
	}
}
