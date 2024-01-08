package hu.nye.progtech;

import java.io.File;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.sql.*;

public class WumpusGame {
    private static final String FILE_PATH = "D:\\vedees\\Vedes-main\\src\\main\\java\\hu\\nye\\progtech\\mapFromText.txt";
    private static final String HIGH_SCORES_FILE_PATH = "D:\\vedees\\Vedes-main\\src\\main\\java\\hu\\nye\\progtech\\highscores.txt";
    private static final String XML_FILE_PATH = "D:\\vedees\\Vedes-main\\src\\main\\java\\hu\\nye\\progtechsavegame.xml";
    private static char[][] gameBoard;
    private static int heroRow;
    private static int heroColumn;
    private static char heroFacing;
    private static int arrowCount;
    private static int stepCount;
    private static Map<String, Integer> scores = new HashMap<>();


    public static void main(String[] args) {
        loadHighScores();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome in the Wimpus Game! (Pre Alpha)\n 70$$ was deducted from your bank account \n There is no refund!\n");
        System.out.print("Please enter a username: ");
        String username = scanner.nextLine();

        while (true) {
            System.out.println("\nA journey of a thousand miles begins with a single step\n Kill The Wumpus!\n Save the princess! (Buy the princess DLC only for 14.99$$)!\n Get the gold!");
            System.out.println("\nMain Menu:");
            System.out.println("1. Start Game");
            System.out.println("2. Load map from txt");
            System.out.println("3. High Scores");
            System.out.println("4. Save (XML)");
            System.out.println("5. Load (XML)");
           // System.out.println("6. Save (DB)");
           // System.out.println("7. Load (DB)");
            System.out.println("8. Quit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    if (gameBoard != null) {
                        playGame(username);
                    } else {
                        System.out.println("Error! There is no map! Please buy your first map for 4.99$$ by pressing button 2");
                    }
                }

                case 2 -> loadGameBoardFromFile();
                case 3 -> viewHighScores();
                case 4 -> saveGameToXML();
                case 5 -> loadGameFromXML();
                //case 6 -> saveHighScoreToDB();
                //case 7 -> readDataFromUsersTable();
                case 8 -> {
                    saveHighScores();

                    System.exit(0);
                }
                default -> System.out.println("Please Choose a number between 1-8.");
            }
        }
    }

    private static void loadGameFromXML() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(XML_FILE_PATH));

            doc.getDocumentElement().normalize();

            // Get game state elements from XML
            Element rootElement = doc.getDocumentElement();
            int heroRow = Integer.parseInt(getElementValue(rootElement, "HeroRow"));
            int heroColumn = Integer.parseInt(getElementValue(rootElement, "HeroColumn"));
            char heroFacing = getElementValue(rootElement, "HeroFacing").charAt(0);
            int arrowCount = Integer.parseInt(getElementValue(rootElement, "ArrowCount"));
            int stepCount = Integer.parseInt(getElementValue(rootElement, "StepCount"));

            // Update game state
            WumpusGame.heroRow = heroRow;
            WumpusGame.heroColumn = heroColumn;
            WumpusGame.heroFacing = heroFacing;
            WumpusGame.arrowCount = arrowCount;
            WumpusGame.stepCount = stepCount;


        } catch (Exception e) {
            System.err.println("Error 404 " + e.getMessage());
        }
    }



    private static String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName).item(0).getChildNodes();
        return nodeList.item(0).getNodeValue();
    }

    private static void loadGameBoardFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));

            String[] info = reader.readLine().split(" ");
            int N = Integer.parseInt(info[0]);

            gameBoard = new char[N][N];

            for (int i = 0; i < N; i++) {
                String line = reader.readLine();
                for (int j = 0; j < N; j++) {
                    gameBoard[i][j] = line.charAt(j);
                    if (gameBoard[i][j] == 'H') {
                        heroRow = i;
                        heroColumn = j;
                        heroFacing = 'N';
                    } else if (gameBoard[i][j] == 'U') {
                        arrowCount++;
                    } else if (gameBoard[i][j] == 'G') {
                        stepCount = 0;
                    }
                }
            }

            System.out.println("\nGet The Gold! Be a true Hero!\n");
            displayGameBoard();
        } catch (IOException e) {
            System.out.println("\nError reading the file: " + e.getMessage());
        }
    }

    private static void displayGameBoard() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                System.out.print(gameBoard[i][j]);
            }
            System.out.println();
        }

        System.out.println("Hos Iranya: " + heroFacing + "  Ammo: " + arrowCount + "  Steps: " + stepCount);
    }

    private static void playGame(String username) {
        Scanner inputScanner = new Scanner(System.in);
        while (true) {
            displayGameBoard();

            System.out.print("Movement: WASD  Q: Quit Game R: shoot arrow): ");
            String move = inputScanner.nextLine().toUpperCase();

            if (move.equals("Q")) {
                break;
            } else if (move.equals("R")) {
                fireArrow();
                continue;
            }

            int newRow = heroRow;
            int newColumn = heroColumn;

            switch (move) {
                case "W" -> {
                    newRow--;
                    heroFacing = 'N';
                }
                case "S" -> {
                    newRow++;
                    heroFacing = 'S';
                }
                case "A" -> {
                    newColumn--;
                    heroFacing = 'W';
                }
                case "D" -> {
                    newColumn++;
                    heroFacing = 'E';
                }
                default -> {
                    System.out.println("Ervenytelen Input. Please use: WASD");
                    continue;
                }
            }

            if (isValidMove(newRow, newColumn)) {
                char newLocation = gameBoard[newRow][newColumn];
                if (newLocation == 'P') {
                    System.err.println("You stepped into a pit. You lost an arrow.");
                    arrowCount--;
                } else if (newLocation == 'U') {
                    System.err.println("You were eaten by the Wumpus!");
                    displayGameBoard();
                    return;
                } else if (newLocation == 'G') {
                    System.out.println("You found the gold. You won!");
                    stepCount++;
                    scores.put(username, scores.getOrDefault(username, 0) + 1); // Increment the high score
                    break;
                } else if (newLocation == '_') {
                    gameBoard[heroRow][heroColumn] = '_';
                    heroRow = newRow;
                    heroColumn = newColumn;
                    gameBoard[heroRow][heroColumn] = 'H';
                    stepCount++;
                } else {
                    System.out.println("You cannot break walls!");
                }
            }
        }
    }

    private static void fireArrow() {
        if (arrowCount > 0) {
            arrowCount--;

            int arrowRow = heroRow;
            int arrowColumn = heroColumn;

            switch (heroFacing) {
                case 'N' -> arrowRow--;
                case 'S' -> arrowRow++;
                case 'W' -> arrowColumn--;
                case 'E' -> arrowColumn++;
            }

            while (isValidMove(arrowRow, arrowColumn)) {
                char target = gameBoard[arrowRow][arrowColumn];
                if (target == 'W') {
                    System.out.println("Critical failure! You missed!.");
                    break;
                } else if (target == 'U') {
                    gameBoard[arrowRow][arrowColumn] = '_';
                    break;
                } else {
                    arrowRow += (heroFacing == 'S') ? 1 : (heroFacing == 'N') ? -1 : 0;
                    arrowColumn += (heroFacing == 'E') ? 1 : (heroFacing == 'W') ? -1 : 0;
                }
            }

            displayGameBoard();
            System.out.println("You hit a wumpus ");
        } else {
            System.out.println("You have no more arrows-.");
        }
    }

    private static void viewHighScores() {
        System.out.println("\nHigh Scores:");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static boolean isValidMove(int newRow, int newColumn) {
        return newRow >= 0 && newRow < gameBoard.length && newColumn >= 0 && newColumn < gameBoard[newRow].length;
    }

    private static void loadHighScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORES_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String username = parts[0].trim();
                int highScore = Integer.parseInt(parts[1].trim());
                scores.put(username, highScore);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void saveHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORES_FILE_PATH))) {
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void saveGameToXML() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Create the root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("WumpusGame");
            doc.appendChild(rootElement);

            // Add game state elements
            addElement(doc, rootElement, "HeroRow", String.valueOf(heroRow));
            addElement(doc, rootElement, "HeroColumn", String.valueOf(heroColumn));
            addElement(doc, rootElement, "HeroFacing", String.valueOf(heroFacing));
            addElement(doc, rootElement, "ArrowCount", String.valueOf(arrowCount));
            addElement(doc, rootElement, "StepCount", String.valueOf(stepCount));

            // Save the XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileWriter(XML_FILE_PATH));

            transformer.transform(source, result);

            System.out.println("Game Saved: " + XML_FILE_PATH);
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            System.err.println("Error configuring XML  transformer: " + e.getMessage());
        } catch (TransformerException | IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void addElement(Document doc, Element parent, String name, String value) {
        Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value));
        parent.appendChild(element);
    }
}
