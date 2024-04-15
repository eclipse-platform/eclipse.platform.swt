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
