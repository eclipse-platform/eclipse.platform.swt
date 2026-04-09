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
other architectures, like `aarch64` (or `loongarch64` / `ppc64le` for Linux).

For details about the operating system specific SWT implementations and their (build) requirements, see
- [Linux](Readme.Linux.md)
- [macOS](Readme.macOS.md)
- [Windows](Readme.Win32.md)

Dependencies:
-------------

* **SWT native binaries**

  Ensure that the fragment matching your <windowingSystem>.<operatingSystem>.<cpuArchitecture>
  (e.g. `org.eclipse.swt.gtk.linux.x86_64`) is open in your workspace.  
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

## Building native binaries

To build only SWT's native binaries, run from the CLI (at the repository root):
```bash
cd "binaries/org.eclipse.swt.<ws>.<os>.<arch>"
mvn clean antrun:run@build-native-binaries "-Dnative=<ws>.<os>.<arch>"
```
With the placeholder
- `<os>` replaced by one of the supported values `macosx`, `linux` or `win32`,
- `<ws>` replaced by `cocoa` (for Mac), `gtk` (for Linux) or `win32` (for Windows),
- `<arch>` replaced by `x86_64`, `aarch64`, `ppc64le` or `riscv64`,

according to the targeted platform.

For example to build the native binaries for Linux (GTK) running on x86 64bit CPUs, execute
```bash
cd "binaries/org.eclipse.swt.gtk.linux.x86_64"
mvn clean antrun:run@build-native-binaries "-Dnative=gtk.linux.x86_64"
```

For Linux, to build only the GTK3 binaries, set the environment variable `GTK_VERSION` to value `3.0`,
to build only GTK4 binaries, set `GTK_VERSION` to `4.0`.

The Maven build will perform a `clean` and then calls [`build-native-libraries`](https://github.com/eclipse-platform/eclipse.platform.swt/blob/a589ee9fedcfdab60f90902da87f5058faf94be9/binaries/pom.xml#L133-L173),
which sets the required environment variables and runs the appropriated build script for the current platform (`build.sh`/`build.bat`).
The script is located in the source folder `Eclipse SWT PI\<current-ws>\library` of the corresponding SWT native fragment.

To build the native binaries as part of a complete build of all SWT artifacts, run:
```bash
mvn clean verify "-Dnative=<ws>.<os>.<arch>"
```
using the placeholder from above.

#### Building native binaries from within the Eclipse IDE

* Ensure the **M2E - Maven integration for Eclipse** is installed (see [m2e](https://eclipse.dev/m2e/))
* Open the **Launch Configurations** dialog.
* Run the **Maven** configuration called: `Build-SWT-native-binaries-for-running-platform`.
  * This will build the native binaries for the current target platform.
* If compile fails, inspect build log for warnings, they can point out issues in your configuration.

Testing the native binaries
----------------------------

Import and build the SWT project `bundles/org.eclipse.swt` and `binaries/org.eclipse.swt.<ws>.<os>.<arch>` in Eclipse.

To test if you binaries actually get used, you could try to revert back to some older version and
run a snippet, you should get the 'missing natives' error. Then if you build the natives
and the snippet(s) work, it means that you've build them successfully.

More Information:
-----------------

See the [README.md](../../README.md) in the main directory of the Git repository for this project to learn more about SWT development.
