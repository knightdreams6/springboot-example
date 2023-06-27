package example.nosql.neo4j;

import example.nosql.neo4j.entity.Dept;
import example.nosql.neo4j.repository.DeptRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import java.util.Arrays;
import java.util.List;

/**
 * neo4j示例应用程序
 *
 * @author knight
 * @since 2023/06/27
 */
@Slf4j
@EnableNeo4jRepositories
@SpringBootApplication
public class ExampleNeo4jApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleNeo4jApplication.class, args);
	}

	@Bean
	CommandLineRunner demo(DeptRepository deptRepository) {
		return args -> {

			deptRepository.deleteAll();

			Dept dept = new Dept("CEO");
			Dept dept1 = new Dept("设计部");
			Dept dept11 = new Dept("设计1组");
			Dept dept12 = new Dept("设计2组");
			Dept dept2 = new Dept("技术部");
			Dept dept21 = new Dept("前端技术部");
			Dept dept22 = new Dept("后端技术部");
			Dept dept23 = new Dept("测试技术部");

			List<Dept> deptList = Arrays.asList(dept, dept1, dept11, dept12, dept2, dept21, dept22, dept23);

			deptRepository.saveAll(deptList);

			dept = deptRepository.findByName("CEO");
			dept.addChildList(dept1, dept2);
			deptRepository.save(dept);

			dept1.addChildList(dept11, dept12);
			deptRepository.save(dept1);

			dept2.addChildList(dept21, dept22, dept23);
			deptRepository.save(dept2);

			dept = deptRepository.findByName("CEO");

			log.info("dept: {}", dept);
		};
	}

}
