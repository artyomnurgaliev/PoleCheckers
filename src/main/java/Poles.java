import game_logic_exceptions.BusyCellException;
import game_logic_exceptions.GameLogicException;
import game_logic_exceptions.InvalidMoveException;

import java.util.*;

public class Poles {
    // Storage of poles with hash calculated from its positions
    private final HashMap<Integer, Pole> poles = new HashMap<>();

    /**
     * Returns hashmap of poles
     *
     * @return poles
     */
    HashMap<Integer, Pole> getPoles() {
        return poles;
    }

    /**
     * Print representation of poles
     */
    public void printPoles() {
        SortedSet<Integer> keys = new TreeSet<>(poles.keySet());
        StringBuilder stringBuilder = new StringBuilder(1000);
        for (int key : keys) {
            if (poles.get(key).getColor() == Color.WHITE) {
                stringBuilder.append(poles.get(key).toString());
                stringBuilder.append(" ");
            }
        }
        System.out.println(stringBuilder.toString());
        stringBuilder = new StringBuilder(1000);
        for (int key : keys) {
            if (poles.get(key).getColor() == Color.BLACK) {
                stringBuilder.append(poles.get(key).toString());
                stringBuilder.append(" ");
            }
        }
        System.out.println(stringBuilder.toString());
    }

    /**
     * Adds new pole on checkerboard
     *
     * @param pole_str - string representation of a pole
     * @throws GameLogicException (if smth against the rules)
     */
    public void addPole(String pole_str) throws GameLogicException {
        Pole pole = new Pole(pole_str);
        poles.put(Pole.pos(pole), pole);
    }

    /**
     * Check whether position of a pole on checkerboard is not empty
     *
     * @param new_pole - pole under consideration
     * @return true if not empty
     */
    boolean checkBusyCell(Pole new_pole) {
        return checkBusyCell(Pole.pos(new_pole));
    }

    /**
     * Check whether this position on checkerboard is not empty
     *
     * @param pos - pos of a position on checkerboard (pos: x + 10 * y)
     * @return true if not empty
     */
    boolean checkBusyCell(int pos) {
        if (!Pole.checkCorrectPos(pos)) {
            return true;
        }
        return poles.containsKey(pos);
    }

    /**
     * Check whether some pole of this color can cut
     *
     * @param color - color
     * @param cut_poles - hash set of cut poles
     * @return true if can cut
     */
    boolean isCanCut(Color color, HashSet<Integer> cut_poles) {
        for (int key : poles.keySet()) {
            if (poles.get(key).getColor() == color && isCanCut(poles.get(key), cut_poles)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether pole can cut another pole from this position
     *
     * @param prev_pole  - pole under consideration
     * @param cut_poles- poles cut during previous moves on this stage
     * @return true if can cut
     */
    boolean isCanCut(Pole prev_pole, HashSet<Integer> cut_poles) {
        int lower_left_hash = Pole.pos(new Pole(-1, -1));
        int upper_left_hash = Pole.pos(new Pole(-1, 1));
        int lower_right_hash = Pole.pos(new Pole(1, -1));
        int upper_right_hash = Pole.pos(new Pole(1, 1));
        int max_distance = 1;
        if (prev_pole.isKing()) {
            max_distance = 7;
        }
        int[] directions = {lower_left_hash, upper_left_hash, lower_right_hash, upper_right_hash};
        for (int move : directions) {
            for (int i = 1; i <= max_distance; ++i) {
                int cur_hash = Pole.pos(prev_pole) + move * i;
                if (poles.containsKey(cur_hash)) {
                    if (poles.get(cur_hash).getColor() == prev_pole.getColor()) {
                        break;
                    } else {
                        if (checkBusyCell(cur_hash + move)) {
                            break;
                        } else {
                            if (cut_poles.contains(cur_hash)) {
                                break;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check whether cut of pole is in accordance with rules
     * If cut is normal remove top checker from cut pole
     *
     * @param prev_pole - pole on previous position
     * @param new_pole  - pole on new position
     * @param cut_poles - poles cut during previous moves on this stage
     * @return true if cut is correct
     * @throws GameLogicException (if the moves are against the rules)
     */
    boolean normalCut(Pole prev_pole, Pole new_pole, HashSet<Integer> cut_poles) throws GameLogicException {
        if (new_pole.getLastCutChecker().getColor() != new_pole.getColor()) {
            prev_pole.addChecker(new_pole.getLastCutChecker());
            if (new_pole.isSameKindCheckers(prev_pole)) {
                if (new_pole.isOnSameDiagonal(prev_pole)) {
                    int direction = prev_pole.direction(new_pole);

                    // Calc max distance depending on isKing
                    int max_distance = 1;
                    if (prev_pole.isKing()) {
                        max_distance = 7;
                    }
                    for (int i = 1; i <= max_distance; ++i) {
                        int cur_hash = Pole.pos(prev_pole) + direction * i;
                        if (poles.containsKey(cur_hash)) {
                            // Check that cut pole color is not the same as new pole color
                            if (poles.get(cur_hash).getColor() == prev_pole.getColor()) {
                                return false;
                            } else {
                                int offset = Math.min(max_distance, poles.get(cur_hash).getDistance(new_pole) - 1);
                                // Check that there are no poles between cut pole and new pole
                                for (int j = 1; j < offset; ++j) {
                                    if (!checkBusyCell(cur_hash + direction * j)) {
                                        return false;
                                    }
                                }
                                // Check if we try to cut the same pole twice
                                if (cut_poles.contains(cur_hash)) {
                                    return false;
                                }

                                // New pole without top checker in the game
                                poles.get(cur_hash).removeChecker();
                                if (poles.get(cur_hash).isEmpty()) {
                                    poles.remove(cur_hash);
                                } else {
                                    cut_poles.add(cur_hash);
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    void SetKing(Pole new_pole, Pole prev_pole) {
        if (new_pole.getColor() == Color.WHITE && new_pole.getY() == 8) {
            new_pole.setKing();
        }
        if (new_pole.getColor() == Color.BLACK && new_pole.getY() == 1) {
            new_pole.setKing();
        }
        if (prev_pole.isKing()) {
            new_pole.setKing();
        }
    }

    /**
     * Checking if move is possible
     * if possible, then process move
     * Check if new_pole can cut
     *
     * @param prev_pole - pole on previous position
     * @param new_pole  - pole on new position
     * @param cut_poles - poles, cut during previous moves on this stage
     * @return true if new_pole can cut
     * @throws GameLogicException (if the moves are against the rules)
     */
    boolean checkNewPoleAndDoMove(Pole prev_pole, Pole new_pole, HashSet<Integer> cut_poles) throws GameLogicException {
        if (!poles.containsKey(Pole.pos(prev_pole))) {
             throw new GameLogicException("error");
        }
        prev_pole = poles.get(Pole.pos(prev_pole));
        SetKing(new_pole, prev_pole);
        if (checkBusyCell(new_pole)) {
            throw new BusyCellException("busy cell");
        }
        if (isCanCut(prev_pole.getColor(), cut_poles)) {
            if (!normalCut(prev_pole, new_pole, cut_poles)) {
                throw new InvalidMoveException("invalid move");
            } else {
                poles.remove(Pole.pos(prev_pole));
                poles.put(Pole.pos(new_pole), new_pole);
                return isCanCut(new_pole, cut_poles);
            }
        } else {
            if (!new_pole.isSameKindCheckers(prev_pole)) {
                throw new GameLogicException("error");
            }
            if (!new_pole.isOnSameDiagonal(prev_pole)) {
                throw new GameLogicException("error");
            }
            int max_distance = 1;
            if (new_pole.isKing()) {
                max_distance = 7;
            }
            if (new_pole.getDistance(prev_pole) > max_distance) {
                throw new GameLogicException("error");
            } else {
                poles.remove(Pole.pos(prev_pole));
                poles.put(Pole.pos(new_pole), new_pole);
            }
        }
        return false;
    }

    /**
     * Perform one player's turn
     *
     * @param moves - moves in one player turn
     * @throws GameLogicException (if the moves are against the rules)
     */
    public void move(String moves) throws GameLogicException {
        StringTokenizer st = new StringTokenizer(moves, ":");
        if (st.countTokens() == 1) {
            st = new StringTokenizer(moves, "-");
        }
        HashSet<Integer> cut_poles = new HashSet<>();

        Pole prev_pole = new Pole(st.nextToken());
        while (st.hasMoreTokens()) {
            Pole new_pole = new Pole(st.nextToken());
            if (checkNewPoleAndDoMove(prev_pole, new_pole, cut_poles) && (!st.hasMoreTokens())) {
                throw new InvalidMoveException("invalid move");
            }
            prev_pole = new_pole;
        }
    }
}
