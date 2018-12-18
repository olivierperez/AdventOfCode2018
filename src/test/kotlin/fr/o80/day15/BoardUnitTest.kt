package fr.o80.day15

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class BoardUnitTest {

    @Test
    @DisplayName("Board should hold entities and give them back")
    fun shouldHoldEntities() {
        // Given
        val board = Board(
                listOf(
                        Goblin(2, 1),
                        Elf(5, 4),
                        Elf(5, 1),
                        Wall(0, 0)
                ),
                Point(0, 0))

        // When
        val entities = board.entities()

        // Then
        assertEquals(5, entities[2].x, "Last entities should be the more down of the three")
        assertEquals(4, entities[2].y, "Last entities should be the more down of the three")
        assertEquals(2, entities[0].x, "The first entity should be the one on top-left")
        assertEquals(1, entities[0].y, "The first entity should be the one on top-left")
    }

    @Test
    @DisplayName("Board should give entities for a given type")
    fun shouldProvideEntitiesByType() {
        // Given
        val board = Board(
                listOf(
                        Goblin(2, 1),
                        Elf(5, 4),
                        Elf(5, 1),
                        Wall(0, 0)
                ),
                Point(0, 0))

        // When
        val elves = board.enemiesOf(Goblin(2, 1))

        // Then
        assertEquals(2, elves.size, "Elves are only elves")
        assertTrue(elves[0] is Elf, "Elves are enemies of goblins")
        assertTrue(elves[1] is Elf, "Elves are enemies of goblins")
    }

    @Test
    @DisplayName("Board should load map")
    fun shouldLoadMap() {
        // Given
        val entities = listOf(
                Goblin(0, 0),
                Elf(1, 1),
                Wall(2, 2))

        // When
        val board = Board(entities, Point(0, 0))

        // Then
        assertTrue(board.isFree(5, 4), "An empty block should be free")
        assertFalse(board.isFree(0, 0), "A goblin should not be free")
        assertFalse(board.isFree(1, 1), "An elf should not be free")
        assertFalse(board.isFree(2, 2), "A wall should not be free")
    }

    @Test
    @DisplayName("Board find shortest path")
    fun shouldFindShortestPath() {
        // Given
        val map = """
            |#######
            |#.....#
            |#.....#
            |#.#.#.#
            |#...#.#
            |#...#.#
            |#######
        """.trimMargin()
        val mapReader = MapReader(map)
        val entities = mapReader.entities()
        val board = Board(entities, mapReader.maxPoint)

        // When
        val firstStep = board.nextStep(
                from = Point(1, 3),
                to = Point(5, 5))
        val secondStep = board.nextStep(
                from = firstStep,
                to = Point(5, 5))
        val thirdStep = board.nextStep(
                from = secondStep,
                to = Point(5, 5))

        // Then
        assertEquals(Point(1, 2), firstStep, "First step should be [1;2]")
        assertEquals(Point(2, 2), secondStep, "Second step should be [2;2]")
        assertEquals(Point(3, 2), thirdStep, "Third step should be [3;2]")
    }
}