package net.abdellahhafid.smartfaceaccess.dao;

import net.abdellahhafid.smartfaceaccess.models.Log;
import net.abdellahhafid.smartfaceaccess.models.Utilisateur;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LogDaoImpl implements LogDao {

    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Log findById(Integer id) {
        Connection connection = SingletonConnectionDB.getConnection();
        Log log = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM logs WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                log = new Log();
                log.setId(resultSet.getInt("id"));
                log.setUtilisateur(new UtilisateurDaoImpl().findById(resultSet.getInt("user_id")));
                log.setAccessTime(parseTimestamp(resultSet.getString("access_time"))); // Parse timestamp manually
                log.setStatus(resultSet.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return log;
    }

    @Override
    public void save(Log log) {
        Connection connection = SingletonConnectionDB.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO logs (user_id, access_time, status) VALUES (?, ?, ?)");
            statement.setInt(1, log.getUtilisateur().getId());
            statement.setString(2, TIMESTAMP_FORMAT.format(log.getAccessTime())); // Format timestamp as string
            statement.setString(3, log.getStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Log log) {
        Connection connection = SingletonConnectionDB.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE logs SET user_id = ?, access_time = ?, status = ? WHERE id = ?");
            statement.setInt(1, log.getUtilisateur().getId());
            statement.setString(2, TIMESTAMP_FORMAT.format(log.getAccessTime())); // Format timestamp as string
            statement.setString(3, log.getStatus());
            statement.setInt(4, log.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Log log) {
        Connection connection = SingletonConnectionDB.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM logs WHERE id = ?");
            statement.setInt(1, log.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Log> findAll() {
        Connection connection = SingletonConnectionDB.getConnection();
        List<Log> logs = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM logs");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Log log = new Log();
                log.setId(resultSet.getInt("id"));
                log.setUtilisateur(new UtilisateurDaoImpl().findById(resultSet.getInt("user_id")));
                log.setAccessTime(parseTimestamp(resultSet.getString("access_time"))); // Parse timestamp manually
                log.setStatus(resultSet.getString("status"));
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    private Timestamp parseTimestamp(String timestampStr) {
        try {
            java.util.Date parsedDate = TIMESTAMP_FORMAT.parse(timestampStr);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }
}
