package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.*;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.dto.*;
import de.bentrm.datacat.repository.*;
import de.bentrm.datacat.repository.relationship.RelGroupsRepository;
import de.bentrm.datacat.service.XtdObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class XtdObjectServiceImpl extends NamedEntityServiceImpl<XtdObject, ObjectRepository> implements XtdObjectService {

    private final ActorRepository actorRepository;
    private final ActivityRepository activityRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public XtdObjectServiceImpl(LanguageRepository languageRepository,
                                ActorRepository actorRepository,
                                ActivityRepository activityRepository,
                                SubjectRepository subjectRepository,
                                ObjectRepository repository) {
        super(languageRepository, repository);
        this.languageRepository = languageRepository;
        this.actorRepository = actorRepository;
        this.activityRepository = activityRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Page<XtdObject> findByRelDocumentsId(String id, int pageNumber, int pageSize) {
        int skip = pageNumber * pageSize;
        Iterable<XtdObject> objects = repository.findByRelDocumentsId(id, skip, pageSize);

        List<XtdObject> content = new ArrayList<>();
        objects.forEach(content::add);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return PageableExecutionUtils.getPage(content, pageRequest, () -> repository.countByRelDocumentsId(id));
    }

    @Override
    public Page<XtdObject> findByRelGroupsId(String id, int pageNumber, int pageSize) {
        int skip = pageNumber * pageSize;
        Iterable<XtdObject> objects = repository.findByRelGroupsId(id, skip, pageSize);

        List<XtdObject> content = new ArrayList<>();
        objects.forEach(content::add);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return PageableExecutionUtils.getPage(content, pageRequest, () -> repository.countByRelGroupsId(id));
    }

    @Override
    public XtdActor createActor(XtdActorInputDto dto) {
        XtdActor newActor = new XtdActor();
        mapInputToObject(newActor, dto);
        return repository.save(newActor);
    }

    @Override
    public XtdActor deleteActor(String id) {
        XtdActor actor = actorRepository.findById(id);
        repository.delete(actor);
        return actor;
    }

    @Override
    public XtdActivity createActivity(XtdActivityInputDto dto) {
        XtdActivity newActivity = new XtdActivity();
        mapInputToObject(newActivity, dto);
        return repository.save(newActivity);
    }

    @Override
    public XtdActivity deleteActivity(String id) {
        XtdActivity activity = activityRepository.findById(id);
        repository.delete(activity);
        return activity;
    }

    @Override
    public XtdSubject createSubject(XtdSubjectInputDto dto) {
        XtdSubject newSubject = new XtdSubject();
        mapInputToObject(newSubject, dto);
        return repository.save(newSubject);
    }

    @Override
    public XtdSubject deleteSubject(String id) {
        XtdSubject subject = subjectRepository.findById(id);
        repository.delete(subject);
        return subject;
    }

    @Override
    public Page<XtdSubject> findAllSubjects(int pageNumber, int pageSize) {
        int skip = pageNumber * pageSize;
        Iterable<XtdSubject> nodes = subjectRepository.findAll(skip, pageSize);

        List<XtdSubject> resultList = new ArrayList<>();
        nodes.forEach(resultList::add);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return PageableExecutionUtils.getPage(resultList, pageRequest, subjectRepository::count);


    }

    @Override
    public Page<XtdSubject> findSubjectsByTerm(String term, int pageNumber, int pageSize) {
        int skip = pageNumber * pageSize;
        Iterable<XtdSubject> nodes = subjectRepository.findByTerm(term, skip, pageSize);

        List<XtdSubject> resultList = new ArrayList<>();
        nodes.forEach(resultList::add);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return PageableExecutionUtils.getPage(resultList, pageRequest, () -> subjectRepository.countByTerm(term));
    }
}
