import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() throws HeadlessException {
        setTitle("Sibirian Snake");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(new GameField());
        setSize(380, 410);
        setLocation(400, 400);
        setVisible(true);
    }


    public static void main(String[] args) {
        MainWindow MW = new MainWindow();

    }
}
