package com.ecom.martin.emtemployeemanagement.service.impl;

import com.ecom.martin.emtemployeemanagement.model.Employee;
import com.ecom.martin.emtemployeemanagement.model.SsoIdentity;
import com.ecom.martin.emtemployeemanagement.persistence.EmployeeDao;
import com.ecom.martin.emtemployeemanagement.persistence.SsoIdentityDao;
import com.ecom.martin.emtemployeemanagement.service.SsoIdentityService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SsoIdentityServiceImpl implements SsoIdentityService {

    private final SsoIdentityDao dao;

    public SsoIdentityServiceImpl(SsoIdentityDao dao) {
        this.dao = dao;
    }

    @Override
    public Optional<Employee> findEmployee(String ssoId) {
        return dao.findById(ssoId)
                .map(it -> Optional.of(it.getEmployee()))
                .orElse(Optional.empty());
    }

    @Override
    public SsoIdentity createSsoIdentity(String ssoId, Employee e) {

        return this.dao.save(new SsoIdentity(ssoId, e));
    }
}
