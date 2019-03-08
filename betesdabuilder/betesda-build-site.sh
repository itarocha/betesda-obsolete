#!/bin/bash

clear

echo "Preparando-se para gerar front-end"
echo "Excluindo arquivos antigos"
rm /home/itamar/projetos_spring/betesda/betesdabuilder/output/betesdaclient.tar.gz
rm /home/itamar/projetos_spring/betesda/betesdabuilder/output/betesda.war
rm /home/itamar/projetos_spring/betesda/betesdabuilder/output/betesda.sql

echo "Realizando backup do banco de dados"
sudo mysqldump --add-drop-table --databases betesda -e -C > /home/itamar/projetos_spring/betesda/betesdabuilder/output/betesda.sql

echo "Construindo front-end"
cd /home/itamar/projetos_spring/betesda/betesda-app

npm run build

cd /home/itamar/projetos_spring/betesda/betesda-app/dist/

echo "Compactando arquivo..."
tar -zcvf /home/itamar/projetos_spring/betesda/betesdabuilder/output/betesdaclient.tar.gz *

echo "Arquivo  /home/itamar/betesda/bts.tar.gz criado com sucesso"



echo "Gerando betesda.war"

echo "Os comandos abaixo foram executados no spring sts"

echo "cd /home/itamar/projetos_spring/betesda"

echo "mvn dependency:tree"

echo "mvn clean package"

echo "Copiando betesda.war"

cp /home/itamar/projetos_spring/betesda/target/betesda.war /home/itamar/projetos_spring/betesda/betesdabuilder/output/betesda.war

