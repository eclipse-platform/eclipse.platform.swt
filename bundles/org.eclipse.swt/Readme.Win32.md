# About

The document is structured so that you only need to read up to the point that you require, with advanced topics at the bottom.

The SWT windows port is based on the Win32 API.

The natives are compiled using the Microsoft Visual Studio (2019) C++ compiler.

## Building Natives.

### Building 64bit natives
(Last tested on Win10 64 bit & Java 17. April 2024):

You need to install the following on your system:  

* Microsoft Visual Studio - Community and Windows 10 SDK:  (1*)
  https://visualstudio.microsoft.com/vs/community/
  Either select the components "MSVC C++-x64/x86-Buildtools" and "Windows 10 SDK"
  or the workload "Desktop development with C++" which includes the required components and some more.
* Java 17 JDK (https://adoptium.net/marketplace/?version=17)

### Building and Testing locally

Import and build the SWT project `bundles/org.eclipse.swt` and `binaries/org.eclipse.swt.win32.win32.${arch}` in Eclipse.

See **Building and Testing locally** in [Readme.md](Readme.md) for further instructions.
