package jeu.jungle.Bo;



import jeu.jungle.BLL.ServiceJeu;

import java.time.LocalDateTime;



import java.time.LocalDateTime;

/**
 * Représente une partie entre deux joueurs
 */
public class Partie {
    private int id;
    private int idJoueur1;
    private int idJoueur2;
    private Integer idVainqueur; // null si égalité
    private Plateau plateau;
    private Joueur nomJoueur1;
    private Joueur nomJoueur2;
    private Joueur nomVainqueur;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private int joueurActuel;

    // Constructeur
    public Partie(int id, int idJoueur1, int idJoueur2, Plateau plateau) {
        this.id = id;
        this.idJoueur1 = idJoueur1;
        this.idJoueur2 = idJoueur2;
        this.plateau = plateau;
        this.dateDebut = LocalDateTime.now();
        this.nomJoueur1 = new Joueur("Joueur " + idJoueur1);
        this.nomJoueur2 = new Joueur("Joueur " + idJoueur2);
    }
    public Partie(int id, Joueur nomJoueur1, Joueur nomJoueur2, Plateau plateau) {
        this.id = id;
        this.nomJoueur1 = nomJoueur1;
        this.nomJoueur2= nomJoueur2;
        this.plateau = plateau;
        this.dateDebut = LocalDateTime.now();
    }

    public Partie(int id, int joueur1Id, int joueur2Id, int vainqueurId, String nomJoueur1, String nomJoueur2, String nomVainqueur, LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.id=id;
        this.idJoueur1=joueur1Id;
        this.idJoueur2=joueur2Id;
        this.idVainqueur=vainqueurId;
        this.nomJoueur1=new Joueur(nomJoueur1);
        this.nomJoueur2=new Joueur(nomJoueur2);
        this.nomVainqueur=new Joueur(nomVainqueur);



    }


    public Plateau getPlateau() {
        return plateau;
    }
    public Integer getIdVainqueur() {
        return idVainqueur; }
    public void setIdVainqueur(Integer idVainqueur) {
        this.idVainqueur = idVainqueur;
        this.dateFin = LocalDateTime.now();
    }

    public int getIdJoueur1() {
        return idJoueur1;
    }

    public int getIdJoueur2() {
        return idJoueur2;
    }

    public Joueur getNomJoueur1() {
        return nomJoueur1;
    }

    public Joueur getNomJoueur2() {
        return nomJoueur2;
    }



    public LocalDateTime getDateDebut() {
        return dateDebut;
    }
    public String getNomVainqueur() {
        if (this.nomVainqueur == null) {
            return null;
        }
        return this.nomVainqueur.getNomUtilisateur();
    }

    public int getJoueurActuel() {
        return joueurActuel;
    }
    public void changerTour() {
        this.joueurActuel = 3 - this.joueurActuel; // Alterne entre 1 et 2
    }
    public void passerAuJoueurSuivant() {
        this.joueurActuel = (this.joueurActuel == 1) ? 2 : 1;
    }
    public Partie(Joueur joueur1, Joueur joueur2) {
        this.nomJoueur1 = joueur1;
        this.nomJoueur2= joueur2;
        this.plateau = new Plateau();
        this.plateau.initialiser();
        this.joueurActuel = 1; // Le joueur 1 commence
    }
    public boolean estTerminee() {
        // 1. Vérifier si un joueur a atteint SON sanctuaire adverse (case S)
        if (aAtteintSanctuaireAdverse(1) || aAtteintSanctuaireAdverse(2)) {
            return true;
        }

        // 2. Vérifier si un joueur n'a plus de pièces (optionnel)
        return !joueurAPieces(1) || !joueurAPieces(2);
    }

    /**
     * Vérifie si le joueur a une pièce sur LE SANCTUAIRE ADVERSAIRE (case S).
     */
    private boolean aAtteintSanctuaireAdverse(int joueur) {
        int xSanctuaire = 3;
        int ySanctuaire = (joueur == 1) ? 8 : 0;

        Piece piece = plateau.obtenirPiece(xSanctuaire, ySanctuaire);
        if (piece != null && piece.getJoueur() == joueur) {
            this.idVainqueur = (joueur == 1) ? idJoueur1 : idJoueur2;
            this.nomVainqueur = (joueur == 1) ? nomJoueur1 : nomJoueur2;
            return true;
        }
        return false;
    }

    /**
     * Vérifie si le joueur a encore des pièces sur le plateau.
     */
    private boolean joueurAPieces(int joueur) {
        for (int x = 0; x < Plateau.LARGEUR; x++) {
            for (int y = 0; y < Plateau.HAUTEUR; y++) {
                Piece piece = plateau.obtenirPiece(x, y);
                if (piece != null && piece.getJoueur() == joueur) {
                    return true;
                }
            }
        }
        return false;
    }}
