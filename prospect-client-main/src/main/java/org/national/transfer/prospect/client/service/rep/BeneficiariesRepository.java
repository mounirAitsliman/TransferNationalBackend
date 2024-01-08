package org.national.transfer.prospect.client.service.rep;

import org.national.transfer.prospect.client.service.model.Beneficiaries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiariesRepository extends JpaRepository<Beneficiaries, String> {

}
