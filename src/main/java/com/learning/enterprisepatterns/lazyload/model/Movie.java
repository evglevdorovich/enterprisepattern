package com.learning.enterprisepatterns.lazyload.model;

import lombok.Data;

@Data
public class Movie {
    private long id;
    private String name;
    private Producer producer;
}
