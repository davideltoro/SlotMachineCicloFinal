/**
 * Un rectangulo que se dibuja en el canvas.
 *
 * @author  Michael Kolling and David J. Barnes (modificado)
 * @version 2.0
 */
public class Rectangle extends Shape
{
    public static int EDGES = 4;

    private int height;
    private int width;

    /**
     * Crea un rectangulo magenta en la posicion por defecto.
     */
    public Rectangle()
    {
        this(30, 40, 70, 15, "magenta");
    }

    /**
     * Crea un rectangulo con el tamano, la posicion y el color dados.
     * @param h alto
     * @param w ancho
     * @param posX posicion horizontal
     * @param posY posicion vertical
     * @param c color
     */
    public Rectangle(int h, int w, int posX, int posY, String c)
    {
        super(posX, posY, c);
        height = h;
        width = w;
    }

    /**
     * Cambia el tamano del rectangulo.
     * @param newHeight el nuevo alto en pixeles, debe ser >= 0
     * @param newWidth el nuevo ancho en pixeles, debe ser >= 0
     */
    public void changeSize(int newHeight, int newWidth)
    {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }

    /**
     * Dibuja el rectangulo en el canvas, si es visible.
     */
    @Override
    protected void draw()
    {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, width, height));
            canvas.wait(10);
        }
    }
}
