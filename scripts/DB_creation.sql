CREATE DATABASE  IF NOT EXISTS `tpch` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `tpch`;

CREATE USER 'tpch'@'%' IDENTIFIED BY 'tpch';
GRANT ALL PRIVILEGES ON tpch.* TO 'tpch'@'%'; # La idea es darle solo permisos para la bd tpch
FLUSH PRIVILEGES;


##############################################################################
##Creamos las tablas que conforman la BD completa con sus restricciones.


#----------------------------------------------------------------
# CREATE region
create table region (r_regionkey integer not null,
                     r_comment varchar(152) not null,
                     r_name char(25) not null,
                     primary key (r_regionkey)) engine=myisam;
#----------------------------------------------------------------
# CREATE nation
create table nation (n_nationkey integer not null, 
                     n_name char(25) not null,
                     n_comment varchar(152) not null, 
                     n_regionkey integer not null,
                     primary key (n_nationkey),
                     foreign key nation_fk1(n_regionkey) 
                     references region(r_regionkey)) engine=myisam;
#----------------------------------------------------------------
# CREATE customer 
create table customer (c_custkey integer not null,
                       c_name varchar(25) not null,
                       c_address varchar(40) not null,
                       c_nationkey integer not null, 
                       c_phone char(15) not null,
                       c_comment varchar(117) not null,
                       c_acctbal decimal(15,2) not null, 
                       c_mktsegment char(10) not null,
                       primary key (c_custkey), 
                       foreign key customer_fk1(c_nationkey) 
                       references nation(n_nationkey)) engine=myisam;
#----------------------------------------------------------------
# CREATE orders
create table orders (o_orderdate date not null, 
                     o_orderkey integer not null, 
                     o_custkey integer not null,
                     o_orderstatus char(1) not null,
                     o_totalprice decimal(15,2) not null, 
                     o_orderpriority char(15) not null,
                     o_clerk char(15) not null, 
                     o_shippriority integer not null,
                     o_comment varchar(79) not null,
                     primary key (o_orderkey), 
                     foreign key orders_fk1(o_custkey) 
                     references customer(c_custkey)
                     ,index orders_dt_idx (o_orderdate)
                     ) engine=myisam;
#----------------------------------------------------------------
# CREATE part
create table part (p_partkey integer not null, 
                   p_mfgr char(25) not null, 
                   p_brand char(10) not null,
                   p_type varchar(25) not null, 
                   p_size integer not null, 
                   p_container char(10) not null,
                   p_comment varchar(23) not null,
                   p_retailprice decimal(15,2) not null,
                   p_name varchar(55) not null, 
                   primary key (p_partkey)
) engine=myisam;
#----------------------------------------------------------------
# CREATE supplier 
create table supplier (s_suppkey integer not null, 
                       s_name char(25) not null,
                       s_address varchar(40) not null,
                       s_nationkey integer not null, 
                       s_phone char(15) not null,
                       s_acctbal decimal(15,2) not null, 
                       s_comment varchar(101) not null,
                       primary key (s_suppkey), 
                       foreign key supplier_fk1(s_nationkey) 
                       references nation(n_nationkey)
) engine=myisam;
#----------------------------------------------------------------
# CREATE partsupp
create table partsupp (ps_partkey integer not null,
                       ps_suppkey integer not null,
                       ps_supplycost decimal(15,2) not null, 
                       ps_comment varchar(199) not null,
                       ps_availqty integer not null,
                       primary key (ps_partkey, ps_suppkey),
                       foreign key partsupp_fk1(ps_partkey) 
                       references part(p_partkey),
                       foreign key partsupp_fk2(ps_suppkey) 
                       references supplier(s_suppkey)) engine=myisam; 
#----------------------------------------------------------------
# CREATE lineitem 
create table lineitem (l_shipdate date not null,
                       l_orderkey integer not null,
                       l_partkey  integer not null,
                       l_suppkey integer not null,
                       l_linenumber integer not null,
                       l_quantity decimal(15,2) not null,
                       l_extendedprice decimal(15,2) not null,
                       l_discount decimal(15,2) not null,
                       l_tax decimal(15,2) not null, 
                       l_returnflag char(1) not null,
                       l_linestatus char(1) not null,
                       l_commitdate date not null,
                       l_receiptdate date not null,
                       l_shipinstruct   char(25) not null,
                       l_comment varchar(44) not null, 
                       l_shipmode char(10) not null,
                       primary key (l_orderkey, l_linenumber),
                       foreign key lineitem_fk1(l_orderkey)
                       references orders(o_orderkey),
                       foreign key lineitem_fk2(l_suppkey)
                       references supplier(s_suppkey),
                       foreign key lineitem_fk3(l_partkey, l_suppkey)
                       references partsupp(ps_partkey, ps_suppkey),
                       foreign key lineitem_fk4(l_partkey)
                       references part(p_partkey)
                       ,index li_shp_dt_idx (l_shipdate)
                       ,index li_com_dt_idx (l_commitdate) 
                       ,index li_rcpt_dt_idx (l_receiptdate)
) engine=myisam;

#############################################
## Hay que añadir que se eliminen las entradas con PK duplicadas, porque
## a veces dbgen genera algunas.
#############################################





##Una vez creadas todas las tablas, se importan los datos desde los ficheros generados
#por la herramienta dgben que se encuentran en ficheros con extensión ".tbl".

##Previamente aseguramos que mysql pueda acceder a los ficheros.

	# sudo gedit /etc/apparmor.d/usr.sbin.mysqld
	
    #/usr/sbin/mysqld {
	#     ...
	#	/data/ r,
	#	/data/ * rw,
	#}
    
    # sudo /etc/init.d/apparmor reload

##Tabla REGION.
TRUNCATE TABLE `tpch`.`region`;
#ALTER TABLE `tpch`.`region` DISABLE KEYS;
LOAD DATA INFILE '/var/lib/mysql/DATA/region.tbl' IGNORE
INTO TABLE `tpch`.`region`
FIELDS TERMINATED BY '|' ;
#ALTER TABLE `tpch`.`region` ENABLE KEYS;
  
##Tabla NATION.
TRUNCATE TABLE `tpch`.`nation`;
#ALTER TABLE `tpch`.`region` DISABLE KEYS;
LOAD DATA INFILE '/var/lib/mysql/DATA/nation.tbl' IGNORE
INTO TABLE `tpch`.`nation`
FIELDS TERMINATED BY '|' ;
#ALTER TABLE `tpch`.`nation` ENABLE KEYS;

##Tabla SUPPLIER.
TRUNCATE TABLE `tpch`.`supplier`;
#ALTER TABLE `tpch`.`supplier` DISABLE KEYS;
LOAD DATA INFILE '/var/lib/mysql/DATA/supplier.tbl' IGNORE
INTO TABLE `tpch`.`supplier`
FIELDS TERMINATED BY '|' ;
#ALTER TABLE `tpch`.`supplier` ENABLE KEYS;
  
##Tabla CUSTOMER.
TRUNCATE TABLE `tpch`.`customer`;
#ALTER TABLE `tpch`.`customer` DISABLE KEYS;
LOAD DATA INFILE '/var/lib/mysql/DATA/customer.tbl' IGNORE
INTO TABLE `tpch`.`customer`
FIELDS TERMINATED BY '|' ;
#ALTER TABLE `tpch`.`customer` ENABLE KEYS;

##Tabla ORDERS.
TRUNCATE TABLE `tpch`.`orders`;
#ALTER TABLE `tpch`.`orders` DISABLE KEYS;
LOAD DATA INFILE '/var/lib/mysql/DATA/orders.tbl' IGNORE
INTO TABLE `tpch`.`orders`
FIELDS TERMINATED BY '|' ;
#ALTER TABLE `tpch`.`orders` ENABLE KEYS;

##Tabla PART.
TRUNCATE TABLE `tpch`.`part`;
#ALTER TABLE `tpch`.`part` DISABLE KEYS;
LOAD DATA INFILE '/var/lib/mysql/DATA/part.tbl' IGNORE
INTO TABLE `tpch`.`part`
FIELDS TERMINATED BY '|' ;
#ALTER TABLE `tpch`.`part` ENABLE KEYS;

##Tabla PARTSUPP.
TRUNCATE TABLE `tpch`.`partsupp`;
#ALTER TABLE `tpch`.`partsupp` DISABLE KEYS;
LOAD DATA INFILE '/var/lib/mysql/DATA/partsupp.tbl' IGNORE
INTO TABLE `tpch`.`partsupp`
FIELDS TERMINATED BY '|' ;
#ALTER TABLE `tpch`.`partsupp` ENABLE KEYS;

##Tabla LINEITEM.
TRUNCATE TABLE `tpch`.`lineitem`;
#ALTER TABLE `tpch`.`lineitem` DISABLE KEYS;
LOAD DATA INFILE '/var/lib/mysql/DATA/lineitem.tbl' IGNORE
INTO TABLE `tpch`.`lineitem`
FIELDS TERMINATED BY '|' ;
#ALTER TABLE `tpch`.`lineitem` ENABLE KEYS;


