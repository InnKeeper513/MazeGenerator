import com.sun.deploy.panel.ControlPanel;

import javax.swing.*;

public class DrawFrame extends JFrame {

    public DrawFrame(){
        super("Maze Generator");

        this.setSize(1500,1000);
        this.setLayout(null);

        DrawPanel dp = new DrawPanel();
        dp.setBounds(0,0,1000,1000);
        this.add(dp);

        Control cp = new Control(dp);
        cp.setBounds(1000,0,500,1000);
        this.add(cp);
    }
}
