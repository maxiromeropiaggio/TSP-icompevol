import tsp.TSPManager;

public class Main {

    /*
     * B) Desarrollar experimentos computacionales para evaluar la efectividad y eficiencia
     * del algoritmo evolutivo implementado.
     * Para el desarrollo de los experimentos, se requiere utilizar alguna de las instancias del
     * problema provistas por la cátedra.
     * Luego, se requiere ejecutar diferentes configuraciones posibles para el algoritmo
     * evolutivo y, posteriormente, realizar un análisis comparativo de los resultados obtenidos
     * mediante las distintas configuraciones ejecutadas. El objetivo de este análisis es detectar
     * a la mejor configuración de las configuraciones ejecutadas.
     */

    public static void main(String[] args) {

        for (String arg: args) {
            TSPManager tspM = new TSPManager(arg);
            Thread n = new Thread(tspM, tspM.name);
            n.start();
        }

    }
}
