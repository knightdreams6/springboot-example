package example.nosql.redis.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.index.GeoIndexed;

import java.security.SecureRandom;
import java.time.Instant;

/**
 * 地址
 *
 * @author knight
 * @since 2024-01-26
 */
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	@GeoIndexed
	Point location;

}
