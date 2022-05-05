package survivors;
import tsp.Individual;

import java.util.ArrayList;

public interface SurvivorsOperator {
    void applyOperator(ArrayList<Individual> population, ArrayList<Individual> sons, int n);
}
