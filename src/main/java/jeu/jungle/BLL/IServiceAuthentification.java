package jeu.jungle.BLL;
import jeu.jungle.Bo.Joueur;

public interface IServiceAuthentification {
    Joueur connecter(String nomUtilisateur, String motDePasse);
    boolean inscrire(String nomUtilisateur, String motDePasse);
}
