// the giant class will generally move toward the walls and
// hug them in a clockwise fashion
import java.awt.*;

public class Giant extends Critter {
   private String appearance;
   private String[] feeFieFoeFum;
   private int steps;

   // constructs a new giant critter
   public Giant() {
      feeFieFoeFum = new String[4];
      String[] giantDisplay = {"fee", "fie", "foe", "fum"};
      for (int i = 0; i < giantDisplay.length; i++) {
         feeFieFoeFum[i] = giantDisplay[i];
      }
      appearance = feeFieFoeFum[0];
   }

   // determines the action of the Giant
   // @param info : methods passed from CritterInfo object
   // @return     : Action the critter will take
   public Action getMove(CritterInfo info) {
      steps++;
      if (steps == 24) {
         steps = 0;
      }
      appearance = feeFieFoeFum[steps / 6];
      if (info.getFront() == Neighbor.OTHER) {
         return Action.INFECT;
      } else if (info.getFront() == Neighbor.EMPTY) {
         return Action.HOP;
      } else {
         return Action.RIGHT;
      }
   }

   // determines the color of the giant
   // @return : the color gray
   public Color getColor() {
      return Color.GRAY;
   }

   // determines the string state of the giant
   // every six turns, changes from "fee" to "fie" to "foe" to "fum", then repeats
   // @return : current string state of the giant
   public String toString() {
      return appearance;
   }
}