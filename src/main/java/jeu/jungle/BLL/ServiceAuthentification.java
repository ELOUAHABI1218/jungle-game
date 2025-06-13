package jeu.jungle.BLL;


import jeu.jungle.Dal.IRepositoryJoueur;
import jeu.jungle.Bo.Joueur;
import java.util.Optional;

public class ServiceAuthentification implements IServiceAuthentification {
    private final IRepositoryJoueur repositoryJoueur;

    public ServiceAuthentification(IRepositoryJoueur repositoryJoueur) {
        this.repositoryJoueur = repositoryJoueur;
    }

    @Override
    public Joueur connecter(String nomUtilisateur, String motDePasse) {
        Optional<Joueur> joueurOpt = repositoryJoueur.trouverParNomUtilisateur(nomUtilisateur);
        if (joueurOpt.isPresent() && joueurOpt.get().getMotDePasse().equals(motDePasse)) {
            return joueurOpt.get();
        }
        return null;
    }

    @Override
    public boolean inscrire(String nomUtilisateur, String motDePasse) {
        if (repositoryJoueur.trouverParNomUtilisateur(nomUtilisateur).isPresent()) {
            return false; // Nom d'utilisateur déjà pris
        }
        Joueur nouveauJoueur = new Joueur(0, nomUtilisateur, motDePasse, 0, 0, 0);
        return repositoryJoueur.creerJoueur(nouveauJoueur);
    }
}
