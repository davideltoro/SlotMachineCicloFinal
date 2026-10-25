import java.awt.geom.Ellipse2D;

/**
 * Un circulo que se dibuja en el canvas.
 *
 * @author  Michael Kolling and David J. Barnes (modificado)
 * @version 2.0
 */
public class Circle extends Shape
{
    public static final double PI = 3.1416;

    private int diameter;

    /**
     * Crea un circulo azul en la posicion por defecto.
     */
    public Circle()
    {
        this(30, 20, 15, "blue");
    }

    /**
     * Crea un circulo con el diametro, la posicion y el color dados.
     * @param diam diametro
     * @param posX posicion horizontal
     * @param posY posicion vertical
     * @param c color
     */
    public Circle(int diam, int posX, int posY, String c)
    {
        super(posX, posY, c);
        diameter = diam;
    }

    /**
     * Cambia el tamano del circulo.
     * @param newDiameter el nuevo diametro (en pixeles), debe ser >= 0
     */
    public void changeSize(int newDiameter)
    {
        erase();
        diameter = newDiameter;
        draw();
    }

    /**
     * Dibuja el circulo en el canvas, si es visible.
     */
    @Override
    protected void draw()
    {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new Ellipse2D.Double(xPosition, yPosition, diameter, diameter));
            canvas.wait(10);
        }
    }
}
