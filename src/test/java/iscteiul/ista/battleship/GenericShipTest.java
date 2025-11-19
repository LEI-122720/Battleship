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
    @Nested
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
    @Nested
    @DisplayName("Testes para Fleet (frota de navios)")
    class FleetTest {

        private Fleet fleet;

        @BeforeEach
        void setUp() {
            fleet = new Fleet();
        }

        // Helpers para criar navios simples dentro do tabuleiro
        private Barge newBarge(int row, int col) {
            return new Barge(Compass.EAST, new Position(row, col));
        }

        private Caravel newCaravel(Compass bearing, int row, int col) {
            return new Caravel(bearing, new Position(row, col));
        }

        private Carrack newCarrack(Compass bearing, int row, int col) {
            return new Carrack(bearing, new Position(row, col));
        }

        @Test
        @DisplayName("Fleet é criada vazia")
        void newFleetIsEmpty() {
            assertTrue(fleet.getShips().isEmpty(), "Nova frota deve começar sem navios");
            assertTrue(fleet.getFloatingShips().isEmpty(), "Nova frota não deve ter navios a flutuar");
        }

        @Test
        @DisplayName("addShip adiciona um navio válido à frota")
        void addShipAddsValidShip() {
            IShip barge = newBarge(1, 1);

            boolean added = fleet.addShip(barge);

            assertTrue(added, "Barco válido deve ser adicionado");
            assertEquals(1, fleet.getShips().size());
            assertSame(barge, fleet.getShips().get(0));
        }

        @Test
        @DisplayName("addShip rejeita navio fora do tabuleiro")
        void addShipRejectsShipOutsideBoard() {
            // Supondo BOARD_SIZE do IFleet (tabuleiro 0..BOARD_SIZE-1)
            int boardSize = IFleet.BOARD_SIZE;

            // Caravela tamanho 2: se começar em (0, boardSize) sai fora pela direita
            IShip outside = newCaravel(Compass.EAST, 0, boardSize);

            boolean added = fleet.addShip(outside);

            assertFalse(added, "Navio fora do tabuleiro não deve ser adicionado");
            assertTrue(fleet.getShips().isEmpty(), "Frota deve continuar vazia");
        }

        @Test
        @DisplayName("addShip rejeita navio em colisão com outro")
        void addShipRejectsCollidingShip() {
            IShip first = newCaravel(Compass.EAST, 3, 3);
            IShip colliding = newCaravel(Compass.EAST, 3, 3); // ocupa as mesmas posições

            assertTrue(fleet.addShip(first), "Primeiro navio deve ser adicionado");
            boolean addedSecond = fleet.addShip(colliding);

            assertFalse(addedSecond, "Navio em colisão não deve ser adicionado");
            assertEquals(1, fleet.getShips().size(), "Só deve existir o primeiro navio");
        }

        @Test
        @DisplayName("getShipsLike devolve apenas navios da categoria pedida")
        void getShipsLikeReturnsOnlyCategory() {
            IShip barge = newBarge(1, 1);                          // categoria "Barca"
            IShip caravel = newCaravel(Compass.EAST, 3, 3);         // categoria "Caravela"

            fleet.addShip(barge);
            fleet.addShip(caravel);

            List<IShip> barges = fleet.getShipsLike("Barca");

            assertTrue(barges.contains(barge), "Deve conter a barca");
            assertFalse(barges.contains(caravel), "Não deve conter a caravela");
        }

        @Test
        @DisplayName("getFloatingShips devolve apenas navios ainda a flutuar")
        void getFloatingShipsReturnsOnlyFloatingShips() {
            IShip barge = newBarge(1, 1);
            IShip carrack = newCarrack(Compass.EAST, 4, 4);

            fleet.addShip(barge);
            fleet.addShip(carrack);

            // Afundar a carrack: dar tiros em todas as posições ocupadas
            for (IPosition p : carrack.getPositions()) {
                carrack.shoot(p);
            }

            List<IShip> floating = fleet.getFloatingShips();

            assertTrue(floating.contains(barge), "Barca ainda deve estar a flutuar");
            assertFalse(floating.contains(carrack), "Carrack afundada não deve aparecer como flutuante");
        }

        @Test
        @DisplayName("shipAt devolve o navio na posição indicada ou null se não houver")
        void shipAtReturnsShipOrNull() {
            IShip barge = newBarge(2, 2);
            fleet.addShip(barge);

            IShip found = fleet.shipAt(new Position(2, 2));
            IShip notFound = fleet.shipAt(new Position(0, 0));

            assertSame(barge, found, "Deve devolver o navio na posição ocupada");
            assertNull(notFound, "Deve devolver null quando não há navio nessa posição");
        }

        @Test
        @DisplayName("Métodos de impressão de estado executam sem lançar exceções")
        void printMethodsRunWithoutExceptions() {
            // Adiciona alguns navios
            fleet.addShip(newBarge(1, 1));
            fleet.addShip(newCaravel(Compass.SOUTH, 3, 3));
            fleet.addShip(newCarrack(Compass.EAST, 5, 5));

            // Estes métodos só escrevem para System.out; chamá-los já cobre as linhas
            fleet.printAllShips();
            fleet.printFloatingShips();
            fleet.printShipsByCategory("Barca");
            fleet.printStatus();
        }
    }
    @Nested
    @DisplayName("Testes para Game (lógica global do jogo)")
    class GameTest {

        private Fleet fleet;
        private Game game;
        private Barge barge;
        private Caravel caravel;

        @BeforeEach
        void setUp() {
            // Criar uma frota simples com dois navios
            fleet = new Fleet();

            barge = new Barge(Compass.EAST, new Position(0, 0));       // tamanho 1
            caravel = new Caravel(Compass.EAST, new Position(2, 2));   // tamanho 2: (2,2) e (2,3)

            assertTrue(fleet.addShip(barge));
            assertTrue(fleet.addShip(caravel));

            game = new Game(fleet);
        }

        // Helper
        private Position pos(int row, int col) {
            return new Position(row, col);
        }

        // -------------------------------------------------------------------------
        @Nested
        @DisplayName("Estado inicial do jogo")
        class InitialStateTests {

            @Test
            @DisplayName("Novo jogo começa sem tiros e sem erros nos contadores simples")
            void newGameBasicState() {
                assertTrue(game.getShots().isEmpty(), "Não deve haver tiros no início");
                assertEquals(0, game.getInvalidShots());
                assertEquals(0, game.getRepeatedShots());
                // navios restantes deve ser igual ao número de navios da frota
                assertEquals(fleet.getShips().size(), game.getRemainingShips());
            }

            @Test
            @DisplayName("getHits e getSunkShips lançam NullPointerException (contadores não inicializados)")
            void hitsAndSinksCountersAreNullInitially() {
                assertThrows(NullPointerException.class,
                        () -> { int h = game.getHits(); },
                        "Devido a countHits não inicializado, getHits provoca NullPointerException");

                assertThrows(NullPointerException.class,
                        () -> { int s = game.getSunkShips(); },
                        "Devido a countSinks não inicializado, getSunkShips provoca NullPointerException");
            }
        }

        // -------------------------------------------------------------------------
        @Nested
        @DisplayName("Disparos inválidos e repetidos")
        class InvalidAndRepeatedShotsTests {

            @Test
            @DisplayName("Tiro fora do tabuleiro é contado como inválido")
            void invalidShotIncrementsInvalidCounter() {
                Position outOfBoard = pos(-1, 0); // linha negativa => tiro inválido

                IShip result = game.fire(outOfBoard);

                assertNull(result, "Tiro inválido não deve acertar em navio nenhum");
                assertEquals(1, game.getInvalidShots(), "Deve aumentar o contador de tiros inválidos");
                assertEquals(0, game.getRepeatedShots());
                assertTrue(game.getShots().isEmpty(), "Tiro inválido não deve ser registado na lista de shots");
            }

            @Test
            @DisplayName("Tiro repetido é contado em repeatedShots mas não repete o efeito")
            void repeatedShotIncrementsRepeatedCounter() {
                Position water = pos(Fleet.BOARD_SIZE - 1, Fleet.BOARD_SIZE - 1); // presumimos água

                // Primeiro tiro (água)
                IShip first = game.fire(water);
                // Segundo tiro, mesma posição
                IShip second = game.fire(water);

                assertNull(first, "Primeiro tiro em água não acerta em nenhum navio");
                assertNull(second, "Segundo tiro repetido também não deve acertar em navio");

                assertEquals(0, game.getInvalidShots());
                assertEquals(1, game.getRepeatedShots(), "Deve contar 1 tiro repetido");
                assertEquals(1, game.getShots().size(),
                        "A posição só deve aparecer uma vez na lista de shots");
            }
        }

        // -------------------------------------------------------------------------
        @Nested
        @DisplayName("Disparos em água e em navios")
        class ShotsOnWaterAndShipsTests {

            @Test
            @DisplayName("Tiro em água não altera o número de navios restantes")
            void shotOnWaterDoesNotChangeRemainingShips() {
                int before = game.getRemainingShips();

                Position water = pos(Fleet.BOARD_SIZE - 1, Fleet.BOARD_SIZE - 1);
                IShip result = game.fire(water);

                assertNull(result);
                assertEquals(before, game.getRemainingShips(), "Tiro em água não deve afundar navios");
                // não chamamos getHits/getSunkShips aqui para evitar o NPE
            }

            @Test
            @DisplayName("Tiro que acerta num navio atualmente lança NullPointerException (bug nos contadores)")
            void hitOnShipCurrentlyThrowsNullPointerException() {
                Position firstCaravelPos = pos(2, 2); // caravel (2,2) e (2,3)

                assertThrows(NullPointerException.class,
                        () -> game.fire(firstCaravelPos),
                        "Devido aos contadores não inicializados, um acerto provoca NullPointerException");
            }
        }

        // -------------------------------------------------------------------------
        @Nested
        @DisplayName("Impressão do tabuleiro")
        class PrintingTests {

            @Test
            @DisplayName("printValidShots e printFleet executam sem lançar exceções")
            void printingMethodsRunWithoutExceptions() {
                // preparar alguns tiros válidos APENAS EM ÁGUA
                game.fire(pos(Fleet.BOARD_SIZE - 1, Fleet.BOARD_SIZE - 1));
                game.fire(pos(Fleet.BOARD_SIZE - 2, Fleet.BOARD_SIZE - 2));

                // estes métodos apenas escrevem para a consola; chamá-los já cobre as linhas
                game.printValidShots();
                game.printFleet();
            }
        }
    }
    @Nested
    @DisplayName("Tests for Frigate (size 4)")
    public class FrigateTest {

        @Test
        @DisplayName("Frigate size is 4")
        void testSize() {
            Frigate f = new Frigate(Compass.NORTH, new Position(5, 5));
            assertEquals(4, f.getSize());
        }

        @Test
        @DisplayName("Frigate NORTH generates 4 vertical positions")
        void testNorthPositions() {
            Position start = new Position(3, 4);
            Frigate f = new Frigate(Compass.NORTH, start);

            assertEquals(4, f.getPositions().size());
            assertEquals(new Position(3, 4), f.getPositions().get(0));
            assertEquals(new Position(4, 4), f.getPositions().get(1));
            assertEquals(new Position(5, 4), f.getPositions().get(2));
            assertEquals(new Position(6, 4), f.getPositions().get(3));
        }

        @Test
        @DisplayName("Frigate EAST generates 4 horizontal positions")
        void testEastPositions() {
            Position start = new Position(1, 1);
            Frigate f = new Frigate(Compass.EAST, start);

            assertEquals(4, f.getPositions().size());
            assertEquals(new Position(1, 1), f.getPositions().get(0));
            assertEquals(new Position(1, 2), f.getPositions().get(1));
            assertEquals(new Position(1, 3), f.getPositions().get(2));
            assertEquals(new Position(1, 4), f.getPositions().get(3));
        }

        @Test
        @DisplayName("Frigate sinks after all hits")
        void testSink() {
            Frigate f = new Frigate(Compass.WEST, new Position(2, 2));

            for (IPosition p : f.getPositions()) {
                f.shoot(p);
            }

            assertFalse(f.stillFloating());
        }

        @Test
        @DisplayName("Frigate is floating if at least 1 position is not hit")
        void testStillFloating() {
            Frigate f = new Frigate(Compass.WEST, new Position(2, 2));

            f.shoot(f.getPositions().get(0));

            assertTrue(f.stillFloating());
        }
    }
    @Nested
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
    @Nested
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
}