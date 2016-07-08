org.eclipse.swt
===============

Main plug-in for the SWT user interface library.

Setting the classpath:
----------------------

To compile this project you need to set the classpath specific for your operating and windowing system.
For this, rename one of the following files to .classpath.

* .classpath_win32 - Windows
* .classpath_cocoa - Mac OS
* .classpath_gtk - Linux and all Unix variants

To see these files you may have to remove the filter for .file in the Project Explorer.
Use the view menu and Custimize View entry for this.


Dependencies:
-------------

You also need to clone https://git.eclipse.org/r/#/admin/projects/platform/eclipse.platform.swt.binaries git repository 
and ensure that the fragment matching your windowing_system.operating_system.cpu_architecture (e.g. org.eclipse.swt.gtk.linux.x86_64).
The fragments provide the platform specific implementations.


More Information:
-----------------

See the Readme.md in the main directory of the Git repository for this project to learn more about SWT development.
