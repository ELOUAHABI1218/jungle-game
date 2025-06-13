package jeu.jungle.Dal;

import java.sql.*;

public class ConnexionBaseDeDonnees implements IConnexionBaseDeDonnees {
    private static final String URL_BD = "jdbc:sqlite:jungle.db";
    private Connection connexion;

    @Override
    public Connection obtenirConnexion() throws SQLException {
        if (connexion == null || connexion.isClosed()) {
            connexion = DriverManager.getConnection(URL_BD);
            initialiserBaseDeDonnees(connexion);
        }
        return connexion;
    }

    @Override
    public void fermerConnexion() throws SQLException {
        if (connexion != null && !connexion.isClosed()) {
            connexion.close();
        }
    }





        private void initialiserBaseDeDonnees(Connection conn) throws SQLException {
            String[] requetes = {
                    "CREATE TABLE IF NOT EXISTS joueurs (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "nom_utilisateur TEXT UNIQUE NOT NULL, " +
                            "mot_de_passe TEXT NOT NULL, " +
                            "victoires INTEGER DEFAULT 0, " +
                            "defaites INTEGER DEFAULT 0, " +
                            "egalites INTEGER DEFAULT 0)",

                    "CREATE TABLE IF NOT EXISTS parties (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "joueur1_id INTEGER NOT NULL, " +
                            "joueur2_id INTEGER NOT NULL, " +
                            "vainqueur_id INTEGER, " +
                            "date_debut TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "date_fin TIMESTAMP, " +
                            "FOREIGN KEY(joueur1_id) REFERENCES joueurs(id), " +
                            "FOREIGN KEY(joueur2_id) REFERENCES joueurs(id))"
            };

            try (Statement stmt = conn.createStatement()) {
                for (String requete : requetes) {
                    stmt.execute(requete);
                }
            }
        }
    }
