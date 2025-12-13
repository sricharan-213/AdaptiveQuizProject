package user;

import exceptions.UserNotFoundException;
import quiz.Quiz;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {

    private static final UserManager INSTANCE = new UserManager();

    private final Map<String, User> userCache = new HashMap<>();
    private User currentUser;

    private UserManager() {
    }

    public static UserManager getInstance() {
        return INSTANCE;
    }

    public void loadAllUsers() {
        List<User> users = UserStorage.loadAllUsers();
        userCache.clear();
        for (User u : users) {
            userCache.put(u.getUsername(), u);
        }
    }

    public boolean userExists(String username) {
        return userCache.containsKey(username) || UserStorage.loadUser(username) != null;
    }

    public User signup(String username, String password, String email) {
        if (userExists(username)) {
            return null;
        }
        Student user = new Student(username, password, email);
        userCache.put(username, user);
        UserStorage.saveUser(user);
        return user;
    }

    public User login(String username, String password) throws UserNotFoundException {
        User user = userCache.get(username);
        if (user == null) {
            user = UserStorage.loadUser(username);
            if (user != null) {
                userCache.put(username, user);
            }
        }
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        if (!user.getPassword().equals(password)) {
            throw new UserNotFoundException("Invalid password");
        }
        currentUser = user;
        return user;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Map<String, User> getUserCache() {
        return userCache;
    }

    /**
     * Record quiz result with full quiz data for replay capability
     */
    public void recordQuizResult(Quiz quiz, int score, int totalQuestions, long totalTimeSeconds) {
        if (currentUser == null || quiz == null) {
            return;
        }
        String now = Instant.now().toString();
        String subject = quiz.getSubject() != null ? quiz.getSubject() : "General";
        double pct = (totalQuestions == 0) ? 0.0 : (100.0 * score / totalQuestions);
        // Serialize quiz data for replay
        String questionData = QuizHistorySerializer.serializeQuiz(quiz);
        currentUser.addHistory(new User.QuizHistoryEntry(now, subject, score, totalQuestions, pct, totalTimeSeconds, questionData));
        UserStorage.saveUser(currentUser);
    }

    /**
     * Record quiz result without detailed question data (backward compatibility)
     * @deprecated Use recordQuizResult(Quiz, int, int, long) instead
     */
    @Deprecated
    public void recordQuizResult(String subject, int score, int totalQuestions, long totalTimeSeconds) {
        if (currentUser == null) {
            return;
        }
        String now = Instant.now().toString();
        double pct = (totalQuestions == 0) ? 0.0 : (100.0 * score / totalQuestions);
        currentUser.addHistory(new User.QuizHistoryEntry(now, subject, score, totalQuestions, pct, totalTimeSeconds));
        UserStorage.saveUser(currentUser);
    }
}


