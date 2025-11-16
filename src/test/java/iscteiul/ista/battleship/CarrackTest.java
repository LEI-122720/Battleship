package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para Carrack (barco de tamanho 3)")
class CarrackTest {

    // Helpers
    private Position pos(int row, int col) {
        return new Position(row, col);
    }

    private Carrack newCarrack(Compass bearing, int row, int col) {
        return new Carrack(bearing, pos(row, col));
    }

    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("Posicionamento da Carrack")
    class PositionTests {

        @Test
        @DisplayName("Carrack tem tamanho 3 e três posições consecutivas na horizontal (EAST)")
        void carrackSizeAndHorizontalPositions() {
            Position start = pos(5, 2);
            Carrack carrack = newCarrack(Compass.EAST, 5, 2);

            assertEquals(3, carrack.getSize());
            assertEquals(3, carrack.getPositions().size());

            Position p1 = start;          // (5,2)
            Position p2 = pos(5, 3);      // (5,3)
            Position p3 = pos(5, 4);      // (5,4)

            assertTrue(carrack.occupies(p1));
            assertTrue(carrack.occupies(p2));
            assertTrue(carrack.occupies(p3));
            assertFalse(carrack.occupies(pos(5, 5)));
        }

        @Test
        @DisplayName("Carrack tem três posições consecutivas na vertical (SOUTH)")
        void carrackVerticalPositionsSouth() {
            Position start = pos(2, 2);
            Carrack carrack = newCarrack(Compass.SOUTH, 2, 2);

            Position p1 = start;          // (2,2)
            Position p2 = pos(3, 2);      // (3,2)
            Position p3 = pos(4, 2);      // (4,2)

            assertTrue(carrack.occupies(p1));
            assertTrue(carrack.occupies(p2));
            assertTrue(carrack.occupies(p3));
            assertFalse(carrack.occupies(pos(5, 2)));
        }

        @Test
        @DisplayName("Carrack com bearing NORTH ocupa três posições verticais")
        void carrackNorthVerticalPositions() {
            Position start = pos(1, 1);
            Carrack carrack = newCarrack(Compass.NORTH, 1, 1);

            assertTrue(carrack.occupies(pos(1, 1)));
            assertTrue(carrack.occupies(pos(2, 1)));
            assertTrue(carrack.occupies(pos(3, 1)));
            assertFalse(carrack.occupies(pos(4, 1)));
        }

        @Test
        @DisplayName("Carrack com bearing WEST ocupa três posições horizontais")
        void carrackWestHorizontalPositions() {
            Position start = pos(4, 4);
            Carrack carrack = newCarrack(Compass.WEST, 4, 4);

            assertTrue(carrack.occupies(pos(4, 4)));
            assertTrue(carrack.occupies(pos(4, 5)));
            assertTrue(carrack.occupies(pos(4, 6)));
            assertFalse(carrack.occupies(pos(4, 7)));
        }
    }

    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("Tiros e estado da Carrack")
    class ShootingTests {

        @Test
        @DisplayName("Carrack só afunda depois de 3 tiros certeiros")
        void carrackSinksAfterThreeHits() {
            Position start = pos(5, 2);
            Carrack carrack = newCarrack(Compass.EAST, 5, 2);

            Position p1 = start;
            Position p2 = pos(5, 3);
            Position p3 = pos(5, 4);

            carrack.shoot(p1);
            carrack.shoot(p2);
            assertTrue(carrack.stillFloating(), "Com 2 tiros ainda deve flutuar");

            carrack.shoot(p3);
            assertFalse(carrack.stillFloating(), "Com 3 tiros deve afundar");
        }
    }

    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("Validação de argumentos")
    class ValidationTests {

        @Test
        @DisplayName("Carrack com bearing null lança exceção")
        void carrackNullBearingThrows() {
            Position start = pos(1, 1);

            // Tal como na Caravel, o construtor acaba por lançar AssertionError em Ship.<init>
            assertThrows(AssertionError.class,
                    () -> new Carrack(null, start));
        }
    }
}
