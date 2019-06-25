public class Main {

    public static void sleep(int time){
        try {
            Thread thread = new Thread();
            thread.sleep(time);
            System.exit(0);
        } catch (Exception e) {}
    }
    public static void main(String[] args) {

        GUI gui = new GUI();
        gui.show();

        // sleep(10000);
    }
}
