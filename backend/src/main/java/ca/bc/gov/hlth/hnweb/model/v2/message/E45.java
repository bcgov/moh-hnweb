package ca.bc.gov.hlth.hnweb.model.v2.message;

import ca.bc.gov.hlth.hnweb.model.v2.segment.HDR;
import ca.bc.gov.hlth.hnweb.model.v2.segment.QPD;
import ca.bc.gov.hlth.hnweb.model.v2.segment.SFT;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.RCP;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.hl7v2.util.Terser;

/**
 * Structure to represent a HL7 E45 Message 
 *
 */
@SuppressWarnings("serial")
public class E45 extends AbstractMessage {

	public E45() {
        this(new DefaultModelClassFactory());
	}

    /**
     * Constructor.
     * 
     * @param theFactory ModelClassFactory is used to call parent constructor.
     */
    public E45(ModelClassFactory theFactory) {
        super(theFactory);
        init();
    }
    
    private void init() {
        try {
            this.add(MSH.class, true, false);
        	Terser.set(this.getMSH(), 1, 0, 1, 1, "|");
            Terser.set(this.getMSH(), 2, 0, 1, 1, "^~\\&");
            
            this.add(HDR.class, true, false);
            this.add(SFT.class, true, false);
            this.add(QPD.class, true, false);
            this.add(RCP.class, true, false);

        } catch(HL7Exception e) {
            log.error("Unexpected error creating E45 - this is probably a bug in the source code generator.", e);
       }
    }
    
    /** 
     *
     * @return Returns "2.4"
     */
    @Override
    public String getVersion() {
       return "2.4";
    }
    
	public MSH getMSH() {
		return getTyped("MSH", MSH.class);
	}

	public HDR getHDR() {
		return getTyped("HDR", HDR.class);
	}

	public SFT getSFT() {
		return getTyped("SFT", SFT.class);
	}

	public QPD getQPD() {
		return getTyped("QPD", QPD.class);
	}

	public RCP getRCP() {
		return getTyped("RCP", RCP.class);
	}

}
