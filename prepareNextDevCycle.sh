#!/bin/bash -xe

#*******************************************************************************
# Copyright (c) 2025, 2025 IBM Hannes Wellmann and others.
#
# This program and the accompanying materials
# are made available under the terms of the Eclipse Public License 2.0
# which accompanies this distribution, and is available at
# https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Hannes Wellmann - initial API and implementation
#*******************************************************************************

# This script is called by the pipeline for preparing the next development cycle (this file's name is crucial!)
# and applies the changes required individually for SWT.

make_common_mak='bundles/org.eclipse.swt/Eclipse SWT/common/library/make_common.mak'
source "${make_common_mak}"
new_min_ver=$((min_ver + 1))

sed -i "${make_common_mak}" \
	--expression "s|min_ver=${min_ver}|min_ver=${new_min_ver}|g" \
	--expression "s|rev=[0-9]\+|rev=0|g"

sed -i 'bundles/org.eclipse.swt/Eclipse SWT PI/common/org/eclipse/swt/internal/Library.java' \
	--expression "s|MINOR_VERSION = ${min_ver};|MINOR_VERSION = ${new_min_ver};|g" \
	--expression "s|REVISION = [0-9]\+;|REVISION = 0;|g"

echo "version ${maj_ver}.${new_min_ver}" > 'bundles/org.eclipse.swt/Eclipse SWT/common/version.txt'


git commit --all --message "Configure SWT build scripts for ${NEXT_RELEASE_VERSION}"
