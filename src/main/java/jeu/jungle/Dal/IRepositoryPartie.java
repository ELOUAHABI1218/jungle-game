package jeu.jungle.Dal;

import jeu.jungle.Bo.Partie;
import java.util.List;

public interface IRepositoryPartie {
    boolean sauvegarderPartie(Partie partie);
    List<Partie> obtenirHistoriqueJoueur(int idJoueur);
}
