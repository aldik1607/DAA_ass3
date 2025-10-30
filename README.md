# City Transportation Network Optimization

## Project Overview
This project implements and compares Prim's and Kruskal's algorithms for finding Minimum Spanning Trees (MST) to optimize city transportation networks. The goal is to determine the most cost-effective set of roads connecting all city districts.

## Features
- **Prim's Algorithm** - Priority queue implementation
- **Kruskal's Algorithm** - Union-Find with path compression  
- **Performance Analysis** - Time and operation counting
- **Graph Generation** - Create test graphs of various sizes and densities
- **CSV Export** - Experimental results for analysis
- **Comprehensive Report** - Algorithm comparison and recommendations

## Project Structure
src/main/java/app/
AppMain.java # Main application runner
PrimMST.java # Prim's algorithm implementation
KruskalMST.java # Kruskal's algorithm implementation
Graph.java # Graph data structure
Edge.java # Edge representation
DisjointSet.java # Union-Find data structure
GraphGenerator.java # Test graph generator

## Run MST algorithms: 
java -cp "src/main/java;." app.AppMain input.json

## View results in output/results.csv and REPORT.md
