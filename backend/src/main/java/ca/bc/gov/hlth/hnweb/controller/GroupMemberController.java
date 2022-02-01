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
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPWC0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPWP0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPXP0Converter;
import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPED0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPEE0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPWC0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPWP0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPXP0;
import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddGroupMemberResponse;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelDependentRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelDependentResponse;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelGroupMemberResponse;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateNumberAndDeptRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateNumberAndDeptResponse;
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
	 * @param updateNumberAndDeptRequest
	 * @return The result of the update.
	 */
	@PostMapping("/update-number-and-dept")
	public ResponseEntity<UpdateNumberAndDeptResponse> updateNumberAndDept(@Valid @RequestBody UpdateNumberAndDeptRequest updateNumberAndDeptRequest) {
		// Do basic validation since there's no point in calling the downstream systems if the data isn't valid
		if (StringUtils.isBlank(updateNumberAndDeptRequest.getDepartmentNumber()) && StringUtils.isBlank(updateNumberAndDeptRequest.getGroupMemberNumber())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department Number or Group Number is required");	
		}

		try {	
			// Handle the department number
			UpdateNumberAndDeptResponse deptNumberResponse = new UpdateNumberAndDeptResponse();
			if (StringUtils.isNotBlank(updateNumberAndDeptRequest.getDepartmentNumber())) {
				RPBSPED0Converter rpbsped0Converter = new RPBSPED0Converter();
				RPBSPED0 rpbsped0Request = rpbsped0Converter.convertRequest(updateNumberAndDeptRequest);
				RPBSPED0 rpbsped0Response = groupMemberService.updateGroupMemberDepartmentNumber(rpbsped0Request);
				deptNumberResponse = rpbsped0Converter.convertResponse(rpbsped0Response);
			}

			// Handle the group member/employee number
			UpdateNumberAndDeptResponse empNumberResponse = new UpdateNumberAndDeptResponse();
			if (StringUtils.isNotBlank(updateNumberAndDeptRequest.getGroupMemberNumber())) {
				RPBSPEE0Converter rpbspee0Converter = new RPBSPEE0Converter();
				RPBSPEE0 rpbspee0Request = rpbspee0Converter.convertRequest(updateNumberAndDeptRequest);
				RPBSPEE0 rpbspee0Response = groupMemberService.updateGroupMemberEmployeeNumber(rpbspee0Request);
				empNumberResponse = rpbspee0Converter.convertResponse(rpbspee0Response);
			}
				
			// Combine the results
			UpdateNumberAndDeptResponse updateNumberAndDeptResponse = handleUpdateGroupMemberResponse(deptNumberResponse, empNumberResponse);
					
			ResponseEntity<UpdateNumberAndDeptResponse> response = ResponseEntity.ok(updateNumberAndDeptResponse);

			logger.info("updateNumberAndDept response: {} ", updateNumberAndDeptResponse);
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

	/**
	 * Add a group member and spouse/dependents.
	 * Maps to the legacy R30.
	 *  
	 * @param addGroupMemberRequest
	 * @return The result of the operation.
	 */
	@PostMapping("/add-group-member")
	public ResponseEntity<AddGroupMemberResponse> addGroupMember(@Valid @RequestBody AddGroupMemberRequest addGroupMemberRequest) {

		try {
			RPBSPXP0Converter converter = new RPBSPXP0Converter();
			RPBSPXP0 rpbspxp0 = converter.convertRequest(addGroupMemberRequest);
			RPBSPXP0 rpbspxp0Response = groupMemberService.addGroupMember(rpbspxp0);
			AddGroupMemberResponse addGroupMemberResponse = converter.convertResponse(rpbspxp0Response);
					
			ResponseEntity<AddGroupMemberResponse> response = ResponseEntity.ok(addGroupMemberResponse);

			logger.info("addGroupMemberResponse response: {} ", addGroupMemberResponse);
			return response;	
		} catch (HNWebException hwe) {
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, hwe.getMessage(), hwe);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /add-group-member request", hwe);				
			}
		} catch (WebClientException wce) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, wce.getMessage(), wce);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /add-group-member request", e);
		}
		
	}

	/**
	 * Cancels a group member's coverage.
	 * Maps to the legacy R35.
	 *  
	 * @param cancelGroupMemberRequest
	 * @return The result of the operation.
	 */
	@PostMapping("/cancel-group-member")
	public ResponseEntity<CancelGroupMemberResponse> cancelGroupMember(@Valid @RequestBody CancelGroupMemberRequest cancelGroupMemberRequest) {

		try {
			RPBSPWC0Converter converter = new RPBSPWC0Converter();
			RPBSPWC0 rpbspwc0 = converter.convertRequest(cancelGroupMemberRequest);
			RPBSPWC0 rpbspwc0Response = groupMemberService.cancelGroupMember(rpbspwc0);
			CancelGroupMemberResponse cancelGroupMemberResponse = converter.convertResponse(rpbspwc0Response);
					
			ResponseEntity<CancelGroupMemberResponse> response = ResponseEntity.ok(cancelGroupMemberResponse);

			logger.info("cancelGroupMemberResponse response: {} ", cancelGroupMemberResponse);
			return response;	
		} catch (HNWebException hwe) {
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, hwe.getMessage(), hwe);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /cancel-group-member request", hwe);				
			}
		} catch (WebClientException wce) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, wce.getMessage(), wce);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /cancel-group-member request", e);
		}
		
	}
	
	/**
	 * Cancels a group member's dependent coverage.
	 * Maps to the legacy R36.
	 *  
	 * @param cancelDependentRequest
	 * @return The result of the operation.
	 */
	@PostMapping("/cancel-dependent")
	public ResponseEntity<CancelDependentResponse> cancelDependent(@Valid @RequestBody CancelDependentRequest cancelDependentRequest) {

		try {
			RPBSPWP0Converter converter = new RPBSPWP0Converter();
			RPBSPWP0 rpbspwp0 = converter.convertRequest(cancelDependentRequest);
			RPBSPWP0 rpbspwc0Response = groupMemberService.cancelDependent(rpbspwp0);
			CancelDependentResponse cancelDependentResponse = converter.convertResponse(rpbspwc0Response);
					
			ResponseEntity<CancelDependentResponse> response = ResponseEntity.ok(cancelDependentResponse);

			logger.info("CancelDependentResponse response: {} ", cancelDependentResponse);
			return response;	
		} catch (HNWebException hwe) {
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, hwe.getMessage(), hwe);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /cancel-dependent request", hwe);				
			}
		} catch (WebClientException wce) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, wce.getMessage(), wce);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /cancel-dependent request", e);
		}
		
	}


	private UpdateNumberAndDeptResponse handleUpdateGroupMemberResponse(UpdateNumberAndDeptResponse deptNumberResponse, UpdateNumberAndDeptResponse empNumberResponse) {
		UpdateNumberAndDeptResponse response = new UpdateNumberAndDeptResponse();
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
	
	private String generateErrorWarningMessage(UpdateNumberAndDeptResponse deptNumberResponse, UpdateNumberAndDeptResponse empNumberResponse) {
		Set<String> messages = new HashSet<>();
		
		// Include all Error and Warning messages but skip Success messages		
		if (deptNumberResponse.getStatus() == StatusEnum.ERROR || deptNumberResponse.getStatus() == StatusEnum.WARNING) {
			messages.add(deptNumberResponse.getMessage());
		}
		if (empNumberResponse.getStatus() == StatusEnum.ERROR || empNumberResponse.getStatus() == StatusEnum.WARNING) {
			messages.add(empNumberResponse.getMessage());
		}
		
		return String.join("\n", messages);
	}

}