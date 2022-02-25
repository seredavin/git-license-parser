package ru.seredavin.gitlicenseparser.service;

import java.net.URL;
import java.util.concurrent.ExecutionException;

public interface GitParseService {
    String test() throws ExecutionException, InterruptedException;
    void addRepo(URL repoUrl) throws ExecutionException, InterruptedException;
}
