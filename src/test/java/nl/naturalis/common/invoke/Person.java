package nl.naturalis.common.invoke;

import java.time.LocalDate;
import java.util.List;

public class Person {

  private int id;
  private String firstName;
  private String lastName;
  private LocalDate lastModified;
  private List<String> hobbies;

  private double someDouble;
  private float someFloat;
  private long someLong;
  private int someInt;
  private short someShort;
  private char someChar;
  private byte someByte;
  private Short someShortWrapper;
  private Double someDoubleWrapper;
  private Number someNumber;
  private CharSequence someCharSequence;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public LocalDate getLastModified() {
    return lastModified;
  }

  public void setLastModified(LocalDate lastModified) {
    this.lastModified = lastModified;
  }

  public List<String> getHobbies() {
    return hobbies;
  }

  public void setHobbies(List<String> hobbies) {
    this.hobbies = hobbies;
  }

  public double getSomeDouble() {
    return someDouble;
  }

  public void setSomeDouble(double someDouble) {
    this.someDouble = someDouble;
  }

  public float getSomeFloat() {
    return someFloat;
  }

  public void setSomeFloat(float someFloat) {
    this.someFloat = someFloat;
  }

  public long getSomeLong() {
    return someLong;
  }

  public void setSomeLong(long someLong) {
    this.someLong = someLong;
  }

  public int getSomeInt() {
    return someInt;
  }

  public void setSomeInt(int someInt) {
    this.someInt = someInt;
  }

  public short getSomeShort() {
    return someShort;
  }

  public void setSomeShort(short someShort) {
    this.someShort = someShort;
  }

  public char getSomeChar() {
    return someChar;
  }

  public void setSomeChar(char someChar) {
    this.someChar = someChar;
  }

  public byte getSomeByte() {
    return someByte;
  }

  public void setSomeByte(byte someByte) {
    this.someByte = someByte;
  }

  public Short getSomeShortWrapper() {
    return someShortWrapper;
  }

  public void setSomeShortWrapper(Short someShortWrapper) {
    this.someShortWrapper = someShortWrapper;
  }

  public Double getSomeDoubleWrapper() {
    return someDoubleWrapper;
  }

  public void setSomeDoubleWrapper(Double someDoubleWrapper) {
    this.someDoubleWrapper = someDoubleWrapper;
  }

  public Number getSomeNumber() {
    return someNumber;
  }

  public void setSomeNumber(Number someNumber) {
    this.someNumber = someNumber;
  }

  public CharSequence getSomeCharSequence() {
    return someCharSequence;
  }

  public void setSomeCharSequence(CharSequence someCharSequence) {
    this.someCharSequence = someCharSequence;
  }
}
