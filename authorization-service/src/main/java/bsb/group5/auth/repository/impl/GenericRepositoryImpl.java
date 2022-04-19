package bsb.group5.auth.repository.impl;

import bsb.group5.auth.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Repository
public abstract class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {

    protected Class<T> entityClass;
    @PersistenceContext
    protected EntityManager em;

    public GenericRepositoryImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[1];
    }

    @Override
    public T persist(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public T merge(T entity) {
        em.merge(entity);
        return entity;
    }

    @Override
    public T remove(T entity) {
        em.remove(entity);
        return entity;
    }

    @Override
    public T detach(T entity) {
        em.detach(entity);
        return entity;
    }

    @Override
    public T findById(I id) {
        return em.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        String query = "FROM " + entityClass.getSimpleName();
        Query result = em.createQuery(query);
        return result.getResultList();
    }
}
