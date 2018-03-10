#!/usr/bin/python3

import sys
import re
from urllib.request import urlopen
from html.parser import HTMLParser

API_URL = "https://developer.gnome.org/gtk3/stable/api-index-deprecated.html"
OS_FILE = "./../../org.eclipse.swt/Eclipse SWT PI/gtk/library/os.c"
FUNCTION_LIST = []
DEPRECATED_FUNCTION_LIST = []
DEPRECATED_SYMBOLS = ""

# This script is Python 3 only.
#
# USAGE OPTIONS:
# <no option specified>: prints list of deprecated dynamic functions to console.
# -f: writes the list of deprecated dynamic functions to a file called dynamic_deprecated_functions.txt.
#     The file is in the same directory as the script


def download_deprecated_symbols():
    # Fetch latest deprecated symbols from the latest stable GTK3 API
    with urlopen(API_URL) as url:
        dep_file = url.read()

    return str(dep_file);

def populate_function_list():
    # Read os_custom.h from file
    with open(OS_FILE) as f:
        os_custom = f.read()

    return str(os_custom);

def find_deprecated_functions(custom_str, dep_str):
    regex = r"((GTK|GDK)_LOAD_FUNCTION\(fp, (\w+))+"
    matchList = re.findall(regex, custom_str)

    # Search through list of regex matches, strip the "_LIB" and populate
    # the dynamic function list.
    for match in matchList:
        function_name = match[2]
        FUNCTION_LIST.append(function_name)

    # Check each dynamic function and see if it's deprecated: if so, populate
    # the deprecated function list.
    for func in FUNCTION_LIST:
        if func in deprecated:
            DEPRECATED_FUNCTION_LIST.append(func)

    return;


if __name__ == "__main__":
    deprecated = download_deprecated_symbols()
    os_custom_str = populate_function_list()
    find_deprecated_functions(os_custom_str, deprecated)

    # If the list of deprecated functions is empty, print a warning and exit
    if not DEPRECATED_FUNCTION_LIST:
        print("No dynamic functions are deprecated!")
        sys.exit(0)

    # If the user specified the "-f" option, the output will be written to
    # a file named "dynamic_deprecated_functions.txt"
    if len(sys.argv) == 2 and "-f" in str(sys.argv):
        with open("dynamic_deprecated_functions.txt","w") as f:
            for i in DEPRECATED_FUNCTION_LIST:
                f.write(i + "\n")
            f.close()
    # If no options are specified, print the list to console
    else:
        for i in DEPRECATED_FUNCTION_LIST:
            print(i)

    sys.exit(0)
