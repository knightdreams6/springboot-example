package example.nosql.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * Student
 *
 * @author knight
 * @since 2024-01-26
 */
@RedisHash("student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Serializable {

	public enum Gender {

		MALE, FEMALE

	}

	private String id;

	private String name;

	private Gender gender;

	private int grade;

}