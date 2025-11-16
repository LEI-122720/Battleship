package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para Carrack (barco de tamanho 3)")
class CarrackTest {

    @Test
    @DisplayName("Carrack tem tamanho 3 e três posições consecutivas na horizontal (EAST)")
    void carrackSizeAndHorizontalPositions() {
        Position start = new Position(5, 2);
        Carrack carrack = new Carrack(Compass.EAST, start);

        assertEquals(3, carrack.getSize());
        assertEquals(3, carrack.getPositions().size());

        Position p1 = start;               // (5,2)
        Position p2 = new Position(5, 3);  // (5,3)
        Position p3 = new Position(5, 4);  // (5,4)

        assertTrue(carrack.occupies(p1));
        assertTrue(carrack.occupies(p2));
        assertTrue(carrack.occupies(p3));
        assertFalse(carrack.occupies(new Position(5, 5)));
    }

    @Test
    @DisplayName("Carrack tem três posições consecutivas na vertical (SOUTH)")
    void carrackVerticalPositions() {
        Position start = new Position(2, 2);
        Carrack carrack = new Carrack(Compass.SOUTH, start);

        Position p1 = start;               // (2,2)
        Position p2 = new Position(3, 2);  // (3,2)
        Position p3 = new Position(4, 2);  // (4,2)

        assertTrue(carrack.occupies(p1));
        assertTrue(carrack.occupies(p2));
        assertTrue(carrack.occupies(p3));
        assertFalse(carrack.occupies(new Position(5, 2)));
    }

    @Test
    @DisplayName("Carrack com bearing NORTH ocupa três posições verticais")
    void carrackNorthVerticalPositions() {
        Position start = new Position(1, 1);
        Carrack carrack = new Carrack(Compass.NORTH, start);

        assertTrue(carrack.occupies(new Position(1, 1)));
        assertTrue(carrack.occupies(new Position(2, 1)));
        assertTrue(carrack.occupies(new Position(3, 1)));
        assertFalse(carrack.occupies(new Position(4, 1)));
    }

    @Test
    @DisplayName("Carrack com bearing WEST ocupa três posições horizontais")
    void carrackWestHorizontalPositions() {
        Position start = new Position(4, 4);
        Carrack carrack = new Carrack(Compass.WEST, start);

        assertTrue(carrack.occupies(new Position(4, 4)));
        assertTrue(carrack.occupies(new Position(4, 5)));
        assertTrue(carrack.occupies(new Position(4, 6)));
        assertFalse(carrack.occupies(new Position(4, 7)));
    }

    @Test
    @DisplayName("Carrack só afunda depois de 3 tiros certeiros")
    void carrackSinksAfterThreeHits() {
        Position start = new Position(5, 2);
        Carrack carrack = new Carrack(Compass.EAST, start);

        Position p1 = start;
        Position p2 = new Position(5, 3);
        Position p3 = new Position(5, 4);

        carrack.shoot(p1);
        carrack.shoot(p2);
        assertTrue(carrack.stillFloating(), "Com 2 tiros ainda deve flutuar");

        carrack.shoot(p3);
        assertFalse(carrack.stillFloating(), "Com 3 tiros deve afundar");
    }

    @Test
    @DisplayName("Carrack com bearing null lança exceção")
    void carrackNullBearingThrows() {
        Position start = new Position(1, 1);

        // Tal como na Caravel, o construtor acaba por lançar AssertionError em Ship.<init>
        assertThrows(AssertionError.class,
                () -> new Carrack(null, start));
    }
}
