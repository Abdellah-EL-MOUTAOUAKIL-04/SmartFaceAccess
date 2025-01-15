package net.abdellahhafid.smartfaceaccess.services;

import net.abdellahhafid.smartfaceaccess.Models.Statistique;
import net.abdellahhafid.smartfaceaccess.dao.StatistiqueDao;
import net.abdellahhafid.smartfaceaccess.dao.StatistiqueDaoImpl;

import java.util.List;

public class StatistiqueServiceImpl implements StatistiqueService {
    private StatistiqueDao statistiqueDao = new StatistiqueDaoImpl();

    @Override
    public Statistique findById(Integer id) {
        return statistiqueDao.findById(id);
    }

    @Override
    public void save(Statistique statistique) {
        statistiqueDao.save(statistique);
    }

    @Override
    public void update(Statistique statistique) {
        statistiqueDao.update(statistique);
    }

    @Override
    public void delete(Statistique statistique) {
        statistiqueDao.delete(statistique);
    }

    @Override
    public List<Statistique> findAll() {
        return statistiqueDao.findAll();
    }
}
