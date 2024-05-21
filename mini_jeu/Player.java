package mini_jeu;
import java.util.Scanner;
import java.util.Random;

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
        if (impact <= this.attack_p && impact > 0){
            int a = p.Getlive_p();
            int b = this.attack_p;
            int c = p.Getdefense_p();
            int d =p.Getattack_p(); 
            if (impact >= 4||(p.attack_p == 0 & p.defense_p == 0 & p.live_P > 0)){
                p.Setlive_p(a - 1);
            }    
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

        
        if ( contre_impact >= 5)
            this.live_P++;
        this.defense_p -= contre_impact;
        this.attack_p++;
        System.out.println("blocage de "+this.pseudo);
    }else{
        System.out.println("impossible de contrer !!!!"+this.pseudo);
    }

    }
// ----------------------------------------------------------------------------------------------------------------------    

    
    public boolean score(Player p1, Player p2){
        if (p1.attack_p < 0 ) p1.Setattack_p(0);
        if (p1.defense_p < 0 ) p1.SetDefense_p(0);
        if (p1.live_P < 0 ) p1.Setlive_p(0);
        if (p2.attack_p < 0 ) p2.Setattack_p(0);
        if (p2.defense_p < 0 ) p2.SetDefense_p(0);
        if (p2.live_P < 0 ) p2.Setlive_p(0);        

    System.out.println("--------------------- score ----------------------------");
    System.out.println(p1.pseudo+" contre  "+p2.pseudo);
    System.out.println("           "+p1.pseudo+"                 "+p2.pseudo+"    ");
    System.out.println("      vie: "+p1.live_P+"            vie: "+p2.live_P+"    ");
    System.out.println("  attaque: "+p1.attack_p+"        attaque: "+p2.attack_p+"    ");
    System.out.println("  defense: "+p1.defense_p+"        defense: "+p2.defense_p+"    ");
    // fonction de state de game over
    return p1.Game_Over(p1, p2);
    }
// -----------------------------------------------------------------------------------------------------------------------------    
    public boolean Game_Over(Player p1, Player p2){
       
        boolean fin=false;
        System.out.println("entree");   
    
        if (p1.live_P == 0 || p2.live_P == 0){
            fin = true;
            System.out.print("\033[H\033[2J");  
            System.out.flush();
            System.out.println("++++++++++++++++++++++++ la partie est terminee ++++++++++++++++++++++++++++++");
                 
     String art = """
     
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
                System.out.println("victoire de                                   ++++>  "+p1.pseudo+" <++++       ");
            }else{
                System.out.println("victoire de                                   ++++>  "+p1.pseudo+" <++++       ");
            }


        }
        System.out.println("sortie");
        return fin;
    }
// -----------------------------------------------------------------------------------------------------------------
    public static int Starting_game(){
        Scanner sc = new Scanner(System.in);
        int entre;
        
        System.out.print("\033[H\033[2J");  
        System.out.flush();   
        System.out.println("__________________________________________________________________________________");
        System.out.println("__________________________________________________________________________________");
        System.out.println("+++++++++++++++++++++++++++++++++++++ fighter console ++++++++++++++++++++++++++++");
        System.out.println("");
        System.out.println("                 [ 1 ] jouer contre ordinateur                                            ");
        System.out.println("                 [ 2 ] jouer a 2                                           ");
        System.out.println("                 [ 3 ] consulter les regles                                            ");
        System.out.println("                                                                                      ");
        System.out.println("                                                                                      ");
        System.out.println("                                                                                      ");
        System.out.println(" appuyer sur l'une des touches 1,2 ou 3 pour valider une option...............            ");
        System.out.println("__________________________________________________________________________________");
        System.out.println();
        System.out.println();
        entre = sc.nextInt();
        while (entre != 1 & entre != 2 & entre != 3) {
        System.out.println("choix non disponible !!!");
        entre = sc.nextInt();
        }
        return entre;
    }
// -------------------------------------------------------------------------------------------------
   

    

public void regles_du_jeu (){
    System.out.print("\033[H\033[2J");  
        System.out.flush();
        Player p1 = new Player(); 
        Scanner sc = new Scanner(System.in);
        int entre,m;  
        System.out.println("__________________________________________________________________________________");
        System.out.println("__________________________________________________________________________________");
        System.out.println("+++++++++++++++++++++++++++++++++++++ regle du jeu ++++++++++++++++++++++++++++");
        System.out.println("             -----------------> fighter console <------------------");
        System.out.println();
        System.out.println();
        System.out.println("    Il s'agit de jeu de simulation d'un combat entre 2 player, chacun player a son tour de jeu");
        System.out.println("et a chaque tour de jeu , le player qui a la main a soit le choix d'attaquer ou de ne rien faire");
        System.out.println("par contre des qu'un jouer choisit d'attaquer, son adversaire a le choix soit de contre-attaquer ");
        System.out.println("soit de contrer l'attaque. les attaques de 2 points et plus coutent 1 point de vie au recepteur de");
        System.out.println("de l'attaque. les attaques reduisent les points de vie de l'adversaire, et quelques points d'attaque");
        System.out.println("de l'auteur de l'attaque. le combat se termine lorsqu'un des 2 players n'a plus de point de vie soit 0");
        System.out.println("celui possedant le plus grand nombre de point de vie est declare vainqueur.");
        System.out.println("");
        System.out.println("BONNE CHANCE ");
        System.out.println("Produced by DevMaes may 2K24");
        System.out.println("");
        System.out.println("appuyer sur [1] pour retourner au menu principal");
        entre = sc.nextInt();
      while (entre != 1 ) {
        System.out.println("choix non disponible !!!");
        entre = sc.nextInt();
      }
      m = Player.Starting_game();
      
      switch (m) {
        case 1:
          System.out.println("===> 1");
        //   p1.Affrontement();
          break;
        case 2:
          System.out.println("===> 2");
          break;
        case 3:
          System.out.println("===> 3");
          p1.regles_du_jeu();  
          break;
      
        
         
      }
    }
//   -----------------------------------------------------------------------------------------------------------      


public void contre_deux(Player p1,Player p2){
Scanner sc = new Scanner(System.in);    
String pseudo1 , pseudo2;
int cpt_tour = 1;
int coup1,coup2,action =3;
boolean fin = false;

System.out.print("\033[H\033[2J");  
System.out.flush();

System.out.println("__________________________________ contre un autre joueur ____________________________________");
System.out.println();
System.out.println();
System.out.print("joueur 1 entrer votre Pseudo: =>     ");
pseudo1 = sc.nextLine();


System.out.println();
System.out.println();
System.out.print("joueur 2 entrer votre Pseudo: =>     ");
pseudo2 = sc.nextLine();
p1.Setpseudo(pseudo1);
p2.Setpseudo(pseudo2);

System.out.print("\033[H\033[2J");  
System.out.flush();

fin = p1.score(p1, p2);
System.out.println();
System.out.println();
System.out.println();



while(fin != true){
    if (cpt_tour % 3 == 0){
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        fin = p1.score(p1, p2);
    }
    System.out.print("{ "+p1.pseudo+" } [1]: attaquer    ||  [2]: defendre  || [3] rien faire ==> ");
    action = sc.nextInt();
    while(action == 2 & cpt_tour == 1){
        System.out.println("Ne peut defendre car aucune attaque en cours !!!!");
        System.out.print("{ "+p1.pseudo+" } [1]: attaquer    ||  [2]: defendre  || [3] rien faire ==> ");
    action = sc.nextInt();
    }    
    if(cpt_tour == 1 || cpt_tour%2 != 0){

        if(action == 1) {
            System.out.println(p1.pseudo+" attaque a coup de: ");
            coup1= sc.nextInt();
            p1.attaque(coup1, p2);
            fin = p1.score(p1, p2);   
        }    
        if(action == 2) {
            System.out.println(p1.pseudo+" defense a coup de: ");
            coup1= sc.nextInt();
            p1.defense(coup1, p2);
            fin = p1.score(p1, p2);   
        }
    
        if(action == 3) System.out.println("RAS");
        
        cpt_tour++;
        System.out.println("cpt_tour: "+cpt_tour);
    }
    if (fin != true){
    if(cpt_tour%2 == 0){
        System.out.print("{ "+p2.pseudo+" } [1]: attaquer    ||  [2]: defendre  || [3] rien faire ==> ");
        action = sc.nextInt();

        if(action == 1) {
            System.out.println(p2.pseudo+" attaque a coup de: ");
            coup2= sc.nextInt();
            p2.attaque(coup2, p1);
            fin = p2.score(p1, p2);   
        }    
        if(action == 2) {
            System.out.println(p2.pseudo+" defense a coup de: ");
            coup2= sc.nextInt();
            p2.defense(coup2, p1);
            fin = p2.score(p1, p2);   
        }
        cpt_tour++;
        System.out.println("cpt_tour: "+cpt_tour);
    }
}

}
}

// ----------------------------------------------------------------------------------------------------------------
public void  Contr_ordinateur(Player p){
  Scanner sc = new Scanner(System.in); 
  int rep;
  int cpt_tour = 1;
  int coup1,coup2,action =3;
  boolean fin=false;

    
  
System.out.print("\033[H\033[2J");  
System.out.flush();

System.out.println("__________________________________ contre l'ordinateur  ____________________________________");
System.out.println();
System.out.println();
 
  System.out.println("     veillez choisir votre adversaire      ");
  System.out.println();
  System.out.println();
  System.out.println("  [1] RODRIGO");
  System.out.println("  [2] JOSELU");
  System.out.println("  [3] BELLIGHAM");
  System.out.println("  [4] CAMAVINGA");
  System.out.println("  [5] VINICIUS");
  System.out.println(); 
  //creation des adversaires  
 Player  robot1 = new Player("robot1","Rodrigo", 3,7 , 15);;
 Player  robot2 = new Player("robot2","Jeselu", 3,7 , 15);
 Player  robot3 = new Player("robot3","Belligham", 3,7 , 15);
 Player  robot4 = new Player("robot4","Camavinga", 3,7 , 15);
 Player  robot5 = new Player("robot5","Vinicius", 3,7 , 15);
 int[] tab = new int[3];
 Player[] robot = new Player[5];
 robot[0]= robot1;
 robot[1]= robot2;
 robot[2]= robot3;
 robot[3]= robot4;
 robot[4]= robot5;

 for (int i=0; i<3;i++){
   tab[i] = (int)(Math.random() * 3);
 }
 rep = sc.nextInt();
 while (rep != 1 & rep != 2 & rep != 3 & rep != 4 & rep != 5 ) {
    System.out.println("choix non disponible !!!");
    rep = sc.nextInt();
   
  }

   rep--;
   if(rep == 0  || rep == 1 || rep == 3 || rep == 2 ){
    robot[rep].Setattack_p(robot[rep].attack_p  + tab[2]+ 2);
    robot[rep].SetDefense_p(robot[rep].attack_p  + tab[1]);
    robot[rep].live_P++;
   }
   if(rep == 2 || rep == 4){
    robot[rep].SetDefense_p(robot[rep].attack_p  + tab[1]+ 4);
    robot[rep].live_P++;
   }

     
   System.out.println();
   fin = p.score(robot[rep], p);
   System.out.println();
   System.out.println();
   System.out.println();
   //combat
while(fin != true){
        
   

    //lancer directement le combat
    System.out.print("{ "+p.pseudo+" } [1]: attaquer    ||  [2]: defendre  || [3] rien faire ==> ");
    action = sc.nextInt();
    while(action == 2 & cpt_tour == 1){
        System.out.println("Ne peut defendre car aucune attaque en cours !!!!");
        System.out.print("{ "+p.pseudo+" } [1]: attaquer    ||  [2]: defendre  || [3] rien faire ==> ");
        action = sc.nextInt();
    }
    if(cpt_tour == 1 || cpt_tour%2 != 0){

        if(action == 1) {
            System.out.println(p.pseudo+" attaque a coup de: ");
            coup1= sc.nextInt();
            p.attaque(coup1, robot[rep]);
            // fin = p.score(p, robot[rep]);   
        }    
        if(action == 2) {
            System.out.println(p.pseudo+" defense a coup de: ");
            coup1= sc.nextInt();
            p.defense(coup1, robot[rep]);
            // fin = p.score(p, robot[rep]);   
        }

        if(action == 3) {
            System.out.println("RAS");
            // fin = p.score(p, robot[rep]);
        }
        
        cpt_tour++;
        System.out.println("cpt_tour: "+cpt_tour);
        action = (int) (Math.random()*3) + 1;
        fin = p.score(p, robot[rep]);
    }

//tour de la machine
if(fin != true){
    if (cpt_tour%2 == 0){
    action = ordi_action(robot[rep], p, 3, cpt_tour);    
    System.out.println("{ "+robot[rep].Getpseudo()+" } [1]: attaquer    ||  [2]: defendre  || [3] rien faire ==> "+action);

    if(action == 1) {
        coup2 = ordi_coup(robot[rep], p, action, cpt_tour);
        robot[rep].attaque(coup2, p);
        System.out.println(robot[rep].Getpseudo()+" attaque a coup de: "+coup2);
        
        // p.score(p, robot[rep]);   
    }  

    if(action == 2) {
        coup2 = ordi_coup(robot[rep], p, action, cpt_tour);
        robot[rep].defense(coup2, p);
        System.out.println(robot[rep].Getpseudo()+" defense a coup de: "+coup2);

        // p.score(p, robot[rep]);   
    }

    if(action == 3) {
        System.out.println("RAS");
        // 
    }

    cpt_tour++;
    fin = p.score(p, robot[rep]);
    System.out.println("cpt_tour: "+cpt_tour);


    }
}
    if (cpt_tour%3 == 0 ) {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        fin = p.score(p, robot[rep]);
    }
    }
 // pour augmenter les points, tout  dependra de l'adversaire choisit par le challenger
 // les adversaires utilise un meme algo de reflection
 // l'objectif crucial de cette section sera d'ecrire l'algo de reflection de l'ordi
 // en realite l'ordinateur l'odinateur choisit des coups qui lui feront garder le plus de points possibles
 // gardons aussi l'eventualite qu'il peut bloquer si necessaire les coups de son adversaire 
}

// ------------------------------------------------------------------------------------------------------------------------------------------------
public int  ordi_action(Player ordi, Player p,int action, int cpt_tour){
 

    if(p.live_P <= 1|| p.attack_p <= 1  ){
        
            action = 1;
      
    }else{
        action = (int) (Math.random()*3) + 1;
    }
    if(ordi.attack_p<4&ordi.attack_p>=0){
        action = 2;
    }
    
    return action ;
}
// ----------------------------------------------------------------------------------------------------------------------------------------------------
public int ordi_coup(Player ordi, Player p,int action, int cpt_tour){
    int coup = 0;
    if(action == 1) coup = (int)((Math.random() * ordi.attack_p )+ 1);
    if(action == 2) coup = (int)((Math.random() * ordi.defense_p)+ 1); 
    
    if (p.live_P <= 1) {
        coup = (int)((Math.random() * p.attack_p )+ 4);
    }else{
        if(ordi.attack_p <= 4 & ordi.defense_p == 0  ) coup = 0; 
    }
    
    System.out.println("++++++++++++++++++++++>>>>>>>>> "+coup);
    return coup; 
}

} 
