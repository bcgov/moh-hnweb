package ca.bc.gov.hlth.hnweb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPCI0Converter;
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPMC0Converter;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPCI0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPMC0;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.ContractInquiryRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.ContractInquiryResponse;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.GetContractPeriodsRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.GetContractPeriodsResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.security.TransactionType;
import ca.bc.gov.hlth.hnweb.service.MspContractsService;

/**
 * Handle requests related to MSP Contracts.
 *
 */
@RequestMapping("/msp-contracts")
@RestController
public class MspContractsController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MspContractsController.class);

	@Autowired
	private MspContractsService mspContractsService;

	/**
	 * Get Contracts Periods for a Personal Health Number (PHN) Inquiry
	 * Maps to the legacy R32.
	 *  
	 * @param getContractPeriodsRequest
	 * @param request 
	 * @return The result of the operation.
	 */
	@PostMapping("/get-contract-periods")
	public ResponseEntity<GetContractPeriodsResponse> getContractPeriods(@Valid @RequestBody GetContractPeriodsRequest getContractPeriodsRequest, HttpServletRequest request) {

		Transaction transaction = auditGetContractPeriodsStart(getContractPeriodsRequest, request);

		try {
			RPBSPMC0Converter converter = new RPBSPMC0Converter();
			RPBSPMC0 rpbspmc0Request = converter.convertRequest(getContractPeriodsRequest);
			RPBSPMC0 rpbspmc0Response = mspContractsService.getContractPeriods(rpbspmc0Request, transaction);
			GetContractPeriodsResponse getContractPeriodsResponse = converter.convertResponse(rpbspmc0Response);
					
			ResponseEntity<GetContractPeriodsResponse> response = ResponseEntity.ok(getContractPeriodsResponse);

			logger.info("getContractPeriodsResponse response: {} ", getContractPeriodsResponse);

			auditGetContractPeriodsEnd(transaction, getContractPeriodsResponse);

			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}
	
	 /**
	 * Get MSP Coverage info for a Personal Health Number (PHN) of a group Inquiry
	 * Maps to the legacy R40.
	 *  
	 * @param contractInquireRequest
	 * @return The result of the operation.
	 */
	@PostMapping("/inquire-contract")
	public ResponseEntity<ContractInquiryResponse> inquireContract(@Valid @RequestBody ContractInquiryRequest contractInquireRequest, HttpServletRequest request) {
		
		Transaction transaction = auditContractInquiryStart(contractInquireRequest, request);
		try {
			RPBSPCI0Converter converter = new RPBSPCI0Converter();
			RPBSPCI0 rpbspci0Request = converter.convertRequest(contractInquireRequest);
			RPBSPCI0 rpbspci0Response = mspContractsService.inquireContract(rpbspci0Request);
			ContractInquiryResponse contractInquiryResponse = converter.convertResponse(rpbspci0Response);
					
			ResponseEntity<ContractInquiryResponse> response = ResponseEntity.ok(contractInquiryResponse);

			logger.info("ContractInquiry response: {} ", rpbspci0Response);
			
			auditContractInquiryEnd(transaction, contractInquiryResponse);
			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}	
	}

	private Transaction auditGetContractPeriodsStart(GetContractPeriodsRequest getContractPeriodsRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.GET_CONTRACT_PERIODS);
		addAffectedParty(transaction, IdentifierType.PHN, getContractPeriodsRequest.getPhn());
		return transaction;
	}

	private void auditGetContractPeriodsEnd(Transaction transaction, GetContractPeriodsResponse getContractPeriodsResponse) {
		List<String> auditedPhns = new ArrayList<>();
		List<String> auditedGroupNumbers = new ArrayList<>();

		transactionComplete(transaction);		
		addAffectedParty(transaction, IdentifierType.PHN, getContractPeriodsResponse.getPhn());
		auditedPhns.add(getContractPeriodsResponse.getPhn());
		
		getContractPeriodsResponse.getBeneficiaryContractPeriods().forEach(bcp -> {
			if(!auditedPhns.contains(bcp.getPhn())) {
				addAffectedParty(transaction, IdentifierType.PHN, bcp.getPhn());				
				auditedPhns.add(bcp.getPhn());
			}
			if(!auditedGroupNumbers.contains(bcp.getGroupNumber())) {
				addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, bcp.getGroupNumber());
				auditedGroupNumbers.add(bcp.getGroupNumber());
			}
			if(!auditedPhns.contains(bcp.getContractHolder())) {
				addAffectedParty(transaction, IdentifierType.PHN, bcp.getContractHolder());
				auditedPhns.add(bcp.getContractHolder());
			}
		});
	}
	
	private Transaction auditContractInquiryStart(ContractInquiryRequest contractInquiryRequest, HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.CONTRACT_INQUIRY);
		addAffectedParty(transaction, IdentifierType.PHN, contractInquiryRequest.getPhn());
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, contractInquiryRequest.getGroupNumber());
		return transaction;
	}

	private void auditContractInquiryEnd(Transaction transaction, ContractInquiryResponse contractInquiryResponse) {
		List<String> auditedPhns = new ArrayList<>();

		transactionComplete(transaction);		
		addAffectedParty(transaction, IdentifierType.PHN, contractInquiryResponse.getPhn());
		auditedPhns.add(contractInquiryResponse.getPhn());
		
		contractInquiryResponse.getContractInquiryBeneficiaries().forEach(cib -> {
			if(!auditedPhns.contains(cib.getPhn())) {
				addAffectedParty(transaction, IdentifierType.PHN, cib.getPhn());				
				auditedPhns.add(cib.getPhn());
			}			
		});
	}
}