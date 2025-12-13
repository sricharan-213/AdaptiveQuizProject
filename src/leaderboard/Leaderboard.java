package leaderboard;

import user.User;
import user.UserStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Leaderboard {

    // Store all entries unsorted (subject-aware entries, but not globally sorted)
    private final List<LeaderboardEntry> entries;

    public Leaderboard() {
        List<User> users = UserStorage.loadAllUsers();
        List<LeaderboardEntry> tmp = new ArrayList<>();
        for (User u : users) {
            // each history entry becomes a leaderboard entry (subject-aware)
            for (User.QuizHistoryEntry h : u.getHistory()) {
                tmp.add(new LeaderboardEntry(
                        u.getUsername(),
                        h.getSubject(),
                        h.getScore(),
                        h.getTotalQuestions(),
                        h.getPercentage(),
                        h.getTotalTimeSeconds(),
                        h.getDateTimeIso()
                ));
            }
        }
        // DO NOT sort globally - entries from different subjects should not be compared
        this.entries = tmp;
    }

    /**
     * Get all entries (unsorted, may contain multiple subjects)
     * For subject-specific leaderboard, use getEntriesForSubject() instead
     */
    public List<LeaderboardEntry> getEntries() {
        return entries;
    }

    /**
     * Get sorted entries for a specific subject only.
     * Ranking: percentage DESC, time ASC, date ASC (older first)
     * This ensures entries from different subjects are NEVER compared.
     * 
     * @deprecated Use getEntriesForSubject(String, LeaderboardMode) instead
     */
    @Deprecated
    public List<LeaderboardEntry> getEntriesForSubject(String subject) {
        return getEntriesForSubject(subject, LeaderboardMode.BEST_ATTEMPT);
    }

    /**
     * Get sorted entries for a specific subject with a specific mode.
     * 
     * @param subject The subject name
     * @param mode FIRST_ATTEMPT (only first attempt per user) or BEST_ATTEMPT (best attempt per user)
     * @return Sorted list of leaderboard entries for the subject
     */
    public List<LeaderboardEntry> getEntriesForSubject(String subject, LeaderboardMode mode) {
        if (subject == null) {
            return new ArrayList<>();
        }
        
        // First, filter by subject
        List<LeaderboardEntry> filtered = new ArrayList<>();
        for (LeaderboardEntry e : entries) {
            if (subject.equals(e.getSubject())) {
                filtered.add(e);
            }
        }
        
        if (filtered.isEmpty()) {
            return filtered;
        }
        
        // Apply mode-specific filtering
        if (mode == LeaderboardMode.FIRST_ATTEMPT) {
            // Group by username, keep only first attempt (earliest timestamp)
            java.util.Map<String, LeaderboardEntry> firstAttempts = new java.util.HashMap<>();
            for (LeaderboardEntry e : filtered) {
                String username = e.getUsername();
                if (!firstAttempts.containsKey(username)) {
                    firstAttempts.put(username, e);
                } else {
                    LeaderboardEntry existing = firstAttempts.get(username);
                    // Compare timestamps - keep the earlier one
                    try {
                        java.time.Instant existingTime = java.time.Instant.parse(existing.getDateTimeIso());
                        java.time.Instant currentTime = java.time.Instant.parse(e.getDateTimeIso());
                        if (currentTime.isBefore(existingTime)) {
                            firstAttempts.put(username, e);
                        }
                    } catch (Exception ex) {
                        // If parsing fails, keep existing
                    }
                }
            }
            filtered = new ArrayList<>(firstAttempts.values());
        } else if (mode == LeaderboardMode.BEST_ATTEMPT) {
            // Group by username, keep only best attempt
            java.util.Map<String, LeaderboardEntry> bestAttempts = new java.util.HashMap<>();
            for (LeaderboardEntry e : filtered) {
                String username = e.getUsername();
                if (!bestAttempts.containsKey(username)) {
                    bestAttempts.put(username, e);
                } else {
                    LeaderboardEntry existing = bestAttempts.get(username);
                    // Compare: percentage DESC, then time ASC, then timestamp ASC
                    int cmp = Double.compare(e.getPercentage(), existing.getPercentage());
                    if (cmp > 0) {
                        // Current has higher percentage
                        bestAttempts.put(username, e);
                    } else if (cmp == 0) {
                        // Same percentage, compare time
                        cmp = Long.compare(e.getTotalTimeSeconds(), existing.getTotalTimeSeconds());
                        if (cmp < 0) {
                            // Current has less time
                            bestAttempts.put(username, e);
                        } else if (cmp == 0) {
                            // Same time, compare timestamp (earlier is better)
                            try {
                                java.time.Instant existingTime = java.time.Instant.parse(existing.getDateTimeIso());
                                java.time.Instant currentTime = java.time.Instant.parse(e.getDateTimeIso());
                                if (currentTime.isBefore(existingTime)) {
                                    bestAttempts.put(username, e);
                                }
                            } catch (Exception ex) {
                                // If parsing fails, keep existing
                            }
                        }
                    }
                }
            }
            filtered = new ArrayList<>(bestAttempts.values());
        }
        
        // Sort the filtered entries: percentage DESC, time ASC, date ASC
        Collections.sort(filtered);
        return filtered;
    }

    /**
     * Get all unique subjects from all entries
     */
    public Set<String> getAllSubjects() {
        Set<String> subjects = new TreeSet<>();
        for (LeaderboardEntry e : entries) {
            subjects.add(e.getSubject());
        }
        return subjects;
    }

    /**
     * Get rank for a user in a specific subject with a specific mode
     */
    public int getRankForUserInSubject(String username, String subject, LeaderboardMode mode) {
        List<LeaderboardEntry> subjectEntries = getEntriesForSubject(subject, mode);
        for (int i = 0; i < subjectEntries.size(); i++) {
            if (subjectEntries.get(i).getUsername().equals(username)) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Get rank for a user in a specific subject (defaults to BEST_ATTEMPT mode)
     * @deprecated Use getRankForUserInSubject(String, String, LeaderboardMode) instead
     */
    @Deprecated
    public int getRankForUserInSubject(String username, String subject) {
        return getRankForUserInSubject(username, subject, LeaderboardMode.BEST_ATTEMPT);
    }

    // Deprecated: use getRankForUserInSubject instead
    @Deprecated
    public int getRankForUser(String username) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getUsername().equals(username)) {
                return i + 1;
            }
        }
        return -1;
    }

    // Deprecated: subject-aware percentile requires subject parameter
    @Deprecated
    public double getPercentileForUser(String username) {
        int rank = getRankForUser(username);
        if (rank <= 0 || entries.isEmpty()) {
            return 0;
        }
        double better = entries.size() - rank;
        return 100.0 * better / entries.size();
    }
}


