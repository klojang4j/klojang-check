package nl.naturalis.common.collection;

import nl.naturalis.common.ClassMethods;
import nl.naturalis.common.x.collection.PrettyTypeComparator;
import org.junit.Ignore;
import org.junit.Test;

import java.io.Closeable;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PrettyTypeComparatorTest {

  @Test
  @Ignore
  public void test00() {
    List<Class<?>> orig = List.of(char.class, int.class, Double.class, Integer.class, Month.class, MyArrayList2.class, MyArrayList.class, ArrayList.class, Enum.class, Number.class, Closeable.class, AutoCloseable.class, String[].class, CharSequence[].class, Object[].class, Object.class);
    List<Class<?>> types = new ArrayList<>(orig);
    for (int i = 0; i < 200; ++i) {
      Collections.shuffle(types);
      Collections.sort(types, new PrettyTypeComparator());
      System.out.println(types);
      assertEquals(orig, types);
    }
  }

  @Test
  public void test01() {
    assertTrue(ClassMethods.isSubtype(String[].class, Object.class));
  }

}
