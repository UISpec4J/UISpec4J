package org.uispec4j.interception;

import org.uispec4j.Trigger;
import org.uispec4j.utils.ArrayUtils;
import org.uispec4j.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileChooserHandlerTest extends InterceptionTestCase {
  private JFileChooser chooser = new JFileChooser();
  private int result= JFileChooser.ERROR_OPTION;
  private Trigger SHOW_OPEN_DIALOG_TRIGGER = new Trigger() {
    public void run() throws Exception {
      JFrame frame = new JFrame();
      result = chooser.showOpenDialog(frame);
    }
  };
  private Trigger SHOW_SAVE_DIALOG_TRIGGER = new Trigger() {
    public void run() throws Exception {
      JFrame frame = new JFrame();
      result = chooser.showSaveDialog(frame);
    }
  };
  private Trigger SHOW_CUSTOM_DIALOG_TRIGGER = new Trigger() {
    public void run() throws Exception {
      JFrame frame = new JFrame();
      result = chooser.showDialog(frame, "OK");
    }
  };
  private File javaHome = new File(System.getProperty("java.home"));
  private File userHome = new File(System.getProperty("user.home"));

  protected void setUp() throws Exception {
    super.setUp();
    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
  }

  public void testSelectionOfASingleFile() throws Exception {
    WindowInterceptor
      .init(SHOW_OPEN_DIALOG_TRIGGER)
      .process(FileChooserHandler.init().select(javaHome))
      .run();
    assertEquals(javaHome, chooser.getSelectedFile());
    assertEquals(JFileChooser.APPROVE_OPTION, result);
  }

  public void testSelectionOfSeveralFiles() throws Exception {
    File[] files = {javaHome, userHome};
    WindowInterceptor
      .init(SHOW_OPEN_DIALOG_TRIGGER)
      .process(FileChooserHandler.init().select(files))
      .run();
    ArrayUtils.assertEquals(files, chooser.getSelectedFiles());
    assertEquals(JFileChooser.APPROVE_OPTION, result);
  }

  public void testSelectionOfASingleStringifiedFile() throws Exception {
    WindowInterceptor
      .init(SHOW_OPEN_DIALOG_TRIGGER)
      .process(FileChooserHandler.init().select(javaHome.getAbsolutePath()))
      .run();
    assertEquals(javaHome, chooser.getSelectedFile());
    assertEquals(JFileChooser.APPROVE_OPTION, result);
  }

  public void testSelectionOfSeveralStringifiedFile() throws Exception {
    String[] files = {javaHome.getAbsolutePath(), userHome.getAbsolutePath()};
    WindowInterceptor
      .init(SHOW_OPEN_DIALOG_TRIGGER)
      .process(FileChooserHandler.init().select(files))
      .run();
    ArrayUtils.assertEquals(new File[]{javaHome, userHome}, chooser.getSelectedFiles());
    assertEquals(JFileChooser.APPROVE_OPTION, result);
  }

  public void testCancelSelection() throws Exception {
    WindowInterceptor
      .init(SHOW_OPEN_DIALOG_TRIGGER)
      .process(FileChooserHandler.init().cancelSelection())
      .run();
    assertEquals(0, chooser.getSelectedFiles().length);
    assertEquals(JFileChooser.CANCEL_OPTION, result);
  }

  public void testAssertCurrentDirEquals() throws Exception {
    chooser.setCurrentDirectory(javaHome);
    WindowInterceptor
      .init(SHOW_OPEN_DIALOG_TRIGGER)
      .process(FileChooserHandler.init()
        .assertCurrentDirEquals(javaHome)
        .select(javaHome))
      .run();
  }

  public void testAssertCurrentDirEqualsError() throws Exception {
    chooser.setCurrentDirectory(javaHome);
    checkError(SHOW_OPEN_DIALOG_TRIGGER, FileChooserHandler.init().assertCurrentDirEquals(userHome),
               javaHome, "Unexpected current directory - expected:<"
                         + userHome + "> but was:<" + javaHome + ">");
  }

  public void testAssertCurrentFileNameEquals() throws Exception {
    chooser.setSelectedFile(new File(javaHome, "aFile.txt"));
    WindowInterceptor
      .init(SHOW_OPEN_DIALOG_TRIGGER)
      .process(FileChooserHandler.init()
        .assertCurrentFileNameEquals("aFile.txt")
        .select(javaHome))
      .run();
  }

  public void testAssertCurrentFileNameEqualsError() throws Exception {
    chooser.setSelectedFile(new File(javaHome, "aFile.txt"));
    checkError(SHOW_OPEN_DIALOG_TRIGGER, 
               FileChooserHandler.init().assertCurrentFileNameEquals("toto.exe"),
               new File(javaHome, "aFile.txt"),
               "Unexpected file name - expected:<toto.exe> but was:<aFile.txt>");
  }

  public void testAssertCurrentFileNameEqualsWithNoSelection() throws Exception {
    checkError(SHOW_OPEN_DIALOG_TRIGGER,
               FileChooserHandler.init().assertCurrentFileNameEquals("toto.dat"),
               new File(javaHome, "aFile.txt"),
               "Unexpected file name - expected:<toto.dat> but was:<>");
  }

  public void testAssertIsOpenSaveDialog() throws Exception {
    checkOk(SHOW_OPEN_DIALOG_TRIGGER, FileChooserHandler.init().assertIsOpenDialog());
    checkError(SHOW_OPEN_DIALOG_TRIGGER, FileChooserHandler.init().assertIsSaveDialog(),
               javaHome, "Chooser is in 'open' mode");

    checkOk(SHOW_SAVE_DIALOG_TRIGGER, FileChooserHandler.init().assertIsSaveDialog());
    checkError(SHOW_SAVE_DIALOG_TRIGGER, FileChooserHandler.init().assertIsOpenDialog(),
               javaHome, "Chooser is in 'save' mode");

    checkError(SHOW_CUSTOM_DIALOG_TRIGGER, FileChooserHandler.init().assertIsSaveDialog(),
               javaHome, "Chooser is in 'custom' mode");
    checkError(SHOW_CUSTOM_DIALOG_TRIGGER, FileChooserHandler.init().assertIsOpenDialog(),
               javaHome, "Chooser is in 'custom' mode");
  }

  public void testDefaultTitle() throws Exception {
    checkOk(SHOW_OPEN_DIALOG_TRIGGER, FileChooserHandler.init().titleEquals("Open"));
    checkOk(SHOW_SAVE_DIALOG_TRIGGER, FileChooserHandler.init().titleEquals("Save"));
  }

  public void testCustomTitle() throws Exception {
    chooser.setDialogTitle("title");
    checkOk(SHOW_OPEN_DIALOG_TRIGGER, FileChooserHandler.init().titleEquals("title"));
    checkError(SHOW_OPEN_DIALOG_TRIGGER, FileChooserHandler.init().titleEquals("error"),
               javaHome, "Unexpected title - expected:<error> but was:<title>");
  }

  public void testAssertApplyButtonTextEquals() throws Exception {
    chooser.setApproveButtonText("text");
    checkOk(SHOW_OPEN_DIALOG_TRIGGER, FileChooserHandler.init().assertApplyButtonTextEquals("text"));
    checkError(SHOW_OPEN_DIALOG_TRIGGER, FileChooserHandler.init().assertApplyButtonTextEquals("other"),
               javaHome, "Unexpected apply button text - expected:<other> but was:<text>");
  }

  public void testAssertAcceptsFilesAndDirectories() throws Exception {
    final int[] modes =
      {JFileChooser.FILES_ONLY,
       JFileChooser.FILES_AND_DIRECTORIES,
       JFileChooser.DIRECTORIES_ONLY};
    final String[] messages =
      {"The file chooser accepts files only.",
       "The file chooser accepts both files and directories.",
       "The file chooser accepts directories only."};
    for (int i = 0; i < modes.length; i++) {
      final FileChooserHandler[] interceptors =
        {FileChooserHandler.init().assertAcceptsFilesOnly(),
         FileChooserHandler.init().assertAcceptsFilesAndDirectories(),
         FileChooserHandler.init().assertAcceptsDirectoriesOnly()};
      chooser.setFileSelectionMode(modes[i]);
      for (int j = 0; j < modes.length; j++) {
        if (i == j) {
          checkOk(SHOW_OPEN_DIALOG_TRIGGER, interceptors[j]);
        }
        else {
          checkError(SHOW_OPEN_DIALOG_TRIGGER, interceptors[j], javaHome, messages[i]);
        }
      }
    }
  }

  public void testAssertMultiSelectionEnabled() throws Exception {
    checkMultiSelectionEnabled(true, "Multi selection is enabled.");
    checkMultiSelectionEnabled(false, "Multi selection is not enabled.");
  }

  public void testShownDialogIsNotAFileChooserButAJFrame() throws Exception {
    checkUnexpectedWindowShown(new JFrame("title"), "title");
  }

  public void testShownDialogIsNotAFileChooserButAModalDialog() throws Exception {
    checkUnexpectedWindowShown(createModalDialog("aDialog"), "aDialog");
  }

  private void checkUnexpectedWindowShown(final Window window, String title) {
    checkAssertionFailedError(WindowInterceptor
      .init(new Trigger() {
        public void run() throws Exception {
          window.setVisible(true);
        }
      })
      .process(FileChooserHandler.init().select(javaHome)),
                              "The shown window is not a file chooser - window content:" + Utils.LINE_SEPARATOR +
                              "<window title=\"" + title + "\"/>");
  }

  private void checkOk(Trigger trigger, FileChooserHandler handler) {
    WindowInterceptor
      .init(trigger)
      .process(handler.select(javaHome))
      .run();
  }

  private void checkError(Trigger trigger,
                          FileChooserHandler handler,
                          File selectedFile,
                          String errorMessage) {
    checkAssertionFailedError(WindowInterceptor
      .init(trigger)
      .process(handler.select(selectedFile)),
                              errorMessage);
  }

  private void checkMultiSelectionEnabled(boolean enabled, String message) {
    chooser.setMultiSelectionEnabled(enabled);
    checkOk(SHOW_OPEN_DIALOG_TRIGGER,
            FileChooserHandler.init().assertMultiSelectionEnabled(enabled));
    checkError(SHOW_OPEN_DIALOG_TRIGGER,
               FileChooserHandler.init().assertMultiSelectionEnabled(!enabled), javaHome, message);
  }
}
