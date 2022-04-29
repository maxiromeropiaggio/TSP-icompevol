import java.util.ArrayList;

public interface CrossoverOperator {
    ArrayList<ArrayList<Integer>> applyOperator(ArrayList<Integer> pairParents,
                                                ArrayList<ArrayList<Integer>> population);
}