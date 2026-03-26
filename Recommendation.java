package com.socialgraph.recommendation;

public class Recommendation {
    private int userId;
    private String userName;
    private double score;
    private int mutualFriends;

    public Recommendation(int userId, String userName, double score, int mutualFriends) {
        this.userId = userId;
        this.userName = userName;
        this.score = score;
        this.mutualFriends = mutualFriends;
    }

    public int getUserId() { return userId; }
    public String getUserName() { return userName; }
    public double getScore() { return score; }
    public int getMutualFriends() { return mutualFriends; }

    @Override
    public String toString() {
        return String.format("Recommend: %s (id=%d) | Jaccard=%.4f | Mutual=%d", userName, userId, score, mutualFriends);
    }
}
