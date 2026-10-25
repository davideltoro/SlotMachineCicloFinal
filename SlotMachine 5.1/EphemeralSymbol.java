/**
 * Simbolo efimero: en cada giro de la maquina reduce su tamano hasta
 * quedar como un punto. Se dibuja como un triangulo para distinguirlo.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public class EphemeralSymbol extends Symbol
{
    private int size;

    /**
     * Crea un simbolo efimero con el color dado y su tamano inicial.
     * @param color el color del simbolo (nombre CSS, ej. "red")
     */
    public EphemeralSymbol(String color)
    {
        super(color, new Triangle());
        size = 30;
    }

    /**
     * En cada giro reduce su tamano en 2, hasta quedar como un punto
     * de tamano 4 (no desaparece).
     */
    @Override
    public void onSpin()
    {
        if(size > 4) {
            size = size - 2;
        }
    }

    /**
     * @return el tamano actual del simbolo
     */
    @Override
    public int getSize()
    {
        return size;
    }

    /**
     * @return "triangle": el efimero se dibuja como triangulo
     */
    @Override
    public String getShape()
    {
        return "triangle";
    }
}
