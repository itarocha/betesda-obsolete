#!/bin/bash

clear

mysql -u itamar --password=itamar123456 betesda < betesda.sql

rm -R /var/www/html/betesda/

mkdir /var/www/html/betesda/

sudo tar xvf /home/temp/betesda/betesdaclient.tar.gz -C /var/www/html/betesda


echo "Parando Tomcat"
cd /opt/tomcat/bin
./catalina.sh stop

echo "Removendo arquivo /opt/tomcat/webapps/betesda.war ..."
rm /opt/tomcat/webapps/betesda.war

echo "Copiando betesda.war ..."
cp /home/temp/betesda/betesda.war /opt/tomcat/webapps/

echo "Startando Tomcat"
./catalina.sh start
