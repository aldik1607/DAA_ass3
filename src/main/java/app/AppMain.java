package app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AppMain {

    private static final String CSV_OUT = "output/results.csv";

    public static void main(String[] args) throws Exception {

        String arg0 = args.length >= 1 ? args[0] : "src/main/resources/input.json";
        int trials = args.length >= 2 ? Integer.parseInt(args[1]) : 1;

        File f0 = new File(arg0);
        List<File> filesToProcess = new ArrayList<>();
        if (f0.isDirectory()) {
            File[] list = f0.listFiles((d, name) -> name.toLowerCase().endsWith(".json"));
            if (list != null) filesToProcess.addAll(Arrays.asList(list));
        } else {
            filesToProcess.add(f0);
        }

        Path outDir = Paths.get("output");
        if (!Files.exists(outDir)) Files.createDirectories(outDir);

        File csv = new File(CSV_OUT);
        boolean writeHeader = !csv.exists();
        try (FileWriter fw = new FileWriter(csv, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            if (writeHeader) {
                bw.write("file,n,m,density,trials,algo,time_ms,ops,totalCost");
                bw.newLine();
            }

            for (File jf : filesToProcess) {
                System.out.println("Processing: " + jf.getAbsolutePath());
                Graph g = readGraphFromJson(jf.getAbsolutePath());

                System.out.printf("Vertices: %d, Edges: %d%n", g.vertices.size(), g.edges.size());

                if (!isConnected(g)) {
                    System.out.println("Graph is not connected — skipping MST.");
                    continue;
                }

                String start = g.vertices.iterator().next();

                PrimMST.Result prim = PrimMST.runRepeated(g, start, trials);
                KruskalMST.Result krus = KruskalMST.runRepeated(g, trials);

                // Print to console
                System.out.printf("Prim MST cost = %.2f, time(ms)=%d, ops=%d%n", prim.totalCost, prim.timeMs, prim.ops);
                prim.mst.forEach(e -> System.out.println(e));
                System.out.printf("Kruskal MST cost = %.2f, time(ms)=%d, ops=%d%n", krus.totalCost, krus.timeMs, krus.ops);
                krus.mst.forEach(e -> System.out.println(e));

                // try guess density label based on m and n
                String density = guessDensity(g.vertices.size(), g.edges.size());

                // Write two CSV lines (Prim and Kruskal)
                bw.write(String.format("%s,%d,%d,%s,%d,Prim,%d,%d,%.2f",
                        jf.getName(), g.vertices.size(), g.edges.size(), density, trials, prim.timeMs, prim.ops, prim.totalCost));
                bw.newLine();
                bw.write(String.format("%s,%d,%d,%s,%d,Kruskal,%d,%d,%.2f",
                        jf.getName(), g.vertices.size(), g.edges.size(), density, trials, krus.timeMs, krus.ops, krus.totalCost));
                bw.newLine();

                bw.flush();
                System.out.println("Result appended to " + CSV_OUT);
                System.out.println("---------------------------------------------------");
            }
        }
    }

    private static Graph readGraphFromJson(String path) throws IOException {
        ObjectMapper om = new ObjectMapper();
        JsonNode root = om.readTree(new File(path));
        Graph g = new Graph();
        for (JsonNode v : root.get("vertices")) g.addVertex(v.asText());
        for (JsonNode e : root.get("edges")) g.addEdge(e.get("u").asText(), e.get("v").asText(), e.get("w").asDouble());
        return g;
    }

    private static String guessDensity(int n, int m) {
        int max = n * (n - 1) / 2;
        double ratio = (max == 0) ? 0.0 : (double) m / max;
        if (ratio < 0.05) return "sparse";
        if (ratio < 0.3) return "medium";
        return "dense";
    }

    private static boolean isConnected(Graph g) {
        if (g.vertices.isEmpty()) return true;
        Map<String, List<Edge>> adj = new HashMap<>();
        for (String v : g.vertices) adj.put(v, new ArrayList<>());
        for (Edge e : g.edges) {
            adj.get(e.u).add(e);
            adj.get(e.v).add(new Edge(e.v, e.u, e.w));
        }
        Set<String> visited = new HashSet<>();
        Deque<String> st = new ArrayDeque<>();
        String start = g.vertices.iterator().next();
        st.push(start);
        while (!st.isEmpty()) {
            String u = st.pop();
            if (visited.contains(u)) continue;
            visited.add(u);
            for (Edge e : adj.get(u)) if (!visited.contains(e.v)) st.push(e.v);
        }
        return visited.size() == g.vertices.size();
    }
}
