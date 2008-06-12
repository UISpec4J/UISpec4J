package samples.calculator.functests;

import org.testng.annotations.Test;
import static org.uispec4j.assertion.UISpecAssert.assertTrue;

/**
 * Stage 2 in the evolution of a tests suite: utility methods and/or classes.
 */
public class CalculatorStage2Test {
  private Calculator calculator = new Calculator();

  @Test
  public void display() throws Exception {
    calculator.checkSequence("1", "1");
    calculator.checkSequence("123", "123");
    calculator.checkSequence("123+", "123");
    calculator.checkSequence("123+4", "4");
    calculator.checkSequence("123+456", "456");
  }

  @Test
  public void simpleAddition() throws Exception {
    calculator.checkAddition("2", "3", "5");
    calculator.checkAddition("12", "34", "46");
  }

  private class Calculator extends Adapter {

    public void checkSequence(String keySequence, String resultingDisplay) throws Exception {
      for (int i = 0, max = keySequence.length(); i < max; i++) {
        getMainWindow().getButton(keySequence.substring(i, i + 1)).click();
      }
      assertTrue(getMainWindow().getTextBox().textEquals(resultingDisplay));
      getMainWindow().getButton("C").click();
    }

    public void checkAddition(String operand1, String operand2, String result) throws Exception {
      checkSequence(operand1 + "+" + operand2 + "=", result);
    }
  }
}
