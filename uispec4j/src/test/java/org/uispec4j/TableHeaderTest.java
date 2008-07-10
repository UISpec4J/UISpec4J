package org.uispec4j;

import junit.framework.AssertionFailedError;
import org.uispec4j.utils.ArrayUtils;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.Functor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.regex.Pattern;

public class TableHeaderTest extends TableTestCase {
  public void test() throws Exception {
    JTableHeader tableHeader = jTable.getTableHeader();
    tableHeader.setVisible(false);
    jTable.setTableHeader(null);
    assertEquals(1, table.getHeader().findColumnIndex("1"));
  }

  public void testContent() throws Exception {
    assertTrue(table.getHeader().contentEquals("0", "1", "2"));
    try {
      assertTrue(table.getHeader().contentEquals());
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      Pattern pattern = Pattern.compile("expected:<.*> but was:<.*0,1,2.*>");
      if (!pattern.matcher(e.getMessage()).matches()) {
        fail("Unexpected message: " + e.getMessage());
      }
    }
  }

  public void testClickOnHeader() throws Exception {
    MouseLogger mouseLogger = new MouseLogger(jTable.getTableHeader());
    table.getHeader().click(0);
    mouseLogger.assertEquals("<log>" +
                             "  <mousePressed button='1'/>" +
                             "  <mouseReleased button='1'/>" +
                             "  <mouseClicked button='1'/>" +
                             "</log>");
  }

  public void testRightClickOnHeader() throws Exception {
    MouseLogger dummyHeaderListener = new MouseLogger(jTable.getTableHeader());
    table.getHeader().rightClick(0);
    dummyHeaderListener.assertEquals("<log>" +
                                     "  <mousePressed button='3'/>" +
                                     "  <mouseReleased button='3'/>" +
                                     "  <mouseClicked button='3'/>" +
                                     "</log>");
  }

  public void testHeader() throws Exception {
    assertTrue(table.hasHeader());

    jTable.setTableHeader(null);
    assertFalse(table.hasHeader());
    try {
      assertTrue(table.hasHeader());
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("The table contains an header", e.getMessage());
    }
  }

  public void testNoHeaderExceptions() throws Exception {
    jTable.setTableHeader(null);
    checkNoHeaderException(new Functor() {
      public void run() {
        assertTrue(table.getHeader().backgroundEquals(null));
      }
    });
    checkNoHeaderException(new Functor() {
      public void run() {
        assertTrue(table.getHeader().contentEquals());
      }
    });
    checkNoHeaderException(new Functor() {
      public void run() {
        table.getHeader().click(0);
      }
    });
    checkNoHeaderException(new Functor() {
      public void run() {
        table.getHeader().click("");
      }
    });
    checkNoHeaderException(new Functor() {
      public void run() throws Exception {
        table.getHeader().triggerClick(0).run();
      }
    });
    checkNoHeaderException(new Functor() {
      public void run() throws Exception {
        table.getHeader().triggerClick("").run();
      }
    });
    checkNoHeaderException(new Functor() {
      public void run() {
        table.getHeader().getDefaultBackground();
      }
    });
    checkNoHeaderException(new Functor() {
      public void run() {
        table.getHeader().rightClick(0);
      }
    });
    checkNoHeaderException(new Functor() {
      public void run() {
        table.getHeader().rightClick("");
      }
    });
    checkNoHeaderException(new Functor() {
      public void run() throws Exception {
        table.getHeader().triggerRightClick(0).run();
      }
    });
    checkNoHeaderException(new Functor() {
      public void run() throws Exception {
        table.getHeader().triggerRightClick("").run();
      }
    });
  }

  public void testAssertHeaderBackgroundEquals() throws Exception {
    jTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (column == 1) {
          component.setBackground(Color.red);
        }
        else {
          component.setBackground(Color.blue);
        }
        return component;
      }
    });

    assertTrue(table.getHeader().backgroundEquals(new Object[]{"blue", "red", "blue"}));

    try {
      assertTrue(table.getHeader().backgroundEquals(new Object[]{"blue", "black", "blue"}));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Unexpected color at column 1 expected:<BLACK> but was:<FF0000>", e.getMessage());
    }
  }

  public void testGetColumnNames() throws Exception {
    ArrayUtils.assertEquals(new String[0], new Table(new JTable()).getHeader().getColumnNames());
    ArrayUtils.assertEquals(new String[]{"0", "1", "2"}, table.getHeader().getColumnNames());
  }

  public void testFindColumnIndex() throws Exception {
    assertEquals(0, table.getHeader().findColumnIndex("0"));
    assertEquals(1, table.getHeader().findColumnIndex("1"));
    assertEquals(2, table.getHeader().findColumnIndex("2"));

    try {
      table.getHeader().findColumnIndex("unknown");
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Column 'unknown' not found", e.getMessage());
    }
  }

  private void checkNoHeaderException(Functor functor) throws Exception {
    try {
      functor.run();
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("The table contains no header", e.getMessage());
    }
  }
}
