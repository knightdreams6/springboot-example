package example.elasticsearch.repository;

import example.elasticsearch.bean.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends ElasticsearchRepository<Person, Long> {

    @NonNull
    Person save(@NonNull Person person);

    @NonNull
    Optional<Person> findById(@NonNull Long id);

    @NonNull
    List<Person> findByFirstname(@NonNull String firstname);

}