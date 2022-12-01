package ca.bc.gov.hlth.hnweb.model.v3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class Address {

  // values for addressChoosedByUser
  public static final String ADDRESS_USE_SUBMITTED = "ADDRESS_USE_SUBMITTED";
  public static final String ADDRESS_USE_BEST_GUESS = "ADDRESS_USE_BEST_GUESS";

  public static final String PHYSICAL_ADDRESS_TYPE = "PHYS";
  public static final String MAILING_ADDRESS_TYPE = "PST";
  public static final String PHYSICAL_ADDRESS_TYPE_FOR_DISPLAY = "Physical";
  public static final String MAILING_ADDRESS_TYPE_FOR_DISPLAY = "Mailing";

  private String addressType;
  private String addressLine1;
  private String addressLine2;
  private String addressLine3;
  private String city;
  private String province;
  private String postalCode;
  private String country;

  private String effectiveDate;

  /** if the address returned from HCIM is valid Canadian address, ver is set to true */
  private boolean ver = false;
  /** Used to store the first address returned by AddressDoctor */
  private Address bestGuessAddress;

  private boolean sameAsBestGuessAddress = false;

  private String addressChoosedByUser = ADDRESS_USE_SUBMITTED;

  private boolean masked = false;

  public void onlyKeepWhatEnteredFromPageAndAddressFormat() {
    this.effectiveDate = null;
    this.masked = false;
    this.ver = false;
    this.addressChoosedByUser = ADDRESS_USE_SUBMITTED;
  }

  /** Constructor. */
  public Address() {}

  /**
   * Constructor
   *
   * @param addressType String
   * @param addressLine1 String
   * @param addressLine2 String
   * @param addressLine3 String
   * @param city String
   * @param province String
   * @param postalCode String
   * @param country String
   * @param ver boolean
   */
  public Address(
      String addressType,
      String addressLine1,
      String addressLine2,
      String addressLine3,
      String city,
      String province,
      String postalCode,
      String country,
      boolean ver) {

    this.addressType = addressType;
    this.addressLine1 = addressLine1;
    this.addressLine2 = addressLine2;
    this.addressLine3 = addressLine3;
    this.city = city;
    this.province = province;
    this.postalCode = postalCode;
    this.country = country;
    this.ver = ver;
  }

  public Address cloneAddress() {

    Address tempAddress = new Address();

    tempAddress.setAddressType(addressType);
    tempAddress.setAddressLine1(addressLine1);
    tempAddress.setAddressLine2(addressLine2);
    tempAddress.setAddressLine3(addressLine3);
    tempAddress.setCity(city);
    tempAddress.setPostalCode(postalCode);
    tempAddress.setProvince(province);
    tempAddress.setCountry(country);
    tempAddress.setVer(ver);

    return tempAddress;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == null || !(obj instanceof Address)) {
      return false;
    }
    Address addressToCheck = (Address) obj;

    // StringUtils to be null safe
    if (!StringUtils.equalsIgnoreCase(this.addressLine1, addressToCheck.getAddressLine1())
        || !StringUtils.equalsIgnoreCase(this.addressLine2, addressToCheck.getAddressLine2())
        || !StringUtils.equalsIgnoreCase(this.addressLine3, addressToCheck.getAddressLine3())
        || !StringUtils.equalsIgnoreCase(this.city, addressToCheck.getCity())
        || !StringUtils.equalsIgnoreCase(this.postalCode, addressToCheck.getPostalCode())
        || !StringUtils.equalsIgnoreCase(this.province, addressToCheck.getProvince())
        || !StringUtils.equalsIgnoreCase(this.country, addressToCheck.getCountry())
        || !this.ver == addressToCheck.isVer()) {

      return false;
    }

    return true;
  }

  /**
   * Checks if the bestGuessAddress has been set.
   *
   * @return boolean
   */
  public boolean isBestGuessAddressReturned() {
    return this.bestGuessAddress != null;
  }

  /**
   * Return true if all all fields are blank.
   *
   * @return
   */
  public boolean isBlankAddress() {
    if (StringUtils.isBlank(this.addressLine1)
        && StringUtils.isBlank(this.addressLine2)
        && StringUtils.isBlank(this.addressLine3)
        && StringUtils.isBlank(this.city)
        && StringUtils.isBlank(this.postalCode)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns true if address fields have been entered by user.
   *
   * @return boolean
   */
  public boolean hasAddressBeenEntered() {
    if (StringUtils.isNoneBlank(addressLine1)
        || StringUtils.isNoneBlank(addressLine2)
        || StringUtils.isNoneBlank(addressLine3)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Return true if the country is CA or CANADA
   *
   * @return boolean
   */
  public boolean isCountryCanada() {
    return country.equals("CA") || country.equals("CANADA");
  }

  /**
   * Return if user has entered an address. The decision is based if the address line1 has value.
   *
   * @return
   */
  public boolean isAddressEntered() {
    if (StringUtils.isNotBlank(this.addressLine1)) {
      return true;
    } else {
      return false;
    }
  }

  public String getDisplayText() {
    StringBuilder sb = new StringBuilder();
    if (addressLine1 != null && !addressLine1.equals("")) {
      sb.append(addressLine1);
    }
    if (addressLine2 != null && !addressLine2.equals("")) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append(addressLine2);
    }
    if (addressLine3 != null && !addressLine3.equals("")) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append(addressLine3);
    }
    if (city != null && !city.equals("")) {
      sb.append(", " + city);
    }
    if (province != null && !province.equals("")) {
      sb.append(", " + province);
    }
    if (country != null && !country.equals("")) {
      sb.append(", " + country);
    }
    if (postalCode != null && !postalCode.equals("")) {
      sb.append(", " + postalCode);
    }

    return sb.toString();
  }

  /**
   * Remove the default values for country/province and displays an empty address for default
   * selections.
   *
   * @return String
   */
  public String getDisplayAddressNoCountryProv() {
    StringBuilder sb = new StringBuilder();
    if (((", CA".equals(getDisplayText()))
            || (", BC".equals(getDisplayText()))
            || (", BC, CA".equals(getDisplayText())))
        && !getDisplayText().equals("")) {

      if (", CA".equals(getDisplayText())) {
        sb.append(getDisplayText().replaceAll(", CA", ""));
      }
      if (", BC".equals(getDisplayText())) {
        sb.append(getDisplayText().replaceAll(", BC", ""));
      }
      if (", BC, CA".equals(getDisplayText())) {
        sb.append(getDisplayText().replaceAll(", BC, CA", ""));
      }
    } else if (getDisplayText().equals("")) {
      sb.append("");
    } else {
      sb.append(getDisplayText());
    }
    return sb.toString();
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

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public String getAddressLine3() {
    return addressLine3;
  }

  public void setAddressLine3(String addressLine3) {
    this.addressLine3 = addressLine3;
  }

  public String getAddressType() {
    return addressType;
  }

  public String getAddressTypeForDisplay() {
    if (addressType != null) {
      if (addressType.compareToIgnoreCase(PHYSICAL_ADDRESS_TYPE) == 0) {
        return PHYSICAL_ADDRESS_TYPE_FOR_DISPLAY;
      } else if (addressType.compareToIgnoreCase(MAILING_ADDRESS_TYPE) == 0) {
        return MAILING_ADDRESS_TYPE_FOR_DISPLAY;
      }
    }
    return "";
  }

  public void setAddressType(String addressType) {
    this.addressType = addressType;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(String effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public boolean isMasked() {
    return masked;
  }

  public void setMasked(boolean masked) {
    this.masked = masked;
  }

  public boolean isVer() {
    return ver;
  }

  public void setVer(boolean ver) {
    this.ver = ver;
  }

  public String getAddressChoosedByUser() {
    return addressChoosedByUser;
  }

  public void setAddressChoosedByUser(String addressChoosedByUser) {
    this.addressChoosedByUser = addressChoosedByUser;
  }

  public Address getBestGuessAddress() {
    return bestGuessAddress;
  }

  public void setBestGuessAddress(Address bestGuessAddress) {
    this.bestGuessAddress = bestGuessAddress;
  }

  public boolean isSameAsBestGuessAddress() {
    return sameAsBestGuessAddress;
  }

  public void setSameAsBestGuessAddress(boolean sameAsBestGuessAddress) {
    this.sameAsBestGuessAddress = sameAsBestGuessAddress;
  }

  @Override
  public String toString() {
    return getDisplayText();
  }
}