package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testes para a classe Compass")
class CompassTest {

    @Test
    @DisplayName("Testa getDirection para todas as constantes")
    void testGetDirection() {
        assertEquals('n', Compass.NORTH.getDirection());
        assertEquals('s', Compass.SOUTH.getDirection());
        assertEquals('e', Compass.EAST.getDirection());
        assertEquals('o', Compass.WEST.getDirection());
        assertEquals('u', Compass.UNKNOWN.getDirection());
    }

    @Test
    @DisplayName("Testa toString para todas as constantes")
    void testToString() {
        assertEquals("n", Compass.NORTH.toString());
        assertEquals("s", Compass.SOUTH.toString());
        assertEquals("e", Compass.EAST.toString());
        assertEquals("o", Compass.WEST.toString());
        assertEquals("u", Compass.UNKNOWN.toString());
    }

    @Test
    @DisplayName("Testa charToCompass para todos os casos válidos")
    void testCharToCompass_ValidChars() {
        assertEquals(Compass.NORTH, Compass.charToCompass('n'));
        assertEquals(Compass.SOUTH, Compass.charToCompass('s'));
        assertEquals(Compass.EAST, Compass.charToCompass('e'));
        assertEquals(Compass.WEST, Compass.charToCompass('o'));
    }

    @Test
    @DisplayName("Testa charToCompass para o caso 'default' (inválido)")
    void testCharToCompass_InvalidChar() {
        // Testa um char que não corresponde a nenhum caso
        assertEquals(Compass.UNKNOWN, Compass.charToCompass('x'));
        // Testa o próprio char 'u' (que também cai no default)
        assertEquals(Compass.UNKNOWN, Compass.charToCompass('u'));
        // Testa um char em maiúscula
        assertEquals(Compass.UNKNOWN, Compass.charToCompass('N'));
    }
}