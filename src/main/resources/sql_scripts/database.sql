DROP DATABASE IF EXISTS bootcamp;
create database bootcamp;

use bootcamp;

create table users(
    id       int auto_increment primary key,
    username varchar(30) unique,
    password varchar(30) not null
);

create table students
(
    id         int auto_increment primary key,
    first_name varchar(30) not null,
    last_name  varchar(30) not null,
    dob        date        not null
);

insert into users(username, password) values('Samuel', 'password');

insert into students(first_name, last_name, dob) values('Samuel', 'Gunda', '2000-06-22');
insert into students(first_name, last_name, dob) values('Emily', 'Smith', '1999-11-15');
insert into students(first_name, last_name, dob) values('Jordan', 'Jones', '2001-03-10');
insert into students(first_name, last_name, dob) values('Olivia', 'Brown', '2002-08-18');
insert into students(first_name, last_name, dob) values('Liam', 'Miller', '1998-04-25');
insert into students(first_name, last_name, dob) values('Ava', 'Davis', '2003-09-30');
insert into students(first_name, last_name, dob) values('Mason', 'Taylor', '2004-01-12');
insert into students(first_name, last_name, dob) values('Isabella', 'Anderson', '1997-07-03');
insert into students(first_name, last_name, dob) values('Ethan', 'Moore', '2005-05-08');
insert into students(first_name, last_name, dob) values('Sophia', 'Wilson', '1996-12-14');




