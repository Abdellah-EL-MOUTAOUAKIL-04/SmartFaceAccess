package net.abdellahhafid.smartfaceaccess.dao;

import net.abdellahhafid.smartfaceaccess.models.Statistique;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatistiqueDaoImpl implements StatistiqueDao {
    @Override
    public Statistique findById(Integer id) {
        Connection connection = SingletonConnectionDB.getConnection();
        Statistique statistique = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM statistiques WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                statistique = new Statistique();
                statistique.setId(resultSet.getInt("id"));
                statistique.setStatDate(resultSet.getDate("stat_date").toString());
                statistique.setTotalAttempts(resultSet.getInt("total_attempts"));
                statistique.setSuccessfulAttempts(resultSet.getInt("successful_attempts"));
                statistique.setFailedAttempts(resultSet.getInt("failed_attempts"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistique;
    }

    @Override
    public void save(Statistique statistique) {
        Connection connection = SingletonConnectionDB.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO statistiques (total_attempts, successful_attempts, failed_attempts, stat_date) VALUES (?, ?, ?, ?)");
            statement.setInt(1, statistique.getTotalAttempts());
            statement.setInt(2, statistique.getSuccessfulAttempts());
            statement.setInt(3, statistique.getFailedAttempts());
            statement.setDate(4, Date.valueOf(statistique.getStatDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Statistique statistique) {
        Connection connection = SingletonConnectionDB.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE statistiques SET total_attempts = ?, successful_attempts = ?, failed_attempts = ?, stat_date = ? WHERE id = ?");
            statement.setInt(1, statistique.getTotalAttempts());
            statement.setInt(2, statistique.getSuccessfulAttempts());
            statement.setInt(3, statistique.getFailedAttempts());
            statement.setInt(4, statistique.getId());
            statement.setDate(5, Date.valueOf(statistique.getStatDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Statistique statistique) {
        Connection connection = SingletonConnectionDB.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM statistiques WHERE id = ?");
            statement.setInt(1, statistique.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Statistique> findAll() {
        Connection connection = SingletonConnectionDB.getConnection();
        List<Statistique> statistiques = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM statistiques");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Statistique statistique = new Statistique();
                statistique.setId(resultSet.getInt("id"));
                statistique.setTotalAttempts(resultSet.getInt("total_attempts"));
                statistique.setSuccessfulAttempts(resultSet.getInt("successful_attempts"));
                statistique.setFailedAttempts(resultSet.getInt("failed_attempts"));
                statistique.setStatDate(resultSet.getDate("stat_date").toString());
                statistiques.add(statistique);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistiques;
    }
}
