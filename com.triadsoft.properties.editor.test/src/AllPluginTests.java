import junit.framework.Test;
import junit.framework.TestSuite;

import com.triadsoft.properties.editor.plugin.DefaultsPreferencesTest;
import com.triadsoft.properties.editor.plugin.FullProjectTest;
import com.triadsoft.properties.editor.plugin.ToRegexTests;
import com.triadsoft.properties.editor.plugin.resource.FlexFilesTest;
import com.triadsoft.properties.editor.plugin.resource.JavaFilesTest;
import com.triadsoft.properties.editor.plugin.resource.PathDiscoveryNewImplTest;
import com.triadsoft.properties.editor.plugin.resource.WebJavaFilesTest;
import com.triadsoft.properties.editor.utils.StringUtilsTest;
import com.triadsoft.properties.editor.utils.WildcardPath2Test;
import com.triadsoft.properties.editor.utils.WildcardPathTest;

public class AllPluginTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllPluginTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTestSuite(WebJavaFilesTest.class);
		suite.addTestSuite(FlexFilesTest.class);
		suite.addTestSuite(JavaFilesTest.class);
		suite.addTestSuite(PathDiscoveryNewImplTest.class);
		suite.addTestSuite(DefaultsPreferencesTest.class);
		suite.addTestSuite(ToRegexTests.class);
		suite.addTestSuite(FullProjectTest.class);
		suite.addTestSuite(WildcardPath2Test.class);
		suite.addTestSuite(WildcardPathTest.class);
		suite.addTestSuite(StringUtilsTest.class);
		// $JUnit-END$
		return suite;
	}

}
