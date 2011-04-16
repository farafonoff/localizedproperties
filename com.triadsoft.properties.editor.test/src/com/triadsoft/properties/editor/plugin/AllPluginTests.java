package com.triadsoft.properties.editor.plugin;

import com.triadsoft.properties.editor.plugin.resource.FlexFilesTest;
import com.triadsoft.properties.editor.plugin.resource.JavaFilesTest;
import com.triadsoft.properties.editor.plugin.resource.PathDiscoveryNewImplTest;
import com.triadsoft.properties.editor.plugin.resource.WebJavaFilesTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPluginTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllPluginTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTestSuite(WebJavaFilesTest.class);
		suite.addTestSuite(FlexFilesTest.class);
		suite.addTestSuite(JavaFilesTest.class);
		suite.addTestSuite(PathDiscoveryNewImplTest.class);
		suite.addTestSuite(WebJavaFilesTest.class);
		suite.addTestSuite(DefaultsPreferencesTest.class);
		suite.addTestSuite(ToRegexTests.class);
		// $JUnit-END$
		return suite;
	}

}
