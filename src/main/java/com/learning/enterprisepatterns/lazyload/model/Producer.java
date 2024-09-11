package com.learning.enterprisepatterns.lazyload.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producer {
    private long id;

    private String name;
}
