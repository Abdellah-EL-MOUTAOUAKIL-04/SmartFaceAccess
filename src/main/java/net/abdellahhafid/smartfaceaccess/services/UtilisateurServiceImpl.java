package net.abdellahhafid.smartfaceaccess.services;

import net.abdellahhafid.smartfaceaccess.models.Utilisateur;
import net.abdellahhafid.smartfaceaccess.dao.UtilisateurDao;
import net.abdellahhafid.smartfaceaccess.dao.UtilisateurDaoImpl;

import java.util.List;

public class UtilisateurServiceImpl implements UtilisateurService {
    private UtilisateurDao utilisateurDao = new UtilisateurDaoImpl();

    @Override
    public Utilisateur findById(Integer id) {
        return utilisateurDao.findById(id);
    }

    @Override
    public void save(Utilisateur utilisateur) {
        utilisateurDao.save(utilisateur);
    }

    @Override
    public void update(Utilisateur utilisateur) {
        utilisateurDao.update(utilisateur);
    }

    @Override
    public void delete(Utilisateur utilisateur) {
        utilisateurDao.delete(utilisateur);
    }

    @Override
    public List<Utilisateur> findAll() {
        return utilisateurDao.findAll();
    }
}
