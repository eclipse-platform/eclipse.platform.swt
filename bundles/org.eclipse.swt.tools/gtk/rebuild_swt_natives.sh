#!/bin/bash

# ABOUT THIS SCRIPT
# This script is designed to be used by a SWT *developer*,
# to simplify daily swt rebuilds for testing and debugging of SWT native bindings.
#  (It's not a build script to be used by a server in production).
#
# It uses SWT's build script ()*1) and adds extra functionality:
# - Builds SWT's JNI native code (libswt-*-xyzz.so) with debugging support enabled by default
#   * Also prints number of warnigns found. (Useful to see if new warnings were introduced)
# - Cleans up binary git repository holding '.so' filse
#   * This prevents the situation where when you checkout an older swt patch and
#     rebuild, swt would use some newer .so's instead of the newly compiled once.
# - Copies .classpath_gtk to .classpatch
#   * This is to deal with changes in .classpath_gtk
#
# [1] ~/git/eclipse.platform.swt/bundles/org.eclipse.swt/Eclipse SWT PI/gtk/library/build.sh


SCRIPT_VERSION=3

# [CONFIG] Find directory where this script is being executed from.
#############################################################
#    The method below works even if the script is called via symlink
#    http://stackoverflow.com/questions/59895/getting-the-source-directory-of-a-bash-script-from-within
SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
SCRIPT_DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
cd ${SCRIPT_DIR}
# [CONFIG END] ALL FUNCTIONAL CODE SHOULD BE BELOW THIS LINE. Otherwise it'll break if you run this script form a sym-link.


# Utility functions
####################
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

func_echo_info "Starting SWT rebuild script version: $SCRIPT_VERSION"

# 0) Find SWT's project directory
###############################
func_echo_info "\n[Step 1] Looking for SWT project directory"
# Try relative path.
SWT_PROJECT_DIR=$(readlink -e "../../org.eclipse.swt")  #get absolute path based on relative path.
if [ -e "${SWT_PROJECT_DIR}/.classpath_gtk" ] && cat "${SWT_PROJECT_DIR}/META-INF/MANIFEST.MF" | grep "Bundle-SymbolicName: org.eclipse.swt" ; then
	func_echo_info "[Step 1] ** Found SWT project directory: $SWT_PROJECT_DIR";
else
	# Try hard-coded path (in case script is moved)
	HARD_CODED_PATH="${HOME}/git/eclipse.platform.swt/bundles/org.eclipse.swt"
	if [ -e "${HARD_CODED_PATH}/.classpath_gtk" ] && cat "${HARD_CODED_PATH}/META-INF/MANIFEST.MF" | grep "Bundle-SymbolicName: org.eclipse.swt" ; then
		func_echo_info "[Step 1] ** SWT project directory found via hard coded path"
			SWT_PROJECT_DIR="$HARD_CODED_PATH";
	else
		func_echo_error "[Step 1] Could not find SWT project neither in: \n$SWT_PROJECT_DIR\nnor in:\n$HARD_CODED_PATH\nYou probably need to build swt project."
		exit 1;
	fi
fi


# 1) Fix '.classpath' for SWT project and for snippets.
#######################################################
# Sometimes .classpath_gtk is updated in commits, e.g when a class folder is added/removed.
# Because renaming .classpath is done manually, this often leads to errors about missing source folders.
# Fix: re-copy the .classpath on every lib rebuild.
func_echo_info "\n[Step 0] Copying .classpath_gtk files to .classpath in SWT project & Snippets"
cd $SWT_PROJECT_DIR
if [ -e .classpath_gtk ] && cat META-INF/MANIFEST.MF | grep "Bundle-SymbolicName: org.eclipse.swt" ; then
	(set -x ; cp .classpath_gtk .classpath)
else
	func_echo_error "[Step 0] I was expecting to be in:\n/git/eclipse.platform.swt/bundles/org.eclipse.swt\nBut I'm in $(pwd)"
	exit 1 # failed
fi

# Navigate to snippets.
cd "../../examples/org.eclipse.swt.snippets/"
if [ -e .classpath_gtk ] &&  cat META-INF/MANIFEST.MF | grep "Bundle-SymbolicName: org.eclipse.swt.snippets"; then
	(set -x ; cp .classpath_gtk .classpath)
else
	func_echo_error "[Step 0] I was expecting to be in snippet repository: /examples/org.eclipse.swt.snippets/ , but I'm in $(pwd)"
	error 1 #failed
fi



# 2) Clean up old '.so' files from binary repository
####################################################
cd "$SWT_PROJECT_DIR"
func_echo_info "\n[Step 2] Cleaning up of old '.so' lib files from binary git repository"

# Try to find output directory for 'so' files.
# 2.1 First try to git binary folder from build.sh
# 2.2 Failing that (e.g old SWT repo), try to guess.

# 2.1
if [ ! -e "${SWT_PROJECT_DIR}/bin/library/build.sh" ]; then
	func_echo_error "[Step 2] Could not find 'build.sh'. You probably need to build swt project. This script fixed the classpath, so try building SWT and running this script again."
	exit 1; # failed.
fi
cd "${SWT_PROJECT_DIR}/bin/library/"

# Check if build.sh is new enough to produce OUTPUT dir (older versions didn't)
if grep --quiet '\-\-print-outputdir-and-exit' build.sh ; then
	# We ask build.sh where OUTPUT is, because output is platform dependent.
	SO_OUTPUT_DIR="$(./build.sh --print-outputdir-and-exit | grep -i OUTPUT_DIR= | cut -f2 -d '=')"  # no trailing '/'
	if [ "$?" -ne 0 ]; then
		func_echo_error "[Step 2] build.sh failed to provide OUTPUT directory"
		exit 1; # Failure
	fi
else
	# 2.2 Try to guess the output directory.
	SO_OUTPUT_DIR="$HOME/git/eclipse.platform.swt/binaries/org.eclipse.swt.gtk.$(uname -s | tr '[:upper:]' '[:lower:]').$(uname -i)"
fi


# Sanity check: Make sure swt binary repo exists and that it contains related MANIFEST. I look for 'SWT-Arch'
# We don't want to run 'git clean -xdf' in some unexpected directory.
if [ -e "${SO_OUTPUT_DIR}/META-INF/MANIFEST.MF"  ]; then
	if [ "$(cat $SO_OUTPUT_DIR/META-INF/MANIFEST.MF | grep 'SWT\-Arch' | wc -l)" = 1 ]; then
		cd "$SO_OUTPUT_DIR"
		func_echo_info "[Step 2] Found binary folder: $(pwd)"
	else
		func_echo_error "[Step 2] Found binary folder: \n$SO_OUTPUT_DIR \n but META-INF/MANIFEST.MF doesn't look like it belongs to SWT"
		exit 1 # Failure
	fi
else
	func_echo_error "[Step 2] Could not find binary folder as indicated by build.sh:\n $SO_OUTPUT_DIR"
	exit 1 # Failure
fi

# 2.2) Clean up binary repo:
func_echo_info "[Step 2] ** [1/2] Removing newly added files. (This removes old 'so' files. Prevents newer files to be loaded when you need to debug older commits):"
(set -x; git clean -xdf)
func_echo_info "[Step 2] ** 1/2] Undoing changes to existing files. (This removes 'so' files that overrode the existing so files. [same version])"
(set -x; git reset --hard)


# 3) Rebuild swt bindings. make_linux.mak should copy them to binary folder.
#########################
func_echo_info "\n[Step 3] Rebuilding SWT bindings and copying them into binary folder"
cd "$SWT_PROJECT_DIR/bin/library/"

#3.1) Check that we're in the right place
if [ ! -e build.sh ]; then
	func_echo_error "[Step 3] Hmmm. I got lost somewhere. Was looking for build.sh file, which is normally in /eclipse.platform.swt/bundles/org.eclipse.swt/library/bin/. But currently I'm in $(pwd) , and I cannot find build.sh"
	exit 1 # Fail
fi

#3.2) Rebuilg gtk bindings with debug support enabled by default
export SWT_LIB_DEBUG=1
temp_log_file="$(mktemp)"  # Keep log so we can count warnings after.
# 'script' command logs commands and their output.
#  This is used instead of output redirection to preserve make colouring output,
#  while at the same time capture log for parsing.
#  (Dev note: old 'build.sh' did not have '-gtk-all' paramater and 'install' was broken. 
#   For really old SWT builds, may need to manually copy '.so' files and manually specify GTK3)
script --quiet --return --command " ./build.sh -gtk-all install" $temp_log_file   #"script" cmd preserves color coding during logging.
if [ "$?" -ne 0 ]; then # Failed
	func_echo_error "[Step 3] Building native glue code failed. Exiting"
	rm "$temp_log_file"
	exit 1 # Failed
else # Success
	WARNING_COUNT=$(cat $temp_log_file | grep warning | wc -l)
	func_echo_info "[Step 3] Bindings compiled sucessfully"
	func_echo_error "[Step 3] ** Warning count: $WARNING_COUNT "
	rm "$temp_log_file"
fi

func_echo_info "Finished"
