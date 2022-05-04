package crossover;

import tsp.Individual;

public interface CrossoverOperator {
    Individual[] applyOperator(Individual[] parents);
}