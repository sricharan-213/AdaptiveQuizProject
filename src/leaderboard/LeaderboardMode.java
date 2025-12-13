package leaderboard;

/**
 * Enum for leaderboard display modes
 */
public enum LeaderboardMode {
    /**
     * First Attempt mode: Shows only the first attempt of each user for a subject
     * Each username appears exactly once
     */
    FIRST_ATTEMPT,
    
    /**
     * Best Attempt mode: Shows the best attempt of each user for a subject
     * Best = highest percentage, then least time, then earliest timestamp
     * Each username appears exactly once
     */
    BEST_ATTEMPT
}

