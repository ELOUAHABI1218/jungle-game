package jeu.jungle.Bo;
/**
 * Représente une pièce du jeu avec sa position et son appartenance à un joueur
 */
public class Piece {
    private TypePiece type;
    private int joueur; // 1 (Joueur 1) ou 2 (Joueur 2)
    private int x, y;   // Position sur le plateau

    public Piece(TypePiece type, int joueur, int x, int y) {
        this.type = type;
        this.joueur = joueur;
        this.x = x;
        this.y = y;
    }
    public Piece(TypePiece type, int joueur) {
        this.type = type;
        this.joueur = joueur;

    }

    // Getters
    public TypePiece getType() {
        return type;
    }
    public int getJoueur() {
        return joueur;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    { return y; }

    // Setters pour la position
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Vérifie si cette pièce peut capturer une autre pièce
     */
    public boolean peutCapturer(Piece cible, boolean dansPiège) {
        if (cible == null) return false;

        // Règles spéciales
        if (this.type == TypePiece.RAT) {
            // Le Rat peut capturer l'Éléphant
            if (cible.type == TypePiece.ELEPHANT) return true;
            // Le Rat ne peut pas capturer en sortant de l'eau
            if (estDansEau(this.x, this.y)) return false;
        }

        if (this.type == TypePiece.ELEPHANT && cible.type == TypePiece.RAT) {
            return false; // L'Éléphant ne peut pas capturer le Rat
        }

        // Piège : toute pièce ennemie peut être capturée
        if (dansPiège)
            return true;

        // Règle normale : rang supérieur ou égal
        return this.type.getRang() >= cible.type.getRang();
    }

    private boolean estDansEau(int x, int y) {
        // Coordonnées des cases "eau" sur le plateau
        return (x >= 3 && x <= 5 && (y == 1 || y == 2 || y == 4 || y == 5));
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }
}
