package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.util.*;

/**
 * Graph JSON generator.
 *
 * Usage:
 *   mvn exec:java -Dexec.mainClass="app.GraphGenerator" -Dexec.args="<n> <density> <outPath> <seed>"
 * Example:
 *   mvn exec:java -Dexec.mainClass="app.GraphGenerator" -Dexec.args="50 sparse output/graph_50_sparse.json 42"
 *
 * density: sparse | medium | dense
 */
public class GraphGenerator {

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.out.println("Usage: GraphGenerator <n> <density> <outPath> <seed>");
            System.out.println("density: sparse|medium|dense");
            return;
        }

        int n = Integer.parseInt(args[0]);
        String density = args[1];
        String outPath = args[2];
        long seed = Long.parseLong(args[3]);

        generate(n, density, outPath, seed);
    }

    public static void generate(int n, String density, String outPath, long seed) throws Exception {
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");

        int maxPossible = n * (n - 1) / 2;
        int m;
        switch (density.toLowerCase()) {
            case "sparse":
                m = Math.min(maxPossible, Math.max(n, n)); // approx n edges
                break;
            case "medium":
                m = Math.min(maxPossible, Math.max(n, 5 * n)); // approx 5n edges
                break;
            case "dense":
                m = Math.min(maxPossible, Math.max(n, (n * (n - 1) / 4))); // ~ n(n-1)/4
                break;
            default:
                throw new IllegalArgumentException("Unknown density: " + density);
        }

        Random rnd = new Random(seed);

        // vertices
        List<String> vertices = new ArrayList<>();
        for (int i = 0; i < n; i++) vertices.add("V" + i);

        // all possible edges (u < v)
        List<int[]> allPairs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                allPairs.add(new int[]{i, j});
            }
        }
        Collections.shuffle(allPairs, rnd);

        // choose first m pairs
        m = Math.min(m, allPairs.size());
        List<int[]> chosen = allPairs.subList(0, m);

        // assign random weights (1..100)
        List<Edge> edges = new ArrayList<>();
        for (int[] p : chosen) {
            double w = 1 + rnd.nextInt(100);
            edges.add(new Edge("V" + p[0], "V" + p[1], w));
        }

        // prepare JSON
        ObjectMapper om = new ObjectMapper();
        ObjectNode root = om.createObjectNode();
        ArrayNode verts = om.createArrayNode();
        for (String v : vertices) verts.add(v);
        root.set("vertices", verts);

        ArrayNode eds = om.createArrayNode();
        for (Edge e : edges) {
            ObjectNode en = om.createObjectNode();
            en.put("u", e.u);
            en.put("v", e.v);
            en.put("w", e.w);
            eds.add(en);
        }
        root.set("edges", eds);

        // write file
        File out = new File(outPath);
        out.getParentFile().mkdirs();
        om.writerWithDefaultPrettyPrinter().writeValue(out, root);

        System.out.printf("Generated graph: n=%d, m=%d, density=%s -> %s (seed=%d)%n", n, m, density, out.getAbsolutePath(), seed);
    }
}
