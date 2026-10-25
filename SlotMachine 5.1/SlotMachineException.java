/**
 * Excepcion del simulador. Guarda como constantes los mensajes de las
 * situaciones en que SlotMachine lanza una excepcion.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public class SlotMachineException extends Exception
{
    // Maquina
    public static final String MAX_SYMBOLS =
        "El numero debe estar entre 1 y la cantidad de simbolos permitidos (13).";

    // Ruedas
    public static final String WHEEL_TYPE_UNKNOWN = "No existe el tipo de rueda: ";
    public static final String NO_WHEELS = "No hay ruedas.";

    // Simbolos
    public static final String SYMBOL_TYPE_UNKNOWN = "No existe el tipo de simbolo: ";
    public static final String COLOR_NOT_SUPPORTED = "El simulador no sabe dibujar el color: ";

    /**
     * Crea una excepcion con el mensaje dado.
     * @param message uno de los mensajes constantes de esta clase
     */
    public SlotMachineException(String message)
    {
        super(message);
    }
}