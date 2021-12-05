package ca.bc.gov.hlth.hnweb.serialization;

import ca.bc.gov.hlth.hnweb.config.HL7Config;
import ca.bc.gov.hlth.hnweb.model.Telecommunication;
import ca.bc.gov.hlth.hnweb.model.v3.Address;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.v3.MedicalRecordNumber;
import ca.bc.gov.hlth.hnweb.model.v3.Message;
import ca.bc.gov.hlth.hnweb.model.v3.MessageMetaData;
import ca.bc.gov.hlth.hnweb.model.v3.Name;
import ca.bc.gov.hlth.hnweb.model.v3.Person;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * HL7Serializer is responsible for converting between HL7 V3 messages, and the native Java domain
 * model defined in the ca.bc.gov.health.hnweb.model package.
 *
 */
public class HL7Serializer {

  private static final String hl4DateFormat = "yyyyMMdd";
  private static final String shortSoapFormat = "yyyy-MM-dd";
  private static final String longSoapFormat = "yyyy-MM-ddThh:mm:ss";
  private XStream xs = new XStream(new DomDriver());
  private Map<Class, Transformer> mappings;
  private HL7Config hl7Config;

  /**
   * Creates a new HL7Serializer instance based on the provided configuration parameters
   *
   * @param hl7Config HL7Config
   */
  public HL7Serializer(HL7Config hl7Config) {

    XStream.setupDefaultSecurity(xs); // to be removed after 1.5
    xs.allowTypesByWildcard(new String[] {"ca.bc.gov.hlth.hnweb.model.**"});
    xs.allowTypes(new Class[] {ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse.class});

    xs.registerConverter(new DateConverter(hl4DateFormat, new String[] {hl4DateFormat}, true));
    xs.aliasType("getDemographics", GetDemographicsRequest.class);
    xs.aliasType("getDemographicsResponse", GetDemographicsResponse.class);   
    xs.aliasType("person", Person.class);
    xs.aliasType("hl7Config", HL7Config.class);
    xs.aliasType("msgConfig", MessageMetaData.class);
	xs.aliasType("medicalRecordNumber", MedicalRecordNumber.class);
	xs.aliasType("telecommunication", Telecommunication.class);
	xs.aliasType("address", Address.class); xs.aliasType("name", Name.class);
	xs.aliasType("message", Message.class);
		 

    xs.setMode(XStream.NO_REFERENCES);

    mappings = new HashMap<Class, Transformer>();
    this.hl7Config = hl7Config;
  }

  /**
   * Returns the XStream serialized version of an Object, useful for debugging
   *
   * @param input Object
   * @return String - simple xml representation of the Object.
   */
  public String xStream(Object input) {
    return xs.toXML(input);
  }

  /**
   * Converts an Object to a generic XML message
   *
   * @param input Object - the object to convert
   * @return String - XML message
   */
  public String toXml(Object input) {
    String xml = xs.toXML(input);
    xml = "<root>" + xml + "</root>";
    return transform(xml, input.getClass());
  }

  /**
   * Converts an Object to an HL7 v3 XML message
   *
   * @param input Object - the object to convert
   * @param meta MessageMetaData - Message metadata
   * @return String - HL7v3 XML message
   */
  public String toXml(Object input, MessageMetaData meta) {
    String xml = xs.toXML(input);
    String hl7params = xs.toXML(hl7Config);
    String msgParams = xs.toXML(meta);
    xml = "<root>" + hl7params + msgParams + xml + "</root>";
    return transform(xml, input.getClass());
  }

  /**
   * Deserializes an HL7v3 message into Java objects
   *
   * @param clazz Class&lt;T&gt; - Class to deserialize to
   * @param input String - HL7v3 message
   * @return Deserialized object
   */
  public <T> T fromXml(String input, Class<T> clazz) {
    input = input.replaceAll("xmlns=\"[^>]*", "");
    String xml = transform(input, clazz);
    return (T) xs.fromXML(xml);
  }

  /**
   * Runs an XSLT transform on an XML document
   *
   * @param content String - Xml document
   * @param xslt String - XML stylesheet transform
   * @return String - The transformed XML
   */
  private String transform(String content, Class clazz) {
    try {
      Transformer transformer = mappings.get(clazz);
      Writer buffer = new StringWriter();
      if (transformer == null) {
        String xslt = getXslt(clazz);
        transformer =
            TransformerFactory.newInstance()
                .newTransformer(new StreamSource(new StringReader(xslt)));
        mappings.put(clazz, transformer);
      }
      transformer.transform(new StreamSource(new StringReader(content)), new StreamResult(buffer));
      return buffer.toString();
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Loads the XSLT file from the classpath for a given class
   *
   * @param clazz Class - Class to load the XSLT for
   * @return String - XSLT document
   */
  private String getXslt(Class clazz) {
    InputStream is = clazz.getClassLoader().getResourceAsStream(clazz.getSimpleName() + ".xslt");
    return readStream(is);
  }

  /**
   * Reads the contents on an InputStream into a String
   *
   * @param is InputStream
   * @return String
   */
  private String readStream(InputStream is) {
    StringBuilder buff = new StringBuilder();
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    try {
      String line = br.readLine();
      while (line != null) {
        buff.append(line);
        line = br.readLine();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return buff.toString();
  }
}