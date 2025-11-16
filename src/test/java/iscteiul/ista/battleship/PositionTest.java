package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para a classe Position")
class PositionTest {

    private Position pos;

    @BeforeEach
    void setUp() {
        // Posição base para a maioria dos testes
        pos = new Position(5, 10);
    }

    @Test
    @DisplayName("Construtor define coordenadas e estado inicial")
    void testConstructorAndInitialState() {
        assertEquals(5, pos.getRow());
        assertEquals(10, pos.getColumn());

        // Testa o estado inicial (flags)
        assertFalse(pos.isOccupied(), "Nova Posição não deve estar ocupada");
        assertFalse(pos.isHit(), "Nova Posição não deve estar atingida");
    }

    @Test
    @DisplayName("Método occupy() e isOccupied()")
    void testOccupy() {
        assertFalse(pos.isOccupied());
        pos.occupy();
        assertTrue(pos.isOccupied());
    }

    @Test
    @DisplayName("Método shoot() e isHit()")
    void testShoot() {
        assertFalse(pos.isHit());
        pos.shoot();
        assertTrue(pos.isHit());
    }

    @Test
    @DisplayName("Método toString()")
    void testToString() {
        assertEquals("Linha = 5 Coluna = 10", pos.toString());
    }

    @Nested
    @DisplayName("Testes para equals()")
    class EqualsTests {

        @Test
        @DisplayName("equals() retorna true para a mesma instância")
        void testEquals_SameInstance() {
            assertEquals(pos, pos); // Ramo: this == otherPosition
        }

        @Test
        @DisplayName("equals() retorna true para objetos com mesmas coordenadas")
        void testEquals_SameCoordinates() {
            Position other = new Position(5, 10);
            assertEquals(pos, other);
            assertEquals(other, pos);
        }

        @Test
        @DisplayName("equals() retorna false para coordenadas diferentes (linha)")
        void testEquals_DifferentRow() {
            Position other = new Position(99, 10);
            assertNotEquals(pos, other);
        }

        @Test
        @DisplayName("equals() retorna false para coordenadas diferentes (coluna)")
        void testEquals_DifferentColumn() {
            Position other = new Position(5, 99);
            assertNotEquals(pos, other);
        }

        @Test
        @DisplayName("equals() retorna false ao comparar com null")
        void testEquals_Null() {
            assertNotEquals(null, pos); // Cobre o 'instanceof'
        }

        @Test
        @DisplayName("equals() retorna false ao comparar com outro tipo")
        void testEquals_DifferentType() {
            // Cobre o ramo 'else' do 'instanceof IPosition'
            assertFalse(pos.equals(new String("Posição 5, 10")));
        }
    }

    @Nested
    @DisplayName("Testes para isAdjacentTo()")
    class AdjacentTests {
        // Posição central (5, 10)

        @Test
        @DisplayName("isAdjacentTo() retorna true para a própria posição")
        void testAdjacent_Self() {
            // A lógica (Math.abs(...) <= 1) inclui a diferença de 0
            assertTrue(pos.isAdjacentTo(new Position(5, 10)));
        }

        @Test
        @DisplayName("isAdjacentTo() retorna true para posições cardeais (N,S,E,O)")
        void testAdjacent_Cardinal() {
            assertTrue(pos.isAdjacentTo(new Position(4, 10)), "Adjacente Norte");
            assertTrue(pos.isAdjacentTo(new Position(6, 10)), "Adjacente Sul");
            assertTrue(pos.isAdjacentTo(new Position(5, 9)), "Adjacente Oeste");
            assertTrue(pos.isAdjacentTo(new Position(5, 11)), "Adjacente Este");
        }

        @Test
        @DisplayName("isAdjacentTo() retorna true para posições diagonais")
        void testAdjacent_Diagonal() {
            assertTrue(pos.isAdjacentTo(new Position(4, 9)), "Adjacente Noroeste");
            assertTrue(pos.isAdjacentTo(new Position(4, 11)), "Adjacente Nordeste");
            assertTrue(pos.isAdjacentTo(new Position(6, 9)), "Adjacente Sudoeste");
            assertTrue(pos.isAdjacentTo(new Position(6, 11)), "Adjacente Sudeste");
        }

        @Test
        @DisplayName("isAdjacentTo() retorna false para posições distantes")
        void testAdjacent_False() {
            // Linha longe
            assertFalse(pos.isAdjacentTo(new Position(7, 10)), "2 casas Sul");
            assertFalse(pos.isAdjacentTo(new Position(3, 10)), "2 casas Norte");

            // Coluna longe
            assertFalse(pos.isAdjacentTo(new Position(5, 12)), "2 casas Este");
            assertFalse(pos.isAdjacentTo(new Position(5, 8)), "2 casas Oeste");

            // Diagonal longe
            assertFalse(pos.isAdjacentTo(new Position(7, 12)), "Diagonal longe");

            // Longe
            assertFalse(pos.isAdjacentTo(new Position(99, 99)), "Muito longe");
        }
    }

    @Nested
    @DisplayName("Testes para hashCode()")
    class HashCodeTests {

        @Test
        @DisplayName("hashCode() é consistente para objetos iguais")
        void testHashCode_Consistent() {
            Position other = new Position(5, 10);
            // Garante que 'equals' é verdadeiro
            assertEquals(pos, other);
            // Garante que hashCodes são iguais
            assertEquals(pos.hashCode(), other.hashCode());
        }

        @Test
        @DisplayName("hashCode() muda se 'isHit' mudar (baseado na implementação)")
        void testHashCode_ChangesOnHit() {
            Position other = new Position(5, 10);

            // Hashcodes são iguais
            int hash1 = pos.hashCode();
            assertEquals(hash1, other.hashCode());

            // Muda o estado de 'other'
            other.shoot(); // isHit = true
            int hash2 = other.hashCode();

            // 'equals' ainda é true (pois só usa row/col)
            assertEquals(pos, other);

            // 'hashCode' mudou (pois usa isHit)
            assertNotEquals(hash1, hash2, "hashCode() deve mudar se isHit mudar");
        }

        @Test
        @DisplayName("hashCode() muda se 'isOccupied' mudar (baseado na implementação)")
        void testHashCode_ChangesOnOccupy() {
            Position other = new Position(5, 10);

            int hash1 = pos.hashCode();
            assertEquals(hash1, other.hashCode());

            // Muda o estado de 'other'
            other.occupy(); // isOccupied = true
            int hash2 = other.hashCode();

            assertEquals(pos, other);
            assertNotEquals(hash1, hash2, "hashCode() deve mudar se isOccupied mudar");
        }
    }
}