# About this script:
# The purpose of this script is to run SWT JUnit tests with multiple versions of GTK. 2.24, 3.14, 3.16, etc..
# Then produce an accumilated output that shows which tests fail in which versions of GTK.
# This is particularly useful for patch submissions/reviews.
# Note, the compiled GTK SO files are an external resource. See below.

# Pre-requisite:
#  0) This script assumes that you checked out SWT's repo into the default ~/git/ folder.
#  If this is not the case, adjust config below.
#
#  1) JUnit and Hamcrest. Hamcrest usually comes with newer versions of JUnit as dependency.
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
#  3) (Optional) Adding more GTK builds.
#  Checkout and compile some GTK, business as usual. Then navigate into ~/git/gtk and search for all '.so' files via:
#  find . | grep "\.so"
#  You only need the '.so' files for the tests, so copy them across to the gtk builds:
#  cp $(find . | grep "\.so") ~/git/swt_dev_tools/gtk_builds/3.18.9/
#  (You need to create 3.xx.yy folder first)

# More documentation:
# see: https://wiki.eclipse.org/SWT/Devel/Gtk/JUnitTests

#### CONFIG
# script assumes that swt source and swt_dev repos are in the default: $HOME/git/... but you can change it:
REPO=$HOME/git

# Location of your gtk_builds. Only change this if you use your own build folder. The build folder should contain just the 'so' files.
GTK_BUILD_DIR=$HOME/git/swt_dev_tools/gtk_builds

LOG_FILE=gtk_test_out.log
LOG_ERROR_FILE=gtk_test_error.log
#### CONFIG END

# Script intro:
echo "Running swt jUnit tests. This may take a while."
echo "Warnings redirected to gtk_test_error.log. Copy of output in gtk_test_out.log"
echo ""

# Clean up from Previous runs:
rm -f $LOG_ERROR_FILE
rm -f $LOG_FILE  # Duplicate output into this file for parsing later.

# Helper functions
func_run_tests () {
  # $1 = SWT tests to run. Ex AllNonBrowserTests

  java -Djava.library.path=$REPO/eclipse.platform.swt.binaries/bundles/org.eclipse.swt.gtk.linux.x86_64 \
  -Dfile.encoding=UTF-8 \
  -classpath /usr/share/java/junit.jar:/usr/share/java/hamcrest/core.jar:$REPO/eclipse.platform.swt/bundles/org.eclipse.swt/bin:$REPO/eclipse.platform.swt/tests/org.eclipse.swt.tests/bin org.eclipse.swt.tests.junit.CmdLineInterface $1 2>> $LOG_ERROR_FILE | tee -a $LOG_FILE
}

func_print_to_logs () {
  # $1 = message to append to long.
  (>&2 echo "$1") 2>> $LOG_ERROR_FILE
  echo "$1" | tee -a $LOG_FILE
}

func_export_ld () {
  # $1 = Gtk version that maches compiled Gtk version. ex 3.16.7
  export LD_LIBRARY_PATH=$GTK_BUILD_DIR/$1
}

# Initialization
TESTS=AllNonBrowserTests

# Test with native libraries:
MSG="------ Testing with gtk libraries from current system ------"
func_print_to_logs "$MSG"
func_run_tests $TESTS

# Tests with compiled versions of GTK 3
export SWT_GTK3=1
for i in $(ls $GTK_BUILD_DIR | grep 3\. | sort -r)
do
  MSG="------ Testing with $i ---------------"
  func_print_to_logs "$MSG"
  func_export_ld "$i"
  func_run_tests $TESTS
done


# Tests with compiled GKT2.
export SWT_GTK3=0
MSG="------ Testing with Gtk 2.24 ------------------"
func_print_to_logs "$MSG"
func_export_ld "2.24"   #this be hard-coded.
func_run_tests $TESTS


# post tests:
echo ""
echo " ~~SUMMARY OF SUMMARIES:"
cat $LOG_FILE | grep "GTK_TEST_RESULT"
echo ""
echo "To see warnings that were thrown during tests, see: $LOG_ERROR_FILE"
