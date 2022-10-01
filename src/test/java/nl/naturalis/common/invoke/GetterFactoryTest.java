package nl.naturalis.common.invoke;

import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GetterFactoryTest {

  @Test
  public void test00() {
    GetterFactory gf = GetterFactory.INSTANCE;
    Map<String, Getter> getters = gf.getGetters(TestRecord.class, true);
    assertEquals(Set.of("foo", "bar", "bozo"), getters.keySet());
  }

  @Test
  public void test01() {
    GetterFactory gf = GetterFactory.INSTANCE;
    Map<String, Getter> getters = gf.getGetters(TestRecord.class, false);
    assertEquals(Set.of("foo", "bar", "bozo"), getters.keySet());
  }

}
