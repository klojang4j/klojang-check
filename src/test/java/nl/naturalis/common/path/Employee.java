package nl.naturalis.common.path;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class Employee extends Person {

  private int id;
  private double salary;
  private int[] birthDate;
  private URL twitter;
  private URL facebook;
  private Map<Object, Object> extraInfo;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }

  public int[] getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(int[] birthDate) {
    this.birthDate = birthDate;
  }

  public URL getTwitter() {
    return twitter;
  }

  public void setTwitter(URL twitter) {
    this.twitter = twitter;
  }

  public URL getFacebook() {
    return facebook;
  }

  public void setFacebook(URL facebook) {
    this.facebook = facebook;
  }

  public Map<Object, Object> getExtraInfo() {
    return extraInfo;
  }

  public void setExtraInfo(Map<Object, Object> extraInfo) {
    this.extraInfo = extraInfo;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Arrays.hashCode(birthDate);
    result = prime * result + Objects.hash(extraInfo, facebook, id, salary, twitter);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Employee other = (Employee) obj;
    return Arrays.equals(birthDate, other.birthDate)
        && Objects.equals(extraInfo, other.extraInfo)
        && Objects.equals(facebook, other.facebook)
        && id == other.id
        && Double.doubleToLongBits(salary) == Double.doubleToLongBits(other.salary)
        && Objects.equals(twitter, other.twitter);
  }
}
