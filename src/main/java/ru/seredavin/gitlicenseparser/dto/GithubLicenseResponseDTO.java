package ru.seredavin.gitlicenseparser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubLicenseResponseDTO {
    public String name;
    public String path;
    public String sha;
    public int size;
    public String url;
    @JsonProperty("html_url")
    public String htmlUrl;
    @JsonProperty("git_url")
    public String gitUrl;
    @JsonProperty("download_url")
    public String downloadUrl;
    public String type;
    public String content;
    public String encoding;
    @JsonProperty("_links")
    public GithubLinksDTO links;
    public GithubLicenseDTO license;
}
