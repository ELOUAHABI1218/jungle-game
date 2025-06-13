package jeu.jungle.BLL;

import jeu.jungle.Bo.Partie;
import jeu.jungle.Bo.Joueur;
import java.util.List;

public interface IServiceJeu {
    Partie commencerNouvellePartie(Joueur joueur1, Joueur joueur2);
    boolean effectuerDeplacement(Partie partie, int depuisX, int depuisY, int versX, int versY);
    boolean sauvegarderResultatPartie(Partie partie);
    List<Partie> obtenirHistoriqueParties(Joueur joueur);
}
