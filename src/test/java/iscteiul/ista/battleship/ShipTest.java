package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Global coverage tests for generic Ship behaviour")
class ShipTest {

    @Nested
    @DisplayName("Floating and sinking behaviour")
    class FloatingTests {

        private Ship ship;

        @BeforeEach
        void setup() {
            ship = new Frigate(Compass.EAST, new Position(1, 1));
        }

        @Test
        @DisplayName("Ship starts floating")
        void startsFloating() {
            assertTrue(ship.stillFloating());
        }

        @Test
        @DisplayName("Ship stops floating after all positions are hit")
        void sinksAfterAllHits() {
            for (IPosition p : ship.getPositions()) {
                ship.shoot(p);
            }
            assertFalse(ship.stillFloating());
        }

        @Test
        @DisplayName("Ship basic getters (category, bearing, position, positions, toString)")
        void basicGettersWork() {
            assertEquals("Fragata", ship.getCategory());
            assertEquals(Compass.EAST, ship.getBearing());
            assertEquals(new Position(1, 1), ship.getPosition());
            assertEquals(4, ship.getPositions().size());
            assertNotNull(ship.toString());
        }
    }


    @Nested
    @DisplayName("Proximity rules (tooCloseTo)")
    class ProximityTests {

        private Ship mainShip;

        @BeforeEach
        void setup() {
            mainShip = new Barge(Compass.EAST, new Position(3, 3));
        }

        @Test
        @DisplayName("Ships that are adjacent are considered too close")
        void shipsTooClose() {
            Ship other = new Barge(Compass.EAST, new Position(3, 4)); // posição adjacente
            assertTrue(mainShip.tooCloseTo(other));
        }

        @Test
        @DisplayName("Ships far apart are not considered too close")
        void shipsNotTooClose() {
            Ship other = new Barge(Compass.EAST, new Position(10, 10));
            assertFalse(mainShip.tooCloseTo(other));
        }

        @Test
        @DisplayName("Position too close to ship is detected")
        void positionTooClose() {
            IPosition posNear = new Position(3, 2); // ao lado
            assertTrue(mainShip.tooCloseTo(posNear));
        }

        @Test
        @DisplayName("Distant position is not considered too close")
        void positionNotTooClose() {
            IPosition posFar = new Position(0, 0);
            assertFalse(mainShip.tooCloseTo(posFar));
        }
    }

    @Nested
    @DisplayName("Factory method buildShip")
    class BuildShipTests {

        private final Position start = new Position(0, 0);

        @Test
        @DisplayName("buildShip creates Barge for 'barca'")
        void buildBarge() {
            Ship s = Ship.buildShip("barca", Compass.EAST, start);
            assertNotNull(s);
            assertTrue(s instanceof Barge);
            assertEquals(1, s.getSize());
        }

        @Test
        @DisplayName("buildShip creates Caravel for 'caravela'")
        void buildCaravel() {
            Ship s = Ship.buildShip("caravela", Compass.NORTH, start);
            assertNotNull(s);
            assertTrue(s instanceof Caravel);
            assertEquals(2, s.getSize());
        }

        @Test
        @DisplayName("buildShip creates Carrack for 'nau'")
        void buildCarrack() {
            Ship s = Ship.buildShip("nau", Compass.SOUTH, start);
            assertNotNull(s);
            assertTrue(s instanceof Carrack);
            assertEquals(3, s.getSize());
        }

        @Test
        @DisplayName("buildShip creates Frigate for 'fragata'")
        void buildFrigate() {
            Ship s = Ship.buildShip("fragata", Compass.WEST, start);
            assertNotNull(s);
            assertTrue(s instanceof Frigate);
            assertEquals(4, s.getSize());
        }

        @Test
        @DisplayName("buildShip creates Galleon for 'galeao'")
        void buildGalleon() {
            Ship s = Ship.buildShip("galeao", Compass.EAST, start);
            assertNotNull(s);
            assertTrue(s instanceof Galleon);
            assertEquals(5, s.getSize());
        }

        @Test
        @DisplayName("buildShip returns null for invalid kind")
        void buildInvalidReturnsNull() {
            Ship s = Ship.buildShip("INVALIDO", Compass.EAST, start);
            assertNull(s);
        }
    }

}
