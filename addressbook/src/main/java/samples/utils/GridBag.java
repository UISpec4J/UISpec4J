package samples.utils;

import java.awt.*;

public class GridBag {
  private Container panel;
  private GridBagLayout layout;

  public GridBag(Container panel) {
    this.panel = panel;
    layout = new GridBagLayout();
    panel.setLayout(layout);
  }

  public void add(Component component,
                  int gridx,
                  int gridy,
                  int gridwidth,
                  int gridheight,
                  double weightx,
                  double weighty,
                  int fill,
                  int anchor,
                  Insets insets) {
    GridBagConstraints constraint = new GridBagConstraints();
    constraint.gridx = gridx;
    constraint.gridy = gridy;
    constraint.gridwidth = gridwidth;
    constraint.gridheight = gridheight;
    constraint.fill = fill;
    constraint.anchor = anchor;
    constraint.weightx = weightx;
    constraint.weighty = weighty;
    if (insets != null) {
      constraint.insets = insets;
    }
    layout.setConstraints(component, constraint);
    panel.add(component);
  }
}
