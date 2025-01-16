package net.abdellahhafid.smartfaceaccess.services;

import net.abdellahhafid.smartfaceaccess.models.Log;
import net.abdellahhafid.smartfaceaccess.dao.LogDao;
import net.abdellahhafid.smartfaceaccess.dao.LogDaoImpl;

import java.util.List;

public class LogServiceImpl implements LogService {
    private LogDao logDao = new LogDaoImpl();

    @Override
    public Log findById(Integer id) {
        return logDao.findById(id);
    }

    @Override
    public void save(Log log) {
        logDao.save(log);
    }

    @Override
    public void update(Log log) {
        logDao.update(log);
    }

    @Override
    public void delete(Log log) {
        logDao.delete(log);
    }

    @Override
    public List<Log> findAll() {
        return logDao.findAll();
    }
}
