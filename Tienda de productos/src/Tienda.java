import java.sql.*;

public class Tienda {

    public Connection conexion;
    public boolean tiendaIsOpen;


    //Cosntructor
    public Tienda(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection ("jdbc:mysql://localhost/prueba_tecnicabdd","root", "password");
            tiendaIsOpen=true;
            //////////////////Productos iniciales para testear//////////
            Statement s = conexion.createStatement();
            s.executeUpdate("USE prueba_tecnicabdd");
            s.executeUpdate("INSERT INTO Producto VALUES (NULL,'Oreos', 80, 'Masitas')");
            s.executeUpdate("INSERT INTO Producto VALUES (NULL,'Play 5', 200000, 'Gaming')");
            s.executeUpdate("INSERT INTO Producto VALUES (NULL,'DsicoSSD', 5000, 'Hardware')");
            s.executeUpdate("INSERT INTO Producto VALUES (NULL,'Motherboard', 2000, 'Hardware')");
            s.executeUpdate("INSERT INTO Producto VALUES (NULL,'RAM', 6000, 'Hardware')");
            s.executeUpdate("INSERT INTO Producto VALUES (NULL,'XBOX S', 180000, 'Gaming')");
            s.executeUpdate("INSERT INTO Producto VALUES (NULL,'TLOU 2', 4000, 'Gaming')");
            s.executeUpdate("INSERT INTO Producto VALUES (NULL,'Funda Iphone', 1000, 'Celulares')");
            s.close();
            ////////////////////////////////////////////////////////////
            System.out.println("Bienvenido a la tienda!\n");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /*
    /   +isOpen() : boolean
    /   La funcionalidad de este metodo es retornar true si la tienda esta abierta y 
    /   falso en caso de que se cierre.
    */
    public boolean isOpen(){
        return tiendaIsOpen;
    }
    
    /*
    /  +showMenu():void
    /   Este metodo muestra por consola un menu que representa el menu principal de la tienda.
    /   Muestra las opciones y espera a que el usuario ingrese un valor correspondiente a alguna accion
    /   para ejecutar la misma.
    */
    public void showMenu(){
        System.out.println("\n//////////Menu///////////\n");
        System.out.println("1- Almacenar un nuevo producto\n");
        System.out.println("2- Buscar productos\n");
        System.out.println("3- Registrar nuevo vendedor\n");
        System.out.println("4- Realizar venta\n");
        System.out.println("5- Obtener comision de un vendedor(falta implementar)\n");
        System.out.println("6- Salir\n");
        
        String input =System.console().readLine();
        action(input);
    }
    /*
    / -ralizarVenta(): void 
    /  Este metodo se encarga de tomar los datos para registrar una venta e introducirlos a la bdd.
    /
    */
    private void realizarVenta(){
        try{   
            System.out.println("\nSi desea cancelar la operacion en cualquier momento ingrese el valor '-1'");
            int codigo_producto=0;
            System.out.println("\nIngresar el codigo del producto:");
            int cp=Integer.parseInt(System.console().readLine());
            if(cp==-1){
                //Vuelve al menu
            }
            else{
                Statement s = conexion.createStatement();
                ResultSet rs= s.executeQuery("SELECT nombre_p,precio_p,codigo_p FROM Producto WHERE codigo_p="+cp);
                boolean vacio=true;
                while(rs.next()){
                    vacio=false;
                    System.out.println("\nProducto: "+rs.getString(1)+", Precio:"+rs.getInt(2));
                }
                if(vacio){
                    System.out.println("\nNo se encuentra el producto.");
                }
                else{
                    System.out.println("\nIngrese el ID del vendedor:");
                    int id_vendedor=Integer.parseInt(System.console().readLine());
                    rs=s.executeQuery("SELECT nombre_v FROM Vendedor WHERE id_v="+id_vendedor);
                    if(rs.next() && id_vendedor!=-1){
                        s.executeUpdate("INSERT INTO Venta VALUES ("+id_vendedor+","+cp+")");
                        System.out.println("Venta Realizada");
                    }
                    else{
                        System.out.println("No se encuentra el vendedor o se quiso cancelar la venta.");
                    }
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        catch(NumberFormatException e){
            System.out.println("\nERROR: debe ingresar un numero.");
        }  
    }
    /*
    / -action(in: String): void 
    /  Este metodo ejecuta el metodo correspondiente segun el String in 
    /  que recibe por parametro.
    */
    private void action(String in){
        try{
            boolean validInput=false;
            
            if(in.equals("1")){
                almacenarProducto();
                validInput=true;
            }

            if(in.equals("2")){
                boolean aux=false;
                while(!aux){
                    aux=elegirTipoDeBusqueda();
                }
                validInput=true;
            }

            if(in.equals("3")){
                regNuevoVendedor();
                validInput=true;
            }

            if(in.equals("4")){
                realizarVenta();
                validInput=true;                
            }
            if(in.equals("5")){
                consultarComision();
                validInput=true;
            }

            if(in.equals("6")){
                conexion.close();
                tiendaIsOpen=false;
                System.out.println("\nAdios!");
                validInput=true;
            }

            if(!validInput){
                System.out.println("La opcion '"+in+"' no es valida.");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void consultarComision(){

    }

    /*
    / -regVendedorNuevo():void
    /  Este metodo registra o no (cancelacion o datos erroneos) a un vendedor en la base de datos.
    */
    private void regNuevoVendedor(){
        System.out.println("\nSi desea cancelar la operacion en cualquier momento ingrese el valor '-1'");
        /////////////////Input name
        String nombre="";
        boolean name=false;
        boolean cancelar =false;
        while(!name && !cancelar){
            System.out.println("\nIngrese el nombre del Vendedor(50 max caracteres):");
            nombre=System.console().readLine();
            if(!(nombre.equals("-1"))){
                if(nombre.length()>50 || nombre.length()==0){
                    System.out.println("\nERROR: el nombre debe tener entre 1 y 50 caracteres.");
                    nombre="";
                }
                else{
                    name=true;
                }
            }
            else{
                cancelar=true;
            }
        }
        /////////////////Input sueldo
        Float sueldo= 0F;
        boolean validFloat=false;
        while(!validFloat && !cancelar){
            System.out.println("\nIngrese el sueldo del vendedor(en caso de ingresar decimales usar '.'. Ejemplo:40.20(cuarenta $ con 20 centavos de $)):");
            try{
                sueldo=Float.parseFloat(System.console().readLine());
                if(!(sueldo==-1F)){
                    if(sueldo<0){
                        System.out.println("\nERROR: el numero no puede ser negativo.");
                    }
                    else{
                        validFloat=true;
                    }
                }
                else{
                    cancelar=true;
                }
            }
            catch(NumberFormatException e){
                System.out.println("\nERROR: debe ingresar un numero.");
            }  
        }
        //Fin de el ingreso de datos. Luego se mandan o no los datos a la bdd.
        try{
            if(!cancelar){
                Statement s = conexion.createStatement();
                s.executeUpdate("INSERT INTO Vendedor VALUES (NULL,'"+nombre+"',"+sueldo+")");
                System.out.println("\nVendedor añadido!.");
                System.out.println("\nNombre: "+nombre+", Sueldo: "+sueldo+".");
                s.close();
            }
        }
        catch(SQLException e){;
            System.out.println(e.getMessage());
        }
    }

    /*
    /   -alamcenatProducto():void
    /   Este metodo le permite al usuario ingresar un producto nuevo a la base de datos.
    /   Muestra por consola los pasos y realiza los controles correspondientes para poder
    /   ingresar el producto nuevo de forma correcta.
    */
    private void almacenarProducto(){
        System.out.println("\nSi desea cancelar la operacion en cualquier momento ingrese el valor '-1'");
        /////////////////Input name
        String nombre="";
        boolean name=false;
        boolean cancelar =false;
        while(!name && !cancelar){
            System.out.println("\nIngrese el nombre del producto(50 max caracteres):");
            nombre=System.console().readLine();
            if(!(nombre.equals("-1"))){
                if(nombre.length()>50 || nombre.length()==0){
                    System.out.println("\nERROR: el nombre debe tener entre 1 y 50 caracteres.");
                    nombre="";
                }
                else{
                    name=true;
                }
            }
            else{
                cancelar=true;
            }
        }
        /////////////////Input precio
        Float precio= 0F;
        boolean validFloat=false;
        while(!validFloat && !cancelar){
            System.out.println("\nIngrese el precio de compra del producto(en caso de ingresar decimales usar '.'. Ejemplo:40.20(cuarenta $ con 20 centavos de $)):");
            try{
                precio=Float.parseFloat(System.console().readLine());
                if(!(precio==-1F)){
                    if(precio<0){
                        System.out.println("\nERROR: el numero no puede ser negativo.");
                    }
                    else{
                        validFloat=true;
                    }
                }
                else{
                    cancelar=true;
                }
            }
            catch(NumberFormatException e){
                System.out.println("\nERROR: debe ingresar un numero.");
            }  
        }
        ////////////////////Input categoria
        String categoria="";
        boolean validCategoria=false;
        while(!validCategoria && !cancelar){
            System.out.println("\nIngrese la categoria a la que pertenece el producto(50 max caracteres):");
            categoria=System.console().readLine();
            if(!(categoria.equals("-1"))){
                if(categoria.length()>50 || categoria.length()==0){
                    System.out.println("\nERROR: la categoria debe tener entre 1 y 50 caracteres.");
                    categoria="";
                }
                else{
                    validCategoria=true;
                }
            }
            else{
                cancelar=true;
            }
        }
        //Fin de el ingreso de datos. Luego se mandan o no los datos a la bdd.
        try{
            if(!cancelar){
                Statement s = conexion.createStatement();
                s.executeUpdate("INSERT INTO Producto VALUES (NULL,'"+nombre+"',"+precio+",'"+categoria+"')");
                System.out.println("\nProducto añadido a la tineda!.");
                System.out.println("\nNombre: "+nombre+", Precio: "+precio+", Categoria: "+categoria+".");
                s.close();
            }
        }
        catch(SQLException e){;
            System.out.println("Ya existe un elemento con este nombre");
        }
    }

    /*
    /   -elegirTipoDeBusqueda(): void
    /   Este metodo despliega un menu de opciones para que el usuario elija la accion que
    /   desea realizar e ingrese el valor correspondiente a la misma.
    /   Retorna true si se selecciono la opcion "S" (es decir que se desea volver al menu principal)
    /   y fasle en caso contrario.
    */
    private boolean elegirTipoDeBusqueda(){
        System.out.println("\n A- Ver listado de productos");
        System.out.println("\n B- Buscar por categoria");
        //System.out.println("\n C- Buscar por nombre");
        System.out.println("\n S- Volver al menu");
        System.out.println("\nIngrese la opcion a realizar:");
        String input= System.console().readLine();
        return actionBP(input);
    }

    /*
    / -actionBP(in:String) : boolean
    /  Este metodo ejecuta el metodo correspondiente segun el String in 
    /  que recibe por parametro.
    /  Retorna true si el String recibido por párametro es "S" y false en caso contrario.
    */
    private boolean actionBP(String in){
        if(in.equals("A")){
            verListadoDeProductos();
            return false;
        }
        if(in.equals("B")){
            buscarPorCategoria();
            return false;
        }
        if(in.equals("S")){
            return true;
        }
        System.out.println("\nLa opcion '"+in+"' no es valida.");
        return false;
    }

    /*
    / -buscarPorCategoria():void
    /  Este metodo lee todos los productos de la base de datos, para luego mostrar por consola 
    /  los productos que correspondan a la categoria ingresada por el usuario.
    /
    */
    private void buscarPorCategoria(){
        try{
            boolean encontro=false;
            while(!encontro){
                System.out.println("\nIngrese el nombre de la categoria que desea buscar (ingrese 'Atras' para cancelar):");
                String categoria=System.console().readLine();
                if(categoria.equals("Atras")){//no se puede guardar una categoria llamada Atras en la bd.
                    encontro=true;
                }
                else{
                    Statement s = conexion.createStatement();
                    ResultSet rs= s.executeQuery("SELECT * FROM Producto WHERE categoria_p='"+categoria+"'");
                    while(rs.next()){
                        encontro=true;
                        System.out.println(rs.getString(2)+", Precio:$"+rs.getFloat(3)+",Codigo de compra:"+rs.getInt(1)+", Categoria="+categoria);
                    }
                    s.close();
                    if(!encontro){
                        System.out.println("La categoria ingresada no existe.");
                    }
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    /*
    /  -verListadoDeProductos():void 
    /  Este metodo muestra por consola todos los productos almacenados en la base de datos.
    /
    */
    private void verListadoDeProductos(){
        try{
            Statement s = conexion.createStatement();
            ResultSet rs =s.executeQuery ("SELECT * FROM Producto");
            System.out.println("\nProductos:");
            while (rs.next()){
                System.out.println (""+rs.getString(2)+", Precio:$"+rs.getFloat(3)+",Codigo de compra:"+rs.getInt(1));
            }
            s.close(); 
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
    }
    
    
}