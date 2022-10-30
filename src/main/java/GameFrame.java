import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;

public class GameFrame extends JFrame implements ActionListener {
    GamePanel gamepanel;
    JButton resetButton;

    GameFrame(){
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(600,600);


        resetButton = new JButton();
        resetButton.setText("Reset");
        resetButton.setSize(80,40);
        resetButton.setLocation(270,525);
        resetButton.setFont(new Font("Calibri", Font.BOLD, 16));
        resetButton.addActionListener(this);

        gamepanel = new GamePanel();
        this.add(resetButton);
        this.add(gamepanel);


        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==resetButton) {
            this.remove(gamepanel);
            gamepanel = new GamePanel();
            this.add(gamepanel);
            gamepanel.requestFocusInWindow();
            SwingUtilities.updateComponentTreeUI(this);

        }


    }

}