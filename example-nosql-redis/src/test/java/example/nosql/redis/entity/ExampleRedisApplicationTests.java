package example.nosql.redis.entity;

import example.nosql.redis.repository.PersonRepository;
import example.nosql.redis.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ExampleRedisApplicationTests
 *
 * @author knight
 * @since 2024-01-26
 */
@SpringBootTest
public class ExampleRedisApplicationTests {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	void testCRUDOperations() {
		// Create
		Student student = new Student("Eng2015001", "John Doe", Student.Gender.MALE, 1);
		studentRepository.save(student);

		// Retrieve
		Optional<Student> retrievedStudent = studentRepository.findById("Eng2015001");
		assertTrue(retrievedStudent.isPresent());

		// Update
		Student updatedStudent = retrievedStudent.get();
		updatedStudent.setName("Richard Watson");
		studentRepository.save(updatedStudent);

		// Verify Update
		Optional<Student> retrievedStudentAfterUpdate = studentRepository.findById("Eng2015001");
		assertTrue(retrievedStudentAfterUpdate.isPresent());
		assertEquals("Richard Watson", retrievedStudentAfterUpdate.get().getName());

		// Delete
		studentRepository.deleteById(student.getId());
		// Verify Delete
		Optional<Student> retrievedStudentAfterDelete = studentRepository.findById("Eng2015001");
		assertTrue(retrievedStudentAfterDelete.isEmpty());
	}

	@Test
	void testGeoOperations() {
		Person rand = new Person("rand", "al'thor", new Address(new Point(13.361389D, 38.115556D)));
		personRepository.save(rand);

		List<Person> byAddressLocationNear = personRepository.findByAddressLocationNear(new Point(15D, 37D),
				new Distance(200, Metrics.KILOMETERS));
		assertFalse(byAddressLocationNear.isEmpty());
	}

	@Test
	void testGeoOperationsByTemplate() {
		String key = "bikes:rentable";

		// 坐标点
		Point stationOnePoint = new Point(-122.27652D, 37.805186D);
		Point stationTwoPoint = new Point(-122.2774626D, 37.8062344D);
		Point stationThreePoint = new Point(-122.2769854D, 37.8104049D);

		stringRedisTemplate.opsForGeo().add(key, stationOnePoint, "station:1");
		stringRedisTemplate.opsForGeo().add(key, new RedisGeoCommands.GeoLocation<>("station:2", stationTwoPoint));
		stringRedisTemplate.opsForGeo().add(key, new RedisGeoCommands.GeoLocation<>("station:3", stationThreePoint));

		RedisGeoCommands.GeoSearchCommandArgs geoSearchCommandArgs = RedisGeoCommands.GeoSearchCommandArgs
				.newGeoSearchArgs().includeDistance() // 包括距离信息
				.includeCoordinates() // 包括坐标信息
				.sortAscending();// 升序排序

		Distance distance = new Distance(5, Metrics.KILOMETERS);

		GeoReference<String> geoReference = GeoReference.fromCoordinate(stationOnePoint);

		GeoResults<RedisGeoCommands.GeoLocation<String>> search = stringRedisTemplate.opsForGeo().search(key,
				geoReference, distance, geoSearchCommandArgs);
		assertNotNull(search);
		assertEquals(3, search.getContent().size());

		List<Point> position = stringRedisTemplate.opsForGeo().position(key, "station:1", "station:2", "station:3");

		assertNotNull(position);
		assertEquals(3, position.size());

		Distance distanced = stringRedisTemplate.opsForGeo().distance(key, "station:1", "station:2",
				Metrics.KILOMETERS);
		assertNotNull(distanced);
	}

	@Test
	void testZSetOperations() {

	}

}
