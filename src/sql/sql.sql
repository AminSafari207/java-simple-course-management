create database student_course_management;

create table student (
    id serial primary key,
    name varchar,
    major varchar,
    year int,
    gpa decimal
);

create table course (
    id serial primary key,
    title varchar,
    department varchar,
    credits int
);

create table enrollment (
    id serial primary key,
    student_id int references student(id),
    course_id int references course(id),
    enrollment_date date,
    grade int,

    unique (student_id, course_id)
);

