import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.Vector;

public class Control extends JPanel {


    Stack<Coordinate> rewind = new Stack<>();
    JButton move = new JButton("Move");

    // Check if the maze has done its creation
    public boolean checkDone(DrawPanel dp){
        for(int i = 0; i < dp.ROW; i++){
            for(int j = 0; j < dp.COL; j++){
                if(dp.maze[i][j] != 2)
                    return false;
            }
        }
        return true;
    }

    public Control(DrawPanel dp){

        Coordinate origin = new Coordinate();
        origin.type = 1;
        origin.xPos = 0;
        origin.yPos = 0;
        rewind.add(origin);
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == move)
                    random(dp);
            }
        };


        move.addActionListener(al);
        move.setVisible(true);
        add(move);

    }

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

    public void random(DrawPanel dp){

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
            dp.xPos = selected.xPos;
            dp.yPos = selected.yPos;



            // repaint();
        } else {
            // Need to rewind back

            Coordinate back = rewind.pop();
            dp.maze[yPos][xPos] = 2;
            dp.xPos = back.xPos;
            dp.yPos = back.yPos;
        }
        // For testing purpose.

        for(int i = 0; i < dp.ROW; i++){
            for(int j = 0; j < dp.COL; j++){
                System.out.print(dp.maze[i][j]);
            }
            System.out.println();
        }
        System.out.println("********************************NEXT************************************");

    }

}
