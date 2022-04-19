public class TSPManager {

    private TSPInstance instance;
    private final String name = "Experiment i";

    /*
     * Realizar inciso A
     */

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
        TSPInstance br17 = new TSPInstance("br17.atsp");
        //System.out.println(br17);

        TSPInstance p43 = new TSPInstance("p43.atsp");
        //System.out.println(p43);
    }

}
