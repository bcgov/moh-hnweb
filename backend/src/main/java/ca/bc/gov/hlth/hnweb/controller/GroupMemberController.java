package ca.bc.gov.hlth.hnweb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPED0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPEE0Converter;
import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPED0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPEE0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateGroupMemberResponse;
import ca.bc.gov.hlth.hnweb.service.GroupMemberService;

/**
 * Handle requests related to Eligibility
 *
 */
@RequestMapping("/group-member")
@RestController
public class GroupMemberController {

	private static final Logger logger = LoggerFactory.getLogger(GroupMemberController.class);

	@Autowired
	private GroupMemberService groupMemberService;

	/**
	 * Updates a group member's number and/or department.
	 * Maps to the legacy R35.
	 *  
	 * @param inquirePhnRequest
	 * @return The result of the query
	 */
	@PostMapping("/update-group-member")
	public ResponseEntity<UpdateGroupMemberResponse> updateGroupMember(@Valid @RequestBody UpdateGroupMemberRequest updateGroupMemberRequest) {

		try {
			// Handle the department number
			RPBSPED0Converter rpbsped0Converter = new RPBSPED0Converter();
			RPBSPED0 ped0Request = rpbsped0Converter.convertRequest(updateGroupMemberRequest);
			RPBSPED0 ped0Response = groupMemberService.updateGroupMemberDepartmentNumber(ped0Request);
			
			// Handle the employee number
			RPBSPEE0Converter rpbspee0Converter = new RPBSPEE0Converter();
			RPBSPEE0 pee0Request = rpbspee0Converter.convertRequest(updateGroupMemberRequest);
			RPBSPEE0 pee0Response = groupMemberService.updateGroupMemberEmployeeNumber(pee0Request);
				
			// Combine the results
			UpdateGroupMemberResponse deptNumberResponse = rpbsped0Converter.convertResponse(ped0Response);
			UpdateGroupMemberResponse empNumberResponse = rpbspee0Converter.convertResponse(pee0Response);
			UpdateGroupMemberResponse updateGroupMemberResponse = handleUpdateGroupMemberResponse(deptNumberResponse, empNumberResponse);
					
			ResponseEntity<UpdateGroupMemberResponse> response = ResponseEntity.ok(updateGroupMemberResponse);

			logger.info("updateGroupMember response: {} ", updateGroupMemberResponse);
			return response;	
		} catch (HNWebException hwe) {
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, hwe.getMessage(), hwe);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /inquire-phn request", hwe);				
			}
		} catch (WebClientException wce) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, wce.getMessage(), wce);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /inquire-phn request", e);
		}
		
	}
	
	private UpdateGroupMemberResponse handleUpdateGroupMemberResponse(UpdateGroupMemberResponse deptNumberResponse, UpdateGroupMemberResponse empNumberResponse) {
		UpdateGroupMemberResponse response = new UpdateGroupMemberResponse();
		// Errors are highest priority, followed by warning, followed by Success
		if (deptNumberResponse.getStatus() == StatusEnum.ERROR || empNumberResponse.getStatus() == StatusEnum.ERROR) {
			response.setMessage(generateErrorWarningMessage(deptNumberResponse, empNumberResponse));
			response.setStatus(StatusEnum.ERROR);
		} else if (deptNumberResponse.getStatus() == StatusEnum.WARNING || empNumberResponse.getStatus() == StatusEnum.WARNING) {
			response.setMessage(generateErrorWarningMessage(deptNumberResponse, empNumberResponse));
			response.setStatus(StatusEnum.WARNING);
		} else {
			// Just grab the message from the first response. We don't need two success messages
			response.setMessage(deptNumberResponse.getMessage());
			response.setStatus(StatusEnum.SUCCESS);
		}
		return response;		
	}
	
	private String generateErrorWarningMessage(UpdateGroupMemberResponse deptNumberResponse, UpdateGroupMemberResponse empNumberResponse) {
		List<String> messages = new ArrayList<>();
		
		// Include all messages which aren't Success			
		if (deptNumberResponse.getStatus() != StatusEnum.SUCCESS) {
			messages.add(deptNumberResponse.getMessage());
		}
		if (empNumberResponse.getStatus() != StatusEnum.SUCCESS) {
			messages.add(empNumberResponse.getMessage());
		}
		
		return String.join("\n", messages);
	}

}