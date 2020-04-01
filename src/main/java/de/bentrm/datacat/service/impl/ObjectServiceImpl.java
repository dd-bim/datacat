package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.repository.RelGroupsRepository;
import de.bentrm.datacat.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ObjectServiceImpl implements ObjectService {

    @Autowired
    private RelGroupsRepository relGroupsRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<XtdRoot> findByRelGroupsId(String id, Pageable pageable) {
        Optional<XtdRelGroups> result = relGroupsRepository.findById(id);
        if (result.isEmpty()) {
            throw new IllegalArgumentException("No relationship with id " + id + " found.");
        }
        XtdRelGroups relation = result.get();
        List<XtdRoot> content = new ArrayList<>();
        int i = 0;
        for (XtdRoot relatedObject : relation.getRelatedThings()) {
            if (i >= (pageable.getOffset() + pageable.getPageSize())) {
                break;
            }
            if (i >= pageable.getOffset()) {
                content.add(relatedObject);
            }
            i++;
        }
        return PageableExecutionUtils.getPage(content, pageable, () -> relation.getRelatedThings().size());
    }
}
