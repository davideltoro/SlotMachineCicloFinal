/**
 * Rueda dormilona: gira una vez y en la siguiente peticion de giro
 * se queda dormida (no cambia de simbolo). Luego vuelve a girar,
 * alternando asi indefinidamente.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public class SleepyWheel extends Wheel
{
    private boolean tired;   // true si acaba de girar y toca descansar

    /**
     * Crea una rueda dormilona en el lugar indicado.
     * @param slotIndex lugar (1-based) que ocupa en la maquina
     */
    public SleepyWheel(int slotIndex)
    {
        super(slotIndex);
        tired = false;
        int x = 40 + (slotIndex - 1) * 50;
        mark = new Rectangle(6, 40, x, 153, "red");
    }

    /**
     * Cada vez que se le pide girar, decide si lo hace o descansa.
     * Alterna: gira, descansa, gira, descansa...
     * @return true si en esta peticion debe girar; false si descansa
     */
    @Override
    public boolean wantsToSpin()
    {
        if(tired) {
            tired = false;
        }else{
        tired = true;}
        return tired;
    }

    /**
     * @return true si en la proxima peticion de giro se quedara dormida
     */
    public boolean isTired()
    {
        return tired;
    }
    @Override
    public void makeVisible()
    {
        super.makeVisible();
        mark.makeVisible();
    }
 
    /**
     * Hace invisible la rueda y su franja.
     */
    @Override
    public void makeInvisible()
    {
        super.makeInvisible();
        mark.makeInvisible();
    }
}