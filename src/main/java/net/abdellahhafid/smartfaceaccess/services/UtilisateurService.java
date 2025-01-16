package net.abdellahhafid.smartfaceaccess.services;

import net.abdellahhafid.smartfaceaccess.models.Utilisateur;

import java.util.List;

public interface UtilisateurService {
    Utilisateur findById(Integer id);
    void save(Utilisateur utilisateur);
    void update(Utilisateur utilisateur);
    void delete(Utilisateur utilisateur);
    List<Utilisateur> findAll();
}
