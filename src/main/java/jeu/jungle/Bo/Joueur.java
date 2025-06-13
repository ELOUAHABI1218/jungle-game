package jeu.jungle.Bo;


public class Joueur {
    private int id;
    private String nomUtilisateur;
    private String motDePasse;
    private int victoires;
    private int defaites;
    private int egalites;

    // Constructeur
    public Joueur(int id, String nomUtilisateur, String motDePasse,
                  int victoires, int defaites, int egalites) {
        this.id = id;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.victoires = victoires;
        this.defaites = defaites;
        this.egalites = egalites;
    }
    public Joueur(String nomUtilisateur){
        this.nomUtilisateur=nomUtilisateur;
    }

    // Getters
    public int getId() {
        return id; }
    public String getNomUtilisateur() {
        return nomUtilisateur;
    }
    public String getMotDePasse() { return motDePasse; }
    public int getVictoires() { return victoires; }
    public int getDefaites() { return defaites; }
    public int getEgalites() { return egalites; }

    // Méthodes pour mettre à jour les stats
    public void incrementerVictoires() { victoires++; }
    public void incrementerDefaites() { defaites++; }
    public void incrementerEgalites() { egalites++; }
}