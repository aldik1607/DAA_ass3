package app;

import java.util.*;

public class PrimMST {

    public static class Result {
        public final List<Edge> mst;
        public final double totalCost;
        public final long ops;
        public final long timeMs; // milliseconds

        public Result(List<Edge> mst, double totalCost, long ops, long timeMs) {
            this.mst = mst;
            this.totalCost = totalCost;
            this.ops = ops;
            this.timeMs = timeMs;
        }
    }

    // Node stored in priority queue: (key, vertex)
    private static class PQNode {
        final String v;
        final double key;
        PQNode(String v, double key) { this.v = v; this.key = key; }
    }


    public static Result run(Graph g, String start) {
        long ops = 0;
        long t0 = System.nanoTime();

        // Initialize
        Map<String, Double> key = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> inMST = new HashSet<>();
        for (String v : g.vertices) { key.put(v, Double.POSITIVE_INFINITY); parent.put(v, null); }

        key.put(start, 0.0);

        // adjacency list
        Map<String, List<Edge>> adj = new HashMap<>();
        for (String v : g.vertices) adj.put(v, new ArrayList<>());
        for (Edge e : g.edges) {
            // undirected: add both directions
            adj.get(e.u).add(e);
            adj.get(e.v).add(new Edge(e.v, e.u, e.w));
        }

        // Priority queue of PQNode, ordered by key
        PriorityQueue<PQNode> pq = new PriorityQueue<>(Comparator.comparingDouble(n -> n.key));
        // initially insert all vertices (or only start). We'll insert start and let others be added lazily.
        pq.add(new PQNode(start, 0.0));

        List<Edge> mst = new ArrayList<>();

        while (!pq.isEmpty()) {
            PQNode node = pq.poll();
            String u = node.v;

            // Lazy-deletion: if this node's key doesn't match current key[u], it's outdated -> skip
            double currentKey = key.getOrDefault(u, Double.POSITIVE_INFINITY);
            if (node.key != currentKey) {
                continue; // outdated entry
            }

            // If u already in MST, skip (shouldn't happen if keys tracked properly)
            if (inMST.contains(u)) continue;

            // Accept this vertex into MST
            inMST.add(u);
            ops++; // extract-min counted as essential operation

            String p = parent.get(u);
            if (p != null) {
                // parent[u] exists => add edge (p - u) to MST using the recorded key value
                mst.add(new Edge(p, u, currentKey));
            }

            // relax adjacency edges
            for (Edge e : adj.get(u)) {
                ops++; // counting adjacency processing
                String v = e.v;
                if (inMST.contains(v)) continue;
                if (e.w < key.get(v)) {
                    key.put(v, e.w);
                    parent.put(v, u);
                    // lazy-decrease: insert new node with smaller key
                    pq.add(new PQNode(v, e.w));
                    ops++; // count decrease-key as an operation
                }
            }
        }

        // If MST doesn't include all vertices, graph isn't connected
        if (inMST.size() != g.vertices.size()) {
            long t1 = System.nanoTime();
            long timeMs = (t1 - t0) / 1_000_000;
            // return partial result but mark cost as Infinity or 0 depending on preference
            double total = mst.stream().mapToDouble(x -> x.w).sum();
            return new Result(mst, total, ops, timeMs);
        }

        double total = mst.stream().mapToDouble(x -> x.w).sum();
        long t1 = System.nanoTime();
        long timeMs = (t1 - t0) / 1_000_000;
        return new Result(mst, total, ops, timeMs);
    }


    public static Result runRepeated(Graph g, String start, int trials) {
        if (trials <= 1) return run(g, start);

        List<Edge> lastMst = null;
        double lastCost = 0.0;
        long sumOps = 0;
        long sumTimeMs = 0;
        for (int i = 0; i < trials; i++) {
            Result r = run(g, start);
            lastMst = r.mst;
            lastCost = r.totalCost;
            sumOps += r.ops;
            sumTimeMs += r.timeMs;
        }
        long avgOps = sumOps / trials;
        long avgTimeMs = sumTimeMs / trials;
        return new Result(lastMst, lastCost, avgOps, avgTimeMs);
    }
}
