#!/bin/bash

# ABOUT THIS SCRIPT
# This script automatically clones, builds (rebuilgs) gtk '.so' libs, so that you can
# run snippets with compiled gtk sources and allows you to debug gtk part of swt snippets.
#
# This script does the following:
# *) If ~/git/gtk does not exist, it asks you if it should create it for you.
# *) Clones git sources if not already cloned.
# *) Configures gtk for debug and compiles gtk
# *) Creates ~/gnomeso/curr if it doesn't exist. (This is where .so files will be)
# *) Copies scattered '.so' files into folder above for use by eclipse snippets.
# *) Prints next steps to console. (See bottom of script).
#
# Author Leonidas@RedHat.com 2017-03Mar-30Thu

# CONFIG
GIT_DIR=$HOME/git
GTK_SRC_DIR=$GIT_DIR/gtk
COMPILED_SO_DIR=$HOME/gnomeso/curr
# CONFIG END

HELP_MSG="
# Optional command line arguments:\n
 -y  - silently build gtk without asking about branch\n
 --nocleanup - do not cleanup git repository. Useful when modifying gtk itself, e.g add printf's.\n
 --noconfig - do not re-configure the project. Used with the above to make compile faster or custom configs.\n
e.g\n
 clone_build_gtk_debug.sh   # Clones and builds gtk for you.\n
 clone_build_gtk_debug.sh -y --nocleanup --noconfig  # For quick rebuilds when modifying gtk sources.\n
------------------\n\n
"

func_echo_info () {
	GREEN='\033[0;32m'
	NC='\033[0m' # No Color
	echo -e "${GREEN}${@}${NC}"
}

func_echo_input () {
	PURPLE='\033[0;35m'
	NC='\033[0m' # No Color
	echo -e "${PURPLE}${@}${NC}"
}

func_echo_error () {
	RED='\033[0;31m'
	NC='\033[0m' # No Color
echo -e "${RED}*** ${@}${NC}"
}

# Print help info to educate user
func_echo_info $HELP_MSG

# Parse arguments
SILENT=false
NOCLEANUP=false   # for when you make changes to gtk source code, e.g print statments etc..
NOCONFIG=false    # faster build speeds.
for arg in "$@"; do  # loop over all input parameters
	if [ "$arg" == "-y" ]; then
		func_echo_info "Assuming 'y' for trivial operations"
		SILENT=true
	fi
	if [ "$arg" == "--nocleanup" ]; then
		func_echo_info "Will not clean up gtk sources"
		NOCLEANUP=true
	fi
	if [ "$arg" == "--noconfig" ]; then
		func_echo_info "Will not reconfigure"
		NOCONFIG=true
	fi
	if [ "$arg" == "--help" ]; then
		exit 0 # If help was asked, only help will be given.
	fi
done

# Check if git source directory exists. Offer to create it if it doesn't.
if [ ! -d "$GIT_DIR" ]; then
	func_echo_input "$GIT_DIR git directory for sources does not exist. Create it?"
	read -p "[y/n]: "
	if [ "$REPLY" == y ]; then
		mkdir "$GIT_DIR"
	else
		func_echo_error "Cannot continue without git folder for sources"
		exit 1 # fail
	fi
fi

# Check if gtk source repository is present. If not, offer to clone it.
if [ ! -d "$GTK_SRC_DIR/.git" ]; then
	func_echo_input "$HOME/git/gtk does not exist. Should I clone gtk git repository?"
	read -p "[y/n]: "
	if [ "$REPLY" == y ]; then
		cd "$GIT_DIR"
		git clone https://github.com/GNOME/gtk.git
	else
		func_echo_error "Cannot continue without gtk src. If you have gtk source in another place, configure this script"
		exit 1 #fail
	fi
fi

# Suggest to the user to checkout a suitable branch.
cd "$GTK_SRC_DIR"
func_echo_info "You are in: $(pwd)"
if [ "$SILENT" == "false" ]; then
	func_echo_input "Currently gtk is on branch:"
	git branch
	func_echo_input "Its reccomended to checkout a version of GTK that
is found on your system, because 'master' usually requires very new upstream libraries.
To find out which version of Gtk3 is on your system, you can check your package manager. Ex on fedora:
sudo dnf list installed | grep '^gtk3\.'
To checkout a particular gtk branch:
  cd $GTK_SRC_DIR
  git branch -r  | grep gtk-3
  git checkout origin/gtk-3-22
Shall I continue (y)? (You can checkout the required branch in another terminal tab.
(To avoid seesing this message, try running this script with '-y'"

	read -p "[y/n]: "
	if [ ! "$REPLY" == y ]; then
		func_echo_error "Please run this script again after checking out a version"
		exit 1 # failure
	fi
fi

# Clean up old gtk bits.
cd "$GTK_SRC_DIR"
if [ "$NOCLEANUP" == false ]; then
	func_echo_info "Cleanup old gtk build files.
	This is needed if switching between gtk version to avoid compiliation issues."
	git clean -xdf   # remove build files.
	git reset --hard # undo changes to existing source files.
else
	func_echo_info "No cleanup"
fi

# Configure gtk sources for build. Assume debug support by default. (No use for production gtk?)
func_echo_info "Configuring gtk with debug support"
GEN_COMPILE_ERROR_MSG="Autogen failed. Check that you have required packages. Try ./install_sysdeps.sh or checkout earlier gtk branch"
if [ "$NOCONFIG" == false ]; then
	./autogen.sh --enable-debug=yes # for more options, see: https://developer.gnome.org/gtk3/stable/gtk-building.html
	if [ "$?" ne 0 ]; then
		func_echo_error $GEN_COMPILE_ERROR_MSG
		exit 1 #failed
	fi
fi

# Build gtk with some extra debug flags (enable macro expansion, turn off optimization..)
# and enable multi-threading for faster build.
func_echo_info "GTK build starting..."
make CFLAGS="-g3 -ggdb3 -O0" -j8
if [ "$?" -ne 0 ]; then
	func_echo_error $GEN_COMPILE_ERROR_MSG
	exit 1 $failed
fi


# We need to move the scattered compiled 'so' files into one place.
# Check if SO dir exists:
if [ ! -d "$COMPILED_SO_DIR" ]; then
	func_echo_info "$COMPILED_SO_DIR does not exist. Creating it. It will contain compiled .so libs"
	mkdir -p "$COMPILED_SO_DIR"
	# add a readme.
	echo "This folder contains '.so' dynamic library files build by swt/gtk build scripts, intended to be
used for running eclipse snippets with env vars: 'LD_LIBRARY_PATH=$COMPILED_SO_DIR'" > $COMPILED_SO_DIR/readme.md
fi

# copy compiled .so files into $COMPILED_SO_DIR
func_echo_info "Copying compiled '.so' files into $COMPILED_SO_DIR"

cp -v $(find . | grep "\.so") $COMPILED_SO_DIR

# Print next step instructions.
func_echo_info "
-----------------
SUCCESS

Gtk3 libs have been created. For a snippet/child eclipse to use the .so files, set environmental variable:
LD_LIBRARY_PATH=$COMPILED_SO_DIR

To tell if the running snippet uses the loaded libs, you can inspect which libraries are mapped:\n
/proc/PID/maps | grep gnomeso
see: http://stackoverflow.com/questions/2184775/getting-a-list-of-used-libraries-by-a-running-process-unix
You can use 'jps' command to find PID of a SWT snippet. I combine things into a command like:
cat /proc/$\(jps | grep -i NAME_OF_YOUR_SNIPPT | cut -f1 -d' ')/maps | grep gnomeso
Output should list 'libgdk-3.so.0' and 'libgtk-3.so.0'.

Also the gtk version in gtk inspector (ctrl+shift+i) will probably be a bit different (3.22.10 vs 3.22.11).
Note, GTKInspector has to be enabled first thou.
see: https://wiki.gnome.org/Projects/GTK+/Inspector

To debug gtk from a java snippet:
0) You should setup Eclipse CDT with Standalone debugger.
1) In Eclipse, create a C makefile/library project in some folder. (not in $GTK_SRC_DIR)
   In project settings, C/C++ General -> Paths and Symdols -> Source location,
   link folder to $GTK_SRC_DIR. Allow C++ indexer to run.
   (The reason for not creating project in $GTK_SRC_DIR is that this script cleans up
   the folder and removes the .project with 	it.)
2) Set a breakpoint somewhere in gtk. I recommend:
	gtkmain.c:gtk_main_do_event(..)
   because that get's ran when you do the main event loop.
3) Set a java breakpoint at the first line of 'main' in your java app.
4) Start snippet in debug mode (with LD_LIBRARY_PATH set), wait for it to break at java break point.
5) Get pid of your new child snippet via 'jps' terminal command
6) In Eclipse's 'Quick access' (ctrl+3), execute 'Debug Attached Executable'.
   (If this option is missing, you probably didn't install 'CDT Standalone Debugger')
7) Search for 'java', and connect to the java process with the given pid.
8) In the debug view, hit continue on java and the C stacktraces till your code reaches gtk_main_do_event().

In case things don't work, try 'gdb -p PID', it often tells you that you need to install some debug packages.

Tip:
- You can move ~/gnomeso/curr to something like ~/gnomeso/GTK-3-xx to keep different versions of gtk."
