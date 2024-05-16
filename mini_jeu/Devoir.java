package mini_jeu;
import java.util.Scanner;

public class Devoir {
    
         public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int m;
            String pseudo_j; 
            System.out.println();
            System.out.println();

            
            Player p = new Player("maes","maes88", 3,7 , 15);
            Player p1 = new Player("maes","mas88", 0,7 , 15);
            Player p2 = new Player("maes","mae8", 3,7 , 15);
            
            m = Player.Starting_game();
            System.out.println("vous avez choisi "+m);
      

      switch (m) {
        case 1: 
          System.out.println("===> 1");
          p.Game_Over(p1, p2);
          System.out.print("joueur  entrer votre Pseudo: =>     ");
          pseudo_j= sc.nextLine();
          p.Setpseudo(pseudo_j);
          p.Contr_ordinateur(p);
          break;
        case 2:
          System.out.println("contre 2");
          p.contre_deux(p1,p2);
          break;
        case 3:
          System.out.println("===> 3");
          p.regles_du_jeu();  
          break;
        
        
         
      }
      System.out.println("vous avez choisi "+(m+1));
      // revoir le scenario du jeu
      // revisiter la fonction defense
      //voir la fonction game_over


// 


    } 
}

