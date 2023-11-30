package example.webflux.r2dbc;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TutorialController {

    private final TutorialService tutorialService;

    @GetMapping("/tutorials")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Tutorial> getAllTutorials(@RequestParam(required = false) String title) {
        if (title == null)
            return tutorialService.findAll();
        else
            return tutorialService.findByTitleContaining(title);
    }

    @GetMapping("/tutorials/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Tutorial> getTutorialById(@PathVariable("id") int id) {
        return tutorialService.findById(id);
    }

    @PostMapping("/tutorials")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
        return tutorialService.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
    }

    @PutMapping("/tutorials/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Tutorial> updateTutorial(@PathVariable("id") int id, @RequestBody Tutorial tutorial) {
        return tutorialService.update(id, tutorial);
    }

    @DeleteMapping("/tutorials/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTutorial(@PathVariable("id") int id) {
        return tutorialService.deleteById(id);
    }

    @DeleteMapping("/tutorials")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAllTutorials() {
        return tutorialService.deleteAll();
    }

    @GetMapping("/tutorials/published")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Tutorial> findByPublished() {
        return tutorialService.findByPublished(true);
    }

}