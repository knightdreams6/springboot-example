package example.nosql.neo4j.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 机构实体类
 *
 * @author knight
 * @since 2023/06/27
 */
@Data
@NoArgsConstructor
public class Dept {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@Relationship(type = "child")
	public Set<Dept> subLevels;

	public Dept(String name) {
		this.name = name;
	}

	public void addChildList(Dept... childList) {
		if (subLevels == null) {
			subLevels = new HashSet<>();
		}
		subLevels.addAll(Arrays.stream(childList).toList());
	}

}
