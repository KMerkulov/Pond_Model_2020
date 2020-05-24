import views.MainForm;

import javax.swing.*;

public class Main {

    /**
     * Application Entry Point
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Graphical Pond Generator Model");

        // Create a main form
        MainForm view = new MainForm();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Graphical Pond Generator Model");

        frame.setContentPane(view.mainPanel);


        frame.pack();
        frame.setSize(800 , 580);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
