import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

enum QueryExecutionMode {SELECT, UPDATE}

public class QueryGen {

    static final String STARTDATE = "1992-01-01";
    static final String CURRENTDATE = "1995-06-17";
    static final String ENDDATE = "1998-12-31";
    static final int SCALE_FACTOR = 1;

    static final String HOST = "localhost:3306";
    static final String DATABASE_NAME = "tpch";
    static final String USERNAME = "tpch";
    static final String PASSWORD = "tpch";

    private Random rnd;
    private Connection connection;

    public QueryGen(){
        rnd = new Random();
        connectToBD();
    }


    public boolean executeQuery(String query, QueryExecutionMode mode){
        try {

            Statement stm = getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if(mode == QueryExecutionMode.SELECT) {
                ResultSet rs = stm.executeQuery(query);

                //System.out.println(rs.getFetchSize() + " row(s) returned");
                //TODO: Print row returned don't works
            }else{
                stm.execute(query);
            }
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        //TODO: Write execution time.

        return true;
    }

    public void connectToBD() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String DB = "jdbc:mysql://"+HOST+"/" + DATABASE_NAME + "?user=" + USERNAME + "&password=" + PASSWORD;
            setConnection(DriverManager.getConnection(DB));
            if(getConnection() != null){
                //System.out.println("Connection success!");
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

    public long query1(){

        // arg1 must be an integer value between 60 and 120.
        int arg1 = 60 + rnd.nextInt(60);

        String query = "select " +
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

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;
    }



    public long query2(){

        // arg1 must be an integer value between 60 and 120.
        int arg1 = 1 + rnd.nextInt(50);

        String arg2= getRndParttype();

        String arg3 = getRndRegion();

        String query = "select\n" +
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
        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;
    }

    public long query3(){

        /*  1. SEGMENT is randomly selected within the list of values defined for Segments;
            2. DATE is a randomly selected day within [1995-03-01 .. 1995-03-31].*/
        String arg1 = getRndMtksegment();

        String arg2;

        int day = rnd.nextInt(31)+1;

        if(day<10){
            arg2= "1995-03-0" + day;
        }else{
            arg2= "1995-03-" + day;
        }

        String query =  " select" +
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

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;
    }



    public long query4(){

        /*  1.DATE (arg1) is the first day of a randomly selected month between the first month of 1993
            and the 10th month of 1997.*/

        int y = (rnd.nextInt(5)+3);

        int m = (rnd.nextInt(12)+1);

        if(y==7 && m>10){
            m=10;
        }

        String month;
        if(m<10){
            month = "0"+m;
        }else{
            month = ""+m;
        }

        String arg1= "199"+y+"-"+month+"-01" ;

        String query = "select" +
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

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;
    }

    public long query5(){

        /*  1. REGION (arg1) is randomly selected within the list of values defined for R_NAME;
            2. DATE (arg2) is the first of January of a randomly selected year within [1993 .. 1997].*/

        String arg1 = getRndRegion();
        String arg2= "199"+(rnd.nextInt(5)+3)+"-01-01" ;


        String query = "select" +
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
        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }


    public long query6(){

        /*  1. DATE (arg1) is the first of January of a randomly selected year within [1993 .. 1997];
            2. DISCOUNT (arg2) is randomly selected within [0.02 .. 0.09];
            3. QUANTITY (arg3) is randomly selected within [24 .. 25].*/

        String arg1 = "199"+(rnd.nextInt(5)+3)+"-01-01" ;
        float arg2= (rnd.nextInt(7)+2)/100;
        int arg3 = rnd.nextInt(2) + 24;


        String query = "select" +
                " sum(l_extendedprice*l_discount) as revenue" +
                " from" +
                " lineitem" +
                " where" +
                " l_shipdate >= date '"+arg1+"'" +
                " and l_shipdate < date '"+arg1+"' + interval '1' year" +
                " and l_discount between "+arg2+" - 0.01 and "+arg2+" + 0.01" +
                " and l_quantity < "+arg3+";";

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query7(){

        /*  1. NATION1 (arg1) is randomly selected within the list of values defined for N_NAME;
            2. NATION2 (arg2) is randomly selected within the list of values defined*/

        String arg1 = getRndNation() ;
        String arg2= getRndNation();


        String query = " select" +
                " supp_nation," +
                " cust_nation," +
                " l_year, sum(volume) as revenue" +
                " from (" +
                " select" +
                " n1.n_name as supp_nation," +
                " n2.n_name as cust_nation," +
                " extract(year from l_shipdate) as l_year," +
                " l_extendedprice * (1 - l_discount) as volume" +
                " from" +
                " supplier," +
                " lineitem," +
                " orders," +
                " customer," +
                " nation n1," +
                " nation n2" +
                " where" +
                " s_suppkey = l_suppkey" +
                " and o_orderkey = l_orderkey" +
                " and c_custkey = o_custkey" +
                " and s_nationkey = n1.n_nationkey" +
                " and c_nationkey = n2.n_nationkey" +
                " and (" +
                " (n1.n_name = '"+arg1+"' and n2.n_name = '"+arg2+"')" +
                " or (n1.n_name = '"+arg2+"' and n2.n_name = '"+arg1+"')" +
                " )" +
                " and l_shipdate between date '1995-01-01' and date '1996-12-31'" +
                " ) as shipping" +
                " group by" +
                " supp_nation," +
                " cust_nation," +
                " l_year" +
                " order by" +
                " supp_nation," +
                " cust_nation," +
                " l_year;";

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query8(){

        /*  1. NATION (arg1) is randomly selected within the list of values defined for N_NAME;
            2. REGION (arg2) is the value defined  for R_NAME where R_REGIONKEY corresponds to
               N_REGIONKEY for the selected NATION in item 1 above;
            3. TYPE (arg3) is randomly selected within the list of 3-syllable strings defined for Types*/

        String arg1 = getRndNation() ;
        String arg2= getRegionByNation(arg1);
        String arg3 = getRndParttype();


        String query = "select" +
                " o_year," +
                " sum(case" +
                " when nation = '"+arg1+"'" +
                " then volume" +
                " else 0" +
                " end) / sum(volume) as mkt_share" +
                " from (" +
                " select" +
                " extract(year from o_orderdate) as o_year," +
                " l_extendedprice * (1-l_discount) as volume," +
                " n2.n_name as nation" +
                " from" +
                " part," +
                " supplier," +
                " lineitem," +
                " orders," +
                " customer," +
                " nation n1," +
                " nation n2," +
                " region" +
                " where" +
                " p_partkey = l_partkey" +
                " and s_suppkey = l_suppkey" +
                " and l_orderkey = o_orderkey" +
                " and o_custkey = c_custkey" +
                " and c_nationkey = n1.n_nationkey" +
                " and n1.n_regionkey = r_regionkey" +
                " and r_name = '"+arg2+"'" +
                " and s_nationkey = n2.n_nationkey" +
                " and o_orderdate between date '1995-01-01' and date '1996-12-31'" +
                " and p_type = '"+arg3+"'" +
                " ) as all_nations" +
                " group by" +
                " o_year" +
                " order by" +
                " o_year;";

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query9(){

        /*  1. COLOR (arg1)is randomly selected within the list of values defined for the generation of P_NAME  */

        String arg1 = getRndColor() ;


        String query = "select" +
                " nation," +
                " o_year," +
                " sum(amount) as sum_profit" +
                " from (" +
                " select" +
                " n_name as nation," +
                " extract(year from o_orderdate) as o_year," +
                " l_extendedprice * (1 - l_discount) - ps_supplycost * l_quantity as amount" +
                " from" +
                " part," +
                " supplier," +
                " lineitem," +
                " partsupp," +
                " orders," +
                " nation" +
                " where" +
                " s_suppkey = l_suppkey" +
                " and ps_suppkey = l_suppkey" +
                " and ps_partkey = l_partkey" +
                " and p_partkey = l_partkey" +
                " and o_orderkey = l_orderkey" +
                " and s_nationkey = n_nationkey" +
                " and p_name like '%"+arg1+"%'" +
                " ) as profit" +
                " group by" +
                " nation," +
                " o_year" +
                " order by" +
                " nation," +
                " o_year desc;";
        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query10(){

        /*  1.  DATE (arg1) is the first day of a randomly selected month from the
                second month of 1993 to the first month of 1995.*/
        int year = (rnd.nextInt(2)+3);
        int month;


        if(year == 3){
            month = (rnd.nextInt(11)+2);
        }else if(year == 5){
            month = 1;
        }else{
            month = (rnd.nextInt(12)+1);
        }

        String arg1;

        //TODO: Repeat this for every date arguments.
        if(month<10){
            arg1 = "199"+year+"-0"+month+"-01" ;
        }else{
            arg1 = "199"+year+"-0"+month+"-01" ;
        }

        String query = "select" +
                " c_custkey," +
                " c_name," +
                " sum(l_extendedprice * (1 - l_discount)) as revenue," +
                " c_acctbal," +
                " n_name," +
                " c_address," +
                " c_phone," +
                " c_comment" +
                " from" +
                " customer," +
                " orders," +
                " lineitem," +
                " nation" +
                " where" +
                " c_custkey = o_custkey" +
                " and l_orderkey = o_orderkey" +
                " and o_orderdate >= date '"+arg1+"'" +
                " and o_orderdate < date '"+arg1+"' + interval '3' month" +
                " and l_returnflag = 'R'" +
                " and c_nationkey = n_nationkey" +
                " group by" +
                " c_custkey," +
                " c_name," +
                " c_acctbal," +
                " c_phone," +
                " n_name," +
                " c_address," +
                " c_comment" +
                " order by" +
                " revenue desc;";
        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }


    public long query11(){

        /*  1. NATION is randomly selected within the list of values defined for N_NAME;
            2. FRACTION is chosen as 0.0001 / SF.*/

        String arg1 = getRndNation();
        double arg2= 0.0001/SCALE_FACTOR;


        String query = "select" +
                " ps_partkey," +
                " sum(ps_supplycost * ps_availqty) as value" +
                " from" +
                " partsupp," +
                " supplier," +
                " nation" +
                " where" +
                " ps_suppkey = s_suppkey" +
                " and s_nationkey = n_nationkey" +
                " and n_name = '"+arg1+"'" +
                " group by" +
                " ps_partkey having" +
                " sum(ps_supplycost * ps_availqty) > (" +
                " select" +
                " sum(ps_supplycost * ps_availqty) * " + arg2 +
                " from" +
                " partsupp," +
                " supplier," +
                " nation" +
                " where" +
                " ps_suppkey = s_suppkey" +
                " and s_nationkey = n_nationkey" +
                " and n_name = '"+arg1+"'" +
                " )" +
                " order by" +
                " value desc;";

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query12(){

        /*  1.  SHIPMODE1 (arg1) is randomly selected within the list of values defined for Modes.;
            2.  SHIPMODE2 (arg2) is randomly selected within the list of values defined for Modes and must be
                different from the value selected for SHIPMODE1 in item 1;
            3.  DATE (arg3) is the first of January of a randomly selected year within [1993 .. 1997].*/

        String arg1 = getRndShipMode();
        String arg2= getRndShipMode();
        while(arg1.equals(arg2)){
            arg2= getRndShipMode();
        }
        String arg3 = "199"+(rnd.nextInt(5)+3)+"-01-01" ;


        String query = "select" +
                " l_shipmode," +
                " sum(case" +
                " when o_orderpriority ='1-URGENT'" +
                " or o_orderpriority ='2-HIGH'" +
                " then 1" +
                " else 0" +
                " end) as high_line_count," +
                " sum(case" +
                " when o_orderpriority <> '1-URGENT'" +
                " and o_orderpriority <> '2-HIGH'" +
                " then 1" +
                " else 0" +
                " end) as low_line_count" +
                " from" +
                " orders," +
                " lineitem" +
                " where" +
                " o_orderkey = l_orderkey" +
                " and l_shipmode in ('"+arg1+"', '"+arg2+"')" +
                " and l_commitdate < l_receiptdate" +
                " and l_shipdate < l_commitdate" +
                " and l_receiptdate >= date '"+arg3+"'" +
                " and l_receiptdate < date '"+arg3+"' + interval '1' year" +
                " group by" +
                " l_shipmode" +
                " order by" +
                " l_shipmode;";

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query13(){

        /*  1. WORD1 is randomly selected from 4 possible values: special, pending, unusual, express.
            2. WORD2 is randomly selected from 4 possible values: packages, requests, accounts, deposits.*/

        String [] words1 = {"special", "pending", "unusual", "express"};
        String [] words2 = {"packages", "requests", "accounts", "deposits"};


        String arg1 = words1[rnd.nextInt(words1.length)];
        String arg2= words2[rnd.nextInt(words2.length)];


        String query = "select" +
                " c_count," +
                " count(*) as custdist" +
                " from" +
                " (" +
                " select" +
                " c_custkey," +
                " count(o_orderkey)" +
                " from" +
                " customer left outer join orders on" +
                " c_custkey = o_custkey" +
                " and o_comment not like '%"+arg1+"%"+arg2+"%'" +
                " group by" +
                " c_custkey" +
                " ) as c_orders (c_custkey, c_count)" +
                " group by" +
                " c_count" +
                " order by" +
                " custdist desc," +
                " c_count desc;";


        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query14(){

        /*  1.  DATE (arg1) is the first day of a month randomly selected from a random year within [1993 .. 1997].*/

        int m = rnd.nextInt(12)+1;
        String month;

        if(m<10){
            month = "0"+m;
        }else{
            month =""+m;
        }

        String arg1 = "199"+(rnd.nextInt(5)+3)+"-"+month+"-01" ;


        String query = "select" +
                " 100.00 * sum(case" +
                " when p_type like 'PROMO%'" +
                " then l_extendedprice*(1-l_discount)" +
                " else 0" +
                " end) / sum(l_extendedprice * (1 - l_discount)) as promo_revenue" +
                " from" +
                " lineitem," +
                " part" +
                " where" +
                " l_partkey = p_partkey" +
                " and l_shipdate >= date '"+arg1+"'" +
                " and l_shipdate < date '"+arg1+"' + interval '1' month;";

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query15(){

        long time = 0;

        /*  1.  DATE (arg1) is the first day of a randomly selected month between
                the first month of 1993 and the 10th month of 1997.*/

        int year = (rnd.nextInt(5)+3);
        int m;

        if(year == 7){
            m = (rnd.nextInt(10)+1);
        }else{
            m = (rnd.nextInt(12)+1);
        }

        String month;

        if(m<10){
            month = "0"+m;
        }else{
            month =""+m;
        }

        String arg1 = "199"+year+"-"+month+"-01" ;

        int streamId = rnd.nextInt(200);

        long start_time = System.currentTimeMillis();

        executeQuery(" create view revenue"+streamId+" (supplier_no, total_revenue) as" +
                " select" +
                " l_suppkey," +
                " sum(l_extendedprice * (1 - l_discount))" +
                " from" +
                " lineitem" +
                " where" +
                " l_shipdate >= date '"+arg1+"'" +
                " and l_shipdate < date '"+arg1+"' + interval '3' month" +
                " group by" +
                " l_suppkey;", QueryExecutionMode.UPDATE);

        time += System.currentTimeMillis() - start_time;

        start_time = System.currentTimeMillis();

        executeQuery(" select" +
                " s_suppkey," +
                " s_name," +
                " s_address," +
                " s_phone," +
                " total_revenue" +
                " from" +
                " supplier," +
                " revenue"+streamId+
                " where" +
                " s_suppkey = supplier_no" +
                " and total_revenue = (" +
                " select" +
                " max(total_revenue)" +
                " from" +
                " revenue"+streamId+"" +
                " )" +
                " order by" +
                " s_suppkey;", QueryExecutionMode.SELECT);

        time += System.currentTimeMillis() - start_time;

        start_time = System.currentTimeMillis();

        executeQuery( "drop view revenue"+streamId+";",QueryExecutionMode.UPDATE);

        time += System.currentTimeMillis() - start_time;

        return time;

    }

    public long query16(){

        /*  1.  BRAND (arg1) = Brand#MN where M and N are two single character strings representing two numbers randomly and
                independently selected within [1 .. 5];
            2.  TYPE (arg2) is made of the first 2 syllables of a string randomly selected within the list of 3-syllable strings defined
                for Types;
            3.  SIZE1 (arg3) is randomly selected as a set of eight different values within [1 .. 50];
            4.  SIZE2 (arg4) is randomly selected as a set of eight different values within [1 .. 50];
            5.  SIZE3 (arg5) is randomly selected as a set of eight different values within [1 .. 50];
            6.  SIZE4 (arg6) is randomly selected as a set of eight different values within [1 .. 50];
            7.  SIZE5 is randomly selected as a set of eight different values within [1 .. 50];
            8.  SIZE6 is randomly selected as a set of eight different values within [1 .. 50];
            9.  SIZE7 is randomly selected as a set of eight different values within [1 .. 50];
            10. SIZE8 is randomly selected as a set of eight different values within [1 .. 50].*/

        String arg1 = "Brand#"+(rnd.nextInt(5)+1)+(rnd.nextInt(5)+1);
        String arg2 = getRndParttype() + " " + getRndParttype();
        int arg3 = (rnd.nextInt(50)+1);
        int arg4 = (rnd.nextInt(50)+1);
        int arg5 = (rnd.nextInt(50)+1);
        int arg6 = (rnd.nextInt(50)+1);
        int arg7 = (rnd.nextInt(50)+1);
        int arg8 = (rnd.nextInt(50)+1);
        int arg9 = (rnd.nextInt(50)+1);
        int arg10 = (rnd.nextInt(50)+1);

        String query = "select" +
                " p_brand," +
                " p_type," +
                " p_size," +
                " count(distinct ps_suppkey) as supplier_cnt" +
                " from" +
                " partsupp," +
                " part" +
                " where" +
                " p_partkey = ps_partkey" +
                " and p_brand <> '"+arg1+"'" +
                " and p_type not like '"+arg2+"%'" +
                " and p_size in ("+arg3+", "+arg4+", "+arg5+", "+arg6+", "+arg7+", "+arg8+", "+arg9+", "+arg10+")" +
                " and ps_suppkey not in (" +
                " select" +
                " s_suppkey" +
                " from" +
                " supplier" +
                " where" +
                " s_comment like '%Customer%Complaints%'" +
                " )" +
                " group by" +
                " p_brand," +
                " p_type," +
                " p_size" +
                " order by" +
                " supplier_cnt desc," +
                " p_brand," +
                " p_type," +
                " p_size;";

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query17(){

        /*  1.  BRAND = 'Brand#MN' where MN is a two character string representing two
                numbers randomly and independently selected within [1 .. 5];
            2.  CONTAINER is randomly selected within the list of 2-syllable strings defined
                for Containers.*/

        String arg1 = "Brand#"+(rnd.nextInt(5)+1)+(rnd.nextInt(5)+1);
        String arg2 = getRndContainer();

        String query = "select" +
                " sum(l_extendedprice) / 7.0 as avg_yearly" +
                " from" +
                " lineitem," +
                " part" +
                " where" +
                " p_partkey = l_partkey" +
                " and p_brand = '"+arg1+"'" +
                " and p_container = '"+arg2+"'" +
                " and l_quantity < (" +
                " select" +
                " 0.2 * avg(l_quantity)" +
                " from" +
                " lineitem" +
                " where" +
                " l_partkey = p_partkey);";

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query18(){

        /*  1.  QUANTITY (arg1) is randomly selected within [312..315].*/

        int arg1 = rnd.nextInt(4)+312;


        String query = "select count(" +
                " c_name," +
                " c_custkey," +
                " o_orderkey," +
                " o_orderdate," +
                " o_totalprice," +
                " sum(l_quantity))" +
                " from" +
                " customer," +
                " orders," +
                " lineitem" +
                " where" +
                " o_orderkey in (" +
                " select" +
                " l_orderkey" +
                " from" +
                " lineitem" +
                " group by" +
                " l_orderkey having" +
                " sum(l_quantity) > " + arg1 +
                " )" +
                " and c_custkey = o_custkey" +
                " and o_orderkey = l_orderkey" +
                " group by" +
                " c_name," +
                " c_custkey," +
                " o_orderkey," +
                " o_orderdate," +
                " o_totalprice" +
                " order by" +
                " o_totalprice desc," +
                " o_orderdate;";

        System.out.println(query);

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query19(){

        /*  1.  QUANTITY1 (arg1) is randomly selected within [1..10].
            2.  QUANTITY2 (arg2) is randomly selected within [10..20].
            3.  QUANTITY3 (arg3) is randomly selected within [20..30].
            4.  BRAND1 (arg4), BRAND2 (arg5), BRAND3 (arg6) = 'Brand#MN' where each MN is a two character
                string representing two numbers randomly and independently selected
                within [1 .. 5].*/

        int arg1 = rnd.nextInt(10)+1;
        int arg2 = rnd.nextInt(11)+10;
        int arg3 = rnd.nextInt(21)+10;
        String arg4 = "Brand#"+(rnd.nextInt(5)+1)+""+(rnd.nextInt(5)+1);
        String arg5 = "Brand#"+(rnd.nextInt(5)+1)+""+(rnd.nextInt(5)+1);
        String arg6 = "Brand#"+(rnd.nextInt(5)+1)+""+(rnd.nextInt(5)+1);

        String query = "select" +
                " sum(l_extendedprice * (1 - l_discount) ) as revenue" +
                " from" +
                " lineitem," +
                " part" +
                " where" +
                " (" +
                " p_partkey = l_partkey" +
                " and p_brand = '"+arg4+"'" +
                " and p_container in ( 'SM CASE', 'SM BOX', 'SM PACK', 'SM PKG')" +
                " and l_quantity >= "+arg1+" and l_quantity <= "+arg1+" + 10" +
                " and p_size between 1 and 5" +
                " and l_shipmode in ('AIR', 'AIR REG')" +
                " and l_shipinstruct = 'DELIVER IN PERSON'" +
                " )" +
                " or" +
                " (" +
                " p_partkey = l_partkey" +
                " and p_brand = '"+arg5+"'" +
                " and p_container in ('MED BAG', 'MED BOX', 'MED PKG', 'MED PACK')" +
                " and l_quantity >= "+arg2+" and l_quantity <= "+arg2+" + 10" +
                " and p_size between 1 and 10" +
                " and l_shipmode in ('AIR', 'AIR REG')" +
                " and l_shipinstruct = 'DELIVER IN PERSON'" +
                " )" +
                " or" +
                " (" +
                " p_partkey = l_partkey" +
                " and p_brand = '"+arg6+"'" +
                " and p_container in ( 'LG CASE', 'LG BOX', 'LG PACK', 'LG PKG')" +
                " and l_quantity >= "+arg3+" and l_quantity <= "+arg3+" + 10" +
                " and p_size between 1 and 15" +
                " and l_shipmode in ('AIR', 'AIR REG')" +
                " and l_shipinstruct = 'DELIVER IN PERSON'" +
                " );";

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query20(){

        /*  1.  COLOR (arg1) is randomly selected within the list of values defined for the generation of P_NAME.
            2.  DATE (arg2) is the first of January of a randomly selected year within 1993..1997.
            3.  NATION (arg3) is randomly selected within the list of values defined for N_NAME.*/

        String arg1 = getRndColor();
        String arg2 = "199"+(rnd.nextInt(5)+3)+"-01-01" ;
        String arg3 = getRndNation();

        String query = "select" +
                " s_name," +
                " s_address" +
                " from" +
                " supplier, nation" +
                " where" +
                " s_suppkey in (" +
                " select" +
                " ps_suppkey" +
                " from" +
                " partsupp" +
                " where" +
                " ps_partkey in (" +
                " select" +
                " p_partkey" +
                " from" +
                " part" +
                " where" +
                " p_name like '"+arg1+"%'" +
                " )" +
                " and ps_availqty > (" +
                " select" +
                " 0.5 * sum(l_quantity)" +
                " from" +
                " lineitem" +
                " where" +
                " l_partkey = ps_partkey" +
                " and l_suppkey = ps_suppkey" +
                " and l_shipdate >= date('"+arg2+"')" +
                " and l_shipdate < date('"+arg2+"') + interval '1' year" +
                " )" +
                " )" +
                " and s_nationkey = n_nationkey" +
                " and n_name = '"+arg3+"'" +
                " order by" +
                " s_name;";

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query21(){

        /*  1.  NATION (arg1) is randomly selected within the list of values defined for N_NAME.*/

        String arg1 = getRndNation();

        String query = "select" +
                " s_name," +
                " count(*) as numwait" +
                " from" +
                " supplier," +
                " lineitem l1," +
                " orders," +
                " nation" +
                " where" +
                " s_suppkey = l1.l_suppkey" +
                " and o_orderkey = l1.l_orderkey" +
                " and o_orderstatus = 'F'" +
                " and l1.l_receiptdate > l1.l_commitdate" +
                " and exists (" +
                " select" +
                " *" +
                " from" +
                " lineitem l2" +
                " where" +
                " l2.l_orderkey = l1.l_orderkey" +
                " and l2.l_suppkey <> l1.l_suppkey" +
                " )" +
                " and not exists (" +
                " select" +
                " *" +
                " from" +
                " lineitem l3" +
                " where" +
                " l3.l_orderkey = l1.l_orderkey" +
                " and l3.l_suppkey <> l1.l_suppkey" +
                " and l3.l_receiptdate > l3.l_commitdate" +
                " )" +
                " and s_nationkey = n_nationkey" +
                " and n_name = '"+arg1+"'" +
                " group by" +
                " s_name" +
                " order by" +
                " numwait desc," +
                " s_name;";

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    public long query22(){

        /*  1.  I1 ... I7 are randomly selected without repetition from the
                possible values for Country code.*/

        ArrayList<Integer> list = new ArrayList<>();
        int cCod;

        for (int i=0;i<7;i++){
            do {
                cCod = rnd.nextInt(25)+10;
            }while(list.contains(cCod));

            list.add(cCod);
        }


        String query = "select" +
                " cntrycode," +
                " count(*) as numcust," +
                " sum(c_acctbal) as totacctbal" +
                " from (" +
                " select" +
                " substring(c_phone from 1 for 2) as cntrycode," +
                " c_acctbal" +
                " from" +
                " customer" +
                " where" +
                " substring(c_phone from 1 for 2) in" +
                " ('"+list.get(0)+"','"+list.get(1)+"','"+list.get(2)+"','"+list.get(3)+"','"+list.get(4)+"','"+list.get(5)+"','"+list.get(6)+"')" +
                " and c_acctbal > (" +
                " select" +
                " avg(c_acctbal)" +
                " from" +
                " customer" +
                " where" +
                " c_acctbal > 0.00" +
                " and substring(c_phone from 1 for 2) in" +
                " ('"+list.get(0)+"','"+list.get(1)+"','"+list.get(2)+"','"+list.get(3)+"','"+list.get(4)+"','"+list.get(5)+"','"+list.get(6)+"')" +
                " )" +
                " and not exists (" +
                " select" +
                " *" +
                " from" +
                " orders" +
                " where" +
                " o_custkey = c_custkey" +
                " )" +
                " ) as custsale" +
                " group by" +
                " cntrycode" +
                " order by" +
                " cntrycode;";

        long start_time = System.currentTimeMillis();
        executeQuery(query,QueryExecutionMode.SELECT);
        return System.currentTimeMillis()-start_time;

    }

    private String getRndContainer() {

        String result;

        String [] syllabe1 = {"SM", "LG", "MED", "JUMBO", "WRAP"};
        String [] syllabe2 = {"CASE", "BOX", "BAG", "JAR", "PKG", "PACK", "CAN", "DRUM"};

        result = syllabe1[rnd.nextInt(syllabe1.length)] + " " + syllabe2[rnd.nextInt(syllabe2.length)];

        return result;
    }

    public String getRndShipMode(){

        String [] shipModes = {"REG AIR", "AIR", "RAIL", "SHIP", "TRUCK", "MAIL", "FOB"};

        int val = rnd.nextInt(shipModes.length);

        return shipModes[val];
    }


    public String getRndColor(){

        String [] colors = {"almond", "antique", "aquamarine", "azure", "beige",
                "bisque", "black", "blanched", "blue","blush", "brown", "burlywood",
                "burnished", "chartreuse", "chiffon", "chocolate", "coral",
                "cornflower", "cornsilk", "cream", "cyan", "dark", "deep", "dim",
                "dodger", "drab", "firebrick", "floral", "forest", "frosted",
                "gainsboro", "ghost", "goldenrod", "green", "grey", "honeydew",
                "hot", "indian", "ivory", "khaki", "lace", "lavender", "lawn",
                "lemon", "light", "lime", "linen", "magenta", "maroon", "medium",
                "metallic", "midnight", "mint", "misty", "moccasin", "navajo",
                "navy", "olive", "orange", "orchid", "pale", "papaya", "peach",
                "peru", "pink", "plum", "powder", "puff", "purple", "red", "rose",
                "rosy", "royal", "saddle", "salmon", "sandy", "seashell", "sienna",
                "sky", "slate", "smoke", "snow", "spring", "steel", "tan", "thistle",
                "tomato", "turquoise", "violet", "wheat", "white", "yellow"};

        int val = rnd.nextInt(colors.length);

        return colors[val];
    }


    public String getRegionByNation(String arg1) {

        String result;

        switch (arg1) {
            case "ALGERIA":
            case "ETHIOPIA":
            case "KENYA":
            case "MOROCCO":
            case "MOZAMBIQUE":
                result = "AFRICA";
                break;
            case "ARGENTINA":
            case "BRAZIL":
            case "CANADA":
            case "PERU":
            case "UNITED STATES":
                result = "AMERICA";
                break;
            case "INDIA":
            case "INDONESIA":
            case "JAPAN":
            case "CHINA":
            case "VIETNAM":
                result = "ASIA";
                break;
            case "FRANCE":
            case "GERMANY":
            case "ROMANIA":
            case "RUSSIA":
            case "UNITED KINGDOM":
                result = "EUROPE";
                break;
            default:
                result = "MIDDLE EAST";
                break;
        }

        return result;
    }

    public String getRndNation() {

        String [] nations = {"ALGERIA", "ARGENTINA", "BRAZIL", "CANADA", "EGYPT",
                "FRANCE", "GERMANY", "INDIA", "INDONESIA", "IRAN", "IRAQ", "JAPAN",
                "JORDAN", "KENYA", "MOROCCO", "MOZAMBIQUE", "PERU", "CHINA", "ROMANIA",
                "SAUDI ARABIA", "VIETNAM", "RUSSIA", "UNITED KINGDOM", "UNITED STATES"};

        int val = rnd.nextInt(nations.length);

        return nations[val];
    }

    public String getRndMtksegment() {

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
