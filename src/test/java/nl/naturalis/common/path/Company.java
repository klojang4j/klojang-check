package nl.naturalis.common.path;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Company {

  private String name;
  private BigDecimal sales;
  private float profit;
  private float[][] quarterlySales;
  private List<Department> departments;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getSales() {
    return sales;
  }

  public void setSales(BigDecimal sales) {
    this.sales = sales;
  }

  public float getProfit() {
    return profit;
  }

  public void setProfit(float profit) {
    this.profit = profit;
  }

  public float[][] getQuarterlySales() {
    return quarterlySales;
  }

  public void setQuarterlySales(float[][] quarterlySales) {
    this.quarterlySales = quarterlySales;
  }

  public List<Department> getDepartments() {
    return departments;
  }

  public void setDepartments(List<Department> departments) {
    this.departments = departments;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.deepHashCode(quarterlySales);
    result = prime * result + Objects.hash(departments, name, profit, sales);
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
    Company other = (Company) obj;
    return Objects.equals(departments, other.departments)
        && Objects.equals(name, other.name)
        && Float.floatToIntBits(profit) == Float.floatToIntBits(other.profit)
        && Arrays.deepEquals(quarterlySales, other.quarterlySales)
        && Objects.equals(sales, other.sales);
  }
}
