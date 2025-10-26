package app;
import java.util.*;

/**
 * Kruskal's algorithm with clear ops counting and runRepeated helper.
 *
 * Counting rules (ops):
 *  - +1 for each find call (we count two finds per edge: for u and v)
 *  - +1 for each successful union (when two different components are merged)
 *  - We do not count the internal operations of sorting (but we measure total time including sort).
 *
 * Time measurement: System.nanoTime() -> ms
 */
public class KruskalMST {

    public static class Result {
        public final List<Edge> mst;
        public final double totalCost;
        public final long ops;
        public final long timeMs;
        public Result(List<Edge> mst, double totalCost, long ops, long timeMs){
            this.mst = mst;
            this.totalCost = totalCost;
            this.ops = ops;
            this.timeMs = timeMs;
        }
    }

    public static Result run(Graph g){
        long ops = 0;
        long t0 = System.nanoTime();

        List<Edge> edges = new ArrayList<>(g.edges);
        Collections.sort(edges); // sort by weight (Edge implements Comparable)
        // note: we don't add explicit ops for sort; document it in report

        DisjointSet ds = new DisjointSet();
        ds.makeSet(g.vertices);

        List<Edge> mst = new ArrayList<>();
        for(Edge e: edges){
            // count two find operations
            String ru = ds.find(e.u); ops++;
            String rv = ds.find(e.v); ops++;
            if(ru == null || rv == null) continue;
            if(!ru.equals(rv)){
                // union by roots (no extra finds)
                boolean united = ds.unionRoots(ru, rv);
                if (united) {
                    mst.add(e);
                    ops++; // count successful union
                }
            }
            if(mst.size() == g.vertices.size() - 1) break;
        }

        double total = mst.stream().mapToDouble(x -> x.w).sum();
        long t1 = System.nanoTime();
        long timeMs = (t1 - t0) / 1_000_000;
        return new Result(mst, total, ops, timeMs);
    }

    public static Result runRepeated(Graph g, int trials){
        if(trials <= 1) return run(g);
        long sumOps = 0;
        long sumTime = 0;
        List<Edge> lastMst = null;
        double lastCost = 0;
        for(int i = 0; i < trials; i++){
            Result r = run(g);
            lastMst = r.mst;
            lastCost = r.totalCost;
            sumOps += r.ops;
            sumTime += r.timeMs;
        }
        return new Result(lastMst, lastCost, sumOps / trials, sumTime / trials);
    }
}
