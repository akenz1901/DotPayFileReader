create database onePipeLogDb;

create user 'kelvin'@'localhost' identified by 'kelvin123';
grant all privileges on onePipeLogDb.* to 'kelvin'@'localhost';
flush privileges