/**
 * Figura que se dibuja en el canvas. Reune lo que tienen en comun
 * Circle, Rectangle y Triangle: posicion, color, visibilidad y
 * movimiento. Cada hija solo dice como se dibuja.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 2.0
 */
public abstract class Shape implements Visibility
{
    protected int xPosition;
    protected int yPosition;
    protected String color;
    protected boolean isVisible;

    /**
     * Inicializa la posicion y el color de la figura. Empieza invisible.
     * @param x posicion horizontal
     * @param y posicion vertical
     * @param color color de la figura
     */
    protected Shape(int x, int y, String color)
    {
        xPosition = x;
        yPosition = y;
        this.color = color;
        isVisible = false;
    }

    /**
     * Hace visible la figura. Si ya era visible, no hace nada.
     */
    public void makeVisible()
    {
        isVisible = true;
        draw();
    }

    /**
     * Hace invisible la figura. Si ya era invisible, no hace nada.
     */
    public void makeInvisible()
    {
        erase();
        isVisible = false;
    }

    /**
     * Cambia el color de la figura.
     * @param newColor el nuevo color
     */
    public void changeColor(String newColor)
    {
        color = newColor;
        draw();
    }

    /**
     * Mueve la figura a la posicion absoluta (x, y).
     * @param x posicion horizontal destino
     * @param y posicion vertical destino
     */
    public void moveTo(int x, int y)
    {
        erase();
        xPosition = x;
        yPosition = y;
        draw();
    }

    /**
     * Mueve la figura horizontalmente.
     * @param distance distancia en pixeles (negativa hacia la izquierda)
     */
    public void moveHorizontal(int distance)
    {
        moveTo(xPosition + distance, yPosition);
    }

    /**
     * Mueve la figura verticalmente.
     * @param distance distancia en pixeles (negativa hacia arriba)
     */
    public void moveVertical(int distance)
    {
        moveTo(xPosition, yPosition + distance);
    }

    /**
     * Mueve la figura unos pixeles a la derecha.
     */
    public void moveRight()
    {
        moveHorizontal(20);
    }

    /**
     * Mueve la figura unos pixeles a la izquierda.
     */
    public void moveLeft()
    {
        moveHorizontal(-20);
    }

    /**
     * Mueve la figura unos pixeles hacia arriba.
     */
    public void moveUp()
    {
        moveVertical(-20);
    }

    /**
     * Mueve la figura unos pixeles hacia abajo.
     */
    public void moveDown()
    {
        moveVertical(20);
    }

    /**
     * Mueve la figura horizontalmente, pixel por pixel.
     * @param distance distancia en pixeles
     */
    public void slowMoveHorizontal(int distance)
    {
        int delta = (distance < 0) ? -1 : 1;
        for(int i = 0; i < Math.abs(distance); i++) {
            xPosition += delta;
            draw();
        }
    }

    /**
     * Mueve la figura verticalmente, pixel por pixel.
     * @param distance distancia en pixeles
     */
    public void slowMoveVertical(int distance)
    {
        int delta = (distance < 0) ? -1 : 1;
        for(int i = 0; i < Math.abs(distance); i++) {
            yPosition += delta;
            draw();
        }
    }

    /**
     * Dibuja la figura en el canvas, si es visible. Cada figura sabe
     * como dibujarse (circulo, rectangulo o triangulo).
     */
    protected abstract void draw();

    /**
     * Borra la figura del canvas, si es visible.
     */
    protected void erase()
    {
        if(isVisible) {
            Canvas.getCanvas().erase(this);
        }
    }
}
