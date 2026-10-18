#!/usr/bin/python3

# Copyright (c) 2019 Red Hat and others. All rights reserved.
# The contents of this file are made available under the terms
# of the GNU Lesser General Public License (LGPL) Version 2.1 that
# accompanies this distribution (lgpl-v21.txt).  The LGPL is also
# available at http://www.gnu.org/licenses/lgpl.html.  If the version
# of the LGPL at http://www.gnu.org is different to the version of
# the LGPL accompanying this distribution and there is any conflict
# between the two license versions, the terms of the LGPL accompanying
# this distribution shall govern.
#
# Contributors:
#     Red Hat - initial API and implementation

import pyatspi

def printTree(root, indent=0):
    try:
        extents = root.queryComponent().getExtents(pyatspi.WINDOW_COORDS)
    except:
        extents = "()"

    print ("%s-> %s %s" % (" " * indent, root, extents))
    for child in root:
        printTree(child, indent+4)

def listener(e):
    # Substitute SWT here for snippets
    if e.host_application.name != "Eclipse":
        return

    print("%s is activated. Accessibility tree with extents:" % e.source)
    printTree(e.source)


pyatspi.Registry.registerEventListener(listener, "window:activate")
pyatspi.Registry.start()
