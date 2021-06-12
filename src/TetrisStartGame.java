/**
 * Main class, where game is started
 */
public class TetrisStartGame
{
    public static void main(String[] args)
    {
        Model model = new Model();
        TetrisFrame game = new TetrisFrame();
        Controller controller = new Controller(model, game);
        game.setController(controller);

        game.init();
    }
}