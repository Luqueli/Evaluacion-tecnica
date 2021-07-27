import java.sql.*;



public class App {
    public static void main(String[] args) throws Exception {
        try{
            Tienda tienda = new Tienda();
            while(tienda.isOpen()){
                tienda.showMenu();
            }
        } 
        catch (Exception e){
           e.printStackTrace();
        }
        
    }
}
