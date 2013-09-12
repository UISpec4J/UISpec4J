package org.uispec4j;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.uispec4j.interception.WindowInterceptor;
import org.uispec4j.utils.UnitTestCase;
import org.uispec4j.utils.Utils;
import org.uispec4j.xml.EventLogger;

public class UISpecTestCaseTest extends UnitTestCase {

	public class MyTestCase extends UISpecTestCase {
		public MyTestCase() {
		}

		public void test() throws Exception {
		}
	}

	public static class MyAdapter extends EventLogger implements UISpecAdapter {
		private final JFrame frame;

		public MyAdapter(final JFrame frame) {
			this.frame = frame;
		}

		public MyAdapter() {
			this(new JFrame("title"));
		}

		public Window getMainWindow() {
			log("getMainWindow");
			return new Window(frame);
		}
	}

	public void testPropertyDefined() throws Exception {
		System.setProperty(UISpecTestCase.ADAPTER_CLASS_PROPERTY,
				MyAdapter.class.getName());
		final MyTestCase testCase = new MyTestCase();
		testCase.setUp();
		assertTrue(testCase.getMainWindow().titleEquals("title"));
	}

	public void testGetMainWindowFailsIfThePropertyWasNotDefined()
			throws Exception {
		System.getProperties().remove(UISpecTestCase.ADAPTER_CLASS_PROPERTY);
		final MyTestCase testCase = new MyTestCase();
		testCase.setUp();
		try {
			testCase.getMainWindow();
			fail();
		} catch (final UISpecTestCase.AdapterNotFoundException e) {
			assertEquals(UISpecTestCase.PROPERTY_NOT_DEFINED, e.getMessage());
		}
	}

	public void testGetMainWindowFailsIfThePropertyWasInitializedWithAWrongValue()
			throws Exception {
		System.setProperty(UISpecTestCase.ADAPTER_CLASS_PROPERTY, "unknown");
		final MyTestCase testCase = new MyTestCase();
		testCase.setUp();
		try {
			testCase.getMainWindow();
			fail();
		} catch (final UISpecTestCase.AdapterNotFoundException e) {
			assertEquals("Adapter class 'unknown' not found", e.getMessage());
		}
	}

	public void testSettingTheAdapter() throws Exception {
		final MyAdapter adapter1 = new MyAdapter();
		final MyAdapter adapter2 = new MyAdapter();

		final MyTestCase testCase = new MyTestCase();
		testCase.setAdapter(adapter1);
		testCase.setUp();
		assertTrue(testCase.getMainWindow().titleEquals("title"));
		adapter1.assertEquals("<log>" + "  <getMainWindow/>" + "</log>");
		adapter2.assertEmpty();

		testCase.getMainWindow();
		adapter1.assertEquals("<log>" + "  <getMainWindow/>" + "</log>");
		adapter2.assertEmpty();

		testCase.setAdapter(adapter2);
		testCase.getMainWindow();
		adapter1.assertEmpty();
		adapter2.assertEquals("<log>" + "  <getMainWindow/>" + "</log>");
	}

	public void testTriggerExceptionsAreStoredAndRethrownInTearDownWhenNotCaughtImmediately()
			throws Exception {
		UISpec4J.setWindowInterceptionTimeLimit(100);
		final JFrame frame = new JFrame("my frame");
		final Exception exception = new RuntimeException("triggerException");

		final MyExceptionTestCase exceptionTestCase = new MyExceptionTestCase(
				frame, exception);
		exceptionTestCase.setAdapter(new MyAdapter(frame));
		exceptionTestCase.setUp();
		exceptionTestCase.test();
		try {
			exceptionTestCase.tearDown();
			fail();
		} catch (final Exception e) {
			assertSame(exception, e.getCause());
		}
	}

	private JDialog createModalDialog(final JFrame frame) {
		final JDialog firstDialog = new JDialog(frame, "dlg", true);
		firstDialog.getContentPane().add(
				new JButton(new AbstractAction("Close") {
					public void actionPerformed(final ActionEvent e) {
						firstDialog.dispose();
					}
				}));
		return firstDialog;
	}

	private class MyExceptionTestCase extends UISpecTestCase {
		final JDialog firstDialog;
		private final Exception exception;

		public MyExceptionTestCase(final JFrame frame, final Exception exception) {
			this.exception = exception;
			this.firstDialog = createModalDialog(frame);
		}

		public void test() throws Exception {
			final Window window = WindowInterceptor
					.getModalDialog(new Trigger() {
						public void run() throws Exception {
							firstDialog.setVisible(true);
							throw exception;
						}
					});
			window.getButton("Close").click();
			Utils.sleep(1);
		}
	}
}
