package net.abdellahhafid.smartfaceaccess.services;

import net.abdellahhafid.smartfaceaccess.Models.Statistique;

import java.util.List;

public interface StatistiqueService {
    Statistique findById(Integer id);
    void save(Statistique statistique);
    void update(Statistique statistique);
    void delete(Statistique statistique);
    List<Statistique> findAll();
}
