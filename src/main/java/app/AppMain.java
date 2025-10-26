package app;
import com.fasterxml.jackson.databind.*;
import java.io.*;
public class AppMain {
    public static void main(String[] args) throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonNode root = om.readTree(new File("src/main/resources/input.json"));

        Graph g = new Graph();
        for(JsonNode v: root.get("vertices")) g.addVertex(v.asText());
        for(JsonNode e: root.get("edges")){
            g.addEdge(e.get("u").asText(), e.get("v").asText(), e.get("w").asDouble());
        }

        System.out.println("Vertices: " + g.vertices.size() + ", Edges: " + g.edges.size());

        PrimMST.Result prim = PrimMST.run(g, g.vertices.iterator().next());
        System.out.println("Prim MST cost = " + prim.totalCost + ", time(ms)=" + prim.timeMs + ", ops=" + prim.ops);
        prim.mst.forEach(System.out::println);

        KruskalMST.Result kruskal = KruskalMST.run(g);
        System.out.println("Kruskal MST cost = " + kruskal.totalCost + ", time(ms)=" + kruskal.timeMs + ", ops=" + kruskal.ops);
        kruskal.mst.forEach(System.out::println);
    }
}
