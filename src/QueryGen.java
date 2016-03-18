import java.sql.*;
import java.util.Random;

public class QueryGen {

    static final String STARTDATE = "1992-01-01";
    static final String CURRENTDATE = "1995-06-17";
    static final String ENDDATE = "1998-12-31";

    static final String DATABASE_NAME = "tpch";
    static final String USERNAME = "tpch";
    static final String PASSWORD = "tpch";

    private Random rnd;
    private Connection connection;

    public QueryGen(){
        rnd = new Random();
        connectToBD();
    }


    public boolean executeQuery(String query){
        try {
            Statement stm = getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            ResultSet rs = stm.executeQuery(query);

            System.out.println(rs.getFetchSize() + " row(s) returned");
            //TODO: Print row returned don't works
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        //TODO: Write execution time.

        //System.out.println(query);
        return true;
    }

    public void connectToBD() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String DB = "jdbc:mysql://localhost/" + DATABASE_NAME + "?user=" + USERNAME + "&password=" + PASSWORD;
            setConnection(DriverManager.getConnection(DB));
            if(getConnection() != null){
                System.out.println("Connection success!");
            }else{
                System.out.println("Connection Error!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String query1(){

        // arg1 must be an integer value between 60 and 120.
        int arg1 = 60 + rnd.nextInt(60);

        return "select " +
                " l_returnflag," +
                " l_linestatus," +
                " sum(l_quantity) as sum_qty," +
                " sum(l_extendedprice) as sum_base_price," +
                " sum(l_extendedprice * (1 - l_discount)) as sum_disc_price," +
                " sum(l_extendedprice * (1 - l_discount) * (1 + l_tax)) as sum_charge," +
                " avg(l_quantity) as avg_qty," +
                " avg(l_extendedprice) as avg_price," +
                " avg(l_discount) as avg_disc," +
                " count(*) as count_order" +
                " from" +
                " lineitem" +
                " where" +
                " l_shipdate <= date '1998-12-01' - interval "+arg1+" day " +
                " group by" +
                " l_returnflag," +
                " l_linestatus" +
                " order by" +
                " l_returnflag," +
                " l_linestatus;";
    }



    public String query2(){

        // arg1 must be an integer value between 60 and 120.
        int arg1 = 1 + rnd.nextInt(50);

        String arg2= getRndParttype();

        String arg3 = getRndRegion();

        return "select\n" +
                " s_acctbal," +
                " s_name," +
                " n_name," +
                " p_partkey," +
                " p_mfgr," +
                " s_address," +
                " s_phone," +
                " s_comment" +
                " from" +
                " part," +
                " supplier," +
                " partsupp," +
                " nation," +
                " region" +
                " where" +
                " p_partkey = ps_partkey" +
                " and s_suppkey = ps_suppkey" +
                " and p_size = "+ arg1 +
                " and p_type like '"+ arg2 +"'" +
                " and s_nationkey = n_nationkey" +
                " and n_regionkey = r_regionkey" +
                " and r_name = '"+ arg3 +"'" +
                " and ps_supplycost = (" +
                " select" +
                " min(ps_supplycost)" +
                " from" +
                " partsupp," +
                " supplier," +
                " nation," +
                " region" +
                " where" +
                " p_partkey = ps_partkey" +
                " and s_suppkey = ps_suppkey" +
                " and s_nationkey = n_nationkey" +
                " and n_regionkey = r_regionkey" +
                " and r_name = '"+ arg3 +"'" +
                " )" +
                " order by" +
                " s_acctbal desc," +
                " n_name," +
                " s_name," +
                " p_partkey;";
    }

    public String query3(){

        // arg1 must be an integer value between 60 and 120.
        String arg1 = getRndMtksegment();

        String arg2= "1995-03-" + (rnd.nextInt(32)+1);

        return " select" +
                " l_orderkey," +
                " sum(l_extendedprice*(1-l_discount)) as revenue," +
                " o_orderdate," +
                " o_shippriority" +
                " from" +
                " customer," +
                " orders," +
                " lineitem" +
                " where" +
                " c_mktsegment = '"+arg1+"'" +
                " and c_custkey = o_custkey" +
                " and l_orderkey = o_orderkey" +
                " and o_orderdate < date '" +arg2+ "'" +
                " and l_shipdate > date '" +arg2+ "'" +
                " group by" +
                " l_orderkey," +
                " o_orderdate," +
                " o_shippriority" +
                " order by" +
                " revenue desc," +
                " o_orderdate;";
    }



    public String query4(){

        /*  1.DATE (arg1) is the first day of a randomly selected month between the first month of 1993
            and the 10th month of 1997.*/

        String arg1= "199"+(rnd.nextInt(5)+3)+"-"+(rnd.nextInt(10)+1)+"-01" ;

        return "select" +
                " o_orderpriority," +
                " count(*) as order_count" +
                " from" +
                " orders" +
                " where" +
                " o_orderdate >= date '"+arg1+"'" +
                " and o_orderdate < date '"+arg1+"' + interval '3' month" +
                " and exists (" +
                " select" +
                " *" +
                " from" +
                " lineitem" +
                " where" +
                " l_orderkey = o_orderkey" +
                " and l_commitdate < l_receiptdate" +
                " )" +
                " group by" +
                " o_orderpriority" +
                " order by" +
                " o_orderpriority;";
    }

    public String query5(){

        /*  1. REGION (arg1) is randomly selected within the list of values defined for R_NAME;
            2. DATE (arg2) is the first of January of a randomly selected year within [1993 .. 1997].*/

        String arg1 = getRndRegion();
        String arg2= "199"+(rnd.nextInt(5)+3)+"-01-01" ;


        return "select" +
                " n_name," +
                " sum(l_extendedprice * (1 - l_discount)) as revenue" +
                " from" +
                " customer," +
                " orders," +
                " lineitem," +
                " supplier," +
                " nation," +
                " region" +
                " where" +
                " c_custkey = o_custkey" +
                " and l_orderkey = o_orderkey" +
                " and l_suppkey = s_suppkey" +
                " and c_nationkey = s_nationkey" +
                " and s_nationkey = n_nationkey" +
                " and n_regionkey = r_regionkey" +
                " and r_name = '"+arg1+"'" +
                " and o_orderdate >= date '"+arg2+"'" +
                " and o_orderdate < date '"+arg2+"' + interval '1' year" +
                " group by" +
                " n_name" +
                " order by" +
                " revenue desc;";
    }


    public String query6(){

        /*  1. DATE (arg1) is the first of January of a randomly selected year within [1993 .. 1997];
            2. DISCOUNT (arg2) is randomly selected within [0.02 .. 0.09];
            3. QUANTITY (arg3) is randomly selected within [24 .. 25].*/

        String arg1 = "199"+(rnd.nextInt(5)+3)+"-01-01" ;
        float arg2= (rnd.nextInt(7)+2)/100;
        int arg3 = rnd.nextInt(2) + 24;


        return "select" +
                " sum(l_extendedprice*l_discount) as revenue" +
                " from" +
                " lineitem" +
                " where" +
                " l_shipdate >= date '"+arg1+"'" +
                " and l_shipdate < date '"+arg1+"' + interval '1' year" +
                " and l_discount between "+arg2+" - 0.01 and "+arg2+" + 0.01" +
                " and l_quantity < "+arg3+";";
    }



    private String getRndMtksegment() {

        String result = "";

        int val = rnd.nextInt(5);

        switch (val){
            case 0:
                result = "AUTOMOBILE";
                break;
            case 1:
                result = "BUILDING";
                break;
            case 2:
                result = "FURNITURE";
                break;
            case 3:
                result = "MACHINERY";
                break;
            case 4:
                result = "HOUSEHOLD";
                break;
        }

        return result;
    }

    public String getRndParttype(){

        String result = "";

        int val = rnd.nextInt(5);

        switch (val){
            case 0:
                result = "TIN";
                break;
            case 1:
                result = "NICKEL";
                break;
            case 2:
                result = "BRASS";
                break;
            case 3:
                result = "STEEL";
                break;
            case 4:
                result = "COPPER";
                break;
        }

        return result;
    }

    public String getRndRegion(){

        String result = "";

        int val = rnd.nextInt(5);

        switch (val){
            case 0:
                result = "AFRICA";
                break;
            case 1:
                result = "AMERICA";
                break;
            case 2:
                result = "ASIA";
                break;
            case 3:
                result = "EUROPE";
                break;
            case 4:
                result = "MIDDLE EAST";
                break;
        }

        return result;
    }

}
