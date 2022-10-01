package nl.naturalis.common.path;

import java.util.Objects;

public class Person {

  private String ssn;
  private String firstName;
  private String lastName;
  private Address address;
  private String naughtyProperty;

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getNaughtyProperty() {
    throw new RuntimeException("I always make a mess");
  }

  public void setNaughtyProperty(String naughtyProperty) {
    throw new RuntimeException("I always make a mess");
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, firstName, lastName, ssn);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Person other = (Person) obj;
    return Objects.equals(address, other.address)
        && Objects.equals(firstName, other.firstName)
        && Objects.equals(lastName, other.lastName)
        && Objects.equals(ssn, other.ssn);
  }

}
