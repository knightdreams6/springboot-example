package example.elasticsearch.controller;

import example.elasticsearch.bean.Person;
import example.elasticsearch.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jpa")
@RequiredArgsConstructor
public class PersonSearchRepositoryController {

    private final PersonRepository personRepository;

    @PostMapping("/person")
    public String save(@RequestBody Person person) {
        Person savedEntity = personRepository.save(person);
        return String.valueOf(savedEntity.getId());
    }

    @GetMapping("/person/{id}")
    public Person findById(@PathVariable("id") Long id) {
        return personRepository.findById(id).orElse(null);
    }

    @GetMapping("/person/name")
    public List<Person> findByFirstname(@RequestParam String firstName) {
        return personRepository.findByFirstname(firstName);
    }

}
