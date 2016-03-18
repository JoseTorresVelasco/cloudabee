#!/bin/bash

##
##  Developed by Scenic
##

# variables

DIRECTORY=cloudabee
ZIP=tpch.zip
DIRECTORY_TPCH=tpch_2_17_0/dbgen/

# subprograms
function advertement {
  echo "CAUTION: TPC-H is licensed and we can NOT distribute it.";
  echo "   You should download it from http://www.tpc.org/tpc_documents_current_versions/current_specifications.asp";
  echo "   In the following, we assume there is a file \"$ZIP\" in the directory where deploy.sh is called.";
  echo "";
}

function checking_tool {
  if ! hash $1 2>/dev/null; then
    echo "Tool $1 needs to be installed";
    sudo apt-get install $1;
  fi
}

# script

advertement

echo "1. Checking if needed tools are available in the system.";
checking_tool zip
checking_tool gcc
checking_tool make
checking_tool mysql-server

if [ -d $DIRECTORY ]; then
  echo "ERROR: $DIRECTORY exists. You should delete it to continue.";
  exit;
fi
if [ ! -f $ZIP ]; then
  echo "ERROR: $ZIP does NOT exist. You should download it to continue.";
  exit;
fi

mkdir $DIRECTORY
cp $ZIP $DIRECTORY
cd $DIRECTORY

echo "2. Unziping $ZIP.";
unzip -qq $ZIP

if [ ! -d $DIRECTORY_TPCH ]; then
  echo "ERROR: $DIRECTORY_TPCH does not exist. You should download the correct version of TPC-H to continue.";
  exit;
fi

cd $DIRECTORY_TPCH

echo "3. Compiling.";
make

###Se crea una bd de un GB.
./dbgen -s 1
#
mkdir DATA
mv *.tbl DATA
sudo mv DATA /var/lib/mysql/

##Instalación de mysql.
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password password jose123'
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password jose123'

##Una vez instalado el servidor, se levanta y se crea un usuario para la BD y la propia BD
sudo service mysql start
#
#
###mysql -u root;
#
##Creación de la BD e importación de los datos generados por DBGEN.
#source ./../../../DB_creation.sql;########################################## --> no funciona
#
#
#
#
