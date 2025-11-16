package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para Barge (barco de tamanho 1)")
class BargeTest {

    private Position start;
    private Barge barge;

    @BeforeEach
    void setUp() {
        start = new Position(3, 3);
        barge = new Barge(Compass.EAST, start);
    }

    @Nested
    @DisplayName("Estado inicial da Barge")
    class InitialStateTests {

        @Test
        @DisplayName("Barge tem tamanho 1 e começa a flutuar")
        void bargeSizeAndInitialState() {
            assertEquals(1, barge.getSize());
            assertEquals(1, barge.getPositions().size());
            assertTrue(barge.stillFloating());
        }
    }

    @Nested
    @DisplayName("Ocupação de posições")
    class OccupationTests {

        @Test
        @DisplayName("Barge ocupa apenas a posição inicial")
        void bargeOccupiesOnlyStartPosition() {
            assertTrue(barge.occupies(start));
            assertFalse(barge.occupies(new Position(3, 4)));
            assertFalse(barge.occupies(new Position(4, 3)));
        }
    }

    @Nested
    @DisplayName("Tiros na Barge")
    class ShootingTests {

        @Test
        @DisplayName("Barge afunda com um único tiro certeiro")
        void bargeSinksAfterOneHit() {
            assertTrue(barge.stillFloating());
            barge.shoot(start);  // tiro na posição onde está
            assertFalse(barge.stillFloating());
        }

        @Test
        @DisplayName("Tiro numa posição errada não afunda a Barge")
        void missDoesNotSinkBarge() {
            Position miss = new Position(3, 4);

            barge.shoot(miss);
            assertTrue(barge.stillFloating(), "Um tiro falhado não deve afundar a barca");
        }
    }
}
