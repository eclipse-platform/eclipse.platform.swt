/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
 
/**
 * Callback implementation.
 */
#include "callback.h"
#include <string.h>

#ifndef CALLBACK_NATIVE
#define CALLBACK_NATIVE(func) Java_org_eclipse_swt_internal_Callback_##func
#endif

/* define this to print out debug statements */
/* #define DEBUG_CALL_PRINTS */

/* --------------- callback globals ----------------- */

static CALLBACK_DATA callbackData[MAX_CALLBACKS];
static int callbackEnabled = 1;
static int callbackEntryCount = 0;
static int initialized = 0;
static jint JNI_VERSION = 0;

#ifdef DEBUG_CALL_PRINTS
	static int counter = 0;

	#if   defined(COCOA)
		#include <objc/runtime.h>
	#elif defined(GTK)
		#include <dlfcn.h>
		#include <gdk/gdk.h>
	#endif
#endif

#ifdef ATOMIC
#include <libkern/OSAtomic.h>
#define ATOMIC_INC(value) OSAtomicIncrement32(&value);
#define ATOMIC_DEC(value) OSAtomicDecrement32(&value);
#else
#define ATOMIC_INC(value) value++;
#define ATOMIC_DEC(value) value--;
#endif

jlong callback(int index, ...);

/* --------------- callback functions --------------- */


/* Function name from index and number of arguments */
#define FN(index, args) fn##index##_##args

/**
 * Functions templates
 *
 * NOTE: If the maximum number of arguments changes (MAX_ARGS), the number
 *       of function templates has to change accordingly.
 */

/* Function template with no arguments */
#define FN_0(index) jlong FN(index, 0)() { return callback(index); }

/* Function template with 1 argument */
#define FN_1(index) jlong FN(index, 1)(jlong p1) { return callback(index, p1); }

/* Function template with 2 arguments */
#define FN_2(index) jlong FN(index, 2)(jlong p1, jlong p2) { return callback(index, p1, p2); }

/* Function template with 3 arguments */
#define FN_3(index) jlong FN(index, 3)(jlong p1, jlong p2, jlong p3) { return callback(index, p1, p2, p3); }

/* Function template with 4 arguments */
#define FN_4(index) jlong FN(index, 4)(jlong p1, jlong p2, jlong p3, jlong p4) { return callback(index, p1, p2, p3, p4); }

/* Function template with 5 arguments */
#define FN_5(index) jlong FN(index, 5)(jlong p1, jlong p2, jlong p3, jlong p4, jlong p5) { return callback(index, p1, p2, p3, p4, p5); }

/* Function template with 6 arguments */
#define FN_6(index) jlong FN(index, 6)(jlong p1, jlong p2, jlong p3, jlong p4, jlong p5, jlong p6) { return callback(index, p1, p2, p3, p4, p5, p6); }

/* Function template with 7 arguments */
#define FN_7(index) jlong FN(index, 7)(jlong p1, jlong p2, jlong p3, jlong p4, jlong p5, jlong p6, jlong p7) { return callback(index, p1, p2, p3, p4, p5, p6, p7); }

/* Function template with 8 arguments */
#define FN_8(index) jlong FN(index, 8)(jlong p1, jlong p2, jlong p3, jlong p4, jlong p5, jlong p6, jlong p7, jlong p8) { return callback(index, p1, p2, p3, p4, p5, p6, p7, p8); }

/* Function template with 9 arguments */
#define FN_9(index) jlong FN(index, 9)(jlong p1, jlong p2, jlong p3, jlong p4, jlong p5, jlong p6, jlong p7, jlong p8, jlong p9) { return callback(index, p1, p2, p3, p4, p5, p6, p7, p8, p9); }

/* Function template with 10 arguments */
#define FN_10(index) jlong FN(index, 10) (jlong p1, jlong p2, jlong p3, jlong p4, jlong p5, jlong p6, jlong p7, jlong p8, jlong p9, jlong p10) { return callback(index, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10); }

/* Function template with 11 arguments */
#define FN_11(index) jlong FN(index, 11) (jlong p1, jlong p2, jlong p3, jlong p4, jlong p5, jlong p6, jlong p7, jlong p8, jlong p9, jlong p10, jlong p11) { return callback(index, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11); }

/* Function template with 12 arguments */
#define FN_12(index) jlong FN(index, 12) (jlong p1, jlong p2, jlong p3, jlong p4, jlong p5, jlong p6, jlong p7, jlong p8, jlong p9, jlong p10, jlong p11, jlong p12) { return callback(index, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12); }

#if defined(GTK)
#define FND(index, args) fnD##index##_##args

#define NUM_DOUBLE_CALLBACKS 2
#define FN_D_4(index) jlong FND(index, 4) (jlong p1, jdouble p2, jdouble p3, jlong p4) { return callback(index, p1, p2, p3, p4); }
#define FN_D_5(index) jlong FND(index, 5) (jlong p1, jint p2, jdouble p3, jdouble p4, jlong p5) { return callback(index, p1, p2, p3, p4, p5); }

#define FN_DOUBLE_BLOCK(args) \
	FN_D_##args(0) \
	FN_D_##args(1) \
	FN_D_##args(2) \
	FN_D_##args(3) \
	FN_D_##args(4) \
	FN_D_##args(5) \
	FN_D_##args(6) \
	FN_D_##args(7) \
	FN_D_##args(8) \
	FN_D_##args(9) \
	FN_D_##args(10) \
	FN_D_##args(11) \
	FN_D_##args(12) \
	FN_D_##args(13) \
	FN_D_##args(14) \
	FN_D_##args(15) \
	FN_D_##args(16) \
	FN_D_##args(17) \
	FN_D_##args(18) \
	FN_D_##args(19) \
	FN_D_##args(20) \
	FN_D_##args(21) \
	FN_D_##args(22) \
	FN_D_##args(23) \
	FN_D_##args(24) \
	FN_D_##args(25) \
	FN_D_##args(26) \
	FN_D_##args(27) \
	FN_D_##args(28) \
	FN_D_##args(29) \
	FN_D_##args(30) \
	FN_D_##args(31) \
	FN_D_##args(32) \
	FN_D_##args(33) \
	FN_D_##args(34) \
	FN_D_##args(35) \
	FN_D_##args(36) \
	FN_D_##args(37) \
	FN_D_##args(38) \
	FN_D_##args(39) \
	FN_D_##args(40) \
	FN_D_##args(41) \
	FN_D_##args(42) \
	FN_D_##args(43) \
	FN_D_##args(44) \
	FN_D_##args(45) \
	FN_D_##args(46) \
	FN_D_##args(47) \
	FN_D_##args(48) \
	FN_D_##args(49) \
	FN_D_##args(50) \
	FN_D_##args(51) \
	FN_D_##args(52) \
	FN_D_##args(53) \
	FN_D_##args(54) \
	FN_D_##args(55) \
	FN_D_##args(56) \
	FN_D_##args(57) \
	FN_D_##args(58) \
	FN_D_##args(59) \
	FN_D_##args(60) \
	FN_D_##args(61) \
	FN_D_##args(62) \
	FN_D_##args(63) \
	FN_D_##args(64) \
	FN_D_##args(65) \
	FN_D_##args(66) \
	FN_D_##args(67) \
	FN_D_##args(68) \
	FN_D_##args(69) \
	FN_D_##args(70) \
	FN_D_##args(71) \
	FN_D_##args(72) \
	FN_D_##args(73) \
	FN_D_##args(74) \
	FN_D_##args(75) \
	FN_D_##args(76) \
	FN_D_##args(77) \
	FN_D_##args(78) \
	FN_D_##args(79) \
	FN_D_##args(80) \
	FN_D_##args(81) \
	FN_D_##args(82) \
	FN_D_##args(83) \
	FN_D_##args(84) \
	FN_D_##args(85) \
	FN_D_##args(86) \
	FN_D_##args(87) \
	FN_D_##args(88) \
	FN_D_##args(89) \
	FN_D_##args(90) \
	FN_D_##args(91) \
	FN_D_##args(92) \
	FN_D_##args(93) \
	FN_D_##args(94) \
	FN_D_##args(95) \
	FN_D_##args(96) \
	FN_D_##args(97) \
	FN_D_##args(98) \
	FN_D_##args(99) \
	FN_D_##args(100) \
	FN_D_##args(101) \
	FN_D_##args(102) \
	FN_D_##args(103) \
	FN_D_##args(104) \
	FN_D_##args(105) \
	FN_D_##args(106) \
	FN_D_##args(107) \
	FN_D_##args(108) \
	FN_D_##args(109) \
	FN_D_##args(110) \
	FN_D_##args(111) \
	FN_D_##args(112) \
	FN_D_##args(113) \
	FN_D_##args(114) \
	FN_D_##args(115) \
	FN_D_##args(116) \
	FN_D_##args(117) \
	FN_D_##args(118) \
	FN_D_##args(119) \
	FN_D_##args(120) \
	FN_D_##args(121) \
	FN_D_##args(122) \
	FN_D_##args(123) \
	FN_D_##args(124) \
	FN_D_##args(125) \
	FN_D_##args(126) \
	FN_D_##args(127) \
	FN_D_##args(128) \
	FN_D_##args(129) \
	FN_D_##args(130) \
	FN_D_##args(131) \
	FN_D_##args(132) \
	FN_D_##args(133) \
	FN_D_##args(134) \
	FN_D_##args(135) \
	FN_D_##args(136) \
	FN_D_##args(137) \
	FN_D_##args(138) \
	FN_D_##args(139) \
	FN_D_##args(140) \
	FN_D_##args(141) \
	FN_D_##args(142) \
	FN_D_##args(143) \
	FN_D_##args(144) \
	FN_D_##args(145) \
	FN_D_##args(146) \
	FN_D_##args(147) \
	FN_D_##args(148) \
	FN_D_##args(149) \
	FN_D_##args(150) \
	FN_D_##args(151) \
	FN_D_##args(152) \
	FN_D_##args(153) \
	FN_D_##args(154) \
	FN_D_##args(155) \
	FN_D_##args(156) \
	FN_D_##args(157) \
	FN_D_##args(158) \
	FN_D_##args(159) \
	FN_D_##args(160) \
	FN_D_##args(161) \
	FN_D_##args(162) \
	FN_D_##args(163) \
	FN_D_##args(164) \
	FN_D_##args(165) \
	FN_D_##args(166) \
	FN_D_##args(167) \
	FN_D_##args(168) \
	FN_D_##args(169) \
	FN_D_##args(170) \
	FN_D_##args(171) \
	FN_D_##args(172) \
	FN_D_##args(173) \
	FN_D_##args(174) \
	FN_D_##args(175) \
	FN_D_##args(176) \
	FN_D_##args(177) \
	FN_D_##args(178) \
	FN_D_##args(179) \
	FN_D_##args(180) \
	FN_D_##args(181) \
	FN_D_##args(182) \
	FN_D_##args(183) \
	FN_D_##args(184) \
	FN_D_##args(185) \
	FN_D_##args(186) \
	FN_D_##args(187) \
	FN_D_##args(188) \
	FN_D_##args(189) \
	FN_D_##args(190) \
	FN_D_##args(191) \
	FN_D_##args(192) \
	FN_D_##args(193) \
	FN_D_##args(194) \
	FN_D_##args(195) \
	FN_D_##args(196) \
	FN_D_##args(197) \
	FN_D_##args(198) \
	FN_D_##args(199) \
	FN_D_##args(200) \
	FN_D_##args(201) \
	FN_D_##args(202) \
	FN_D_##args(203) \
	FN_D_##args(204) \
	FN_D_##args(205) \
	FN_D_##args(206) \
	FN_D_##args(207) \
	FN_D_##args(208) \
	FN_D_##args(209) \
	FN_D_##args(210) \
	FN_D_##args(211) \
	FN_D_##args(212) \
	FN_D_##args(213) \
	FN_D_##args(214) \
	FN_D_##args(215) \
	FN_D_##args(216) \
	FN_D_##args(217) \
	FN_D_##args(218) \
	FN_D_##args(219) \
	FN_D_##args(220) \
	FN_D_##args(221) \
	FN_D_##args(222) \
	FN_D_##args(223) \
	FN_D_##args(224) \
	FN_D_##args(225) \
	FN_D_##args(226) \
	FN_D_##args(227) \
	FN_D_##args(228) \
	FN_D_##args(229) \
	FN_D_##args(230) \
	FN_D_##args(231) \
	FN_D_##args(232) \
	FN_D_##args(233) \
	FN_D_##args(234) \
	FN_D_##args(235) \
	FN_D_##args(236) \
	FN_D_##args(237) \
	FN_D_##args(238) \
	FN_D_##args(239) \
	FN_D_##args(240) \
	FN_D_##args(241) \
	FN_D_##args(242) \
	FN_D_##args(243) \
	FN_D_##args(244) \
	FN_D_##args(245) \
	FN_D_##args(246) \
	FN_D_##args(247) \
	FN_D_##args(248) \
	FN_D_##args(249) \
	FN_D_##args(250) \
	FN_D_##args(251) \
	FN_D_##args(252) \
	FN_D_##args(253) \
	FN_D_##args(254) \
	FN_D_##args(255)

#define FN_DOUBLE_A_BLOCK(args) { \
	(jlong)FND(0, args), \
	(jlong)FND(1, args), \
	(jlong)FND(2, args), \
	(jlong)FND(3, args), \
	(jlong)FND(4, args), \
	(jlong)FND(5, args), \
	(jlong)FND(6, args), \
	(jlong)FND(7, args), \
	(jlong)FND(8, args), \
	(jlong)FND(9, args), \
	(jlong)FND(10, args), \
	(jlong)FND(11, args), \
	(jlong)FND(12, args), \
	(jlong)FND(13, args), \
	(jlong)FND(14, args), \
	(jlong)FND(15, args), \
	(jlong)FND(16, args), \
	(jlong)FND(17, args), \
	(jlong)FND(18, args), \
	(jlong)FND(19, args), \
	(jlong)FND(20, args), \
	(jlong)FND(21, args), \
	(jlong)FND(22, args), \
	(jlong)FND(23, args), \
	(jlong)FND(24, args), \
	(jlong)FND(25, args), \
	(jlong)FND(26, args), \
	(jlong)FND(27, args), \
	(jlong)FND(28, args), \
	(jlong)FND(29, args), \
	(jlong)FND(30, args), \
	(jlong)FND(31, args), \
	(jlong)FND(32, args), \
	(jlong)FND(33, args), \
	(jlong)FND(34, args), \
	(jlong)FND(35, args), \
	(jlong)FND(36, args), \
	(jlong)FND(37, args), \
	(jlong)FND(38, args), \
	(jlong)FND(39, args), \
	(jlong)FND(40, args), \
	(jlong)FND(41, args), \
	(jlong)FND(42, args), \
	(jlong)FND(43, args), \
	(jlong)FND(44, args), \
	(jlong)FND(45, args), \
	(jlong)FND(46, args), \
	(jlong)FND(47, args), \
	(jlong)FND(48, args), \
	(jlong)FND(49, args), \
	(jlong)FND(50, args), \
	(jlong)FND(51, args), \
	(jlong)FND(52, args), \
	(jlong)FND(53, args), \
	(jlong)FND(54, args), \
	(jlong)FND(55, args), \
	(jlong)FND(56, args), \
	(jlong)FND(57, args), \
	(jlong)FND(58, args), \
	(jlong)FND(59, args), \
	(jlong)FND(60, args), \
	(jlong)FND(61, args), \
	(jlong)FND(62, args), \
	(jlong)FND(63, args), \
	(jlong)FND(64, args), \
	(jlong)FND(65, args), \
	(jlong)FND(66, args), \
	(jlong)FND(67, args), \
	(jlong)FND(68, args), \
	(jlong)FND(69, args), \
	(jlong)FND(70, args), \
	(jlong)FND(71, args), \
	(jlong)FND(72, args), \
	(jlong)FND(73, args), \
	(jlong)FND(74, args), \
	(jlong)FND(75, args), \
	(jlong)FND(76, args), \
	(jlong)FND(77, args), \
	(jlong)FND(78, args), \
	(jlong)FND(79, args), \
	(jlong)FND(80, args), \
	(jlong)FND(81, args), \
	(jlong)FND(82, args), \
	(jlong)FND(83, args), \
	(jlong)FND(84, args), \
	(jlong)FND(85, args), \
	(jlong)FND(86, args), \
	(jlong)FND(87, args), \
	(jlong)FND(88, args), \
	(jlong)FND(89, args), \
	(jlong)FND(90, args), \
	(jlong)FND(91, args), \
	(jlong)FND(92, args), \
	(jlong)FND(93, args), \
	(jlong)FND(94, args), \
	(jlong)FND(95, args), \
	(jlong)FND(96, args), \
	(jlong)FND(97, args), \
	(jlong)FND(98, args), \
	(jlong)FND(99, args), \
	(jlong)FND(100, args), \
	(jlong)FND(101, args), \
	(jlong)FND(102, args), \
	(jlong)FND(103, args), \
	(jlong)FND(104, args), \
	(jlong)FND(105, args), \
	(jlong)FND(106, args), \
	(jlong)FND(107, args), \
	(jlong)FND(108, args), \
	(jlong)FND(109, args), \
	(jlong)FND(110, args), \
	(jlong)FND(111, args), \
	(jlong)FND(112, args), \
	(jlong)FND(113, args), \
	(jlong)FND(114, args), \
	(jlong)FND(115, args), \
	(jlong)FND(116, args), \
	(jlong)FND(117, args), \
	(jlong)FND(118, args), \
	(jlong)FND(119, args), \
	(jlong)FND(120, args), \
	(jlong)FND(121, args), \
	(jlong)FND(122, args), \
	(jlong)FND(123, args), \
	(jlong)FND(124, args), \
	(jlong)FND(125, args), \
	(jlong)FND(126, args), \
	(jlong)FND(127, args), \
	(jlong)FND(128, args), \
	(jlong)FND(129, args), \
	(jlong)FND(130, args), \
	(jlong)FND(131, args), \
	(jlong)FND(132, args), \
	(jlong)FND(133, args), \
	(jlong)FND(134, args), \
	(jlong)FND(135, args), \
	(jlong)FND(136, args), \
	(jlong)FND(137, args), \
	(jlong)FND(138, args), \
	(jlong)FND(139, args), \
	(jlong)FND(140, args), \
	(jlong)FND(141, args), \
	(jlong)FND(142, args), \
	(jlong)FND(143, args), \
	(jlong)FND(144, args), \
	(jlong)FND(145, args), \
	(jlong)FND(146, args), \
	(jlong)FND(147, args), \
	(jlong)FND(148, args), \
	(jlong)FND(149, args), \
	(jlong)FND(150, args), \
	(jlong)FND(151, args), \
	(jlong)FND(152, args), \
	(jlong)FND(153, args), \
	(jlong)FND(154, args), \
	(jlong)FND(155, args), \
	(jlong)FND(156, args), \
	(jlong)FND(157, args), \
	(jlong)FND(158, args), \
	(jlong)FND(159, args), \
	(jlong)FND(160, args), \
	(jlong)FND(161, args), \
	(jlong)FND(162, args), \
	(jlong)FND(163, args), \
	(jlong)FND(164, args), \
	(jlong)FND(165, args), \
	(jlong)FND(166, args), \
	(jlong)FND(167, args), \
	(jlong)FND(168, args), \
	(jlong)FND(169, args), \
	(jlong)FND(170, args), \
	(jlong)FND(171, args), \
	(jlong)FND(172, args), \
	(jlong)FND(173, args), \
	(jlong)FND(174, args), \
	(jlong)FND(175, args), \
	(jlong)FND(176, args), \
	(jlong)FND(177, args), \
	(jlong)FND(178, args), \
	(jlong)FND(179, args), \
	(jlong)FND(180, args), \
	(jlong)FND(181, args), \
	(jlong)FND(182, args), \
	(jlong)FND(183, args), \
	(jlong)FND(184, args), \
	(jlong)FND(185, args), \
	(jlong)FND(186, args), \
	(jlong)FND(187, args), \
	(jlong)FND(188, args), \
	(jlong)FND(189, args), \
	(jlong)FND(190, args), \
	(jlong)FND(191, args), \
	(jlong)FND(192, args), \
	(jlong)FND(193, args), \
	(jlong)FND(194, args), \
	(jlong)FND(195, args), \
	(jlong)FND(196, args), \
	(jlong)FND(197, args), \
	(jlong)FND(198, args), \
	(jlong)FND(199, args), \
	(jlong)FND(200, args), \
	(jlong)FND(201, args), \
	(jlong)FND(202, args), \
	(jlong)FND(203, args), \
	(jlong)FND(204, args), \
	(jlong)FND(205, args), \
	(jlong)FND(206, args), \
	(jlong)FND(207, args), \
	(jlong)FND(208, args), \
	(jlong)FND(209, args), \
	(jlong)FND(210, args), \
	(jlong)FND(211, args), \
	(jlong)FND(212, args), \
	(jlong)FND(213, args), \
	(jlong)FND(214, args), \
	(jlong)FND(215, args), \
	(jlong)FND(216, args), \
	(jlong)FND(217, args), \
	(jlong)FND(218, args), \
	(jlong)FND(219, args), \
	(jlong)FND(220, args), \
	(jlong)FND(221, args), \
	(jlong)FND(222, args), \
	(jlong)FND(223, args), \
	(jlong)FND(224, args), \
	(jlong)FND(225, args), \
	(jlong)FND(226, args), \
	(jlong)FND(227, args), \
	(jlong)FND(228, args), \
	(jlong)FND(229, args), \
	(jlong)FND(230, args), \
	(jlong)FND(231, args), \
	(jlong)FND(232, args), \
	(jlong)FND(233, args), \
	(jlong)FND(234, args), \
	(jlong)FND(235, args), \
	(jlong)FND(236, args), \
	(jlong)FND(237, args), \
	(jlong)FND(238, args), \
	(jlong)FND(239, args), \
	(jlong)FND(240, args), \
	(jlong)FND(241, args), \
	(jlong)FND(242, args), \
	(jlong)FND(243, args), \
	(jlong)FND(244, args), \
	(jlong)FND(245, args), \
	(jlong)FND(246, args), \
	(jlong)FND(247, args), \
	(jlong)FND(248, args), \
	(jlong)FND(249, args), \
	(jlong)FND(250, args), \
	(jlong)FND(251, args), \
	(jlong)FND(252, args), \
	(jlong)FND(253, args), \
	(jlong)FND(254, args), \
	(jlong)FND(255, args), \
},

#else
#define NUM_DOUBLE_CALLBACKS 0
#endif

/**
 * Define all functions with the specified number of arguments.
 *
 * NOTE: If the maximum number of callbacks changes (MAX_CALLBACKS),
 *       this macro has to be updated. 
 */
#if MAX_CALLBACKS == 16
#define FN_BLOCK(args) \
	FN_##args(0) \
	FN_##args(1) \
	FN_##args(2) \
	FN_##args(3) \
	FN_##args(4) \
	FN_##args(5) \
	FN_##args(6) \
	FN_##args(7) \
	FN_##args(8) \
	FN_##args(9) \
	FN_##args(10) \
	FN_##args(11) \
	FN_##args(12) \
	FN_##args(13) \
	FN_##args(14) \
	FN_##args(15)
#elif MAX_CALLBACKS == 128
#define FN_BLOCK(args) \
	FN_##args(0) \
	FN_##args(1) \
	FN_##args(2) \
	FN_##args(3) \
	FN_##args(4) \
	FN_##args(5) \
	FN_##args(6) \
	FN_##args(7) \
	FN_##args(8) \
	FN_##args(9) \
	FN_##args(10) \
	FN_##args(11) \
	FN_##args(12) \
	FN_##args(13) \
	FN_##args(14) \
	FN_##args(15) \
	FN_##args(16) \
	FN_##args(17) \
	FN_##args(18) \
	FN_##args(19) \
	FN_##args(20) \
	FN_##args(21) \
	FN_##args(22) \
	FN_##args(23) \
	FN_##args(24) \
	FN_##args(25) \
	FN_##args(26) \
	FN_##args(27) \
	FN_##args(28) \
	FN_##args(29) \
	FN_##args(30) \
	FN_##args(31) \
	FN_##args(32) \
	FN_##args(33) \
	FN_##args(34) \
	FN_##args(35) \
	FN_##args(36) \
	FN_##args(37) \
	FN_##args(38) \
	FN_##args(39) \
	FN_##args(40) \
	FN_##args(41) \
	FN_##args(42) \
	FN_##args(43) \
	FN_##args(44) \
	FN_##args(45) \
	FN_##args(46) \
	FN_##args(47) \
	FN_##args(48) \
	FN_##args(49) \
	FN_##args(50) \
	FN_##args(51) \
	FN_##args(52) \
	FN_##args(53) \
	FN_##args(54) \
	FN_##args(55) \
	FN_##args(56) \
	FN_##args(57) \
	FN_##args(58) \
	FN_##args(59) \
	FN_##args(60) \
	FN_##args(61) \
	FN_##args(62) \
	FN_##args(63) \
	FN_##args(64) \
	FN_##args(65) \
	FN_##args(66) \
	FN_##args(67) \
	FN_##args(68) \
	FN_##args(69) \
	FN_##args(70) \
	FN_##args(71) \
	FN_##args(72) \
	FN_##args(73) \
	FN_##args(74) \
	FN_##args(75) \
	FN_##args(76) \
	FN_##args(77) \
	FN_##args(78) \
	FN_##args(79) \
	FN_##args(80) \
	FN_##args(81) \
	FN_##args(82) \
	FN_##args(83) \
	FN_##args(84) \
	FN_##args(85) \
	FN_##args(86) \
	FN_##args(87) \
	FN_##args(88) \
	FN_##args(89) \
	FN_##args(90) \
	FN_##args(91) \
	FN_##args(92) \
	FN_##args(93) \
	FN_##args(94) \
	FN_##args(95) \
	FN_##args(96) \
	FN_##args(97) \
	FN_##args(98) \
	FN_##args(99) \
	FN_##args(100) \
	FN_##args(101) \
	FN_##args(102) \
	FN_##args(103) \
	FN_##args(104) \
	FN_##args(105) \
	FN_##args(106) \
	FN_##args(107) \
	FN_##args(108) \
	FN_##args(109) \
	FN_##args(110) \
	FN_##args(111) \
	FN_##args(112) \
	FN_##args(113) \
	FN_##args(114) \
	FN_##args(115) \
	FN_##args(116) \
	FN_##args(117) \
	FN_##args(118) \
	FN_##args(119) \
	FN_##args(120) \
	FN_##args(121) \
	FN_##args(122) \
	FN_##args(123) \
	FN_##args(124) \
	FN_##args(125) \
	FN_##args(126) \
	FN_##args(127)
#elif MAX_CALLBACKS == 256
#define FN_BLOCK(args) \
	FN_##args(0) \
	FN_##args(1) \
	FN_##args(2) \
	FN_##args(3) \
	FN_##args(4) \
	FN_##args(5) \
	FN_##args(6) \
	FN_##args(7) \
	FN_##args(8) \
	FN_##args(9) \
	FN_##args(10) \
	FN_##args(11) \
	FN_##args(12) \
	FN_##args(13) \
	FN_##args(14) \
	FN_##args(15) \
	FN_##args(16) \
	FN_##args(17) \
	FN_##args(18) \
	FN_##args(19) \
	FN_##args(20) \
	FN_##args(21) \
	FN_##args(22) \
	FN_##args(23) \
	FN_##args(24) \
	FN_##args(25) \
	FN_##args(26) \
	FN_##args(27) \
	FN_##args(28) \
	FN_##args(29) \
	FN_##args(30) \
	FN_##args(31) \
	FN_##args(32) \
	FN_##args(33) \
	FN_##args(34) \
	FN_##args(35) \
	FN_##args(36) \
	FN_##args(37) \
	FN_##args(38) \
	FN_##args(39) \
	FN_##args(40) \
	FN_##args(41) \
	FN_##args(42) \
	FN_##args(43) \
	FN_##args(44) \
	FN_##args(45) \
	FN_##args(46) \
	FN_##args(47) \
	FN_##args(48) \
	FN_##args(49) \
	FN_##args(50) \
	FN_##args(51) \
	FN_##args(52) \
	FN_##args(53) \
	FN_##args(54) \
	FN_##args(55) \
	FN_##args(56) \
	FN_##args(57) \
	FN_##args(58) \
	FN_##args(59) \
	FN_##args(60) \
	FN_##args(61) \
	FN_##args(62) \
	FN_##args(63) \
	FN_##args(64) \
	FN_##args(65) \
	FN_##args(66) \
	FN_##args(67) \
	FN_##args(68) \
	FN_##args(69) \
	FN_##args(70) \
	FN_##args(71) \
	FN_##args(72) \
	FN_##args(73) \
	FN_##args(74) \
	FN_##args(75) \
	FN_##args(76) \
	FN_##args(77) \
	FN_##args(78) \
	FN_##args(79) \
	FN_##args(80) \
	FN_##args(81) \
	FN_##args(82) \
	FN_##args(83) \
	FN_##args(84) \
	FN_##args(85) \
	FN_##args(86) \
	FN_##args(87) \
	FN_##args(88) \
	FN_##args(89) \
	FN_##args(90) \
	FN_##args(91) \
	FN_##args(92) \
	FN_##args(93) \
	FN_##args(94) \
	FN_##args(95) \
	FN_##args(96) \
	FN_##args(97) \
	FN_##args(98) \
	FN_##args(99) \
	FN_##args(100) \
	FN_##args(101) \
	FN_##args(102) \
	FN_##args(103) \
	FN_##args(104) \
	FN_##args(105) \
	FN_##args(106) \
	FN_##args(107) \
	FN_##args(108) \
	FN_##args(109) \
	FN_##args(110) \
	FN_##args(111) \
	FN_##args(112) \
	FN_##args(113) \
	FN_##args(114) \
	FN_##args(115) \
	FN_##args(116) \
	FN_##args(117) \
	FN_##args(118) \
	FN_##args(119) \
	FN_##args(120) \
	FN_##args(121) \
	FN_##args(122) \
	FN_##args(123) \
	FN_##args(124) \
	FN_##args(125) \
	FN_##args(126) \
	FN_##args(127) \
	FN_##args(128) \
	FN_##args(129) \
	FN_##args(130) \
	FN_##args(131) \
	FN_##args(132) \
	FN_##args(133) \
	FN_##args(134) \
	FN_##args(135) \
	FN_##args(136) \
	FN_##args(137) \
	FN_##args(138) \
	FN_##args(139) \
	FN_##args(140) \
	FN_##args(141) \
	FN_##args(142) \
	FN_##args(143) \
	FN_##args(144) \
	FN_##args(145) \
	FN_##args(146) \
	FN_##args(147) \
	FN_##args(148) \
	FN_##args(149) \
	FN_##args(150) \
	FN_##args(151) \
	FN_##args(152) \
	FN_##args(153) \
	FN_##args(154) \
	FN_##args(155) \
	FN_##args(156) \
	FN_##args(157) \
	FN_##args(158) \
	FN_##args(159) \
	FN_##args(160) \
	FN_##args(161) \
	FN_##args(162) \
	FN_##args(163) \
	FN_##args(164) \
	FN_##args(165) \
	FN_##args(166) \
	FN_##args(167) \
	FN_##args(168) \
	FN_##args(169) \
	FN_##args(170) \
	FN_##args(171) \
	FN_##args(172) \
	FN_##args(173) \
	FN_##args(174) \
	FN_##args(175) \
	FN_##args(176) \
	FN_##args(177) \
	FN_##args(178) \
	FN_##args(179) \
	FN_##args(180) \
	FN_##args(181) \
	FN_##args(182) \
	FN_##args(183) \
	FN_##args(184) \
	FN_##args(185) \
	FN_##args(186) \
	FN_##args(187) \
	FN_##args(188) \
	FN_##args(189) \
	FN_##args(190) \
	FN_##args(191) \
	FN_##args(192) \
	FN_##args(193) \
	FN_##args(194) \
	FN_##args(195) \
	FN_##args(196) \
	FN_##args(197) \
	FN_##args(198) \
	FN_##args(199) \
	FN_##args(200) \
	FN_##args(201) \
	FN_##args(202) \
	FN_##args(203) \
	FN_##args(204) \
	FN_##args(205) \
	FN_##args(206) \
	FN_##args(207) \
	FN_##args(208) \
	FN_##args(209) \
	FN_##args(210) \
	FN_##args(211) \
	FN_##args(212) \
	FN_##args(213) \
	FN_##args(214) \
	FN_##args(215) \
	FN_##args(216) \
	FN_##args(217) \
	FN_##args(218) \
	FN_##args(219) \
	FN_##args(220) \
	FN_##args(221) \
	FN_##args(222) \
	FN_##args(223) \
	FN_##args(224) \
	FN_##args(225) \
	FN_##args(226) \
	FN_##args(227) \
	FN_##args(228) \
	FN_##args(229) \
	FN_##args(230) \
	FN_##args(231) \
	FN_##args(232) \
	FN_##args(233) \
	FN_##args(234) \
	FN_##args(235) \
	FN_##args(236) \
	FN_##args(237) \
	FN_##args(238) \
	FN_##args(239) \
	FN_##args(240) \
	FN_##args(241) \
	FN_##args(242) \
	FN_##args(243) \
	FN_##args(244) \
	FN_##args(245) \
	FN_##args(246) \
	FN_##args(247) \
	FN_##args(248) \
	FN_##args(249) \
	FN_##args(250) \
	FN_##args(251) \
	FN_##args(252) \
	FN_##args(253) \
	FN_##args(254) \
	FN_##args(255)
#else
#error Invalid MAX_CALLBACKS
#endif /* MAX_CALLBACKS == 16 */

/**
 * Define all callback functions.
 *
 * NOTE: If the maximum number of arguments changes (MAX_ARGS), the following
 *       has to change accordinglly.
 */
FN_BLOCK(0)
FN_BLOCK(1)
FN_BLOCK(2)
FN_BLOCK(3)
FN_BLOCK(4)
FN_BLOCK(5)
FN_BLOCK(6)
FN_BLOCK(7)
FN_BLOCK(8)
FN_BLOCK(9)
FN_BLOCK(10)
FN_BLOCK(11)
FN_BLOCK(12)
#if defined(GTK)
FN_DOUBLE_BLOCK(4) /* JDDJ */
FN_DOUBLE_BLOCK(5) /* JJDDJ */
#endif

/**
 * Initialize the function pointers for the callback routines.
 *
 * NOTE: If MAX_ARGS or MAX_CALLBACKS changes, the following has to be updated.
 */
#if MAX_CALLBACKS == 16
#define FN_A_BLOCK(args) { \
	(jlong)FN(0, args), \
	(jlong)FN(1, args), \
	(jlong)FN(2, args), \
	(jlong)FN(3, args), \
	(jlong)FN(4, args), \
	(jlong)FN(5, args), \
	(jlong)FN(6, args), \
	(jlong)FN(7, args), \
	(jlong)FN(8, args), \
	(jlong)FN(9, args), \
	(jlong)FN(10, args), \
	(jlong)FN(11, args), \
	(jlong)FN(12, args), \
	(jlong)FN(13, args), \
	(jlong)FN(14, args), \
	(jlong)FN(15, args), \
},
#elif MAX_CALLBACKS == 128
#define FN_A_BLOCK(args) { \
	(jlong)FN(0, args), \
	(jlong)FN(1, args), \
	(jlong)FN(2, args), \
	(jlong)FN(3, args), \
	(jlong)FN(4, args), \
	(jlong)FN(5, args), \
	(jlong)FN(6, args), \
	(jlong)FN(7, args), \
	(jlong)FN(8, args), \
	(jlong)FN(9, args), \
	(jlong)FN(10, args), \
	(jlong)FN(11, args), \
	(jlong)FN(12, args), \
	(jlong)FN(13, args), \
	(jlong)FN(14, args), \
	(jlong)FN(15, args), \
	(jlong)FN(16, args), \
	(jlong)FN(17, args), \
	(jlong)FN(18, args), \
	(jlong)FN(19, args), \
	(jlong)FN(20, args), \
	(jlong)FN(21, args), \
	(jlong)FN(22, args), \
	(jlong)FN(23, args), \
	(jlong)FN(24, args), \
	(jlong)FN(25, args), \
	(jlong)FN(26, args), \
	(jlong)FN(27, args), \
	(jlong)FN(28, args), \
	(jlong)FN(29, args), \
	(jlong)FN(30, args), \
	(jlong)FN(31, args), \
	(jlong)FN(32, args), \
	(jlong)FN(33, args), \
	(jlong)FN(34, args), \
	(jlong)FN(35, args), \
	(jlong)FN(36, args), \
	(jlong)FN(37, args), \
	(jlong)FN(38, args), \
	(jlong)FN(39, args), \
	(jlong)FN(40, args), \
	(jlong)FN(41, args), \
	(jlong)FN(42, args), \
	(jlong)FN(43, args), \
	(jlong)FN(44, args), \
	(jlong)FN(45, args), \
	(jlong)FN(46, args), \
	(jlong)FN(47, args), \
	(jlong)FN(48, args), \
	(jlong)FN(49, args), \
	(jlong)FN(50, args), \
	(jlong)FN(51, args), \
	(jlong)FN(52, args), \
	(jlong)FN(53, args), \
	(jlong)FN(54, args), \
	(jlong)FN(55, args), \
	(jlong)FN(56, args), \
	(jlong)FN(57, args), \
	(jlong)FN(58, args), \
	(jlong)FN(59, args), \
	(jlong)FN(60, args), \
	(jlong)FN(61, args), \
	(jlong)FN(62, args), \
	(jlong)FN(63, args), \
	(jlong)FN(64, args), \
	(jlong)FN(65, args), \
	(jlong)FN(66, args), \
	(jlong)FN(67, args), \
	(jlong)FN(68, args), \
	(jlong)FN(69, args), \
	(jlong)FN(70, args), \
	(jlong)FN(71, args), \
	(jlong)FN(72, args), \
	(jlong)FN(73, args), \
	(jlong)FN(74, args), \
	(jlong)FN(75, args), \
	(jlong)FN(76, args), \
	(jlong)FN(77, args), \
	(jlong)FN(78, args), \
	(jlong)FN(79, args), \
	(jlong)FN(80, args), \
	(jlong)FN(81, args), \
	(jlong)FN(82, args), \
	(jlong)FN(83, args), \
	(jlong)FN(84, args), \
	(jlong)FN(85, args), \
	(jlong)FN(86, args), \
	(jlong)FN(87, args), \
	(jlong)FN(88, args), \
	(jlong)FN(89, args), \
	(jlong)FN(90, args), \
	(jlong)FN(91, args), \
	(jlong)FN(92, args), \
	(jlong)FN(93, args), \
	(jlong)FN(94, args), \
	(jlong)FN(95, args), \
	(jlong)FN(96, args), \
	(jlong)FN(97, args), \
	(jlong)FN(98, args), \
	(jlong)FN(99, args), \
	(jlong)FN(100, args), \
	(jlong)FN(101, args), \
	(jlong)FN(102, args), \
	(jlong)FN(103, args), \
	(jlong)FN(104, args), \
	(jlong)FN(105, args), \
	(jlong)FN(106, args), \
	(jlong)FN(107, args), \
	(jlong)FN(108, args), \
	(jlong)FN(109, args), \
	(jlong)FN(110, args), \
	(jlong)FN(111, args), \
	(jlong)FN(112, args), \
	(jlong)FN(113, args), \
	(jlong)FN(114, args), \
	(jlong)FN(115, args), \
	(jlong)FN(116, args), \
	(jlong)FN(117, args), \
	(jlong)FN(118, args), \
	(jlong)FN(119, args), \
	(jlong)FN(120, args), \
	(jlong)FN(121, args), \
	(jlong)FN(122, args), \
	(jlong)FN(123, args), \
	(jlong)FN(124, args), \
	(jlong)FN(125, args), \
	(jlong)FN(126, args), \
	(jlong)FN(127, args), \
},
#elif MAX_CALLBACKS == 256
#define FN_A_BLOCK(args) { \
	(jlong)FN(0, args), \
	(jlong)FN(1, args), \
	(jlong)FN(2, args), \
	(jlong)FN(3, args), \
	(jlong)FN(4, args), \
	(jlong)FN(5, args), \
	(jlong)FN(6, args), \
	(jlong)FN(7, args), \
	(jlong)FN(8, args), \
	(jlong)FN(9, args), \
	(jlong)FN(10, args), \
	(jlong)FN(11, args), \
	(jlong)FN(12, args), \
	(jlong)FN(13, args), \
	(jlong)FN(14, args), \
	(jlong)FN(15, args), \
	(jlong)FN(16, args), \
	(jlong)FN(17, args), \
	(jlong)FN(18, args), \
	(jlong)FN(19, args), \
	(jlong)FN(20, args), \
	(jlong)FN(21, args), \
	(jlong)FN(22, args), \
	(jlong)FN(23, args), \
	(jlong)FN(24, args), \
	(jlong)FN(25, args), \
	(jlong)FN(26, args), \
	(jlong)FN(27, args), \
	(jlong)FN(28, args), \
	(jlong)FN(29, args), \
	(jlong)FN(30, args), \
	(jlong)FN(31, args), \
	(jlong)FN(32, args), \
	(jlong)FN(33, args), \
	(jlong)FN(34, args), \
	(jlong)FN(35, args), \
	(jlong)FN(36, args), \
	(jlong)FN(37, args), \
	(jlong)FN(38, args), \
	(jlong)FN(39, args), \
	(jlong)FN(40, args), \
	(jlong)FN(41, args), \
	(jlong)FN(42, args), \
	(jlong)FN(43, args), \
	(jlong)FN(44, args), \
	(jlong)FN(45, args), \
	(jlong)FN(46, args), \
	(jlong)FN(47, args), \
	(jlong)FN(48, args), \
	(jlong)FN(49, args), \
	(jlong)FN(50, args), \
	(jlong)FN(51, args), \
	(jlong)FN(52, args), \
	(jlong)FN(53, args), \
	(jlong)FN(54, args), \
	(jlong)FN(55, args), \
	(jlong)FN(56, args), \
	(jlong)FN(57, args), \
	(jlong)FN(58, args), \
	(jlong)FN(59, args), \
	(jlong)FN(60, args), \
	(jlong)FN(61, args), \
	(jlong)FN(62, args), \
	(jlong)FN(63, args), \
	(jlong)FN(64, args), \
	(jlong)FN(65, args), \
	(jlong)FN(66, args), \
	(jlong)FN(67, args), \
	(jlong)FN(68, args), \
	(jlong)FN(69, args), \
	(jlong)FN(70, args), \
	(jlong)FN(71, args), \
	(jlong)FN(72, args), \
	(jlong)FN(73, args), \
	(jlong)FN(74, args), \
	(jlong)FN(75, args), \
	(jlong)FN(76, args), \
	(jlong)FN(77, args), \
	(jlong)FN(78, args), \
	(jlong)FN(79, args), \
	(jlong)FN(80, args), \
	(jlong)FN(81, args), \
	(jlong)FN(82, args), \
	(jlong)FN(83, args), \
	(jlong)FN(84, args), \
	(jlong)FN(85, args), \
	(jlong)FN(86, args), \
	(jlong)FN(87, args), \
	(jlong)FN(88, args), \
	(jlong)FN(89, args), \
	(jlong)FN(90, args), \
	(jlong)FN(91, args), \
	(jlong)FN(92, args), \
	(jlong)FN(93, args), \
	(jlong)FN(94, args), \
	(jlong)FN(95, args), \
	(jlong)FN(96, args), \
	(jlong)FN(97, args), \
	(jlong)FN(98, args), \
	(jlong)FN(99, args), \
	(jlong)FN(100, args), \
	(jlong)FN(101, args), \
	(jlong)FN(102, args), \
	(jlong)FN(103, args), \
	(jlong)FN(104, args), \
	(jlong)FN(105, args), \
	(jlong)FN(106, args), \
	(jlong)FN(107, args), \
	(jlong)FN(108, args), \
	(jlong)FN(109, args), \
	(jlong)FN(110, args), \
	(jlong)FN(111, args), \
	(jlong)FN(112, args), \
	(jlong)FN(113, args), \
	(jlong)FN(114, args), \
	(jlong)FN(115, args), \
	(jlong)FN(116, args), \
	(jlong)FN(117, args), \
	(jlong)FN(118, args), \
	(jlong)FN(119, args), \
	(jlong)FN(120, args), \
	(jlong)FN(121, args), \
	(jlong)FN(122, args), \
	(jlong)FN(123, args), \
	(jlong)FN(124, args), \
	(jlong)FN(125, args), \
	(jlong)FN(126, args), \
	(jlong)FN(127, args), \
	(jlong)FN(128, args), \
	(jlong)FN(129, args), \
	(jlong)FN(130, args), \
	(jlong)FN(131, args), \
	(jlong)FN(132, args), \
	(jlong)FN(133, args), \
	(jlong)FN(134, args), \
	(jlong)FN(135, args), \
	(jlong)FN(136, args), \
	(jlong)FN(137, args), \
	(jlong)FN(138, args), \
	(jlong)FN(139, args), \
	(jlong)FN(140, args), \
	(jlong)FN(141, args), \
	(jlong)FN(142, args), \
	(jlong)FN(143, args), \
	(jlong)FN(144, args), \
	(jlong)FN(145, args), \
	(jlong)FN(146, args), \
	(jlong)FN(147, args), \
	(jlong)FN(148, args), \
	(jlong)FN(149, args), \
	(jlong)FN(150, args), \
	(jlong)FN(151, args), \
	(jlong)FN(152, args), \
	(jlong)FN(153, args), \
	(jlong)FN(154, args), \
	(jlong)FN(155, args), \
	(jlong)FN(156, args), \
	(jlong)FN(157, args), \
	(jlong)FN(158, args), \
	(jlong)FN(159, args), \
	(jlong)FN(160, args), \
	(jlong)FN(161, args), \
	(jlong)FN(162, args), \
	(jlong)FN(163, args), \
	(jlong)FN(164, args), \
	(jlong)FN(165, args), \
	(jlong)FN(166, args), \
	(jlong)FN(167, args), \
	(jlong)FN(168, args), \
	(jlong)FN(169, args), \
	(jlong)FN(170, args), \
	(jlong)FN(171, args), \
	(jlong)FN(172, args), \
	(jlong)FN(173, args), \
	(jlong)FN(174, args), \
	(jlong)FN(175, args), \
	(jlong)FN(176, args), \
	(jlong)FN(177, args), \
	(jlong)FN(178, args), \
	(jlong)FN(179, args), \
	(jlong)FN(180, args), \
	(jlong)FN(181, args), \
	(jlong)FN(182, args), \
	(jlong)FN(183, args), \
	(jlong)FN(184, args), \
	(jlong)FN(185, args), \
	(jlong)FN(186, args), \
	(jlong)FN(187, args), \
	(jlong)FN(188, args), \
	(jlong)FN(189, args), \
	(jlong)FN(190, args), \
	(jlong)FN(191, args), \
	(jlong)FN(192, args), \
	(jlong)FN(193, args), \
	(jlong)FN(194, args), \
	(jlong)FN(195, args), \
	(jlong)FN(196, args), \
	(jlong)FN(197, args), \
	(jlong)FN(198, args), \
	(jlong)FN(199, args), \
	(jlong)FN(200, args), \
	(jlong)FN(201, args), \
	(jlong)FN(202, args), \
	(jlong)FN(203, args), \
	(jlong)FN(204, args), \
	(jlong)FN(205, args), \
	(jlong)FN(206, args), \
	(jlong)FN(207, args), \
	(jlong)FN(208, args), \
	(jlong)FN(209, args), \
	(jlong)FN(210, args), \
	(jlong)FN(211, args), \
	(jlong)FN(212, args), \
	(jlong)FN(213, args), \
	(jlong)FN(214, args), \
	(jlong)FN(215, args), \
	(jlong)FN(216, args), \
	(jlong)FN(217, args), \
	(jlong)FN(218, args), \
	(jlong)FN(219, args), \
	(jlong)FN(220, args), \
	(jlong)FN(221, args), \
	(jlong)FN(222, args), \
	(jlong)FN(223, args), \
	(jlong)FN(224, args), \
	(jlong)FN(225, args), \
	(jlong)FN(226, args), \
	(jlong)FN(227, args), \
	(jlong)FN(228, args), \
	(jlong)FN(229, args), \
	(jlong)FN(230, args), \
	(jlong)FN(231, args), \
	(jlong)FN(232, args), \
	(jlong)FN(233, args), \
	(jlong)FN(234, args), \
	(jlong)FN(235, args), \
	(jlong)FN(236, args), \
	(jlong)FN(237, args), \
	(jlong)FN(238, args), \
	(jlong)FN(239, args), \
	(jlong)FN(240, args), \
	(jlong)FN(241, args), \
	(jlong)FN(242, args), \
	(jlong)FN(243, args), \
	(jlong)FN(244, args), \
	(jlong)FN(245, args), \
	(jlong)FN(246, args), \
	(jlong)FN(247, args), \
	(jlong)FN(248, args), \
	(jlong)FN(249, args), \
	(jlong)FN(250, args), \
	(jlong)FN(251, args), \
	(jlong)FN(252, args), \
	(jlong)FN(253, args), \
	(jlong)FN(254, args), \
	(jlong)FN(255, args), \
},
#else
#error Invalid MAX_CALLBACKS
#endif /* MAX_CALLBACKS == 16 */

jlong fnx_array[MAX_ARGS+1+NUM_DOUBLE_CALLBACKS][MAX_CALLBACKS] = {
	FN_A_BLOCK(0)    
	FN_A_BLOCK(1)    
	FN_A_BLOCK(2)    
	FN_A_BLOCK(3)    
	FN_A_BLOCK(4)    
	FN_A_BLOCK(5)    
	FN_A_BLOCK(6)    
	FN_A_BLOCK(7)    
	FN_A_BLOCK(8)    
	FN_A_BLOCK(9)    
	FN_A_BLOCK(10)    
	FN_A_BLOCK(11)    
	FN_A_BLOCK(12)
	#if defined(GTK)
	FN_DOUBLE_A_BLOCK(4)
	FN_DOUBLE_A_BLOCK(5)
	#endif
};

/* --------------- callback class calls --------------- */

JNIEXPORT jlong JNICALL CALLBACK_NATIVE(bind)
  (JNIEnv *env, jclass that, jobject callbackObject, jobject object, jstring method, jstring signature, jint argCount, jboolean isStatic, jboolean isArrayBased, jlong errorResult)
{
	int i;
	jmethodID mid = NULL;
	jclass javaClass = that;
	const char *methodString = NULL, *sigString = NULL;
	jlong result = 0;
	if (JNI_VERSION == 0) JNI_VERSION = (*env)->GetVersion(env);
	if (!initialized) {
		memset(&callbackData, 0, sizeof(callbackData));
		initialized = 1;
	}
	if (method) methodString = (const char *) (*env)->GetStringUTFChars(env, method, NULL);
	if (signature) sigString = (const char *) (*env)->GetStringUTFChars(env, signature, NULL);
	if (object && methodString && sigString) {
		if (isStatic) {
			mid = (*env)->GetStaticMethodID(env, object, methodString, sigString);
		} else {
			javaClass = (*env)->GetObjectClass(env, object);    
			mid = (*env)->GetMethodID(env, javaClass, methodString, sigString);
		}
	}
	if (mid == 0) goto fail;
	for (i=0; i<MAX_CALLBACKS; i++) {
		if (!callbackData[i].callback) {
			if ((callbackData[i].callback = (*env)->NewGlobalRef(env, callbackObject)) == NULL) goto fail;
			if ((callbackData[i].object = (*env)->NewGlobalRef(env, object)) == NULL) goto fail;
			callbackData[i].isStatic = isStatic;
			callbackData[i].isArrayBased = isArrayBased;
			callbackData[i].argCount = argCount;
			callbackData[i].errorResult = errorResult;
			callbackData[i].methodID = mid;

			#ifdef DEBUG_CALL_PRINTS
				#if defined(COCOA)
					callbackData[i].arg_Selector = -1;

					if (!strcmp(methodString, "applicationProc") ||
						!strcmp(methodString, "dragSourceProc") ||
						!strcmp(methodString, "windowProc") ||
						!strcmp(methodString, "dialogProc"))
					{
						callbackData[i].arg_Selector = 1;
					}
				#elif defined(GTK)
					callbackData[i].arg_GObject = -1;
					callbackData[i].arg_GdkEvent = -1;
					callbackData[i].arg_SwtSignalID = -1;

					if (!strcmp(methodString, "windowProc")) {
						callbackData[i].arg_GObject = 0;
						callbackData[i].arg_SwtSignalID = argCount - 1;
					}

					if (!strcmp(methodString, "eventProc")) {
						callbackData[i].arg_GdkEvent = 0;
					}
				#endif

				fprintf(stderr, "SWT-JNI: Registered callback[%02d] = %s%s\n", i, methodString, sigString);
				fflush(stderr);
			#endif

			#if defined(GTK)
				if (strcmp(strtok((char *)sigString, ")"), "(JDDJ") == 0) {
					result = (jlong) fnx_array[MAX_ARGS + 1][i];
				} else if (strcmp(strtok((char *)sigString, ")"), "(JIDDJ") == 0) {
					result = (jlong) fnx_array[MAX_ARGS + 2][i];
				} else {
					result = (jlong) fnx_array[argCount][i];
				}
			#else
				result = (jlong) fnx_array[argCount][i];
			#endif
			break;
		}
	}

fail:
	if (method && methodString) (*env)->ReleaseStringUTFChars(env, method, methodString);
	if (signature && sigString) (*env)->ReleaseStringUTFChars(env, signature, sigString);
	return result;
}

JNIEXPORT void JNICALL CALLBACK_NATIVE(unbind)
  (JNIEnv *env, jclass that, jobject callback)
{
	int i;
	for (i=0; i<MAX_CALLBACKS; i++) {
		if (callbackData[i].callback != NULL && (*env)->IsSameObject(env, callback, callbackData[i].callback)) {
			if (callbackData[i].callback != NULL) (*env)->DeleteGlobalRef(env, callbackData[i].callback);
			if (callbackData[i].object != NULL) (*env)->DeleteGlobalRef(env, callbackData[i].object);
			memset(&callbackData[i], 0, sizeof(CALLBACK_DATA));
		}
	}
}

JNIEXPORT jboolean JNICALL CALLBACK_NATIVE(getEnabled)
  (JNIEnv *env, jclass that)
{
	return (jboolean)callbackEnabled;
}

JNIEXPORT jint JNICALL CALLBACK_NATIVE(getEntryCount)
  (JNIEnv *env, jclass that)
{
	return (jint)callbackEntryCount;
}

JNIEXPORT void JNICALL CALLBACK_NATIVE(setEnabled)
  (JNIEnv *env, jclass that, jboolean enable)
{
	callbackEnabled = enable;
}

JNIEXPORT void JNICALL CALLBACK_NATIVE(reset)
  (JNIEnv *env, jclass that)
{
	memset((void *)&callbackData, 0, sizeof(callbackData));
}

#if (defined(DEBUG_CALL_PRINTS) && defined(GTK))
const char* glibTypeNameFromInstance(void* object) {
	static int isInitialized = 0;
	static const char* (*g_type_name_from_instance)(void*) = 0;

	if (!isInitialized) {
		/* Do not dlclose(gobjectHandle); we're going to continue using the library */
		void* gobjectHandle = dlopen("libgobject-2.0.so.0", RTLD_LAZY);

		if (gobjectHandle)
			g_type_name_from_instance = dlsym(gobjectHandle, "g_type_name_from_instance");

		isInitialized = 1;
	}

	if (!g_type_name_from_instance)
		return "<No glib>";

	return g_type_name_from_instance(object);
}

const char* swtSignalNameFromId(int id) {
	/* Adapted from constants in org.eclipse.swt.widgets.Widget */
	switch (id) {
		case 1:   return "ACTIVATE";
		case 2:   return "BUTTON_PRESS_EVENT";
		case 3:   return "BUTTON_PRESS_EVENT_INVERSE";
		case 4:   return "BUTTON_RELEASE_EVENT";
		case 5:   return "BUTTON_RELEASE_EVENT_INVERSE";
		case 6:   return "CHANGED";
		case 7:   return "CHANGE_VALUE";
		case 8:   return "CLICKED";
		case 9:   return "COMMIT";
		case 10:  return "CONFIGURE_EVENT";
		case 11:  return "DELETE_EVENT";
		case 12:  return "DELETE_RANGE";
		case 13:  return "DELETE_TEXT";
		case 14:  return "ENTER_NOTIFY_EVENT";
		case 15:  return "EVENT";
		case 16:  return "EVENT_AFTER";
		case 17:  return "EXPAND_COLLAPSE_CURSOR_ROW";
		case 18:  return "EXPOSE_EVENT";
		case 19:  return "EXPOSE_EVENT_INVERSE";
		case 20:  return "FOCUS";
		case 21:  return "FOCUS_IN_EVENT";
		case 22:  return "FOCUS_OUT_EVENT";
		case 23:  return "GRAB_FOCUS";
		case 24:  return "HIDE";
		case 25:  return "INPUT";
		case 26:  return "INSERT_TEXT";
		case 27:  return "KEY_PRESS_EVENT";
		case 28:  return "KEY_RELEASE_EVENT";
		case 29:  return "LEAVE_NOTIFY_EVENT";
		case 30:  return "MAP";
		case 31:  return "MAP_EVENT";
		case 32:  return "MNEMONIC_ACTIVATE";
		case 33:  return "MOTION_NOTIFY_EVENT";
		case 34:  return "MOTION_NOTIFY_EVENT_INVERSE";
		case 35:  return "MOVE_FOCUS";
		case 36:  return "OUTPUT";
		case 37:  return "POPULATE_POPUP";
		case 38:  return "POPUP_MENU";
		case 39:  return "PREEDIT_CHANGED";
		case 40:  return "REALIZE";
		case 41:  return "ROW_ACTIVATED";
		case 42:  return "SCROLL_CHILD";
		case 43:  return "SCROLL_EVENT";
		case 44:  return "SELECT";
		case 45:  return "SHOW";
		case 46:  return "SHOW_HELP";
		case 47:  return "SIZE_ALLOCATE";
		case 48:  return "STYLE_UPDATED";
		case 49:  return "SWITCH_PAGE";
		case 50:  return "TEST_COLLAPSE_ROW";
		case 51:  return "TEST_EXPAND_ROW";
		case 52:  return "TEXT_BUFFER_INSERT_TEXT";
		case 53:  return "TOGGLED";
		case 54:  return "UNMAP";
		case 55:  return "UNMAP_EVENT";
		case 56:  return "UNREALIZE";
		case 57:  return "VALUE_CHANGED";
		case 59:  return "WINDOW_STATE_EVENT";
		case 60:  return "ACTIVATE_INVERSE";
		case 61:  return "DAY_SELECTED";
		case 62:  return "MONTH_CHANGED";
		case 63:  return "STATUS_ICON_POPUP_MENU";
		case 64:  return "ROW_INSERTED";
		case 65:  return "ROW_DELETED";
		case 66:  return "DAY_SELECTED_DOUBLE_CLICK";
		case 67:  return "ICON_RELEASE";
		case 68:  return "SELECTION_DONE";
		case 69:  return "START_INTERACTIVE_SEARCH";
		case 70:  return "BACKSPACE";
		case 71:  return "BACKSPACE_INVERSE";
		case 72:  return "COPY_CLIPBOARD";
		case 73:  return "COPY_CLIPBOARD_INVERSE";
		case 74:  return "CUT_CLIPBOARD";
		case 75:  return "CUT_CLIPBOARD_INVERSE";
		case 76:  return "PASTE_CLIPBOARD";
		case 77:  return "PASTE_CLIPBOARD_INVERSE";
		case 78:  return "DELETE_FROM_CURSOR";
		case 79:  return "DELETE_FROM_CURSOR_INVERSE";
		case 80:  return "MOVE_CURSOR";
		case 81:  return "MOVE_CURSOR_INVERSE";
		case 82:  return "DIRECTION_CHANGED";
		case 83:  return "CREATE_MENU_PROXY";
		case 84:  return "ROW_HAS_CHILD_TOGGLED";
		case 85:  return "POPPED_UP";
		case 86:  return "FOCUS_IN";
		case 87:  return "FOCUS_OUT";
		case 88:  return "IM_UPDATE";
		case 89:  return "KEY_PRESSED";
		case 90:  return "KEY_RELEASED";
		case 91:  return "DECELERATE";
		case 92:  return "SCROLL";
		case 93:  return "SCROLL_BEGIN";
		case 94:  return "SCROLL_END";
		case 95:  return "ENTER";
		case 96:  return "LEAVE";
		case 97:  return "MOTION";
		case 98:  return "MOTION_INVERSE";
		case 99:  return "CLOSE_REQUEST";
		case 100: return "GESTURE_PRESSED";
		case 101: return "GESTURE_RELEASED";
		case 102: return "NOTIFY_STATE";
		case 103: return "SIZE_ALLOCATE_GTK4";
		case 104: return "DPI_CHANGED";
	}

	return 0;
}
#endif

jlong callback(int index, ...)
{
	if (!callbackEnabled) return 0;

	{
	JNIEnv *env = NULL;
	jmethodID mid = callbackData[index].methodID;
	jobject object = callbackData[index].object;
	jboolean isStatic = callbackData[index].isStatic;
	jboolean isArrayBased = callbackData[index].isArrayBased;
	jint argCount = callbackData[index].argCount;
	jlong result = callbackData[index].errorResult;
	jthrowable ex;
	int detach = 0;
	va_list vl;

	va_start(vl, index);

#ifdef DEBUG_CALL_PRINTS
	{
		int i;
		va_list vaCopy;
		va_copy(vaCopy, vl);

		counter++;
		fprintf(stderr, "SWT-JNI:%*scallback[%d](", counter, "", index);
		for (i=0; i<argCount; i++) {
			void* arg = va_arg(vaCopy, void*);
			int isPrinted = 0;

			#ifdef COCOA
				if (!isPrinted && (i == callbackData[index].arg_Selector)) {
					fprintf(stderr, "%s ", sel_getName(arg));
					isPrinted = 1;
				}
			#elif defined(GTK)
				if (!isPrinted && (i == callbackData[index].arg_GObject)) {
					fprintf(stderr, "%p [%s] ", arg, glibTypeNameFromInstance(arg));
					isPrinted = 1;
				}

				if (!isPrinted && (i == callbackData[index].arg_GdkEvent)) {
					const GdkEventAny* event = (const GdkEventAny*)arg;
					fprintf(stderr,
						"%p [GdkEvent type=%d window=%p [%s]] ",
						event,
						event->type,
						event->window,
						glibTypeNameFromInstance(event->window)
					);

					isPrinted = 1;
				}

				if (!isPrinted && (i == callbackData[index].arg_SwtSignalID)) {
					int signalID = (int)(long long)arg;
					const char* signalName = swtSignalNameFromId(signalID);
					if (signalName)
						fprintf(stderr, "%s ", signalName);
					else
						fprintf(stderr, "%d ", signalID);
					isPrinted = 1;
				}
			#endif

			if (!isPrinted)
				fprintf(stderr, "%p ", arg);
		}
		fprintf(stderr, ") {\n");

		fflush(stderr);
		va_end(vaCopy);
	}
#endif

(*JVM)->GetEnv(JVM, (void **)&env, JNI_VERSION_1_4);

if (env == NULL) {
	(*JVM)->AttachCurrentThreadAsDaemon(JVM, (void **)&env, NULL);
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "SWT-JNI: AttachCurrentThreadAsDaemon\n");
#endif
	detach = 1;
}
	
	/* If the current thread is not attached to the VM, it is not possible to call into the VM */
	if (env == NULL) {
#ifdef DEBUG_CALL_PRINTS
		fprintf(stderr, "SWT-JNI:%*s ERROR(%d): (env == NULL)\n", counter, "", __LINE__);
		fflush(stderr);
#endif
		goto noEnv;
	}

	/* If an exception has occurred in previous callbacks do not call into the VM. */
	if ((ex = (*env)->ExceptionOccurred(env))) {
#ifdef DEBUG_CALL_PRINTS
		fprintf(stderr, "SWT-JNI:%*s ERROR(%d): (*env)->ExceptionOccurred()\n", counter, "", __LINE__);
		fflush(stderr);
#endif
		(*env)->DeleteLocalRef(env, ex);
		goto done;
	}

	/* Call into the VM. */
	ATOMIC_INC(callbackEntryCount);

	if (isArrayBased) {
		int i;
		jlongArray argsArray = (*env)->NewLongArray(env, argCount);
		if (argsArray != NULL) {
			jlong *elements = (*env)->GetLongArrayElements(env, argsArray, NULL);
			if (elements != NULL) {
				for (i=0; i<argCount; i++) {
					elements[i] = va_arg(vl, jlong);
				}
				(*env)->ReleaseLongArrayElements(env, argsArray, elements, 0);
				if (isStatic) {
					result = (*env)->CallStaticLongMethod(env, object, mid, argsArray);
				} else {
					result = (*env)->CallLongMethod(env, object, mid, argsArray);
				}
			}
			/*
			* This function may be called many times before returning to Java,
			* explicitly delete local references to avoid GP's in certain VMs.
			*/
			(*env)->DeleteLocalRef(env, argsArray);
		}
	} else {
		if (isStatic) {
			result = (*env)->CallStaticLongMethodV(env, object, mid, vl);
		} else {
			result = (*env)->CallLongMethodV(env, object, mid, vl);
		}
	}
	ATOMIC_DEC(callbackEntryCount);

done:
	va_end(vl);

	/* If an exception has occurred in Java, return the error result. */
	if ((ex = (*env)->ExceptionOccurred(env))) {
		(*env)->DeleteLocalRef(env, ex);
#ifdef DEBUG_CALL_PRINTS
		fprintf(stderr, "SWT-JNI:%*s ERROR(%d): (*env)->ExceptionOccurred()\n", counter, "", __LINE__);
		fflush(stderr);

		/* 
		 * WARNING: ExceptionDescribe() also clears exception as if it never happened.
		 * Don't do this because it changes the behavior of debugged program significantly.
		 * (*env)->ExceptionDescribe(env);
		 */
#endif
		result = callbackData[index].errorResult;
	}

	if (detach) {
		(*JVM)->DetachCurrentThread(JVM);
#ifdef DEBUG_CALL_PRINTS
		fprintf(stderr, "SWT-JNI: DetachCurrentThread\n");
#endif
		env = NULL;
	}

noEnv:

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "SWT-JNI:%*s} ret=%p\n", counter, "", (void*)result);
	fflush(stderr);
	counter--;
#endif

	return result;
	}
}

/* ------------- callback class calls end --------------- */
