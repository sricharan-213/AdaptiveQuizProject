package leaderboard;

import user.User;
import user.UserStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leaderboard {

    private final List<LeaderboardEntry> entries;

    public Leaderboard() {
        List<User> users = UserStorage.loadAllUsers();
        List<LeaderboardEntry> tmp = new ArrayList<>();
        for (User u : users) {
            tmp.add(new LeaderboardEntry(u));
        }
        Collections.sort(tmp);
        this.entries = tmp;
    }

    public List<LeaderboardEntry> getEntries() {
        return entries;
    }

    public int getRankForUser(String username) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getUsername().equals(username)) {
                return i + 1;
            }
        }
        return -1;
    }

    public double getPercentileForUser(String username) {
        int rank = getRankForUser(username);
        if (rank <= 0 || entries.isEmpty()) {
            return 0;
        }
        double better = entries.size() - rank;
        return 100.0 * better / entries.size();
    }
}


