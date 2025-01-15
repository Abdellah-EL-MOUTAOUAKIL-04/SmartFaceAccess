package net.abdellahhafid.smartfaceaccess.dao;

import java.util.List;

public interface Dao <T,U>{
    //findAll, findById, save, update, delete.
    public T findById(U id);
    public void save(T o);
    public void update(T o);
    public void delete(T o);
    public List<T> findAll();
}
