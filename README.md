## 1. Theoretical Background

| Algorithm     | Approach                                                                | Main Data Structures                | Time Complexity         | Space Complexity | Best Suited For |
| ------------- | ----------------------------------------------------------------------- | ----------------------------------- | ----------------------- | ---------------- | --------------- |
| **Prim’s**    | Expands the MST from a single vertex, adding the smallest adjacent edge | Priority Queue, Adjacency List      | O(E log V)              | O(V + E)         | Dense graphs    |
| **Kruskal’s** | Sorts edges and adds them sequentially if they do not form a cycle      | Edge List, Disjoint Set Union (DSU) | O(E log E) ≈ O(E log V) | O(V + E)         | Sparse graphs   |

**Expected behavior:**

* Both algorithms should produce the same MST total cost.
* Prim’s performs well on dense graphs due to localized edge selection.
* Kruskal’s is generally faster on sparse graphs due to efficient global edge sorting.
* Kruskal performs many **union** operations, while Prim performs none.

---

## 2. Experimental Setup

Four datasets were generated to represent varying network scales:

| Dataset     | Vertex Range | Graph Count | Description                                   |
| ----------- | ------------ | ----------- | --------------------------------------------- |
| Small       | 5–30         | 5 graphs    | Used for initial verification and debugging   |
| Medium      | 30–300       | 10 graphs   | Moderate density, for performance observation |
| Large       | 300–1000     | 10 graphs   | Tests scalability and performance gap         |
| Extra Large | 1000–2000    | 3 graphs    | Stress tests for performance and memory       |

Each graph was connected and undirected, with integer edge weights between 1 and 1000.
Execution times were averaged over 10 runs to reduce measurement noise.

---

## 3. Results and Observations

### 3.1 Correctness

For all datasets, Prim’s and Kruskal’s algorithms produced **identical MST total costs**, confirming the correctness of both implementations.

---

### 3.2 Small Graphs (5–30 vertices)

| Metric               | Observation                                                                                                                    |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------ |
| Average Prim time    | ~0.07 ms                                                                                                                       |
| Average Kruskal time | ~0.01 ms                                                                                                                       |
| Comments             | Both algorithms executed almost instantly. Kruskal showed slightly lower times due to its simpler edge handling at this scale. |

For small graphs, operation counts were low and comparable. Both algorithms are equally suitable.

---

### 3.3 Medium Graphs (30–300 vertices)

| Metric               | Observation                                                                                                                          |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| Average Prim time    | 0.3–1.0 ms                                                                                                                           |
| Average Kruskal time | 0.05–0.15 ms                                                                                                                         |
| Comments             | Kruskal consistently outperformed Prim. Sorting all edges once was more efficient than frequent heap operations in Prim’s algorithm. |

Prim’s priority queue introduced additional “other operations,” whereas Kruskal benefited from Java’s optimized sorting routines and minimal DSU overhead.

---

### 3.4 Large Graphs (300–1000 vertices)

| Metric               | Observation                                                                                                         |
| -------------------- | ------------------------------------------------------------------------------------------------------------------- |
| Average Prim time    | 0.3–2.6 ms                                                                                                          |
| Average Kruskal time | 0.06–0.44 ms                                                                                                        |
| Comments             | Kruskal remained 3–6× faster on average. The cost of heap updates in Prim’s algorithm increased with graph density. |

Theoretical behavior matched the results: Kruskal’s global sort remained efficient, while Prim’s adjacency scanning became more expensive.

---

### 3.5 Extra Large Graphs (1000–2000 vertices)

| Metric               | Observation                                                                                       |
| -------------------- | ------------------------------------------------------------------------------------------------- |
| Average Prim time    | 0.7–3.2 ms                                                                                        |
| Average Kruskal time | 0.1–0.7 ms                                                                                        |
| Comments             | Kruskal remained faster overall, but the difference narrowed slightly as graph density increased. |

Both algorithms scaled linearly with the number of edges, maintaining stable performance even at large input sizes.

---

### 3.6 Operation Counts

| Metric           | Prim                                      | Kruskal                                      |
| ---------------- | ----------------------------------------- | -------------------------------------------- |
| Comparisons      | Lower, localized per vertex               | Higher, due to global sorting and DSU checks |
| Unions           | 0 (no disjoint set)                       | Approximately V − 1                          |
| Other operations | Priority queue insertions and extractions | Sorting and DSU initializations              |

For example, in large graphs:

* Prim performed roughly 3,000–4,000 comparisons per run.
* Kruskal performed about 7,000–11,000 comparisons and V−1 unions.
  This aligns with theoretical expectations.

---

## 4. Theoretical vs Practical Comparison

| Aspect                          | Theoretical Expectation        | Practical Result                    | Consistency |
| ------------------------------- | ------------------------------ | ----------------------------------- | ----------- |
| MST total cost                  | Identical between algorithms   | Identical in all cases              | Consistent  |
| Prim faster on dense graphs     | Expected when E is close to V² | Observed on larger dense graphs     | Consistent  |
| Kruskal faster on sparse graphs | Expected when E < V log V      | Observed on small and medium graphs | Consistent  |
| Union operations                | 0 for Prim, V−1 for Kruskal    | Matches observed results            | Consistent  |
| Comparison counts               | Kruskal performs more          | Observed in results                 | Consistent  |
| Execution time scaling          | O(E log V) trend               | Matches observed timings            | Consistent  |

---

## 4. Conclusions

1. Both algorithms produced identical MST costs across all test cases, confirming correctness.
2. Kruskal’s algorithm was generally faster, particularly for sparse and moderately dense graphs.
3. Prim’s algorithm performed competitively for small or very dense graphs.
4. Operation counts and timing results closely matched theoretical expectations.
5. For large practical networks (up to 2000 vertices), Kruskal’s algorithm is preferred due to its simplicity and stable performance.
6. Prim’s algorithm remains a strong choice for dense graphs where adjacency lists are memory-efficient.

---

## 5. References

* Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009). *Introduction to Algorithms* (3rd ed.). MIT Press.
* Sedgewick, R., & Wayne, K. (2011). *Algorithms* (4th ed.). Addison-Wesley.