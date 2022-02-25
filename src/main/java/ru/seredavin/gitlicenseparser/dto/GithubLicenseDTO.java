package ru.seredavin.gitlicenseparser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubLicenseDTO {
    public String key;
    public String name;
    @JsonProperty("spdx_id")
    public String spdxId;
    public String url;
    @JsonProperty("node_id")
    public String nodeId;
}
