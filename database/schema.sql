-- MateKids Database Schema

-- User Profile Table
CREATE TABLE IF NOT EXISTS user_profiles (
    id INTEGER PRIMARY KEY,
    avatar TEXT NOT NULL DEFAULT 'avatar_1',
    alias TEXT NOT NULL DEFAULT 'Ingeniero',
    totalXP INTEGER NOT NULL DEFAULT 0,
    level INTEGER NOT NULL DEFAULT 1,
    operationsResolved INTEGER NOT NULL DEFAULT 0,
    problemsResolved INTEGER NOT NULL DEFAULT 0,
    accuracyRate REAL NOT NULL DEFAULT 0.0,
    currentStreak INTEGER NOT NULL DEFAULT 0,
    lastActivityDate INTEGER NOT NULL
);

-- Operations Table
CREATE TABLE IF NOT EXISTS operations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL,
    operand1 INTEGER NOT NULL,
    operand2 INTEGER NOT NULL,
    correctAnswer INTEGER NOT NULL,
    userAnswer INTEGER,
    isCorrect INTEGER NOT NULL DEFAULT 0,
    xpEarned INTEGER NOT NULL DEFAULT 0,
    timestamp INTEGER NOT NULL
);

-- Problems Table
CREATE TABLE IF NOT EXISTS problems (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    description TEXT NOT NULL,
    difficulty INTEGER NOT NULL,
    correctAnswer INTEGER NOT NULL,
    userAnswer INTEGER,
    isCorrect INTEGER NOT NULL DEFAULT 0,
    xpEarned INTEGER NOT NULL DEFAULT 0,
    timestamp INTEGER NOT NULL
);

-- Achievements Table
CREATE TABLE IF NOT EXISTS achievements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL UNIQUE,
    unlockedAt INTEGER,
    isUnlocked INTEGER NOT NULL DEFAULT 0
);

-- Indexes for better performance
CREATE INDEX IF NOT EXISTS idx_operations_type ON operations(type);
CREATE INDEX IF NOT EXISTS idx_operations_timestamp ON operations(timestamp);
CREATE INDEX IF NOT EXISTS idx_operations_isCorrect ON operations(isCorrect);
CREATE INDEX IF NOT EXISTS idx_problems_difficulty ON problems(difficulty);
CREATE INDEX IF NOT EXISTS idx_problems_timestamp ON problems(timestamp);
CREATE INDEX IF NOT EXISTS idx_problems_isCorrect ON problems(isCorrect);
CREATE INDEX IF NOT EXISTS idx_achievements_isUnlocked ON achievements(isUnlocked);
