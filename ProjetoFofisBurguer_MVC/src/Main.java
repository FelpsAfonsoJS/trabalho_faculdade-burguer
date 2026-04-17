import controller.LanchoneteController;
import view.LanchoneteView;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        LanchoneteView view = new LanchoneteView(sc);
        LanchoneteController controller = new LanchoneteController(view);

        controller.iniciar();

        sc.close();
    }
}
