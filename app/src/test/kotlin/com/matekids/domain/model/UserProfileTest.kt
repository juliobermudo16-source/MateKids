package com.matekids.domain.model

import org.junit.Test
import kotlin.test.assertEquals

class UserProfileTest {

    @Test
    fun testNextLevelXPCalculation() {
        val profile = UserProfile(level = 1)
        assertEquals(50L, profile.getNextLevelXP())

        val profile2 = UserProfile(level = 5)
        assertEquals(250L, profile2.getNextLevelXP())

        val profile3 = UserProfile(level = 20)
        assertEquals(1000L, profile3.getNextLevelXP())
    }

    @Test
    fun testProgressToNextLevel() {
        // Level 1: 0-50 XP
        val profile1 = UserProfile(totalXP = 25, level = 1)
        assertEquals(0.5f, profile1.getProgressToNextLevel())

        // Level 2: 50-100 XP
        val profile2 = UserProfile(totalXP = 75, level = 2)
        assertEquals(0.5f, profile2.getProgressToNextLevel())

        // At max level
        val profile3 = UserProfile(totalXP = 1000, level = 20)
        assertEquals(1f, profile3.getProgressToNextLevel())
    }

    @Test
    fun testXPToNextLevel() {
        val profile = UserProfile(totalXP = 25, level = 1)
        assertEquals(25L, profile.getXPToNextLevel())

        val profile2 = UserProfile(totalXP = 50, level = 2)
        assertEquals(50L, profile2.getXPToNextLevel())
    }
}
