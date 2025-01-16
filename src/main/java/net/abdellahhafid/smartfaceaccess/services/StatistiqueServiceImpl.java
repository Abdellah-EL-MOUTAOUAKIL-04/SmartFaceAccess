package net.abdellahhafid.smartfaceaccess.services;

import net.abdellahhafid.smartfaceaccess.models.Statistique;
import net.abdellahhafid.smartfaceaccess.dao.StatistiqueDao;
import net.abdellahhafid.smartfaceaccess.dao.StatistiqueDaoImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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


    @Override
    public Statistique getTodayStatistique() {
        // Récupère toutes les statistiques depuis la base de données
        List<Statistique> statistiques = statistiqueDao.findAll();

        // Obtenir la date d'aujourd'hui au format utilisé dans les statistiques
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Initialiser les variables pour les statistiques d'aujourd'hui
        int totalAttempts = 0;
        int successfulAttempts = 0;
        int failedAttempts = 0;

        // Filtrer les statistiques par la date d'aujourd'hui et calculer les totaux
        for (Statistique stat : statistiques) {
            if (today.equals(stat.getStatDate())) {
                totalAttempts += stat.getTotalAttempts();
                successfulAttempts += stat.getSuccessfulAttempts();
                failedAttempts += stat.getFailedAttempts();
            }
        }

        // Créer et retourner un objet Statistique pour les données d'aujourd'hui
        Statistique todayStatistique = new Statistique();
        todayStatistique.setTotalAttempts(totalAttempts);
        todayStatistique.setSuccessfulAttempts(successfulAttempts);
        todayStatistique.setFailedAttempts(failedAttempts);
        todayStatistique.setStatDate(today);

        return todayStatistique;
    }

    @Override
    public Statistique getAllTimeStatistique() {
        // Récupère toutes les statistiques depuis la base de données
        List<Statistique> statistiques = statistiqueDao.findAll();

        // Initialiser les variables pour les statistiques globales
        int totalAttempts = 0;
        int successfulAttempts = 0;
        int failedAttempts = 0;

        // Parcourir toutes les statistiques et additionner les valeurs
        for (Statistique stat : statistiques) {
            totalAttempts += stat.getTotalAttempts();
            successfulAttempts += stat.getSuccessfulAttempts();
            failedAttempts += stat.getFailedAttempts();
        }

        // Créer un objet Statistique pour les données globales
        Statistique allTimeStatistique = new Statistique();
        allTimeStatistique.setTotalAttempts(totalAttempts);
        allTimeStatistique.setSuccessfulAttempts(successfulAttempts);
        allTimeStatistique.setFailedAttempts(failedAttempts);
        allTimeStatistique.setStatDate("ALL_TIME"); // Indique que ces statistiques sont globales

        return allTimeStatistique;
    }

}
