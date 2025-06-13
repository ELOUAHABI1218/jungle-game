package jeu.jungle.BLL;
import jeu.jungle.Dal.IRepositoryPartie;
import jeu.jungle.Bo.*;
import java.util.List;

public class ServiceJeu implements IServiceJeu {
    private final IRepositoryPartie repositoryPartie;

    public ServiceJeu(IRepositoryPartie repositoryPartie) {
        this.repositoryPartie = repositoryPartie;
    }

    @Override
    public Partie commencerNouvellePartie(Joueur joueur1, Joueur joueur2) {
        Partie partie = new Partie(joueur1, joueur2);
        System.out.println("DEBUG: Nouvelle partie - Joueur actuel: " + partie.getJoueurActuel());
        return partie;
    }

    @Override
    // Dans ServiceJeu.java
    public boolean effectuerDeplacement(Partie partie, int x1, int y1, int x2, int y2) {
        // Vérification initiale
        if (partie.getJoueurActuel() != 1 && partie.getJoueurActuel() != 2) {
            System.out.println("ERREUR CRITIQUE: joueurActuel = " + partie.getJoueurActuel());
            return false;
        }

        Piece piece = partie.getPlateau().obtenirPiece(x1, y1);
        if (piece == null) {
            System.out.println("Aucune pièce à cette position");
            return false;
        }

        System.out.println("Déplacement tenté par Joueur " + partie.getJoueurActuel() +
                " sur pièce appartenant à Joueur " + piece.getJoueur());

        if (piece.getJoueur() != partie.getJoueurActuel()) {
            System.out.println("Cette pièce ne vous appartient pas");
            return false;
        }
        if (piece == null || piece.getJoueur() != partie.getJoueurActuel()) {
            System.out.println("Ce n'est pas votre tour ou pièce invalide!");
            return false;
        }

        // Vérifier les règles de déplacement
        if (!estDeplacementValide(partie.getPlateau(),x1, y1, x2, y2)) {
            return false;
        }

        // Effectuer le déplacement
        partie.getPlateau().deplacerPiece(x1, y1, x2, y2);

        // Passer au joueur suivant
        partie.passerAuJoueurSuivant();
        return true;
    }

    public boolean estDeplacementValide(Plateau plateau, int x1, int y1, int x2, int y2) {
        // 1. Vérifier que les coordonnées sont dans les limites
        if (x1 < 0 || x1 >= Plateau.LARGEUR || y1 < 0 || y1 >= Plateau.HAUTEUR ||
                x2 < 0 || x2 >= Plateau.LARGEUR || y2 < 0 || y2 >= Plateau.HAUTEUR) {
            return false;
        }

        Piece piece = plateau.obtenirPiece(x1, y1);
        Piece cible = plateau.obtenirPiece(x2, y2);

        // 2. Vérifier qu'il y a une pièce à déplacer
        if (piece == null) {
            return false;
        }

        // 3. Vérifier le type de déplacement
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        // 3a. Déplacement standard (1 case horizontal/vertical)
        if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) {
            return estDeplacementStandardValide(plateau, piece, x1, y1, x2, y2);
        }
        // 3b. Saut de rivière (Lion/Tigre)
        else if ((dx > 1 || dy > 1) && piece.getType().peutSauterRiviere()) {
            return estSautRiviereValide(plateau, piece, x1, y1, x2, y2);
        }

        return false;
    }

    private boolean estDeplacementStandardValide(Plateau plateau, Piece piece, int x1, int y1, int x2, int y2) {
        Piece cible = plateau.obtenirPiece(x2, y2);

        // 1. Vérifier case destination
        if (cible != null && cible.getJoueur() == piece.getJoueur()) {
            return false; // Case occupée par une pièce alliée
        }

        // 2. Vérifier les zones spéciales
        if (estDansSaPropreRiviere(piece, x2, y2)) {
            return false;
        }

        if (estDansSonPropreSanctuaire(piece, x2, y2)) {
            return false;
        }

        // 3. Règles spéciales pour le Rat
        if (piece.getType() == TypePiece.RAT) {
            if (estDansEau(x1, y1) && !estDansEau(x2, y2) && cible != null) {
                return false; // Rat ne peut pas capturer en sortant de l'eau
            }
        }

        return true;
    }

    private boolean estSautRiviereValide(Plateau plateau, Piece piece, int x1, int y1, int x2, int y2) {
        // 1. Doit être horizontal ou vertical pur
        if (!(x1 == x2 || y1 == y2)) {
            return false;
        }

        // 2. Vérifier la trajectoire
        int stepX = Integer.compare(x2, x1);
        int stepY = Integer.compare(y2, y1);

        int x = x1 + stepX;
        int y = y1 + stepY;

        while (x != x2 || y != y2) {
            // 2a. Toute la trajectoire doit être de l'eau
            if (!estRiviere(x, y)) {
                return false;
            }

            // 2b. Ne peut pas sauter par-dessus un Rat
            if (plateau.obtenirPiece(x, y) != null) {
                return false;
            }

            x += stepX;
            y += stepY;
        }

        // 3. Vérifier la case destination
        Piece cible = plateau.obtenirPiece(x2, y2);
        return cible == null || cible.getJoueur() != piece.getJoueur();
    }

    // Méthodes utilitaires
    private boolean estRiviere(int x, int y) {
        return (x >= 1 && x <= 7) && (y >= 3 && y <= 5);
    }

    private boolean estDansEau(int x, int y) {
        return estRiviere(x, y);
    }

    private boolean estDansSaPropreRiviere(Piece piece, int x, int y) {
        return estRiviere(x, y) && piece.getType() != TypePiece.RAT;
    }

    private boolean estDansSonPropreSanctuaire(Piece piece, int x, int y) {
        int sanctuaireY = (piece.getJoueur() == 1) ? 0 : 8;
        return (x == 3 && y == sanctuaireY);
    }

    private boolean estPrise(Plateau plateau, Piece piece, int versX, int versY) {
        return plateau.obtenirPiece(versX, versY) != null &&
                plateau.obtenirPiece(versX, versY).getJoueur() != piece.getJoueur();
    }

    private boolean peutCapturer(Piece attaquant, Piece defenseur) {
        if (defenseur == null) return false;

        // Rat peut capturer Elephant
        if (attaquant.getType() == TypePiece.RAT && defenseur.getType() == TypePiece.ELEPHANT) {
            return true;
        }

        // Elephant ne peut pas capturer Rat
        if (attaquant.getType() == TypePiece.ELEPHANT && defenseur.getType() == TypePiece.RAT) {
            return false;
        }

        return attaquant.getType().getRang() >= defenseur.getType().getRang();
    }

    @Override
    public boolean sauvegarderResultatPartie(Partie partie) {
        return repositoryPartie.sauvegarderPartie(partie);
    }

    @Override
    public List<Partie> obtenirHistoriqueParties(Joueur joueur) {
        return repositoryPartie.obtenirHistoriqueJoueur(joueur.getId());
    }

}
