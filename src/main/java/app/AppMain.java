package app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;

public class AppMain {
    public static void main(String[] args) throws Exception {
        // args: <path-to-json> <trials>
        String path = "src/main/resources/input.json";
        int trials = 1;
        if (args.length >= 1) path = args[0];
        if (args.length >= 2) trials = Integer.parseInt(args[1]);

        ObjectMapper om = new ObjectMapper();
        JsonNode root = om.readTree(new File(path));

        Graph g = new Graph();
        for (JsonNode v : root.get("vertices")) g.addVertex(v.asText());
        for (JsonNode e : root.get("edges")) {
            g.addEdge(e.get("u").asText(), e.get("v").asText(), e.get("w").asDouble());
        }

        System.out.printf("Vertices: %d, Edges: %d%n", g.vertices.size(), g.edges.size());

        // Optional: check connectivity
        if (!isConnected(g)) {
            System.out.println("Graph is not connected — MST not defined.");
            return;
        }

        // Start vertex for Prim
        String start = g.vertices.iterator().next();

        // Run Prim repeated
        PrimMST.Result primRes = PrimMST.runRepeated(g, start, trials);
        System.out.println("Prim MST cost = " + primRes.totalCost + ", time(ms)=" + primRes.timeMs + ", ops=" + primRes.ops);
        primRes.mst.forEach(System.out::println);

        // Run Kruskal repeated
        KruskalMST.Result krusRes = KruskalMST.runRepeated(g, trials);
        System.out.println("Kruskal MST cost = " + krusRes.totalCost + ", time(ms)=" + krusRes.timeMs + ", ops=" + krusRes.ops);
        krusRes.mst.forEach(System.out::println);

        // Print CSV line for easy collection
        System.out.println();
        System.out.println("CSV:");
        System.out.println("n,m,trials,algo,time_ms,ops,totalCost");
        System.out.printf("%d,%d,%d,Prim,%d,%d,%.2f%n", g.vertices.size(), g.edges.size(), trials, primRes.timeMs, primRes.ops, primRes.totalCost);
        System.out.printf("%d,%d,%d,Kruskal,%d,%d,%.2f%n", g.vertices.size(), g.edges.size(), trials, krusRes.timeMs, krusRes.ops, krusRes.totalCost);
    }

    // simple connectivity check using DFS
    private static boolean isConnected(Graph g) {
        if (g.vertices.isEmpty()) return true;
        Map<String, List<Edge>> adj = new HashMap<>();
        for (String v : g.vertices) adj.put(v, new ArrayList<>());
        for (Edge e : g.edges) {
            adj.get(e.u).add(e);
            adj.get(e.v).add(new Edge(e.v, e.u, e.w));
        }
        Set<String> visited = new HashSet<>();
        Stack<String> st = new Stack<>();
        String start = g.vertices.iterator().next();
        st.push(start);
        while (!st.isEmpty()) {
            String u = st.pop();
            if (visited.contains(u)) continue;
            visited.add(u);
            for (Edge e : adj.get(u)) {
                if (!visited.contains(e.v)) st.push(e.v);
            }
        }
        return visited.size() == g.vertices.size();
    }
}
