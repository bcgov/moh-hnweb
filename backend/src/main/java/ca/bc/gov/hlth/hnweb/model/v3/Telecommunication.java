package ca.bc.gov.hlth.hnweb.model.v3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Telecommunication {

  public static final String PHONE_NUMBER_PATTERN = "[0-9-]+";

  private String type;
  private String telecommunicationNumber;
  private String effectiveDate;
  private boolean masked = false;

  public Telecommunication() {}

  public Telecommunication cloneTelecom() {
    Telecommunication tempTelecom = new Telecommunication();

    tempTelecom.setTelecommunicationNumber(this.telecommunicationNumber);
    tempTelecom.setType(this.type);
    tempTelecom.setMasked(this.masked);
    tempTelecom.setEffectiveDate(this.effectiveDate);

    return tempTelecom;
  }

  public String getDisplayText() {
    return telecommunicationNumber;
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

  public String getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(String effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  public String getTelecommunicationNumber() {
    return telecommunicationNumber == null
        ? null
        : (telecommunicationNumber.replaceAll("tel:", "").replaceAll("mailto:", ""));
  }

  public void setTelecommunicationNumber(String telecommunicationNumber) {
    if (telecommunicationNumber != null) {
      this.telecommunicationNumber =
          telecommunicationNumber.replaceAll("tel:", "").replaceAll("mailto:", "");
    }
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isMasked() {
    return masked;
  }

  public void setMasked(boolean masked) {
    this.masked = masked;
  }
}
