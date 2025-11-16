package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BargeTest {

    @Test
    @DisplayName("Barge tem tamanho 1 e começa a flutuar")
    void bargeSizeAndInitialState() {
        Position p = new Position(3, 3);
        Barge b = new Barge(Compass.EAST, p);

        assertEquals(1, b.getSize());
        assertEquals(1, b.getPositions().size());
        assertTrue(b.stillFloating());
    }

    @Test
    @DisplayName("Barge ocupa apenas a posição inicial")
    void bargeOccupiesOnlyStartPosition() {
        Position p = new Position(3, 3);
        Barge b = new Barge(Compass.EAST, p);

        assertTrue(b.occupies(p));
        assertFalse(b.occupies(new Position(3, 4)));
        assertFalse(b.occupies(new Position(4, 3)));
    }

    @Test
    @DisplayName("Barge afunda com um único tiro certeiro")
    void bargeSinksAfterOneHit() {
        Position p = new Position(3, 3);
        Barge b = new Barge(Compass.EAST, p);

        assertTrue(b.stillFloating());
        b.shoot(p);  // tiro na posição onde está
        assertFalse(b.stillFloating());
    }
}
