package ca.bc.gov.hlth.hnweb.model.v2.segment;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.datatype.TS;
import ca.uhn.hl7v2.model.v24.datatype.XON;
import ca.uhn.hl7v2.parser.ModelClassFactory;


/**
 *<p>Represents an HL7 ZHD message segment. 
 * This segment has the following fields:</p>
 * <ul>
     * <li>ZHD-1: Event Date/Time (TS) <b> </b> 
     * <li>ZHD-2: Organization (XON) <b> </b>
     * <li>ZHD-3: User Group (ST) <b>optional </b>
     * <li>ZHD-7: Software Version Number (ST)<b>optional </b>
 * </ul>
*/
@SuppressWarnings("serial")
public class ZHD extends AbstractSegment {
    
    /** 
     * Creates a new ZHD segment
     * @param parent
     * @param factory
     */
    public ZHD(Group parent, ModelClassFactory factory) {
       super(parent, factory);
       init(factory);
    }
    
    private void init(ModelClassFactory factory) {
        try {
            this.add(TS.class, true, 1, 19, new Object[]{ getMessage() }, "Event Date/Time"); 
            this.add(XON.class, true, 1, 20, new Object[]{ getMessage() }, "Organization");
            this.add(ST.class, false, 1, 30, new Object[]{ getMessage() }, "User Group");
            // CRS Retirement does not use these fields, but the HAPI library requires 
            // filler fields to know that "Software Version Number" is the 7th element.
            this.add(Varies.class, false, 1, 1, new Object[]{ getMessage() }, "Unused by CRS Retirement");
            this.add(Varies.class, false, 1, 1, new Object[]{ getMessage() }, "Unused by CRS Retirement");
            this.add(Varies.class, false, 1, 1, new Object[]{ getMessage() }, "Unused by CRS Retirement");
            this.add(ST.class, false, 1, 15, new Object[]{ getMessage() }, "Software Version Number");

       } catch(HL7Exception e) {
            log.error("Unexpected error creating ZHD - this is probably a bug in the source code generator.", e);
       }
    }

    /**
     * ZHD1 - Create if necessary
     * @return retVal
     */
    public TS getZHD1_EventDateTime() { 
        TS retVal = this.getTypedField(1, 0);
        return retVal;
    }
    public TS getEventDateTime() {
        TS retVal = this.getTypedField(1, 0);
        return retVal;
    }
    
    /**
     * ZHD2 - Create if necessary
     * @return retVal
     */
    public XON getZHD2_Organization() { 
        XON retVal = this.getTypedField(2, 0);
        return retVal;
    }
    public XON getOrganization() { 
        XON retVal = this.getTypedField(2, 0);
        return retVal;
    }
    
    /**
     * ZHD3 - Create if necessary
     * @return retVal
     */
    public ST getZHD3_UserGroup() { 
        ST retVal = this.getTypedField(3, 0);
        return retVal;
    }
    public ST getUserGroup() { 
        ST retVal = this.getTypedField(3, 0);
        return retVal;
    }
    
    /**
     * ZHD7 - Create if necessary
     * @return retVal
     */
    public ST getZHD7_SoftwareVersionNumber() { 
        ST retVal = this.getTypedField(7, 0);
        return retVal;
    }
    public ST getSoftwareVersionNumber() { 
        ST retVal = this.getTypedField(7, 0);
        return retVal;
    }
    
}
