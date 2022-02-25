package ru.seredavin.gitlicenseparser.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GitReposDTO {
    public String id;
    public String name;
    public String url;
    public String license;
}
