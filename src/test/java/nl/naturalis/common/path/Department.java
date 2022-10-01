package nl.naturalis.common.path;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Department {

  private String name;
  private Address address;
  private String[] telNos;
  private Employee manager;
  private List<Employee> employees;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String[] getTelNos() {
    return telNos;
  }

  public void setTelNos(String[] telNos) {
    this.telNos = telNos;
  }

  public Employee getManager() {
    return manager;
  }

  public void setManager(Employee manager) {
    this.manager = manager;
  }

  public List<Employee> getEmployees() {
    return employees;
  }

  public void setEmployees(List<Employee> employees) {
    this.employees = employees;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(telNos);
    result = prime * result + Objects.hash(address, employees, manager, name);
    return result;
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
    Department other = (Department) obj;
    return Objects.equals(address, other.address)
        && Objects.equals(employees, other.employees)
        && Objects.equals(manager, other.manager)
        && Objects.equals(name, other.name)
        && Arrays.equals(telNos, other.telNos);
  }
}
