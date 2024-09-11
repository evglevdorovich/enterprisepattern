package com.learning.enterprisepatterns.lazyload.virtualProxy;

public interface Finder<T> {
    T findById(int id);
}
