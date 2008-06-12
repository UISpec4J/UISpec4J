package org.uispec4j;

import junit.framework.AssertionFailedError;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.Counter;
import org.uispec4j.xml.EventLogger;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TableSelectionTest extends TableTestCase {

  public void testClickCallsMouseListeners() throws Exception {
    MouseLogger mouseLogger = new MouseLogger(jTable);
    table.click(0, 1);
    mouseLogger.assertEquals("<log>" +
                             "  <mousePressed button='1'/>" +
                             "  <mouseReleased button='1'/>" +
                             "  <mouseClicked button='1'/>" +
                             "</log>");
  }

  public void testAssertSelectionEquals() throws Exception {
    jTable.setCellSelectionEnabled(true);
    table.click(0, 1);
    assertTrue(table.selectionEquals(new boolean[][]{
      {false, true, false},
      {false, false, false}
    }));

    table.click(1, 1, Key.Modifier.CONTROL);
    assertTrue(table.selectionEquals(new boolean[][]{
      {false, true, false},
      {false, true, false}
    }));

    try {
      assertTrue(table.selectionEquals(new boolean[][]{
        {false, false, false},
        {false, false, false}
      }));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("expected:<[[false,false,false], [false,false,false]]> " +
                   "but was:<[[false,true,false], [false,true,false]]>",
                   e.getMessage());
    }
  }

  public void testAssertRowSelected() throws Exception {
    jTable.setCellSelectionEnabled(false);
    table.click(0, 1);

    assertTrue(table.rowIsSelected(0));
    assertFalse(table.rowIsSelected(1));

    try {
      assertTrue(table.rowIsSelected(1));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }

    try {
      assertFalse(table.rowIsSelected(0));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }
  }

  public void testAssertRowsSelected() throws Exception {
    checkAssertRowsSelected(false);
    checkAssertRowsSelected(true);
  }

  private void checkAssertRowsSelected(boolean cellSelectionEnabled) {
    jTable.setCellSelectionEnabled(cellSelectionEnabled);
    jTable.clearSelection();
    assertTrue(table.rowsAreSelected(new int[]{}));

    table.click(0, 1);
    assertTrue(table.rowsAreSelected(new int[]{0}));

    table.click(1, 1);
    assertTrue(table.rowsAreSelected(new int[]{1}));

    table.click(0, 1, Key.Modifier.CONTROL);
    assertTrue(table.rowsAreSelected(new int[]{0, 1}));
    assertTrue(table.rowsAreSelected(new int[]{1, 0}));

    try {
      assertTrue(table.rowsAreSelected(new int[]{0}));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Expected: [0]\nActual:   [0,1]", e.getMessage());
    }
  }

  public void testDoubleClickChangesSelection() throws Exception {
    final Counter doubleClickCounter = new Counter();
    jTable.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          doubleClickCounter.increment();
        }
      }
    });
    jTable.setCellSelectionEnabled(true);
    table.selectCell(1, 0);

    assertTrue(table.selectionEquals(new boolean[][]{
      {false, false, false},
      {true, false, false}
    }));

    assertEquals(0, doubleClickCounter.getCount());
    table.doubleClick(0, 1);
    assertEquals(1, doubleClickCounter.getCount());

    assertTrue(table.selectionEquals(new boolean[][]{
      {false, true, false},
      {false, false, false}
    }));
  }

  public void testAssertCellSelected() throws Exception {
    jTable.setCellSelectionEnabled(true);
    table.click(0, 1);

    assertTrue(table.cellIsSelected(0, 1));
    assertFalse(table.cellIsSelected(1, 1));

    try {
      assertTrue(table.cellIsSelected(1, 1));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }

    try {
      assertFalse(table.cellIsSelected(0, 1));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }
  }

  public void testAssertCellSelectedWithRowLevelSelection() throws Exception {
    jTable.setCellSelectionEnabled(false);
    table.click(0, 1);

    try {
      assertTrue(table.cellIsSelected(0, 1));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Cell-level selection is not supported on this table", e.getMessage());
    }
  }

  public void testAssertSelectionIsEmpty() throws Exception {
    assertTrue(table.selectionIsEmpty());
    table.click(1, 0);
    try {
      assertTrue(table.selectionIsEmpty());
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Selection is not empty", e.getMessage());
    }
    table.clearSelection();
    table.selectionIsEmpty();
  }

  public void testClearSelection() throws Exception {
    jTable.addRowSelectionInterval(1, 1);
    table.clearSelection();
    assertTrue(table.selectionIsEmpty());
  }

  public void testSelectCell() throws Exception {
    jTable.setCellSelectionEnabled(true);
    table.selectCell(0, 0);
    assertTrue(table.selectionEquals(new boolean[][]{
      {true, false, false},
      {false, false, false}
    }));
    table.selectCell(1, 2);
    assertTrue(table.selectionEquals(new boolean[][]{
      {false, false, false},
      {false, false, true}
    }));
  }

  public void testSelectCellWithCellSelectionDisabled() throws Exception {
    jTable.setCellSelectionEnabled(false);
    try {
      table.selectCell(0, 0);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Individual cell selection is not allowed on this table", e.getMessage());
    }
  }

  public void testSelectRow() throws Exception {
    checkSelectRow(false);
    checkSelectRow(true);
  }

  private void checkSelectRow(boolean cellSelectionEnabled) {
    jTable.setCellSelectionEnabled(cellSelectionEnabled);
    table.selectRow(0);
    checkRowSelection(true, false);

    table.selectRow(1);
    checkRowSelection(false, true);
  }

  public void testSelectRows() throws Exception {
    checkSelectRows(false);
    checkSelectRows(true);
  }

  private void checkSelectRows(boolean cellSelectionEnabled) {
    jTable.setCellSelectionEnabled(cellSelectionEnabled);
    table.selectRows(new int[]{0});
    checkRowSelection(true, false);

    table.selectRows(new int[]{1});
    checkRowSelection(false, true);

    table.selectRows(new int[]{0, 1});
    checkRowSelection(true, true);

    table.selectRows(0, 0);
    checkRowSelection(true, false);

    table.selectRows(1, 1);
    checkRowSelection(false, true);

    table.selectRows(0, 1);
    checkRowSelection(true, true);
  }

  public void testSelectRowsRequiresThatTheStartIndexBeLessThanTheEndIndex() throws Exception {
    try {
      table.selectRows(1, 0);
      throw new AssertionFailureNotDetectedError();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Invalid indexes: 1 > 0", e.getMessage());
    }
  }

  public void testSelectBlock() throws Exception {
    jTable.setCellSelectionEnabled(true);
    table.selectBlock(0, 0, 1, 1);
    table.selectionEquals(new boolean[][]{
      {true, true, false},
      {true, true, false}
    });
    table.selectBlock(0, 1, 0, 2);
    table.selectionEquals(new boolean[][]{
      {false, true, true},
      {false, false, false}
    });
    table.selectBlock(1, 1, 1, 1);
    table.selectionEquals(new boolean[][]{
      {false, false, false},
      {false, true, false}
    });
  }

  public void testSelectBlockWithInvalidRectangle() throws Exception {
    jTable.setCellSelectionEnabled(true);
    try {
      table.selectBlock(1, 1, 0, 0);
      throw new AssertionFailureNotDetectedError();
    }
    catch (IllegalArgumentException e) {
      assertEquals("Invalid block definition - expected top <= bottom and left <= right", e.getMessage());
    }
  }

  public void testSelectBlockWithJTableCellSelectionDisabled() throws Exception {
    jTable.setCellSelectionEnabled(false);
    try {
      table.selectBlock(0, 0, 1, 1);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Only row-level selection is allowed on this table", e.getMessage());
    }
  }

  public void testAddRemoveSelectedRow() throws Exception {
    checkAddRemoveSelectedRows(false);
    checkAddRemoveSelectedRows(true);
  }

  public void testRemoveRowFromSelectionChecksThatTheRowWasSelected() throws Exception {
    try {
      table.removeRowFromSelection(1);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Row 1 is not selected", e.getMessage());
    }
  }

  public void testMultiSelectionUpdatedTheAValueIsAdjustingMode() throws Exception {
    final EventLogger logger = new EventLogger();
    jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
          logger.log("valueChanged");
        }
      }
    });

    table.selectRows(new int[]{0, 1});
    logger.assertEquals("<log><valueChanged/></log>");
    table.selectRows(0, 1);
    logger.assertEquals("<log><valueChanged/></log>");
  }

  private void checkAddRemoveSelectedRows(boolean cellSelectionEnabled) {
    jTable.setCellSelectionEnabled(cellSelectionEnabled);
    table.clearSelection();
    table.addRowToSelection(0);
    checkRowSelection(true, false);

    table.addRowToSelection(1);
    checkRowSelection(true, true);

    table.removeRowFromSelection(0);
    checkRowSelection(false, true);

    table.removeRowFromSelection(1);
    checkRowSelection(false, false);
  }

  private void checkRowSelection(boolean row0Selected, boolean row1Selected) {
    boolean[] trues = {true, true, true};
    boolean[] falses = {false, false, false};
    boolean[][] expected = new boolean[2][3];
    expected[0] = row0Selected ? trues : falses;
    expected[1] = row1Selected ? trues : falses;
    assertTrue(table.selectionEquals(expected));
  }

}
