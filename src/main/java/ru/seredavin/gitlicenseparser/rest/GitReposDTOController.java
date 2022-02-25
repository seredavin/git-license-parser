package ru.seredavin.gitlicenseparser.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.seredavin.gitlicenseparser.service.GitParseService;

import java.net.URL;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@RequestMapping("/add-repo")
@CrossOrigin("*")
public class GitReposDTOController {
    private final GitParseService gitParseService;

    @GetMapping
    ResponseEntity<String> get(@RequestParam URL repoName) throws ExecutionException, InterruptedException {
        gitParseService.addRepo(repoName);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
