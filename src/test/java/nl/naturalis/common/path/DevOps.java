package nl.naturalis.common.path;

import java.util.Arrays;
import java.util.Objects;

public class DevOps extends Department {

  private boolean hipsterFriendly;
  private int[][] reactiveBingoDates;

  public boolean isHipsterFriendly() {
    return hipsterFriendly;
  }

  public void setHipsterFriendly(boolean hipsterFriendly) {
    this.hipsterFriendly = hipsterFriendly;
  }

  public int[][] getReactiveBingoDates() {
    return reactiveBingoDates;
  }

  public void setReactiveBingoDates(int[][] reactiveBingoDates) {
    this.reactiveBingoDates = reactiveBingoDates;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Arrays.deepHashCode(reactiveBingoDates);
    result = prime * result + Objects.hash(hipsterFriendly);
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
    DevOps other = (DevOps) obj;
    return hipsterFriendly == other.hipsterFriendly
        && Arrays.deepEquals(reactiveBingoDates, other.reactiveBingoDates);
  }
}
