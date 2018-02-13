import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        AppGUI gui = new AppGUI( "Алгоритм К-средних" );
        gui.setVisible( true );
        gui.pack();
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gui.setLocationRelativeTo(null);
        gui.setResizable(false);
    }
}
