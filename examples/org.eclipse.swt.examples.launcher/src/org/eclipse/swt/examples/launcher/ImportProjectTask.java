package org.eclipse.swt.examples.launcher;/* * (c) Copyright IBM Corp. 2000, 2001. * All Rights Reserved */import org.eclipse.core.internal.events.*;import org.eclipse.core.resources.*;import org.eclipse.core.runtime.*;import org.eclipse.jface.dialogs.*;import org.eclipse.swt.widgets.*;import org.eclipse.ui.dialogs.*;import org.eclipse.ui.internal.*;import org.eclipse.ui.wizards.datatransfer.*;import java.io.*;import java.lang.reflect.*;import java.net.*;import java.util.*;import java.util.zip.*;
/** * ImportProjectTask maintains information about a project import session and provides * all necessary user interface support. */
public class ImportProjectTask {	// Support for externalized project properties information	private static final String IMPORTPROJECT_PROPERTIES = "importproject.properties";	private static final String PROPERTYKEY_BUILDERS = "builders";	private static final String PROPERTYKEY_NATURES = "natures";		// Default project properties	private static final Properties defaultProjectProperties;	static {		defaultProjectProperties = new Properties();		defaultProjectProperties.setProperty(PROPERTYKEY_NATURES,			"org.eclipse.jdt.core.javanature");		defaultProjectProperties.setProperty(PROPERTYKEY_BUILDERS,			"org.eclipse.jdt.core.javabuilder");	}		// Internal data
	private Shell  parentShell;
	private String defaultProjectName;
	private URL    sourceUrl;
	
	/**
	 * Creates an ImportProjectTask specifying information about an import operation
	 * to be performed.
	 *
	 * @param parentShell the Shell instance to use in dialogs.
	 * @param defaultProjectName the default name of the project to import into
	 * @param sourceUrl the URL of the ZIP/JAR file which we will import
	 */
	public ImportProjectTask(Shell parentShell, String defaultProjectName, URL sourceUrl) {
		this.parentShell = parentShell;
		this.defaultProjectName = defaultProjectName;
		this.sourceUrl = sourceUrl;
	}
	
	/**
	 * Disposes of operating system and platform resources associated with this instance.
	 */
	public void dispose() {
		sourceUrl = null;
	}
	
	/**
	 * Perform the import.
	 * 
	 * @return true iff the import succeeded
	 */
	public boolean execute() {
		String projectName = getNewProjectName(parentShell, defaultProjectName);
		if (projectName == null) return false;
		return doImportZip(projectName);
	}

	/**
	 * Returns a project name into which to import a new item.
	 * 
	 * @param defaultProjectName the default name to give the project
	 * @return the name a new project, null if the user aborted
	 */
	private String getNewProjectName(Shell parentShell, final String defaultProjectName) {
		InputDialog inputDialog = new InputDialog(parentShell,
			LauncherPlugin.getResourceString("dialog.ImportPathInput.title"),
			LauncherPlugin.getResourceString("dialog.ImportPathInput.message"),
			defaultProjectName,
			new IInputValidator() {
			public String isValid(String projectPath) {
				// verify that name is well-formed
				IWorkspace workspace = WorkbenchPlugin.getPluginWorkspace();
				projectPath = new Path(projectPath).makeAbsolute().toString();
				IStatus status = workspace.validatePath(projectPath,
					IResource.PROJECT | IResource.FOLDER);
				if (! status.isOK()) {
					return LauncherPlugin.getResourceString("dialog.InvalidTargetProjectPath.interactive");
				}
				// verify that project does not already exist
				IWorkspaceRoot workspaceRoot = workspace.getRoot();
				IProject project = workspaceRoot.getProject(projectPath);
				if (project.exists()) {
					return LauncherPlugin.getResourceString("dialog.ExistingProjectPath.interactive");
				}
				return null;
			}
		});
		inputDialog.setBlockOnOpen(true);
		inputDialog.open();
		String path = inputDialog.getValue();
		inputDialog.close();

		return (inputDialog.getReturnCode() == InputDialog.OK) ? path : null;
	}	

	/**
	 * Imports a new project from a Zip file.
	 * Note that user feedback is provided when the result is false
	 *
	 * @param projectName the name to give the new project
	 * @return true if the operation succeeds
	 */
	private boolean doImportZip(String projectName) {
		final IPath projectPath = new Path(projectName).makeAbsolute();

		ZipFile zipFile = null;
		try {
			/* Open the Zip file and get a StructureProvider for it */			File file = URLHelper.urlToFile(sourceUrl);			if (file == null) throw new Exception();			
			zipFile = new ZipFile(file);
			ZipFileStructureProvider provider = new ZipFileStructureProvider(zipFile);
			
			return importExecute(projectPath, provider.getRoot(), provider);
		} catch (Exception e) {
			MessageDialog.openError(parentShell,
				LauncherPlugin.getResourceString("dialog.ImportProgramProblems.title"),
				LauncherPlugin.getResourceString("dialog.ErrorAccessingZipFile.message",
					new Object[] { sourceUrl.getFile(), e.getMessage() }));
			return false;
		} finally {
			try {
				if (zipFile != null) zipFile.close();
			} catch (IOException e) { }
		}
	}

	/**
	 * Performs an import operation
	 * Note that user feedback is provided when the result is false
	 *
	 * @param projectPath the absolute path of the project to create
	 * @param source the source node for the import
	 * @param provider the structure provider ImportOperation will use to retrieve file contents
	 * @return true if the operation succeeds
	 */
	private boolean importExecute(IPath projectPath, Object source, IImportStructureProvider provider) {
		/* Validate the target path */
		if (! importValidateProjectPath(projectPath)) return false;

		/* Locate the Project we will import into, or create it anew */
		IWorkspace workspace = WorkbenchPlugin.getPluginWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		String projectName = projectPath.toString();
		IProject project = workspaceRoot.getProject(projectName);

		/* Create a progress monitor for the import process */
		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(parentShell);
		progressDialog.setCancelable(true);
		progressDialog.open();

		/* Manipulate the ProjectDescription so that we can build the example */
		String exceptionMessage = null;
		try {
			if (! project.exists()) {
				/* Create the project */
				exceptionMessage = "dialog.ErrorCreatingNewProject.message";

				IProjectDescription projectDesc = workspace.newProjectDescription(projectName);
				project.create(projectDesc, progressDialog.getProgressMonitor());
			}
			/* Open Project */
			project.open(progressDialog.getProgressMonitor());
						/* Import project properties from disk */			Properties projectProperties = defaultProjectProperties;			try {				Object node = findStructuredChildElement(provider, source, IMPORTPROJECT_PROPERTIES);				if (node == null) {					InputStream is = provider.getContents(node);					Properties properties = new Properties();					properties.load(is);					projectProperties = properties;				}			} catch (Exception e) {			}			
			/* Set Project properties */
			exceptionMessage = "dialog.ErrorUpdatingProjectProperties.message";

			IProjectDescription projectDesc = project.getDescription();
			updateProjectDescription(projectDesc, projectProperties);
			project.setDescription(projectDesc, progressDialog.getProgressMonitor());
		} catch (Exception e) {
			if (progressDialog != null) progressDialog.close();
			if (exceptionMessage == null) exceptionMessage = "{0}";
			MessageDialog.openError(parentShell,
				LauncherPlugin.getResourceString("dialog.ImportProgramProblems.title"),
				LauncherPlugin.getResourceString(exceptionMessage, new Object[] { e.getMessage() }));
			return false;
		}
	
		/* Create the ImportOperation */
		ImportOperation importOperation = new ImportOperation(projectPath, source, provider,
			new IOverwriteQuery() {
				final String[] responses = new String[] {
					IOverwriteQuery.YES,
					IOverwriteQuery.NO,
					IOverwriteQuery.ALL,
					IOverwriteQuery.CANCEL};
				final String[] labels = new String[] {
					IDialogConstants.YES_LABEL, 
					IDialogConstants.NO_LABEL, 
					IDialogConstants.YES_TO_ALL_LABEL, 
					IDialogConstants.CANCEL_LABEL};

				/* called to query user if existing files should be overwritten */
				public String queryOverwrite(String pathString) {
					final MessageDialog dialog = 
						new MessageDialog(parentShell,
							LauncherPlugin.getResourceString("dialog.ImportProgramOverwrite.title"),
							null, LauncherPlugin.getResourceString("dialog.FileAlreadyExists.message",
								new Object[] { pathString }),
							MessageDialog.QUESTION, labels, 0); 
					// run in syncExec because callback is from an operation,
					// which is probably not running in the UI thread.
					parentShell.getDisplay().syncExec(new Runnable() {
						public void run() {
							dialog.open();
						}
					});
					return dialog.getReturnCode() < 0 ? CANCEL : responses[dialog.getReturnCode()];
				}
			});
		importOperation.setOverwriteResources(false); // ask user before overwriting...

		/* Execute the operation */
		try {
			progressDialog.run(true, true, importOperation);
		} catch (InterruptedException e) {
			// aborted
			return false;
		} catch (InvocationTargetException e) {
			// failed
			MessageDialog.openError(parentShell,
				LauncherPlugin.getResourceString("dialog.ImportProgramProblems.title"),
				e.getTargetException().getMessage());
			return false;
		}

		/* Verify that the operation succeeded */
		IStatus status = importOperation.getStatus();
		if (! status.isOK()) {
			ErrorDialog.openError(parentShell,
				LauncherPlugin.getResourceString("dialog.ImportProgramProblems.title"),
				LauncherPlugin.getResourceString("dialog.ImportFailed.message"),
				status);
			return false;
		}
		return true;
	}

	/**
	 * Updates a ProjectDescription prior to an import.
	 * 
	 * @param projectDesc the IProjectDescription to fix	 * @param properties a Properties file containing entries of the form	 *        natures = <string>[;<string>] and builders = <string>[;<string>]
	 */
	private void updateProjectDescription(IProjectDescription projectDesc, Properties properties) {		if (properties == null) return;		
		/* Add JDT and PDE nature */
		String[] natureIds = projectDesc.getNatureIds();		final String naturesList = properties.getProperty(PROPERTYKEY_NATURES);		if (naturesList != null) {			StringTokenizer entries = new StringTokenizer(naturesList, ";");			while (entries.hasMoreTokens()) {				natureIds = addUniqueString(natureIds, entries.nextToken());			}			projectDesc.setNatureIds(natureIds);		}

		/* Add JDT and PDE builders */
		ICommand[] buildSpecs = projectDesc.getBuildSpec();
		final String buildersList = properties.getProperty(PROPERTYKEY_BUILDERS);		if (buildersList != null) {			StringTokenizer entries = new StringTokenizer(buildersList, ";");			while (entries.hasMoreTokens()) {				buildSpecs = addUniqueCommand(buildSpecs, entries.nextToken(), new HashMap());			}			projectDesc.setBuildSpec(buildSpecs);		}	}

	/**
	 * Validates the target project path
	 *
	 * @param projectPath the project path to verify
	 * @return true if the path is valid
	 *         Note that user feedback is provided when the result is false
	 */
	private boolean importValidateProjectPath(IPath projectPath) {
		IWorkspace workspace = WorkbenchPlugin.getPluginWorkspace();
		IStatus status = workspace.validatePath(projectPath.toString(), IResource.PROJECT | IResource.FOLDER);
		if (! status.isOK()) {
			ErrorDialog.openError(parentShell,
				LauncherPlugin.getResourceString("dialog.ImportProgramProblems.title"),
				LauncherPlugin.getResourceString("dialog.InvalidTargetProjectPath.message",
					new Object[] { projectPath.toString() }),
				status);
			return false;
		}
		return true;
	}	

	/**
	 * Adds a unique String to a String[].
	 * 
	 * @param array the old String array
	 * @param string the new String
	 * @return the new String array (may be same as old)
	 */
	private String[] addUniqueString(String[] array, String string) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i].equals(string)) return array;
		}
		String[] newArray = new String[array.length + 1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[array.length] = string;
		return newArray;
	}

	/**
	 * Adds a unique ICommand to a ICommand[].
	 * 
	 * @param array the old ICommand array
	 * @param builderName the new builder name
	 * @param builderArgs the new arguments
	 * @return the new ICommand array (may be same as old)
	 */
	private ICommand[] addUniqueCommand(ICommand[] array, String builderName, Map builderArgs) {
		for (int i = 0; i < array.length; ++i) {
			final String name = array[i].getBuilderName();
			final Map args = array[i].getArguments();
			
			if ((name != null) && (name.equals(builderName))) return array;
		}
		ICommand[] newArray = new ICommand[array.length + 1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		BuildCommand newCommand = new BuildCommand();
		newCommand.setBuilderName(builderName);
		newCommand.setArguments(builderArgs);
		newArray[array.length] = newCommand;
		return newArray;
	}		/**	 * Finds a particular child of a container in an IImportStructureProvider.	 * 	 */	private Object findStructuredChildElement(IImportStructureProvider provider, Object parent, String name) {		if (! provider.isFolder(parent)) return null;				java.util.List list = provider.getChildren(parent);		if (list == null) return null;				for (Iterator it = list.iterator(); it.hasNext(); ) {			Object item = it.next();			if (provider.getLabel(item).equals(name)) return item;		}		return null;	}
}
