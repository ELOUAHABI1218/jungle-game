package jeu.jungle.gui;

// ui/InterfaceConsole.java


import jeu.jungle.BLL.IServiceAuthentification;
import jeu.jungle.BLL.IServiceJeu;
import jeu.jungle.Bo.Joueur;
import jeu.jungle.Bo.Partie;
import jeu.jungle.Bo.Piece;
import jeu.jungle.Bo.Plateau;
import jeu.jungle.Dal.IRepositoryJoueur;

import java.util.List;
import java.util.Scanner;

public class InterfaceConsole {
    private final IServiceAuthentification serviceAuth;
    private final IServiceJeu serviceJeu;
    private final Scanner scanner;
    private Joueur joueurConnecte;

    public InterfaceConsole(IServiceAuthentification serviceAuth, IServiceJeu serviceJeu) {
        this.serviceAuth = serviceAuth;
        this.serviceJeu = serviceJeu;
        this.scanner = new Scanner(System.in);
    }

    public void demarrer() {
        System.out.println("============================================ JEU XOU DOU QI (JUNGLE) ============================================");

        while (true) {
            if (joueurConnecte == null) {
                afficherMenuConnexion();
            } else {
                afficherMenuPrincipal();
            }
        }
    }

    private void afficherMenuConnexion() {
        System.out.println("\n1. Se connecter");
        System.out.println("2. S'inscrire");
        System.out.println("3. Quitter");
        System.out.print("Votre choix : ");

        int choix = scanner.nextInt();
        scanner.nextLine(); // Nettoyer le buffer

        switch (choix) {
            case 1 -> connecterJoueur();
            case 2 -> inscrireJoueur();
            case 3 -> System.exit(0);
            default -> System.out.println("Option invalide");
        }
    }

    private void connecterJoueur() {
        System.out.print("Nom d'utilisateur : ");
        String nom = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = scanner.nextLine();

        joueurConnecte = serviceAuth.connecter(nom, mdp);
        if (joueurConnecte != null) {
            System.out.println("Connexion réussie !");
        } else {
            System.out.println("Échec de la connexion");
        }
    }

    private void inscrireJoueur() {
        System.out.print("Choisir un nom d'utilisateur : ");
        String nom = scanner.nextLine();
        System.out.print("Choisir un mot de passe : ");
        String mdp = scanner.nextLine();

        if (serviceAuth.inscrire(nom, mdp)) {
            System.out.println("Inscription réussie !");
        } else {
            System.out.println("Ce nom d'utilisateur est déjà pris");
        }
    }

    private void afficherMenuPrincipal() {
        System.out.println("\n============================================== MENU PRINCIPAL ==============================================");
        System.out.println("1. Nouvelle partie");
        System.out.println("2. Historique des parties");
        System.out.println("3. Règles du jeu");
        System.out.println("4. Se déconnecter");
        System.out.print("Votre choix : ");

        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1 -> demarrerNouvellePartie();
            case 2 -> afficherHistorique();
            case 3 -> afficherRegles();
            case 4 -> joueurConnecte = null;
            default -> System.out.println("Option invalide");
        }
    }

    private void demarrerNouvellePartie() {
        System.out.print("Nom d'utilisateur de l'adversaire : ");
        String adversaire = scanner.nextLine();

        // Création d'un joueur temporaire (remplacé par recherche en base)
        Joueur joueur2 = new Joueur(0, adversaire, "", 0, 0, 0);

        Partie partie = serviceJeu.commencerNouvellePartie(joueurConnecte, joueur2);
        jouerPartie(partie);
    }

    private void jouerPartie(Partie partie) {

        System.out.println("\n============================================ NOUVELLE PARTIE ============================================");

        System.out.println(" Début partie - Joueur actuel: " + partie.getJoueurActuel());
        int joueurActuel = 1; // Commence par le joueur 1
        boolean partieTerminee = false;

        while (!partieTerminee) {
            // Afficher le plateau AVANT chaque tour
            afficherPlateau(partie.getPlateau());

            if (partie.estTerminee()) {
                partieTerminee = true;

                afficherResultat(partie);
                // Sauvegarder le résultat
                serviceJeu.sauvegarderResultatPartie(partie);

                break;
            }

            System.out.println("Tour de " +
                    (joueurActuel == 1 ? partie.getNomJoueur1().getNomUtilisateur()
                            : partie.getNomJoueur2().getNomUtilisateur()));

            System.out.print("> ");
            String commande = scanner.nextLine().trim().toLowerCase();


            if (commande.equals("abandonner")) {
                partieTerminee = true;
                partie.setIdVainqueur(joueurActuel == 1 ? partie.getIdJoueur2() : partie.getIdJoueur1());
                System.out.println("Partie abandonnée");
            } else if (commande.startsWith("deplacer ")) {
                String[] coordonnees = commande.split(" ");
                if (coordonnees.length == 5) {
                    try {
                        int x1 = Integer.parseInt(coordonnees[1]);
                        int y1 = Integer.parseInt(coordonnees[2]);
                        int x2 = Integer.parseInt(coordonnees[3]);
                        int y2 = Integer.parseInt(coordonnees[4]);

                        // DEBUG: Afficher les coordonnées avant déplacement
                        System.out.println("Tentative de déplacement de (" + x1 + "," + y1 + ") à (" + x2 + "," + y2 + ")");

                        if (serviceJeu.effectuerDeplacement(partie, x1, y1, x2, y2)) {
                            // DEBUG: Confirmation du déplacement
                            System.out.println("Déplacement effectué avec succès!");

                            // Afficher le plateau APRÈS déplacement
                            afficherPlateau(partie.getPlateau());

                            if (partie.getIdVainqueur() != null) {
                                partieTerminee = true;
                                afficherResultat(partie);
                            }
                            joueurActuel = 3 - joueurActuel;
                        } else {
                            System.out.println("Déplacement invalide! Raisons possibles:");
                            System.out.println("- Ce n'est pas votre pièce");
                            System.out.println("- Déplacement non autorisé");
                            System.out.println("- Case destination occupée");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Format incorrect. Usage: deplacer x1 y1 x2 y2");
                    }
                }
            } else if (commande.equals("aide")) {
                afficherRegles();
            } else {
                System.out.println("Commande inconnue");
            }
        }
        // Sauvegarder le résultat
        serviceJeu.sauvegarderResultatPartie(partie);

        // Mettre à jour les statistiques
        if (partie.getIdVainqueur() != null) {
            if (partie.getIdVainqueur().equals(joueurConnecte.getId())) {
                joueurConnecte.incrementerVictoires();
            } else {
                joueurConnecte.incrementerDefaites();
            }

        }

    }
    private void afficherPlateau(Plateau plateau) {
        // En-tête avec lettres de  (7 colonnes)
        System.out.println("\n     0   1   2   3   4   5   6");
        System.out.println("   +---+---+---+---+---+---+---+");

        for (int y = 0; y < Plateau.HAUTEUR; y++) { // 9 lignes
            // Numéro de ligne (1-9)
            System.out.print((y) + " |");

            for (int x = 0; x < Plateau.LARGEUR; x++) { // 7 colonnes
                Piece piece = plateau.obtenirPiece(x, y);
                String cellule = "   "; // Case vide par défaut

                // Identifier les cases spéciales
                if (estSanctuaire(x, y)) {
                    cellule = " S ";
                } else if (estPiege(x, y)) {
                    cellule = " P ";
                } else if (estRiviere(x, y)) {
                    cellule = "~~~";
                }

                // Afficher la pièce si présente
                if (piece != null) {
                    cellule = String.format("%3s", getSymbolePiece(piece));
                }

                System.out.print(cellule + "|");
            }
            System.out.println("\n   +---+---+---+---+---+---+---+");
        }

        // Légende améliorée
        System.out.println("\nLégende:");
        System.out.println("S: Sanctuaire | P: Piège | ~~~: Rivière");
        System.out.println("Li:Lion, Ti:Tigre, El:Éléphant, Pa:Panthère");
        System.out.println("Ch:Chien, Lo:Loup, Ca:Chat, Ra:Rat");
        System.out.println("(1:Joueur1, 2:Joueur2)");
        System.out.println("\n ------------------------------------------\n");
        System.out.println("Commandes disponibles :");
        System.out.println("- deplacer x1 y1 x2 y2 : Déplacer une pièce");
        System.out.println("- abandonner : Quitter la partie");
        System.out.println("- aide : Afficher les règles");
    }

    // Méthodes utilitaires adaptées pour 9x7
    private boolean estSanctuaire(int x, int y) {
        return (x == 3 && y == 0) || (x == 3 && y == 8); // Colonne D, lignes 1 et 9
    }

    private boolean estPiege(int x, int y) {
        // Positions des pièges (3 par joueur)
        return (x == 2 && y == 0) || (x == 4 && y == 0) || (x == 3 && y == 1) || // Joueur 1
                (x == 2 && y == 8) || (x == 4 && y == 8) || (x == 3 && y == 7);   // Joueur 2
    }

    private boolean estRiviere(int x, int y) {
        // Zone centrale verticale (2 colonnes centrales)
        return (x == 1 || x == 2 || x == 4|| x == 5) && (y >= 3 && y <= 5);
    }

    private String getSymbolePiece(Piece piece) {
        String symbole;
        switch (piece.getType()) {
            case ELEPHANT: symbole = "El"; break;
            case LION:     symbole = "Li"; break;
            case TIGRE:    symbole = "Ti"; break;
            case PANTHERE: symbole = "Pa"; break;
            case CHIEN:    symbole = "Ch"; break;
            case LOUP:     symbole = "Lo"; break;
            case CHAT:     symbole = "Ca"; break;
            case RAT:      symbole = "Ra"; break;
            default:       symbole = "??";
        }
        return symbole + piece.getJoueur();
    }



    private void afficherResultat(Partie partie) {
        if (partie.getIdVainqueur() == null) {
            System.out.println("Match nul");
        } else {
            String message = partie.getIdVainqueur().equals(joueurConnecte.getId())
                    ? "\n    ----------------------- Félicitations ! Vous avez gagné! ---------------------\n"
                    : " \n   ------------------------  Le vainqueur est " + partie.getNomVainqueur()+"  ------------------------\n";
            System.out.println(message);
        }
    }

    private void afficherHistorique() {
        List<Partie> historique = serviceJeu.obtenirHistoriqueParties(joueurConnecte);
        System.out.println("\n============================================== VOTRE HISTORIQUE ============================================");

        if (historique.isEmpty()) {
            System.out.println("Aucune partie enregistrée");
        } else {
            for (Partie partie : historique) {
                String resultat;
                if (partie.getIdVainqueur() == null) {
                    resultat = "Égalité";
                } else if (partie.getNomVainqueur().equals(joueurConnecte.getNomUtilisateur())) {
                    resultat = "Victoire";
                } else {
                    resultat = "Défaite";
                }

                System.out.printf("%s vs %s - %s \n",
                        partie.getNomJoueur1().getNomUtilisateur(),
                        partie.getNomJoueur2().getNomUtilisateur(),
                        resultat);
            }
        }
    }

    private void afficherRegles() {
        System.out.println("\n========================================= RÈGLES DU JEU =========================================");
        System.out.println("Hiérarchie des animaux (du plus fort au plus faible):");
        System.out.println("1. Éléphant");
        System.out.println("2. Lion");
        System.out.println("3. Tigre");
        System.out.println("4. Panthère");
        System.out.println("5. Chien");
        System.out.println("6. Loup");
        System.out.println("7. Chat");
        System.out.println("8. Rat");
        System.out.println("\nRègles spéciales:-------------------------------------------------------------------------------------------------------------------");
        System.out.println("- Le Rat peut capturer l'Éléphant");
        System.out.println("- Le Lion et le Tigre peuvent sauter par-dessus la rivière");
        System.out.println("- Les pièces se déplacent toutes d’une case horizontalement ou verticalement mais ne peuvent pénétrer dans leur propre sanctuaire");
        System.out.println("- Le rat est le seul à pouvoir se déplacer dans l’eau");
        System.out.println("- Le premier à atteindre le sanctuaire ennemi gagne.");
    }
}
