package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.service.AdminService;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void purgeDatabase() {
        Session session = sessionFactory.openSession();
        session.purgeDatabase();
    }
}
