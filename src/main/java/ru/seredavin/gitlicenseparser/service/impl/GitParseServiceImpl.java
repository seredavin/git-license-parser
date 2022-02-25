package ru.seredavin.gitlicenseparser.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.reactive.function.client.WebClient;
import ru.seredavin.gitlicenseparser.dto.GithubLicenseDTO;
import ru.seredavin.gitlicenseparser.dto.GithubLicenseResponseDTO;
import ru.seredavin.gitlicenseparser.service.GitParseService;
import ru.seredavin.gitlicenseparser.transport.Producer;
import ru.seredavin.gitlicenseparser.utils.Licenses;

import java.net.URL;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@Service
@Slf4j
@AllArgsConstructor
public class GitParseServiceImpl implements GitParseService {
    private final Producer producer;
    @Override
    public String test() throws ExecutionException, InterruptedException {
        WebClient client = WebClient.create("https://api.github.com");
        GithubLicenseResponseDTO license = client.get()
                .uri("repos/octopus-network/octopus-pallets/license")
                .header("Accept", "application/vnd.github.v3+json")
                .retrieve()
                .toEntity(GithubLicenseResponseDTO.class)
                .block()
                .getBody();
        ListenableFuture<SendResult<String, String>> listenableFuture = producer.sendMessage("INPUT_DATA", license.name, license.license.key);
        SendResult<String, String> result = listenableFuture.get();
        log.info(String.format("Produced:\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d", result.getRecordMetadata().topic(),
                result.getRecordMetadata().offset(),
                result.getRecordMetadata().partition(), result.getRecordMetadata().serializedValueSize()));
        return "";
    }

    @Override
    public void addRepo(URL repoUrl) throws ExecutionException, InterruptedException {
        WebClient client = WebClient.create("https://api.github.com");
        GithubLicenseResponseDTO license;
        try {
            license = client.get()
                    .uri("repos/" + repoUrl.getPath() + "/license")
                    .header("Accept", "application/vnd.github.v3+json")
                    .retrieve()
                    .toEntity(GithubLicenseResponseDTO.class)
                    .block()
                    .getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            license = manualParse(repoUrl);
        }
        ListenableFuture<SendResult<String, String>> listenableFuture = producer.sendMessage("INPUT_DATA", repoUrl.toString(), license.license.key);
        SendResult<String, String> result = listenableFuture.get();
        log.info(String.format("Produced:\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d", result.getRecordMetadata().topic(),
                result.getRecordMetadata().offset(),
                result.getRecordMetadata().partition(), result.getRecordMetadata().serializedValueSize()));
    }

    private GithubLicenseResponseDTO manualParse(URL url) {
        String body;
        try {
            body = getFile(url, "README.md");
        } catch (Exception e) {
            body = getFile(url, "README.rst");
        }
        if (body == null) {
            return getSimpleLicense("Unknown");
        }
        for (Licenses license : Licenses.values()) {
            for (String regexp : license.getPatterns()) {
                if (Pattern.matches(regexp, body)) {
                    return getSimpleLicense(license.getName());
                }

            }
        }
        return getSimpleLicense("Unknown");
    }

    private String getFile(URL url, String filename) {
        WebClient client = WebClient.create("https://raw.githubusercontent.com");
        return client.get()
                .uri(url.getPath() + "/master/" + filename)
                .retrieve()
                .toEntity(String.class)
                .block()
                .getBody();
    }

    private GithubLicenseResponseDTO getSimpleLicense(String license) {
        GithubLicenseDTO githubLicenseDTO = new GithubLicenseDTO();
        githubLicenseDTO.key = license;
        GithubLicenseResponseDTO githubLicenseResponseDTO = new GithubLicenseResponseDTO();
        githubLicenseResponseDTO.license = githubLicenseDTO;
        return githubLicenseResponseDTO;
    }
}
