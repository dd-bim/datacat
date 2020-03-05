package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.repository.ObjectRepository;
import de.bentrm.datacat.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ObjectServiceImpl implements ObjectService {

    @Autowired
    private ObjectRepository objectRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<XtdObject> findByRelGroupsId(String id, Pageable pageable) {
        Iterable<XtdObject> objects = objectRepository.findByRelGroupsId(id, pageable.getOffset(), pageable.getPageSize());
        List<XtdObject> content = new ArrayList<>();
        objects.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> objectRepository.countByRelGroupsId(id));
    }
}
