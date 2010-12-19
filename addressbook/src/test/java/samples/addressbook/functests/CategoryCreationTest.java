package samples.addressbook.functests;

import org.uispec4j.Key;
import org.uispec4j.Trigger;
import org.uispec4j.interception.BasicHandler;
import org.uispec4j.interception.WindowInterceptor;

public class CategoryCreationTest extends AddressBookTestCase {

  public void testTreeStateAtStartup() throws Exception {
    assertThat(categoryTree.contentEquals("All"));
    assertThat(categoryTree.selectionEquals(""));
  }

  public void testCategoryCannotBeCreatedIfThereIsNoSelectionInTree() throws Exception {
    categoryTree.clearSelection();
    assertFalse(newCategoryButton.isEnabled());
  }

  public void testCreatingACategory() throws Exception {
    assertThat(categoryTree.contentEquals("All"));

    createCategory("", "friends");
    assertThat(categoryTree.contentEquals("All\n" +
                                          "  friends"));
    assertThat(categoryTree.pathIsExpanded(""));

    createCategory("", "work");
    assertThat(categoryTree.contentEquals("All\n" +
                                          "  friends\n" +
                                          "  work"));
    assertThat(categoryTree.pathIsExpanded(""));

    createCategory("friends", "Homer");
    assertThat(categoryTree.contentEquals("All\n" +
                                          "  friends\n" +
                                          "    Homer\n" +
                                          "  work"));
    assertThat(categoryTree.pathIsExpanded("friends"));
  }

  public void testCreatingACategoryThroughKeyStroke() throws Exception {
    assertThat(categoryTree.contentEquals("All"));

    createCategory("", "friends", new Trigger() {
      public void run() throws Exception {
        getMainWindow().typeKey(Key.control(Key.N));
      }
    });
    assertThat(categoryTree.contentEquals("All\n" +
                                          "  friends"));
  }

  public void testCreatingACategoryThatAlreadyExists() throws Exception {
    createCategory("", "work");
    assertThat(categoryTree.contentEquals("All\n" +
                                          "  work"));

    WindowInterceptor
      .init(new Trigger() {
        public void run() throws Exception {
          createCategory("", "work");
        }
      })
      .process(BasicHandler.init()
        .assertContainsText("Category 'work' already exists")
        .triggerButtonClick("OK"))
      .run();

    assertThat(categoryTree.contentEquals("All\n" +
                                          "  work"));
  }
}
