package com.mamalimomen.base.repositories;

import com.mamalimomen.base.domains.BaseEntity;
import com.mamalimomen.base.dtos.BaseDTO;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<PK extends Long, E extends BaseEntity, D extends BaseDTO> {
    void closeEntityManger();

    boolean saveOne(E e);

    boolean saveMany(List<E> l);

    boolean updateOne(E e);

    boolean updateMany(List<E> l);

    boolean deleteOne(E e);

    boolean deleteMany(List<E> l);

    Optional<E> findOneById(Class<E> c, PK id);

    Optional<E> findOneByNamedQuery(String namedQuery, Class<E> c, Object... parameters);

    List<E> findManyByNamedQuery(String namedQuery, Class<E> c, Object... parameters);

    List<E> findAllByNamedQuery(String namedQuery, Class<E> c);

    <T> List<T> advancedSearch(D dto, Class<T> resultClass, Class<E> entityClass);
}