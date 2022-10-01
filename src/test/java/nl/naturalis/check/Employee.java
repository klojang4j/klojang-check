package nl.naturalis.check;

import java.util.Arrays;
import java.util.List;

class Employee {
  int id;
  String fullName;
  Integer age;
  List<String> hobbies;
  float[] scores;

  public String toString() {
    return fullName + " (" + id + ")";
  }

  int getId() {
    return id;
  }

  void setId(int id) {
    this.id = id;
  }

  String getFullName() {
    return fullName;
  }

  void setFullName(String fullName) {
    this.fullName = fullName;
  }

  Integer getAge() {
    return age;
  }

  void setAge(Integer age) {
    this.age = age;
  }

  List<String> getHobbies() {
    return hobbies;
  }

  void setHobbies(List<String> hobbies) {
    this.hobbies = hobbies;
  }

  void setHobbies(String... hobbies) {
    this.hobbies = Arrays.asList(hobbies);
  }

  float[] getScores() {
    return scores;
  }

  void setScores(float[] scores) {
    this.scores = scores;
  }
}
