import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

// Some more new features to the control panel class.
// 1. One path finder that marks out the path
// 2. Clear out the path
// 3. Create a city simulator. And keep adding in new features.
// 4. Zoom in and zoom out of the program.
// 5. Can drag the program around
// 6. Set the exit and starting position of the maze.
// 7. Customize the size of the maze.



public class Control extends JPanel {

    boolean developerMode = false;
    int grid[][];
    Stack<Coordinate> rewind = new Stack<>();
    JLabel generate = new JLabel("Generate a Maze");
    JButton move = new JButton("Move");
    JButton timer = new JButton("Start");
    JButton clear = new JButton("Clear");
    JButton create = new JButton("Create");
    JLabel run = new JLabel("Run through the Maze");
    JButton autoRun = new JButton("Auto-Run");
    JCheckBox editor = new JCheckBox("Developer Mode");

    public Control(DrawPanel dp){

        Coordinate origin = new Coordinate();
        origin.type = 1;
        origin.xPos = 0;
        origin.yPos = 0;
        rewind.add(origin);
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == autoRun){
                    if(checkDone(dp))
                        autoRun(dp);
                }


                if(e.getSource() == move)
                    random(dp,0);

                if(e.getSource() == timer){
                    random(dp,50);
                    Timer timing = new Timer();
                    timing.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            if (!checkDone(dp)) {
                                random(dp,50);
                            } else {
                                timing.cancel();
                                timing.purge();
                            }

                        }
                    },0,50);

                }

                if(e.getSource() == editor){
                    System.out.println("ASD");
                    dp.developerMode = !dp.developerMode;
                    dp.repaint();
                }

                if(e.getSource() == clear){
                    dp.xPos = 0;
                    dp.yPos = 0;
                    dp.prevYPos = 0;
                    dp.prevXPos = 0;

                    for(int i = 0; i < dp.ROW; i ++){
                        for(int j = 0; j < dp.COL; j++){
                            dp.maze[i][j] = 0;
                        }
                    }

                    for(int i = 0; i < dp.ROW * 3; i++){
                        for(int j = 0; j < dp.COL * 3; j++){
                            dp.grid[i][j] = 0;
                        }
                    }
                    dp.repaint();
                }

                if(e.getSource() == create){
                    while(!checkDone(dp))
                        random(dp,0);
                }
            }
        };

        this.setLayout(null);

        generate.setBounds(10,10,200,20);
        add(generate);

        move.setBounds(10,40,100,30);
        move.addActionListener(al);
        add(move);

        timer.setBounds(120,40,100,30);
        timer.addActionListener(al);
        add(timer);

        clear.setBounds(230,40,100,30);
        clear.addActionListener(al);
        add(clear);

        create.setBounds(340,40,100,30);
        create.addActionListener(al);
        add(create);

        run.setBounds(10,100,200,20);
        add(run);

        autoRun.setBounds(10,130,100,30);
        autoRun.addActionListener(al);
        add(autoRun);

        editor.setBounds(10,900,200,40);
        editor.addActionListener(al);
        add(editor);

    }


    // ************************** Maze Generator *****************************************************
    Vector<Coordinate> availablePositions(DrawPanel dp) {

        Vector<Coordinate> coord = new Vector<Coordinate>();
        Coordinate temp = new Coordinate();

        int xPos = dp.getX();
        int yPos = dp.getY();

        if (xPos == 0) {
            // Can only check the positions at  right
            // Check right position
            if (dp.maze[yPos][xPos + 1] == 0) {
                temp.xPos = xPos + 1;
                temp.yPos = yPos;
                coord.add(temp);
            }
        } else if (xPos == dp.COL - 1) {
            // Can only check for the positions at left

            if (dp.maze[yPos][xPos - 1] == 0) {
                temp.xPos = xPos - 1;
                temp.yPos = yPos;
                coord.add(temp);
            }
        } else {
            // Can check left and right positions
            if (dp.maze[yPos][xPos - 1] == 0) {
                temp.xPos = xPos - 1;
                temp.yPos = yPos;
                coord.add(temp);
            }
            Coordinate temp3 = new Coordinate();
            if (dp.maze[yPos][xPos + 1] == 0) {
                temp3.xPos = xPos + 1;
                temp3.yPos = yPos;
                coord.add(temp3);
            }
        }
        Coordinate temp2 = new Coordinate();

        // Check up and down
        if (yPos == 0) {
            // Can only check the positions at bot
            if (dp.maze[yPos + 1][xPos] == 0) {
                temp2.xPos = xPos;
                temp2.yPos = yPos + 1;
                coord.add(temp2);
            }
        } else if (yPos == dp.ROW - 1) {
            // Can only check the position at top
            if (dp.maze[yPos - 1][xPos] == 0) {
                temp2.xPos = xPos;
                temp2.yPos = yPos - 1;
                coord.add(temp2);
            }
        } else {
            // Can check both bot and top positions
            if (dp.maze[yPos - 1][xPos] == 0) {
                temp2.xPos = xPos;
                temp2.yPos = yPos - 1;
                coord.add(temp2);
            }
            if (dp.maze[yPos + 1][xPos] == 0) {
                Coordinate temp3 = new Coordinate();
                temp3.xPos = xPos;
                temp3.yPos = yPos + 1;
                coord.add(temp3);
            }
        }
        return coord;
    }

    public void random(DrawPanel dp, int seconds){

        // If not done
        if(checkDone(dp)){
            System.out.println("END");
            return;
        }

        int xPos = dp.getX();
        int yPos = dp.getY();

        // List all the positions that is 0.
        Vector<Coordinate> coord = availablePositions(dp);
        int size = coord.size();

        // Pick a position that is 0, also add the grid positions to a stack.Also mark the grid position as a number 1.
        // If there are no surrounding positions that is 0, then roll backward according to the stack. Also mark the rolled back position as 2

        if(size != 0) {
            // Select one coordinate from the list of coordinates
            int ranNum = (int) (Math.random() * size);
            System.out.println(ranNum);
            Coordinate selected = coord.get(ranNum);

            // Add current location to the stack, and mark the current position as 1.
            dp.maze[yPos][xPos] = 1;
            Coordinate current = new Coordinate();
            current.xPos = xPos;
            current.yPos = yPos;
            current.type = 1;
            rewind.push(current);

            // Proceed forward.
            dp.prevXPos = dp.xPos;
            dp.prevYPos = dp.yPos;
            dp.xPos = selected.xPos;
            dp.yPos = selected.yPos;

            int xDiff = dp.xPos - dp.prevXPos ;
            int yDiff = dp.yPos - dp.prevYPos;

            try
            {
                // Draw the center point of the previous position
                dp.grid[dp.prevXPos * 3 + 1][dp.prevYPos * 3 + 1] = 1;
                Thread.sleep(seconds);
                // Draw the road to the next point
                dp.grid[dp.prevXPos * 3 + 1 + xDiff][dp.prevYPos * 3 + 1 + yDiff] = 1;
                Thread.sleep(seconds);
                // Draw the raod to the next point
                dp.grid[dp.xPos * 3 + 1 - xDiff][dp.yPos * 3 + 1 - yDiff] = 1;
                // Draw the next point's center point.
                dp.grid[dp.xPos * 3 + 1][dp.yPos * 3 + 1] = 1;
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }

            dp.repaint();
        } else {
            // Need to rewind back

            Coordinate back = rewind.pop();
            dp.maze[yPos][xPos] = 2;
            dp.prevXPos = dp.xPos;
            dp.prevYPos = dp.yPos;
            dp.xPos = back.xPos;
            dp.yPos = back.yPos;

            int xDiff = dp.xPos - dp.prevXPos ;
            int yDiff = dp.yPos - dp.prevYPos;

            try
            {
                // Draw the center point of the previous position
                dp.grid[dp.prevXPos * 3 + 1][dp.prevYPos * 3 + 1] = 2;
                Thread.sleep(seconds);
                // Draw the road to the next point
                dp.grid[dp.prevXPos * 3 + 1 + xDiff][dp.prevYPos * 3 + 1 + yDiff] = 2;
                Thread.sleep(seconds);
                // Draw the raod to the next point
                dp.grid[dp.xPos * 3 + 1 - xDiff][dp.yPos * 3 + 1 - yDiff] = 2;
                // Draw the next point's center point.
                dp.grid[dp.xPos * 3 + 1][dp.yPos * 3 + 1] = 2;
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }

        }
        // For testing purpose.
        dp.repaint();

        for(int i = 0; i < dp.ROW; i++){
            for(int j = 0; j < dp.COL; j++){
                System.out.print(dp.maze[i][j]);
            }
            System.out.println();
        }
        System.out.println("********************************NEXT************************************");

    }

    public boolean checkDone(DrawPanel dp){
        for(int i = 0; i < dp.ROW; i++){
            for(int j = 0; j < dp.COL; j++){
                if(dp.maze[i][j] != 2)
                    return false;
            }
        }
        return true;
    }

    // ************************** Maze Generator *****************************************************
    // ************************** Maze Runner ********************************************************
    public void autoRun(DrawPanel dp){

        // Place a copy
        grid = dp.grid;
        dp.grid[1][1] = 3;

        dp.grid[dp.ROW * 3 - 2][dp.COL * 3 - 2] = 3;

        dp.repaint();
        // Red spot represents the player's person identity.

        // Beginning at 1,1.

        // If find a position at ROW * 3 - 2, COLUMN * 3 - 2

    }
    // ************************** Maze Runner ********************************************************

}
