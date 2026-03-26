package com.socialgraph.core;

import java.util.*;

public class Graph {
    private HashMap<Integer, List<Integer>> adjList;
    private HashMap<Integer, UserNode> users;

    public Graph() {
        this.adjList = new HashMap<>();
        this.users = new HashMap<>();
    }

    public void addUser(int id, String name) {
        users.put(id, new UserNode(id, name));
        adjList.putIfAbsent(id, new ArrayList<>());
    }

    public void addConnection(int u, int v) {
        adjList.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        adjList.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
    }

    public boolean hasConnection(int u, int v) {
        return adjList.getOrDefault(u, Collections.emptyList()).contains(v);
    }

    public List<Integer> getNeighbors(int userId) {
        return adjList.getOrDefault(userId, Collections.emptyList());
    }

    public HashMap<Integer, UserNode> getUsers() { return users; }
    public HashMap<Integer, List<Integer>> getAdjList() { return adjList; }
    public Set<Integer> getAllUserIds() { return adjList.keySet(); }

    public List<Integer> bfsShortestPath(int source, int target) {
        if (!adjList.containsKey(source) || !adjList.containsKey(target)) return null;
        Queue<Integer> queue = new LinkedList<>();
        HashMap<Integer, Integer> parent = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        queue.add(source); visited.add(source); parent.put(source, -1);
        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (current == target) {
                LinkedList<Integer> path = new LinkedList<>();
                int curr = target;
                while (curr != -1) { path.addFirst(curr); curr = parent.get(curr); }
                return path;
            }
            for (int neighbor : adjList.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor); parent.put(neighbor, current); queue.add(neighbor);
                }
            }
        }
        return null;
    }

    public List<Integer> dfsReachable(int source) {
        List<Integer> visited = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();
        dfsHelper(source, seen, visited);
        return visited;
    }

    private void dfsHelper(int node, Set<Integer> seen, List<Integer> visited) {
        seen.add(node); visited.add(node);
        for (int neighbor : adjList.getOrDefault(node, Collections.emptyList())) {
            if (!seen.contains(neighbor)) dfsHelper(neighbor, seen, visited);
        }
    }

    public int getVertexCount() { return adjList.size(); }
    public int getEdgeCount() { return adjList.values().stream().mapToInt(List::size).sum() / 2; }
    public String getUserName(int id) { UserNode u = users.get(id); return u != null ? u.getName() : "Unknown#" + id; }
}
