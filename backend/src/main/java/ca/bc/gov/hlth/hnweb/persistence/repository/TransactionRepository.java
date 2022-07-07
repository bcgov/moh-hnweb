package ca.bc.gov.hlth.hnweb.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}
