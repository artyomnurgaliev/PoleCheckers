import java.util.*;

class Poles {
    // Storage of poles with hash calculated from its positions
    private final HashMap<Integer, Pole> poles = new HashMap<>();

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
     * @param pole_str - string representation of a pole
     * @throws GameLogicException (if smth against the rules)
     */
    public void addPole(String pole_str) throws GameLogicException {
        Pole pole = new Pole(pole_str);
        poles.put(Pole.hash(pole), pole);
    }

    /**
     * Check whether position of a pole on checkerboard is not empty
     * @param new_pole - pole under consideration
     * @return true if not empty
     */
    public boolean checkBusyCell(Pole new_pole) {
        return poles.containsKey(Pole.hash(new_pole));
    }

    /**
     * Check whether this position on checkerboard is not empty
     * @param hash - hash of a position on checkerboard (hash: x + 10 * y)
     * @return true if not empty
     */
    boolean checkBusyCell(int hash) {
        return poles.containsKey(hash);
    }

    /**
     * Checks whether pole can cut another pole from this position
     * @param prev_pole - pole under consideration
     * @param cut_poles- poles cut during previous moves on this stage
     * @return true if can cut
     */
    public boolean isCanCut(Pole prev_pole, HashSet<Integer> cut_poles) {
        int lower_left_hash = Pole.hash(new Pole(-1, -1));
        int upper_left_hash = Pole.hash(new Pole(-1, 1));
        int lower_right_hash = Pole.hash(new Pole(1, -1));
        int upper_right_hash = Pole.hash(new Pole(1, 1));
        int max_distance = 1;
        if (prev_pole.isKing()) {
            max_distance = 7;
        }
        int[] directions = {lower_left_hash, upper_left_hash, lower_right_hash, upper_right_hash};
        for (int move : directions) {
            for (int i = 1; i <= max_distance; ++i) {
                int cur_hash = Pole.hash(prev_pole) + move * i;
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
     *
     * @param prev_pole - pole on previous position
     * @param new_pole  - pole on new position
     * @param cut_poles - poles cut during previous moves on this stage
     * @return true if cut is correct
     * @throws GameLogicException (if the moves are against the rules)
     */
    public boolean isNormalCut(Pole prev_pole, Pole new_pole, HashSet<Integer> cut_poles) throws GameLogicException {
        if (new_pole.getLastCutChecker().getColor() != new_pole.getColor()) {
            prev_pole.addChecker(new_pole.getLastCutChecker());
            if (new_pole.isSameKindCheckers(prev_pole)) {
                if (new_pole.isOnSameDiagonal(prev_pole)) {
                    int direction = prev_pole.direction_hash(new_pole);

                    // Calc max distance depending on isKing
                    int max_distance = 1;
                    if (new_pole.isKing()) {
                        max_distance = 7;
                    }
                    for (int i = 1; i <= max_distance; ++i) {
                        int cur_hash = Pole.hash(prev_pole) + direction * i;
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

    /**
     * Checking if move is possible
     *
     * @param prev_pole - pole on previous position
     * @param new_pole  - pole on new position
     * @param cut_poles - poles, cut during previous moves on this stage
     * @throws GameLogicException (if the moves are against the rules)
     */
    void checkNewPole(Pole prev_pole, Pole new_pole, HashSet<Integer> cut_poles) throws GameLogicException {
        if (checkBusyCell(new_pole)) {
            throw new GameLogicException("busy cell");
        }
        if (isCanCut(prev_pole, cut_poles)) {
            if (!isNormalCut(prev_pole, new_pole, cut_poles)) {
                throw new GameLogicException("invalid move");
            } else {
                poles.remove(Pole.hash(prev_pole));
                poles.put(Pole.hash(new_pole), new_pole);
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
                poles.remove(Pole.hash(prev_pole));
                poles.put(Pole.hash(new_pole), new_pole);
            }
        }
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
            checkNewPole(prev_pole, new_pole, cut_poles);
            prev_pole = new_pole;
        }
    }
}
