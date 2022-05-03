package tsp;

import java.io.*;
import java.util.ArrayList;

public class TSPInstance {

    /*
     * 1) Seleccionar el archivo con la información de la instancia del problema a resolver.
     * El archivo contendrá la cantidad de ciudades a considerar, y también el costo del viaje
     * entre cada par de esas ciudades. Se solicita utilizar los archivos provistos por la cátedra.
     * En este respecto, se proveen archivos con distintas instancias del problema provenientes
     * de la librería TSPLIB95.
     * */

    private final String PATH;
    private String NAME;
    private int DIMENSION;
    private String EDGE_WEIGHT_TYPE;
    private String EDGE_WEIGHT_FORMAT;
    private int[][] EDGE_WEIGHT_SECTION;

    public TSPInstance(String file) {
        this.PATH = System.getenv("PWD") + "/instancias-TSP/" + file;
        try (FileReader fr = new FileReader(this.PATH)) {
            BufferedReader br = new BufferedReader(fr);

            String ln = br.readLine();
            this.NAME = ln.substring(5).replace(" ", "");

            // Skip TYPE and COMMENT.
            br.readLine();
            br.readLine();

            ln = br.readLine();
            this.DIMENSION = Integer.parseInt(ln.substring(10).replace(" ", ""));

            ln = br.readLine();
            this.EDGE_WEIGHT_TYPE = ln.substring(17).replace(" ", "");

            ln = br.readLine();
            this.EDGE_WEIGHT_FORMAT = ln.substring(19).replace(" ", "");

            br.readLine();
            this.EDGE_WEIGHT_SECTION = new int[this.DIMENSION][];
            int f = 0;

            ln = br.readLine();

            while (!ln.equals("EOF")) {
                String[] arr_ln = ln.replace("    ", " ")
                        .replace("   ", " ")
                        .replace("  ", " ")
                        .substring(1)
                        .split(" ");
                int[] columns = new int[this.DIMENSION];
                this.EDGE_WEIGHT_SECTION[f] = columns;
                for (int j = 0; j < columns.length; j++)
                    columns[j] = Integer.parseInt(arr_ln[j]);
                ln = br.readLine();
                f++;
            }
            br.close();

        } catch (IOException e) {
            System.err.println("tsp.TSPInstance: file not found. Please re-try and be sure of input path.");
        }
    }

    public int getCost(int src, int dst) {
        try {
            if (src == dst)
                throw new RuntimeException();
            return this.EDGE_WEIGHT_SECTION[src][dst];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("TSPInstance: the path does not exist. (" + src + ", " + dst + ")");
        } catch (RuntimeException e) {
            System.out.println("TSPInstance: source and destination are equals.");
        }
        return Integer.MAX_VALUE;
    }

    public String getNAME() {
        return NAME;
    }

    public int getDIMENSION() {
        return DIMENSION;
    }

    public String getEDGE_WEIGHT_TYPE() {
        return EDGE_WEIGHT_TYPE;
    }

    public String getEDGE_WEIGHT_FORMAT() {
        return EDGE_WEIGHT_FORMAT;
    }

    public String toString() {
        return  "NAME: " + this.getNAME() + "\n" +
                "TYPE: ATSP\n" +
                "COMMENT: TSP instance.\n" +
                "DIMENSION: " + this.getDIMENSION() + "\n" +
                "EDGE_WEIGHT_TYPE: " + this.getEDGE_WEIGHT_TYPE() + "\n" +
                "EDGE_WEIGHT_FORMAT: " + this.getEDGE_WEIGHT_FORMAT() + "\n" +
                "EDGE_WEIGHT_SECTION:\n" +
                this.EDGE_WEIGHT_SECTION;
    }

}
