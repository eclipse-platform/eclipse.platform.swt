package org.eclipse.swt.tests.skia;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ Test_org_eclipse_swt_skia_RectangleConverter.class, //
		Test_org_eclipse_swt_skia_RGBAEncoder.class, //
		Test_org_eclipse_swt_skia_SkijaToSwtImageConvert.class, //
		Test_org_eclipse_swt_skia_SkijaToSwtImageConverter.class, //
})
public class AllSkiaTests {

}