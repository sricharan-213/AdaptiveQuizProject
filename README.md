==> Adaptive Interactive Quiz System <==

=>The Adaptive Interactive Quiz System is a powerful, JavaFX-based learning platform designed to transform ordinary quizzes into a dynamic, time-tracked, analytics-driven learning experience.
Every feature in this project is built to help learners understand not just what they got wrong, but why — and how to improve.

=>Why This Project Matters

=>Most quiz tools simply show right/wrong answers.
This system goes much further:
It tracks time, accuracy, weak topics, performance trends, and even provides improvement suggestions.
It is built to be extended into a fully AI-powered evaluation tool.

Core Features (Highlights)
1. Text-Based Quiz Import

The system supports uploading your own question files.
This makes it highly flexible for:

Exams

Mock tests
Practice sets
College assignments

Format supported:
Question text
A) Option 1
B) Option 2
C) Option 3
D) Option 4
ANSWER: A
TOPIC: TopicName

2. Smart Question Navigation

A clean navigation panel lets users jump to any question.
Each question preserves:

Saved answer

Time spent

State (answered / not answered)

This gives a professional test-like experience.

3. Dual Timer System

Two independent timers are always running:

Global Timer:
Tracks entire quiz duration — useful for judging speed and pacing.

Per-Question Timer:
Tracks how long you spend on each question.
Pauses when you move away and resumes when you return.

This level of detail makes analytics extremely powerful.

4. Fair Attempt Mode

Correct answers are not shown during the quiz.
This prevents accidental bias and maintains test integrity.
Feedback appears only after submission.

5. Rich Post-Quiz Analytics

After finishing, users get:

Total Score

Accuracy Percentage

Time-per-question chart

Weak topic identification

Personalized improvement suggestions

This turns a regular quiz into a learning insight dashboard.

6. Secure User Accounts

Each user gets:

Personal quiz history

Performance tracking

Saved analytics

Progress trends

Everything is stored and retrieved locally.

7. Leaderboard (Optional)

The system can rank users based on:

Total score

Quizzes attempted

Average performance

Great for group learning, competitions, and training programs.

Project Structure (Clean Overview)
src/
├── main/            (JavaFX app entry)
├── ui/              (User interface screens)
├── quiz/            (Core quiz engine)
├── parser/          (Question & answer parsers)
├── analytics/       (Performance calculations)
├── leaderboard/     (Ranking logic)
├── user/            (User accounts, sessions)
└── utils/           (Common helpers)


User data stored under resources/users/ is intentionally excluded from GitHub.

Requirements

Java 17 or higher

JavaFX SDK installed

Compilation Command
javac --module-path "<path-to-javafx>/lib" --add-modules javafx.controls,javafx.fxml ^
  -d out ^
  (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })

Run Command
java --module-path "<path-to-javafx>/lib" --add-modules javafx.controls,javafx.fxml ^
  -cp out main.App
