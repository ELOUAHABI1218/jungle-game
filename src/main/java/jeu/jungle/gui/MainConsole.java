package jeu.jungle.gui;




import jeu.jungle.BLL.ServiceAuthentification;
import jeu.jungle.BLL.ServiceJeu;
import jeu.jungle.Dal.ConnexionBaseDeDonnees;
import jeu.jungle.Dal.RepositoryJoueur;
import jeu.jungle.Dal.RepositoryPartie;


public class MainConsole {
    public static void main(String[] args) {
        // Initialisation de la base de données
        ConnexionBaseDeDonnees connexionBD = new ConnexionBaseDeDonnees();

        // Initialisation des repositories
        RepositoryJoueur repositoryJoueur = new RepositoryJoueur(connexionBD);
        RepositoryPartie repositoryPartie = new RepositoryPartie(connexionBD);

        // Initialisation des services
        ServiceAuthentification serviceAuth = new ServiceAuthentification(repositoryJoueur);
        ServiceJeu serviceJeu = new ServiceJeu(repositoryPartie);

        // Création et démarrage de l'interface
        InterfaceConsole interfaceJeu = new InterfaceConsole(serviceAuth, serviceJeu,repositoryJoueur);
        interfaceJeu.demarrer();
    }
    }

