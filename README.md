# groovyfx-music-db
A JavaFX app displaying a list of data from a Music database.

### To use the DB: ###

Run the docker-compose-databases.yml file

Access the docker container: docker exec -it app_music-db_1 bash

Run the backup script: mysql -u root music_db < /etc/mysql/backup.sql

After backup, run the app.
