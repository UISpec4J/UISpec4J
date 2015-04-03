package org.uispec4j.extension;

import org.objectweb.asm.ClassReader;
import org.uispec4j.AbstractUIComponent;
import org.uispec4j.Panel;
import org.uispec4j.UIComponent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Provides a means for extending UISpec4J to support user-defined components.
 * <p>This class offers a <code>main()</code> function which generates a JAR file containing extended
 * versions of some UISpec4J classes (for instance {@link Panel}).
 * This JAR file is intended to be placed before the UISpec4J JAR in your classpath.
 * <p>Arguments:
 * <pre><code>
 * &lt;path&gt; &lt;name:class&gt; &lt;name:class&gt; ...
 * </code></pre>
 * where:
 * <ul>
 * <li><code>&lt;output&gt;</code> is the path of the JAR file to be generated</li>
 * <li>each <code>&lt;name:class&gt;</code> defines an extension, where:
 * <ul>
 * <li><code>name</code> is the name of the component to be integrated</li>
 * <li><code>class</code> is the name of the component class</li>
 * </ul>
 * </li>
 * </ul>
 * For instance:
 * <pre><code>
 * java -cp ...  org.uispec4j.extension.ExtensionGenerator lib/uispec_ext.jar Calendar:com.xxx.Calendar
 * </code></pre>
 * <p>
 * The component class and the associated Swing class must adhere to certain conventions:
 * <ul>
 * <li>The Calendar class must implement {@link UIComponent} (or better yet extend
 * {@link AbstractUIComponent})</li>
 * <li>The Calendar class must declare a static field TYPE_NAME providing the related type name</li>
 * <li>The Calendar class must declare a static field SWING_CLASSES providing the related swing classes</li>
 * <li>The Calendar class must provide one public constructor with a JCalendar parameter</li>
 * <li>The JCalendar class must extend java.awt.Component</li>
 * <li>The given name ("calendar") must not be already used within UISpec4J.</li>
 * </ul>
 * For instance:
 * <pre><code>
 * public class Calendar extends AbstractUIComponent {
 *   public static final String TYPE_NAME = "calendar";
 *   public static final Class[] SWING_CLASSES = {JCalendar.class};
 *   public Calendar(JCalendar calendar) {
 *     ...
 *   }
 * }
 * </code></pre>
 *
 * @see <a href="http://www.uispec4j.org/custom-components">Adding Custom Components</a>
 */
public class ExtensionGenerator {

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.out.println("--- UISpec4J Extension Generator ---");
      System.out.println("Arguments: <path> <name:class> " +
                         "<name:class> ...");
      System.out.println("where:");
      System.out.println("  <path> is the path of the JAR file to be generated");
      System.out.println("  <name:class:swingClass> defines an extension, where:");
      System.out.println("  - name is the name of the component");
      System.out.println("  - class is the name of the component class");
      return;
    }

    File output = new File(args[0]);
    List<Extension> extensions = new ArrayList<Extension>();
    for (int i = 1; i < args.length; i++) {
      StringTokenizer tokenizer = new StringTokenizer(args[i], ":");
      String name = tokenizer.nextToken();
      String className = tokenizer.nextToken();
      extensions.add(new Extension(name, className));
    }
    run(output, extensions.toArray(new Extension[extensions.size()]));
  }

  private static void run(File output, Extension[] extensions) throws Exception {
      Class<Panel> panelClass = Panel.class;
      byte[] panelBytecode = PanelClassEnhancer.transformClass(createClassReader(panelClass), extensions);

    ZipOutputStream outputStream = null;
    try {
      outputStream = new ZipOutputStream(new FileOutputStream(output));
      outputStream.putNextEntry(new ZipEntry(computeClassPath(panelClass.getName())));
      outputStream.write(panelBytecode);
      outputStream.closeEntry();
    }
    finally {
      if (outputStream != null) {
        outputStream.close();
      }
    }
  }


  private static ClassReader createClassReader(Class clazz) throws IOException {
    return new ClassReader(ExtensionGenerator.class.getResourceAsStream(toClassFile(clazz)));
  }

  static String toClassFile(Class aClass) {
    String className = aClass.getName();
    return "/" + className.replace('.', '/') + ".class";
  }


    private static String computeClassPath(String className) {
    return className.replace('.', '/') + ".class";
  }
}
