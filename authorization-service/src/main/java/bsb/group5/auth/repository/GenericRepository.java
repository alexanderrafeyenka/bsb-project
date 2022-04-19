package bsb.group5.auth.repository;

import java.util.List;

public interface GenericRepository<I, T> {

    T persist(T instance);

    T merge(T instance);

    T remove(T instance);

    T detach(T entity);

    T findById(I id);

    List<T> findAll();
}
