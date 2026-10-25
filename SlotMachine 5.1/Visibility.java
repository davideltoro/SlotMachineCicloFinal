/**
 * Contrato de todo lo que se puede mostrar u ocultar en el simulador:
 * las figuras, los simbolos, las ruedas y la maquina completa.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.0
 */
public interface Visibility
{
    /**
     * Hace visible el elemento en el canvas.
     */
    void makeVisible();

    /**
     * Hace invisible el elemento en el canvas.
     */
    void makeInvisible();
}
