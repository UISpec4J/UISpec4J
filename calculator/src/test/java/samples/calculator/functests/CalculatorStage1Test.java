package samples.calculator.functests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.uispec4j.Panel;
import static org.uispec4j.assertion.UISpecAssert.assertTrue;

/**
 * Stage 1 in the evolution of a tests suite: total duplication.
 */
public class CalculatorStage1Test {
  private Panel calculatorPanel;


    @BeforeMethod()
    protected void setUp() throws Exception {
    Adapter adapter = new Adapter();
    calculatorPanel = adapter.getMainWindow();
  }



    @Test
    public void startupDisplay() {
    assertTrue(calculatorPanel.getTextBox().textEquals("0"));
  }


    @Test
    public void displayForASingleNumber() {
    calculatorPanel.getButton("1").click();
    assertTrue(calculatorPanel.getTextBox().textEquals("1"));
  }


    @Test
    public void displayForASequenceOfNumbers() {
    calculatorPanel.getButton("2").click();
    calculatorPanel.getButton("3").click();
    calculatorPanel.getButton("4").click();
    calculatorPanel.getButton("5").click();
    assertTrue(calculatorPanel.getTextBox().textEquals("2345"));
  }


    @Test
    public void firstOperandIsDisplayedWhenAnOperatorIsEntered() {
    calculatorPanel.getButton("2").click();
    calculatorPanel.getButton("3").click();
    calculatorPanel.getButton("+").click();
    assertTrue(calculatorPanel.getTextBox().textEquals("23"));
  }


    @Test
    public void secondOperandIsDisplayedAfterAnOperatorWasEntered() {
    calculatorPanel.getButton("2").click();
    calculatorPanel.getButton("+").click();
    calculatorPanel.getButton("3").click();
    assertTrue(calculatorPanel.getTextBox().textEquals("3"));
  }


    @Test
    public void simpleAddition() {
    calculatorPanel.getButton("2").click();
    calculatorPanel.getButton("+").click();
    calculatorPanel.getButton("3").click();
    calculatorPanel.getButton("=").click();
    assertTrue(calculatorPanel.getTextBox().textEquals("5"));
  }


    @Test
    public void additionWithMultiDigitNumbers() {
    calculatorPanel.getButton("1").click();
    calculatorPanel.getButton("2").click();
    calculatorPanel.getButton("+").click();
    calculatorPanel.getButton("3").click();
    calculatorPanel.getButton("4").click();
    calculatorPanel.getButton("=").click();
    assertTrue(calculatorPanel.getTextBox().textEquals("46"));
  }


    @Test
    public void cancel() {
    calculatorPanel.getButton("2").click();
    calculatorPanel.getButton("+").click();
    calculatorPanel.getButton("3").click();
    calculatorPanel.getButton("C").click();
    assertTrue(calculatorPanel.getTextBox().textEquals("0"));
  }
}
