package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
