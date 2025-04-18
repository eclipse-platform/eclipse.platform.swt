/*******************************************************************************
 * Copyright (c) 2000, 2014 QNX Software Systems and others.
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
 *     Wind River Systems   - bug 248071, bug 286162
 *     Martin Oberhuber (Wind River) - [303083] Split out the Spawner
 *******************************************************************************/
package org.eclipse.swt.pty;

import java.io.*;

public class Spawner extends Process {

	private final static int SIG_NOOP = 0;
	private final static int SIG_HUP = 1;
	private final static int SIG_KILL = 9;
	private final static int SIG_TERM = 15;

	/**
	 * On Windows, what this does is far from easy to explain.
	 * Some of the logic is in the JNI code, some in the spawner.exe code.
	 *
	 * <ul>
	 * <li>If the process this is being raised against was launched by us (the Spawner)
	 *    <ul>
	 *    <li>If the process is a cygwin program (has the cygwin1.dll loaded), then issue a 'kill -SIGINT'. If
	 *    the 'kill' utility isn't available, send the process a CTRL-C
	 *    <li>If the process is <i>not</i> a cygwin program, send the process a CTRL-C
	 *    </ul>
	 * <li>If the process this is being raised against was <i>not</i> launched by us, use
	 * DebugBreakProcess to interrupt it (sending a CTRL-C is easy only if we share a console
	 * with the target process)
	 * </ul>
	 *
	 * On non-Windows, raising this just raises a POSIX SIGINT
	 */
	private final static int SIG_INT = 2;

	/**
	 * A fabricated signal number for use on Windows only. Tells the starter program to send a CTRL-C
	 * regardless of whether the process is a Cygwin one or not.
	 */
	private final static int SIG_CTRLC = 1000; // arbitrary high number to avoid collision

	private final static int DEFAULT_GRACEFUL_EXIT_TIME_MS;

	static {
		String timeStr = System.getProperty("org.eclipse.cdt.core.graceful_exit_time_ms"); //$NON-NLS-1$
		int time = 1000;
		if (timeStr != null) {
			try {
				time = Integer.parseInt(timeStr);
			} catch (NumberFormatException e) {
				System.err.println(
						"Failed to parse system property. Falling back to " + time + " ms graceful exit time: "+e);
			}
		}
		DEFAULT_GRACEFUL_EXIT_TIME_MS = time;
	}

	int pid = 0;
	int status;
	final IChannel[] fChannels = { null, null, null };
	OutputStream out;
	InputStream in;
	InputStream err;
	private PTY fPty;

	private final int fGracefulExitTimeMs;

	private static enum State {
		RUNNING, DESTROYING, DONE
	}

	private State fState = State.RUNNING;

	Spawner(String[] cmdarray, String[] envp, File dir) throws IOException {
		this(cmdarray, envp, dir, DEFAULT_GRACEFUL_EXIT_TIME_MS);
	}

	/**
	 * Executes the specified command and arguments in a separate process with the
	 * specified environment and working directory.
	 * @since 6.2
	 **/
	Spawner(String[] cmdarray, String[] envp, File dir, int gracefulExitTimeMs) throws IOException {
		fGracefulExitTimeMs = gracefulExitTimeMs;
		String dirpath = "."; //$NON-NLS-1$
		if (dir != null)
			dirpath = dir.getAbsolutePath();
		exec(cmdarray, envp, dirpath);
	}

	Spawner(String[] cmdarray, String[] envp, File dir, PTY pty) throws IOException {
		this(cmdarray, envp, dir, pty, DEFAULT_GRACEFUL_EXIT_TIME_MS);
	}

	/**
	 * @since 6.2
	 */
	Spawner(String[] cmdarray, String[] envp, File dir, PTY pty, int gracefulExitTimeMs) throws IOException {
		fGracefulExitTimeMs = gracefulExitTimeMs;
		String dirpath = "."; //$NON-NLS-1$
		if (dir != null)
			dirpath = dir.getAbsolutePath();
		fPty = pty;
		exec_pty(cmdarray, envp, dirpath, pty);
	}

	/**
	 * Executes the specified command and arguments in a separate process.
	 **/
	Spawner(String[] cmdarray) throws IOException {
		this(cmdarray, null);
	}

	/**
	 * @since 6.2
	 */
	Spawner(String[] cmdarray, int gracefulExitTimeMs) throws IOException {
		this(cmdarray, null, gracefulExitTimeMs);
	}

	/**
	 * Executes the specified command and arguments in a separate process with the
	 * specified environment.
	 **/
	Spawner(String[] cmdarray, String[] envp) throws IOException {
		this(cmdarray, envp, null);
	}

	/**
	 * @since 6.2
	 */
	Spawner(String[] cmdarray, String[] envp, int gracefulExitTimeMs) throws IOException {
		this(cmdarray, envp, null, gracefulExitTimeMs);
	}

	@Override
	protected void finalize() throws Throwable {
		closeUnusedStreams();
	}

	/**
	 * See java.lang.Process#getInputStream ();
	 * The client is responsible for closing the stream explicitly.
	 **/
	@Override
	public synchronized InputStream getInputStream() {
		if (null == in) {
			if (fPty != null) {
				in = fPty.getInputStream();
			} else {
				in = new SpawnerInputStream(fChannels[1]);
			}
		}
		return in;
	}

	/**
	 * See java.lang.Process#getOutputStream ();
	 * The client is responsible for closing the stream explicitly.
	 **/
	@Override
	public synchronized OutputStream getOutputStream() {
		if (null == out) {
			if (fPty != null) {
				out = fPty.getOutputStream();
			} else {
				out = new SpawnerOutputStream(fChannels[0]);
			}
		}
		return out;
	}

	/**
	 * See java.lang.Process#getErrorStream ();
	 * The client is responsible for closing the stream explicitly.
	 **/
	@Override
	public synchronized InputStream getErrorStream() {
		if (null == err) {
			if (fPty != null && !fPty.isConsole()) {
				// If PTY is used and it's not in "Console" mode, then stderr is
				// redirected to the PTY's output stream.  Therefore, return a
				// dummy stream for error stream.
				err = new InputStream() {
					@Override
					public int read() throws IOException {
						return -1;
					}
				};
			} else {
				err = new SpawnerInputStream(fChannels[2]);
			}
		}
		return err;
	}

	/**
	 * See java.lang.Process#waitFor ();
	 **/
	@Override
	public synchronized int waitFor() throws InterruptedException {
		while (fState != State.DONE) {
			wait();
		}

		// For situations where the user does not call destroy(),
		// we try to kill the streams that were not used here.
		// We check for streams that were not created, we create
		// them to attach to the pipes, and then we close them
		// to release the pipes.
		// Streams that were created by the client need to be
		// closed by the client itself.
		//
		// But 345164
		closeUnusedStreams();
		return status;
	}

	/**
	 * See java.lang.Process#exitValue ();
	 **/
	@Override
	public synchronized int exitValue() {
		if (fState != State.DONE) {
			throw new IllegalThreadStateException("Process not Terminated"); //$NON-NLS-1$
		}
		return status;
	}

	/**
	 * See java.lang.Process#destroy ();
	 *
	 * Clients are responsible for explicitly closing any streams
	 * that they have requested through
	 *   getErrorStream(), getInputStream() or getOutputStream()
	 **/
	@Override
	public synchronized void destroy() {
		switch (fState) {
		case RUNNING:
			fState = State.DESTROYING;

			// Sends the TERM
			terminate();

			// Close the streams on this side.
			//
			// We only close the streams that were
			// never used by any client.
			// So, if the stream was not created yet,
			// we create it ourselves and close it
			// right away, so as to release the pipe.
			// Note that even if the stream was never
			// created, the pipe has been allocated in
			// native code, so we need to create the
			// stream and explicitly close it.
			//
			// We don't close streams the clients have
			// created because we don't know when the
			// client will be finished using them.
			// It is up to the client to close those
			// streams.
			//
			// But 345164
			closeUnusedStreams();

			// Grace before using the heavy gun.
			if (fState != State.DONE) {
				try {
					wait(fGracefulExitTimeMs);
				} catch (InterruptedException e) {
				}
			}
			if (fState != State.DONE) {
				kill();
			}
			break;

		case DESTROYING:
		case DONE:
			// Nothing to do
			break;
		}
	}

	@Override
	public long pid() {
		return pid;
	}

	/**
	 * On Windows, interrupt the spawned program by using Cygwin's utility 'kill -SIGINT' if it's a Cgywin
	 * program, otherwise send it a CTRL-C. If Cygwin's 'kill' command is not available, send a CTRL-C. On
	 * linux, interrupt it by raising a SIGINT.
	 */
	public int interrupt() {
		return raise(pid, SIG_INT);
	}

	/**
	 * On Windows, interrupt the spawned program by send it a CTRL-C (even if it's a Cygwin program). On
	 * linux, interrupt it by raising a SIGINT.
	 *
	 * @since 5.2
	 */
	public int interruptCTRLC() {
		if (EnvironmentReader.isWindows) {
			return raise(pid, SIG_CTRLC);
		} else {
			return interrupt();
		}
	}

	public int hangup() {
		return raise(pid, SIG_HUP);
	}

	public int kill() {
		return raise(pid, SIG_KILL);
	}

	public int terminate() {
		return raise(pid, SIG_TERM);
	}

	public boolean isRunning() {
		return (raise(pid, SIG_NOOP) == 0);
	}

	/**
	 * @since 6.3
	 * @return the current pty instance for this spawner
	 */
	public PTY pty() {
		return fPty;
	}

	private void exec(String[] cmdarray, String[] envp, String dirpath) throws IOException {
		if (envp == null)
			envp = new String[0];

		Reaper reaper = new Reaper(cmdarray, envp, dirpath);
		reaper.setDaemon(true);
		reaper.start();

		// Wait until the subprocess is started or error.
		synchronized (this) {
			while (pid == 0) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}

		// Check for errors.
		if (pid == -1) {
			throw new IOException(reaper.getErrorMessage());
		}
	}

	private void exec_pty(String[] cmdarray, String[] envp, String dirpath, final PTY pty) throws IOException {
		if (envp == null)
			envp = new String[0];

		Reaper reaper = new Reaper(cmdarray, envp, dirpath) {
			@Override
			int execute(String[] cmd, String[] env, String dir, IChannel[] channels) throws IOException {
				return pty.exec_pty(Spawner.this, cmd, env, dir, channels);
			}

			@Override
			protected int waitFor(int pid) {
				return pty.waitFor(Spawner.this, pid);
			}
		};
		reaper.setDaemon(true);
		reaper.start();

		// Wait until the subprocess is started or error.
		synchronized (this) {
			while (pid == 0) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}

		// Check for errors.
		if (pid == -1) {
			throw new IOException("Exec_tty error:" + reaper.getErrorMessage()); //$NON-NLS-1$
		}
	}

	public void exec_detached(String[] cmdarray, String[] envp, String dirpath) throws IOException {
		if (envp == null)
			envp = new String[0];
		pid = exec1(cmdarray, envp, dirpath);
		if (pid == -1) {
			throw new IOException("Exec error"); //$NON-NLS-1$
		}
	}

	/**
	 * Close any streams not used by clients.
	 */
	private synchronized void closeUnusedStreams() {
		try {
			if (null == err)
				getErrorStream().close();
		} catch (IOException e) {
		}
		try {
			if (null == in)
				getInputStream().close();
		} catch (IOException e) {
		}
		try {
			if (null == out)
				getOutputStream().close();
		} catch (IOException e) {
		}
	}

	/**
	 * Native method use in normal exec() calls.
	 */
	native int exec0(String[] cmdarray, String[] envp, String dir, IChannel[] chan) throws IOException;

	/**
	 * Native method use in no redirect meaning to streams will created.
	 */
	native int exec1(String[] cmdarray, String[] envp, String dir) throws IOException;

	/**
	 * Native method when executing with a terminal emulation.
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public native int exec2(String[] cmdarray, String[] envp, String dir, IChannel[] chan, String slaveName,
			int masterFD, boolean console) throws IOException;

	/**
	 * Native method to drop a signal on the process with pid.
	 */
	public native int raise(int processID, int sig);

	/**
	 * @since 6.2
	 */
	public int raise(int sig) {
		return raise(pid, sig);
	}

	/**
	 * Native method to wait(3) for process to terminate.
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public native int waitFor(int processID);

	static {
		try {
			System.loadLibrary("spawner"); //$NON-NLS-1$
			//TODO use system properties
//			configureNativeTrace(Platform.getDebugBoolean(CNativePlugin.PLUGIN_ID + "/debug/spawner"), //$NON-NLS-1$
//					Platform.getDebugBoolean(CNativePlugin.PLUGIN_ID + "/debug/spawner/details"), //$NON-NLS-1$
//					Platform.getDebugBoolean(CNativePlugin.PLUGIN_ID + "/debug/spawner/starter"), //$NON-NLS-1$
//					Platform.getDebugBoolean(CNativePlugin.PLUGIN_ID + "/debug/spawner/read_report")); //$NON-NLS-1$
		} catch (SecurityException e) {
			System.err.println(e);
		} catch (UnsatisfiedLinkError e) {
			System.err.println(e);
		}
	}

	/**
	 * @since 6.0
	 */
	private static native void configureNativeTrace(boolean spawner, boolean spawnerDetails, boolean starter,
			boolean readReport);

	/**
	 * @since 6.0
	 */
	public static interface IChannel {
	}

	/**
	 * @since 6.0
	 */
	public static class WinChannel implements IChannel {
		final long handle;

		public WinChannel(long handle) {
			this.handle = handle;
		}
	}

	/**
	 * @since 6.0
	 */
	public static class UnixChannel implements IChannel {
		final int fd;

		public UnixChannel(int fd) {
			this.fd = fd;
		}
	}

	// Spawn a thread to handle the forking and waiting
	// We do it this way because on linux the SIGCHLD is
	// send to the one thread.  So do the forking and
	// the wait in the same thread.
	class Reaper extends Thread {
		String[] fCmdarray;
		String[] fEnvp;
		String fDirpath;
		volatile Throwable fException;

		public Reaper(String[] array, String[] env, String dir) {
			super("Spawner Reaper"); //$NON-NLS-1$
			fCmdarray = array;
			fEnvp = env;
			fDirpath = dir;
			fException = null;
		}

		int execute(String[] cmdarray, String[] envp, String dir, IChannel[] channels) throws IOException {
			return exec0(cmdarray, envp, dir, channels);
		}

		int waitFor(int pid) {
			return Spawner.this.waitFor(pid);
		}

		@Override
		public void run() {
			int _pid;
			try {
				_pid = execute(fCmdarray, fEnvp, fDirpath, fChannels);
			} catch (Exception e) {
				_pid = -1;
				fException = e;
			}

			// Tell spawner that the process started.
			synchronized (Spawner.this) {
				pid = _pid;
				Spawner.this.notifyAll();
			}

			if (_pid != -1) {
				// Sync with spawner and notify when done.
				status = waitFor(pid);
				synchronized (Spawner.this) {
					fState = Spawner.State.DONE;
					Spawner.this.notifyAll();
				}
			}
		}

		public String getErrorMessage() {
			final String reason = fException != null ? fException.getMessage() : "Unknown reason"; //$NON-NLS-1$
			return Messages.Util_error_cannotRun.replace("{0}", fCmdarray[0]).replace("{1}", reason);
		}
	}
}
