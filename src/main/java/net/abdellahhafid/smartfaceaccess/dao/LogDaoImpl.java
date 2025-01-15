package net.abdellahhafid.smartfaceaccess.dao;

import net.abdellahhafid.smartfaceaccess.Models.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDaoImpl implements LogDao {

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
                log.setUserId(resultSet.getInt("user_id"));
                log.setAccessTime(resultSet.getTimestamp("access_time"));
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
            statement.setInt(1, log.getUserId());
            statement.setTimestamp(2, log.getAccessTime());
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
            statement.setInt(1, log.getUserId());
            statement.setTimestamp(2, log.getAccessTime());
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
                log.setUserId(resultSet.getInt("user_id"));
                log.setAccessTime(resultSet.getTimestamp("access_time"));
                log.setStatus(resultSet.getString("status"));
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}
