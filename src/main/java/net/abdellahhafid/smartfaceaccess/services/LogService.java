package net.abdellahhafid.smartfaceaccess.services;

import net.abdellahhafid.smartfaceaccess.Models.Log;

import java.util.List;

public interface LogService {
    Log findById(Integer id);
    void save(Log log);
    void update(Log log);
    void delete(Log log);
    List<Log> findAll();
}