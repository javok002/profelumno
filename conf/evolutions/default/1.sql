# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table student (
  id                        bigint not null,
  constraint pk_student primary key (id))
;

create table teacher (
  id                        bigint not null,
  subscription              varchar(255),
  renewal_date              timestamp,
  constraint pk_teacher primary key (id))
;

create table user (
  id                        bigint not null,
  name                      varchar(255),
  surname                   varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  gender                    varchar(255),
  secure_question           varchar(255),
  secure_answer             varchar(255),
  constraint pk_user primary key (id))
;

create sequence student_seq;

create sequence teacher_seq;

create sequence user_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists student;

drop table if exists teacher;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists student_seq;

drop sequence if exists teacher_seq;

drop sequence if exists user_seq;

