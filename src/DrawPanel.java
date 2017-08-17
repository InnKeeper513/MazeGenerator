import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {

    static final int PIXEL_SIZE = 9;
    static final int SINGLE_PIX = PIXEL_SIZE / 3;
    static final int ROW = 30;
    static final int COL = 30;
    static final int MOVE = 3;
    static final int BLANK = 75;
    static int [][] grid;
    static int [][] maze;
    int xPos = 0;
    int yPos = 0;

    int prevXPos = 0;
    int prevYPos = 0;

    public int getX(){
        return xPos;
    }
    public int getY(){
        return yPos;
    }

    DrawPanel() {

        // initialization;
        grid = new int[ROW * PIXEL_SIZE][COL * PIXEL_SIZE];
        maze = new int[ROW][COL];

        // For the maze 0 is unexplored, 1 is explored, 2 is went back
        for(int i = 0; i < ROW; i++){
            for(int j = 0; j < COL; j++){
                maze[i][j] = 0;
            }
        }
        // Set the walls
        for (int i = 0; i < ROW * PIXEL_SIZE; i++) {
            for (int j = 0; j < COL * PIXEL_SIZE; j+=PIXEL_SIZE) {
               grid[i][j] = 3;
               grid[i][j+1] = 3;
               grid[i][j+2] = 3;
               grid[i][j+3] = 0;
               grid[i][j+4] = 0;
               grid[i][j+5] = 0;
               grid[i][j+6] = 3;
               grid[i][j+7] = 3;
               grid[i][j+8] = 3;
            }
        }

        for (int i = 0; i < ROW * PIXEL_SIZE; i++) {
            for (int j = 0; j < COL * PIXEL_SIZE; j+=PIXEL_SIZE) {
                grid[j+3][i] = 0;
                grid[j+4][i] = 0;
                grid[j+5][i] = 0;
            }
        }
    }

    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.GRAY);
        g2d.fillRect(0,0,getWidth(),getHeight());

        for(int i = 0; i < ROW * PIXEL_SIZE; i++){
            for(int j = 0; j < ROW * PIXEL_SIZE; j++){
                if(grid[i][j] == 0){
                    // WALL
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(BLANK + i * SINGLE_PIX,BLANK + j * SINGLE_PIX, SINGLE_PIX,SINGLE_PIX);
                } else if(grid[i][j] == 1){
                    // PATH
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(BLANK + i * SINGLE_PIX,BLANK + j * SINGLE_PIX, SINGLE_PIX,SINGLE_PIX);
                } else if(grid[i][j] == 2){
                    // RETURN PATH
                    g2d.setColor(Color.BLUE);
                    g2d.fillRect(BLANK + i * SINGLE_PIX, BLANK + j * SINGLE_PIX, SINGLE_PIX,SINGLE_PIX);
                } else if(grid[i][j] == 3){
                    // WALL Cannot be modified
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(BLANK + i * SINGLE_PIX, BLANK + j * SINGLE_PIX, SINGLE_PIX,SINGLE_PIX);
                }
            }
        }
    }
}
