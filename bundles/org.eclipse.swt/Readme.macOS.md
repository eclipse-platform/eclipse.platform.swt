macOS Development for org.eclipse.swt
=====================================

Please follow the following pattern for macOS development:

* Copy `.classpath_cocoa` to `.classpath`
* Update `.bridgesupport` files when necessary, i.e. consuming new api in newer macOS versions
* Generate native and Java code using `MacGenerator`



Updating .bridgesupport files
-----------------------------

The `.bridgesupport` files are located in `Eclipse SWT PI/cocoa/org/eclipse/swt/internal/cocoa`. They are
generated using the tool `gen_bridge_metadata` which is included with macOS (Xcode).

Please note that we use an undocumented option with the `gen_bridge_metadata` to generate a complete XML including the `declared_type`.
It's not clear why that option is undocumented.

FWIW, there seems to be an issue with the tool that is included in macOS. After lots of reading and asking questions, it seems that
the best approach is to build a current version yourself and launch it from there
(see tip [here](https://twitter.com/watson1978/status/786929396711948288)).

    git clone https://github.com/HipByte/BridgeSupport.git
    cd BridgeSupport
    make

    # once done let's define a function to make generation more convenient
    #
    # gen_bridge_metadata_complete() {
    #    RUBYLIB=./DSTROOT/System/Library/BridgeSupport/ruby-2.0                 \
    #    ./gen_bridge_metadata.rb                                                \
    #        --format complete                                                   \
    #        --cflags "-I.                                                       \
    #                  -isysroot /                                               \
    #                  -mmacosx-version-min=10.12                                \
    #                  -DTARGET_OS_MAC=1                                         \
    #                  -D__ENVIRONMENT_MAC_OS_X_VERSION_MIN_REQUIRED__=101200    \
    #                  -F/System/Library/Frameworks                              \
    #                  -framework $1 "                                           \
    #        --framework /System/Library/Frameworks/$1.framework
    #  }
    #
    # (now all in one line)
    gen_bridge_metadata_complete() { RUBYLIB=$(pwd)/DSTROOT/System/Library/BridgeSupport/ruby-2.0 $(pwd)/gen_bridge_metadata.rb --format complete --cflags "-I. -isysroot / -mmacosx-version-min=10.12 -DTARGET_OS_MAC=1 -D__ENVIRONMENT_MAC_OS_X_VERSION_MIN_REQUIRED__=101200 -F/System/Library/Frameworks -framework $1" --framework /System/Library/Frameworks/$1.framework; }

Once done with that, go back into the `org.eclipse.swt` bundle directory and execute the following commands to generate all the `.bridgesupport` files.

     # should be executed from the BridgeSupport directory
     # (where the gen_bridge_metadata is located)
     #
     gen_bridge_metadata_complete AppKit            > '<org.eclipse.swt>/Eclipse SWT PI/cocoa/org/eclipse/swt/internal/cocoa/AppKitFull.bridgesupport'
     gen_bridge_metadata_complete CoreFoundation    > '<org.eclipse.swt>/Eclipse SWT PI/cocoa/org/eclipse/swt/internal/cocoa/CoreFoundationFull.bridgesupport'
     gen_bridge_metadata_complete CoreText          > '<org.eclipse.swt>/Eclipse SWT PI/cocoa/org/eclipse/swt/internal/cocoa/CoreTextFull.bridgesupport'
     gen_bridge_metadata_complete Foundation        > '<org.eclipse.swt>/Eclipse SWT PI/cocoa/org/eclipse/swt/internal/cocoa/FoundationFull.bridgesupport'
     gen_bridge_metadata_complete SecurityInterface > '<org.eclipse.swt>/Eclipse SWT PI/cocoa/org/eclipse/swt/internal/cocoa/SecurityInterfaceFull.bridgesupport'
     gen_bridge_metadata_complete WebKit.framework  > '<org.eclipse.swt>/Eclipse SWT PI/cocoa/org/eclipse/swt/internal/cocoa/WebKitFull.bridgesupport'

The `.extras` files are created by the `MacGenerator` later. They store additional information (eg., how types are mapped to SWT).
We don't need to worry about them now.


Generate code using MacGenerator
--------------------------------

There are two possible option:

* Install the `org.eclipse.swt.tools` plug-in into Eclipse and use the view.
* Import the `org.eclipse.swt.tools` project into a second workspace and run `MacGeneratorUI` as vanilla Java application.

Running `MacGeneratorUI` from within a second workspace has the benefit that you can tweak the `MacGeneratorUI` tool should you run into any issues.
It should consume `org.eclipse.swt` from a target platform (not in source form). The project `org.eclipse.swt.tools.base` and `org.eclipse.swt.tools.spies`
should also be in that second workspace.

Please note that `MacGeneratorUI` makes certain assumptions (hard-coded) about the environment and paths. That's usually fine
because all is within the same Git repository. It should only break when things are restructured without adjusting it. However, that's also
the reason why you should run it from within a second workspace with SWT consumed from a target platform. It modifies SWT *in* the same
Git repo. If SWT is also in the same workspace, you will run into issues once some generation breaks SWT.

More information on how to use the generator is available [here](https://www.eclipse.org/swt/macgen.php). Volunteers wanted to convert
this information into this readme.


Compiling native libs
---------------------

This is done from within the binaries project.

    cd <eclipse.platform.swt.binaries>/bundles/org.eclipse.swt.cocoa.macosx.x86_64
    mvn clean package -Dnative=cocoa.macosx.x86_64 -Pbuild-individual-bundles


Happy development!