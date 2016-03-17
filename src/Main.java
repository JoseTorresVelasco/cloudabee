
public class Main {
    public static void main(String [] args){
        QueryGen qg = new QueryGen();
        System.out.println("Ejecutando query 1");
        qg.executeQuery(qg.query1());

        System.out.println("Ejecutando query 2");
        qg.executeQuery(qg.query2());

        System.out.println("Ejecutando query 3");
        qg.executeQuery(qg.query3());

        System.out.println("Fin");
    }


}
