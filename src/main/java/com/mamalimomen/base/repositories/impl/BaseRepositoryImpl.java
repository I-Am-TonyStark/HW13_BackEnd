package com.mamalimomen.base.repositories.impl;

import com.mamalimomen.base.domains.BaseEntity;
import com.mamalimomen.base.repositories.BaseRepository;
import com.mamalimomen.base.dtos.BaseDTO;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepositoryImpl<PK extends Long, E extends BaseEntity, D extends BaseDTO> implements BaseRepository<PK, E, D> {
    private final EntityManager em;

    public BaseRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void closeEntityManger() {
        if (em.isOpen()) {
            em.close();
        }
    }

    @Override
    public boolean saveOne(E e) {
        try {
            em.getTransaction().begin();

            em.persist(e);

            em.getTransaction().commit();

            return true;
        } catch (EntityExistsException | RollbackException ex) {
            em.getTransaction().rollback();
        }
        return false;
    }

    @Override
    public boolean saveMany(List<E> l) {
        try {
            em.getTransaction().begin();

            for (E e : l)
                em.persist(e);

            em.getTransaction().commit();

            return true;
        } catch (EntityExistsException | RollbackException ex) {
            em.getTransaction().rollback();
        }
        return false;
    }

    @Override
    public boolean updateOne(E e) {
        try {
            em.getTransaction().begin();

            if (!em.contains(e))
                em.merge(e);

            em.getTransaction().commit();

            return true;
        } catch (IllegalArgumentException | RollbackException ex) {
            em.getTransaction().rollback();
        }
        return false;
    }

    @Override
    public boolean updateMany(List<E> l) {
        try {
            em.getTransaction().begin();

            for (E e : l) {
                if (!em.contains(e))
                    em.merge(e);
            }

            em.getTransaction().commit();

            return true;
        } catch (IllegalArgumentException | RollbackException ex) {
            em.getTransaction().rollback();
        }
        return false;
    }

    @Override
    public boolean deleteOne(E e) {
        try {
            em.getTransaction().begin();

            em.remove(e);

            em.getTransaction().commit();

            return true;
        } catch (IllegalArgumentException | RollbackException ex) {
            em.getTransaction().rollback();
        }
        return false;
    }

    /*@Override
    public boolean deleteOne(E e) {
        try {
            em.getTransaction().begin();

            Query query = em.createQuery("DELETE FROM " + e.getClass().getSimpleName() + "e WHERE e.id = ?1");
            query.setParameter(1, e.getId());
            int result = query.executeUpdate();

            em.getTransaction().commit();

            if (result > 0) {
                return true;
            }
        } catch (IllegalArgumentException | RollbackException ex) {
            em.getTransaction().rollback();
        }
        return false;
    }*/

    @Override
    public boolean deleteMany(List<E> l) {
        try {
            em.getTransaction().begin();

            for (E e : l) {
                em.remove(e);
            }

            em.getTransaction().commit();
            return true;
        } catch (IllegalArgumentException | RollbackException ex) {
            em.getTransaction().rollback();
        }
        return false;
    }

    @Override
    public Optional<E> findOneById(Class<E> c, PK id) {
        E e = em.find(c, id);
        return e != null ? Optional.of(e) : Optional.empty();
    }

    @Override
    public Optional<E> findOneByNamedQuery(String namedQuery, Class<E> c, Object... parameters) {
        E e;
        try {
            TypedQuery<E> nq = em.createNamedQuery(namedQuery, c);

            for (int i = 1; i <= parameters.length; i++) {
                nq = nq.setParameter(i, parameters[i - 1]);
            }

            e = nq.getSingleResult();
        } catch (NoResultException ex) {
            e = null;
        }
        return e != null ? Optional.of(e) : Optional.empty();
    }

    @Override
    public List<E> findManyByNamedQuery(String namedQuery, Class<E> c, Object... parameters) {
        TypedQuery<E> nq = em.createNamedQuery(namedQuery, c);

        for (int i = 1; i <= parameters.length; i++) {
            nq = nq.setParameter(i, parameters[i - 1]);
        }

        return nq.getResultList();
    }

    @Override
    public List<E> findAllByNamedQuery(String namedQuery, Class<E> c) {
        return em.createNamedQuery(namedQuery, c)
                .getResultList();
    }

    @Override
    public <T> List<T> advancedSearch(D dto, Class<T> resultClass, Class<E> entityClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(resultClass);
        Root<E> root = query.from(entityClass);

        List<Predicate> predicates = new ArrayList<>();
        setClausesForAdvancedSearch(dto, predicates, cb, root);

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return em.createQuery(query).getResultList();
    }

    protected abstract void setClausesForAdvancedSearch(D dto, List<Predicate> predicates, CriteriaBuilder cb, Root<E> root);
}
