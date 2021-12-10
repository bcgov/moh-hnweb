package ca.bc.gov.hlth.hnweb.model.v3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class Person {

  private Date birthDate;
  private boolean birthDateMasked = false;
  private String deceased;
  private boolean deathConfirmed = false;
  private String eligibility;
  private Date deathDate;
  private String gender;
  private String phn;
  private String phnStatus;
  private boolean forceCreatePhn = false;
  private Name declaredName = new Name();
  private boolean declaredNameMasked = false;
  private Name documentedName = new Name();
  private boolean documentedNameMasked = false;
  private Address physicalAddress = new Address();
  private Address existingPhysicalAddress = new Address();
  private Address mailingAddress = new Address();
  private Address existingMailingAddress = new Address();

  private Telecommunication homePhone = new Telecommunication();
  private Telecommunication workPhone = new Telecommunication();
  private Telecommunication mobilePhone = new Telecommunication();
  private Telecommunication homeEmail = new Telecommunication();
  private Telecommunication workEmail = new Telecommunication();
  private Telecommunication mobileEmail = new Telecommunication();
  private List<MedicalRecordNumber> medicalRecordNumbers = new ArrayList<MedicalRecordNumber>();
  private Date createdDate;
  private Date updatedDate;
  private String updatedBy;

  private List<Person> personHistory;
  private List<Name> nameHistory;
  private List<Telecommunication> telecommunicationHistory;
  private List<Address> addressHistory;
  private List<MedicalRecordNumber> mrnHistory;

  public Person() {
    // initialize default values for physicalAddress/mailingAddress instances
    this.physicalAddress.setAddressType("PHYS");
    this.mailingAddress.setAddressType("PST");
    // initialize default values for homePhone, workPhone and mobilePhone instances
    this.homePhone.setType("H");
    this.workPhone.setType("WP");
    this.mobilePhone.setType("MC");
    // initialize empty string value for eligibility
    this.eligibility = "";
  }

  public List<Address> getActiveAddresses() {
    List<Address> addresses = new ArrayList<Address>();
    if (physicalAddress != null) {
      if (physicalAddress.isMasked()) {
        addresses.add(physicalAddress);
      } else if (!StringUtils.isEmpty(physicalAddress.getDisplayAddressNoCountryProv())) {
        addresses.add(physicalAddress);
      }
    }
    if (mailingAddress != null) {
      if (mailingAddress.isMasked()) {
        addresses.add(mailingAddress);
      } else if (!StringUtils.isEmpty(mailingAddress.getDisplayAddressNoCountryProv())) {
        addresses.add(mailingAddress);
      }
    }

    /**
     * ** if (physicalAddress!=null &&
     * !StringUtils.isEmpty(physicalAddress.getDisplayAddressNoCountryProv())){
     * addresses.add(physicalAddress); } if (mailingAddress!=null &&
     * !StringUtils.isEmpty(mailingAddress.getDisplayAddressNoCountryProv())){
     * addresses.add(mailingAddress); } **
     */
    return addresses;
  }

  public boolean isExistingPhysicalAddressBlank() {
    return this.existingPhysicalAddress.isBlankAddress();
  }

  public boolean isExistingMailingAddressBlank() {
    return this.existingMailingAddress.isBlankAddress();
  }

  public List<Telecommunication> getActiveTelecommunications() {
    List<Telecommunication> telecommunications = new ArrayList<Telecommunication>();
    if (homePhone != null) {
      if (homePhone.isMasked()) {
        telecommunications.add(homePhone);
      } else if (!StringUtils.isEmpty(homePhone.getDisplayText())) {
        telecommunications.add(homePhone);
      }
    }
    if (workPhone != null) {
      if (workPhone.isMasked()) {
        telecommunications.add(workPhone);
      } else if (!StringUtils.isEmpty(workPhone.getDisplayText())) {
        telecommunications.add(workPhone);
      }
    }
    if (mobilePhone != null) {
      if (mobilePhone.isMasked()) {
        telecommunications.add(mobilePhone);
      } else if (!StringUtils.isEmpty(mobilePhone.getDisplayText())) {
        telecommunications.add(mobilePhone);
      }
    }
    if (homeEmail != null && !StringUtils.isEmpty(homeEmail.getDisplayText())) {
      telecommunications.add(homeEmail);
    }
    if (workEmail != null && !StringUtils.isEmpty(workEmail.getDisplayText())) {
      telecommunications.add(workEmail);
    }
    if (mobileEmail != null && !StringUtils.isEmpty(mobileEmail.getDisplayText())) {
      telecommunications.add(mobileEmail);
    }
    return telecommunications;
  }

  public boolean hasAddressBeenModified(String type) {
    if (type.equals(Address.MAILING_ADDRESS_TYPE)) {
      return this.existingMailingAddress.isBlankAddress()
          || this.mailingAddress
                  .getAddressLine1()
                  .compareTo(this.existingMailingAddress.getAddressLine1())
              != 0
          || this.mailingAddress
                  .getAddressLine2()
                  .compareTo(this.existingMailingAddress.getAddressLine2())
              != 0
          || this.mailingAddress
                  .getAddressLine3()
                  .compareTo(this.existingMailingAddress.getAddressLine3())
              != 0
          || this.mailingAddress.getCity().compareTo(this.existingMailingAddress.getCity()) != 0
          || this.mailingAddress.getProvince().compareTo(this.existingMailingAddress.getProvince())
              != 0
          || this.mailingAddress
                  .getPostalCode()
                  .compareTo(this.existingMailingAddress.getPostalCode())
              != 0
          || this.mailingAddress.getCountry().compareTo(this.existingMailingAddress.getCountry())
              != 0;
    } else if (type.equals(Address.MAILING_ADDRESS_TYPE)) {
      return this.existingPhysicalAddress.isBlankAddress()
          || this.physicalAddress
                  .getAddressLine1()
                  .compareTo(this.existingPhysicalAddress.getAddressLine1())
              != 0
          || this.physicalAddress
                  .getAddressLine2()
                  .compareTo(this.existingPhysicalAddress.getAddressLine2())
              != 0
          || this.physicalAddress
                  .getAddressLine3()
                  .compareTo(this.existingPhysicalAddress.getAddressLine3())
              != 0
          || this.physicalAddress.getCity().compareTo(this.existingPhysicalAddress.getCity()) != 0
          || this.physicalAddress
                  .getProvince()
                  .compareTo(this.existingPhysicalAddress.getProvince())
              != 0
          || this.physicalAddress
                  .getPostalCode()
                  .compareTo(this.existingPhysicalAddress.getPostalCode())
              != 0
          || this.physicalAddress.getCountry().compareTo(this.existingPhysicalAddress.getCountry())
              != 0;
    }
    return true;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getDeathDate() {
    return deathDate;
  }

  public void setDeathDate(Date deathDate) {
    this.deathDate = deathDate;
  }

  public String getDeceased() {
    if (((String) deceased) == null || ((String) deceased).equals("")) {
      deceased = "";
    } else if ("true".equals(deceased)) {
      deceased = "Y";
    } else if ("false".equals(deceased)) {
      deceased = "N";
    }
    return deceased;
  }

  public void setDeceased(String deceased) {
    this.deceased = deceased;
  }

  public Name getDeclaredName() {
    return declaredName;
  }

  public void setDeclaredName(Name declaredName) {
    this.declaredName = declaredName;
  }

  public Name getDocumentedName() {
    return documentedName;
  }

  public void setDocumentedName(Name documentedName) {
    this.documentedName = documentedName;
  }

  public String getEligibility() {
    return eligibility;
  }

  public String getEligibilityForDisplay() {
    if (eligibility != null
        && (eligibility.compareToIgnoreCase("Aborted") == 0
            || eligibility.compareToIgnoreCase("Not found") == 0)) {
      return "N";
    } else if (eligibility != null && eligibility.compareToIgnoreCase("Active") == 0) {
      return "Y";
    } else {
      return "N";
    }
  }

  public void setEligibility(String eligibility) {
    this.eligibility = eligibility;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Telecommunication getHomePhone() {
    return homePhone;
  }

  public void setHomePhone(Telecommunication homePhone) {
    this.homePhone = homePhone;
  }

  public Address getMailingAddress() {
    return mailingAddress;
  }

  public void setMailingAddress(Address mailingAddress) {
    this.mailingAddress = mailingAddress;
  }

  public List<MedicalRecordNumber> getMedicalRecordNumbers() {
    return medicalRecordNumbers;
  }

  public void setMedicalRecordNumbers(List<MedicalRecordNumber> medicalRecordNumbers) {
    this.medicalRecordNumbers = medicalRecordNumbers;
  }

  public Telecommunication getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(Telecommunication mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getPhn() {
    return phn;
  }

  public void setPhn(String phn) {
    this.phn = phn;
  }

  public Address getPhysicalAddress() {
    return physicalAddress;
  }

  public void setPhysicalAddress(Address physicalAddress) {
    this.physicalAddress = physicalAddress;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  public Telecommunication getWorkPhone() {
    return workPhone;
  }

  public void setWorkPhone(Telecommunication workPhone) {
    this.workPhone = workPhone;
  }

  public List<Address> getAddressHistory() {
    return addressHistory;
  }

  public void setAddressHistory(List<Address> addressHistory) {
    this.addressHistory = addressHistory;
  }

  public List<MedicalRecordNumber> getMrnHistory() {
    return mrnHistory;
  }

  public void setMrnHistory(List<MedicalRecordNumber> mrnHistory) {
    this.mrnHistory = mrnHistory;
  }

  public List<Person> getPersonHistory() {
    return personHistory;
  }

  public void setPersonHistory(List<Person> personHistory) {
    this.personHistory = personHistory;
  }

  public List<Telecommunication> getTelecommunicationHistory() {
    return telecommunicationHistory;
  }

  public void setTelecommunicationHistory(List<Telecommunication> telecommunicationHistory) {
    this.telecommunicationHistory = telecommunicationHistory;
  }

  public List<Name> getNameHistory() {
    return nameHistory;
  }

  public void setNameHistory(List<Name> nameHistory) {
    this.nameHistory = nameHistory;
  }

  public Telecommunication getHomeEmail() {
    return homeEmail;
  }

  public void setHomeEmail(Telecommunication homeEmail) {
    this.homeEmail = homeEmail;
  }

  public Telecommunication getMobileEmail() {
    return mobileEmail;
  }

  public void setMobileEmail(Telecommunication mobileEmail) {
    this.mobileEmail = mobileEmail;
  }

  public Telecommunication getWorkEmail() {
    return workEmail;
  }

  public void setWorkEmail(Telecommunication workEmail) {
    this.workEmail = workEmail;
  }

  public String getPhnStatus() {
    if ("true".equals(phnStatus)) {
      phnStatus = "Active";
    } else if ("false".equals(phnStatus)) {
      phnStatus = "Merged";
    } else if (phnStatus.isEmpty() && phnStatus == null) {
      phnStatus = "";
    }
    return phnStatus;
  }

  public void setPhnStatus(String phnStatus) {
    this.phnStatus = phnStatus;
  }

  public boolean isBirthDateMasked() {
    return birthDateMasked;
  }

  public void setBirthDateMasked(boolean birthDateMasked) {
    this.birthDateMasked = birthDateMasked;
  }

  public boolean isDeclaredNameMasked() {
    return declaredNameMasked;
  }

  public void setDeclaredNameMasked(boolean declaredNameMasked) {
    this.declaredNameMasked = declaredNameMasked;
  }

  public boolean isDocumentedNameMasked() {
    return documentedNameMasked;
  }

  public void setDocumentedNameMasked(boolean documentedNameMasked) {
    this.documentedNameMasked = documentedNameMasked;
  }

  public Address getExistingPhysicalAddress() {
    return existingPhysicalAddress;
  }

  public void setExistingPhysicalAddress(Address existingPhysicalAddress) {
    this.existingPhysicalAddress = existingPhysicalAddress;
  }

  public Address getExistingMailingAddress() {
    return existingMailingAddress;
  }

  public void setExistingMailingAddress(Address existingMailingAddress) {
    this.existingMailingAddress = existingMailingAddress;
  }

  public boolean getForceCreatePhn() {
    return forceCreatePhn;
  }

  public void setForceCreatePhn(boolean forceCreatePhn) {
    this.forceCreatePhn = forceCreatePhn;
  }

  public boolean isDeathConfirmed() {
    deathConfirmed = false;
    if (getDeceased().equals("Y")) {
      deathConfirmed = true;
    }
    return deathConfirmed;
  }

  public void setDeathConfirmed(boolean deathConfirmed) {
    this.deathConfirmed = deathConfirmed;
  }
}
