package org.eclipse.swt.examples.launcher;/* * (c) Copyright IBM Corp. 2000, 2001. * All Rights Reserved */import java.io.*;import java.util.*;import org.eclipse.core.boot.*;import org.eclipse.core.runtime.*;import org.eclipse.debug.core.*;import org.eclipse.debug.core.model.*;import org.eclipse.jdt.launching.*;/** * ApplicationRunner provides an interface to run registered Eclipse Platform applications * standalone in their own VM. */
public class ApplicationRunner {
	protected String   appName;
	protected String[] appArgs;
	protected String   appStatePath = null;
	protected String[] appPluginsPath = null;
	
	/**
	 * Constructs an ApplicationRunner
	 * 
	 * @param applicationName the id of a registered <code>org.eclipse.core.runtime.applications</code>
	 *        extension point
	 * @param args the arguments to pass to the application, or null if none
	 */
	public ApplicationRunner(String applicationName, String[] args) {
		this.appName = applicationName;
		this.appArgs = args;
	}
	
	/**
	 * Specify the location of the plugins directory used to launch the application and to locate
	 * plugins while the application is running.
	 * <p>
	 * The default is to let the Eclipse Platform search for the plugins directory in the manner
	 * specified by <code>org.eclipse.core.boot.BootLoader</code> when no -plugins argument is
	 * supplied to startup().  Generally, this means it will look in the current working directory
	 * for either a directory of plugins, or a file telling it where to find these directories.
	 * </p>
	 * 
	 * @param pluginPath an array of absolute filesystem paths to the plugins directories,
	 *        or null for the default
	 * @see #getCurrentPluginsPath
	 */
	public void setPluginsPath(String[] pluginsPath) {
		this.appPluginsPath = pluginsPath;
	}

	/**
	 * Specify the location of the applicate saved state directory used to store information
	 * about the application's state while it is running.
	 * <p>
	 * The default is to let the Eclipse Platform search for the state directory in the manner
	 * specified by <core>org.eclipse.core.boot.BootLoader</code> when no -platform argument is
	 * supplied to startup().  Generally, this means it will create its state in the current
	 * working directory.
	 * </p>
	 * 
	 * @param statePath the absolute filesystem path to the application state directory,
	 *        or null for the default
	 * @see #getCurrentStatePath
	 */
	public void setStatePath(String statePath) {
		this.appStatePath = statePath;
	}
	
	/**
	 * Returns the plugins directory path in the current Eclipse Platform instance.
	 * <p>
	 * Not implemented.  Returns null which is usually good enough.
	 * </p>
	 * 
	 * @return the current plugins directory absolute filesystem paths array
	 */
	public static String[] getCurrentPluginsPath() {
		return null;
	}

	/**
	 * Returns the state directory path in the current Eclipse Platform instance.
	 * 
	 * @return the current state directory absolute filesystem path
	 */
	public static String getCurrentStatePath() {
		return Platform.getLocation().toFile().getAbsolutePath();
	}

	/**
	 * Run the application asynchronously in its own VM and Eclipse Platform instance.
	 * 
	 * @return a VMRunnerResult with status and control information about the launch, may be null
	 */
	public VMRunnerResult run() {
		int numArgs = (appArgs != null) ? appArgs.length : 0;
		numArgs += 4;
		if (appPluginsPath != null && appPluginsPath.length != 0) numArgs += 2;
		if (appStatePath != null) numArgs += 2;
		
		String[] launcherArgs = new String[numArgs];

		int arg = 0;
		launcherArgs[arg++] = "-dev";          // tell Eclipse to look in bin subdirectories
		launcherArgs[arg++] = "bin";           //      under plugins directories as part of the classpath
		launcherArgs[arg++] = "-application";  // tell Eclipse to launch our application
		launcherArgs[arg++] = appName;
		
		if (appStatePath != null) {
			launcherArgs[arg++] = "-platform"; // tell Eclipse where its saved state should be placed
			launcherArgs[arg++] = appStatePath;
		}
		if (appPluginsPath != null && appPluginsPath.length != 0) {
			// Plugin path information must be stored on disk in a Properties file for some reason...
			Properties properties = new Properties();
			for (int i = 0; i < appPluginsPath.length; ++i) {
				properties.setProperty("pluginPathEntry" + i, appPluginsPath[i]);
			}

			try {
				File pathFile = File.createTempFile("tempPluginPaths", ".ini");
				pathFile.deleteOnExit();
	
				OutputStream os = new FileOutputStream(pathFile);
				properties.store(os, "");
				os.close();

				launcherArgs[arg++] = "-plugins";  // tell Eclipse where its plugins are stored
				launcherArgs[arg++] = pathFile.getAbsoluteFile().toURL().getFile();
			} catch (IOException e) {
				return null;
			}
		}
		if (appArgs != null) System.arraycopy(appArgs, 0, launcherArgs, arg, appArgs.length);

		return runJavaClass(getPlatformClass(), getPlatformClassPath(),
			new String[0], launcherArgs);
	}

	/**
	 * Determines if a VMRunnerResult indicates success or failure.
	 * <p>
	 * A return value of <code>true</code> should be taken on advisory only.  There is at present
	 * no general way of determining if the operation was completed successfully.
	 * </p>
	 * @param result the VMRunnerResult to check, null is permissible and causes a return value of false
	 * @return true if the VM started correctly (not a guarantee that the application will run!)
	 */
	public static boolean isResultOk(VMRunnerResult result) {
		if (result == null) return false;
		
		IProcess[] processes = result.getProcesses();
		return (processes != null && processes.length > 0);		
	}

	protected static VMRunnerResult runJavaClass(String className, String[] classPath,
		String[] vmArgs, String[] programArgs) {
		IVMRunner vmRunner = getJavaVMRunner();
		if (vmRunner == null) return null;
		
		VMRunnerConfiguration vmRunConfig = new VMRunnerConfiguration(className, classPath);
		vmRunConfig.setVMArguments(vmArgs);
		vmRunConfig.setProgramArguments(programArgs);
		return vmRunner.run(vmRunConfig);
	}

	protected static String getPlatformClass() {
		return "org.eclipse.core.launcher.UIMain";
	}

	protected static String getPlatformPath() {
		return URLHelper.urlToLocalFile(BootLoader.getInstallURL()).getAbsolutePath();
	}

	protected static String[] getPlatformClassPath() {
		final String platformPath = getPlatformPath();
		return new String[] {
			new File(platformPath, "startup.jar").getAbsolutePath(),
			new File(new File(platformPath, "bin"), "startup.jar").getAbsolutePath()
		};
	}

	protected static IVMRunner getJavaVMRunner() {
		// get the current install
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		if (vmInstall == null) {
			LauncherPlugin.logError(LauncherPlugin.getResourceString("run.error.CouldNotFindDefaultVM"), null);
			return null;
		}
			
		// get a runner		
		IVMRunner vmRunner = vmInstall.getVMRunner(ILaunchManager.RUN_MODE);
		if (vmRunner == null) {
			LauncherPlugin.logError(LauncherPlugin.getResourceString("run.error.CouldNotFindVMRunner",
				new Object[] { vmInstall.getName() }), null);
			return null;
		}
		return vmRunner;
	}
}
