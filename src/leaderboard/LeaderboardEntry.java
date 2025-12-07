package leaderboard;

import user.User;

public class LeaderboardEntry implements Comparable<LeaderboardEntry> {

    private final String username;
    private final int totalScore;
    private final int quizzesTaken;
    private final double averageScore;

    public LeaderboardEntry(User user) {
        this.username = user.getUsername();
        this.totalScore = user.getTotalScore();
        this.quizzesTaken = user.getTotalQuizzes();
        this.averageScore = user.getAverageScore();
    }

    public String getUsername() {
        return username;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getQuizzesTaken() {
        return quizzesTaken;
    }

    public double getAverageScore() {
        return averageScore;
    }

    @Override
    public int compareTo(LeaderboardEntry o) {
        int byScore = Integer.compare(o.totalScore, this.totalScore);
        if (byScore != 0) return byScore;
        int byQuizzes = Integer.compare(o.quizzesTaken, this.quizzesTaken);
        if (byQuizzes != 0) return byQuizzes;
        return Double.compare(o.averageScore, this.averageScore);
    }
}


