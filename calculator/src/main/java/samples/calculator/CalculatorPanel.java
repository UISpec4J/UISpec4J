package samples.calculator;

import samples.utils.GridBag;
import samples.utils.SampleUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorPanel extends JPanel {

  Calculator calculator = new Calculator();
  JTextField textField = new JTextField();

  public CalculatorPanel() {
    GridBag gridBag = new GridBag(this);
    updateTextField();
    textField.setHorizontalAlignment(JTextField.RIGHT);
    textField.setEditable(false);
    textField.setBackground(Color.WHITE);
    gridBag.add(textField, 0, 0, 3, 1, 1, 1,
                GridBagConstraints.BOTH, GridBagConstraints.NORTH,
                new Insets(5, 5, 5, 5));
    gridBag.add(createButton('C'),
                3, 0, 1, 1, 1, 1,
                GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH,
                new Insets(5, 5, 5, 5));
    addButtons(gridBag,
               new char[][]{
                 {'7', '8', '9', '+'},
                 {'4', '5', '6', '-'},
                 {'1', '2', '3', '*'},
                 {'0', '.', 0, '/'}
               });
    gridBag.add(createButton('='),
                2, 5, 2, 1, 1, 1,
                GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH,
                new Insets(5, 5, 5, 5));
  }

  private void addButtons(GridBag gridBag, char[][] chars) {
    for (int colIndex = 0; colIndex < chars.length; colIndex++) {
      char[] row = chars[colIndex];
      for (int rowIndex = 0; rowIndex < row.length; rowIndex++) {
        if (chars[colIndex][rowIndex] != 0) {
          gridBag.add(createButton(row[rowIndex]),
                      rowIndex, colIndex + 1, 1, 1, 1, rowIndex == 3 ? 10 : 1,
                      GridBagConstraints.BOTH, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5));
        }
      }
    }
  }

  private JButton createButton(final char buttonChar) {
    JButton button = new JButton(String.valueOf(buttonChar));
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        calculator.enter(buttonChar);
        updateTextField();
      }
    });
    return button;
  }

  private void updateTextField() {
    textField.setText(calculator.getCurrentValue());
  }

  public static void main(String[] args) throws Exception {
    SampleUtils.show(new CalculatorPanel(), "Calculator");
  }
}
