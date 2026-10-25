import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Simula una maquina tragamonedas con n ruedas y n simbolos posibles
 *
 * Los simbolos se guardan en dos estructuras que trabajan juntas:
 * - symbols: un HashMap indexado por color, porque el color es la identidad
 *   del simbolo y casi todas las operaciones lo buscan por ahi. La busqueda
 *   es en tiempo constante y evita recorrer el catalogo entero.
 * - order: la lista de colores en el orden en que estan en la rueda. El
 *   HashMap no guarda ningun orden, y los requisitos piden que addSymbol
 *   reciba una posicion y que symbols() devuelva los colores en orden.
 * Toda operacion que agregue o elimine un simbolo debe tocar las dos.
 *
 * @author Juan Castellanos - Nicole Paez
 * @version 1.2
 */
public class SlotMachine implements Visibility
{
    private ArrayList<Wheel> wheels;
    private HashMap<String,Symbol> symbols;
    private ArrayList<String> order;
    private boolean ok;
    private boolean visible;

    private Rectangle body;
    private Rectangle base;
    private Rectangle arm;
    private Circle knob;
    
    
    /**
     * Constructor para crear una maquina con un numero de ruedas y simbolos predeterminado
     * @param n numero de ruedas y simbolos a crear, para la simulacion
     * @throws SlotMachineException si n no esta entre 1 y la cantidad de
     *         simbolos permitidos (13)
     */
    public SlotMachine(int n) throws SlotMachineException{
        buildCasing();
        wheels = new ArrayList<Wheel>();
        symbols = new HashMap<String,Symbol>();
        order = new ArrayList<String>();

        String[] PALETTE = {"red", "blue", "yellow", "green", "orange", "magenta",
        "cyan", "black", "darkGray", "lightGray", "pink", "white","gray"};
        if(n < 1 || n > 13){
           ok = false;
           throw new SlotMachineException(SlotMachineException.MAX_SYMBOLS);
        }
        for (int i=1; i<=n; i++){
            addWheel(i);
            addSymbol(i,PALETTE[i-1]);
        }
        spin();
        visible = false;
        ok = true;
    }
    /**
     * Crea una maquina tragamonedas vacia (sin ruedas ni simbolos)
     * junto con su representacion visual.
     */
    public SlotMachine()
    {
        wheels = new ArrayList<Wheel>();
        symbols = new HashMap<String,Symbol>();
        order = new ArrayList<String>();
        ok = true;
        visible = false;
        buildCasing();
    }

    /**
     * Construye y posiciona las figuras que forman la carcasa
     * de la maquina (cuerpo, base y palanca). La palanca va a la
     * izquierda del cuerpo, asi no se mueve cuando el cuerpo crece
     * hacia la derecha al agregar ruedas.
     */
    private void buildCasing()
    {
        body = new Rectangle(150,220,30,50,"blue");
        base = new Rectangle(20,160,50,200,"black");
        arm = new Rectangle(50,10,10,60,"black");
        knob = new Circle(30,0,30,"red");
    }
    

    /**
     * Agrega una rueda nueva en la posicion indicada (1-based).
     * @param pos posicion donde insertar la rueda
     */
    public void addWheel(int pos)
    {
        addWheel("normal",pos);
    }
    /**
     * Ciclo 4
     * Agrega una rueda del tipo indicado en la posicion indicada (1-based).
     * Si ya hay simbolos, la rueda nueva muestra uno al azar.
     * Una rueda no se puede poner encima de otra: si en esa posicion ya
     * hay una rueda, o si el tipo de rueda no existe, no se agrega y se
     * informa al usuario.
     * @param type tipo de rueda: "normal", "lefty", "rebel" o "sleepy"
     * @param pos posicion donde agregar la rueda
     */
    public void addWheel(String type, int pos)
    {
        int difference;
        ok = false;
        try {
            int index = fixPosition(pos, wheels.size() + 1);
            if(index <= wheels.size()) {
                showError("Ya hay una rueda en esa posicion.");
                return;
            }
            Wheel w = newWheel(type, index);
            wheels.add(w);
            w.setCurrentPosition(randomSymbol());   // si no hay simbolos, queda vacia
            difference = Math.max(0, wheels.size() - 4) * 50;
            body.changeSize(150, 220 + difference);
            base.changeSize(20, 160 + difference);
            if(visible) {
                w.makeVisible();
            }
            changesJackpot();
            ok = true;
        } catch(SlotMachineException e) {
            ok = false;
            showError(e.getMessage());
        }
    }

    /**
     * Ciclo 4
     * Crea una rueda del tipo indicado.
     * @param type tipo de rueda: "normal", "lefty", "rebel" o "sleepy"
     * @param index lugar (1-based) que ocupara la rueda
     * @return la rueda creada
     * @throws SlotMachineException si el tipo de rueda no existe
     */
    private Wheel newWheel(String type, int index) throws SlotMachineException
    {
        String lower = type.toLowerCase();
        if(lower.equals("normal")) {
            return new Wheel(index);
        } else if(lower.equals("lefty")) {
            return new LeftyWheel(index, wheels);
        } else if(lower.equals("rebel")) {
            return new RebelWheel(index);
        } else if(lower.equals("sleepy")) {
            return new SleepyWheel(index);
        }
        throw new SlotMachineException(SlotMachineException.WHEEL_TYPE_UNKNOWN + type);
    }
    /**
     * Elimina la rueda en la posicion indicada (1-based) y corre un lugar
     * a la izquierda las ruedas que estaban a su derecha. Si no hay ruedas
     * o la rueda no se deja eliminar, se informa al usuario.
     * @param pos posicion de la rueda a eliminar
     */
    public void delWheel(int pos){
        ok = false;
        try {
            int index = fixPosition(pos, wheels.size());
            if(!wheels.get(index - 1).canDelete()) {
                showError("Esta rueda no se deja eliminar.");
                return;
            }
            Wheel w = wheels.remove(index - 1);
            w.makeInvisible();
            for(int i = index - 1; i < wheels.size(); i++) {
                wheels.get(i).moveToSlot(i + 1);
            }
            int difference = Math.max(0, wheels.size() - 4) * 50;
            body.changeSize(150, 220 + difference);
            base.changeSize(20, 160 + difference);
            changesJackpot();  
            ok = true;
        } catch(SlotMachineException e) {
            ok = false;
            showError(e.getMessage());
        }
    }

    /**
     * Agrega un simbolo nuevo del color dado en la posicion indicada.
     * @param pos posicion donde insertar el simbolo
     * @param color color del nuevo simbolo
     */
    public void addSymbol(int pos, String color){
        addSymbol("normal",pos,color);
    }
    /**
     * Agrega un simbolo del tipo y color dados en la posicion indicada.
     * Las ruedas que estaban vacias pasan a mostrar un simbolo al azar.
     * Si el color ya existe, si el canvas no sabe dibujar ese color o si
     * el tipo de simbolo no existe, no se agrega y se informa al usuario.
     * @param type tipo de simbolo: "normal", "ephemeral" o "shy"
     * @param pos posicion donde insertar el simbolo
     * @param color color del nuevo simbolo
     */
    public void addSymbol(String type,int pos, String color){
        ok = false;
        try {
            String lower = color.toLowerCase();
            if (symbols.containsKey(lower)){
                showError("Ya existe un simbolo con este color.");
                return;
            }
            int index = fixPosition(pos, order.size() + 1);
            Symbol s = newSymbol(type,lower);
            symbols.put(lower,s);
            order.add(index - 1, lower);
            for(Wheel w : wheels) {
                if(w.getCurrentPosition() == null) {
                    w.setCurrentPosition(randomSymbol());
                }
            }
            changesJackpot();
            ok = true;
        } catch(SlotMachineException e) {
            ok = false;
            showError(e.getMessage());
        }
    }
    /**
     * Crea un simbolo del tipo indicado.
     * @param type tipo de simbolo: "normal", "ephemeral" o "shy"
     * @param color color del simbolo
     * @return el simbolo creado
     * @throws SlotMachineException si el canvas no sabe dibujar ese color
     *         o si el tipo de simbolo no existe
     */
    private Symbol newSymbol(String type, String color) throws SlotMachineException
    {
        if(!Canvas.isKnownColor(color)) {
            throw new SlotMachineException(SlotMachineException.COLOR_NOT_SUPPORTED + color);
        }
        String lower = type.toLowerCase();
        if(lower.equals("normal")) {
            return new Symbol(color);
        } else if(lower.equals("ephemeral")) {
            return new EphemeralSymbol(color);
        } else if(lower.equals("shy")) {
            return new ShySymbol(color);
        }
        throw new SlotMachineException(SlotMachineException.SYMBOL_TYPE_UNKNOWN + type);
    }
    /**
     * Elimina el primer simbolo que tenga el color indicado. Las ruedas que
     * lo mostraban pasan a otro simbolo al azar (quedan vacias solo si ya
     * no quedan simbolos). Si no existe un simbolo con ese color, se
     * informa al usuario.
     * @param symbol color del simbolo a eliminar
     */
    public void delSymbol(String symbol)
    {
        ok = false;
        String lower = symbol.toLowerCase();
        Symbol deleted = symbols.remove(lower);
        if(deleted == null) {
            showError("El simbolo a eliminar no existe.");
            return;
        }
        order.remove(lower);
        for(Wheel w : wheels) {
            if(w.getCurrentPosition() == deleted) {
                w.setCurrentPosition(randomSymbol());
            }
        }
        changesJackpot();
        ok = true;
    }

    /**
     * Hace visible la maquina completa: la carcasa y cada una de sus ruedas.
     * Solo la muestra: no cambia lo que muestra cada rueda.
     */
    public void makeVisible(){
        body.makeVisible();
        base.makeVisible();
        arm.makeVisible();
        knob.makeVisible();
        for(Wheel w : wheels) {
            w.makeVisible();
        }
        visible = true;
        ok = true;
    }

    /**
     * Hace invisible la maquina completa: la carcasa y cada una de sus ruedas.
     */
    public void makeInvisible()
    {
        body.makeInvisible();
        base.makeInvisible();
        arm.makeInvisible();
        knob.makeInvisible();
        for(Wheel w : wheels) {
            w.makeInvisible();
        }
        visible = false;
        ok = true;
    }

    /**
     * Termina el simulador, ocultando toda su representacion visual.
     */
    public void exit()
    {
        makeInvisible();
    }

    /**
     * @return true si la ultima operacion se realizo con exito
     */
    public boolean ok()
    {
        return ok;
    }

        /**
     * Ubica el simbolo del color indicado en la rueda indicada.
     * Si no hay ruedas o el color no existe, se informa al usuario.
     * @param wheel posicion de la rueda (1-based)
     * @param symbol color del simbolo a colocar
     */
    public void placeSymbol(int wheel, String symbol)
    {
        ok = false;
        try {
            int wIndex = fixPosition(wheel, wheels.size());
            String lower = symbol.toLowerCase();
            Symbol s = symbols.get(lower);
            if(s == null) {
                showError("No existe un simbolo de color: " + symbol);
                return;
            }
            Wheel w = wheels.get(wIndex - 1);
            select(w,s);
            changesJackpot();
            ok = true;
        } catch(SlotMachineException e) {
            ok = false;
            showError(e.getMessage());
        }
    }

    /**
     * Gira la rueda indicada, dejandola en un simbolo aleatorio del catalogo.
     * Si no hay ruedas o simbolos, o la rueda esta fijada, se informa al usuario.
     * @param wheel posicion de la rueda a girar (1-based)
     */
    public void spin(int wheel)
    {
        ok = false;
        try {
            if(symbols.isEmpty()) {
                showError("No hay ruedas o simbolos para girar.");
                return;
            }
            int wIndex = fixPosition(wheel, wheels.size());
            Wheel w = wheels.get(wIndex - 1);
            if(w.isHeld()) {
                showError("La rueda esta fijada, sueltela antes de rotar.");
                return;
            }
            if(w.wantsToSpin()) {
                select(w,w.chooseSymbol(randomSymbol()));
                afterSpin();   // solo cuenta como giro si la rueda giro
            }
            changesJackpot();
            ok = true;
        } catch(SlotMachineException e) {
            ok = false;
            showError(e.getMessage());
        }
    }
    /**
     * Escoge un symbolo random del catalogo de symbols
     */
    private Symbol randomSymbol(){
    
        if(order.isEmpty()) {
            return null;
        }
        int target = (int)(Math.random() * order.size());
        return symbols.get(order.get(target));
    }
     
    /**
     * Gira todas las ruedas de la maquina que no esten fijadas, de
     * izquierda a derecha. Cuenta como un solo giro de la maquina.
     * Si no hay ruedas o simbolos para girar, se informa al usuario.
     */
    public void spin()
    {
        ok = false;
        if(wheels.isEmpty() || symbols.isEmpty()) {
            showError("No hay ruedas o simbolos para girar.");
            return;
        }
        for(Wheel w : wheels) {
            if(!w.isHeld() && w.wantsToSpin()) {
                select(w,w.chooseSymbol(randomSymbol()));
            }
        }
        afterSpin();
        changesJackpot();
        ok = true;
    }
    
    /**
     * Busca saber los colores de los simbolos creados, en el orden en que
     * estan en la rueda empezando por el 1.
     * @return los colores de los simbolos
     */
    public String[] symbols(){
        ok=true;
        return order.toArray(new String[0]);
    }
    /**
     * Busca saber que simbolo esta en cada rueda 
     * Si no hay simbolos disponibles, se informa al usuario.
     * @return positions los colores de los simbolos de cada rueda
     *         (un arreglo vacio si no hay simbolos)
     */
    public String [] configuration(){
        ok = false;
        if(symbols.isEmpty()) {
            showError("No hay simbolos disponibles.");
            return new String[0];
        }
        String[] positions = currentColors();
        ok = true;
        return positions;
    }
    /**
     * Arma el arreglo con el color que muestra cada rueda ("" si esta vacia),
     * sin validar nada. Lo usan las consultas internas (isJackpot,
     * distinctSymbols) para que no lancen excepcion por un catalogo vacio.
     * @return los colores que muestran las ruedas
     */
    private String[] currentColors(){
        String[] positions = new String[wheels.size()];
        for (int i=0;i< wheels.size();i++){
            Symbol s = wheels.get(i).getCurrentPosition();
            if (s!= null){
                positions[i]= s.getColor();
                }
            else{positions[i]= "";}
        }
        return positions;
    }
    
    /**
     * Cuenta cuantos colores distintos hay actualmente entre las ruedas.
     * @return el numero de simbolos distintos visibles en la maquina
     */
    public int distinctSymbols(){
        ok = false;
        String [] positions = currentColors();
        ArrayList<String> diferent = new ArrayList<String>();
        for (int i=0;i< positions.length;i++){
            if (!positions[i].isEmpty() && !diferent.contains(positions[i])){
                diferent.add(positions[i]);
            }
        }
        ok = true;
        return diferent.size();
        
        }

    /**
     * Indica si la maquina esta en configuracion ganadora.
     * @return true si todas las ruedas muestran el mismo simbolo
     */
    public boolean isJackpot(){
        ok = true;   // consultar nunca falla
        String[] positions = currentColors();
        if(positions.length == 0) {
            return false;
        }
        for(String color : positions){
            if(color.isEmpty() || !color.equals(positions[0])){
            return false;
        }
        }
        return true;
    }
    /**
     * Intercambiar ruedas
     * Intercambia los simbolos que muestran dos ruedas. Si hay menos de
     * dos ruedas o alguna no se deja intercambiar, se informa al usuario.
     * @param wheel1 posicion de la primera rueda (1-based)
     * @param wheel2 posicion de la segunda rueda (1-based)
     */
    public void swap(int wheel1, int wheel2)
    {
        ok = false;
        try {
            int i1 = fixPosition(wheel1, wheels.size());
            int i2 = fixPosition(wheel2, wheels.size());
            if(wheels.size() < 2) {
                showError("Se necesitan al menos dos ruedas para intercambiar.");
                return;
            }
            Wheel w1 = wheels.get(i1 - 1);
            Wheel w2 = wheels.get(i2 - 1);
            if(!w1.canSwap() || !w2.canSwap()) {
                showError("Una de las ruedas no se deja intercambiar.");
                return;
            }

            Symbol temp = w1.getCurrentPosition();
            w1.setCurrentPosition(w2.getCurrentPosition());
            w2.setCurrentPosition(temp);

            changesJackpot();
            ok = true;
        } catch(SlotMachineException e) {
            ok = false;
            showError(e.getMessage());
        }
    }
    /**
     * Fija una rueda para que no gire hasta que se suelte. Si no hay
     * ruedas o la rueda no se deja fijar, se informa al usuario.
     * @param wheel posicion de la rueda a fijar (1-based)
     */
    public void lock(int wheel)
    {
        ok = false;
        try {
            int index = fixPosition(wheel, wheels.size());
            if(!wheels.get(index - 1).canLock()) {
                showError("Esta rueda no se deja fijar.");
                return;
            }
            wheels.get(index - 1).setHeld(true);
            ok = true;
        } catch(SlotMachineException e) {
            ok = false;
            showError(e.getMessage());
        }
    }
    /**
     * Suelta una rueda previamente fijada. Si no hay ruedas, se informa
     * al usuario.
     * @param wheel posicion de la rueda a soltar (1-based)
     */
    public void unlock(int wheel)
    {
        ok = false;
        try {
            int index = fixPosition(wheel, wheels.size());
            wheels.get(index - 1).setHeld(false);
            ok = true;
        } catch(SlotMachineException e) {
            ok = false;
            showError(e.getMessage());
        }
    }
    /**
     * Rota una rueda un numero de pasos dentro de la
     * lista de simbolos, dando la vuelta si se pasa del final (o del
     * inicio, si steps es negativo). Si no hay ruedas o simbolos, o la
     * rueda esta fijada, no la rota y se informa al usuario.
     * @param wheel posicion de la rueda a rotar (1-based)
     * @param steps numero de pasos a avanzar (puede ser negativo)
     */
    public void spin(int wheel, int steps)
    {
        ok = false;
        try {
            if(symbols.isEmpty()) {
                showError("No hay ruedas o simbolos para rotar.");
                return;
            }
            int index = fixPosition(wheel, wheels.size());
            Wheel w = wheels.get(index - 1);
            if(w.isHeld()) {
                showError("La rueda esta fijada, sueltela antes de rotar.");
                return;
            }
            if(!w.wantsToSpin()) {
                ok = true;
                return;
            }

            Symbol currentSymbol= w.getCurrentPosition();
            int size = order.size();
            int start = 0;
            if(currentSymbol != null) {
                start = order.indexOf(currentSymbol.getColor());
                if(start < 0) {
                start = 0;
                }
            }

            int direction = (steps < 0) ? -1 : 1;
            for(int i = 0; i < Math.abs(steps); i++) {
                start = (start + direction + size) % size;   // +size basta: direction es -1 o 1
                w.setCurrentPosition(symbols.get(order.get(start)));
                if(visible) {
                Canvas.getCanvas().wait(120);
                }
            }
            select(w,w.chooseSymbol(w.getCurrentPosition()));
            afterSpin();
            changesJackpot();
            ok = true;
        } catch(SlotMachineException e) {
            ok = false;
            showError(e.getMessage());
        }
    }
    /**
     * Deja la maquina en la configuracion dada, un
     * color por cada rueda, en el mismo orden de las ruedas. Las ruedas
     * fijadas no se modifican. Si no hay ruedas o simbolos, si la cantidad
     * de colores no coincide con la de ruedas, o si algun color no existe,
     * no cambia nada y se informa al usuario.
     * @param setSymbols arreglo con el color deseado para cada rueda
     */
    public void spin(String[] setSymbols)
    {
        ok = false;
        if(wheels.isEmpty() || symbols.isEmpty()) {
            showError("No hay ruedas o simbolos disponibles.");
            return;
        }
        if(setSymbols.length != wheels.size()) {
            showError("La cantidad de colores no coincide con la cantidad de ruedas.");
            return;
        }
        for(int i = 0; i < setSymbols.length; i++) {
            String lower = setSymbols[i].toLowerCase();
            if(!symbols.containsKey(lower)) {
                showError("No existe un simbolo de color: " + setSymbols[i]);
                return;
            }
        }

        for(int i = 0; i < setSymbols.length; i++) {
            Wheel w = wheels.get(i);
            if(!w.isHeld()) {
                String lower = setSymbols[i].toLowerCase();
                Symbol sIndex = symbols.get(lower);
                select(w,sIndex);
            }
        }
        changesJackpot();
        ok = true;
    }
    /**
     * Avisa a todos los simbolos que hubo un giro (el efimero se encoge).
     */
    private void afterSpin()
    {
        for(Symbol s : symbols.values()) {
            s.onSpin();
        }
    }
    /**
     * Deja la rueda en el simbolo dado y le avisa al simbolo que fue
     * seleccionado (el timido alterna entre visible e invisible).
     * @param w la rueda
     * @param s el simbolo en el que queda la rueda
     */
    private void select(Wheel w, Symbol s)
    {
        if(s != null) {
            s.onSelected();
        }
        w.setCurrentPosition(s);
    }
    /**
     * Muestra un mensaje de error al usuario en un JOptionPane,
     * unicamente si el simulador esta visible.
     * @param message el mensaje a mostrar
     */
    private void showError(String message)
    {
        if(visible) {
            JOptionPane.showMessageDialog(null, message, "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Ajusta una posicion 1-based al rango valido [1, max].
     * @param pos posicion solicitada
     * @param max valor maximo permitido
     * @return la posicion corregida
     * @throws SlotMachineException si max es menor que 1: no hay ruedas,
     *         asi que no existe ninguna posicion valida
     */
    private int fixPosition(int pos, int max) throws SlotMachineException
    {
        if(max < 1) {
            throw new SlotMachineException(SlotMachineException.NO_WHEELS);
        }
        if(pos < 1) {
            return 1;
        }
        if(pos > max) {
            return max;
        }
        return pos;
    }
    /**
     * Actualiza el color de la maquina segun si hay jackpot, y
     * redibuja las ruedas para que queden por encima del cuerpo.
     */
    private void changesJackpot(){
        if(isJackpot()) {
            body.changeColor("green");
        } else {
            body.changeColor("blue");
        }
        if(visible) {
            for(Wheel w : wheels) {
                w.makeVisible();  
                }
            }
            
        }
    
    }