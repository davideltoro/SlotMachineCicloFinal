/**
 * Un simbolo de la maquina tragamonedas, identificado por su color.
 * Cada simbolo dibuja su propio circulo en el canvas.
 * 
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public class Symbol implements Visibility
{
    private String color;
    private Shape figure;
    private int xPosition;
    private int yPosition;

    /**
     * Crea un simbolo con el color dado.
     * @param color el color del simbolo (nombre CSS, ej. "red")
     */
    public Symbol(String color)
    {
        this(color, new Circle());
    }
    protected Symbol(String color, Shape figure)
    {
        this.color = color;
        this.figure = figure;
        figure.changeColor(color);
        xPosition = 20;
        yPosition = 15;
    }

    /**
     * Hace visible el simbolo en el canvas.
     */
    public void makeVisible()
    {
        figure.makeVisible();
    }

    /**
     * Hace invisible el simbolo en el canvas.
     */
    public void makeInvisible()
    {
        figure.makeInvisible();
    }

    /**
     * @return el color del simbolo
     */
    public String getColor()
    {
        return color;
    }
     /**
     * Mueve el circulo del simbolo a la posicion absoluta (x, y).
     * Se usara para ubicarlo dentro de la ventana de una rueda.
     * @param x posicion horizontal destino
     * @param y posicion vertical destino
     */
    public void placeAt(int x, int y)
    {
        figure.moveTo(x, y);
        xPosition = x;
        yPosition = y;
    }
    public void onSelected() { 
    }          // el normal no hace nada
    /**
     * Ciclo 4
     * La maquina lo llama en cada giro. El simbolo normal no hace nada;
     * los tipos de simbolo pueden redefinirlo.
     */
    public void onSpin() { 
    }          // el normal no hace nada
    public int getSize() { 
        return 24; 
    }
    public boolean isShown() { 
        return true; 
    }
    /**
     * @return la forma con que se dibuja el simbolo: "circle", "square" o "triangle"
     */
    public String getShape() { 
        return "circle"; 
    }
}