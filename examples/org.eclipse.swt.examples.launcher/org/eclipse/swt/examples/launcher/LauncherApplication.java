package org.eclipse.swt.examples.launcher;/* * (c) Copyright IBM Corp. 2000, 2001. * All Rights Reserved */import java.lang.reflect.*;import java.util.*;import org.eclipse.core.boot.*;import org.eclipse.core.runtime.*;/** * LauncherApplication provides a mechanism for launching arbitrary executable programs * from within the Eclipse Platform.  This class binds to the * <core>org.eclipse.core.runtime.applications</code> extension point. */
public class LauncherApplication implements IPlatformRunnable {
	public static final String APPLICATION_ID = "org.eclipse.swt.examples.launcher.application";
		/**	 * Invokes the program specified in the arguments.	 * 	 * @param argsVanilla a String[] containing the arguments, one token per array index.	 * <ul>	 *   <li>-appclass &lt;class name&gt; the name of the class whose main() method is to be invoked	 *       [note: do not add a .java or .class suffix]	 *   <li>-appplugin &lt;pluginid&gt; the id of the plugin containing the specified class	 *   <li>-appargs &lt;args&gt; all remaining tokens to the right of this switch are passed as	 *       arguments to the main() method when the program is launched	 * </ul>	 * @return null	 */
	public Object run(Object argsVanilla) throws Exception {
		final String[] args = (String[]) argsVanilla;
		String   programPluginId = null; // id of plugin containing the program
		String[] programArgs = null;     // arguments for the program
		String   programClass = null;    // class with main() method
		
		try {
			for (int i = 0; i < args.length; ++i) {
				if (args[i].equalsIgnoreCase("-appplugin")) {
					programPluginId = args[++i];
				} else if (args[i].equalsIgnoreCase("-appclass")) {
					programClass = args[++i];
				} else if (args[i].equalsIgnoreCase("-appargs")) {
					++i;
					programArgs = new String[args.length - i];
					System.arraycopy(args, i, programArgs, 0, programArgs.length);
					break;
				} else {
					// ignore unrecognized argument
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("run.error.MalformedArgumentList");
		}
		
		if (programPluginId == null) throw new IllegalArgumentException(
			"run.error.MissingAppPlugin");
		if (programClass == null) throw new IllegalArgumentException(
			"run.error.MissincAppClass");
		if (programArgs == null) programArgs = new String[0];

		// get the platform's public plugin registry
		IPluginRegistry pluginRegistry = Platform.getPluginRegistry();
		// retrieve plugin descriptors for all plugins matching pluginId
		// [array may contain multiple versions of a given plugin]
		IPluginDescriptor[] pluginDescriptors = pluginRegistry.getPluginDescriptors(programPluginId);
		
		if (pluginDescriptors == null || pluginDescriptors.length == 0) {
			LauncherPlugin.logError(LauncherPlugin.getResourceString("run.error.CouldNotFindPlugin",
				new Object[] { programPluginId }), null);
			return null;
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
		for (int i = 0; i < pluginDescriptors.length; ++i) {
			final IPluginDescriptor pd = pluginDescriptors[i];
			final ClassLoader loader = pd.getPluginClassLoader();
	
			try {
				Class programMainClass = Class.forName(programClass, true, loader);
				
				final Class[] parameterList = { String[].class };
				Method programMainMethod = programMainClass.getMethod("main", parameterList);
	
				final Object[] parameters = { new String[0] };
				programMainMethod.invoke(null, parameters);				// SUCCESS!				return null;			} catch (InvocationTargetException e) {
				LauncherPlugin.logError(LauncherPlugin.getResourceString("run.error.Execution"), e);
				break; // it ran so stop searching
			} catch (Throwable e) {
				LauncherPlugin.logError(LauncherPlugin.getResourceString("run.error.Invocation"), e);
			}
		}
		LauncherPlugin.logError(LauncherPlugin.getResourceString("run.error.CouldNotFindMain"), null);
		return null;
	}
}
