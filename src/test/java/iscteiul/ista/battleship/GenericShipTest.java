package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;
// Importações reais do projeto
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para a classe abstrata genérica Ship")
class GenericShipTest {

    // Usamos Carrack (Nau) como a instância de teste concreta
    private Ship testShip;
    private Position pos1_1;
    private Position pos2_1;
    private Position pos3_1;

    // Constantes do 'switch' da classe Ship
    private static final String GALEAO = "galeao";
    private static final String FRAGATA = "fragata";
    private static final String NAU = "nau";
    private static final String CARAVELA = "caravela";
    private static final String BARCA = "barca";

    /**
     * Configura um navio de teste (Nau/Carrack) com 3 posições:
     * (1,1), (2,1), (3,1)
     */
    @BeforeEach
    void setUp() {
        pos1_1 = new Position(1, 1);
        pos2_1 = new Position(2, 1);
        pos3_1 = new Position(3, 1);

        // Assumimos que Carrack(SOUTH, 1,1) cria um navio em
        // (1,1), (2,1), (3,1) e tem tamanho 3
        testShip = new Carrack(Compass.SOUTH, pos1_1);
    }

    @Test
    @DisplayName("Testa getters básicos e construtor")
    void testConstructorAndGetters() {
        assertEquals("Nau", testShip.getCategory()); // Assumindo que Carrack usa "Nau"
        assertEquals(Compass.SOUTH, testShip.getBearing());
        assertEquals(pos1_1, testShip.getPosition()); // Posição inicial
        assertNotNull(testShip.getPositions());
        assertEquals(3, testShip.getPositions().size());
        assertEquals(3, testShip.getSize());
    }

    @Test
    @DisplayName("Testa representação toString")
    void testToString() {
        String expected = "[Nau s Linha = 1 Coluna = 1]"; // Assumindo categoria "Nau"
        assertEquals(expected, testShip.toString());
    }

    @Test
    @DisplayName("Testa construtor com asserts nulos")
    void testConstructorNullAsserts() {
        // Estes testes requerem que as asserções Java (-ea) estejam ativadas
        assertThrows(AssertionError.class, () ->
                        new Carrack(null, new Position(1, 1))
                , "Construtor deve falhar com bearing nulo");

        assertThrows(AssertionError.class, () ->
                        new Carrack(Compass.NORTH, null)
                , "Construtor deve falhar com posição nula");
    }

    @Nested
    @DisplayName("Testes do método factory buildShip")
    class BuildShipTests {

        @Test
        @DisplayName("Testa buildShip para tipos válidos")
        void testBuildShipValid() {
            Position p = new Position(0, 0);

            // Testa se os tipos válidos criam instâncias não-nulas
            // e da classe correta
            assertTrue(Ship.buildShip(BARCA, Compass.NORTH, p) instanceof Barge);
            assertTrue(Ship.buildShip(CARAVELA, Compass.NORTH, p) instanceof Caravel);
            assertTrue(Ship.buildShip(NAU, Compass.NORTH, p) instanceof Carrack);
            assertTrue(Ship.buildShip(FRAGATA, Compass.NORTH, p) instanceof Frigate);
            assertTrue(Ship.buildShip(GALEAO, Compass.NORTH, p) instanceof Galleon);
        }

        @Test
        @DisplayName("Testa buildShip para tipo inválido (default)")
        void testBuildShipDefault() {
            Ship s = Ship.buildShip("TIPO_INVALIDO", Compass.NORTH, new Position(0, 0));
            assertNull(s, "buildShip deve retornar null para um tipo inválido");
        }
    }


    @Nested
    @DisplayName("Testes de Lógica de Posição")
    class PositionLogicTests {
        @Test
        @DisplayName("Testa cálculo das posições extremas (Vertical)")
        void testExtremePositionsVertical() {
            // Navio está em (1,1), (2,1), (3,1)
            assertEquals(1, testShip.getTopMostPos());
            assertEquals(3, testShip.getBottomMostPos());
            assertEquals(1, testShip.getLeftMostPos());
            assertEquals(1, testShip.getRightMostPos());
        }

        @Test
        @DisplayName("Testa cálculo das posições extremas (Horizontal)")
        void testExtremePositionsHorizontal() {
            // Testa um navio horizontal
            Ship horizontalShip = new Carrack(Compass.EAST, new Position(5, 5));
            // Assumindo que ocupa (5,5), (5,6), (5,7)

            assertEquals(5, horizontalShip.getTopMostPos());
            assertEquals(5, horizontalShip.getBottomMostPos());
            assertEquals(5, horizontalShip.getLeftMostPos());
            assertEquals(7, horizontalShip.getRightMostPos());
        }


        @Test
        @DisplayName("Testa 'occupies' para posições no navio e fora")
        void testOccupies() {
            assertTrue(testShip.occupies(new Position(1, 1)), "Deve ocupar (1,1)");
            assertTrue(testShip.occupies(new Position(2, 1)), "Deve ocupar (2,1)");
            assertTrue(testShip.occupies(new Position(3, 1)), "Deve ocupar (3,1)");
            assertFalse(testShip.occupies(new Position(4, 1)), "Não deve ocupar (4,1)");
            assertFalse(testShip.occupies(new Position(1, 2)), "Não deve ocupar (1,2)");
            assertFalse(testShip.occupies(new Position(9, 9)), "Não deve ocupar (9,9)");
        }

        @Test
        @DisplayName("Testa 'occupies' com null (deve dar AssertionError)")
        void testOccupiesNull() {
            assertThrows(AssertionError.class, () ->
                    testShip.occupies(null)
            );
        }

        @Test
        @DisplayName("Testa getTopMostPos (cobrindo a lógica 'if')")
        void testGetTopMostPosCoversIf() {
            // Assumimos que Carrack(NORTH, 3,1) cria um navio "para cima"
            // Posições na lista: (3,1), (2,1), (1,1)
            Ship ship = new Carrack(Compass.NORTH, new Position(3, 1));

            // Execução:
            // 1. top = get(0).getRow() -> top = 3

            assertEquals(3, ship.getTopMostPos(), "O 'top' deve ser 3");
        }

        @Test
        @DisplayName("Testa getLeftMostPos (cobrindo a lógica 'if')")
        void testGetLeftMostPosCoversIf() {
            // Assumimos que Carrack(WEST, 5,7) cria um navio "para a esquerda"
            // Posições na lista: (5,7), (5,6), (5,5)
            Ship ship = new Carrack(Compass.WEST, new Position(5, 7));

            // Execução:
            // 1. left = get(0).getColumn() -> left = 7


            assertEquals(7, ship.getLeftMostPos(), "O 'left' deve ser 7");
        }

        @Test
        @DisplayName("Testa getBottomMostPos (cobrindo a lógica 'if')")
        void testGetBottomMostPosCoversIf() {
            // Oposto de TopMostPos
            // Assumimos que Carrack(SOUTH, 1,1) cria (1,1), (2,1), (3,1)
            Ship ship = new Carrack(Compass.SOUTH, new Position(1, 1));

            // Execução:
            // 1. bottom = get(0).getRow() -> bottom = 1
            // 2. i=1: get(1).getRow() (2) > 1 -> true. bottom = 2
            // 3. i=2: get(2).getRow() (3) > 2 -> true. bottom = 3
            // 4. Retorna 3

            assertEquals(3, ship.getBottomMostPos(), "O 'bottom' deve ser 3");
        }

        @Test
        @DisplayName("Testa getRightMostPos (cobrindo a lógica 'if')")
        void testGetRightMostPosCoversIf() {
            // Oposto de LeftMostPos
            // Assumimos que Carrack(EAST, 5,5) cria (5,5), (5,6), (5,7)
            Ship ship = new Carrack(Compass.EAST, new Position(5, 5));

            // Execução:
            // 1. right = get(0).getColumn() -> right = 5
            // 2. i=1: get(1).getColumn() (6) > 5 -> true. right = 6
            // 3. i=2: get(2).getColumn() (7) > 6 -> true. right = 7
            // 4. Retorna 7

            assertEquals(7, ship.getRightMostPos(), "O 'right' deve ser 7");
        }
    }


    @Nested
    @DisplayName("Testes de Lógica de Combate")
    class CombatLogicTests {
        @Test
        @DisplayName("Testa lógica 'stillFloating' (intacto, atingido, afundado)")
        void testStillFloatingLogic() {
            // 1. Intacto
            assertTrue(testShip.stillFloating(), "Navio novo deve estar a flutuar");

            // 2. Atingido (1 tiro)
            testShip.shoot(pos1_1);
            assertTrue(testShip.stillFloating(), "Navio parcialmente atingido (1/3) deve flutuar");

            // 3. Atingido (2 tiros)
            testShip.shoot(pos2_1);
            assertTrue(testShip.stillFloating(), "Navio parcialmente atingido (2/3) deve flutuar");

            // 4. Afundado (3 tiros)
            testShip.shoot(pos3_1);
            assertFalse(testShip.stillFloating(), "Navio totalmente atingido (3/3) não deve flutuar");
        }

        @Test
        @DisplayName("Testa 'shoot' numa posição válida")
        void testShootValidPosition() {
            // Posições são (1,1), (2,1), (3,1)
            // A lista interna de 'positions' pode não ter a mesma referência
            // que as nossas variáveis de teste, mas devem ser .equals()

            Position target = new Position(2, 1);

            // Encontra a posição correspondente dentro do navio
            IPosition internalPos = testShip.getPositions().stream()
                    .filter(p -> p.equals(target))
                    .findFirst()
                    .orElse(null);

            assertNotNull(internalPos, "Posição (2,1) deve existir no navio");
            assertFalse(internalPos.isHit(), "Posição (2,1) não deve estar atingida inicialmente");

            testShip.shoot(target);

            assertTrue(internalPos.isHit(), "Posição (2,1) deve estar atingida após disparo");
        }

        @Test
        @DisplayName("Testa 'shoot' numa posição inválida")
        void testShootInvalidPosition() {
            testShip.shoot(new Position(9, 9)); // Dispara para o vazio

            // Verifica se nenhuma das posições reais foi atingida
            for (IPosition pos : testShip.getPositions()) {
                assertFalse(pos.isHit(), "Nenhuma posição deve ser atingida por um tiro falhado");
            }
        }

        @Test
        @DisplayName("Testa 'shoot' com null (deve dar AssertionError)")
        void testShootNull() {
            assertThrows(AssertionError.class, () ->
                    testShip.shoot(null)
            );
        }
    }


    @Nested
    @DisplayName("Testes de Proximidade (tooCloseTo)")
    class ProximityTests {

        @Test
        @DisplayName("Testa 'tooCloseTo(IPosition)' (adjacente, longe, sobreposto)")
        void testTooCloseToPosition() {
            // Navio está em (1,1), (2,1), (3,1)

            // 1. Adjacente (lado) - perto de (1,1)
            assertTrue(testShip.tooCloseTo(new Position(1, 2)), "Deve ser 'too close' a (1,2)");
            // 2. Adjacente (lado) - perto de (2,1)
            assertTrue(testShip.tooCloseTo(new Position(2, 2)), "Deve ser 'too close' a (2,2)");
            // 3. Adjacente (diagonal) - perto de (1,1)
            assertTrue(testShip.tooCloseTo(new Position(0, 0)), "Deve ser 'too close' a (0,0)");
            // 4. Adjacente (diagonal) - perto de (3,1)
            assertTrue(testShip.tooCloseTo(new Position(4, 2)), "Deve ser 'too close' a (4,2)");

            // 5. Longe
            assertFalse(testShip.tooCloseTo(new Position(5, 5)), "Não deve ser 'too close' a (5,5)");
            assertFalse(testShip.tooCloseTo(new Position(1, 3)), "Não deve ser 'too close' a (1,3)"); // 2 casas

            // 6. Sobreposto (uma posição do próprio navio)
            // (2,1) é adjacente a (1,1) e (3,1)
            assertTrue(testShip.tooCloseTo(new Position(2, 1)), "Deve ser 'too close' a (2,1) (adjacente a outras partes)");
        }

        @Test
        @DisplayName("Testa 'tooCloseTo(IShip)' (perto, longe)")
        void testTooCloseToShip() {
            // testShip está em (1,1), (2,1), (3,1)

            // 1. Navio Longe (Barge em 8,8)
            Ship farShip = new Barge(Compass.NORTH, new Position(8, 8));

            assertFalse(testShip.tooCloseTo(farShip), "Navios não devem estar demasiado perto");
            assertFalse(farShip.tooCloseTo(testShip), "Verificação de proximidade deve ser mútua");

            // 2. Navio Perto (Barge em 2,2)
            // (2,2) é adjacente a (1,1), (2,1), (3,1)
            Ship closeShip = new Barge(Compass.NORTH, new Position(2, 2));

            assertTrue(testShip.tooCloseTo(closeShip), "Navios devem estar demasiado perto");
            assertTrue(closeShip.tooCloseTo(testShip), "Verificação de proximidade deve ser mútua");
        }

        @Test
        @DisplayName("Testa 'tooCloseTo(IShip)' com navios sobrepostos")
        void testTooCloseToOverlappingShip() {
            // Navio que ocupa (3,1) (que o testShip também ocupa)
            Ship overlappingShip = new Barge(Compass.NORTH, new Position(3, 1));

            // (3,1) é adjacente a (2,1) (outra parte do testShip)
            assertTrue(testShip.tooCloseTo(overlappingShip), "Navios sobrepostos devem ser 'too close'");
        }

        @Test
        @DisplayName("Testa 'tooCloseTo(IShip)' com null (deve dar AssertionError)")
        void testTooCloseToShipNull() {
            assertThrows(AssertionError.class, () ->
                    testShip.tooCloseTo((IShip) null)
            );
        }
    }
}