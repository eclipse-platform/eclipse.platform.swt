#!/bin/bash
version=2

############## DOCUMENTATION
### ABOUT
# About this script:
# The purpose of this script is to run SWT JUnit tests with multiple versions of GTK. 2.24, 3.14, 3.16, etc..
# Then produce an accumilated output that shows which tests fail in which versions of GTK.
# This is particularly useful for patch submissions/reviews.
# Note, the compiled GTK SO files are an external resource. See below.

### Support and feature requests.
# Leo Ufimtsev: lufimtse@redhat.com 2016
# You can also create bug in bugzilla for Platform -> SWT with [autotester] in subject.


### SETUP
# (Pre-requisite):
#  0) This script assumes that you checked out SWT's sources repo into the default ~/git/ folder.
#  If this is not the case, adjust config below.
#
#  1) JUnit setup: 
#  JUnit and Hamcrest packages are needed for junit to work from the command line.
#  I.e, these have to be installed on your system (Eclipse uses it's own jar bundles)
#  Hamcrest usually comes with newer versions of JUnit as dependency.
#  ex on Fedora: sudo dnf install junit
#
#  2) GTK BUILDS:
#  The compiled gtk .so files are readily provided in git repo:
#  https://pagure.io/swt_dev_tools
#  You can simply clone the repo into:  ~/git/    and you're good to go. I.e:
#  mkdir ~/git/
#  cd ~/git
#  git clone https://pagure.io/swt_dev_tools.git
#
#
#  3) (Optional) Adding more GTK builds.
#  I (Leo) usually add new builds to the repo. You just need to pull from time to time.
#  However, if you want to do some yourself:
#  Checkout and compile some GTK build, business as usual. Then navigate into ~/git/gtk and search for all '.so' files via:
#  find . | grep "\.so"
#  You only need the '.so' files for the tests, so copy them across to the gtk builds:
#  cp $(find . | grep "\.so") ~/git/swt_dev_tools/gtk_builds/3.18.9/
#  (You need to create 3.xx.yy folder first)
#
#
### COMMAND LINE ARGUMENTS
# -n  :  test with _native gtk. I.e, use the gtk on your system. This is particularly useful if you run this with JHbuild.
#		 Note, -a and -t are not compatible with each other, but '-n' can be used alongside either one of them.

# -a  : test with _all gtk builds in swt_dev_tools/gtk_builds/.
# -t "3.20 3.18 3.16 3.08 2.24" : _test with certain versions of gtk. Please note:
#           * Version must match _exact_ folder names found in swt_dev_tools/gtk_builds/.
#           	i.e, '3.08' and not '3.8', and not '3.8.6'
# 			* Also note that the list must be inside quotes. I.e
#				-t "3.20 3.18"    >> will work, but 
#          	 -t 3.20 3.18      >> will only run the first one.
#
### Examples:
#		navigate to script directory & execute:
#		cd ~/git/eclipse.platform.swt/tests/org.eclipse.swt.tests
#		./gtk_version_tests.sh -t "3.20 3.12"   #run certain gtk versions
#       ./gtk_version_tests.sh -nt "3.20 3.12"  #run native gtk and certain gtk versions
#		./gtk_version_tests.sh -a              #run all gtk versions in gtk_builds
#       ./gtk_version_tests.sh -na             #run natives and all gtk versions in gtk_builds
#
### Return values
# 0 if all tests pass on all GTK versions 
# 1 on some error
# 2 if no arguments were given.
#
# More documentation:
# see: https://wiki.eclipse.org/SWT/Devel/Gtk/JUnitTests




############## CONFIGURATION
# SWT source and binary git repo locations:
# 	script assumes that swt source (git) and swt_dev repos are in the default: $HOME/git/... but you can change it:
REPO=$HOME/git

# Location of your gtk_builds. Only change this if you use your own build folder. The build folder should contain just the 'so' files.
GTK_BUILD_DIR=$HOME/git/swt_dev_tools/gtk_builds

LOG_FILE=gtk_test_out.log
LOG_ERROR_FILE=gtk_test_error.log
#### CONFIG END


###################################### CONFIG AND DOCUMENTATION END. Below be dragons

# Script intro:
echo "#################################################"
echo "#  Running SWT jUnit tests. This may take a while."
echo "#  Warnings redirected to gtk_test_error.log."
echo "#  Copy of output in gtk_test_out.log"
echo "#  Script version: $version"
echo "#################################################"

# Clean up from Previous runs:
rm -f $LOG_ERROR_FILE
rm -f $LOG_FILE  # Duplicate output into this file for parsing later.

################# FUNCTIONS
# Helper functions
func_run_junit () {
  # $1 = SWT tests to run. Ex AllNonBrowserTests

  java -Djava.library.path=$REPO/eclipse.platform.swt.binaries/bundles/org.eclipse.swt.gtk.linux.x86_64 \
  -Dfile.encoding=UTF-8 \
  -classpath /usr/share/java/junit.jar:/usr/share/java/hamcrest/core.jar:$REPO/eclipse.platform.swt/bundles/org.eclipse.swt/bin:$REPO/eclipse.platform.swt/tests/org.eclipse.swt.tests/bin org.eclipse.swt.tests.junit.CmdLineInterface $1 2>> $LOG_ERROR_FILE | tee -a $LOG_FILE
}


func_run_tests () {
	# -m Message to be logged
	# -p gtk version in the repo. ex 3.12. This represents the folder the 'so' files are in.
	# -t tests to be passed to java command line handler. (additional tests need to be added to java file manually).
	# NOTE: '-t' need to be passed in last.
	local OPTIND
	while getopts "m:p:t:" opt; do
		case $opt in
			m)	#message to be logged.
				(>&2 echo "$OPTARG") 2>> $LOG_ERROR_FILE
			  echo "$OPTARG" | tee -a $LOG_FILE
				;;
			p) # set LD_LIBRARY_PATH
				 export LD_LIBRARY_PATH=$GTK_BUILD_DIR/$OPTARG
				;;
			t)
				func_run_junit "$OPTARG"
				;;
		esac
	done
}


# Handle command line arguments if given.
func_handle_arguments () {
	while getopts "t:na" opt; do
	case $opt in
		t)
			GTK_VERSIONS="$OPTARG"
			;;
		a)
			GTK_VERSIONS=$(ls $GTK_BUILD_DIR | sort -r)  # default=all meaning all folders in build folder. Can be overriden via given args.
			;;
		n)
			TESTNATIVES=true
			;;
		\?)
			echo "Invalid option: -$OPTARG"
			;;
	esac
done
}




################# LOGIC

# Initialization
TESTS=AllNonBrowserTests
TESTNATIVES=false #overriden via '-n' paramater
GTK_VERSIONS=

# Handle input arguments
if [ $# -ne 0 ]; then # more than 1 arg
	func_handle_arguments "$@"
else
	echo "Error. No command line arguments were given."
	echo "Please see content of script for argument list. (ex -a for all tests etc...)"
	exit 2
fi

# if '-n' was specified, test with native platform.
if [ "$TESTNATIVES" = true ]; then
	func_run_tests -m "------ Testing with native GTK libraries on your system" -p "" -t "$TESTS"
fi


# run tests for gtk versions:
for i in $GTK_VERSIONS; do
	#local PREFIX
	PREFIX=${i:0:2}  #ex 3. 2.
	# case gtk 3
	if [ $PREFIX == "3." ]; then
		export SWT_GTK3=1
	elif [ "$PREFIX" == "2." ]; then
		export SWT_GTK3=0
	else
		echo " Skipping over folder: $i"
		continue
	fi
	MSG="------ Testing with $i ---------------"
	func_run_tests -m "$MSG" -p "$i" -t "$TESTS"
done


# Post run analysis.
echo ""
echo " ~~SUMMARY OF SUMMARIES:"
cat $LOG_FILE | grep "GTK_TEST_RESULT"
echo ""
echo "To see warnings that were thrown during tests, see: $LOG_ERROR_FILE"


# Exit
if [ $(cat gtk_test_out.log | grep "SOME TESTS FAIL" | wc -l) == "0" ]; then
	echo "EXIT  0: All went well"
	exit 0
else
	echo "EXIT  1: errors occured"
	exit 1
fi