import java.awt.Polygon;

/**
 * Un triangulo que se dibuja en el canvas. La posicion es la de la
 * punta de arriba.
 *
 * @author  Michael Kolling and David J. Barnes (modificado)
 * @version 2.0
 */
public class Triangle extends Shape
{
    public static int VERTICES = 3;

    private int height;
    private int width;

    /**
     * Crea un triangulo verde en la posicion por defecto.
     */
    public Triangle()
    {
        this(30, 40, 140, 15, "green");
    }

    /**
     * Ciclo 4
     * Crea un triangulo con el tamano, la posicion y el color dados.
     * @param h alto del triangulo
     * @param w ancho de la base del triangulo
     * @param posX posicion horizontal de la punta de arriba
     * @param posY posicion vertical de la punta de arriba
     * @param c color del triangulo
     */
    public Triangle(int h, int w, int posX, int posY, String c)
    {
        super(posX, posY, c);
        height = h;
        width = w;
    }

    /**
     * Crea un triangulo dado su color, altura y ancho.
     * @param newColor color
     * @param newHeight alto
     * @param newWidth ancho
     */
    public Triangle(String newColor, int newHeight, int newWidth)
    {
        this(newHeight, newWidth, 140, 15, newColor);
    }

    /**
     * Cambia el tamano del triangulo. Ignora valores no positivos.
     * @param newHeight el nuevo alto en pixeles
     * @param newWidth el nuevo ancho en pixeles
     */
    public void changeSize(int newHeight, int newWidth)
    {
        erase();
        if(newHeight > 0 && newWidth > 0) {
            height = newHeight;
            width = newWidth;
        }
        draw();
    }

    /**
     * @return el area del triangulo
     */
    public double area()
    {
        return (height * width) / 2;
    }

    /**
     * Convierte el triangulo en equilatero conservando su area.
     */
    public void equilateral()
    {
        double currentArea = area();
        int side = (int) Math.round(Math.sqrt((4 * currentArea) / Math.sqrt(3)));
        int newHeight = (int) Math.round((Math.sqrt(3) / 2) * side);
        changeSize(newHeight, side);
    }

    /**
     * Camina en diagonal hacia la derecha (positivo) o la izquierda (negativo).
     * @param times cantidad de pasos
     */
    public void walks(int times)
    {
        int step = Math.abs(times);
        for(int i = 0; i <= step; i++) {
            moveVertical(5);
            moveHorizontal(times > 0 ? 5 : -5);
        }
    }

    /**
     * Cambia la posicion x y y del triangulo.
     * @param positionx nueva posicion horizontal
     * @param positiony nueva posicion vertical
     */
    public void changePositionXandY(int positionx, int positiony)
    {
        moveTo(positionx, positiony);
    }

    /**
     * Dibuja el triangulo en el canvas, si es visible.
     */
    @Override
    protected void draw()
    {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = { xPosition, xPosition + (width / 2), xPosition - (width / 2) };
            int[] ypoints = { yPosition, yPosition + height, yPosition + height };
            canvas.draw(this, color, new Polygon(xpoints, ypoints, 3));
            canvas.wait(10);
        }
    }
}
