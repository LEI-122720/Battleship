package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para Caravel (barco de tamanho 2)")
class CaravelTest {

    // Helper para criar posições
    private Position pos(int row, int col) {
        return new Position(row, col);
    }

    // Helper para criar caravela
    private Caravel newCaravel(Compass bearing, int row, int col) {
        return new Caravel(bearing, pos(row, col));
    }

    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("Posicionamento da Caravel")
    class PositionTests {

        @Test
        @DisplayName("Caravel tem tamanho 2 e duas posições consecutivas na horizontal (EAST)")
        void caravelSizeAndHorizontalPositions() {
            Position start = pos(4, 4);
            Caravel c = newCaravel(Compass.EAST, 4, 4);

            assertEquals(2, c.getSize());
            assertEquals(2, c.getPositions().size());

            Position p1 = start;          // (4,4)
            Position p2 = pos(4, 5);      // (4,5)

            assertTrue(c.occupies(p1));
            assertTrue(c.occupies(p2));
        }

        @Test
        @DisplayName("Caravel tem duas posições consecutivas na vertical (SOUTH)")
        void caravelVerticalPositionsSouth() {
            Position start = pos(2, 2);
            Caravel c = newCaravel(Compass.SOUTH, 2, 2);

            Position p1 = start;          // (2,2)
            Position p2 = pos(3, 2);      // (3,2)

            assertTrue(c.occupies(p1));
            assertTrue(c.occupies(p2));
            assertFalse(c.occupies(pos(4, 2)));
        }

        @Test
        @DisplayName("Caravel com bearing NORTH também fica na vertical")
        void caravelNorthVertical() {
            Position start = pos(1, 1);
            Caravel c = newCaravel(Compass.NORTH, 1, 1);

            Position p1 = start;          // (1,1)
            Position p2 = pos(2, 1);      // (2,1)

            assertTrue(c.occupies(p1));
            assertTrue(c.occupies(p2));
            assertFalse(c.occupies(pos(3, 1)));
        }

        @Test
        @DisplayName("Caravel com bearing WEST ocupa duas posições na horizontal")
        void caravelWestHorizontal() {
            Position start = pos(3, 3);
            Caravel c = newCaravel(Compass.WEST, 3, 3);

            Position p1 = start;          // (3,3)
            Position p2 = pos(3, 4);      // (3,4) – mesma lógica que EAST

            assertTrue(c.occupies(p1));
            assertTrue(c.occupies(p2));
            assertFalse(c.occupies(pos(3, 5)));
        }
    }

    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("Tiros e estado da Caravel")
    class ShootingTests {

        @Test
        @DisplayName("Caravel só afunda depois de 2 tiros nas posições certas")
        void caravelSinksAfterTwoHits() {
            Position start = pos(4, 4);
            Caravel c = newCaravel(Compass.EAST, 4, 4);

            Position p1 = start;
            Position p2 = pos(4, 5);

            c.shoot(p1);
            assertTrue(c.stillFloating(), "Com 1 tiro ainda deve flutuar");

            c.shoot(p2);
            assertFalse(c.stillFloating(), "Com 2 tiros deve afundar");
        }
    }

    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("Validação de argumentos")
    class ValidationTests {

        @Test
        @DisplayName("Caravel lança uma exceção se bearing for null")
        void caravelNullBearingThrows() {
            Position start = pos(1, 1);

            // O código real acaba por lançar AssertionError em Ship.<init>
            assertThrows(AssertionError.class,
                    () -> new Caravel(null, start));
        }
    }
}
