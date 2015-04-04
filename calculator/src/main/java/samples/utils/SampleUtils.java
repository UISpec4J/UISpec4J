package samples.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SampleUtils {

  public static void show(JComponent panel, String title) throws Exception {
    JFrame frame = createFrame(panel);
    frame.setTitle(title);
    show(frame);
  }

  public static void show(Window window) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    window.pack();
    window.setVisible(true);
  }

  public static JFrame createFrame(JComponent component) {
    JFrame frame = new JFrame();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frame.getContentPane().add(component);
    return frame;
  }

  public static JPanel createPanelWithScroller(JComponent component) {
    JPanel panel = new JPanel();
    JScrollPane scroller = new JScrollPane();
    scroller.getViewport().add(component);
    scroller.getViewport().setMinimumSize(new Dimension(30, 30));
    GridBag gridBag = new GridBag(panel);
    gridBag.add(scroller,
                0, 0, 1, 1, 1, 1,
                GridBagConstraints.BOTH,
                GridBagConstraints.CENTER,
                new Insets(0, 0, 0, 0));
    return panel;
  }
}
