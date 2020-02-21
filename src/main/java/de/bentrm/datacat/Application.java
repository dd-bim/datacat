package de.bentrm.datacat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages = {"de.bentrm.datacat.repository"})
@SpringBootApplication
public class Application {

//	@Autowired
//	ExternalDocumentRepository externalDocumentRepository;
//
//	@Autowired
//	private SubjectRepository subjectRepository;
//
//	@Autowired
//	private BagRepository bagRepository;
//
//	@Autowired
//	private RelDocumentsRepository relDocumentsRepository;
//
//	@Autowired
//	private RelCollectsRepository relCollectsRepository;
//
//	@Autowired
//	private RelGroupsRepository relGroupsRepository;

//	private Map<String, String> entityIds = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	@Bean
//	@Transactional
//	public CommandLineRunner importInitialData() {
//		return (args) -> {
//			CsvMapper mapper = new CsvMapper();
//			mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
//			mapper.enable(CsvParser.Feature.TRIM_SPACES);
//			CsvSchema columns = mapper.schemaFor(String[].class).withColumnSeparator(';');
//
//			ClassPathResource exportFile = new ClassPathResource("Export.csv");
//			ObjectReader objectReader = mapper.readerFor(String[].class).with(columns);
//
//			subjectRepository.emptyDatabase();
//
//			importCollectionNode(objectReader.readValues(exportFile.getInputStream()));
//			importGroupNode(objectReader.readValues(exportFile.getInputStream()));
//			importSubjectNode(objectReader.readValues(exportFile.getInputStream()));
//			importSubjectDescriptions();
//			importDocuments();
//			importCollects(objectReader.readValues(exportFile.getInputStream()));
//			importGroups(objectReader.readValues(exportFile.getInputStream()));
//
//			PropertyDataFetcher.clearReflectionCache();
//		};
//	}
//
//	@Transactional
//	protected  void importCollectionNode(MappingIterator<String[]> rows) {
//		var lineNumber = 0;
//		while (rows.hasNext()) {
//			String[] properties = rows.next();
//			if (lineNumber > 0) {
//				String names = properties[0];
//
//				// map "Fachmodell" to Collection
//				if (!names.isBlank()) {
//					XtdBag newBag = bagRepository.findByUniqueId(entityIds.get(names));
//
//					if (newBag != null) {
//						continue;
//					}
//
//					newBag = new XtdBag();
//					String[] split = names.split(",");
//					for (int i = 0; i < split.length; i++) {
//						String name = split[i];
//						Name newName = new Name();
////						newName.setLanguage("de");
//						newName.setName(name);
//						newName.setSortOrder(i);
//						newBag.getNames().add(newName);
//					}
//
//					newBag = bagRepository.save(newBag);
//					entityIds.put(names, newBag.getUniqueId());
//				}
//
//			}
//			lineNumber++;
//		}
//	}
//
//	@Transactional
//	protected void importGroupNode(MappingIterator<String[]> rows)  {
//		var lineNumber = 0;
//		while (rows.hasNext()) {
//			String[] properties = rows.next();
//			if (lineNumber > 0) {
//				String names = properties[1];
//
//				if (!names.isBlank()) {
//					XtdSubject newSubject = subjectRepository.findByUniqueId(entityIds.get(names));
//
//					if (newSubject != null) {
//						continue;
//					}
//
//					newSubject = new Subject();
//
//					String[] split = names.split(",");
//					for (int i = 0; i < split.length; i++) {
//						String name = split[i];
//						Name newName = new Name();
////						newName.setLanguage("de");
//						newName.setName(name);
//						newName.setSortOrder(i);
//						newSubject.getNames().add(newName);
//					}
//
//					newSubject = subjectRepository.save(newSubject);
//					entityIds.put(names, newSubject.getUniqueId());
//				}
//
//			}
//			lineNumber++;
//		}
//	}
//
//	@Transactional
//	void importSubjectNode(MappingIterator<String[]> rows) {
//		var lineNumber = 0;
//		while (rows.hasNext()) {
//			String[] properties = rows.next();
//			if (lineNumber > 0) {
//				String names = properties[2];
//
//				if (!names.isBlank()) {
//					XtdSubject newSubject = subjectRepository.findByUniqueId(entityIds.get(names));
//
//					if (newSubject != null) {
//						continue;
//					}
//
//					newSubject = new Subject();
//					String[] split = names.split(",");
//					for (int i = 0; i < split.length; i++) {
//						String name = split[i];
//						Name newName = new Name();
////						newName.setLanguage("de");
//						newName.setName(name);
//						newName.setSortOrder(i);
//						newSubject.getNames().add(newName);
//					}
//
//					newSubject = subjectRepository.save(newSubject);
//					entityIds.put(names, newSubject.getUniqueId());
//				}
//
//			}
//			lineNumber++;
//		}
//	}
//
//
//	@Transactional
//	protected void importSubjectDescriptions() throws IOException {
//		CsvMapper mapper = new CsvMapper();
//		mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
//		mapper.enable(CsvParser.Feature.TRIM_SPACES);
//		CsvSchema columns = mapper.schemaFor(String[].class).withColumnSeparator(';');
//
//		ClassPathResource file = new ClassPathResource("Descriptions.csv");
//		ObjectReader reader = mapper.readerFor(String[].class).with(columns);
//		MappingIterator<String[]> rows = reader.readValues(file.getInputStream());
//
//		int lineNumber = 0;
//		while (rows.hasNext()) {
//			String[] line = rows.next();
//
//			if (lineNumber > 0) {
//				var subjectName = line[0];
//				var desc = line[2];
//
//				if (subjectName.isBlank() || desc.isBlank()) {
//					continue;
//				}
//
//				var subject = subjectRepository.findByUniqueId(entityIds.get(subjectName));
//				if (subject == null) {
//					System.out.println("No subject found with name: " + subjectName);
//					continue;
//				}
//
//				var descriptionEntity = new Description();
////				descriptionEntity.setLanguage("de");
//				descriptionEntity.setDescription(desc);
//				subject.addDescription(descriptionEntity);
//				subjectRepository.save(subject);
//			}
//			lineNumber++;
//		}
//	}
//
//	@Transactional
//	protected void importDocuments() {
//		var buildingSmart = new ExternalDocument();
//		var name = new Name();
//		name.setName("BuildingSMART FG");
//		buildingSmart.getNames().add(name);
//
//		externalDocumentRepository.save(buildingSmart);
//
//		var relationship = new RelDocuments();
//		relationship.setRelatingDocument(buildingSmart);
//		subjectRepository.findAll().forEach(relationship::addRelatedThings);
//		relDocumentsRepository.save(relationship);
//	}
//
//	@Transactional
//	protected void importCollects(MappingIterator<String[]> rows) {
//		var lineNumber = 0;
//		String curRelatingCollectionName = null;
//		String curRelatedThing = null;
//
//		RelCollects relationship = null;
//
//		while (rows.hasNext()) {
//			String[] properties = rows.next();
//			if (lineNumber > 0) {
//				String bagName = properties[0];
//				String subjectName = properties[1];
//
//				if (!bagName.isBlank() && !bagName.equals(curRelatingCollectionName)) {
//					curRelatingCollectionName = bagName;
//
//					if (relationship != null) {
//						relCollectsRepository.save(relationship);
//					}
//
//					relationship = new RelCollects();
//					relationship.setRelatingCollection(bagRepository.findByUniqueId(entityIds.get((curRelatingCollectionName))));
//				}
//
//				if (!subjectName.isBlank() && !subjectName.equals(curRelatedThing)) {
//					curRelatedThing = subjectName;
//
//					var subject = subjectRepository.findByUniqueId(entityIds.get(curRelatedThing));
//					relationship.addRelatedThings(subject);
//				}
//			}
//			lineNumber++;
//		}
//
//		if (relationship != null) {
//			relCollectsRepository.save(relationship);
//		}
//	}
//
//	@Transactional
//	protected void importGroups(MappingIterator<String[]> rows) {
//		var lineNumber = 0;
//		String curGroupName = null;
//		String curGroupMemberName = null;
//
//		RelGroups relationship = null;
//
//		while (rows.hasNext()) {
//			String[] properties = rows.next();
//			if (lineNumber > 0) {
//				String groupName = properties[1];
//				String subjectName = properties[2];
//
//				if (!groupName.isBlank() && !groupName.equals(curGroupName)) {
//					curGroupName = groupName;
//
//					if (relationship != null) {
//						relGroupsRepository.save(relationship);
//					}
//
//					var relatingObject = subjectRepository.findByUniqueId(entityIds.get((curGroupName)));
//					if (!relatingObject.getAssociates().isEmpty()) {
//						relationship = (RelGroups) relatingObject.getAssociates().iterator().next();
//					} else {
//						relationship = new RelGroups();
//						relationship.setRelatingObject(relatingObject);
//					}
//
//				}
//
//				if (!subjectName.isBlank() && !subjectName.equals(curGroupName) && !subjectName.equals(curGroupMemberName)) {
//					curGroupMemberName = subjectName;
//
//					var subject = subjectRepository.findByUniqueId(entityIds.get(curGroupMemberName));
//					relationship.getRelatedObjects().add(subject);
//				}
//			}
//			lineNumber++;
//		}
//
//		if (relationship != null) {
//			relGroupsRepository.save(relationship);
//		}
//	}
}
