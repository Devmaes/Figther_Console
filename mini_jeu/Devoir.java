package mini_jeu;
import java.util.Scanner;

public class Devoir {
    
    public static void main(String[] args) { 
      @SuppressWarnings("resource")
      Scanner sc = new Scanner(System.in);
      int  entre ;
            System.out.println();
            System.out.println();

            
            Player p1 = new Player("maes","maes88", 3,7 , 15);
            Player p2 = new Player("mayem","arkemis4", 3,7, 15);
          
      p1.Stating_game();
      System.out.println();
      System.out.println();
      entre = sc.nextInt();
      while (entre != 1 & entre != 2 & entre != 3) {
        System.out.println("choix non disponible !!!");
        entre = sc.nextInt();
      }

      switch (entre) {
        case 1:
          System.out.println("===> 1");
          break;
        case 2:
          System.out.println("===> 2");
          break;
        case 3:
          System.out.println("===> 3");
          p1.regles_du_jeu();  
          break;
      
        
         
      }
      
      // creer un systeme de switch case pour faire le choix ici
      // contre ordinateur sera un random juste des possibilite





    } 
}

