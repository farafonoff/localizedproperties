package com.triadsoft.properties.editor.plugin;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPluginTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllPluginTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(WebJavaFilesTest.class);
		suite.addTestSuite(FlexFilesTest.class);
		suite.addTestSuite(JavaFilesTest.class);
		//$JUnit-END$
		return suite;
	}

}
