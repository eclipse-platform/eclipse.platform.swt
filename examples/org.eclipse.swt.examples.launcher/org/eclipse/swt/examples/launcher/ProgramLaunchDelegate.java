package org.eclipse.swt.examples.launcher;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.core.runtime.*;import org.eclipse.debug.core.*;import org.eclipse.debug.core.model.*;import org.eclipse.jdt.launching.*;import org.eclipse.ui.*;

/**
 * A launch delegate for running a standalone program embedded inside a plugin package.
 */
class ProgramLaunchDelegate implements LaunchDelegate {
	private String mainClassName;
	private String pluginId;

	public ProgramLaunchDelegate(String pluginId, String mainClassName) {
		this.pluginId = pluginId;
		this.mainClassName = mainClassName;
	}

	public boolean launch(IViewPart hostView) {
		boolean result = launchHelper(hostView);
		return result;
	}

	public boolean launchHelper(IViewPart hostView) {
		// get the platform's public plugin registry
		IPluginRegistry pluginRegistry = Platform.getPluginRegistry();
		// retrieve plugin descriptors for all plugins matching pluginId
		// [array may contain multiple versions of a given plugin]
		IPluginDescriptor[] pluginDescriptors = pluginRegistry.getPluginDescriptors(pluginId);
		
		if (pluginDescriptors == null || pluginDescriptors.length == 0) {
			LauncherPlugin.logError(LauncherPlugin.getResourceString("run.error.CouldNotFindPlugin",
				new Object[] { pluginId }), null);
			return false;
		}
		
		String[] appArgs = new String[] { "-appplugin", pluginId, "-appclass", mainClassName };
		ApplicationRunner runner = new ApplicationRunner(LauncherApplication.APPLICATION_ID, appArgs);
		runner.setPluginsPath(ApplicationRunner.getCurrentPluginsPath());
		runner.setStatePath(LauncherPlugin.getDefault().getStateLocation().toFile().getAbsolutePath());
		ILaunch result = runner.run();
		if (! ApplicationRunner.isResultOk(result)) return false;
		
		initWatch(result);
		return true;
	}

	private void initWatch(ILaunch result) {
		final IProcess[] processes = result.getProcesses();
		for (int i = 0; i < processes.length; ++i) {
			final IProcess process = processes[i];
			if (process == null) continue;
			final IStreamsProxy streamsProxy = process.getStreamsProxy();
			if (streamsProxy == null) continue;
			
			final IStreamMonitor outMonitor = streamsProxy.getOutputStreamMonitor();
			if (outMonitor != null) {
				outMonitor.addListener(new IStreamListener() {
					public void streamAppended(String text, IStreamMonitor monitor) {
						System.out.println(text);
					}
				});
			}
			
			final IStreamMonitor errMonitor = streamsProxy.getErrorStreamMonitor();
			if (errMonitor != null) {
				errMonitor.addListener(new IStreamListener() {
					public void streamAppended(String text, IStreamMonitor monitor) {
						System.err.println(text);
					}
				});
			}
		}			
	}
}

