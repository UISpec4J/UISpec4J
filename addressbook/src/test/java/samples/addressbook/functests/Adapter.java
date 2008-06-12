package samples.addressbook.functests;

import org.uispec4j.Trigger;
import org.uispec4j.UISpecAdapter;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;
import samples.addressbook.main.Main;

public class Adapter implements UISpecAdapter {
  private Window window;

  public Window getMainWindow() {
    if (window == null) {
      window = WindowInterceptor.run(new Trigger() {
        public void run() throws Exception {
          Main.main(new String[0]);
        }
      });
    }
    return window;
  }
}
