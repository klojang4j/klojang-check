package nl.naturalis.common.path;

import java.util.Objects;

public class Address {

  private String street;
  private Integer streetNo;
  private String zip;
  private String city;

  public Address(String street, Integer streetNo, String zip, String city) {
    this.street = street;
    this.streetNo = streetNo;
    this.zip = zip;
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public Integer getStreetNo() {
    return streetNo;
  }

  public void setStreetNo(Integer streetNo) {
    this.streetNo = streetNo;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public int hashCode() {
    return Objects.hash(city, street, streetNo, zip);
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
    Address other = (Address) obj;
    return Objects.equals(city, other.city)
        && Objects.equals(street, other.street)
        && Objects.equals(streetNo, other.streetNo)
        && Objects.equals(zip, other.zip);
  }
}
