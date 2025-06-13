package jeu.jungle.Dal;

import jeu.jungle.Bo.Joueur;
import java.sql.*;
import java.util.Optional;

public class RepositoryJoueur implements IRepositoryJoueur {
    private final IConnexionBaseDeDonnees connexionBD;

    public RepositoryJoueur(IConnexionBaseDeDonnees connexionBD) {
        this.connexionBD = connexionBD;
    }

    @Override
    public Optional<Joueur> trouverParNomUtilisateur(String nomUtilisateur) {
        String sql = "SELECT * FROM joueurs WHERE nom_utilisateur = ?";
        try (Connection conn = connexionBD.obtenirConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomUtilisateur);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Joueur joueur = new Joueur(
                        rs.getInt("id"),
                        rs.getString("nom_utilisateur"),
                        rs.getString("mot_de_passe"),
                        rs.getInt("victoires"),
                        rs.getInt("defaites"),
                        rs.getInt("egalites")
                );
                return Optional.of(joueur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean creerJoueur(Joueur joueur) {
        String sql = "INSERT INTO joueurs(nom_utilisateur, mot_de_passe) VALUES(?, ?)";
        try (Connection conn = connexionBD.obtenirConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, joueur.getNomUtilisateur());
            stmt.setString(2, joueur.getMotDePasse());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean mettreAJourStatistiques(Joueur joueur) {
        String sql = "UPDATE joueurs SET victoires = ?, defaites = ?, egalites = ? WHERE id = ?";
        try (Connection conn = connexionBD.obtenirConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, joueur.getVictoires());
            stmt.setInt(2, joueur.getDefaites());
            stmt.setInt(3, joueur.getEgalites());
            stmt.setInt(4, joueur.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}