org.eclipse.swt
===============

Main plug-in for the SWT user interface library.

Dependencies:
-------------

* **SWT Binaries**  

  Ensure that the fragment matching your windowingSystem.operatingSystem.cpuArchitecture  
  (e.g. org.eclipse.swt.gtk.linux.x86_64) is open in your workspace.  
  The fragments provide the platform-specific native libraries and compile the java source code.

Using Assertions:
----------------
Assertions are added to the code. These don't run in production, but they do when:
* JUnits are ran, they turn on assertions by default.
* If you run a java run configuration and add '-ea' to the 'VM Arguments'

Assertions look like:

	assert expression ;
	assert expression : msg ;

See: https://docs.oracle.com/javase/8/docs/technotes/guides/language/assert.html

More Information:
-----------------

See the [Readme.md](../../Readme.md) in the main directory of the Git repository for this project to learn more about SWT development.
