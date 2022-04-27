import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
    private ArrayList<ArrayList<Integer>> EDGE_WEIGHT_SECTION;

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
            this.EDGE_WEIGHT_SECTION = new ArrayList<>();

            ln = br.readLine();

            while (!ln.equals("EOF")) {
                String[] arr_ln = ln.replace("    ", " ")
                        .replace("   ", " ")
                        .replace("  ", " ")
                        .substring(1)
                        .split(" ");
                ArrayList<Integer> last = new ArrayList<>();
                this.EDGE_WEIGHT_SECTION.add(last);
                for (String i: arr_ln)
                    last.add(Integer.parseInt(i));
                ln = br.readLine();
            }
            br.close();

        } catch (IOException e) {
            System.err.println("TSPInstance: file not found. Please re-try and be sure of input path.");
        }
    }

    public int getCost(int src, int dst) {
        try {
            if (src == dst)
                throw new RuntimeException();
            return this.EDGE_WEIGHT_SECTION.get(src).get(dst);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("TSPInstance: the path does not exist. (" + src + ", " + dst + ")");
        } catch (RuntimeException e) {
            System.out.println("TSPInstance: source and destination are equals.");
        }
        return 0;
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
