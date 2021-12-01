package ca.bc.gov.hlth.hnweb.model.v3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class Name {

  private String type;
  private String surname;
  private String firstGivenName;
  private String secondGivenName;
  private String thirdGivenName;
  private String preferredName;
  private String effectiveDate;

  public Name() {}

  public Name cloneName() {

    Name tempName = new Name();
    tempName.setFirstGivenName(this.firstGivenName);
    tempName.setSecondGivenName(this.secondGivenName);
    tempName.setThirdGivenName(this.thirdGivenName);
    tempName.setPreferredName(this.preferredName);
    tempName.setSurname(this.surname);
    tempName.setType(this.type);

    return tempName;
  }

  public void upperCaseAllNames() {
    if (surname != null) {
      surname = surname.toUpperCase();
    }
    if (firstGivenName != null) {
      firstGivenName = firstGivenName.toUpperCase();
    }
    if (secondGivenName != null) {
      secondGivenName = secondGivenName.toUpperCase();
    }
    if (thirdGivenName != null) {
      thirdGivenName = thirdGivenName.toUpperCase();
    }
    if (preferredName != null) {
      preferredName = preferredName.toUpperCase();
    }
  }

  public String getDisplayText() {
    StringBuilder result = new StringBuilder();
    if (surname != null
        && !surname.isEmpty()
        && firstGivenName != null
        && !firstGivenName.isEmpty()) {
      result.append(surname).append(", ").append(firstGivenName);
    } else {
      return result.toString();
    }
    if (secondGivenName != null && !"".equals(secondGivenName)) {
      result.append(", " + secondGivenName);
    }
    if (thirdGivenName != null && !"".equals(thirdGivenName)) {
      result.append(", " + thirdGivenName);
    }
    if (preferredName != null && !"".equals(preferredName)) {
      result.append(" [" + preferredName + "]");
    }
    return result.toString();
  }

  public String getDisplayTextSurnameGivenNames() {
    StringBuilder result = new StringBuilder();
    if (surname != null && firstGivenName != null) {
      result.append(surname + ", " + firstGivenName);
    }
    if (secondGivenName != null && !"".equals(secondGivenName)) {
      result.append(", " + secondGivenName);
    }
    if (thirdGivenName != null && !"".equals(thirdGivenName)) {
      result.append(", " + thirdGivenName);
    }
    return result.toString();
  }

  public String getDisplayTextGivenNames() {
    StringBuilder result = new StringBuilder();
    if (firstGivenName != null) {
      result.append(firstGivenName);
    }
    if (secondGivenName != null && !"".equals(secondGivenName)) {
      result.append(", " + secondGivenName);
    }
    if (thirdGivenName != null && !"".equals(thirdGivenName)) {
      result.append(", " + thirdGivenName);
    }
    return result.toString();
  }

  /**
   * Parses and displays effective date using time format from HCIM system.
   *
   * @return Date parsedDate throws ParseException pex
   */
  public Date getDisplayEffectiveDate() throws ParseException {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
      Date parsedDate = sdf.parse(this.effectiveDate);
      return parsedDate;
    } catch (ParseException pex) {
      pex.getStackTrace();
    }
    return new Date();
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == null || !(obj instanceof Name)) {
      return false;
    }
    Name nameToCheck = (Name) obj;

    // StringUtils to be null safe
    if (!StringUtils.equals(this.firstGivenName, nameToCheck.getFirstGivenName())
        || !StringUtils.equals(this.secondGivenName, nameToCheck.getSecondGivenName())
        || !StringUtils.equals(this.thirdGivenName, nameToCheck.getThirdGivenName())
        || !StringUtils.equals(this.surname, nameToCheck.getSurname())
        || !StringUtils.equals(this.type, nameToCheck.getType())) {

      return false;
    }

    return true;
  }

  public String getFirstGivenName() {
    return firstGivenName;
  }

  public void setFirstGivenName(String firstGivenName) {
    this.firstGivenName = firstGivenName;
  }

  public String getPreferredName() {
    return preferredName;
  }

  public void setPreferredName(String preferredName) {
    this.preferredName = preferredName;
  }

  public String getSecondGivenName() {
    return secondGivenName;
  }

  public void setSecondGivenName(String secondGivenName) {
    this.secondGivenName = secondGivenName;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getThirdGivenName() {
    return thirdGivenName;
  }

  public void setThirdGivenName(String thirdGivenName) {
    this.thirdGivenName = thirdGivenName;
  }
}