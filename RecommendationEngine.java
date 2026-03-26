package com.socialgraph.recommendation;

import com.socialgraph.core.Graph;

import java.util.*;

/**
 * Recommendation Engine — Jaccard Similarity + Triadic Closure + Greedy Priority Queue
 * Author: Gourvansh Airen Agarwal
 */
public class RecommendationEngine {

    private Graph graph;

    public RecommendationEngine(Graph graph) {
        this.graph = graph;
    }

    // ─── Jaccard Similarity Index ─────────────────────────────────────────────

    /**
     * Jaccard(u, v) = |N(u) ∩ N(v)| / |N(u) ∪ N(v)|
     * Normalises by total neighbourhood — eliminates popularity bias.
     * Returns score between 0.0 (no overlap) and 1.0 (identical neighbourhood).
     */
    public double jaccardSimilarity(int u, int v) {
        Set<Integer> neighborsU = new HashSet<>(graph.getNeighbors(u));
        Set<Integer> neighborsV = new HashSet<>(graph.getNeighbors(v));

        if (neighborsU.isEmpty() && neighborsV.isEmpty()) return 0.0;

        // Intersection
        Set<Integer> intersection = new HashSet<>(neighborsU);
        intersection.retainAll(neighborsV);

        // Union
        Set<Integer> union = new HashSet<>(neighborsU);
        union.addAll(neighborsV);

        return (double) intersection.size() / union.size();
    }

    // ─── Triadic Closure ─────────────────────────────────────────────────────

    /**
     * Finds all "open triangles" for a given user.
     * An open triangle = u knows w, w knows v, but u doesn't know v yet.
     * These are the prime candidates for friend recommendations.
     */
    public Set<Integer> getTriadicCandidates(int userId) {
        Set<Integer> candidates = new HashSet<>();
        Set<Integer> directFriends = new HashSet<>(graph.getNeighbors(userId));
        directFriends.add(userId); // exclude self

        for (int friend : graph.getNeighbors(userId)) {
            for (int friendOfFriend : graph.getNeighbors(friend)) {
                if (!directFriends.contains(friendOfFriend)) {
                    candidates.add(friendOfFriend);
                }
            }
        }
        return candidates;
    }

    // ─── Greedy Ranked Recommendations (Priority Queue) ──────────────────────

    /**
     * Returns top-N recommendations using a Max-Heap Priority Queue.
     * Sorted by Jaccard score — O(1) poll after O(C log C) build.
     * This is the Greedy approach: always pick the locally best match.
     */
    public List<Recommendation> getTopRecommendations(int userId, int topN) {
        Set<Integer> candidates = getTriadicCandidates(userId);

        // Max-heap: highest Jaccard score first
        PriorityQueue<Recommendation> maxHeap = new PriorityQueue<>(
                (a, b) -> Double.compare(b.getScore(), a.getScore())
        );

        for (int candidate : candidates) {
            double score = jaccardSimilarity(userId, candidate);
            int mutualCount = countMutualFriends(userId, candidate);
            maxHeap.offer(new Recommendation(candidate, graph.getUserName(candidate), score, mutualCount));
        }

        List<Recommendation> results = new ArrayList<>();
        int count = 0;
        while (!maxHeap.isEmpty() && count < topN) {
            results.add(maxHeap.poll());
            count++;
        }
        return results;
    }

    // ─── Mutual Friend Count ─────────────────────────────────────────────────

    public int countMutualFriends(int u, int v) {
        Set<Integer> neighborsU = new HashSet<>(graph.getNeighbors(u));
        Set<Integer> neighborsV = new HashSet<>(graph.getNeighbors(v));
        neighborsU.retainAll(neighborsV);
        return neighborsU.size();
    }
}
