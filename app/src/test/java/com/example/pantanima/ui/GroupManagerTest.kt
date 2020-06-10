package com.example.pantanima.ui

import com.example.pantanima.ui.models.Group
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.*


class GroupManagerTest {

    private var groupManager: GroupManager? = null

    @Before
    fun setUp() {
        val list = arrayListOf("Strikers", "Alpha", "Anonymous")
        groupManager = GroupManager(list)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getGroups() {
        val groups = groupManager?.groups
        val message = "after creating GroupManager obj, groups list must prepared automatically!"
        assertThat(message, groups, notNullValue())
        assertThat(groups?.size, equalTo(3))
        assertThat(groups, hasItem(Group("Strikers")))
        assertThat(groups?.get(2)?.name, equalTo("Anonymous"))
    }

    @Test
    fun incAnsweredCount() {
        groupManager?.incAnsweredCount()
        assertThat(groupManager?.groups?.get(0)?.roundAnsweredCount, equalTo(1))
    }

    @Test
    fun decAnsweredCount() {
        groupManager?.decAnsweredCount()
        assertThat(groupManager?.groups?.get(0)?.roundAnsweredCount, equalTo(-1))
    }

    @Test
    fun switchGroup() {
        groupManager?.incAnsweredCount()
        groupManager?.switchGroup()
        groupManager?.incAnsweredCount()
        assertEquals(groupManager?.groups?.get(0)?.statistics?.size, 1)
        assertEquals(groupManager?.groups?.get(0)?.statistics?.get(0), 1)
        assertEquals(groupManager?.groups?.get(0)?.roundAnsweredCount, 0)
        assertEquals(groupManager?.groups?.get(1)?.roundAnsweredCount, 1)
    }

    @Test
    fun resetState() {
        groupManager?.incAnsweredCount()
        groupManager?.resetState()
        assertThat(groupManager?.groups?.size, equalTo(3))
        assertThat(groupManager?.groups?.get(0)?.roundAnsweredCount, equalTo(0))
    }
}