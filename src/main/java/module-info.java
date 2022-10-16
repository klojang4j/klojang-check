/**
 * Java module containing basic language extensions and utility classes. The module
 * is light-weight and self-contained, with zero dependencies outside
 * {@code java.base}. It is one of the main utility libraries underneath the Klojang
 * templating API (yet to be published on Maven Central). It centers around the
 * functionality described below.
 *
 * <h2>Naturalis Check</h2>
 *
 * @author Ayco Holleman
 */
module nl.naturalis.check {
  exports nl.naturalis.check;
  exports nl.naturalis.check.util;
  exports nl.naturalis.check.function;
  exports nl.naturalis.check.relation;
}
