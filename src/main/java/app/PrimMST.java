package app;

import java.util.*;

public class PrimMST {

    public static class Result {
        public final List<Edge> mst;
        public final double totalCost;
        public final long ops;
        public final long timeMs;

        public Result(List<Edge> mst, double totalCost, long ops, long timeMs) {
            this.mst = mst;
            this.totalCost = totalCost;
            this.ops = ops;
            this.timeMs = timeMs;
        }
    }

    public static Result run(Graph g, String start) {
        long ops = 0;
        long t0 = System.nanoTime();

        // Инициализация
        Map<String, Double> key = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        Map<String, Boolean> inMST = new HashMap<>();

        for (String v : g.vertices) {
            key.put(v, Double.MAX_VALUE);
            parent.put(v, null);
            inMST.put(v, false);
            ops += 3;
        }

        key.put(start, 0.0);
        ops++;

        // Построение списка смежности
        Map<String, List<Edge>> adj = new HashMap<>();
        for (String v : g.vertices) {
            adj.put(v, new ArrayList<>());
            ops++;
        }

        for (Edge e : g.edges) {
            adj.get(e.u).add(e);
            // Для неориентированного графа добавляем обратное ребро
            adj.get(e.v).add(new Edge(e.v, e.u, e.w));
            ops += 2;
        }

        // Приоритетная очередь для вершин
        PriorityQueue<Map.Entry<String, Double>> pq =
                new PriorityQueue<>(Comparator.comparingDouble(Map.Entry::getValue));
        pq.offer(new AbstractMap.SimpleEntry<>(start, 0.0));
        ops++;

        List<Edge> mst = new ArrayList<>();

        while (!pq.isEmpty()) {
            Map.Entry<String, Double> entry = pq.poll();
            String u = entry.getKey();
            double weight = entry.getValue();
            ops++;

            // Если вершина уже в MST, пропускаем
            if (inMST.get(u)) {
                ops++;
                continue;
            }

            inMST.put(u, true);
            ops++;

            // Если это не стартовая вершина, добавляем ребро в MST
            if (!u.equals(start)) {
                String p = parent.get(u);
                mst.add(new Edge(p, u, weight));
                ops += 2;
            }

            // Обходим соседей
            for (Edge edge : adj.get(u)) {
                String v = edge.v.equals(u) ? edge.u : edge.v;
                ops++;

                if (!inMST.get(v) && edge.w < key.get(v)) {
                    key.put(v, edge.w);
                    parent.put(v, u);
                    pq.offer(new AbstractMap.SimpleEntry<>(v, edge.w));
                    ops += 4; // сравнение, два присваивания, добавление в очередь
                } else {
                    ops++; // за сравнение, если не выполнилось
                }
            }
        }

        double totalCost = mst.stream().mapToDouble(e -> e.w).sum();
        long t1 = System.nanoTime();
        long timeMs = (t1 - t0) / 1_000_000;

        return new Result(mst, totalCost, ops, timeMs);
    }

    public static Result runRepeated(Graph g, String start, int trials) {
        if (trials <= 1) return run(g, start);

        long sumOps = 0;
        long sumTime = 0;
        List<Edge> lastMst = null;
        double lastCost = 0;

        for (int i = 0; i < trials; i++) {
            Result r = run(g, start);
            lastMst = r.mst;
            lastCost = r.totalCost;
            sumOps += r.ops;
            sumTime += r.timeMs;
        }

        return new Result(lastMst, lastCost, sumOps / trials, sumTime / trials);
    }
}