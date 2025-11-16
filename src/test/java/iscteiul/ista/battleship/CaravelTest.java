package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para Caravel (barco de tamanho 2)")
class CaravelTest {

    @Test
    @DisplayName("Caravel tem tamanho 2 e duas posições consecutivas na horizontal (EAST)")
    void caravelSizeAndHorizontalPositions() {
        Position start = new Position(4, 4);
        Caravel c = new Caravel(Compass.EAST, start);

        assertEquals(2, c.getSize());
        assertEquals(2, c.getPositions().size());

        Position p1 = start;               // (4,4)
        Position p2 = new Position(4, 5);  // (4,5)

        assertTrue(c.occupies(p1));
        assertTrue(c.occupies(p2));
    }

    @Test
    @DisplayName("Caravel tem duas posições consecutivas na vertical (SOUTH)")
    void caravelVerticalPositions() {
        Position start = new Position(2, 2);
        Caravel c = new Caravel(Compass.SOUTH, start);

        Position p1 = start;               // (2,2)
        Position p2 = new Position(3, 2);  // (3,2)

        assertTrue(c.occupies(p1));
        assertTrue(c.occupies(p2));
        assertFalse(c.occupies(new Position(4, 2)));
    }

    @Test
    @DisplayName("Caravel com bearing NORTH também fica na vertical")
    void caravelNorthVertical() {
        Position start = new Position(1, 1);
        Caravel c = new Caravel(Compass.NORTH, start);

        Position p1 = start;               // (1,1)
        Position p2 = new Position(2, 1);  // (2,1)

        assertTrue(c.occupies(p1));
        assertTrue(c.occupies(p2));
        assertFalse(c.occupies(new Position(3, 1)));
    }

    @Test
    @DisplayName("Caravel com bearing WEST ocupa duas posições na horizontal")
    void caravelWestHorizontal() {
        Position start = new Position(3, 3);
        Caravel c = new Caravel(Compass.WEST, start);

        Position p1 = start;               // (3,3)
        Position p2 = new Position(3, 4);  // (3,4)

        assertTrue(c.occupies(p1));
        assertTrue(c.occupies(p2));
        assertFalse(c.occupies(new Position(3, 5)));
    }

    @Test
    @DisplayName("Caravel só afunda depois de 2 tiros nas posições certas")
    void caravelSinksAfterTwoHits() {
        Position start = new Position(4, 4);
        Caravel c = new Caravel(Compass.EAST, start);

        Position p1 = start;
        Position p2 = new Position(4, 5);

        c.shoot(p1);
        assertTrue(c.stillFloating(), "Com 1 tiro ainda deve flutuar");

        c.shoot(p2);
        assertFalse(c.stillFloating(), "Com 2 tiros deve afundar");
    }

    @Test
    @DisplayName("Caravel lança uma exceção se bearing for null")
    void caravelNullBearingThrows() {
        Position start = new Position(1, 1);

        // O código real atira AssertionError no Ship.<init>
        assertThrows(AssertionError.class,
                () -> new Caravel(null, start));
    }
}
