#!/bin/bash

#Creación de la carpeta tcph y dentro descompresión el benchmark
mkdir tpch

##Instalación de la herramienta zip si no está.
echo Instalando herramienta "zip"
sudo apt-get install zip

mv tpch.zip ./tpch
cd tpch/
unzip tpch.zip
cd tpch_2_17_0/dbgen/
#mv makefile.suite makefile

### cambiar las lineas del makefile para que se adapten a la máquina.
## En realidad todo esto podría sustituirse por copiar el makefile ya modificado.

echo Instalando herramienta "gcc"
sudo apt-get install gcc

echo Instalando herramienta "make"
sudo apt-get install make
make

##Se crea una bd de un GB.
./dbgen -s 1

mkdir DATA
mv *.tbl DATA
sudo mv DATA /var/lib/mysql/

##Instalación de mysql.
#sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password password jose123'
#sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password jose123'
sudo apt-get -y install mysql-server

##Una vez instalado el servidor, se levanta y se crea un usuario para la BD y la propia BD
sudo service mysql start


##mysql -u root;

#Creación de la BD e importación de los datos generados por DBGEN.
#source ./../../../DB_creation.sql;########################################## --> no funciona




