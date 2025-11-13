package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for Galleon (size 5)")
public class GalleonTest {

    @Test
    @DisplayName("Galleon size is 5")
    void testSize() {
        Galleon g = new Galleon(Compass.NORTH, new Position(0, 0));
        assertEquals(5, g.getSize());
    }

    @Test
    @DisplayName("Galleon NORTH generates correct positions")
    void testNorthPositions() {
        Position p = new Position(2, 2);
        Galleon g = new Galleon(Compass.NORTH, p);

        assertEquals(5, g.getPositions().size());

        assertEquals(new Position(2, 2), g.getPositions().get(0));
        assertEquals(new Position(2, 3), g.getPositions().get(1));
        assertEquals(new Position(2, 4), g.getPositions().get(2));
        assertEquals(new Position(3, 3), g.getPositions().get(3));
        assertEquals(new Position(4, 3), g.getPositions().get(4));
    }

    @Test
    @DisplayName("Galleon SOUTH generates correct positions")
    void testSouthPositions() {
        Position p = new Position(1, 1);
        Galleon g = new Galleon(Compass.SOUTH, p);

        assertEquals(5, g.getPositions().size());

        assertEquals(new Position(1, 1), g.getPositions().get(0));
        assertEquals(new Position(2, 1), g.getPositions().get(1));
        assertEquals(new Position(3, 0), g.getPositions().get(2));
        assertEquals(new Position(3, 1), g.getPositions().get(3));
        assertEquals(new Position(3, 2), g.getPositions().get(4));
    }


    @Test
    @DisplayName("Galleon EAST generates correct positions")
    void testEastPositions() {
        Position p = new Position(5, 5);
        Galleon g = new Galleon(Compass.EAST, p);

        assertEquals(5, g.getPositions().size());

        assertEquals(new Position(5, 5), g.getPositions().get(0));
        assertEquals(new Position(6, 3), g.getPositions().get(1));
        assertEquals(new Position(6, 4), g.getPositions().get(2));
        assertEquals(new Position(6, 5), g.getPositions().get(3));
        assertEquals(new Position(7, 5), g.getPositions().get(4));
    }


    @Test
    @DisplayName("Galleon WEST generates correct positions")
    void testWestPositions() {
        Position p = new Position(3, 3);
        Galleon g = new Galleon(Compass.WEST, p);

        assertEquals(new Position(3, 3), g.getPositions().get(0));
        assertEquals(new Position(4, 3), g.getPositions().get(1));
        assertEquals(new Position(4, 4), g.getPositions().get(2));
        assertEquals(new Position(4, 5), g.getPositions().get(3));
        assertEquals(new Position(5, 3), g.getPositions().get(4));
    }

    @Test
    @DisplayName("Galleon sinks after all hits")
    void testSink() {
        Galleon g = new Galleon(Compass.NORTH, new Position(0, 0));

        for (IPosition p : g.getPositions()) {
            g.shoot(p);
        }

        assertFalse(g.stillFloating());
    }

    @Test
    @DisplayName("Galleon floating if not fully hit")
    void testStillFloating() {
        Galleon g = new Galleon(Compass.NORTH, new Position(0, 0));

        g.shoot(g.getPositions().get(0));

        assertTrue(g.stillFloating());
    }
}
