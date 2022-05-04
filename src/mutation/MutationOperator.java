package mutation;

import tsp.Individual;

public interface MutationOperator {
    void applyOperator(Individual son);
}
