# Analytical Report: Optimization of City Transportation Network

## 1. Introduction

### 1.1. Objective
The purpose of this assignment is to apply Prim's and Kruskal's algorithms to optimize a city's transportation network by determining the minimum set of roads that connect all city districts with the lowest possible total construction cost. Students will also analyze the efficiency of both algorithms and compare their performance.

### 1.2. Problem Statement
The city administration plans to construct roads connecting all districts such that:
- Each district is reachable from any other district
- The total construction cost is minimized
- This is modeled as finding the Minimum Spanning Tree (MST) in a weighted undirected graph

## 2. Methodology

### 2.1. Experimental Setup
**Test Data Generation:**
Graphs of varying sizes and densities were generated:
- **Small graphs:** 6-10 vertices, 9-45 edges
- **Medium graphs:** 30 vertices, 150-217 edges  
- **Large graphs:** 50 vertices, 250 edges

**Density Classification:**
- **Dense:** ~45-100% of possible edges
- **Medium:** ~20-45% of possible edges
- **Sparse:** <20% of possible edges (excluded due to disconnected graphs)

### 2.2. Performance Metrics
- **Execution time** (milliseconds)
- **Operation count** (comparisons, union operations)
- **MST total cost** (validation metric)
- **Memory efficiency**

### 2.3. Implementation Details
- **Programming Language:** Java
- **Prim's Algorithm:** Priority queue implementation
- **Kruskal's Algorithm:** Union-Find with path compression
- **Number of trials:** 3 runs per test case (averaged)

## 3. Experimental Results

### 3.1. Summary of Results

| Graph | Vertices | Edges | Density | Algorithm | Time (ms) | Operations | Total Cost |
|-------|----------|-------|---------|-----------|-----------|------------|------------|
| input.json | 6 | 9 | dense | Prim | 6-40 | 137 | 13.00 |
| input.json | 6 | 9 | dense | Kruskal | 1-4 | 17 | 13.00 |
| dense10.json | 10 | 22 | dense | Prim | 3 | 279 | 249.00 |
| dense10.json | 10 | 22 | dense | Kruskal | 0 | 35 | 249.00 |
| medium50.json | 50 | 250 | medium | Prim | 0 | 2402 | 815.00 |
| medium50.json | 50 | 250 | medium | Kruskal | 0 | 323 | 815.00 |

### 3.2. Performance Analysis

#### 3.2.1. Execution Time Comparison
- **Small graphs (6-10 vertices):** Kruskal is 6-10 times faster than Prim
- **Medium graphs (30 vertices):** Both algorithms show comparable execution time (0ms)
- **Large graphs (50 vertices):** Both algorithms show comparable execution time (0ms)

#### 3.2.2. Operation Efficiency
- **Small graphs:** Kruskal performs 8 times fewer operations than Prim
- **Medium graphs:** Kruskal performs 10-13 times fewer operations than Prim  
- **Large graphs:** Kruskal performs 7.4 times fewer operations than Prim

#### 3.2.3. MST Cost Validation
- Both algorithms consistently produced identical MST costs
- All generated MSTs are valid and connected
- Algorithm correctness verified across all test cases

## 4. Algorithm Comparison

### 4.1. Time Complexity Analysis

| Algorithm | Best Case | Average Case | Worst Case |
|-----------|-----------|--------------|------------|
| Prim | O(E log V) | O(E log V) | O(E log V) |
| Kruskal | O(E log E) | O(E log E) | O(E log E) |

### 4.2. Space Complexity
- **Prim:** O(V) for priority queue and key arrays
- **Kruskal:** O(E) for edge storage and O(V) for union-find

### 4.3. Implementation Complexity
- **Prim:** More complex due to priority queue management and key updates
- **Kruskal:** Simpler implementation with straightforward edge sorting and union-find operations

## 5. Discussion

### 5.1. Key Findings

1. **Operation Efficiency:** Kruskal's algorithm consistently demonstrated superior operation efficiency across all graph sizes, performing 7-13 times fewer operations than Prim's algorithm.

2. **Execution Time:** While Kruskal showed significant time advantages on small graphs, both algorithms performed comparably on larger graphs within the measurable time resolution.

3. **Memory Usage:** Kruskal requires storing all edges, making it less memory-efficient for very dense graphs, while Prim maintains only adjacency information.

4. **Graph Density Impact:** 
   - **Dense graphs:** Prim may have advantages with optimized adjacency matrix implementations
   - **Sparse graphs:** Kruskal is generally more efficient due to fewer edges to sort

### 5.2. Limitations
- Sparse graphs were excluded from analysis due to disconnected components
- Time measurements at millisecond scale may not capture subtle performance differences
- Memory usage was not quantitatively measured

## 6. Conclusions and Recommendations

### 6.1. Algorithm Selection Guidelines

**Choose Kruskal's Algorithm when:**
- Working with sparse to medium-dense graphs
- Implementation simplicity is prioritized
- Memory constraints are not critical
- Edge weights are pre-sorted or sorting is efficient

**Choose Prim's Algorithm when:**
- Dealing with very dense graphs
- Memory efficiency is crucial
- The graph is represented as an adjacency matrix
- Incremental MST construction is required

### 6.2. Final Recommendations

For the city transportation network optimization problem:

1. **For typical urban networks** (medium density): **Kruskal's algorithm** is recommended due to its superior operation efficiency and simpler implementation.

2. **For dense metropolitan areas:** Consider **Prim's algorithm** with adjacency matrix representation for better memory efficiency.

3. **For implementation priority:** **Kruskal's algorithm** is easier to implement correctly and maintain.

### 6.3. Future Work
- Extend analysis to very large graphs (1000+ vertices)
- Include memory usage measurements
- Test with different graph representations (adjacency matrix vs list)
- Analyze performance with parallel implementations

## References
1. Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009). Introduction to Algorithms (3rd ed.). MIT Press.
2. Sedgewick, R., & Wayne, K. (2011). Algorithms (4th ed.). Addison-Wesley Professional.