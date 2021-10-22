package ca.bc.gov.hlth.hnweb.model.v2.segment;

import ca.bc.gov.hlth.hnweb.model.v2.datatype.ZRG;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.v24.datatype.HD;
import ca.uhn.hl7v2.model.v24.datatype.IS;
import ca.uhn.hl7v2.model.v24.datatype.SI;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 *<p>Represents an HL7 ZIK message segment. </p>
*/
@SuppressWarnings("serial")
public class ZIK extends AbstractSegment {

    /** 
     * Creates a new ZIK segment
     */
    public ZIK(Group parent, ModelClassFactory factory) {
       super(parent, factory);
       init(factory);
    }

	private void init(ModelClassFactory factory) {
		try {
																											// TABLE				healthnetBC Usage		
			this.add(HD.class, true, 1, 52, new Object[] { getMessage() }, "Assigning Facility");
			this.add(IS.class, true, 1, 5, new Object[] { getMessage() }, "Document Medium");				// HNET:9948
			this.add(IS.class, true, 1, 1, new Object[] { getMessage() }, "Original DocumentIndicator");	// HNET:9940
			this.add(ZRG.class, true, 2, 91, new Object[] { getMessage() }, "Document Argument");			//					Repeating group of arguments used to describe documents
			this.add(IS.class, true, 1, 20, new Object[] { getMessage() }, "Document Purpose");
			this.add(SI.class, true, 1, 4, new Object[] { getMessage() }, "Set ID - ZIK");
			
		} catch (HL7Exception e) {
			log.error("Unexpected error creating ZIK - this is probably a bug in the source code generator.", e);
		}
	}


	/**
     * Returns all repetitions of Document Argument (ZIK-4).
     */
    public ZRG[] getDocumentArgument() {
    	ZRG[] retVal = this.getTypedField(4, new ZRG[0]);
    	return retVal;
    }

    /**
     * Returns all repetitions of Document Argument (ZIK-4).
     */
    public ZRG[] getZik4_DocumentArgument() {
    	ZRG[] retVal = this.getTypedField(4, new ZRG[0]);
    	return retVal;
    }


    /**
     * Returns a count of the current number of repetitions of Document Argument (ZIK-4).
     * This method does not create a repetition, so if no repetitions have currently been defined or accessed,
     * it will return zero.
     */
    public int getDocumentArgumentReps() {
    	return this.getReps(4);
    }


    /**
     * Returns a count of the current number of repetitions of Document Argument (ZIK-4).
     * This method does not create a repetition, so if no repetitions have currently been defined or accessed,
     * it will return zero.
     */
    public int getZik4_DocumentArgumentReps() {
    	return this.getReps(4);
    }


    /**
     * Returns a specific repetition of
     * ZIK-4: "Document Argument" - creates it if necessary
     *
     * @param rep The repetition index (0-indexed)
     */
    public ZRG getDocumentArgument(int rep) { 
		ZRG retVal = this.getTypedField(4, rep);
		return retVal;
    }

    /**
     * Returns a specific repetition of
     * ZIK-4: "Document Argument" - creates it if necessary
     *
     * @param rep The repetition index (0-indexed)
     */
    public ZRG getZik4_DocumentArgument(int rep) { 
		ZRG retVal = this.getTypedField(4, rep);
		return retVal;
    }

    /**
     * Inserts a repetition of
     * ZIK-4: "Document Argument" at a specific index
     *
     * @param rep The repetition index (0-indexed)
     * @throws HL7Exception If the rep is invalid (below 0, or too high for the allowable repetitions)
     */
    public ZRG insertDocumentArgument(int rep) throws HL7Exception { 
        return (ZRG) super.insertRepetition(4, rep);
    }


    /**
     * Inserts a repetition of
     * ZIK-4: "Document Argument" at a specific index
     *
     * @param rep The repetition index (0-indexed)
     * @throws HL7Exception If the rep is invalid (below 0, or too high for the allowable repetitions)
     */
    public ZRG insertZik4_DocumentArgument(int rep) throws HL7Exception { 
        return (ZRG) super.insertRepetition(4, rep);
    }


    /**
     * Removes a repetition of
     * ZIK-4: "Document Argument" at a specific index
     *
     * @param rep The repetition index (0-indexed)
     * @throws HL7Exception If the rep is invalid (below 0, or too high for the allowable repetitions)
     */
    public ZRG removeDocumentArgument(int rep) throws HL7Exception { 
        return (ZRG) super.removeRepetition(4, rep);
    }


    /**
     * Removes a repetition of
     * ZIK-4: "Document Argument" at a specific index
     *
     * @param rep The repetition index (0-indexed)
     * @throws HL7Exception If the rep is invalid (below 0, or too high for the allowable repetitions)
     */
    public ZRG removeZik4_DocumentArgument(int rep) throws HL7Exception { 
        return (ZRG) super.removeRepetition(4, rep);
    }


}
