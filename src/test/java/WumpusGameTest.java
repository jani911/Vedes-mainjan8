
/*import hu.nye.progtech.WumpusGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Test
public void testFireArrow() {
    // Teszteset adatok inicializálása
    WumpusGame game = new WumpusGame();
    game.setArrowCount(1);
    game.setHeroPosition(1, 1);
    game.setHeroFacing('N');
    game.setGameBoard(new char[][]{
            {'_', '_', '_'},
            {'_', 'W', '_'},
            {'H', 'U', '_'}
    });

    // Tesztelt metódus hívása
    game.fireArrow();

    // Elvárt eredmények ellenőrzése
    assertEquals(0, game.getArrowCount()); // csökkent a nyilvesszok szama
    assertEquals('U', game.getGameBoard()[2][1]); // A Wumpus le lett lőve
}
}

@Test
public void testFireArrowHitWumpus() {
    // Előkészítés: beállítjuk a játék állapotát, ahol a wumpus a lövés irányában van
    game.setArrowCount(1);
    game.setHeroPosition(1, 1);
    game.setHeroFacing('N');
    game.setGameBoard(new char[][]{
            {'_', 'U', '_'},
            {'_', 'H', '_'},
            {'_', '_', '_'}
    });

    // Tesztelt metódus hívása
    game.fireArrow();

    // Elvárt eredmények ellenőrzése
    assertEquals(0, game.getArrowCount()); // Az íj elhasználódott
    assertEquals('_', game.getGameBoard()[0][1]); // A Wumpus eltalálva, eltűnt
}



@Test
public void testFireArrowMissedWumpus() {
    // Előkészítés: beállítjuk a játék állapotát, ahol a wumpus nincs a lövés irányában
    game.setArrowCount(1);
    game.setHeroPosition(1, 1);
    game.setHeroFacing('E');
    game.setGameBoard(new char[][]{
            {'_', '_', 'U'},
            {'H', '_', 'W'},
            {'_', '_', '_'}
    });

    // Tesztelt metódus hívása
    game.fireArrow();

    // Elvárt eredmények ellenőrzése
    assertEquals(0, game.getArrowCount()); // Az íj elhasználódott
    // A Wumpus nem érintett, a térkép nem változott
    assertEquals('W', game.getGameBoard()[1][2]);
}
}
*/




