package ca.bc.gov.hlth.hnweb.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPED0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPEE0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPWB0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPWC0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPWP0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPXP0Converter;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPED0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPEE0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPWB0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPWC0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPWP0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPXP0;
import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddDependentRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddDependentResponse;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddGroupMemberResponse;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelDependentRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelDependentResponse;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelGroupMemberResponse;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateNumberAndDeptRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateNumberAndDeptResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.service.GroupMemberService;

/**
 * Handle requests related to Group Member.
 *
 */
@RequestMapping("/group-member")
@RestController
public class GroupMemberController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(GroupMemberController.class);

	@Autowired
	private GroupMemberService groupMemberService;

	/**
	 * Add a dependent to a group member's coverage.
	 * Maps to the legacy R31.
	 *  
	 * @param addDependentRequest
	 * @return The result of the operation.
	 */
	@PostMapping("/add-dependent")
	public ResponseEntity<AddDependentResponse> addDependent(@Valid @RequestBody AddDependentRequest addDependentRequest, HttpServletRequest request) {

		Transaction transaction = auditAddDependentStart(addDependentRequest, request);

		try {
			RPBSPWB0Converter converter = new RPBSPWB0Converter();
			RPBSPWB0 rpbspwb0Request = converter.convertRequest(addDependentRequest);
			RPBSPWB0 rpbspwb0Response = groupMemberService.addDependent(rpbspwb0Request, transaction);
			AddDependentResponse addDependentResponse = converter.convertResponse(rpbspwb0Response);
					
			ResponseEntity<AddDependentResponse> response = ResponseEntity.ok(addDependentResponse);

			logger.info("addDependentResponse response: {} ", addDependentResponse);
			
			transactionComplete(transaction);
			addAffectedParty(transaction, IdentifierType.PHN, addDependentResponse.getPhn(), AffectedPartyDirection.OUTBOUND);
			
			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	/**
	 * Updates a group member's number and/or department.
	 * Maps to the legacy R34.
	 *  
	 * @param updateNumberAndDeptRequest
	 * @return The result of the update.
	 */
	@PostMapping("/update-number-and-dept")
	public ResponseEntity<UpdateNumberAndDeptResponse> updateNumberAndDept(@Valid @RequestBody UpdateNumberAndDeptRequest updateNumberAndDeptRequest, HttpServletRequest request) {
		
		Transaction transaction = auditUpdateNumberAndDeptStart(updateNumberAndDeptRequest, request);

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
				RPBSPED0 rpbsped0Response = groupMemberService.updateGroupMemberDepartmentNumber(rpbsped0Request, transaction);
				deptNumberResponse = rpbsped0Converter.convertResponse(rpbsped0Response);
			}

			// Handle the group member/employee number
			UpdateNumberAndDeptResponse empNumberResponse = new UpdateNumberAndDeptResponse();
			if (StringUtils.isNotBlank(updateNumberAndDeptRequest.getGroupMemberNumber())) {
				RPBSPEE0Converter rpbspee0Converter = new RPBSPEE0Converter();
				RPBSPEE0 rpbspee0Request = rpbspee0Converter.convertRequest(updateNumberAndDeptRequest);
				RPBSPEE0 rpbspee0Response = groupMemberService.updateGroupMemberEmployeeNumber(rpbspee0Request, transaction);
				empNumberResponse = rpbspee0Converter.convertResponse(rpbspee0Response);
			}
				
			// Combine the results
			UpdateNumberAndDeptResponse updateNumberAndDeptResponse = handleUpdateGroupMemberResponse(deptNumberResponse, empNumberResponse);
					
			ResponseEntity<UpdateNumberAndDeptResponse> response = ResponseEntity.ok(updateNumberAndDeptResponse);

			logger.info("updateNumberAndDept response: {} ", updateNumberAndDeptResponse);

			transactionComplete(transaction);
			addAffectedParty(transaction, IdentifierType.PHN, updateNumberAndDeptResponse.getPhn(), AffectedPartyDirection.OUTBOUND);
			
			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
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
	public ResponseEntity<AddGroupMemberResponse> addGroupMember(@Valid @RequestBody AddGroupMemberRequest addGroupMemberRequest, HttpServletRequest request) {

		Transaction transaction = auditAddGroupMemberStart(addGroupMemberRequest, request);

		try {
			RPBSPXP0Converter converter = new RPBSPXP0Converter();
			RPBSPXP0 rpbspxp0 = converter.convertRequest(addGroupMemberRequest);
			RPBSPXP0 rpbspxp0Response = groupMemberService.addGroupMember(rpbspxp0, transaction);
			AddGroupMemberResponse addGroupMemberResponse = converter.convertResponse(rpbspxp0Response);
					
			ResponseEntity<AddGroupMemberResponse> response = ResponseEntity.ok(addGroupMemberResponse);

			logger.info("addGroupMemberResponse response: {} ", addGroupMemberResponse);
			
			transactionComplete(transaction);
			addAffectedParty(transaction, IdentifierType.PHN, addGroupMemberResponse.getPhn(), AffectedPartyDirection.OUTBOUND);

			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
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
	public ResponseEntity<CancelGroupMemberResponse> cancelGroupMember(@Valid @RequestBody CancelGroupMemberRequest cancelGroupMemberRequest, HttpServletRequest request) {

		Transaction transaction = auditCancelGroupMemberStart(cancelGroupMemberRequest, request);

		try {
			RPBSPWC0Converter converter = new RPBSPWC0Converter();
			RPBSPWC0 rpbspwc0 = converter.convertRequest(cancelGroupMemberRequest);
			RPBSPWC0 rpbspwc0Response = groupMemberService.cancelGroupMember(rpbspwc0, transaction);
			CancelGroupMemberResponse cancelGroupMemberResponse = converter.convertResponse(rpbspwc0Response);
					
			ResponseEntity<CancelGroupMemberResponse> response = ResponseEntity.ok(cancelGroupMemberResponse);

			logger.info("cancelGroupMemberResponse response: {} ", cancelGroupMemberResponse);

			transactionComplete(transaction);
			addAffectedParty(transaction, IdentifierType.PHN, cancelGroupMemberResponse.getPhn(), AffectedPartyDirection.OUTBOUND);

			return response;	
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
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
	public ResponseEntity<CancelDependentResponse> cancelDependent(@Valid @RequestBody CancelDependentRequest cancelDependentRequest, HttpServletRequest request) {

		Transaction transaction = auditCancelDependentStart(cancelDependentRequest, request);

		try {
			RPBSPWP0Converter converter = new RPBSPWP0Converter();
			RPBSPWP0 rpbspwp0 = converter.convertRequest(cancelDependentRequest);
			RPBSPWP0 rpbspwc0Response = groupMemberService.cancelDependent(rpbspwp0, transaction);
			CancelDependentResponse cancelDependentResponse = converter.convertResponse(rpbspwc0Response);
					
			ResponseEntity<CancelDependentResponse> response = ResponseEntity.ok(cancelDependentResponse);

			logger.info("CancelDependentResponse response: {} ", cancelDependentResponse);
			
			transactionComplete(transaction);
			addAffectedParty(transaction, IdentifierType.PHN, cancelDependentResponse.getPhn(), AffectedPartyDirection.OUTBOUND);

			return response;	
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
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

	private Transaction auditAddGroupMemberStart(AddGroupMemberRequest addGroupMemberRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.ADD_GROUP_MEMBER);
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, addGroupMemberRequest.getGroupNumber(), AffectedPartyDirection.INBOUND);
		addAffectedParty(transaction, IdentifierType.PHN, addGroupMemberRequest.getPhn(), AffectedPartyDirection.INBOUND);
		if (StringUtils.isNotBlank(addGroupMemberRequest.getGroupMemberNumber())) {
			addAffectedParty(transaction, IdentifierType.GROUP_MEMBER_NUMBER, addGroupMemberRequest.getGroupMemberNumber(), AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(addGroupMemberRequest.getDepartmentNumber())) {
			addAffectedParty(transaction, IdentifierType.DEPARTMENT_NUMBER, addGroupMemberRequest.getDepartmentNumber(), AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(addGroupMemberRequest.getSpousePhn())) {
			addAffectedParty(transaction, IdentifierType.PHN, addGroupMemberRequest.getSpousePhn(), AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(addGroupMemberRequest.getDependentPhn1())) {
			addAffectedParty(transaction, IdentifierType.PHN, addGroupMemberRequest.getDependentPhn1(), AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(addGroupMemberRequest.getDependentPhn2())) {
			addAffectedParty(transaction, IdentifierType.PHN, addGroupMemberRequest.getDependentPhn2(), AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(addGroupMemberRequest.getDependentPhn3())) {
			addAffectedParty(transaction, IdentifierType.PHN, addGroupMemberRequest.getDependentPhn3(), AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(addGroupMemberRequest.getDependentPhn4())) {
			addAffectedParty(transaction, IdentifierType.PHN, addGroupMemberRequest.getDependentPhn4(), AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(addGroupMemberRequest.getDependentPhn5())) {
			addAffectedParty(transaction, IdentifierType.PHN, addGroupMemberRequest.getDependentPhn5(), AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(addGroupMemberRequest.getDependentPhn6())) {
			addAffectedParty(transaction, IdentifierType.PHN, addGroupMemberRequest.getDependentPhn6(), AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(addGroupMemberRequest.getDependentPhn7())) {
			addAffectedParty(transaction, IdentifierType.PHN, addGroupMemberRequest.getDependentPhn7(), AffectedPartyDirection.INBOUND);
		}
		return transaction;
	}

	private Transaction auditAddDependentStart(AddDependentRequest addDependentRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.ADD_DEPENDENT);
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, addDependentRequest.getGroupNumber(), AffectedPartyDirection.INBOUND);
		addAffectedParty(transaction, IdentifierType.PHN, addDependentRequest.getPhn(), AffectedPartyDirection.INBOUND);
		addAffectedParty(transaction, IdentifierType.PHN, addDependentRequest.getDependentPhn(), AffectedPartyDirection.INBOUND);
		return transaction;
	}

	private Transaction auditUpdateNumberAndDeptStart(UpdateNumberAndDeptRequest updateNumberAndDeptRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.UPDATE_NUMBER_AND_OR_DEPT);
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, updateNumberAndDeptRequest.getGroupNumber(), AffectedPartyDirection.INBOUND);
		addAffectedParty(transaction, IdentifierType.PHN, updateNumberAndDeptRequest.getPhn(), AffectedPartyDirection.INBOUND);
		if (StringUtils.isNotBlank(updateNumberAndDeptRequest.getGroupMemberNumber())) {
			addAffectedParty(transaction, IdentifierType.GROUP_MEMBER_NUMBER, updateNumberAndDeptRequest.getGroupMemberNumber(), AffectedPartyDirection.INBOUND);
		}
		if (StringUtils.isNotBlank(updateNumberAndDeptRequest.getDepartmentNumber())) {
			addAffectedParty(transaction, IdentifierType.DEPARTMENT_NUMBER, updateNumberAndDeptRequest.getDepartmentNumber(), AffectedPartyDirection.INBOUND);
		}
		return transaction;
	}

	private Transaction auditCancelGroupMemberStart(CancelGroupMemberRequest cancelGroupMemberRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.CANCEL_GROUP_MEMBER);
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, cancelGroupMemberRequest.getGroupNumber(), AffectedPartyDirection.INBOUND);
		addAffectedParty(transaction, IdentifierType.PHN, cancelGroupMemberRequest.getPhn(), AffectedPartyDirection.INBOUND);
		return transaction;
	}
	
	private Transaction auditCancelDependentStart(CancelDependentRequest cancelDependentRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.CANCEL_DEPENDENT);
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, cancelDependentRequest.getGroupNumber(), AffectedPartyDirection.INBOUND);
		addAffectedParty(transaction, IdentifierType.PHN, cancelDependentRequest.getPhn(), AffectedPartyDirection.INBOUND);
		addAffectedParty(transaction, IdentifierType.PHN, cancelDependentRequest.getDependentPhn(), AffectedPartyDirection.INBOUND);
		return transaction;
	}

}