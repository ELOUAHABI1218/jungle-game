package jeu.jungle.Dal;
import java.sql.Connection;
import java.sql.SQLException;

public interface IConnexionBaseDeDonnees {
    Connection obtenirConnexion() throws SQLException;
    void fermerConnexion() throws SQLException;
}
