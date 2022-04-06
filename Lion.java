// this lion critter will generally move north to south and east to west
// they randomly choose a color (red, blue, or green) every three turns
import java.awt.*;
import java.util.*;

public class Lion extends Critter {
   private String appearance;
   private Random pickColor;
   private int steps;
   private int color;

   // constructs a new Lion critter
   public Lion() {
      appearance = "L";
      pickColor = new Random();
      color = pickColor.nextInt(3);
   }

   // determines the action taken by the lion critter
   // @param info : methods passed from CritterInfo object
   // @return     : Action the critter will take
   public Action getMove(CritterInfo info) {
      steps++;
      if (steps % 3 == 0) {
         color = pickColor.nextInt(3);
      }
      if (info.getFront() == Neighbor.OTHER) {
         return Action.INFECT;
      } else if (info.getFront() == Neighbor.WALL || info.getRight() == Neighbor.WALL) {
         return Action.LEFT;
      } else if (info.getFront() == Neighbor.SAME) {
         return Action.RIGHT;
      } else {
         return Action.HOP;
      }
   }

   // determines the color of the lion critter
   // every three turns, randomly chooses between red, blue, or green
   // and keeps that color for three turns, then randomly chooses again
   // @return : color of the lion critter
   public Color getColor() {
      if (color == 0) {
         return Color.RED;
      } else if (color == 1) {
         return Color.GREEN;
      } else {
         return Color.BLUE;
      }
   }

   // determines the string state of the lion critter
   // @return : string state of the lion critter
   public String toString() {
      return appearance;
   }
}