// This bear critter will generally move toward
// the walls and hug them in a counterclockwise fashion
import java.awt.*;

public class Bear extends Critter {
   private String appearance;
   private boolean isWhite;
   private int steps;

   // constructs a new bear critter
   // @param polar : dependent on a test run by the CritterModel class
   public Bear(boolean polar) {
      isWhite = polar;
      appearance = "/";
   }

   // determines the action taken by the bear
   // @param info : methods passed from CritterInfo object
   // @return     : Action the critter will take
   public Action getMove(CritterInfo info) {
      steps++;
      if (steps % 2 == 0) {
         appearance = "/";
      } else {
         appearance = "\\";
      }
      if (info.getFront() == Neighbor.OTHER) {
         return Action.INFECT;
      } else if (info.getFront() == Neighbor.EMPTY) {
         return Action.HOP;
      } else {
         return Action.LEFT;
      }
   }

   // determines the color of the bear
   // @return : white or black depending on initialization in constructor
   public Color getColor() {
      if (isWhite) {
         return Color.WHITE;
      } else {
         return Color.BLACK;
      }
   }

   // determines the string state of the bear critter
   // alternates between "/" and "\" every turn
   // @return : current string state of critter
   public String toString() {
      return appearance;
   }
}