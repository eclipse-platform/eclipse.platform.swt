/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSString extends NSObject {

public NSString() {
	super();
}

public NSString(int id) {
	super(id);
}

public int UTF8String() {
	return OS.objc_msgSend(this.id, OS.sel_UTF8String);
}

public static int availableStringEncodings() {
	return OS.objc_msgSend(OS.class_NSString, OS.sel_availableStringEncodings);
}

public boolean boolValue() {
	return OS.objc_msgSend(this.id, OS.sel_boolValue) != 0;
}

public int cString() {
	return OS.objc_msgSend(this.id, OS.sel_cString);
}

public int cStringLength() {
	return OS.objc_msgSend(this.id, OS.sel_cStringLength);
}

public int cStringUsingEncoding(int encoding) {
	return OS.objc_msgSend(this.id, OS.sel_cStringUsingEncoding_1, encoding);
}

public boolean canBeConvertedToEncoding(int encoding) {
	return OS.objc_msgSend(this.id, OS.sel_canBeConvertedToEncoding_1, encoding) != 0;
}

public NSString capitalizedString() {
	int result = OS.objc_msgSend(this.id, OS.sel_capitalizedString);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public int caseInsensitiveCompare(NSString string) {
	return OS.objc_msgSend(this.id, OS.sel_caseInsensitiveCompare_1, string != null ? string.id : 0);
}

public short characterAtIndex(int index) {
	return (short)OS.objc_msgSend(this.id, OS.sel_characterAtIndex_1, index);
}

public NSString commonPrefixWithString(NSString aString, int mask) {
	int result = OS.objc_msgSend(this.id, OS.sel_commonPrefixWithString_1options_1, aString != null ? aString.id : 0, mask);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public int compare_(NSString string) {
	return OS.objc_msgSend(this.id, OS.sel_compare_1, string != null ? string.id : 0);
}

public int compare_options_(NSString string, int mask) {
	return OS.objc_msgSend(this.id, OS.sel_compare_1options_1, string != null ? string.id : 0, mask);
}

public int compare_options_range_(NSString string, int mask, NSRange compareRange) {
	return OS.objc_msgSend(this.id, OS.sel_compare_1options_1range_1, string != null ? string.id : 0, mask, compareRange);
}

public int compare_options_range_locale_(NSString string, int mask, NSRange compareRange, id locale) {
	return OS.objc_msgSend(this.id, OS.sel_compare_1options_1range_1locale_1, string != null ? string.id : 0, mask, compareRange, locale != null ? locale.id : 0);
}

public int completePathIntoString(int outputName, boolean flag, int outputArray, NSArray filterTypes) {
	return OS.objc_msgSend(this.id, OS.sel_completePathIntoString_1caseSensitive_1matchesIntoArray_1filterTypes_1, outputName, flag, outputArray, filterTypes != null ? filterTypes.id : 0);
}

public NSArray componentsSeparatedByCharactersInSet(NSCharacterSet separator) {
	int result = OS.objc_msgSend(this.id, OS.sel_componentsSeparatedByCharactersInSet_1, separator != null ? separator.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray componentsSeparatedByString(NSString separator) {
	int result = OS.objc_msgSend(this.id, OS.sel_componentsSeparatedByString_1, separator != null ? separator.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSData dataUsingEncoding_(int encoding) {
	int result = OS.objc_msgSend(this.id, OS.sel_dataUsingEncoding_1, encoding);
	return result != 0 ? new NSData(result) : null;
}

public NSData dataUsingEncoding_allowLossyConversion_(int encoding, boolean lossy) {
	int result = OS.objc_msgSend(this.id, OS.sel_dataUsingEncoding_1allowLossyConversion_1, encoding, lossy);
	return result != 0 ? new NSData(result) : null;
}

public NSString decomposedStringWithCanonicalMapping() {
	int result = OS.objc_msgSend(this.id, OS.sel_decomposedStringWithCanonicalMapping);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString decomposedStringWithCompatibilityMapping() {
	int result = OS.objc_msgSend(this.id, OS.sel_decomposedStringWithCompatibilityMapping);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public static int defaultCStringEncoding() {
	return OS.objc_msgSend(OS.class_NSString, OS.sel_defaultCStringEncoding);
}

public NSString description() {
	int result = OS.objc_msgSend(this.id, OS.sel_description);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public double doubleValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_doubleValue);
}

public int fastestEncoding() {
	return OS.objc_msgSend(this.id, OS.sel_fastestEncoding);
}

public int fileSystemRepresentation() {
	return OS.objc_msgSend(this.id, OS.sel_fileSystemRepresentation);
}

public float floatValue() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_floatValue);
}

public boolean getBytes(int buffer, int maxBufferCount, int usedBufferCount, int encoding, int options, NSRange range, int leftover) {
	return OS.objc_msgSend(this.id, OS.sel_getBytes_1maxLength_1usedLength_1encoding_1options_1range_1remainingRange_1, buffer, maxBufferCount, usedBufferCount, encoding, options, range, leftover) != 0;
}

public void getCString_(int bytes) {
	OS.objc_msgSend(this.id, OS.sel_getCString_1, bytes);
}

public void getCString_maxLength_(int bytes, int maxLength) {
	OS.objc_msgSend(this.id, OS.sel_getCString_1maxLength_1, bytes, maxLength);
}

public boolean getCString_maxLength_encoding_(int buffer, int maxBufferCount, int encoding) {
	return OS.objc_msgSend(this.id, OS.sel_getCString_1maxLength_1encoding_1, buffer, maxBufferCount, encoding) != 0;
}

public void getCString_maxLength_range_remainingRange_(int bytes, int maxLength, NSRange aRange, int leftoverRange) {
	OS.objc_msgSend(this.id, OS.sel_getCString_1maxLength_1range_1remainingRange_1, bytes, maxLength, aRange, leftoverRange);
}

public void getCharacters_(char[] buffer) {
	OS.objc_msgSend(this.id, OS.sel_getCharacters_1, buffer);
}

public void getCharacters_range_(char[] buffer, NSRange aRange) {
	OS.objc_msgSend(this.id, OS.sel_getCharacters_1range_1, buffer, aRange);
}

public boolean getFileSystemRepresentation(int cname, int max) {
	return OS.objc_msgSend(this.id, OS.sel_getFileSystemRepresentation_1maxLength_1, cname, max) != 0;
}

public void getLineStart(int startPtr, int lineEndPtr, int contentsEndPtr, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_getLineStart_1end_1contentsEnd_1forRange_1, startPtr, lineEndPtr, contentsEndPtr, range);
}

public void getParagraphStart(int startPtr, int parEndPtr, int contentsEndPtr, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_getParagraphStart_1end_1contentsEnd_1forRange_1, startPtr, parEndPtr, contentsEndPtr, range);
}

public boolean hasPrefix(NSString aString) {
	return OS.objc_msgSend(this.id, OS.sel_hasPrefix_1, aString != null ? aString.id : 0) != 0;
}

public boolean hasSuffix(NSString aString) {
	return OS.objc_msgSend(this.id, OS.sel_hasSuffix_1, aString != null ? aString.id : 0) != 0;
}

public int hash() {
	return OS.objc_msgSend(this.id, OS.sel_hash);
}

public NSString initWithBytes(int bytes, int len, int encoding) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBytes_1length_1encoding_1, bytes, len, encoding);
	return result != 0 ? this : null;
}

public NSString initWithBytesNoCopy(int bytes, int len, int encoding, boolean freeBuffer) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithBytesNoCopy_1length_1encoding_1freeWhenDone_1, bytes, len, encoding, freeBuffer);
	return result != 0 ? this : null;
}

public NSString initWithCString_(int bytes) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCString_1, bytes);
	return result != 0 ? this : null;
}

public NSString initWithCString_encoding_(int nullTerminatedCString, int encoding) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCString_1encoding_1, nullTerminatedCString, encoding);
	return result != 0 ? this : null;
}

public NSString initWithCString_length_(int bytes, int length) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCString_1length_1, bytes, length);
	return result != 0 ? this : null;
}

public NSString initWithCStringNoCopy(int bytes, int length, boolean freeBuffer) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCStringNoCopy_1length_1freeWhenDone_1, bytes, length, freeBuffer);
	return result != 0 ? this : null;
}

public NSString initWithCharacters(int characters, int length) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCharacters_1length_1, characters, length);
	return result != 0 ? this : null;
}

public NSString initWithCharactersNoCopy(int characters, int length, boolean freeBuffer) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCharactersNoCopy_1length_1freeWhenDone_1, characters, length, freeBuffer);
	return result != 0 ? this : null;
}

public NSString initWithContentsOfFile_(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfFile_1, path != null ? path.id : 0);
	return result != 0 ? this : null;
}

public NSString initWithContentsOfFile_encoding_error_(NSString path, int enc, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfFile_1encoding_1error_1, path != null ? path.id : 0, enc, error);
	return result != 0 ? this : null;
}

public NSString initWithContentsOfFile_usedEncoding_error_(NSString path, int enc, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfFile_1usedEncoding_1error_1, path != null ? path.id : 0, enc, error);
	return result != 0 ? this : null;
}

public NSString initWithContentsOfURL_(NSURL url) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1, url != null ? url.id : 0);
	return result != 0 ? this : null;
}

public NSString initWithContentsOfURL_encoding_error_(NSURL url, int enc, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1encoding_1error_1, url != null ? url.id : 0, enc, error);
	return result != 0 ? this : null;
}

public NSString initWithContentsOfURL_usedEncoding_error_(NSURL url, int enc, int error) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1usedEncoding_1error_1, url != null ? url.id : 0, enc, error);
	return result != 0 ? this : null;
}

public NSString initWithData(NSData data, int encoding) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1encoding_1, data != null ? data.id : 0, encoding);
	return result != 0 ? this : null;
}

public NSString initWithFormat_(NSString initWithFormat) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFormat_1, initWithFormat != null ? initWithFormat.id : 0);
	return result != 0 ? this : null;
}

public NSString initWithFormat_arguments_(NSString format, int argList) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFormat_1arguments_1, format != null ? format.id : 0, argList);
	return result != 0 ? this : null;
}

public NSString initWithFormat_locale_(NSString format, id locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFormat_1locale_1, format != null ? format.id : 0, locale != null ? locale.id : 0);
	return result != 0 ? this : null;
}

public NSString initWithFormat_locale_arguments_(NSString format, id locale, int argList) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFormat_1locale_1arguments_1, format != null ? format.id : 0, locale != null ? locale.id : 0, argList);
	return result != 0 ? this : null;
}

public NSString initWithString(NSString aString) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithString_1, aString != null ? aString.id : 0);
	return result != 0 ? this : null;
}

public NSString initWithUTF8String(int nullTerminatedCString) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithUTF8String_1, nullTerminatedCString);
	return result != 0 ? this : null;
}

public int intValue() {
	return OS.objc_msgSend(this.id, OS.sel_intValue);
}

public int integerValue() {
	return OS.objc_msgSend(this.id, OS.sel_integerValue);
}

public boolean isAbsolutePath() {
	return OS.objc_msgSend(this.id, OS.sel_isAbsolutePath) != 0;
}

public boolean isEqualToString(NSString aString) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToString_1, aString != null ? aString.id : 0) != 0;
}

public NSString lastPathComponent() {
	int result = OS.objc_msgSend(this.id, OS.sel_lastPathComponent);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public int length() {
	return OS.objc_msgSend(this.id, OS.sel_length);
}

public int lengthOfBytesUsingEncoding(int enc) {
	return OS.objc_msgSend(this.id, OS.sel_lengthOfBytesUsingEncoding_1, enc);
}

public NSRange lineRangeForRange(NSRange range) {
	NSRange result = new NSRange();
	OS.objc_msgSend_struct(result, this.id, OS.sel_lineRangeForRange_1, range);
	return result;
}

public int localizedCaseInsensitiveCompare(NSString string) {
	return OS.objc_msgSend(this.id, OS.sel_localizedCaseInsensitiveCompare_1, string != null ? string.id : 0);
}

public int localizedCompare(NSString string) {
	return OS.objc_msgSend(this.id, OS.sel_localizedCompare_1, string != null ? string.id : 0);
}

public static NSString localizedNameOfStringEncoding(int encoding) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_localizedNameOfStringEncoding_1, encoding);
	return result != 0 ? new NSString(result) : null;
}

public static id localizedStringWithFormat(NSString localizedStringWithFormat) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_localizedStringWithFormat_1, localizedStringWithFormat != null ? localizedStringWithFormat.id : 0);
	return result != 0 ? new id(result) : null;
}

public long longLongValue() {
	return (long)OS.objc_msgSend(this.id, OS.sel_longLongValue);
}

public int lossyCString() {
	return OS.objc_msgSend(this.id, OS.sel_lossyCString);
}

public NSString lowercaseString() {
	int result = OS.objc_msgSend(this.id, OS.sel_lowercaseString);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public int maximumLengthOfBytesUsingEncoding(int enc) {
	return OS.objc_msgSend(this.id, OS.sel_maximumLengthOfBytesUsingEncoding_1, enc);
}

public NSRange paragraphRangeForRange(NSRange range) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_paragraphRangeForRange_1, range);
	return result;
}

public NSArray pathComponents() {
	int result = OS.objc_msgSend(this.id, OS.sel_pathComponents);
	return result != 0 ? new NSArray(result) : null;
}

public NSString pathExtension() {
	int result = OS.objc_msgSend(this.id, OS.sel_pathExtension);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public static NSString pathWithComponents(NSArray components) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_pathWithComponents_1, components != null ? components.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString precomposedStringWithCanonicalMapping() {
	int result = OS.objc_msgSend(this.id, OS.sel_precomposedStringWithCanonicalMapping);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString precomposedStringWithCompatibilityMapping() {
	int result = OS.objc_msgSend(this.id, OS.sel_precomposedStringWithCompatibilityMapping);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public id propertyList() {
	int result = OS.objc_msgSend(this.id, OS.sel_propertyList);
	return result != 0 ? new id(result) : null;
}

public NSDictionary propertyListFromStringsFileFormat() {
	int result = OS.objc_msgSend(this.id, OS.sel_propertyListFromStringsFileFormat);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSRange rangeOfCharacterFromSet_(NSCharacterSet aSet) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeOfCharacterFromSet_1, aSet != null ? aSet.id : 0);
	return result;
}

public NSRange rangeOfCharacterFromSet_options_(NSCharacterSet aSet, int mask) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeOfCharacterFromSet_1options_1, aSet != null ? aSet.id : 0, mask);
	return result;
}

public NSRange rangeOfCharacterFromSet_options_range_(NSCharacterSet aSet, int mask, NSRange searchRange) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeOfCharacterFromSet_1options_1range_1, aSet != null ? aSet.id : 0, mask, searchRange);
	return result;
}

public NSRange rangeOfComposedCharacterSequenceAtIndex(int index) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeOfComposedCharacterSequenceAtIndex_1, index);
	return result;
}

public NSRange rangeOfComposedCharacterSequencesForRange(NSRange range) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeOfComposedCharacterSequencesForRange_1, range);
	return result;
}

public NSRange rangeOfString_(NSString aString) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeOfString_1, aString != null ? aString.id : 0);
	return result;
}

public NSRange rangeOfString_options_(NSString aString, int mask) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeOfString_1options_1, aString != null ? aString.id : 0, mask);
	return result;
}

public NSRange rangeOfString_options_range_(NSString aString, int mask, NSRange searchRange) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeOfString_1options_1range_1, aString != null ? aString.id : 0, mask, searchRange);
	return result;
}

public NSRange rangeOfString_options_range_locale_(NSString aString, int mask, NSRange searchRange, NSLocale locale) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeOfString_1options_1range_1locale_1, aString != null ? aString.id : 0, mask, searchRange, locale != null ? locale.id : 0);
	return result;
}

public int smallestEncoding() {
	return OS.objc_msgSend(this.id, OS.sel_smallestEncoding);
}

public static NSString string() {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_string);
	return result != 0 ? new NSString(result) : null;
}

public NSString stringByAbbreviatingWithTildeInPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByAbbreviatingWithTildeInPath);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAddingPercentEscapesUsingEncoding(int enc) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByAddingPercentEscapesUsingEncoding_1, enc);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAppendingFormat(NSString stringByAppendingFormat) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByAppendingFormat_1, stringByAppendingFormat != null ? stringByAppendingFormat.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAppendingPathComponent(NSString str) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByAppendingPathComponent_1, str != null ? str.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAppendingPathExtension(NSString str) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByAppendingPathExtension_1, str != null ? str.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByAppendingString(NSString aString) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByAppendingString_1, aString != null ? aString.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByDeletingLastPathComponent() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByDeletingLastPathComponent);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByDeletingPathExtension() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByDeletingPathExtension);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByExpandingTildeInPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByExpandingTildeInPath);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByFoldingWithOptions(int options, NSLocale locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByFoldingWithOptions_1locale_1, options, locale != null ? locale.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByPaddingToLength(int newLength, NSString padString, int padIndex) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByPaddingToLength_1withString_1startingAtIndex_1, newLength, padString != null ? padString.id : 0, padIndex);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByReplacingCharactersInRange(NSRange range, NSString replacement) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByReplacingCharactersInRange_1withString_1, range, replacement != null ? replacement.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByReplacingOccurrencesOfString_withString_(NSString target, NSString replacement) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByReplacingOccurrencesOfString_1withString_1, target != null ? target.id : 0, replacement != null ? replacement.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByReplacingOccurrencesOfString_withString_options_range_(NSString target, NSString replacement, int options, NSRange searchRange) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByReplacingOccurrencesOfString_1withString_1options_1range_1, target != null ? target.id : 0, replacement != null ? replacement.id : 0, options, searchRange);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByReplacingPercentEscapesUsingEncoding(int enc) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByReplacingPercentEscapesUsingEncoding_1, enc);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByResolvingSymlinksInPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByResolvingSymlinksInPath);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByStandardizingPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByStandardizingPath);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString stringByTrimmingCharactersInSet(NSCharacterSet set) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringByTrimmingCharactersInSet_1, set != null ? set.id : 0);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public static id static_stringWithCString_(int bytes) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithCString_1, bytes);
	return result != 0 ? new NSString(result) : null;
}

public static id static_stringWithCString_encoding_(int cString, int enc) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithCString_1encoding_1, cString, enc);
	return result != 0 ? new NSString(result) : null;
}

public static NSString static_stringWithCString_length_(int bytes, int length) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithCString_1length_1, bytes, length);
	return result != 0 ? new NSString(result) : null;
}

public static NSString stringWithCharacters(char[] characters, int length) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithCharacters_1length_1, characters, length);
	return result != 0 ? new NSString(result) : null;
}

public static NSString stringWith(String str) {
	char[] chars = new char[str.length()];
	str.getChars(0, chars.length, chars, 0);
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithCharacters_1length_1, chars, chars.length);
	return result != 0 ? new NSString(result) : null;
}

public static id static_stringWithContentsOfFile_(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithContentsOfFile_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_stringWithContentsOfFile_encoding_error_(NSString path, int enc, int error) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithContentsOfFile_1encoding_1error_1, path != null ? path.id : 0, enc, error);
	return result != 0 ? new id(result) : null;
}

public static id static_stringWithContentsOfFile_usedEncoding_error_(NSString path, int enc, int error) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithContentsOfFile_1usedEncoding_1error_1, path != null ? path.id : 0, enc, error);
	return result != 0 ? new id(result) : null;
}

public static id static_stringWithContentsOfURL_(NSURL url) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithContentsOfURL_1, url != null ? url.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_stringWithContentsOfURL_encoding_error_(NSURL url, int enc, int error) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithContentsOfURL_1encoding_1error_1, url != null ? url.id : 0, enc, error);
	return result != 0 ? new id(result) : null;
}

public static id static_stringWithContentsOfURL_usedEncoding_error_(NSURL url, int enc, int error) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithContentsOfURL_1usedEncoding_1error_1, url != null ? url.id : 0, enc, error);
	return result != 0 ? new id(result) : null;
}

public static id stringWithFormat(NSString stringWithFormat) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithFormat_1, stringWithFormat != null ? stringWithFormat.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id stringWithString(NSString string) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithString_1, string != null ? string.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSString stringWithUTF8String(int nullTerminatedCString) {
	int result = OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithUTF8String_1, nullTerminatedCString);
	return result != 0 ? new NSString(result) : null;
}

public NSArray stringsByAppendingPaths(NSArray paths) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringsByAppendingPaths_1, paths != null ? paths.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSString substringFromIndex(int from) {
	int result = OS.objc_msgSend(this.id, OS.sel_substringFromIndex_1, from);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString substringToIndex(int to) {
	int result = OS.objc_msgSend(this.id, OS.sel_substringToIndex_1, to);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString substringWithRange(NSRange range) {
	int result = OS.objc_msgSend(this.id, OS.sel_substringWithRange_1, range);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public NSString uppercaseString() {
	int result = OS.objc_msgSend(this.id, OS.sel_uppercaseString);
	return result == this.id ? this : (result != 0 ? new NSString(result) : null);
}

public boolean writeToFile_atomically_(NSString path, boolean useAuxiliaryFile) {
	return OS.objc_msgSend(this.id, OS.sel_writeToFile_1atomically_1, path != null ? path.id : 0, useAuxiliaryFile) != 0;
}

public boolean writeToFile_atomically_encoding_error_(NSString path, boolean useAuxiliaryFile, int enc, int error) {
	return OS.objc_msgSend(this.id, OS.sel_writeToFile_1atomically_1encoding_1error_1, path != null ? path.id : 0, useAuxiliaryFile, enc, error) != 0;
}

public boolean writeToURL_atomically_(NSURL url, boolean atomically) {
	return OS.objc_msgSend(this.id, OS.sel_writeToURL_1atomically_1, url != null ? url.id : 0, atomically) != 0;
}

public boolean writeToURL_atomically_encoding_error_(NSURL url, boolean useAuxiliaryFile, int enc, int error) {
	return OS.objc_msgSend(this.id, OS.sel_writeToURL_1atomically_1encoding_1error_1, url != null ? url.id : 0, useAuxiliaryFile, enc, error) != 0;
}

}
