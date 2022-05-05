package mutation;

import tsp.Individual;

public class MutationInsertion implements MutationOperator {

    public static final String NAME = "insertion";

    /**
     *
     * • Se eligen dos valores al azar de la solución
     * • Se mueve el segundo valor a continuación del primero (se corren los valores ubicados entre medio de los dos
     * elegidos)
     * • Este operador preserva la mayor parte del orden existente entre los valores y de la información
     * sobre adyacencias
     *
     * @param son
     */
    @Override
    public void applyOperator(Individual son) {
        int p1, p2;

        do {
            p1 = (int) (Math.random() * (son.instance.DIMENSION - 1));
            p2 = (int) (Math.random() * son.instance.DIMENSION);
        } while (p1 == p2);

        if (p1 > p2) {
            int tmp = p1;
            p1 = p2;
            p2 = tmp;
        }

        /*System.out.println("punto1: " + p1 + " punto2: " + p2);
        System.out.println(son.toString());*/

        int valueToMove = son.genotype[p2];

        for (int i = p2; i > p1 + 1; i--)
            son.genotype[i] = son.genotype[i-1];

        son.genotype[p1+1] = valueToMove;
    }
}
