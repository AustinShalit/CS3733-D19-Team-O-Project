package edu.wpi.cs3733.d19.teamO.entity;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Login {

  public enum EmployeeType {
    DEFAULT("Default"),
    ADMIN("Admin"),
    SANITATION("Sanitation"),
    SECURITY("Security");

    private static final Map<String, EmployeeType> lookup
        = new ConcurrentHashMap<>();

    static {
      for (Login.EmployeeType type : values()) {
        lookup.put(type.name(), type);
      }
    }

    private final String name;

    EmployeeType(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

    public static Login.EmployeeType get(final String name) {
      return lookup.get(name);
    }
  }

  private final int id;
  private String username;
  private String password;
  private String name;
  private EmployeeType type;

  /**
   * Constructor for Login, Create a Login.
   *
   * @param id       Login's integer ID
   * @param username Username
   * @param password Password
   * @param name     User's Name
   * @param type     Position Type
   */
  public Login(int id, String username, String password, String name, EmployeeType type) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.name = name;
    this.type = type;
  }

  /**
   * Constructor for Login, Initialization of Login.
   *
   * @param username Username
   * @param password Password
   * @param name     User's Name
   * @param type     Position Type
   */
  public Login(String username, String password, String name, EmployeeType type) {
    this.id = -1;
    this.username = username;
    this.password = password;
    this.name = name;
    this.type = type;
  }

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getName() {
    return name;
  }

  public EmployeeType getType() {
    return type;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setType(EmployeeType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Login{"
        + "id=" + getId()
        + ", username=" + getUsername()
        + ", password=" + getPassword()
        + ", user=" + getName()
        + ", type='" + type + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Login that = (Login) o;
    return getId() == that.getId()
        && username.equals(that.username)
        && password.equals(that.password)
        && name.equals(that.name)
        && type == that.type;
  }

  /**
   * Function to check the username and password.
   *
   * @param o Object
   */
  public boolean equalsLog(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Login that = (Login) o;
    return username.equals(that.username)
        && password.equals(that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, name, type);
  }
}
