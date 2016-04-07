import java.util.Random;

public class Main {

    private static QueryGen qg = new QueryGen();

    public static void main(String [] args){

        //executeNQueries(1000);
        //executeAllQueries();
        executeQueryNtimes(18,1);
    }

    public static void executeAllQueries(){

        System.out.println("Ejecutando query 1");
        System.out.println("\t" + qg.query1() + " milisegundos.");

        System.out.println("Ejecutando query 2");
        System.out.println("\t" + qg.query2() + " milisegundos.");

        System.out.println("Ejecutando query 3");
        System.out.println("\t" + qg.query3() + " milisegundos.");

        System.out.println("Ejecutando query 4");
        System.out.println("\t" + qg.query4() + " milisegundos.");

        System.out.println("Ejecutando query 5");
        System.out.println("\t" + qg.query5() + " milisegundos.");

        System.out.println("Ejecutando query 6");
        System.out.println("\t" + qg.query6() + " milisegundos.");

        System.out.println("Ejecutando query 7");
        System.out.println("\t" + qg.query7() + " milisegundos.");

        System.out.println("Ejecutando query 8");
        System.out.println("\t" + qg.query8() + " milisegundos.");

        System.out.println("Ejecutando query 9");
        System.out.println("\t" + qg.query9() + " milisegundos.");

        System.out.println("Ejecutando query 10");
        System.out.println("\t" + qg.query10() + " milisegundos.");

        System.out.println("Ejecutando query 11");
        System.out.println("\t" + qg.query11() + " milisegundos.");

        System.out.println("Ejecutando query 12");
        System.out.println("\t" + qg.query12() + " milisegundos.");

        System.out.println("Ejecutando query 13");
        System.out.println("\t" + qg.query13() + " milisegundos.");

        System.out.println("Ejecutando query 14");
        System.out.println("\t" + qg.query14() + " milisegundos.");

        System.out.println("Ejecutando query 15");
        System.out.println("\t" + qg.query15() + " milisegundos.");

        System.out.println("Ejecutando query 16");
        System.out.println("\t" + qg.query16() + " milisegundos.");

        System.out.println("Ejecutando query 17");
        System.out.println("\t" + qg.query17() + " milisegundos.");

        System.out.println("Ejecutando query 18");
        System.out.println("\t" + qg.query18() + " milisegundos.");

        System.out.println("Ejecutando query 19");
        System.out.println("\t" + qg.query19() + " milisegundos.");

        System.out.println("Ejecutando query 20");
        System.out.println("\t" + qg.query20() + " milisegundos.");

        System.out.println("Ejecutando query 21");
        System.out.println("\t" + qg.query21() + " milisegundos.");

        System.out.println("Ejecutando query 22");
        System.out.println("\t" + qg.query22() + " milisegundos.");

        System.out.println("Fin");
    }

    public static void executeNQueries(int nQueries){

        Random rnd = new Random();

        for(int i=0;i<nQueries;i++){

            int queryNum = rnd.nextInt(23);

            switch (queryNum){
                case 0:
                    System.out.println("1 " + qg.query1());
                    break;
                case 1:
                    System.out.println("2 " + qg.query2());
                    break;
                case 2:
                    System.out.println("3 " + qg.query3());
                    break;
                case 3:
                    System.out.println("4 " + qg.query4());
                    break;
                case 4:
                    System.out.println("5 " + qg.query5());
                    break;
                case 5:
                    System.out.println("6 " + qg.query6());
                    break;
                case 6:
                    System.out.println("7 " + qg.query7());
                    break;
                case 7:
                    System.out.println("8 " + qg.query8());
                    break;
                case 8:
                    System.out.println("9 " + qg.query9());
                    break;
                case 9:
                    System.out.println("10 " + qg.query10());
                    break;
                case 10:
                    System.out.println("11 " + qg.query11());
                    break;
                case 11:
                    System.out.println("12 " + qg.query12());
                    break;
                /*case 12:
                    System.out.println("13 " + qg.query13());
                    break;*/
                case 13:
                    System.out.println("14 " + qg.query14());
                    break;
                case 14:
                    System.out.println("15 " + qg.query15());
                    break;
                case 15:
                    System.out.println("16 " + qg.query16());
                    break;
                case 16:
                    System.out.println("17 " + qg.query17());
                    break;
                case 17:
                    System.out.println("18 " + qg.query18());
                    break;
                case 18:
                    System.out.println("19 " + qg.query19());
                    break;
                case 19:
                    System.out.println("20 " + qg.query20());
                    break;
                case 20:
                    System.out.println("21 " + qg.query21());
                    break;
                case 21:
                    System.out.println("22 " + qg.query22());
                    break;
            }
        }

    }

    public static void executeQueryNtimes(int query, int n){
        for(int i=0;i<n;i++){
            System.out.println("Ejecutando query " + query + " " + n + " veces");

            System.out.println("Ejecución: " + (i+1) +" Tiempo:");

            switch (query-1){
                case 0:
                    System.out.println(qg.query1());
                    break;
                case 1:
                    System.out.println(qg.query2());
                    break;
                case 2:
                    System.out.println(qg.query3());
                    break;
                case 3:
                    System.out.println(qg.query4());
                    break;
                case 4:
                    System.out.println(qg.query5());
                    break;
                case 5:
                    System.out.println(qg.query6());
                    break;
                case 6:
                    System.out.println(qg.query7());
                    break;
                case 7:
                    System.out.println(qg.query8());
                    break;
                case 8:
                    System.out.println(qg.query9());
                    break;
                case 9:
                    System.out.println(qg.query10());
                    break;
                case 10:
                    System.out.println(qg.query11());
                    break;
                case 11:
                    System.out.println(qg.query12());
                    break;
                case 12:
                    System.out.println(qg.query13());
                    break;
                case 13:
                    System.out.println(qg.query14());
                    break;
                case 14:
                    System.out.println(qg.query15());
                    break;
                case 15:
                    System.out.println(qg.query16());
                    break;
                case 16:
                    System.out.println(qg.query17());
                    break;
                case 17:
                    System.out.println(qg.query18());
                    break;
                case 18:
                    System.out.println(qg.query19());
                    break;
                case 19:
                    System.out.println(qg.query20());
                    break;
                case 20:
                    System.out.println(qg.query21());
                    break;
                case 21:
                    System.out.println(qg.query22());
                    break;
            }
        }
    }

}
