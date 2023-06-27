package example.nosql.neo4j.repository;

import example.nosql.neo4j.entity.Dept;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * 机构存储库
 *
 * @author knight
 * @since 2023/06/27
 */
public interface DeptRepository extends Neo4jRepository<Dept, Long> {

	Dept findByName(String name);

}
