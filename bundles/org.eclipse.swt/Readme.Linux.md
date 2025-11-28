# About

The SWT Linux port is based on the GTK.

The natives are build using make and (by default) gcc.

## Building Natives

### Building 64bit natives for GTK

You need to install the following on your system:

* make
* gcc
* GTK3/GTK4 development files (see https://www.gtk.org/docs/installations/linux/)
* Java 21 JDK (https://adoptium.net/marketplace/?version=21)
* (optional) Webkit for GTK development files (webkit2gtk3-devel)

### Building and Testing locally

Import and build the SWT project `bundles/org.eclipse.swt` and `binaries/org.eclipse.swt.gtk.linux.${arch}` in Eclipse.

See **Building and Testing locally** in [Readme.md](Readme.md) for further instructions.

Additional information on GTK SWT development can be found in Eclipse Wiki:

https://wiki.eclipse.org/SWT/Devel/Gtk/Dev_guide
