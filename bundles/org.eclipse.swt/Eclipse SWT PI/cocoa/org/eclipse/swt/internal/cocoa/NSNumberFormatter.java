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

public class NSNumberFormatter extends NSFormatter {

public NSNumberFormatter() {
	super();
}

public NSNumberFormatter(int id) {
	super(id);
}

public boolean allowsFloats() {
	return OS.objc_msgSend(this.id, OS.sel_allowsFloats) != 0;
}

public boolean alwaysShowsDecimalSeparator() {
	return OS.objc_msgSend(this.id, OS.sel_alwaysShowsDecimalSeparator) != 0;
}

public NSAttributedString attributedStringForNil() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedStringForNil);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSAttributedString attributedStringForNotANumber() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedStringForNotANumber);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSAttributedString attributedStringForZero() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedStringForZero);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSString currencyCode() {
	int result = OS.objc_msgSend(this.id, OS.sel_currencyCode);
	return result != 0 ? new NSString(result) : null;
}

public NSString currencyDecimalSeparator() {
	int result = OS.objc_msgSend(this.id, OS.sel_currencyDecimalSeparator);
	return result != 0 ? new NSString(result) : null;
}

public NSString currencyGroupingSeparator() {
	int result = OS.objc_msgSend(this.id, OS.sel_currencyGroupingSeparator);
	return result != 0 ? new NSString(result) : null;
}

public NSString currencySymbol() {
	int result = OS.objc_msgSend(this.id, OS.sel_currencySymbol);
	return result != 0 ? new NSString(result) : null;
}

public NSString decimalSeparator() {
	int result = OS.objc_msgSend(this.id, OS.sel_decimalSeparator);
	return result != 0 ? new NSString(result) : null;
}

public static int defaultFormatterBehavior() {
	return OS.objc_msgSend(OS.class_NSNumberFormatter, OS.sel_defaultFormatterBehavior);
}

public NSString exponentSymbol() {
	int result = OS.objc_msgSend(this.id, OS.sel_exponentSymbol);
	return result != 0 ? new NSString(result) : null;
}

public NSString format() {
	int result = OS.objc_msgSend(this.id, OS.sel_format);
	return result != 0 ? new NSString(result) : null;
}

public int formatWidth() {
	return OS.objc_msgSend(this.id, OS.sel_formatWidth);
}

public int formatterBehavior() {
	return OS.objc_msgSend(this.id, OS.sel_formatterBehavior);
}

public boolean generatesDecimalNumbers() {
	return OS.objc_msgSend(this.id, OS.sel_generatesDecimalNumbers) != 0;
}

public boolean getObjectValue(int obj, NSString string, int rangep, int error) {
	return OS.objc_msgSend(this.id, OS.sel_getObjectValue_1forString_1range_1error_1, obj, string != null ? string.id : 0, rangep, error) != 0;
}

public NSString groupingSeparator() {
	int result = OS.objc_msgSend(this.id, OS.sel_groupingSeparator);
	return result != 0 ? new NSString(result) : null;
}

public int groupingSize() {
	return OS.objc_msgSend(this.id, OS.sel_groupingSize);
}

public boolean hasThousandSeparators() {
	return OS.objc_msgSend(this.id, OS.sel_hasThousandSeparators) != 0;
}

public NSString internationalCurrencySymbol() {
	int result = OS.objc_msgSend(this.id, OS.sel_internationalCurrencySymbol);
	return result != 0 ? new NSString(result) : null;
}

public boolean isLenient() {
	return OS.objc_msgSend(this.id, OS.sel_isLenient) != 0;
}

public boolean isPartialStringValidationEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isPartialStringValidationEnabled) != 0;
}

public NSLocale locale() {
	int result = OS.objc_msgSend(this.id, OS.sel_locale);
	return result != 0 ? new NSLocale(result) : null;
}

public boolean localizesFormat() {
	return OS.objc_msgSend(this.id, OS.sel_localizesFormat) != 0;
}

public NSNumber maximum() {
	int result = OS.objc_msgSend(this.id, OS.sel_maximum);
	return result != 0 ? new NSNumber(result) : null;
}

public int maximumFractionDigits() {
	return OS.objc_msgSend(this.id, OS.sel_maximumFractionDigits);
}

public int maximumIntegerDigits() {
	return OS.objc_msgSend(this.id, OS.sel_maximumIntegerDigits);
}

public int maximumSignificantDigits() {
	return OS.objc_msgSend(this.id, OS.sel_maximumSignificantDigits);
}

public NSNumber minimum() {
	int result = OS.objc_msgSend(this.id, OS.sel_minimum);
	return result != 0 ? new NSNumber(result) : null;
}

public int minimumFractionDigits() {
	return OS.objc_msgSend(this.id, OS.sel_minimumFractionDigits);
}

public int minimumIntegerDigits() {
	return OS.objc_msgSend(this.id, OS.sel_minimumIntegerDigits);
}

public int minimumSignificantDigits() {
	return OS.objc_msgSend(this.id, OS.sel_minimumSignificantDigits);
}

public NSString minusSign() {
	int result = OS.objc_msgSend(this.id, OS.sel_minusSign);
	return result != 0 ? new NSString(result) : null;
}

public NSNumber multiplier() {
	int result = OS.objc_msgSend(this.id, OS.sel_multiplier);
	return result != 0 ? new NSNumber(result) : null;
}

public NSString negativeFormat() {
	int result = OS.objc_msgSend(this.id, OS.sel_negativeFormat);
	return result != 0 ? new NSString(result) : null;
}

public NSString negativeInfinitySymbol() {
	int result = OS.objc_msgSend(this.id, OS.sel_negativeInfinitySymbol);
	return result != 0 ? new NSString(result) : null;
}

public NSString negativePrefix() {
	int result = OS.objc_msgSend(this.id, OS.sel_negativePrefix);
	return result != 0 ? new NSString(result) : null;
}

public NSString negativeSuffix() {
	int result = OS.objc_msgSend(this.id, OS.sel_negativeSuffix);
	return result != 0 ? new NSString(result) : null;
}

public NSString nilSymbol() {
	int result = OS.objc_msgSend(this.id, OS.sel_nilSymbol);
	return result != 0 ? new NSString(result) : null;
}

public NSString notANumberSymbol() {
	int result = OS.objc_msgSend(this.id, OS.sel_notANumberSymbol);
	return result != 0 ? new NSString(result) : null;
}

public NSNumber numberFromString(NSString string) {
	int result = OS.objc_msgSend(this.id, OS.sel_numberFromString_1, string != null ? string.id : 0);
	return result != 0 ? new NSNumber(result) : null;
}

public int numberStyle() {
	return OS.objc_msgSend(this.id, OS.sel_numberStyle);
}

public NSString paddingCharacter() {
	int result = OS.objc_msgSend(this.id, OS.sel_paddingCharacter);
	return result != 0 ? new NSString(result) : null;
}

public int paddingPosition() {
	return OS.objc_msgSend(this.id, OS.sel_paddingPosition);
}

public NSString perMillSymbol() {
	int result = OS.objc_msgSend(this.id, OS.sel_perMillSymbol);
	return result != 0 ? new NSString(result) : null;
}

public NSString percentSymbol() {
	int result = OS.objc_msgSend(this.id, OS.sel_percentSymbol);
	return result != 0 ? new NSString(result) : null;
}

public NSString plusSign() {
	int result = OS.objc_msgSend(this.id, OS.sel_plusSign);
	return result != 0 ? new NSString(result) : null;
}

public NSString positiveFormat() {
	int result = OS.objc_msgSend(this.id, OS.sel_positiveFormat);
	return result != 0 ? new NSString(result) : null;
}

public NSString positiveInfinitySymbol() {
	int result = OS.objc_msgSend(this.id, OS.sel_positiveInfinitySymbol);
	return result != 0 ? new NSString(result) : null;
}

public NSString positivePrefix() {
	int result = OS.objc_msgSend(this.id, OS.sel_positivePrefix);
	return result != 0 ? new NSString(result) : null;
}

public NSString positiveSuffix() {
	int result = OS.objc_msgSend(this.id, OS.sel_positiveSuffix);
	return result != 0 ? new NSString(result) : null;
}

public NSDecimalNumberHandler roundingBehavior() {
	int result = OS.objc_msgSend(this.id, OS.sel_roundingBehavior);
	return result != 0 ? new NSDecimalNumberHandler(result) : null;
}

public NSNumber roundingIncrement() {
	int result = OS.objc_msgSend(this.id, OS.sel_roundingIncrement);
	return result != 0 ? new NSNumber(result) : null;
}

public int roundingMode() {
	return OS.objc_msgSend(this.id, OS.sel_roundingMode);
}

public int secondaryGroupingSize() {
	return OS.objc_msgSend(this.id, OS.sel_secondaryGroupingSize);
}

public void setAllowsFloats(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsFloats_1, flag);
}

public void setAlwaysShowsDecimalSeparator(boolean b) {
	OS.objc_msgSend(this.id, OS.sel_setAlwaysShowsDecimalSeparator_1, b);
}

public void setAttributedStringForNil(NSAttributedString newAttributedString) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedStringForNil_1, newAttributedString != null ? newAttributedString.id : 0);
}

public void setAttributedStringForNotANumber(NSAttributedString newAttributedString) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedStringForNotANumber_1, newAttributedString != null ? newAttributedString.id : 0);
}

public void setAttributedStringForZero(NSAttributedString newAttributedString) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedStringForZero_1, newAttributedString != null ? newAttributedString.id : 0);
}

public void setCurrencyCode(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setCurrencyCode_1, string != null ? string.id : 0);
}

public void setCurrencyDecimalSeparator(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setCurrencyDecimalSeparator_1, string != null ? string.id : 0);
}

public void setCurrencyGroupingSeparator(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setCurrencyGroupingSeparator_1, string != null ? string.id : 0);
}

public void setCurrencySymbol(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setCurrencySymbol_1, string != null ? string.id : 0);
}

public void setDecimalSeparator(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setDecimalSeparator_1, string != null ? string.id : 0);
}

public static void setDefaultFormatterBehavior(int behavior) {
	OS.objc_msgSend(OS.class_NSNumberFormatter, OS.sel_setDefaultFormatterBehavior_1, behavior);
}

public void setExponentSymbol(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setExponentSymbol_1, string != null ? string.id : 0);
}

public void setFormat(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setFormat_1, string != null ? string.id : 0);
}

public void setFormatWidth(int number) {
	OS.objc_msgSend(this.id, OS.sel_setFormatWidth_1, number);
}

public void setFormatterBehavior(int behavior) {
	OS.objc_msgSend(this.id, OS.sel_setFormatterBehavior_1, behavior);
}

public void setGeneratesDecimalNumbers(boolean b) {
	OS.objc_msgSend(this.id, OS.sel_setGeneratesDecimalNumbers_1, b);
}

public void setGroupingSeparator(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setGroupingSeparator_1, string != null ? string.id : 0);
}

public void setGroupingSize(int number) {
	OS.objc_msgSend(this.id, OS.sel_setGroupingSize_1, number);
}

public void setHasThousandSeparators(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHasThousandSeparators_1, flag);
}

public void setInternationalCurrencySymbol(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setInternationalCurrencySymbol_1, string != null ? string.id : 0);
}

public void setLenient(boolean b) {
	OS.objc_msgSend(this.id, OS.sel_setLenient_1, b);
}

public void setLocale(NSLocale locale) {
	OS.objc_msgSend(this.id, OS.sel_setLocale_1, locale != null ? locale.id : 0);
}

public void setLocalizesFormat(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setLocalizesFormat_1, flag);
}

public void setMaximum(NSNumber number) {
	OS.objc_msgSend(this.id, OS.sel_setMaximum_1, number != null ? number.id : 0);
}

public void setMaximumFractionDigits(int number) {
	OS.objc_msgSend(this.id, OS.sel_setMaximumFractionDigits_1, number);
}

public void setMaximumIntegerDigits(int number) {
	OS.objc_msgSend(this.id, OS.sel_setMaximumIntegerDigits_1, number);
}

public void setMaximumSignificantDigits(int number) {
	OS.objc_msgSend(this.id, OS.sel_setMaximumSignificantDigits_1, number);
}

public void setMinimum(NSNumber number) {
	OS.objc_msgSend(this.id, OS.sel_setMinimum_1, number != null ? number.id : 0);
}

public void setMinimumFractionDigits(int number) {
	OS.objc_msgSend(this.id, OS.sel_setMinimumFractionDigits_1, number);
}

public void setMinimumIntegerDigits(int number) {
	OS.objc_msgSend(this.id, OS.sel_setMinimumIntegerDigits_1, number);
}

public void setMinimumSignificantDigits(int number) {
	OS.objc_msgSend(this.id, OS.sel_setMinimumSignificantDigits_1, number);
}

public void setMinusSign(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setMinusSign_1, string != null ? string.id : 0);
}

public void setMultiplier(NSNumber number) {
	OS.objc_msgSend(this.id, OS.sel_setMultiplier_1, number != null ? number.id : 0);
}

public void setNegativeFormat(NSString format) {
	OS.objc_msgSend(this.id, OS.sel_setNegativeFormat_1, format != null ? format.id : 0);
}

public void setNegativeInfinitySymbol(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setNegativeInfinitySymbol_1, string != null ? string.id : 0);
}

public void setNegativePrefix(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setNegativePrefix_1, string != null ? string.id : 0);
}

public void setNegativeSuffix(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setNegativeSuffix_1, string != null ? string.id : 0);
}

public void setNilSymbol(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setNilSymbol_1, string != null ? string.id : 0);
}

public void setNotANumberSymbol(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setNotANumberSymbol_1, string != null ? string.id : 0);
}

public void setNumberStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setNumberStyle_1, style);
}

public void setPaddingCharacter(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setPaddingCharacter_1, string != null ? string.id : 0);
}

public void setPaddingPosition(int position) {
	OS.objc_msgSend(this.id, OS.sel_setPaddingPosition_1, position);
}

public void setPartialStringValidationEnabled(boolean b) {
	OS.objc_msgSend(this.id, OS.sel_setPartialStringValidationEnabled_1, b);
}

public void setPerMillSymbol(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setPerMillSymbol_1, string != null ? string.id : 0);
}

public void setPercentSymbol(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setPercentSymbol_1, string != null ? string.id : 0);
}

public void setPlusSign(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setPlusSign_1, string != null ? string.id : 0);
}

public void setPositiveFormat(NSString format) {
	OS.objc_msgSend(this.id, OS.sel_setPositiveFormat_1, format != null ? format.id : 0);
}

public void setPositiveInfinitySymbol(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setPositiveInfinitySymbol_1, string != null ? string.id : 0);
}

public void setPositivePrefix(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setPositivePrefix_1, string != null ? string.id : 0);
}

public void setPositiveSuffix(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setPositiveSuffix_1, string != null ? string.id : 0);
}

public void setRoundingBehavior(NSDecimalNumberHandler newRoundingBehavior) {
	OS.objc_msgSend(this.id, OS.sel_setRoundingBehavior_1, newRoundingBehavior != null ? newRoundingBehavior.id : 0);
}

public void setRoundingIncrement(NSNumber number) {
	OS.objc_msgSend(this.id, OS.sel_setRoundingIncrement_1, number != null ? number.id : 0);
}

public void setRoundingMode(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setRoundingMode_1, mode);
}

public void setSecondaryGroupingSize(int number) {
	OS.objc_msgSend(this.id, OS.sel_setSecondaryGroupingSize_1, number);
}

public void setTextAttributesForNegativeInfinity(NSDictionary newAttributes) {
	OS.objc_msgSend(this.id, OS.sel_setTextAttributesForNegativeInfinity_1, newAttributes != null ? newAttributes.id : 0);
}

public void setTextAttributesForNegativeValues(NSDictionary newAttributes) {
	OS.objc_msgSend(this.id, OS.sel_setTextAttributesForNegativeValues_1, newAttributes != null ? newAttributes.id : 0);
}

public void setTextAttributesForNil(NSDictionary newAttributes) {
	OS.objc_msgSend(this.id, OS.sel_setTextAttributesForNil_1, newAttributes != null ? newAttributes.id : 0);
}

public void setTextAttributesForNotANumber(NSDictionary newAttributes) {
	OS.objc_msgSend(this.id, OS.sel_setTextAttributesForNotANumber_1, newAttributes != null ? newAttributes.id : 0);
}

public void setTextAttributesForPositiveInfinity(NSDictionary newAttributes) {
	OS.objc_msgSend(this.id, OS.sel_setTextAttributesForPositiveInfinity_1, newAttributes != null ? newAttributes.id : 0);
}

public void setTextAttributesForPositiveValues(NSDictionary newAttributes) {
	OS.objc_msgSend(this.id, OS.sel_setTextAttributesForPositiveValues_1, newAttributes != null ? newAttributes.id : 0);
}

public void setTextAttributesForZero(NSDictionary newAttributes) {
	OS.objc_msgSend(this.id, OS.sel_setTextAttributesForZero_1, newAttributes != null ? newAttributes.id : 0);
}

public void setThousandSeparator(NSString newSeparator) {
	OS.objc_msgSend(this.id, OS.sel_setThousandSeparator_1, newSeparator != null ? newSeparator.id : 0);
}

public void setUsesGroupingSeparator(boolean b) {
	OS.objc_msgSend(this.id, OS.sel_setUsesGroupingSeparator_1, b);
}

public void setUsesSignificantDigits(boolean b) {
	OS.objc_msgSend(this.id, OS.sel_setUsesSignificantDigits_1, b);
}

public void setZeroSymbol(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setZeroSymbol_1, string != null ? string.id : 0);
}

public NSString stringFromNumber(NSNumber number) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringFromNumber_1, number != null ? number.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSDictionary textAttributesForNegativeInfinity() {
	int result = OS.objc_msgSend(this.id, OS.sel_textAttributesForNegativeInfinity);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary textAttributesForNegativeValues() {
	int result = OS.objc_msgSend(this.id, OS.sel_textAttributesForNegativeValues);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary textAttributesForNil() {
	int result = OS.objc_msgSend(this.id, OS.sel_textAttributesForNil);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary textAttributesForNotANumber() {
	int result = OS.objc_msgSend(this.id, OS.sel_textAttributesForNotANumber);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary textAttributesForPositiveInfinity() {
	int result = OS.objc_msgSend(this.id, OS.sel_textAttributesForPositiveInfinity);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary textAttributesForPositiveValues() {
	int result = OS.objc_msgSend(this.id, OS.sel_textAttributesForPositiveValues);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary textAttributesForZero() {
	int result = OS.objc_msgSend(this.id, OS.sel_textAttributesForZero);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSString thousandSeparator() {
	int result = OS.objc_msgSend(this.id, OS.sel_thousandSeparator);
	return result != 0 ? new NSString(result) : null;
}

public boolean usesGroupingSeparator() {
	return OS.objc_msgSend(this.id, OS.sel_usesGroupingSeparator) != 0;
}

public boolean usesSignificantDigits() {
	return OS.objc_msgSend(this.id, OS.sel_usesSignificantDigits) != 0;
}

public NSString zeroSymbol() {
	int result = OS.objc_msgSend(this.id, OS.sel_zeroSymbol);
	return result != 0 ? new NSString(result) : null;
}

}
