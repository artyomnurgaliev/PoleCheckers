import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

class Poles {
    private HashMap<Integer, Pole> poles = new HashMap<>();

    public void addPole(String pole_str) throws GameLogicException {
        Pole pole = new Pole(pole_str);
        poles.put(Pole.hash(pole), pole);
    }

    public boolean checkBusyCell(Pole new_pole) {
        return poles.containsKey(Pole.hash(new_pole));
    }

    public boolean checkBusyCell(int hash) {
        return poles.containsKey(hash);
    }

    public boolean IsCanCut(Pole new_pole, HashSet<Integer> cut_poles) {
        int lower_left_hash = -11;
        int upper_left_hash = -9;
        int lower_right_hash = 9;
        int upper_right_hash = 11;
        int max_distance = 1;
        if (new_pole.isKing()) {
            max_distance = 7;
        }
        int[] directions = {lower_left_hash, upper_left_hash, lower_right_hash, upper_right_hash};
        for (int move : directions) {
            for (int i = 1; i < max_distance; ++i) {
                int cur_hash = Pole.hash(new_pole) + move * i;
                if (poles.containsKey(cur_hash)) {
                    if (poles.get(cur_hash).getColor() == new_pole.getColor()) {
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

    public boolean isNormalCut(Pole prev_pole, Pole new_pole, HashSet<Integer> cut_poles) throws GameLogicException {
        if (new_pole.getLastCutChecker().getColor() != new_pole.getColor()) {
            prev_pole.addChecker(new_pole.getLastCutChecker());
            if (new_pole.isSameCheckers(prev_pole)) {
                if (new_pole.isOnSameDiagonal(prev_pole)) {
                    int direction = prev_pole.direction_hash(new_pole);

                    // Calc max distance depending on isKing
                    int max_distance = 1;
                    if (new_pole.isKing()) {
                        max_distance = 7;
                    }
                    for (int i = 1; i < max_distance; ++i) {
                        int cur_hash = Pole.hash(new_pole) + direction * i;
                        if (poles.containsKey(cur_hash)) {
                            // Check that cut pole color is not the same as new pole color
                            if (poles.get(cur_hash).getColor() == new_pole.getColor()) {
                                return false;
                            }
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
                            cut_poles.add(cur_hash);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void checkNewPole(Pole prev_pole, Pole new_pole, HashSet<Integer> cut_poles) throws GameLogicException {
        if (checkBusyCell(new_pole)) {
            throw new GameLogicException("busy cell");
        }
        if (IsCanCut(new_pole, cut_poles)) {
            if (!isNormalCut(prev_pole, new_pole, cut_poles)) {
                throw new GameLogicException("invalid move");
            } else {
                poles.remove(Pole.hash(prev_pole));
                poles.put(Pole.hash(new_pole), new_pole);
            }
        }
        if (!IsCanCut(new_pole, cut_poles)) {
            if (!new_pole.isSameCheckers(prev_pole)) {
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