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

import ca.bc.gov.hlth.hnweb.model.rest.pbf.UserPayeeMappingRequest;
import ca.bc.gov.hlth.hnweb.model.rest.pbf.UserPayeeMappingResponse;

/**
 * Tests for {@link UserPayeeMappingController}
 *
 */
@SpringBootTest
@Transactional
@Sql({ "classpath:scripts/user_payee_mapping.sql", "classpath:scripts/pbf_clinic_payee.sql" })
public class UserPayeeMappingControllerTest {
	
	@Autowired
	private UserPayeeMappingController userPayeeMappingController;

	@Test
	public void testAddUserPayeeMapping_success() {
		
		String userGuid = UUID.randomUUID().toString();		
		String payeeNumber = "00063";
		
		UserPayeeMappingRequest userPayeeMappingRequest = createUserPayeeMappingRequest(userGuid, payeeNumber);
		
		ResponseEntity<UserPayeeMappingResponse> response = userPayeeMappingController.addUserPayeeMapping(userPayeeMappingRequest);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		UserPayeeMappingResponse userPayeeMappingResponse = response.getBody();
		assertNotNull(userPayeeMappingResponse);
		assertEquals(userGuid, userPayeeMappingResponse.getUserGuid());
		assertEquals(payeeNumber, userPayeeMappingResponse.getPayeeNumber());		
	}

	@Test
	public void testAddUserPayeeMapping_fail_missing_user_guid() {

		String userGuid = null;		
		String payeeNumber = "00063";
		
		UserPayeeMappingRequest userPayeeMappingRequest = createUserPayeeMappingRequest(userGuid, payeeNumber);
		
		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> userPayeeMappingController.addUserPayeeMapping(userPayeeMappingRequest))
		.withMessage("400 BAD_REQUEST \"Missing value in required request field userGuid.\"");
	}
	
	@Test
	public void testAddUserPayeeMapping_fail_missing_user_guid_empty_value() {

		String userGuid = " "; //Should fail for anything except a populated String		
		String payeeNumber = "00063";
		
		UserPayeeMappingRequest userPayeeMappingRequest = createUserPayeeMappingRequest(userGuid, payeeNumber);
		
		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> userPayeeMappingController.addUserPayeeMapping(userPayeeMappingRequest))
		.withMessage("400 BAD_REQUEST \"Missing value in required request field userGuid.\"");		
	}
	
	@Test
	public void testAddUserPayeeMapping_fail_missing_payee_number() {
		
		String userGuid = UUID.randomUUID().toString();		
		String payeeNumber = "   ";  //Should fail for anything except a populated String
		
		UserPayeeMappingRequest userPayeeMappingRequest = createUserPayeeMappingRequest(userGuid, payeeNumber);
		
		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> userPayeeMappingController.addUserPayeeMapping(userPayeeMappingRequest))
		.withMessage("400 BAD_REQUEST \"Missing value in required request field payeeNumber.\"");		
	}
	
	@Test
	public void testAddUserPayeeMapping_fail_user_already_exists() {
		
		String userGuid = "a9c3b536-4598-411a-bda2-4068d6b5cc20";		
		String payeeNumber = "00063";
		
		UserPayeeMappingRequest userPayeeMappingRequest = createUserPayeeMappingRequest(userGuid, payeeNumber);
		
		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> userPayeeMappingController.addUserPayeeMapping(userPayeeMappingRequest))
		.withMessage("409 CONFLICT \"Entity already exists.\"; nested exception is ca.bc.gov.hlth.hnweb.exception.UserPayeeMappingException: Entity already exists.");
	}
	
    @Test
    public void testGetUserPayeeMapping_success() {
        
        String userGuid = "14100f9b-7daa-4938-a833-c8c56a5988e9";       
        final String payeeNumber = "00023";
        
        ResponseEntity<UserPayeeMappingResponse> response = userPayeeMappingController.getUserPayeeMapping(userGuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserPayeeMappingResponse userPayeeMappingResponse = response.getBody();
        assertNotNull(userPayeeMappingResponse);
        assertEquals(userGuid, userPayeeMappingResponse.getUserGuid());
        assertEquals(payeeNumber, userPayeeMappingResponse.getPayeeNumber());
        assertTrue(userPayeeMappingResponse.getPayeeIsActive());
    }

	@Test
	public void testGetUserPayeeMapping_success_archived_payee() {
		
		String userGuid = "a9c3b536-4598-411a-bda2-4068d6b5cc20";		
		final String payeeNumber = "00053";
		
		ResponseEntity<UserPayeeMappingResponse> response = userPayeeMappingController.getUserPayeeMapping(userGuid);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		UserPayeeMappingResponse userPayeeMappingResponse = response.getBody();
		assertNotNull(userPayeeMappingResponse);
		assertEquals(userGuid, userPayeeMappingResponse.getUserGuid());
		assertEquals(payeeNumber, userPayeeMappingResponse.getPayeeNumber());
		assertEquals(false, userPayeeMappingResponse.getPayeeIsActive());
	}

    @Test
    public void testGetUserPayeeMapping_success_no_payee_status() {
        
        String userGuid = "f33c9e07-6f49-46c2-90c2-6e0013729c9d";       
        final String payeeNumber = "X0054";
        
        ResponseEntity<UserPayeeMappingResponse> response = userPayeeMappingController.getUserPayeeMapping(userGuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserPayeeMappingResponse userPayeeMappingResponse = response.getBody();
        assertNotNull(userPayeeMappingResponse);
        assertEquals(userGuid, userPayeeMappingResponse.getUserGuid());
        assertEquals(payeeNumber, userPayeeMappingResponse.getPayeeNumber());
        assertEquals(false, userPayeeMappingResponse.getPayeeIsActive());
    }

	@Test
	public void testGetUserPayeeMapping_fail_not_found() {
		
		String userGuid = UUID.randomUUID().toString();		

		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> userPayeeMappingController.getUserPayeeMapping(userGuid))
		.withMessage("404 NOT_FOUND \"Entity not found for ID %s\"", userGuid);
	}

	@Test
	public void testUpdateUserPayeeMapping_success() {
		
		String userGuid = "a9c3b536-4598-411a-bda2-4068d6b5cc20";		
		String updatedPayeeNumber = "00023";
		
		UserPayeeMappingRequest userPayeeMappingRequest = createUserPayeeMappingRequest(userGuid, updatedPayeeNumber);
		
		ResponseEntity<UserPayeeMappingResponse> response = userPayeeMappingController.updateUserPayeeMapping(userPayeeMappingRequest, userGuid);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		UserPayeeMappingResponse updatedUserPayeeMappingResponse = response.getBody();
		assertNotNull(updatedUserPayeeMappingResponse);
		assertEquals(userGuid, updatedUserPayeeMappingResponse.getUserGuid());
		assertEquals(updatedPayeeNumber, updatedUserPayeeMappingResponse.getPayeeNumber());
	}
	
	@Test
	public void testUpdateUserPayeeMapping_fail_does_not_exist() {
		
		String userGuid = "a9c3b536-4598-411a-bda2-4068d6b5cc20";		
		String updatedPayeeNumber = "00023";
		
		UserPayeeMappingRequest userPayeeMappingRequest = createUserPayeeMappingRequest(userGuid, updatedPayeeNumber);
		
		String incorrectUserGuid = UUID.randomUUID().toString();

		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> userPayeeMappingController.updateUserPayeeMapping(userPayeeMappingRequest, incorrectUserGuid))
		.withMessage("404 NOT_FOUND \"Entity not found.\"; nested exception is ca.bc.gov.hlth.hnweb.exception.UserPayeeMappingException: Entity not found.");
	}
	
	@Test
	public void testUpdateUserPayeeMapping_fail_missing_payee_number() {
		
		String userGuid = "a9c3b536-4598-411a-bda2-4068d6b5cc20";		
		String updatedPayeeNumber = "  ";
		
		UserPayeeMappingRequest userPayeeMappingRequest = createUserPayeeMappingRequest(userGuid, updatedPayeeNumber);		
		
		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> userPayeeMappingController.updateUserPayeeMapping(userPayeeMappingRequest, userGuid))
		.withMessage("400 BAD_REQUEST \"Missing value in required request field payeeNumber\"");		
	}
	
	@Test
	public void testDeleteUserPayeeMapping_success() {
		
		String userGuid = "a9c3b536-4598-411a-bda2-4068d6b5cc20";
		
		ResponseEntity<Void> deleteRresponse = userPayeeMappingController.deleteUserPayeeMapping(userGuid);
		assertEquals(HttpStatus.NO_CONTENT, deleteRresponse.getStatusCode());
		assertEquals(deleteRresponse.getBody(), null);
		
		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> userPayeeMappingController.getUserPayeeMapping(userGuid))
		.withMessage("404 NOT_FOUND \"Entity not found for ID %s\"", userGuid);
	}

	@Test
	public void testDeleteUserPayeeMapping_fail_not_found() {
		
		String userGuid = UUID.randomUUID().toString();		

		assertThatExceptionOfType(ResponseStatusException.class)
		.isThrownBy(() -> userPayeeMappingController.deleteUserPayeeMapping(userGuid))
		.withMessage("404 NOT_FOUND \"Entity not found.\"; nested exception is ca.bc.gov.hlth.hnweb.exception.UserPayeeMappingException: Entity not found.");
	}

	private UserPayeeMappingRequest createUserPayeeMappingRequest(String userGuid, String payeeNumber) {
		UserPayeeMappingRequest userPayeeMappingRequest = new UserPayeeMappingRequest();
		userPayeeMappingRequest.setUserGuid(userGuid);
		userPayeeMappingRequest.setPayeeNumber(payeeNumber);
		return userPayeeMappingRequest;
	}
	
}


