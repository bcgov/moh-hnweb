package ca.bc.gov.hlth.hnweb.model.v2.message;

import static ca.bc.gov.hlth.hnweb.util.V2MessageUtil.ENCODING_CHARACTERS;
import static ca.bc.gov.hlth.hnweb.util.V2MessageUtil.FIELD_SEPARATOR;

import ca.bc.gov.hlth.hnweb.model.v2.segment.IN1Custom;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZHD;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIA;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIH;
import ca.bc.gov.hlth.hnweb.model.v2.segment.ZIK;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.hl7v2.util.Terser;

/**
 * Structure to represent a HL7 R50 Message 
 *
 */
@SuppressWarnings("serial")
public class R50 extends AbstractMessage {

	public R50() {
        this(new DefaultModelClassFactory());
	}

    /**
     * Constructor.
     * @param theFactory ModelClassFactory is used to call parent constructor.
     */
    public R50(ModelClassFactory theFactory) {
        super(theFactory);
        init();
    }
    
    private void init() {
        try {
            this.add(MSH.class, true, false);
            this.add(ZHD.class, true, false);
            this.add(PID.class, true, false);          
            this.add(ZIA.class, true, false);
            this.add(IN1Custom.class, true, false);
            this.add(ZIH.class, false, false);
            this.add(ZIK.class, false, false);

        	Terser.set(this.getMSH(), 1, 0, 1, 1, FIELD_SEPARATOR);
            Terser.set(this.getMSH(), 2, 0, 1, 1, ENCODING_CHARACTERS);
            
       } catch(HL7Exception e) {
            log.error("Unexpected error creating R50 - this is probably a bug in the source code generator.", e);
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

	public ZHD getZHD() {
		return getTyped("ZHD", ZHD.class);
	}

	public PID getPID() {
		return getTyped("PID", PID.class);
	}

	public ZIA getZIA() {
		return getTyped("ZIA", ZIA.class);
	}

	public IN1Custom getIN1() {
		return getTyped("IN1Custom", IN1Custom.class);
	}

	public ZIH getZIH() {
		return getTyped("ZIH", ZIH.class);
	}

	public ZIK getZIK() {
		return getTyped("ZIK", ZIK.class);
	}

}
