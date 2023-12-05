package example.elasticsearch.controller;

import example.elasticsearch.bean.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonSearchOperationsController {

    private final ElasticsearchOperations elasticsearchOperations;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody Person person) {
        Person savedEntity = elasticsearchOperations.save(person);
        return ResponseEntity.ok(String.valueOf(savedEntity.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(elasticsearchOperations.get(id.toString(), Person.class));
    }

    @GetMapping("/name")
    public ResponseEntity<List<SearchHit<Person>>> findByFirstname(@RequestParam String firstName) {
        Criteria criteria = new Criteria("firstName").is(firstName);
        CriteriaQuery query = new CriteriaQuery(criteria);
        SearchHits<Person> search = elasticsearchOperations.search(query, Person.class);
        return ResponseEntity.ok(search.getSearchHits());
    }

    @DeleteMapping("/index")
    public ResponseEntity<Boolean> deleteIndex() {
        return ResponseEntity.ok(elasticsearchOperations.indexOps(Person.class).delete());
    }

}
