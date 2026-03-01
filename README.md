# Adaptive Interactive Quiz System

A JavaFX-based desktop application that transforms traditional quizzes into a time-tracked, analytics-driven learning system.

Built to move beyond right/wrong answers by tracking performance patterns, topic weaknesses, and time efficiency.

---

## 🚀 Key Features

- Text-based quiz import (custom question files)
- Dual timer system (global + per-question tracking)
- Smart navigation with answer persistence
- Secure user accounts with quiz history
- Rich post-quiz analytics:
  - Accuracy %
  - Time-per-question insights
  - Weak topic detection
  - Personalized improvement suggestions
- Optional leaderboard system

---

## 🏗 Architecture Overview

Modular structure with clear separation of concerns:

```
src/
├── main/ (App entry point)
├── ui/ (JavaFX screens)
├── quiz/ (Quiz engine)
├── parser/ (Question parsing logic)
├── analytics/ (Performance calculations)
├── leaderboard/ (Ranking system)
├── user/ (User session management)
└── utils/ (Helper utilities)


Designed with scalability in mind — extendable toward AI-based evaluation.
```
---

## 🎥 Demo

 

---

## ⚙️ Tech Stack

- Java 17
- JavaFX
- OOP principles
- File-based persistence
- Modular architecture
