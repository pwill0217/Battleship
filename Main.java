import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.Random;

public class Main extends Application {
    private static final int BOARD_SIZE = 10;
    private Ship[][] grid = new Ship[BOARD_SIZE][BOARD_SIZE];
   private int totalShips = 5; // Total number of ships on the board
    private int sunkShips = 0; 


    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = createGameBoard();
        Scene scene = new Scene(gridPane, 400, 400);

        primaryStage.setTitle("Battleship Game Board");
        primaryStage.setScene(scene);
        primaryStage.show();
        placeShipsRandomly(gridPane);
      
    }

    private GridPane createGameBoard() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button cellButton = createCellButton(row, col);
                gridPane.add(cellButton, col, row);
            }
        }

        return gridPane;
    }

    private Button createCellButton(int row, int col) {
       Button cellButton = new Button();
    cellButton.setMinSize(30, 30);
    cellButton.setOnAction(e -> handleCellButtonClick(row, col, cellButton)); // Pass the button
    return cellButton;
    }

    private void handleCellButtonClick(int row, int col, Button cellButton) {
    System.out.println("Attack launched at " + row + ", col: " + col);

    if (grid[row][col] == null) {
        System.out.println("Miss");
        cellButton.setDisable(true); // Disable the button after a miss
    } else {
        Ship ship = grid[row][col];
        ship.hit();
        System.out.println("Hit " + ship.getName());

        if (ship.isSunk()) {
            System.out.println(ship.getName() + " has been sunk!");
            cellButton.setStyle("-fx-background-color: red;");
            cellButton.setDisable(true); // Disable the button after a hit or sunk
            sunkShips++;

            if (sunkShips == totalShips) {
                System.out.println("Congratulations! You have sunk all the ships. You win!");
                // You can add additional logic here for handling the game-over state.
            }
        }
    }
}


    public static void main(String[] args) {
        launch(args);
    }

  public class Ship {
    private String name;
    private int length;
    private int width;
    private boolean isSunk;

    // Constructor to initialize a ship with a name, length, and width
    public Ship(String name, int length, int width) {
        this.name = name;
        this.length = length;
        this.width = width;
        this.isSunk = false; // A new ship is not sunk initially
    }

    // Getter methods for accessing ship attributes
    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public boolean isSunk() {
        return isSunk;
    }

    // Method to update the status of the ship when it gets hit
    public void hit() {
        isSunk = true;
    }
  }
private void placeShipsRandomly(GridPane gridPane) {
        Random random = new Random();
  // Create Ship instances
        Ship carrier = new Ship("Carrier", 5, 1);
        Ship battleship = new Ship("Battleship", 4, 1);
        Ship cruiser = new Ship("Cruiser", 3, 1);
        Ship submarine = new Ship("Submarine", 3, 1);
        Ship destroyer = new Ship("Destroyer", 2, 1);
  // Create an array of ships for easier iteration
        Ship[] ships = {carrier, battleship, cruiser, submarine, destroyer};
  // Place ships on the grid
        for (Ship ship : ships) {
            boolean isValidPlacement = false;

            while (!isValidPlacement) {
                int row = random.nextInt(BOARD_SIZE);
                int col = random.nextInt(BOARD_SIZE);
                boolean isHorizontal = random.nextBoolean();

                isValidPlacement = tryPlaceShip(ship, row, col, isHorizontal);
            }
        }
    }
  
   private boolean tryPlaceShip(Ship ship, int row, int col, boolean isHorizontal) {
        // Check if the placement is valid and update the grid
     if (isHorizontal) {
    if (col + ship.getLength() <= BOARD_SIZE) {
        // Check if the cells are empty before placing the ship
        boolean isEmptyCells = true;
        for (int c = col; c < col + ship.getLength(); c++) {
            if (grid[row][c] != null) {
                isEmptyCells = false;
                break;
            }
        }
        
        // If cells are empty, place the ship
        if (isEmptyCells) {
                // Check if there are no ships placed nearby
                boolean isAdjacentFree = true;
                for (int c = col; c < col + ship.getLength(); c++) {
                    if (!isCellFree(row, c)) {
                        isAdjacentFree = false;
                        break;
                    }
                }
       if (isAdjacentFree) {
                    // Place the ship
                    for (int c = col; c < col + ship.getLength(); c++) {
                        grid[row][c] = ship;
                    }
                   System.out.println(ship.getName() + " placed at (" + row + ", " + col + ")");
                    return true; // Ship placed successfully
                }
            }
        }
  
} else {
    if (row + ship.getLength() <= BOARD_SIZE) {
        // Check if the cells are empty before placing the ship
        boolean isEmptyCells = true;
        for (int r = row; r < row + ship.getLength(); r++) {
            if (grid[r][col] != null) {
                isEmptyCells = false;
                break;
            }
        }
        
        // If cells are empty, place the ship
         if (isEmptyCells) {
                // Check if there are no ships placed nearby
              boolean isAdjacentFree = true;
              for (int r = row; r < row + ship.getLength(); r++) {
                  if (!isCellFree(r, col)) {
                      isAdjacentFree = false;
                      break;
                  }
              }
            if (isAdjacentFree) {
                    // Place the ship
                for (int r = row; r < row + ship.getLength(); r++) {
                    grid[r][col] = ship;
                    }
                  return true; // Ship placed successfully
                }
            }
        }
    }

return false; // Ship placement failed

}
  
private boolean isCellFree(int row, int col) {
    return (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && grid[row][col] == null);
}
}
