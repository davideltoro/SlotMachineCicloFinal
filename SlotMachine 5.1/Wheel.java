/**
 * Una rueda de la maquina tragamonedas. Guarda en que posicion
 * (indice dentro de la lista de simbolos de SlotMachine) esta parada,
 * y se representa visualmente como una ventana con un circulo indicador
 * que cambia de color segun el simbolo que muestra.
 * 
 * @author Juan David Castellanos - Nicole Paez
 * @version 1.2
 */
public class Wheel implements Visibility
{
    private Symbol current;
    private Rectangle window;
    private Circle indicator;
    private boolean held;
    protected Rectangle mark;
    private Rectangle squareInd;
    private Triangle triInd;
    private boolean visible;
    private int drawnSize;  
    private int centerX;
    private int centerY;
    private int slotIndex;


    /**
     * Crea una rueda en la posicion 1 por defecto, con su ventana
     * ubicada segun el lugar que ocupa entre las demas ruedas.
     * @param slotIndex lugar (1-based) que ocupa esta rueda en la maquina
     */
    public Wheel(int slotIndex)
    {
        current = null;
        this.slotIndex = slotIndex;
        int targetX = 40 + (slotIndex - 1) * 50;
        int targetY = 90;

        window = new Rectangle(60,40,targetX,targetY,"white");
        
        int circleX = targetX + (40 - 24) / 2;   // 40 = ancho real de la ventana
        int circleY = targetY + (60 - 24) / 2;   
        indicator = new Circle(24,circleX,circleY,"white");
        
        held=false;
        squareInd = new Rectangle(24, 24, circleX, circleY, "white");
        triInd = new Triangle(24, 24, targetX + 20, circleY, "white");   // punta arriba, centrada en la ventana
        drawnSize = 24;
        visible = false;
        centerX = targetX + 20;   // centro de la ventana (40 de ancho)
        centerY = targetY + 30;   // centro de la ventana (60 de alto)
    }

    /**
     * Mueve la rueda (ventana, franja y simbolo) al lugar indicado de la
     * maquina. Se usa cuando se elimina una rueda que estaba a su izquierda.
     * @param newSlot nuevo lugar (1-based) que ocupa la rueda
     */
    public void moveToSlot(int newSlot)
    {
        int distance = (newSlot - slotIndex) * 50;
        window.moveHorizontal(distance);
        if(mark != null) {
            mark.moveHorizontal(distance);
        }
        centerX = centerX + distance;
        slotIndex = newSlot;
        paint();
    }

    /**
     * @return la posicion actual de la rueda
     */
    public Symbol getCurrentPosition()
    {
        return current;
    }

    /**
     * Cambia la posicion actual de la rueda.
     * @param pos la nueva posicion
     */
    public void setCurrentPosition(Symbol s)
    {
        current = s;
        paint();
    }

    /**
     * Hace visible la ventana y el indicador de la rueda.
     */
    public void makeVisible()
    {
        window.makeVisible();
        visible = true;
        paint();
    }

    /**
     * Hace invisible la ventana y el indicador de la rueda.
     */
    public void makeInvisible()
    {
        window.makeInvisible();
        visible = false;
        paint();
    }
    
    /**
     * @return true si la rueda esta fijada (no debe girar)
     */
    public boolean isHeld()
    {
        return held;
    }

    /**
     * Fija o suelta la rueda.
     * @param held true para fijarla, false para soltarla
     */
    public void setHeld(boolean held)
    {
        this.held = held;
    }
    /**
     * Decide en que simbolo queda la rueda al girar.
     * @param proposed el simbolo en el que quedaria una rueda normal
     * @return el simbolo en el que realmente queda
     */
    public Symbol chooseSymbol(Symbol proposed)
    {
        return proposed;
    }

    /**
     * Ciclo 4
     * Dibuja el simbolo actual dentro de la ventana con su forma y su
     * tamano: un circulo para el simbolo normal y un triangulo para el
     * efimero. Si la rueda esta invisible, solo oculta las figuras.
     */
    private void paint()
    {
        indicator.makeInvisible();
        triInd.makeInvisible();
        squareInd.makeInvisible();
        if(!visible) {
            return;
        }
        if(current == null) {
            drawCircle("white", 24);
        } else if (!current.isShown()){
             return;
        } else if(current.getShape().equals("triangle")) {
            drawTriangle(current.getColor(), current.getSize());
        }else if (current.getShape().equals("square")){
            drawSquare(current.getColor(), current.getSize());
        }else {
            drawCircle(current.getColor(), current.getSize());
        }
    }

    /**
     * Dibuja un circulo centrado en la ventana.
     * @param color color del circulo
     * @param size diametro del circulo
     */
    private void drawCircle(String color, int size)
    {
        indicator = new Circle(size, centerX - size / 2, centerY - size / 2, color);
        indicator.makeVisible();
    }

    /**
     * Dibuja un triangulo centrado en la ventana.
     * @param color color del triangulo
     * @param size alto y ancho del triangulo
     */
    private void drawTriangle(String color, int size)
    {
        triInd = new Triangle(size, size, centerX, centerY - size / 2, color);
        triInd.makeVisible();
    }
    /**
     * Dibuja un cuadrado centrado en la ventana.
     * @param color color del cuadrado
     * @param size lado del cuadrado
     */
    private void drawSquare(String color, int size)
    {
        squareInd = new Rectangle(size, size, centerX - size / 2, centerY - size / 2, color);
        squareInd.makeVisible();
    }
    
    /**
     * Ciclo 4
     * Indica si la rueda gira cuando se le pide. Una rueda normal
     * siempre gira; las ruedas de otros tipos pueden redefinirlo.
     * @return true si gira en esta peticion
     */
    public boolean wantsToSpin()
    {
        return true;
    }

    /**
     * Ciclo 4
     * Indica si la rueda se deja fijar.
     * @return true: una rueda normal se deja fijar
     */
    public boolean canLock()
    {
        return true;
    }

    /**
     * Ciclo 4
     * Indica si la rueda se deja intercambiar con otra.
     * @return true: una rueda normal se deja intercambiar
     */
    public boolean canSwap()
    {
        return true;
    }

    /**
     * Ciclo 4
     * Indica si la rueda se deja eliminar de la maquina.
     * @return true: una rueda normal se deja eliminar
     */
    public boolean canDelete()
    {
        return true;
    }

}