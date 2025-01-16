package net.abdellahhafid.smartfaceaccess.dao;

import net.abdellahhafid.smartfaceaccess.models.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDaoImpl implements UtilisateurDao {
    @Override
    public Utilisateur findById(Integer id) {
        Connection connection = SingletonConnectionDB.getConnection();
        Utilisateur utilisateur = null;
        try  {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM utilisateur WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                utilisateur = new Utilisateur();
                utilisateur.setId(resultSet.getInt("id"));
                utilisateur.setName(resultSet.getString("name"));
                utilisateur.setEmail(resultSet.getString("email"));
                utilisateur.setNumero(resultSet.getString("numero"));
                utilisateur.setFaceImage(resultSet.getBytes("face_image"));
                utilisateur.setEtage(resultSet.getInt("etage"));
                utilisateur.setFonctionne(resultSet.getString("fonctionne"));
                utilisateur.setAccessStatus(resultSet.getString("access_status"));
                utilisateur.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }

    @Override
    public void save(Utilisateur utilisateur) {
        Connection connection = SingletonConnectionDB.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO utilisateur (name, email, numero, face_image, etage, fonctionne, access_status,password) VALUES (?, ?, ?, ?, ?, ?, ?,?)");
            statement.setString(1, utilisateur.getName());
            statement.setString(2, utilisateur.getEmail());
            statement.setString(3, utilisateur.getNumero());
            statement.setBytes(4, utilisateur.getFaceImage());
            statement.setInt(5, utilisateur.getEtage());
            statement.setString(6, utilisateur.getFonctionne());
            statement.setString(7, utilisateur.getAccessStatus());
            statement.setString(8, utilisateur.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Utilisateur utilisateur) {
        Connection connection = SingletonConnectionDB.getConnection();
        try  {
            PreparedStatement statement = connection.prepareStatement("UPDATE utilisateur SET name = ?, email = ?, numero = ?, face_image = ?, etage = ?, fonctionne = ?, access_status = ?,password=? WHERE id = ?");
            statement.setString(1, utilisateur.getName());
            statement.setString(2, utilisateur.getEmail());
            statement.setString(3, utilisateur.getNumero());
            statement.setBytes(4, utilisateur.getFaceImage());
            statement.setInt(5, utilisateur.getEtage());
            statement.setString(6, utilisateur.getFonctionne());
            statement.setString(7, utilisateur.getAccessStatus());
            statement.setString(8,utilisateur.getPassword());
            statement.setInt(9, utilisateur.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Utilisateur utilisateur) {
        Connection connection = SingletonConnectionDB.getConnection();
        try  {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM utilisateur WHERE id = ?");
            statement.setInt(1, utilisateur.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Utilisateur> findAll() {
        Connection connection = SingletonConnectionDB.getConnection();
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM utilisateur");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(resultSet.getInt("id"));
                utilisateur.setName(resultSet.getString("name"));
                utilisateur.setEmail(resultSet.getString("email"));
                utilisateur.setNumero(resultSet.getString("numero"));
                utilisateur.setFaceImage(resultSet.getBytes("face_image"));
                utilisateur.setEtage(resultSet.getInt("etage"));
                utilisateur.setFonctionne(resultSet.getString("fonctionne"));
                utilisateur.setAccessStatus(resultSet.getString("access_status"));
                utilisateur.setPassword(resultSet.getString("password"));
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }
}