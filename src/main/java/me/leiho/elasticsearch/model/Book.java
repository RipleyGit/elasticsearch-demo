package me.leiho.elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Book {
    private String id;
    private String title;
    private String author;
    private float price;
}