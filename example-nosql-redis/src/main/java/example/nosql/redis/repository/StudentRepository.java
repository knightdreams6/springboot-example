package example.nosql.redis.repository;

import example.nosql.redis.entity.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * StudentRepository
 *
 * @author knight
 * @since 2024-01-26
 */
@Repository
public interface StudentRepository extends CrudRepository<Student, String> {

}