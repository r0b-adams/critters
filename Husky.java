// This husky critter will primarily try to avoid threats
// If it determines an enemy is nearby, it will do its best to evade
// (both to take advantage of the immunity that a successful hop gives and
// to build up moveScore). However, if it determines that an enemy to the
// right or left is not facing it, the husky will turn to face it (and subsequently
// try to infect), provided that its threat tests remain false. The husky will also,
// chase the enemy to a limited extent. This can give an advantage in the early game
// while there are still stationary critters, building up numbers by infecting food/flyTraps
// while trying to stay out of harms way. A rudimentary "pack" behavior was implemented
// later such that if a critter encounters another of the same species while moving along a wall,
// it will try to turn back into the open field.

// Initially, the idea was to keep the huskies in the open field (away from the walls)
// to try to take advantage of their threat checks (for example, if one of the neighbors was a
// wall, check the direction of the husky and turn inside after a certain number of moves),
// so as to attack the critters moving around the walls from the flank.

// This was successful some of the time, *if* the huskies gained a numerical advantage early,
// but many times giants or bears won out, as the winner of the bears vs. giants fight
// would tend to dominate by attrition, even though the huskies could survive for some
// time in the middle of the arena.

// The decision was made to make the husky's "non-threatened" behavior resemble that of the
// giant/bear behavior, so that a husky could build up its moveScore and turn inside to chase
// if an enemy presented itself (or if it followed a fellow husky too closely).

// Consequently the critter has both the advantage of boosting the moveScore unhindered while
// still having the numbers to take on other critters along the outside, as well as having superior
// flexibility in the middle of the arena (superior to the default critters, at least).
// Some will hug the borders, while others will sweep east/west and north/south, changing direction
// if a non-threatening enemy wanders by. They now tend to hold their own pretty well.

import java.awt.*;

public class Husky extends Critter {
   private String appearance;
   private Action currentAction;
   private Action[] move = {Action.INFECT, Action.HOP, Action.RIGHT, Action.LEFT};
   private Direction[] facing = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
   private Neighbor[] adjacent = {Neighbor.OTHER, Neighbor.EMPTY, Neighbor.SAME, Neighbor.WALL};
   private boolean enemyRight;
   private boolean enemyNear;
   private boolean imminentThreat;

   // constructs a new husky critter
   public Husky() {
      appearance = "(*_*)";
   }

   // determines the huskies move
   // it will always try to infect if there is an enemy in front
   // otherwise, it will prioritize evasion followed by facing nearby enemies
   public Action getMove(CritterInfo info) {

      // check if an enemy occupies an adjacent square (excluding front)
      enemyNear = enemySighted(info);

      // check if an enemy in an adjacent square if facing this critter
      if (enemyNear) {
         imminentThreat = identifyThreat(info);
      }

      // always try to infect an enemy in front
      if (info.getFront() == adjacent[0]) {
         currentAction = move[0];

      // attempt evasive action if an enemy is facing this critter
      } else if (imminentThreat) {
         currentAction = evasiveManeuvers(info);

      // turn toward an encountered enemy to the left or right
      } else if (enemyNear) {
         currentAction = faceEnemy();

      // determine action if a friendly is encountered
      // and a wall is on one side
      } else if (info.getFront() == adjacent[2]) {
         currentAction = allyNearWall(info);

      // turn right if this critter encounters a wall
      } else if (info.getFront() == adjacent[3]) {
         currentAction = move[2];

      // otherwise, move forward.
      } else {
         currentAction = move[1];
      }

      // updates appearance field
      if (currentAction == move[0]) {
         appearance = "(^o^)";
      } else {
         appearance = "(*_*)";
      }

      // resets threat checks for the next turn
      imminentThreat = false;
      enemyNear = false;
      return currentAction;
   }

   // sets the color of the husky critter
   // @return : color of the husky
   public Color getColor() {
      return Color.YELLOW;
   }

   // sets the string state of the critter
   // @return : a joyful face if its action on the current turn was to infect
   //           otherwise, it is just another starry-eyed zombie
   public String toString() {
      return appearance;
   }

   // will try to get out of the way of an immenent threat if possible
   // @param info : methods passed from CritterInfo object
   // @return     : either hop or turn based on circumstances
   private Action evasiveManeuvers(CritterInfo info) {

      // hop out of the way if possible
      if (info.getFront() == adjacent[1]) {
         return move[1];

      // otherwise try to turn to the enemy
      // this may have the effect of tying up the infecting enemy
      } else {
         return faceEnemy();
      }
   }

   // determines action when a critter of the same species is in front
   // tends to encourage small groups of huskies to turn back into
   // the open field in small packs
   // @return : Action right if a wall is to the left
   //           Action left if a wall is to the right
   private Action allyNearWall(CritterInfo info) {

      // a wall is to the right
      if (info.getRight() == adjacent[3]) {
         return move[3];

      // assumes a wall is to the left
      } else {
         return move[2];
      }

   }

   // If an enemy is sighted, will turn to face them.
   // Testing showed the check to see if an enemy is to the
   // left was unnecessary. It can be assumed under most
   // circumstances that by this point if there is an enemy and
   // it's not to the right, then it must be to the left.
   // This is not always true, of course, however, if the critter
   // is by this point unable to evade successfully, then it
   // is usually lost anyway. At the very least, this may hinder enemy critters
   // @return : turn right if there is an enemy to the right. Otherwise, turn left.
   private Action faceEnemy() {
      if (enemyRight) {
         return move[2];
      } else {
         return move[3];
      }
   }

   // determines if a critter of another species occupies an adjacent square
   // also tests whether there is a threat specifically to the right
   // so that faceEnemy() may take action, if appropriate (excludes front)
   // @param info : methods passed from CritterInfo object
   // @return     : true if an enemy occupies a square to the left, right, or front
   private boolean enemySighted(CritterInfo info) {
      if (info.getRight() == adjacent[0] || info.getLeft() == adjacent[0] ||
                                            info.getBack() == adjacent[0]) {
         if (info.getRight() == adjacent[0]) {
            enemyRight = true;
         } else {
            enemyRight = false;
         }
         return true;
      } else {
         return false;
      }
   }

   // determines if an enemy on any side other than front is facing this critter
   // @param info : methods passed from CritterInfo object
   // @return     : true if an enemy on any side (excluding front) is facing this critter
   private boolean identifyThreat(CritterInfo info) {

      // this critter's direction is the same as that of an enemy behind it
      if (info.getBack() == adjacent[0] && info.getDirection() == info.getBackDirection()) {
         return true;

      // this critter is facing north
      } else if (info.getDirection() == facing[0]) {

         // an enemy to the left is facing east OR an enemy to the right is facing west
         if (info.getLeft() == adjacent[0] && info.getLeftDirection() == facing[2] ||
             info.getRight() == adjacent[0] && info.getRightDirection() == facing[3]) {
            return true;
         }

      // this critter is facing south
      } else if (info.getDirection() == facing[1]) {

         // an enemy to the left is facing west OR an enemy to the right is facing east
         if (info.getLeft() == adjacent[0] && info.getLeftDirection() == facing[3] ||
             info.getRight() == adjacent[0] && info.getRightDirection() == facing[2]) {
            return true;
         }

      // this critter is facing east
      } else if (info.getDirection() == facing[2]) {

         // an enemy to the left is facing south OR an enemy to the right is facing north
         if (info.getLeft() == adjacent[0] && info.getLeftDirection() == facing[1] ||
             info.getRight() == adjacent[0] && info.getRightDirection() == facing[0]) {
            return true;
         }

      // this critter is facing west
      } else if (info.getDirection() == facing[3]) {

         // an enemy to the left is facing north OR an enemy to the right is facing south
         if (info.getLeft() == adjacent[0] && info.getLeftDirection() == facing[0] ||
             info.getRight() == adjacent[0] &&info.getRightDirection() == facing[1]) {
            return true;
         }
      }

      // otherwise, there is an enemy in an adjacent square, but it is not facing this critter
      // therefore, it's not a threat on this turn
      return false;
   }
}