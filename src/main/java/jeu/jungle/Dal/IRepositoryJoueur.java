package jeu.jungle.Dal;

import jeu.jungle.Bo.Joueur;
import java.util.Optional;

public interface IRepositoryJoueur {
    Optional<Joueur> trouverParNomUtilisateur(String nomUtilisateur);
    boolean creerJoueur(Joueur joueur);
    boolean mettreAJourStatistiques(Joueur joueur);
}
