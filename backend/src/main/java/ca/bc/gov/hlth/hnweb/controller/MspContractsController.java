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
import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPMA0Converter;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPCI0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPMC0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPMA0;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.ContractInquiryRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.ContractInquiryResponse;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.GetContractPeriodsRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.GetContractPeriodsResponse;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.UpdateContractAddressRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.UpdateContractAddressResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
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
	 * Get Contracts Periods for a Personal Health Number (PHN) Inquiry Maps to the
	 * legacy R32.
	 * 
	 * @param getContractPeriodsRequest
	 * @param request
	 * @return The result of the operation.
	 */
	@PostMapping("/get-contract-periods")
	public ResponseEntity<GetContractPeriodsResponse> getContractPeriods(
			@Valid @RequestBody GetContractPeriodsRequest getContractPeriodsRequest, HttpServletRequest request) {

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
	 * Also used by R37 as the results required for R37 are a subset of those
	 * returned for Contract Inquiry so it can return the same result. This does not
	 * break overall security as currently all roles with permissions for R37(Get
	 * Group Member's Contract Address) also have permission for R40(Contract
	 * Inquiry).
	 * 
	 * @param contractInquiryRequest
	 * @return The result of the operation.
	 */
	@PostMapping("/inquire-contract")
	public ResponseEntity<ContractInquiryResponse> inquireContract(
			@Valid @RequestBody ContractInquiryRequest contractInquiryRequest, HttpServletRequest request) {

		Transaction transaction = auditContractInquiryStart(contractInquiryRequest, request);
		try {
			RPBSPCI0Converter converter = new RPBSPCI0Converter();
			RPBSPCI0 rpbspci0Request = converter.convertRequest(contractInquiryRequest);
			RPBSPCI0 rpbspci0Response = mspContractsService.inquireContract(rpbspci0Request);
			ContractInquiryResponse contractInquiryResponse = converter.convertResponse(rpbspci0Response);

			ResponseEntity<ContractInquiryResponse> response = ResponseEntity.ok(contractInquiryResponse);

			logger.info("ContractInquiry response: {} ", contractInquiryResponse);

			auditContractInquiryEnd(transaction, contractInquiryResponse);
			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	/**
	 * Update Group Member's Contract Address for a Personal Health Number of a
	 * group. Maps to legacy R38
	 * 
	 * @param updateContractAddressRequest
	 * @param request
	 * @return The result of the operation
	 */
	@PostMapping("/update-contract-address")
	public ResponseEntity<UpdateContractAddressResponse> updateContractAddress(
			@Valid @RequestBody UpdateContractAddressRequest updateContractAddressRequest, HttpServletRequest request) {

		Transaction transaction = auditUpdateContractAddressStart(updateContractAddressRequest, request);
		try {
			RPBSPMA0Converter converter = new RPBSPMA0Converter();
			RPBSPMA0 rpbspma0Request = converter.convertRequest(updateContractAddressRequest);
			RPBSPMA0 rpbspma0Response = mspContractsService.updateContractAddress(rpbspma0Request);
			UpdateContractAddressResponse updateContractAddressResponse = converter.convertResponse(rpbspma0Response);

			ResponseEntity<UpdateContractAddressResponse> response = ResponseEntity.ok(updateContractAddressResponse);

			logger.info("UpdateContractAddressResponse response: {} ", updateContractAddressResponse);

			auditUpdateContractAddressEnd(transaction, updateContractAddressResponse);
			return response;
		} catch (Exception e) {
			handleException(transaction, e);
			return null;
		}
	}

	private Transaction auditGetContractPeriodsStart(GetContractPeriodsRequest getContractPeriodsRequest,
			HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.GET_CONTRACT_PERIODS);
		addAffectedParty(transaction, IdentifierType.PHN, getContractPeriodsRequest.getPhn(),
				AffectedPartyDirection.OUTBOUND);
		return transaction;
	}

	private void auditGetContractPeriodsEnd(Transaction transaction,
			GetContractPeriodsResponse getContractPeriodsResponse) {
		List<String> auditedPhns = new ArrayList<>();
		List<String> auditedGroupNumbers = new ArrayList<>();

		transactionComplete(transaction);
		addAffectedParty(transaction, IdentifierType.PHN, getContractPeriodsResponse.getPhn(),
				AffectedPartyDirection.INBOUND);
		auditedPhns.add(getContractPeriodsResponse.getPhn());

		getContractPeriodsResponse.getBeneficiaryContractPeriods().forEach(bcp -> {
			if (!auditedPhns.contains(bcp.getPhn())) {
				addAffectedParty(transaction, IdentifierType.PHN, bcp.getPhn(), AffectedPartyDirection.INBOUND);
				auditedPhns.add(bcp.getPhn());
			}
			if (!auditedGroupNumbers.contains(bcp.getGroupNumber())) {
				addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, bcp.getGroupNumber(),
						AffectedPartyDirection.INBOUND);
				auditedGroupNumbers.add(bcp.getGroupNumber());
			}
			if (!auditedPhns.contains(bcp.getContractHolder())) {
				addAffectedParty(transaction, IdentifierType.PHN, bcp.getContractHolder(),
						AffectedPartyDirection.INBOUND);
				auditedPhns.add(bcp.getContractHolder());
			}
		});
	}

	private Transaction auditContractInquiryStart(ContractInquiryRequest contractInquiryRequest,
			HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.CONTRACT_INQUIRY);
		addAffectedParty(transaction, IdentifierType.PHN, contractInquiryRequest.getPhn(),
				AffectedPartyDirection.OUTBOUND);
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, contractInquiryRequest.getGroupNumber(),
				AffectedPartyDirection.OUTBOUND);
		return transaction;
	}

	private void auditContractInquiryEnd(Transaction transaction, ContractInquiryResponse contractInquiryResponse) {
		List<String> auditedPhns = new ArrayList<>();

		transactionComplete(transaction);
		addAffectedParty(transaction, IdentifierType.PHN, contractInquiryResponse.getPhn(),
				AffectedPartyDirection.INBOUND);
		auditedPhns.add(contractInquiryResponse.getPhn());

		contractInquiryResponse.getContractInquiryBeneficiaries().forEach(cib -> {
			if (!auditedPhns.contains(cib.getPhn())) {
				addAffectedParty(transaction, IdentifierType.PHN, cib.getPhn(), AffectedPartyDirection.INBOUND);
				auditedPhns.add(cib.getPhn());
			}
		});
	}

	private Transaction auditUpdateContractAddressStart(UpdateContractAddressRequest updateContractAddressRequest,
			HttpServletRequest request) {
		Transaction transaction = transactionStart(request, TransactionType.UPDATE_CONTRACT_ADDRESS);
		addAffectedParty(transaction, IdentifierType.PHN, updateContractAddressRequest.getPhn(),
				AffectedPartyDirection.OUTBOUND);
		addAffectedParty(transaction, IdentifierType.GROUP_NUMBER, updateContractAddressRequest.getGroupNumber(),
				AffectedPartyDirection.OUTBOUND);
		return transaction;
	}

	private void auditUpdateContractAddressEnd(Transaction transaction,
			UpdateContractAddressResponse updateContractAddressResponse) {
		transactionComplete(transaction);
		addAffectedParty(transaction, IdentifierType.PHN, updateContractAddressResponse.getPhn(),
				AffectedPartyDirection.INBOUND);
	}
}