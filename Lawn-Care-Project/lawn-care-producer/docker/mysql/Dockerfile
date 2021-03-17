#Using the mysql base image
FROM mysql

#Setting environment variables required for a mysql image
ENV MYSQL_ROOT_PASSWORD test
ENV MYSQL_DATABASE lawn-care
ENV MYSQL_USER jrosado
ENV MYSQL_PASSWORD test

#Copying sql file with database schema to put onto new mysql image
COPY ./sql-scripts/ /docker-entrypoint-initdb.d/