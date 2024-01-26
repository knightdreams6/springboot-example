package example.nosql.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

/**
 * people
 *
 * @author knight
 * @since 2024-01-26
 */
@RedisHash("people")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {

	@Id
	String id;

	@Indexed
	String firstname;

	String lastname;

	private Address address;

	public Person(String firstname, String lastname, Address address) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
	}

}