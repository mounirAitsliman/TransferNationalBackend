package org.national.transfer.prospect.client.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "beneficiaries")
public class Beneficiaries extends AuditModel {

    @Id
    private String beneficiaryEmail;
    @Column(nullable = false, length = 50)
    private String beneficiaryLastName;
    @Column(nullable = false, length = 50)
    private String beneficiaryFirstName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "identity_number", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    private ProspectClient prospectClient;
}
