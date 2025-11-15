package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for Frigate (size 4)")
public class FrigateTest {

    @Test
    @DisplayName("Frigate size is 4")
    void testSize() {
        Frigate f = new Frigate(Compass.NORTH, new Position(5, 5));
        assertEquals(4, f.getSize());
    }

    @Test
    @DisplayName("Frigate NORTH generates 4 vertical positions")
    void testNorthPositions() {
        Position start = new Position(3, 4);
        Frigate f = new Frigate(Compass.NORTH, start);

        assertEquals(4, f.getPositions().size());
        assertEquals(new Position(3, 4), f.getPositions().get(0));
        assertEquals(new Position(4, 4), f.getPositions().get(1));
        assertEquals(new Position(5, 4), f.getPositions().get(2));
        assertEquals(new Position(6, 4), f.getPositions().get(3));
    }

    @Test
    @DisplayName("Frigate EAST generates 4 horizontal positions")
    void testEastPositions() {
        Position start = new Position(1, 1);
        Frigate f = new Frigate(Compass.EAST, start);

        assertEquals(4, f.getPositions().size());
        assertEquals(new Position(1, 1), f.getPositions().get(0));
        assertEquals(new Position(1, 2), f.getPositions().get(1));
        assertEquals(new Position(1, 3), f.getPositions().get(2));
        assertEquals(new Position(1, 4), f.getPositions().get(3));
    }

    @Test
    @DisplayName("Frigate sinks after all hits")
    void testSink() {
        Frigate f = new Frigate(Compass.WEST, new Position(2, 2));

        for (IPosition p : f.getPositions()) {
            f.shoot(p);
        }

        assertFalse(f.stillFloating());
    }

    @Test
    @DisplayName("Frigate is floating if at least 1 position is not hit")
    void testStillFloating() {
        Frigate f = new Frigate(Compass.WEST, new Position(2, 2));

        f.shoot(f.getPositions().get(0));

        assertTrue(f.stillFloating());
    }
}
