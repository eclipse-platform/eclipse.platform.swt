/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/* Special sizeof's */
#define sigaction_sizeof() sizeof(struct sigaction)

/* Libraries for dynamic loaded functions */
#define XpmReadFileToPixmap_LIB "libXpm.so"

#define KMimeTypeListIterator_dereference(arg0) *(*((QValueListIterator<KMimeType::Ptr>*) arg0))
#define KMimeTypeListIterator_equals(arg0, arg1) (*((QValueListIterator<KMimeType::Ptr>*) arg0) == *((QValueListIterator<KMimeType::Ptr>*) arg1))
#define KMimeTypeListIterator_increment(arg0) ++(*((QValueListIterator<KMimeType::Ptr>*) arg0))
#define QStringListIterator_dereference(arg0) *(*((QValueListIterator<QString>*) arg0))
#define QStringListIterator_equals(arg0, arg1) (*((QValueListIterator<KMimeType::Ptr>*) arg0) == *((QValueListIterator<KMimeType::Ptr>*) arg1))
#define QStringListIterator_increment(arg0) ++(*((QValueListIterator<KMimeType::Ptr>*) arg0))
#define QString_equals(arg0, arg1) (*((QString*) arg0) == *((QString*) arg1))



