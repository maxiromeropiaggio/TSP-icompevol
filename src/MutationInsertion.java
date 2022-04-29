import java.util.ArrayList;

public class MutationInsertion implements MutationOperator {

    /**
     *
     * • Se eligen dos valores al azar de la solución
     * • Se mueve el segundo valor a continuación del primero (se corren los valores ubicados entre medio de los dos
     * elegidos)
     * • Este operador preserva la mayor parte del orden existente entre los valores y de la información
     * sobre adyacencias
     *
     * @param p
     */
    @Override
    public void applyOperator(ArrayList<Integer> p) {

        int p1 = (int) (Math.random() * p.size());
        int p2 = (int) (Math.random() * p.size());

        if (p1 > p2) {
            int tmp = p1;
            p1 = p2;
            p2 = tmp;
        }

        int valueToMove = p.get(p2);
        p.remove(p2);

        p.add(p1+1, valueToMove);

    }
}
