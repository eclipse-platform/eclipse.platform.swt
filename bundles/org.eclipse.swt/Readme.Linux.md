# About

The SWT Linux port is based on the GTK.

The natives are build using make and (by default) gcc.

# Development overview
SWT is made up of 2 parts, Java and the natives (written in C).

* **Java Part**
If you only make changes in the Java part or need to configure SWT for running
snippets, then you don't need to recompile the natives, instead, you can just
use the pre-compiled libraries in the binary repository.

* **Project Panama**

In some areas SWT makes use of [Project Panama](https://openjdk.org/projects/panama/),
to (re)generate the binding you need to download a suitable `jextract` (currently https://jdk.java.net/panama/17/)

* **Natives Part**
Make sure the binary project for your platform is imported and open in your
workspace.

## Generating Panama Bindings

### OpenGL Bindings

1. Install the opengl and freeglut headers, e.g. `sudo apt-get install libglu1-mesa-dev freeglut3-dev mesa-common-dev`
2. go into folder `<your swt checkout>/org.eclipse.swt/Eclipse SWT OpenGL/gtk`
3. run `jextract --source -l glut -l GLU -l GL -l GLX -I /usr/include/GL -t org.eclipse.swt.opengl.panama /usr/include/GL/glut.h`
4. Open `org/eclipse/swt/opengl/panama/glut_h.java` and delete the package protected default contructor

## Building Natives

### Building 64bit natives for GTK3

You need to install the following on your system:

* make
* gcc
* GTK+ development files (gtk3-devel)
* Java 8 or 11 JDK
* (optional) Webkit for GTK development files (webkit2gtk3-devel)

### Building and Testing locally

Import and build the SWT project `bundles/org.eclipse.swt` in Eclipse. See the
platform independent Readme for how to set the .classpath. This will populate
the `bundles/org.eclipse.swt/bin/library` with the native code and build files.

To build SWT natives run the `build.sh` script in `./bin/library` directory. To
use the locally build natives run the build script with the 'install' argument
(`./build.sh install`) or --help to see more options.

You can find additional information on GTK SWT development can be found in
Eclipse Wiki.

https://wiki.eclipse.org/SWT/Devel/Gtk/Dev_guide
