package com.group.s3example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadRes {
    @JsonProperty("document_group")
    private String documentGroup;
    @JsonProperty("document_id")
    private String documentId;
    private String url;
}
