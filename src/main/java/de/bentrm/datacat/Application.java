package de.bentrm.datacat;

import de.bentrm.datacat.repository.impl.GraphEntityRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableNeo4jRepositories(
		basePackages = {"de.bentrm.datacat.repository"},
		repositoryFactoryBeanClass = GraphEntityRepositoryFactoryBean.class
)
@SpringBootApplication
public class Application {

	// CALL db.index.fulltext.createNodeIndex("namesAndDescriptions",["XtdName", "XtdDescription"],["name", "description"]);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
