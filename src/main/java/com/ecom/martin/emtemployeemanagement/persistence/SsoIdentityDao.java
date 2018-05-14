package com.ecom.martin.emtemployeemanagement.persistence;

import com.ecom.martin.emtemployeemanagement.model.SsoIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SsoIdentityDao extends JpaRepository<SsoIdentity, String> {
}
