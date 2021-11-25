package ca.bc.gov.hlth.hnweb.model;

public class MedicalRecordNumber {
  private String mrn;
  private String source;
  private String mrnStatus;

  public MedicalRecordNumber() {}

  public String getMrnStatus() {
    if ("true".equals(mrnStatus)) {
      mrnStatus = "Active";
    } else if ("false".equals(mrnStatus)) {
      mrnStatus = "Merged";
    } else if (mrnStatus.isEmpty() && mrnStatus == null) {
      mrnStatus = "";
    }
    return mrnStatus;
  }

  public void setMrnStatus(String mrnStatus) {
    this.mrnStatus = mrnStatus;
  }

  public String getMrn() {
    return mrn;
  }

  public void setMrn(String mrn) {
    this.mrn = mrn;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }
}
