package example.nosql.redis.repository;

import example.nosql.redis.entity.Person;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * personRepository
 *
 * @author knight
 * @since 2024-01-26
 */
public interface PersonRepository extends CrudRepository<Person, String> {

	List<Person> findByAddressLocationNear(Point point, Distance distance);

	List<Person> findByAddressLocationWithin(Circle circle);

}
