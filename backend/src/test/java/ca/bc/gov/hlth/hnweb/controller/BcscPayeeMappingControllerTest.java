package ca.bc.gov.hlth.hnweb.controller;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.model.rest.pbf.BcscPayeeMappingRequest;
import ca.bc.gov.hlth.hnweb.model.rest.pbf.BcscPayeeMappingResponse;

/**
 * Tests for {@link BcscPayeeMappingController}
 *
 */
@SpringBootTest
@Transactional
@Sql({ "classpath:scripts/bcsc_payee_mapping.sql", "classpath:scripts/pbf_clinic_payee.sql" })
public class BcscPayeeMappingControllerTest {
	
	@Autowired
	private BcscPayeeMappingController bcscPayeeMappingController;

	@Test
	public void testAddBcscPayeeMapping_success() {
		
		String bcscGuid = UUID.randomUUID().toString();		
		String payeeNumber = "00063";
		
		BcscPayeeMappingRequest bcscPayeeMappingRequest = createBcscPayeeMappingRequest(bcscGuid, payeeNumber);
		
		ResponseEntity<BcscPayeeMappingResponse> response = bcscPayeeMappingController.addBcscPayeeMapping(bcscPayeeMappingRequest);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		BcscPayeeMappingResponse bcscPayeeMappingResponse = response.getBody();
		assertNotNull(bcscPayeeMappingResponse);
		assertEquals(bcscGuid, bcscPayeeMappingResponse.getBcscGuid());
		assertEquals(payeeNumber, bcscPayeeMappingResponse.getPayeeNumber());		
	}

	@Test
	public void testAddBcscPayeeMapping_fail_missing_bcsc_guid() {

		String bcscGuid = null;		
		String payeeNumber = "00063";
		
		BcscPayeeMappingRequest bcscPayeeMappingRequest = createBcscPayeeMappingRequest(bcscGuid, payeeNumber);
		
		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> bcscPayeeMappingController.addBcscPayeeMapping(bcscPayeeMappingRequest))
		.withMessage("400 BAD_REQUEST \"Missing value in required request field bcscGuid.\"");
	}
	
	@Test
	public void testAddBcscPayeeMapping_fail_missing_bcsc_guid_empty_value() {

		String bcscGuid = " "; //Should fail for anything except a populated String		
		String payeeNumber = "00063";
		
		BcscPayeeMappingRequest bcscPayeeMappingRequest = createBcscPayeeMappingRequest(bcscGuid, payeeNumber);
		
		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> bcscPayeeMappingController.addBcscPayeeMapping(bcscPayeeMappingRequest))
		.withMessage("400 BAD_REQUEST \"Missing value in required request field bcscGuid.\"");		
	}
	
	@Test
	public void testAddBcscPayeeMapping_fail_missing_payee_number() {
		
		String bcscGuid = UUID.randomUUID().toString();		
		String payeeNumber = "   ";  //Should fail for anything except a populated String
		
		BcscPayeeMappingRequest bcscPayeeMappingRequest = createBcscPayeeMappingRequest(bcscGuid, payeeNumber);
		
		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> bcscPayeeMappingController.addBcscPayeeMapping(bcscPayeeMappingRequest))
		.withMessage("400 BAD_REQUEST \"Missing value in required request field payeeNumber.\"");		
	}
	
	@Test
	public void testAddBcscPayeeMapping_fail_bcsc_already_exists() {
		
		String bcscGuid = "a9c3b536-4598-411a-bda2-4068d6b5cc20";		
		String payeeNumber = "00063";
		
		BcscPayeeMappingRequest bcscPayeeMappingRequest = createBcscPayeeMappingRequest(bcscGuid, payeeNumber);
		
		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> bcscPayeeMappingController.addBcscPayeeMapping(bcscPayeeMappingRequest))
		.withMessage("409 CONFLICT \"Entity already exists.\"; nested exception is ca.bc.gov.hlth.hnweb.exception.BcscPayeeMappingException: Entity already exists.");
	}
	
    @Test
    public void testGetBcscPayeeMapping_success() {
        
        String bcscGuid = "14100f9b-7daa-4938-a833-c8c56a5988e9";       
        final String payeeNumber = "00023";
        
        ResponseEntity<BcscPayeeMappingResponse> response = bcscPayeeMappingController.getBcscPayeeMapping(bcscGuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BcscPayeeMappingResponse bcscPayeeMappingResponse = response.getBody();
        assertNotNull(bcscPayeeMappingResponse);
        assertEquals(bcscGuid, bcscPayeeMappingResponse.getBcscGuid());
        assertEquals(payeeNumber, bcscPayeeMappingResponse.getPayeeNumber());
        assertTrue(bcscPayeeMappingResponse.getPayeeIsActive());
    }

	@Test
	public void testGetBcscPayeeMapping_success_archived_payee() {
		
		String bcscGuid = "a9c3b536-4598-411a-bda2-4068d6b5cc20";		
		final String payeeNumber = "00053";
		
		ResponseEntity<BcscPayeeMappingResponse> response = bcscPayeeMappingController.getBcscPayeeMapping(bcscGuid);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		BcscPayeeMappingResponse bcscPayeeMappingResponse = response.getBody();
		assertNotNull(bcscPayeeMappingResponse);
		assertEquals(bcscGuid, bcscPayeeMappingResponse.getBcscGuid());
		assertEquals(payeeNumber, bcscPayeeMappingResponse.getPayeeNumber());
		assertEquals(false, bcscPayeeMappingResponse.getPayeeIsActive());
	}

    @Test
    public void testGetBcscPayeeMapping_success_no_payee_status() {
        
        String bcscGuid = "f33c9e07-6f49-46c2-90c2-6e0013729c9d";       
        final String payeeNumber = "X0054";
        
        ResponseEntity<BcscPayeeMappingResponse> response = bcscPayeeMappingController.getBcscPayeeMapping(bcscGuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BcscPayeeMappingResponse bcscPayeeMappingResponse = response.getBody();
        assertNotNull(bcscPayeeMappingResponse);
        assertEquals(bcscGuid, bcscPayeeMappingResponse.getBcscGuid());
        assertEquals(payeeNumber, bcscPayeeMappingResponse.getPayeeNumber());
        assertEquals(false, bcscPayeeMappingResponse.getPayeeIsActive());
    }

	@Test
	public void testGetBcscPayeeMapping_fail_not_found() {
		
		String bcscGuid = UUID.randomUUID().toString();		

		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> bcscPayeeMappingController.getBcscPayeeMapping(bcscGuid))
		.withMessage("404 NOT_FOUND \"Entity not found for ID %s\"", bcscGuid);
	}

	@Test
	public void testUpdateBcscPayeeMapping_success() {
		
		String bcscGuid = "a9c3b536-4598-411a-bda2-4068d6b5cc20";		
		String updatedPayeeNumber = "00023";
		
		BcscPayeeMappingRequest bcscPayeeMappingRequest = createBcscPayeeMappingRequest(bcscGuid, updatedPayeeNumber);
		
		ResponseEntity<BcscPayeeMappingResponse> response = bcscPayeeMappingController.updateBcscPayeeMapping(bcscPayeeMappingRequest, bcscGuid);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		BcscPayeeMappingResponse updatedBcscPayeeMappingResponse = response.getBody();
		assertNotNull(updatedBcscPayeeMappingResponse);
		assertEquals(bcscGuid, updatedBcscPayeeMappingResponse.getBcscGuid());
		assertEquals(updatedPayeeNumber, updatedBcscPayeeMappingResponse.getPayeeNumber());
	}
	
	@Test
	public void testUpdateBcscPayeeMapping_fail_does_not_exist() {
		
		String bcscGuid = "a9c3b536-4598-411a-bda2-4068d6b5cc20";		
		String updatedPayeeNumber = "00023";
		
		BcscPayeeMappingRequest bcscPayeeMappingRequest = createBcscPayeeMappingRequest(bcscGuid, updatedPayeeNumber);
		
		String incorrectBcscGuid = UUID.randomUUID().toString();

		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> bcscPayeeMappingController.updateBcscPayeeMapping(bcscPayeeMappingRequest, incorrectBcscGuid))
		.withMessage("404 NOT_FOUND \"Entity not found.\"; nested exception is ca.bc.gov.hlth.hnweb.exception.BcscPayeeMappingException: Entity not found.");
	}
	
	@Test
	public void testUpdateBcscPayeeMapping_fail_missing_payee_number() {
		
		String bcscGuid = "a9c3b536-4598-411a-bda2-4068d6b5cc20";		
		String updatedPayeeNumber = "  ";
		
		BcscPayeeMappingRequest bcscPayeeMappingRequest = createBcscPayeeMappingRequest(bcscGuid, updatedPayeeNumber);		
		
		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> bcscPayeeMappingController.updateBcscPayeeMapping(bcscPayeeMappingRequest, bcscGuid))
		.withMessage("400 BAD_REQUEST \"Missing value in required request field payeeNumber\"");		
	}
	
	@Test
	public void testDeleteBcscPayeeMapping_success() {
		
		String bcscGuid = "a9c3b536-4598-411a-bda2-4068d6b5cc20";
		
		ResponseEntity<Void> deleteRresponse = bcscPayeeMappingController.deleteBcscPayeeMapping(bcscGuid);
		assertEquals(HttpStatus.NO_CONTENT, deleteRresponse.getStatusCode());
		assertEquals(deleteRresponse.getBody(), null);
		
		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> bcscPayeeMappingController.getBcscPayeeMapping(bcscGuid))
		.withMessage("404 NOT_FOUND \"Entity not found for ID %s\"", bcscGuid);
	}

	@Test
	public void testDeleteBcscPayeeMapping_fail_not_found() {
		
		String bcscGuid = UUID.randomUUID().toString();		

		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> bcscPayeeMappingController.deleteBcscPayeeMapping(bcscGuid))
		.withMessage("404 NOT_FOUND \"Entity not found.\"; nested exception is ca.bc.gov.hlth.hnweb.exception.BcscPayeeMappingException: Entity not found.");
	}

	private BcscPayeeMappingRequest createBcscPayeeMappingRequest(String bcscGuid, String payeeNumber) {
		BcscPayeeMappingRequest bcscPayeeMappingRequest = new BcscPayeeMappingRequest();
		bcscPayeeMappingRequest.setBcscGuid(bcscGuid);
		bcscPayeeMappingRequest.setPayeeNumber(payeeNumber);
		return bcscPayeeMappingRequest;
	}
	
}


