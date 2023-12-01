package pacman;

import javafx.scene.input.KeyCode;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;
    /**
     * Gets the opposite direction.
     *
     * @return the direction 180º away from the current direction
     */
    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            default:
                return LEFT;
        }
    }

    /**
     * Calculates the new X coordinate after moving one step, given the current X coordinate
     * and the current direction of movement.
     *
     * @param currentX the current X coordinate
     * @return the new X coordinate after moving one step in the current direction
     */
    public double newX(double currentX) {
        switch (this) {
            case LEFT:
                return currentX - Constants.SQUARE_WIDTH;
            case RIGHT:
                return currentX + Constants.SQUARE_WIDTH;
            default:
                return currentX;
        }
    }

    /**
     * Calculates the new Y coordinate after moving one step, given the current Y coordinate
     * and the current direction of movement.
     *
     * @param currentY the current Y coordinate
     * @return the new Y coordinate after moving one step in the current direction
     */
    public double newY(double currentY) {
        switch (this) {
            case UP:
                return currentY - Constants.SQUARE_WIDTH;
            case DOWN:
                return currentY + Constants.SQUARE_WIDTH;
            default:
                return currentY;
        }
    }
    public static Direction fromKeyCode(KeyCode keyCode) {
        switch (keyCode) {
            case UP:
                return UP;
            case DOWN:
                return DOWN;
            case LEFT:
                return LEFT;
            case RIGHT:
                return RIGHT;
            default:
                return null;
        }
    }

}
