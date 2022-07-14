# About

The document is structured so that you only need to read up to the point that you require, with advanced topics at the bottom.

The SWT windows port is based on the Win32 api.

The natives are compiled via an ant build.xml script with the Microsoft Visual Studio (2019) C++ compiler.

# Development overview
SWT is made up of 2 parts, Java and the natives (written in C).

* **Java Part**  
If you only make changes in the Java part or need to configure SWT for running snippets,
then you don't need to recompile the natives, instead, you can just use the pre-compiled
libraries in the binary repository.

* **Natives Part**  
(Make sure the binary project for your platform is imported and open in your workspace).

## Configure workspace to run snippets.

* Download & install Java *JDK* (Last tested on JDK9) http://www.oracle.com/technetwork/java/javase/downloads/index.html
* Download and install Eclipse. Either the "Eclipse IDE for Eclipse Committers" or a recent integration build:
  https://download.eclipse.org/eclipse/downloads/
* (Optionally) install CDT from marketplace if you want to work on C/Native parts of SWT.
* (if not already installed) installed EGit integration.
* Open the git perspective. Add the following two repositories, which can be found on: (use git protocol)
	- https://github.com/eclipse-platform/eclipse.platform.swt
	- https://github.com/eclipse-platform/eclipse.platform.swt.binaries
* From swt repo, import most projects (except Cocoa/Gtk, root 'swt' project, once)
* In org.eclipse.swt project, you need to copy .classpath_win32 to .classpath  & refresh/rebuild workspace.
  (This can be done via command line or by not-filtering *.resources in package view).
* From swt.binary repo, import the project that reflects your platform.
* In the snippet project, add the swt project as dependency to launch snippets.
* You should be able to run snippets now. (e.g Snippet1).

## (Advanced) Building Natives.

### Building just 64bit natives with no Webkit
(Last tested on Win10 64 bit & Java 11. May 2019):

You need to install the following on your system:  

* Microsoft Visual Studio - Community and Windows 10 SDK:  (1*)
  https://visualstudio.microsoft.com/vs/community/
  Either select the components "MSVC C++-x64/x86-Buildtools" and "Windows 10 SDK"
  or the workload "Desktop development with C++" which includes the required components and some more.
* (Optional) Install Cygwin
* Install Java 11. Oracle JDK or IBM JDK:  
  http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html  
  https://developer.ibm.com/javasdk/downloads/  

### Building and Testing locally

In the binary git repo, in the windows project, you can build the natives via the build.xml. To do so:  

* In Eclipse, in the binary project org.eclipse.swt.win32.win32.x86_64, right click on build.xml:  
* Run As -> Ant Build ...
* On the Targets tab check the build_libraries target  (it should be the only one check for just the natives).
* On the JRE tab select "Run in the same JRE as the workspace"
* On the Refresh tab check "Refresh resources upon completion" to refresh your workspace after running the build; this ensures that Eclipse will pick up the fresh binaries
* Press the "Run" button to begin.
* If compile fails, inspect build log for warnings, they can point out issues in your configuration.

To test if you binaries actually get used, you could try to checkout an older binary repo commit with old
bindings. Then try to run a snippet and get the 'missing natives' error. Then if you build the natives
and the snippet(s) work, it shows that you've build them successfully.

### Optional additional configuration
* Install and configure Cygwin SSH Server on Windows. (Make sure 'openssh' package is installed).
  See: https://docs.oracle.com/cd/E24628_01/install.121/e22624/preinstall_req_cygwin_ssh.htm#EMBSC281
* Configure the machine for password-less SSH authentication with the Hudson machine.
  For more details you can refer: http://users.cecs.anu.edu.au/~xzhang/pubDoc/IT/SSH%20without%20password%20from%20Windows.htm  
  Sharing some key steps below(which I recall):
  - Generate the 'dsa' public/private key from your "swtbuild" account from windows machine.
  - Now login to the Hudson machine with "swtbuild" account.
  - Copy the public keys and register then on the Hudson machine.. this should enable password-less authentication.
  
## (More advanced) (Optional) Building Webkit
Note, I.E is the default backend. But Webkit is also a possible alternative Browser backend.

Disclaimer:

Note Webkit SDKs are an older version being consumed by SWT native build process and Webkit build binaries are no more available for download in public Webkit download page (https://webkit.org/downloads/)

But WebKit-r72896 sources are still avilable for download via either of:
https://builds-nightly.webkit.org/files/trunk/src/WebKit-r72896.tar.bz2  
http://build.eclipse.org/eclipse/swt/WebKit-r72896.tar.bz2  

Webkit SDKs:  

* WebKit-r72896 http://build.eclipse.org/eclipse/swt/WebKit-r72896.zip
* WebKitSupportLibrary http://build.eclipse.org/eclipse/swt/WebKitSupportLibrary.zip


Steps:  

* Create an SWT Build dir such as `C:\SWT_BUILD_DIR`
* In your enviornment:  
    `set SWT_BUILD_DIR=YOUR.DIR`
* Unzip two Webkit SDks into:  
  - `SWT_BUILD_DIR\Webkit\r72896`  
  - `SWT_BLUID_DIR\Webkit\SupportLibrary`  
  - (Note: SWT's Webkit support exists for SWT 32bit on Windows, so Webkit SDKs are consumed only by the SWT 32bit build process)

## Custom Java paths and 32 bit builds

You can specify a specific java sdk via:  
  `set SWT_JAVA_HOME=<YOUR_PATH>`

To do a 32 bit build, install a 32bit java & specify it's path or put it into:
`SWT_BUILD_DIR\Java\Oracle\jdk1.8.0-latest\x86`

For building windows native build for 32bit, you need to convert the source to 32bit first, by running 'replace.64.to.32' target.  
Once sources are converted to 32bit you can run the 'build_libraries' target for ${workspace_loc:/org.eclipse.swt.win32.win32.x86/build.xml}

## Hudson
(Optional for local setup)  
Now you can point the Windows hudson job to this machine and trigger a native build.  
Note: For testing purpose from hudson, temporarily turn the nativeChanges flag to 'true' to force a native build compilation.

# Footnotes
[1]  https://bugs.eclipse.org/bugs/show_bug.cgi?id=526802



