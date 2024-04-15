org.eclipse.swt
===============

Main plug-in for the SWT user interface library.

Development overview:
-------------

SWT is made up of 2 parts, Java and the natives (written in C).

* **Java Part**:

If you only make changes in the Java part or need to configure SWT for running
snippets then you don't need to recompile the natives and you can just
use the pre-compiled libraries in the binary repository instead.

* **Natives Part**

Make sure the binary project for your platform is imported and open in your workspace,
_for example_
  * `binaries/org.eclipse.swt.win32.win32.x86_64` for Windows or
  * `binaries/org.eclipse.swt.gtk.linux.x86_64` for Linux or
  * `binaries/org.eclipse.swt.cocoa.macosx.x86_64` for Mac

These examples refer to the architecture `x86_64` but there are other projects for
other architectures, like `aarch64` (or `loongarch64` / `ppc64le` for Linux)

Dependencies:
-------------

* **SWT Binaries**  

  Ensure that the fragment matching your _windowingSystem.operatingSystem.cpuArchitecture_
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

Running the snippets:
---------------------

* Download & install Java *JDK* (Last tested on JDK17)
* Download and install a pre-configured Eclipse SDK using Oomph as described in [Contributing.md](https://github.com/eclipse-platform/.github/blob/main/CONTRIBUTING.md#creating-an-eclipse-development-environment).
* (Optionally) You can uncheck everything that has nothing to do with SWT from the **Projects** selection page
* (Optionally) install CDT from marketplace if you want to work on C/Native parts of SWT.
* You should be able to run snippets now. (_e.g._ `Snippet1`).

Building and Testing locally:
-----------------------------

From Eclipse:

* If necessary, install the **M2E - Maven integration for Eclipse** (see [m2e](https://eclipse.dev/m2e/))
* Open the **Launch Configurations** dialog.
* Run the **Maven** configuration called: `Build-SWT-native-binaries-for-running-platform`.
* If compile fails, inspect build log for warnings, they can point out issues in your configuration.

The Maven configuration will perform a `clean` and then call [`build-native-libraries`](https://github.com/eclipse-platform/eclipse.platform.swt/blob/979d3f1580b4786aee1efe046da8a6f836300804/binaries/pom.xml#L92-L134),
which sets the required environment variables and then runs the appropriate
build script for the current platform (`build.sh`/`build.bat`). The script is located in the
source folder `Eclipse SWT PI\<current-ws>\library` in the appropriate fragment.

To test if you binaries actually get used, you could try to revert back to some older version and
run a snippet, you should get the 'missing natives' error. Then if you build the natives
and the snippet(s) work, it means that you've build them successfully.

More Information:
-----------------

See the [Readme.md](../../Readme.md) in the main directory of the Git repository for this project to learn more about SWT development.
