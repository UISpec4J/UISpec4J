package samples.calculator;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import samples.utils.AssertionFailureNotDetectedError;

public class CalculatorTest {
  private Calculator calculator;

  @BeforeMethod
  protected void setUp() throws Exception {
    calculator = new Calculator();
  }

  @Test
  public void initialValue() throws Exception {
    assertEquals(calculator.getCurrentValue(), "0");
  }

  @Test
  public void writingTheFirstOperand() throws Exception {
    check('1', "1");
    check('2', "12");
    check('3', "123");
    check('4', "1234");
  }

  @Test
  public void initialZeroesAreIgnored() throws Exception {
    check('0', "0");
    check('0', "0");
    check('0', "0");
    check('1', "1");
    check('0', "10");
  }

  @Test
  public void simpleAddition() throws Exception {
    check('1', "1");
    check('+', "1");
    check('2', "2");
    check('=', "3");
  }

  @Test
  public void initialZeroesAreIgnoredForTheSecondOperand() throws Exception {
    check('1', "1");
    check('+', "1");
    check('0', "0");
    check('0', "0");
    check('0', "0");
    check('1', "1");
    check('0', "10");
  }

  @Test
  public void usingEqualsAfterTheFirstOperandDoesNothing() throws Exception {
    check('1', "1");
    check('=', "1");
    check('+', "1");
    check('2', "2");
    check('=', "3");
  }

  @Test
  public void resultWithLongNumber() throws Exception {
    check('1', "1");
    check('2', "12");
    check('3', "123");
    check('4', "1234");
    check('5', "12345");
    check('6', "123456");
  }

  @Test
  public void usingAnOperatorAfterTheSecondOperand() throws Exception {
    check('1', "1");
    check('+', "1");
    check('1', "1");
    check('-', "2");
    check('=', "0");
    check('=', "-2");
  }

  @Test
  public void onlyTheLastOperatorOfASequenceIsKept() throws Exception {
    check('1', "1");
    check('+', "1");
    check('-', "1");
    check('1', "1");
    check('=', "0");
  }

  @Test
  public void usingEqualsAfterTheFirstOperatorUsesTheFirstOperandTwice() throws Exception {
    check('1', "1");
    check('+', "1");
    check('=', "2");
    check('=', "3");
    check('=', "4");
  }

  @Test
  public void operatorAfterResultKeepsFirstOperand() throws Exception {
    check('1', "1");
    check('+', "1");
    check('=', "2");
    check('+', "2");
    check('1', "1");
    check('=', "3");
  }

  @Test
  public void operatorAfterSecondOperandAppliesOperator() throws Exception {
    check('1', "1");
    check('+', "1");
    check('1', "1");
    check('+', "2");
  }

  @Test
  public void substraction() throws Exception {
    check('3', "3");
    check('-', "3");
    check('1', "1");
    check('2', "12");
    check('=', "-9");
  }

  @Test
  public void multiplication() throws Exception {
    check('3', "3");
    check('*', "3");
    check('1', "1");
    check('2', "12");
    check('=', "36");
  }

  @Test
  public void multiplicationWithFloatResult() throws Exception {
    check('3', "3");
    check('.', "3.");
    check('3', "3.3");
    check('*', "3.3");
    check('1', "1");
    check('.', "1.");
    check('2', "1.2");
    check('=', "3.96");
  }

  @Test
  public void division() throws Exception {
    check('1', "1");
    check('2', "12");
    check('/', "12");
    check('3', "3");
    check('=', "4");
  }

  @Test
  public void divisionWithFloatResult() throws Exception {
    check('1', "1");
    check('2', "12");
    check('/', "12");
    check('5', "5");
    check('=', "2.4");
  }

  @Test
  public void divisionByZero() throws Exception {
    check('1', "1");
    check('/', "1");
    check('0', "0");
    check('=', "Division by zero");
  }

  @Test
  public void dotIsSupported() {
    check('1', "1");
    check('.', "1.");
    check('1', "1.1");
    check('+', "1.1");
    check('1', "1");
    check('.', "1.");
    check('2', "1.2");
    check('=', "2.3");
  }

  @Test
  public void dotIsIgnoredIfEnteredTwice() {
    check('.', "0.");
    check('.', "0.");
    check('1', "0.1");
    check('.', "0.1");
  }

  @Test
  public void dotWorksWhenZeroIsOmitted() {
    check('.', "0.");
    check('1', "0.1");
    check('*', "0.1");
    check('2', "2");
    check('.', "2.");
    check('1', "2.1");
    check('=', "0.21");
  }

  @Test
  public void cancelReturnsToInitialState() {
    check('1', "1");
    check('C', "0");
    check('3', "3");
    check('+', "3");
    check('C', "0");
    check('=', "0");
    check('3', "3");
    check('+', "3");
    check('3', "3");
    check('C', "0");
    check('=', "0");
  }

  @Test
  public void invalidChar() throws Exception {
    try {
      calculator.enter('A');
      throw new AssertionFailureNotDetectedError();
    }
    catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Character 'A' is not supported");
    }
  }

  private void check(char c, String expectedValue) {
    calculator.enter(c);
    assertEquals(calculator.getCurrentValue(), expectedValue);
  }
}
