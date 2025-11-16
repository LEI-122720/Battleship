package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes globais para Fleet, organizados com @Nested
 * para a Parte D (cobertura global).
 */
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

    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("Quando a frota é nova")
    class NewFleetTests {

        @Test
        @DisplayName("Fleet é criada vazia")
        void newFleetIsEmpty() {
            assertTrue(fleet.getShips().isEmpty(), "Nova frota deve começar sem navios");
            assertTrue(fleet.getFloatingShips().isEmpty(), "Nova frota não deve ter navios a flutuar");
        }
    }

    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("Adicionar navios à frota")
    class AddShipTests {

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
    }

    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("Consultas sobre a frota")
    class QueryTests {

        @Test
        @DisplayName("getShipsLike devolve apenas navios da categoria pedida")
        void getShipsLikeReturnsOnlyCategory() {
            IShip barge = newBarge(1, 1);                  // categoria "Barca"
            IShip caravel = newCaravel(Compass.EAST, 3, 3); // categoria "Caravela"

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
    }

    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("Métodos de impressão da frota")
    class PrintingTests {

        @Test
        @DisplayName("Métodos de impressão executam sem lançar exceções")
        void printMethodsRunWithoutExceptions() {
            fleet.addShip(newBarge(1, 1));
            fleet.addShip(newCaravel(Compass.SOUTH, 3, 3));
            fleet.addShip(newCarrack(Compass.EAST, 5, 5));

            // estes métodos apenas escrevem para System.out; chamá-los já cobre as linhas
            fleet.printAllShips();
            fleet.printFloatingShips();
            fleet.printShipsByCategory("Barca");
            fleet.printStatus();
        }
    }
}
