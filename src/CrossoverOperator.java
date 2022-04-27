import java.util.ArrayList;

public interface CrossoverOperator {
    ArrayList<ArrayList<Integer>> applyOperator(ArrayList<Integer> parParents, ArrayList<ArrayList<Integer>> poblation);
}