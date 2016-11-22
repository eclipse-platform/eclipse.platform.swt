#!/usr/bin/python3
#*******************************************************************************
# Copyright (c) 2016 The Eclipse Foundation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Rolf Theunissen - initial version, adapted from dynamic_deprecated.py
#*******************************************************************************

import sys
import re
from urllib.request import urlopen
from html.parser import HTMLParser

API_GTK2_STABLE_URL = "https://developer.gnome.org/gtk2/stable/api-index-full.html"
API_GTK3_0_URL = "https://developer.gnome.org/gtk3/3.0/api-index-full.html"
API_GTK3_STABLE_URL = "https://developer.gnome.org/gtk3/stable/api-index-full.html"
API_GTK3_DEPRECATED_URL = "https://developer.gnome.org/gtk3/stable/api-index-deprecated.html"

OS_CUSTOM_FILE = "./../org.eclipse.swt/Eclipse SWT PI/gtk/library/os_custom.h"
FUNCTION_LIST = []
GTK2_FUNCTION_LIST = []
GTK30_FUNCTION_LIST = []
GTK3x_FUNCTION_LIST = []
GTK3x_DEPR_FUNCTION_LIST = []

GTK2_GTK30_SHARED_FUNCTION_LIST = []
GTK2_GTK3x_SHARED_FUNCTION_LIST = []

# This script is Python 3 only.
#
# USAGE OPTIONS:
# <no option specified>: prints list of deprecated dynamic functions to console.
# -f: writes the list of deprecated dynamic functions to a file called dynamic_deprecated_functions.txt.
#     The file is in the same directory as the script

def download_symbols(api_url):
    # Fetch symbols from api_url
    with urlopen(api_url) as url:
        dep_file = url.read()

    return str(dep_file);

def download_gtk2_stable_symbols():
    return download_symbols(API_GTK2_STABLE_URL);

def download_gtk3_0_symbols():
    return download_symbols(API_GTK3_0_URL);

def download_gtk3_stable_symbols():
    return download_symbols(API_GTK3_STABLE_URL);

def download_gtk3_deprecated_symbols():
    return download_symbols(API_GTK3_DEPRECATED_URL);

def populate_function_list():
    # Read os_custom.h from file
    with open(OS_CUSTOM_FILE) as f:
        os_custom = f.read()

    return str(os_custom);

def find_gtk_version_functions(custom_str, gtk2_str, gtk30_str, gtk3x_str, gtk3x_dpr_str):
    regex = r"((\w)+(_)+(LIB)(\s)+(LIB_)+)+"
    matchList = re.findall(regex, custom_str)

    # Search through list of regex matches, strip the "_LIB" and populate
    # the dynamic function list.
    for match in matchList:
        location = match[0].find("_LIB")
        function_name = match[0][:location]
        FUNCTION_LIST.append(function_name)

    # Check each dynamic function and see if it's deprecated: if so, populate
    # the deprecated function list.
    for func in FUNCTION_LIST:
        func_tag = ">" + func + "<"
        if func_tag in gtk2_str:
            GTK2_FUNCTION_LIST.append(func)
        if func_tag in gtk30_str:
            GTK30_FUNCTION_LIST.append(func)
        if func_tag in gtk3x_str:
            GTK3x_FUNCTION_LIST.append(func)
        if func_tag in gtk3x_dpr_str:
            GTK3x_DEPR_FUNCTION_LIST.append(func)
    return;

def find_gtk2_and_gtk3_shared():
    for func in GTK2_FUNCTION_LIST:
        if func in GTK30_FUNCTION_LIST:
            GTK2_GTK30_SHARED_FUNCTION_LIST.append(func)
        elif func in GTK30_FUNCTION_LIST:
            GTK2_GTK3x_SHARED_FUNCTION_LIST.append(func)


if __name__ == "__main__":
    gtk2functions = download_gtk2_stable_symbols()
    gtk30functions = download_gtk3_0_symbols()
    gtk3xfunctions = download_gtk3_stable_symbols()
    gtk3xdeprecated = download_gtk3_deprecated_symbols()

    os_custom_str = populate_function_list()

    find_gtk_version_functions(os_custom_str, gtk2functions, gtk30functions,
            gtk3xfunctions, gtk3xdeprecated)

    find_gtk2_and_gtk3_shared()

    # If the user specified the "-f" option, the output will be written to
    # a file named "dynamic_deprecated_functions.txt"
    if len(sys.argv) == 2 and "-f" in str(sys.argv):
        with open("dynamic_gtk2x_gtk30_shared_functions.txt","w") as f:
            for i in GTK2_GTK30_SHARED_FUNCTION_LIST:
                if i in GTK3x_DEPR_FUNCTION_LIST:
                    f.write ("#")
                f.write(i + "\n")
            f.close()
        with open("dynamic_gtk2x_gtk3x_shared_functions.txt","w") as f:
            for i in GTK2_GTK3x_SHARED_FUNCTION_LIST:
                if i in GTK3x_DEPR_FUNCTION_LIST:
                    f.write ("#")
                f.write(i + "\n")
            f.close()
    # If no options are specified, print the list to console
    else:
        print ("# GTK2.x - GTK3.0 SHARED")
        for i in GTK2_GTK30_SHARED_FUNCTION_LIST:
            if i in GTK3x_DEPR_FUNCTION_LIST:
                print("#" + i)
            else:
                print(i)

        print()
        print ("# GTK2.x - GTK3.x SHARED")
        for i in GTK2_GTK3x_SHARED_FUNCTION_LIST:
            if i in GTK3x_DEPR_FUNCTION_LIST:
                print("#" + i)
            else:
                print(i)
    sys.exit(0)
