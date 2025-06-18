package com.sks.tariff_01.mapper;

public interface Mapper<T, U> {

    U map(T source);

    T mapBack(U source);

    default U mapIfNotNull(T source) {
        return source == null ? null : map(source);
    }

    default T mapBackIfNotNull(U source) {
        return source == null ? null : mapBack(source);
    }
}
