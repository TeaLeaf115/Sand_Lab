import java.awt.*;
import java.util.*;

public class SandLab {

    //add constants for particle types here
    public static final int AIR = 0;
    public static final int METAL = 1;
    public static final int SAND = 2;
    public static final int WATER = 3;
    public static final int ACID = 4;

    //do not add any more fields
    private int[][] grid;
    private SandDisplay display;

    public SandLab(int numRows, int numCols) {

        // Different particle elements.
        String[] names;
        names = new String[5];
        names[AIR] = "Air";
        names[METAL] = "Metal";
        names[SAND] = "Sand";
        names[WATER] = "Water";
        names[ACID] = "Acid";

        display = new SandDisplay("Falling Sand", numRows, numCols, names);

        this.grid = new int[numRows][numCols];
    }

    //called when the user clicks on a location using the given tool
    private void locationClicked(int row, int col, int tool) {
        grid[row][col] = tool;
    }

    //copies each element of grid into the display
    public void updateDisplay() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {

                // Color for the background.
                if (grid[row][col] == 0) {
                    display.setColor(row, col, new Color(0, 0, 0));
                }

                // Color for the Metal particle.
                else if (grid[row][col] == 1) {
                    display.setColor(row, col, new Color(128, 128, 128));
                }

                // Color for the Sand particle.
                else if (grid[row][col] == 2) {
                    display.setColor(row, col, new Color(232, 187, 26));
                }

                // Color for the Water particle.
                else if (grid[row][col] == 3) {
                    display.setColor(row, col, new Color(1, 101, 252));
                }

                // Color for the Acid particle.
                else if (grid[row][col] == 4) {
                    display.setColor(row, col, new Color(123, 255, 46));
                }
            }
        }
    }

    //called repeatedly.
    //causes one random particle to maybe do something.
    public void step() {
        // Initializes a random generator.
        Random randGen = new Random();

        // Generates a random y value to update.
        int y = randGen.nextInt(grid.length - 1);
        // Generates a random x value to update.
        int x = randGen.nextInt(grid[0].length);

        boolean hasAirUnderneath = grid[y+1][x] == AIR;
        boolean isNextToBorder = (grid[y][x] == 0) || (grid[y][x] == grid[0].length-1);

        // ----------
        // SAND
        // ----------
        // Updates any sand particles.
        if (grid[y][x] == SAND) {
            if (grid[y+1][x] == AIR) {
                grid[y][x] = AIR;
                grid[y+1][x] = SAND;
            }

            // Places Sand particles underneath any Water particles.
            else if (grid[y+1][x] == WATER) {
                grid[y+1][x] = SAND;
                grid[y][x] = WATER;
            }

            // Sand particles will 'absorb' any Acid particles.
            else if (grid[y+1][x] == ACID) {
                grid[y+1][x] = SAND;
                grid[y][x] = AIR;
            }

            // Creates a flowing Sand state
//            else if () {
//
//            }
        }

        // ----------
        // WATER
        // ----------
        // Updates any Water particles.
        else if (grid[y][x] == WATER) {

            // Creates a random direction for the Water particle to flow to.
            int waterDirection = randGen.nextInt(2);

            // Moves the Water particles down one.
            if (grid[y+1][x] == AIR) {
                grid[y][x] = AIR;
                grid[y+1][x] = WATER;
            }

            // Moves the Water particle down if it is next to a border and has air underneath
            else if (isNextToBorder && hasAirUnderneath) {
                grid[y][x] = AIR;
                grid[y+1][x] = WATER;
            }

            // Makes the Water particle stay put if it is near an edge.
            else if (isNextToBorder) {
                grid[y][x] = WATER;
            }

            else {
                // If 'waterDirection' is equal to '0' and has an available Air particle next to it, it will move the Water particle Left.
                if ((x-1 >= 0) && waterDirection == 0 && grid[y][x-1] == AIR) {
                    grid[y][x] = AIR;
                    grid[y][x-1] = WATER;
                }

                // If 'waterDirection' is equal to '1' and has an available Air particle next to it, it will move the Water particle Right.
                else if ((x+1 != grid[0].length) && waterDirection == 1 && grid[y][x+1] == AIR) {
                    grid[y][x] = AIR;
                    grid[y][x+1] = WATER;
                }
            }
        }

        // ----------
        // ACID
        // ----------
        // Updates any Acid particles.
        else if (grid[y][x] == ACID) {

            // Creates a random direction for the Acid particle to flow to.
            int acidDirection = randGen.nextInt(2);

            // Moves the Acid particles down one.
            if (grid[y+1][x] == AIR) {
                grid[y][x] = AIR;
                grid[y+1][x] = ACID;
            }

            // If an Acid particle touches a Metal particle then it will 'corrode' away the Metal particle leaving air.
            else if (grid[y+1][x] == METAL) {
                grid[y][x] = AIR;
                grid[y+1][x] = AIR;
            }

            // Moves the Acid particle down if it is next to a border and has air underneath
            else if (isNextToBorder && hasAirUnderneath) {
                grid[y][x] = AIR;
                grid[y+1][x] = ACID;
            }

            // Makes the Acid particle stay put if it is near an edge.
            else if (isNextToBorder) {
                grid[y][x] = ACID;
            }

            else {
                // If 'acidDirection' is equal to '0' and has an available Air particle next to it, it will move the Acid particle Left.
                if ((x-1 >= 0) && acidDirection == 0 && grid[y][x-1] == AIR) {
                    grid[y][x] = AIR;
                    grid[y][x-1] = ACID;
                }

                // If 'acidDirection' is equal to '1' and has an available Air particle next to it, it will move the Acid particle Right.
                else if ((x+1 != grid[0].length) && acidDirection == 1 && grid[y][x+1] == AIR) {
                    grid[y][x] = AIR;
                    grid[y][x+1] = ACID;
                }
            }
        }
    }

    //do not modify
    public void run() {

        while (true) {

            for (int i = 0; i < display.getSpeed(); i++)
                step();
            updateDisplay();
            display.repaint();
            display.pause(1);  //wait for redrawing and for mouse
            int[] mouseLoc = display.getMouseLocation();
            if (mouseLoc != null)  //test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
        }
    }
}

/* END OF CLASS */