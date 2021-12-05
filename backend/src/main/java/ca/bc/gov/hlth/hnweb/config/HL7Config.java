package ca.bc.gov.hlth.hnweb.config;

/**
 *Config class to build GetDemographicsRequest
 *
 */
public class HL7Config {
  private String senderDeviceRoot;
  private String senderDeviceExtension;
  private String senderOrgRoot;
  private String senderOrgExtension;

  private String recieverDeviceRoot;
  private String recieverDeviceExtension;
  private String recieverOrgRoot;
  private String recieverOrgExtension;

  private String hl7Version;

  public HL7Config() {
    hl7Version = "V3PR1";
    recieverDeviceRoot = "2.16.840.1.113883.3.51.1.1.4";
    recieverDeviceExtension = "192.168.0.1";
    recieverOrgRoot = "2.16.840.1.113883.3.51.1.1.3";
    recieverOrgExtension = "HCIM";

    senderDeviceRoot = "2.16.840.1.113883.3.51.1.1.5";
    senderDeviceExtension = "EWEB_PHN";
    senderOrgRoot = "2.16.840.1.113883.3.51.1.1.3";
    senderOrgExtension = "MOH_CRS";
  }

  public String getHl7Version() {
    return hl7Version;
  }

  public void setHl7Version(String hl7Version) {
    this.hl7Version = hl7Version;
  }

  public String getRecieverDeviceExtension() {
    return recieverDeviceExtension;
  }

  public void setRecieverDeviceExtension(String recieverDeviceExtension) {
    this.recieverDeviceExtension = recieverDeviceExtension;
  }

  public String getRecieverDeviceRoot() {
    return recieverDeviceRoot;
  }

  public void setRecieverDeviceRoot(String recieverDeviceRoot) {
    this.recieverDeviceRoot = recieverDeviceRoot;
  }

  public String getRecieverOrgExtension() {
    return recieverOrgExtension;
  }

  public void setRecieverOrgExtension(String recieverOrgExtension) {
    this.recieverOrgExtension = recieverOrgExtension;
  }

  public String getRecieverOrgRoot() {
    return recieverOrgRoot;
  }

  public void setRecieverOrgRoot(String recieverOrgRoot) {
    this.recieverOrgRoot = recieverOrgRoot;
  }

  public String getSenderDeviceExtension() {
    return senderDeviceExtension;
  }

  public void setSenderDeviceExtension(String senderDeviceExtension) {
    this.senderDeviceExtension = senderDeviceExtension;
  }

  public String getSenderDeviceRoot() {
    return senderDeviceRoot;
  }

  public void setSenderDeviceRoot(String senderDeviceRoot) {
    this.senderDeviceRoot = senderDeviceRoot;
  }

  public String getSenderOrgExtension() {
    return senderOrgExtension;
  }

  public void setSenderOrgExtension(String senderOrgExtension) {
    this.senderOrgExtension = senderOrgExtension;
  }

  public String getSenderOrgRoot() {
    return senderOrgRoot;
  }

  public void setSenderOrgRoot(String senderOrgRoot) {
    this.senderOrgRoot = senderOrgRoot;
  }
}