-- Sample data for MateKids database

-- Initialize User Profile
INSERT OR IGNORE INTO user_profiles (id, avatar, alias, totalXP, level, operationsResolved, problemsResolved, accuracyRate, currentStreak, lastActivityDate)
VALUES (1, 'avatar_1', 'Ingeniero Beta', 0, 1, 0, 0, 0.0, 0, CURRENT_TIMESTAMP);

-- Initialize Achievements
INSERT OR IGNORE INTO achievements (type, isUnlocked, unlockedAt) VALUES ('MACHINE_SUMADORA', 0, NULL);
INSERT OR IGNORE INTO achievements (type, isUnlocked, unlockedAt) VALUES ('MACHINE_RESTADORA', 0, NULL);
INSERT OR IGNORE INTO achievements (type, isUnlocked, unlockedAt) VALUES ('MACHINE_MULTIPLICADORA', 0, NULL);
INSERT OR IGNORE INTO achievements (type, isUnlocked, unlockedAt) VALUES ('MACHINE_DIVISORA', 0, NULL);
INSERT OR IGNORE INTO achievements (type, isUnlocked, unlockedAt) VALUES ('MENTAL_MATH', 0, NULL);
INSERT OR IGNORE INTO achievements (type, isUnlocked, unlockedAt) VALUES ('PROBLEM_SOLVER', 0, NULL);
INSERT OR IGNORE INTO achievements (type, isUnlocked, unlockedAt) VALUES ('CHALLENGE_MASTER', 0, NULL);
INSERT OR IGNORE INTO achievements (type, isUnlocked, unlockedAt) VALUES ('GENIUS', 0, NULL);

-- Sample Operations (Suma)
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('SUM', 2, 3, 5, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('SUM', 5, 4, 9, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('SUM', 10, 15, 25, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('SUM', 7, 8, 15, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('SUM', 12, 13, 25, 0, 0, CURRENT_TIMESTAMP);

-- Sample Operations (Resta)
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('SUBTRACT', 10, 3, 7, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('SUBTRACT', 20, 8, 12, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('SUBTRACT', 15, 5, 10, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('SUBTRACT', 25, 10, 15, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('SUBTRACT', 30, 12, 18, 0, 0, CURRENT_TIMESTAMP);

-- Sample Operations (Multiplicación)
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('MULTIPLY', 2, 3, 6, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('MULTIPLY', 5, 4, 20, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('MULTIPLY', 6, 7, 42, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('MULTIPLY', 8, 3, 24, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('MULTIPLY', 9, 2, 18, 0, 0, CURRENT_TIMESTAMP);

-- Sample Operations (División)
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('DIVIDE', 10, 2, 5, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('DIVIDE', 20, 4, 5, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('DIVIDE', 15, 3, 5, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('DIVIDE', 24, 6, 4, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO operations (type, operand1, operand2, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('DIVIDE', 30, 5, 6, 0, 0, CURRENT_TIMESTAMP);

-- Sample Problems
INSERT INTO problems (description, difficulty, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('El reactor necesita 3 cajas de energía. Si cada caja tiene 5 unidades, ¿cuántas unidades hay en total?', 1, 15, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO problems (description, difficulty, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('Tenías 50 piezas. Usaste 12 en la máquina Sumadora. ¿Cuántas piezas te quedan?', 1, 38, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO problems (description, difficulty, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('La fábrica produce 8 máquinas por día. ¿Cuántas máquinas produce en 7 días?', 2, 56, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO problems (description, difficulty, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('Tienes 100 circuitos. 25 son defectuosos. ¿Cuántos circuitos funcionan correctamente?', 1, 75, 0, 0, CURRENT_TIMESTAMP);
INSERT INTO problems (description, difficulty, correctAnswer, isCorrect, xpEarned, timestamp)
VALUES ('Si una máquina consume 12 unidades de energía por hora y funciona 6 horas, ¿cuánta energía consume en total?', 2, 72, 0, 0, CURRENT_TIMESTAMP);
