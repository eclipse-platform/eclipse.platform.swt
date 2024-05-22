# Global settings folder

This folder contains preferences that should be applied to all projects/bundles in the sibling folders.

## Motivation

* Reduce maintenance effort
* Consistency: Apply the same preferences to all projects in the same category

## How to use this folder

If you want to share some configurations accross projects, you need to:
1. Create the configuration file: use the preferences dialog in Eclipse to do it
2. In the system explorer, move the configuration file to this folder
3. Inside Eclipse, create a link under the `.settings` folder and point it to the file in the `.settings_global` folder. **IMPORTANT: name the link exactly as the file so Eclipse recognizes it as the appropriate settings file.**

Example: sharing the formatting options accross all _SWT Examples_:
1. Create the proper **project-specific** configuration in one of the projects
2. In the system explorer, move the file `org.eclipse.jdt.core.prefs` from `<ECLIPSE_ROOT>/git/eclipse.platform.swt/examples/<PROJECT>/.settings/` to `<ECLIPSE_ROOT>/git/eclipse.platform.swt/examples/.settings_global/`
3. In Eclipse, create the link in the `.settings` folder:
  * Name: `org.eclipse.jdt.core.prefs`
  * Destination `PROJECT_LOC/../.settings_global/org.eclipse.jdt.core.prefs`  

## Other alternatives

For a future alternative to this approach, check this discussion in GitHub:
*  [Support hierarchical project preferences #89](https://github.com/eclipse-platform/eclipse.platform/issues/89)