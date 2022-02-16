package ca.bc.gov.hlth.hnweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEvent;

public interface TransactionEventRepository extends JpaRepository<TransactionEvent, Long> {

}
