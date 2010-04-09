/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
  
#include "os.h"
#include <atk/atk.h>
#include "atk_custom.h"

#if (GTK_MAJOR_VERSION>=2 && GTK_MINOR_VERSION>=10)
#define SWT_AtkObjectClass_get_attributes get_attributes
#define SWT_AtkObjectClass_get_attributes_cast AtkAttributeSet* (*)()
#else
#define SWT_AtkObjectClass_get_attributes pad1
#define SWT_AtkObjectClass_get_attributes_cast AtkFunction
#endif 
