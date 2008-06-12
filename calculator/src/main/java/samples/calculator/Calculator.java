package samples.calculator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Calculator {

  private State state;
  private StringBuffer firstOperand = new StringBuffer();
  private Operator operator;
  private StringBuffer secondOperand = new StringBuffer();
  public static final DecimalFormat FORMAT = new DecimalFormat("0.########");
  private static final int MAX_CHARATER_COUNT = 10;
  private NumberFormat numberFormat;

  public Calculator() {
    state = new EnteringFirstOperandState('0');
    numberFormat = NumberFormat.getInstance(Locale.ENGLISH);
  }

  public void enter(char c) {
    switch (c) {
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        state.processDigit(c);
        break;
      case '.':
        state.processDot();
        break;
      case '+':
      case '-':
      case '/':
      case '*':
        state.processOperator(c);
        break;
      case '=':
        state.processEquals();
        break;
      case 'C':
        state = new EnteringFirstOperandState('0');
        break;
      default:
        throw new IllegalArgumentException("Character '" + c + "' is not supported");
    }
  }

  public String getCurrentValue() {
    return state.getCurrentValue();
  }

  private interface Operator {
    public BigDecimal apply(BigDecimal a, BigDecimal b);
  }

  private interface State {
    void processDigit(char c);

    void processOperator(char c);

    void processEquals();

    String getCurrentValue();

    void processDot();
  }

  public abstract class AbstractOperandState implements State {
    private StringBuffer buffer;

    protected AbstractOperandState(StringBuffer buffer, char c) {
      this.buffer = buffer;
      buffer.setLength(0);
      buffer.append(c);
    }

    public final void processDigit(char c) {
      if (buffer.toString().length() == MAX_CHARATER_COUNT) {
        return;
      }
      if (buffer.toString().equals("0")) {
        buffer.setCharAt(0, c);
      }
      else {
        buffer.append(c);
      }
    }

    public final String getCurrentValue() {
      return buffer.toString();
    }

    public void processDot() {
      if (buffer.toString().length() == MAX_CHARATER_COUNT) {
        return;
      }
      if (buffer.toString().indexOf(".") == -1) {
        buffer.append(".");
      }
    }
  }

  public class EnteringFirstOperandState extends AbstractOperandState {
    public EnteringFirstOperandState(char c) {
      super(firstOperand, c);
    }

    public void processOperator(char c) {
      state = new EnteringOperatorState(c);
    }

    public void processEquals() {
    }
  }

  public class EnteringOperatorState implements State {
    public EnteringOperatorState(char c) {
      processOperator(c);
    }

    public void processDigit(char c) {
      state = new EnteringSecondOperandState(c);
    }

    public String getCurrentValue() {
      return firstOperand.toString();
    }

    public void processDot() {
      state = new EnteringSecondOperandState('0');
      state.processDot();
    }

    public void processOperator(char c) {
      switch (c) {
        case '+':
          operator = OPERATOR_PLUS;
          break;
        case '-':
          operator = OPERATOR_MINUS;
          break;
        case '/':
          operator = OPERATOR_DIVIDE;
          break;
        case '*':
          operator = OPERATOR_TIMES;
          break;
        default:
          throw new RuntimeException("unexpected operator");
      }
    }

    public void processEquals() {
      secondOperand.setLength(0);
      secondOperand.append(firstOperand);
      state = new ShowingResultState();
    }
  }

  public class EnteringSecondOperandState extends AbstractOperandState {
    public EnteringSecondOperandState(char c) {
      super(secondOperand, c);
    }

    public void processOperator(char c) {
      applyOperator();
      state = new EnteringOperatorState(c);
    }

    public void processEquals() {
      state = new ShowingResultState();
    }
  }

  public class ShowingResultState implements State {

    public ShowingResultState() {
      applyOperator();
    }

    public void processDigit(char c) {
      firstOperand.setLength(0);
      secondOperand.setLength(0);
      firstOperand.append('0');
      state = new EnteringFirstOperandState(c);
    }

    public void processOperator(char c) {
      state = new EnteringOperatorState(c);
    }

    public void processEquals() {
      applyOperator();
    }

    public String getCurrentValue() {
      return firstOperand.toString();
    }

    public void processDot() {
      processDigit('0');
      state.processDot();
    }
  }

  private void applyOperator() {
    if (operator == null) {
      throw new RuntimeException("No operator defined");
    }
    BigDecimal firstValue = new BigDecimal(firstOperand.toString());
    BigDecimal secondValue = new BigDecimal(secondOperand.toString());
    try {
      BigDecimal result = operator.apply(firstValue, secondValue);
      firstOperand = new StringBuffer(numberFormat.format(result.doubleValue()));
    }
    catch (Exception e) {
      firstOperand = new StringBuffer(e.getMessage());
    }
  }

  private final Operator OPERATOR_PLUS = new Operator() {
    public BigDecimal apply(BigDecimal a, BigDecimal b) {
      return a.add(b);
    }
  };
  private final Operator OPERATOR_MINUS = new Operator() {
    public BigDecimal apply(BigDecimal a, BigDecimal b) {
      return a.subtract(b);
    }
  };
  private final Operator OPERATOR_DIVIDE = new Operator() {
    public BigDecimal apply(BigDecimal a, BigDecimal b) {
      if (b.intValue() == 0) {
        throw new ArithmeticException("Division by zero");
      }
      return a.divide(b, 5, BigDecimal.ROUND_HALF_DOWN);
    }
  };
  private final Operator OPERATOR_TIMES = new Operator() {
    public BigDecimal apply(BigDecimal a, BigDecimal b) {
      return a.multiply(b);
    }
  };
}
