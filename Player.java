package mini_jeu;


class Player {
    private String name;
    private String pseudo;
    private int live_P; 
    private int defense_p;
    private int attack_p;
    boolean fin = false;
    

// le contructeur par defaut
    public Player(){
        name= "unknown";
        pseudo= "unknown";
        live_P = 0;
        defense_p = 0;
        attack_p = 0; 
    }
    public Player(String name, String pseudo, int live_P,int defense_p,int attack_p){
        this.name= name;
        this.pseudo= pseudo;
        this.live_P = live_P;
        this.defense_p = defense_p;
        this.attack_p = attack_p;



    }
// les getters
    String Getname(){
        return name;
    }
    String Getpseudo(){
        return pseudo;
    }
    int Getlive_p(){
        return live_P;
    }
    int Getdefense_p(){
        return defense_p;
    }
    int Getattack_p(){
        return attack_p;
    }
// les setters
    public void Setname(String n){
        this.name = n;
    }
    public void Setpseudo(String pn){
        this.pseudo = pn;  
    }
    public void Setlive_p(int lp){
        this.live_P = lp;
    }
    public void SetDefense_p(int dp) {
        this.defense_p = dp;
    }
    public void Setattack_p (int at) {
        this.attack_p = at;
    }
    //les methodes de manpulations et autres 
    // penser a une methode pour les etats score, point, manche
    public void PresentYou(){
        System.out.println("hello ! my name is "+this.Getname()+ " my pseudo in this game will be: "+this.Getpseudo());
        System.out.println("my remaining point is "+this.Getlive_p());
        System.out.println("Defense: =>"+this.Getdefense_p()+"<= Attack: => "+this.Getattack_p()+" <=");
    }
    public void attaque (int impact,Player p){
        if (impact <= this.attack_p){
            int a = p.Getlive_p();
            int b = this.attack_p;
            int c = p.Getdefense_p();
            int d =p.Getattack_p(); 
            if (impact >= 2)
                p.Setlive_p(a - 1);
            this.Setattack_p(b - impact);
            p.SetDefense_p(c- impact);
            p.Setattack_p(d-1);
            System.out.println("attaque de "+this.pseudo+" vers "+p.pseudo);



        }else{
            System.out.println("impossible de lancer cette attaque !!! "+this.pseudo);
        }

    }
    public void defense (int contre_impact, Player p){
        if (this.defense_p >= contre_impact){

        
        if ( contre_impact >= 2)
            this.live_P++;
        this.defense_p -= contre_impact;
        this.attack_p++;
        System.out.println("blocage de "+this.pseudo);
    }else{
        System.out.println("impossible de contrer !!!!"+this.pseudo);
    }

    }
    

    
    public void score(Player p1, Player p2){
        if (p1.attack_p < 0 ) p1.Setattack_p(0);
        if (p1.defense_p < 0 ) p1.SetDefense_p(0);
        if (p1.live_P < 0 ) p1.Setlive_p(0);
        if (p2.attack_p < 0 ) p2.Setattack_p(0);
        if (p2.defense_p < 0 ) p2.SetDefense_p(0);
        if (p2.live_P < 0 ) p2.Setlive_p(0);        

    System.out.println("--------------------- score ----------------------------");
    System.out.println("           "+p1.pseudo+"                 "+p2.pseudo+"    ");
    System.out.println("      vie: "+p1.live_P+"            vie: "+p2.live_P+"    ");
    System.out.println("  attaque: "+p1.attack_p+"        attaque: "+p2.attack_p+"    ");
    System.out.println("  defense: "+p1.defense_p+"        defense: "+p2.defense_p+"    ");
    p1.Game_Over(p1, p2);
    }
    public void Game_Over(Player p1, Player p2){
        System.out.print("\033[H\033[2J");  
        System.out.flush();   
    
        if (p1.live_P == 0 || p2.live_P == 0){
            fin = true;
            System.out.println("+++++++++partie terminee++++++++++++++++++++++++++++++");
                 
     string art = """
     
     ░▒▓██████▓▒░ ░▒▓██████▓▒░░▒▓██████████████▓▒░░▒▓████████▓▒░       ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓████████▓▒░▒▓███████▓▒░  
     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░             ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░ 
     ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░             ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒▒▓█▓▒░░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░ 
     ░▒▓█▓▒▒▓███▓▒░▒▓████████▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓██████▓▒░        ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒▒▓█▓▒░░▒▓██████▓▒░ ░▒▓███████▓▒░  
     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░             ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▓█▓▒░ ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░ 
     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░             ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▓█▓▒░ ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░ 
      ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓████████▓▒░       ░▒▓██████▓▒░   ░▒▓██▓▒░  ░▒▓████████▓▒░▒▓█▓▒░░▒▓█▓▒░ 
                                                                                                                             
                                                                                                                             
             
    
    
                 """;
            
                System.out.println(art);
    
            if (p1.live_P > p2.live_P){
                System.out.println("victoire de "+p1.pseudo);
            }else{
                System.out.println("victoire de "+p2.pseudo);
            }


        }
    }
    public void Stating_game(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();   
        System.out.println("__________________________________________________________________________________");
        System.out.println("__________________________________________________________________________________");
        System.out.println("+++++++++++++++++++++++++++++++++++++ fighter console +++++++++++++++++++++");
        System.out.println("");
        System.out.println("                 [ 1 ] jouer contre ordinateur                                            ");
        System.out.println("                 [ 2 ] jouer a 2                                           ");
        System.out.println("                 [ 3 ] consulter les regles                                            ");
        System.out.println("                                                                                      ");
        System.out.println("                                                                                      ");
        System.out.println("                                                                                      ");
        System.out.println(" appuyer sur l'une des touches 1,2 ou 3 pour valider une option...............            ");
        System.out.println("__________________________________________________________________________________");
        
    }
    public void Affrontement(player p1, player p2){
int cpt_tour = 1;
boolean fin = false;
//creation des joueurs

  if (cpt_tour%2 == 0){
    //joueur 1
    cpt_tour++;
  }else{
    //joueur 2
    cpt_tour++;
  }
while (fin = false){
    //jouer
}

    }

}
