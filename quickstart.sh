#!/bin/sh


echo ">> quick-start: building INDITEX-PVP-SERVLET project"
echo "..."
cd inditex-pvp-servlet
mvn clean install -Dmaven.test.skip=true -Dcheckstyle.skip -Dpmd.skip=true -Dspotbugs.skip=true > /dev/null

echo ">> quick-start: building INDITEX-PVP-SERVLET DOCKER image"
echo "..."
mvn -f launcher/pom.xml spring-boot:build-image > /dev/null
cd ..

cd inditex-pvp-reactive
echo ">> quick-start: building INDITEX-PVP-REACTIVE project"
echo "..."
mvn clean install -Dmaven.test.skip=true -Dcheckstyle.skip -Dpmd.skip=true -Dspotbugs.skip=true > /dev/null

echo ">> quick-start: building INDITEX-PVP-REACTIVE DOCKER image"
echo "..."
mvn -f launcher/pom.xml spring-boot:build-image  > /dev/null
cd ..


docker-compose up