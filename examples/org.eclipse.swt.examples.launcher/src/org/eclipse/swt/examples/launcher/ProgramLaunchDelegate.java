package org.eclipse.swt.examples.launcher;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.core.runtime.IPluginDescriptor;import org.eclipse.core.runtime.IPluginRegistry;import org.eclipse.core.runtime.Platform;import org.eclipse.core.runtime.PluginVersionIdentifier;import org.eclipse.ui.IViewPart;import java.io.PrintWriter;import java.io.StringWriter;import java.lang.reflect.InvocationTargetException;import java.lang.reflect.Method;import java.util.Arrays;import java.util.Comparator;

/**
 * A launch delegate for running a standalone program embedded inside a plugin package
 */
public class ProgramLaunchDelegate implements LaunchDelegate {
	private String mainClassName;
	private String pluginId;

	public ProgramLaunchDelegate(String pluginId, String mainClassName) {
		this.pluginId = pluginId;
		this.mainClassName = mainClassName;
	}

	public boolean launch(IViewPart hostView, PrintWriter logWriter) {
		logWriter.println(LauncherPlugin.getResourceString("run.InvocationSummaryHeader"));
		logWriter.println(LauncherPlugin.getResourceString("run.InvokingProgram",
			new Object[] { pluginId, mainClassName }));
		
		boolean result = launchHelper(hostView, logWriter);
		logWriter.println(LauncherPlugin.getResourceString("run.InvocationSummaryFooter"));
		return result;
	}

	public boolean launchHelper(IViewPart hostView, PrintWriter logWriter) {
		// get the platform's public plugin registry
		IPluginRegistry pluginRegistry = Platform.getPluginRegistry();
		// retrieve plugin descriptors for all plugins matching pluginId
		// [array may contain multiple versions of a given plugin]
		IPluginDescriptor[] pluginDescriptors = pluginRegistry.getPluginDescriptors(pluginId);
		
		if (pluginDescriptors == null || pluginDescriptors.length == 0) {
			logWriter.println(LauncherPlugin.getResourceString("run.error.CouldNotFindPlugin",
				new Object[] { pluginId }));
			return false;
		}
		
		// sort list of plugins in decreasing order by version number
		Arrays.sort(pluginDescriptors, new Comparator() {
			public int compare(Object a, Object b) {
				final PluginVersionIdentifier versionA = ((IPluginDescriptor) a).getVersionIdentifier();
				final PluginVersionIdentifier versionB = ((IPluginDescriptor) b).getVersionIdentifier();
				return versionA.isGreaterThan(versionB) ? -1 :
					(versionA.equals(versionB) ? 0 : 1);
			}
		});
		
		// attempt to load and run a program in decreasing order by version until we find one
		// that works or we run out of them
		Throwable exception = null;
		for (int i = 0; i < pluginDescriptors.length; ++i) {
			final IPluginDescriptor pd = pluginDescriptors[i];
			final ClassLoader loader = pd.getPluginClassLoader();
	
			try {
				Class programMainClass = Class.forName(mainClassName, true, loader);
				
				final Class[] parameterList = { String[].class };
				Method programMainMethod = programMainClass.getMethod("main", parameterList);
	
				final Object[] parameters = { new String[0] };
				programMainMethod.invoke(null, parameters);
				break;
			} catch (InvocationTargetException e) {
				logWriter.println(LauncherPlugin.getResourceString("run.error.Execution"));
				exception = e;
				break;
			} catch (Throwable e) {
				logWriter.println(LauncherPlugin.getResourceString("run.error.Invocation"));
				exception = e;
			}
		}
		// log exceptions
		if (exception != null) {
			logWriter.println(exception.toString());
			exception.printStackTrace(logWriter);
			return false;
		}
		return true;
	}
}

