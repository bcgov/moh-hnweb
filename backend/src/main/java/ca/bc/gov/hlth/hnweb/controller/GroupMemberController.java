package ca.bc.gov.hlth.hnweb.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
 * Handle requests related to Group Member.
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
	 * Maps to the legacy R34.
	 *  
	 * @param inquirePhnRequest
	 * @return The result of the query
	 */
	@PostMapping("/update-group-member")
	public ResponseEntity<UpdateGroupMemberResponse> updateGroupMember(@Valid @RequestBody UpdateGroupMemberRequest updateGroupMemberRequest) {
		// Do basic validation since there's no point in calling the downstream systems if the data isn't valid
		if (StringUtils.isBlank(updateGroupMemberRequest.getDepartmentNumber()) && StringUtils.isBlank(updateGroupMemberRequest.getGroupMemberNumber())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department Number or Group Number is required");	
		}

		try {	
			// Handle the department number
			UpdateGroupMemberResponse deptNumberResponse = new UpdateGroupMemberResponse();
			if (StringUtils.isNotBlank(updateGroupMemberRequest.getDepartmentNumber())) {
				RPBSPED0Converter rpbsped0Converter = new RPBSPED0Converter();
				RPBSPED0 ped0Request = rpbsped0Converter.convertRequest(updateGroupMemberRequest);
				RPBSPED0 ped0Response = groupMemberService.updateGroupMemberDepartmentNumber(ped0Request);
				deptNumberResponse = rpbsped0Converter.convertResponse(ped0Response);
			}

			// Handle the group member/employee number
			UpdateGroupMemberResponse empNumberResponse = new UpdateGroupMemberResponse();
			if (StringUtils.isNotBlank(updateGroupMemberRequest.getGroupMemberNumber())) {
				RPBSPEE0Converter rpbspee0Converter = new RPBSPEE0Converter();
				RPBSPEE0 pee0Request = rpbspee0Converter.convertRequest(updateGroupMemberRequest);
				RPBSPEE0 pee0Response = groupMemberService.updateGroupMemberEmployeeNumber(pee0Request);
				empNumberResponse = rpbspee0Converter.convertResponse(pee0Response);
			}
				
			// Combine the results
			UpdateGroupMemberResponse updateGroupMemberResponse = handleUpdateGroupMemberResponse(deptNumberResponse, empNumberResponse);
					
			ResponseEntity<UpdateGroupMemberResponse> response = ResponseEntity.ok(updateGroupMemberResponse);

			logger.info("updateGroupMember response: {} ", updateGroupMemberResponse);
			return response;	
		} catch (HNWebException hwe) {
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, hwe.getMessage(), hwe);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /update-group-member request", hwe);				
			}
		} catch (WebClientException wce) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, wce.getMessage(), wce);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /update-group-member request", e);
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
			// Just grab the message from the first populated response. We don't need two success messages
			response.setMessage(StringUtils.isNotBlank(deptNumberResponse.getMessage()) ? deptNumberResponse.getMessage() : empNumberResponse.getMessage());
			response.setStatus(StatusEnum.SUCCESS);
		}
		response.setPhn(StringUtils.isNotBlank(deptNumberResponse.getPhn()) ? deptNumberResponse.getPhn() : empNumberResponse.getPhn());
		return response;		
	}
	
	private String generateErrorWarningMessage(UpdateGroupMemberResponse deptNumberResponse, UpdateGroupMemberResponse empNumberResponse) {
		Set<String> messages = new HashSet<>();
		
		// Include all Error and Warning messages but			
		if (deptNumberResponse.getStatus() == StatusEnum.ERROR || deptNumberResponse.getStatus() == StatusEnum.WARNING) {
			messages.add(deptNumberResponse.getMessage());
		}
		if (empNumberResponse.getStatus() == StatusEnum.ERROR || empNumberResponse.getStatus() == StatusEnum.WARNING) {
			messages.add(empNumberResponse.getMessage());
		}
		
		return String.join("\n", messages);
	}

}