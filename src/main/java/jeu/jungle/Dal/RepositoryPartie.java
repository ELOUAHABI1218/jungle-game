package jeu.jungle.Dal;

import jeu.jungle.Bo.Partie;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RepositoryPartie implements IRepositoryPartie {
    private final IConnexionBaseDeDonnees connexionBD;

    public RepositoryPartie(IConnexionBaseDeDonnees connexionBD) {
        this.connexionBD = connexionBD;
    }

    @Override
    public boolean sauvegarderPartie(Partie partie) {

            String sql = "INSERT INTO parties(joueur1_id, joueur2_id, vainqueur_id) VALUES(?, ?, ?)";

            try (Connection conn = connexionBD.obtenirConnexion();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {



                stmt.setInt(1, partie.getNomJoueur1().getId());
                stmt.setInt(2, partie.getNomJoueur2().getId());

                if (partie.getIdVainqueur() != null) {
                    stmt.setInt(3, partie.getNomVainqueur().getId());
                } else {
                    stmt.setNull(3, Types.INTEGER);
                }



                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("Erreur SQL: " + e.getMessage());
                return false;
            }
        }



    @Override
    public List<Partie> obtenirHistoriqueJoueur(int idJoueur) {
        List<Partie> historique = new ArrayList<>();
        String sql = "SELECT p.*, j1.nom_utilisateur as nom_joueur1, j2.nom_utilisateur as nom_joueur2, " +
                "j3.nom_utilisateur as nom_vainqueur FROM parties p " +
                "JOIN joueurs j1 ON p.joueur1_id = j1.id " +
                "JOIN joueurs j2 ON p.joueur2_id = j2.id " +
                "LEFT JOIN joueurs j3 ON p.vainqueur_id = j3.id " +
                "WHERE p.joueur1_id = ? OR p.joueur2_id = ? " +
                "ORDER BY p.date_fin DESC";

        try (Connection conn = connexionBD.obtenirConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idJoueur);
            stmt.setInt(2, idJoueur);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Partie partie = new Partie(
                        rs.getInt("id"),
                        rs.getInt("joueur1_id"),
                        rs.getInt("joueur2_id"),
                        rs.getInt("vainqueur_id"),
                        rs.getString("nom_joueur1"),
                        rs.getString("nom_joueur2"),
                        rs.getString("nom_vainqueur")
                        );
                historique.add(partie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historique;
    }
}
