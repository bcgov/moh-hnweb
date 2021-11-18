package ca.bc.gov.hlth.hnweb.model.v2.segment;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.datatype.XON;
import ca.uhn.hl7v2.parser.ModelClassFactory;


/**
 *<p>Represents an HL7 SFT message segment. 
 * This segment has the following fields:</p>
 * <ul>
     * <li>SFT-1: Standard Version Number (ST) <b>optional </b> 
     * <li>SFT-2: Standard Name (?) <b>optional </b>
     * <li>SFT-3: Software Vendor Organization (XON) <b>optional </b>
     * <li>SFT-4: Software Version Number (ST) <b>required </b>
     * <li>SFT-5: Software Product Name (ST) <b>optional </b>
     * <li>SFT-6: Software Product Information (ST) <b>optional </b>
     * <li>SFT-7: Software Install Date (ST) <b>optional </b>
 * </ul>
*/
@SuppressWarnings("serial")
public class SFT extends AbstractSegment {
    
    /** 
     * Creates a new SFT segment
     * @param parent
     * @param factory
     */
    public SFT(Group parent, ModelClassFactory factory) {
       super(parent, factory);
       init();
    }
    
    private void init() {
        try {
            this.add(ST.class, false, 1, 30, new Object[]{ getMessage() }, "Standard Version Number"); 
            this.add(ST.class, false, 1, 30, new Object[]{ getMessage() }, "Standard Name"); 					//Not Supported.
            this.add(XON.class, false, 1, 20, new Object[]{ getMessage() }, "Software Vendor Organization");
            this.add(ST.class, true, 1, 30, new Object[]{ getMessage() }, "Software Version Number");
            this.add(ST.class, false, 1, 30, new Object[]{ getMessage() }, "Software Product Name");
            this.add(ST.class, false, 1, 30, new Object[]{ getMessage() }, "Software Product Information");
            this.add(ST.class, false, 1, 15, new Object[]{ getMessage() }, "Software Install Date");

       } catch(HL7Exception e) {
            log.error("Unexpected error creating SFT - this is probably a bug in the source code generator.", e);
       }
    }

    /**
     * SFT1 - Create if necessary
     * @return retVal
     */
    public ST getSFT1_StandardVersionNumber() { 
    	return this.getTypedField(1, 0);
    }
    
    public ST getStandardVersionNumber() {
        return this.getTypedField(1, 0);
    }
    
    /**
     * SFT2 - Create if necessary
     * @return retVal
     */
    public ST getSFT2_StandardName() { 
    	return this.getTypedField(2, 0);
    }

    public ST getStandardName() { 
    	return this.getTypedField(2, 0);
    }
    
    /**
     * SFT3 - Create if necessary
     * @return retVal
     */
    public XON getSFT3_SoftwareVendorOrganization() { 
    	return this.getTypedField(3, 0);
    }

    public XON getSoftwareVendorOrganization() { 
    	return this.getTypedField(3, 0);
    }
    
    /**
     * SFT4 - Create if necessary
     * @return retVal
     */
    public ST getSFT4_SoftwareVersionNumber() { 
    	return this.getTypedField(4, 0);
    }

    public ST getSoftwareVersionNumber() { 
    	return this.getTypedField(4, 0);
    }
    
    /**
     * SFT5 - Create if necessary
     * @return retVal
     */
    public ST getSFT5_SoftwareProductName() { 
    	return this.getTypedField(5, 0);
    }

    public ST getSoftwareProductName() { 
    	return this.getTypedField(5, 0);
    }
    
}
