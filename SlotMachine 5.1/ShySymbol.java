/**
 * Clase Shy: hija de Symbol. Este simbolo es timido:
 * alterna entre visible e invisible cada vez que la rueda es seleccionada.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public class ShySymbol extends Symbol
{
    private boolean shown;

    public ShySymbol(String color)
    {
        super(color, new Rectangle());
        shown = true;
    }

    /**
     * Alterna entre visible e invisible. Solo cambia el estado: la rueda
     * es la que dibuja el cuadrado (o lo oculta) consultando isShown().
     */
    @Override
    public void onSelected()
    {
        shown = !shown;
    }

    @Override
    public boolean isShown()
    {
        return shown;
    }

    @Override
    public String getShape()
    {
        return "square";
    }
}