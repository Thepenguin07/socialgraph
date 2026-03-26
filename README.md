[Readme.md](https://github.com/user-attachments/files/26262895/Readme.md)
# SocialGraph — Intelligent Link Prediction & Recommendation System
### Phase 2 | 50% Completion | DAA Project

> Solving the **Popularity Bias** problem in friend recommendation systems using Graph Theory and the Jaccard Similarity Index.

---

## Team

| Member | Role | Module |
|---|---|---|
| Shifa Parveen | Team Lead | Core Graph Engine + System Integration |
| Vinay Bisht | Member | Database Connectivity (JDBC) |
| Gourvansh Airen Agarwal | Member | Recommendation Math Logic |
| Shaurya Tiwari | Member | Frontend UI/UX |

---

## Files Submitted (50% — Phase 2)

```
src/main/java/com/socialgraph/core/Graph.java                ← Core adjacency list + BFS + DFS
src/main/java/com/socialgraph/core/UserNode.java              ← Vertex data class
src/main/java/com/socialgraph/recommendation/RecommendationEngine.java  ← Jaccard + Triadic Closure
src/main/java/com/socialgraph/recommendation/Recommendation.java         ← Result model
src/main/java/com/socialgraph/db/DatabaseConnector.java       ← JDBC MySQL connector
src/main/resources/schema.sql                                 ← MySQL schema + seed data
pom.xml                                                       ← Maven build config
```

---

## DAA Concepts Implemented

| Concept | Class | Complexity |
|---|---|---|
| Custom Adjacency List | `Graph.java` | O(V+E) space |
| Breadth-First Search | `Graph.java` | O(V+E) time |
| Depth-First Search | `Graph.java` | O(V+E) time |
| Jaccard Similarity Index | `RecommendationEngine.java` | O(degree) per pair |
| Triadic Closure | `RecommendationEngine.java` | O(degree²) per user |
| Greedy + Priority Queue | `RecommendationEngine.java` | O(C log C) |

---

## Why Jaccard beats Mutual Friend Count

```
User A: 100 friends, 10 mutual → Jaccard = 10/110 = 0.09  (correctly ranked lower)
User B:  12 friends, 10 mutual → Jaccard = 10/14  = 0.71  (correctly ranked higher)
```
Standard systems rank both equally at score=10 — our system correctly identifies User B as the better match.

---

## Pending (Phase 3)
- ApiServer.java — REST API endpoints
- index.html — Frontend dashboard
- Full MySQL end-to-end integration
- JUnit test suite
- Dynamic node/edge addition UI
