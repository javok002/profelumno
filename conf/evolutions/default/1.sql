# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table student (
  id                        bigint not null,
  user_id                   bigint,
  profile_picture           blob,
  constraint uq_student_user_id unique (user_id),
  constraint pk_student primary key (id))
;

create table teacher (
  id                        bigint not null,
  user_id                   bigint,
  subscription              varchar(255),
  renewal_date              timestamp,
  is_in_trial               boolean,
  has_card                  boolean,
  profile_picture           blob,
  description               varchar(255),
  home_classes              varchar(255),
  constraint uq_teacher_user_id unique (user_id),
  constraint pk_teacher primary key (id))
;

create table user (
  id                        bigint not null,
  name                      varchar(255),
  surname                   varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  birthday                  timestamp,
  gender                    varchar(255),
  address                   varchar(255),
  secure_question           varchar(255),
  secure_answer             varchar(255),
  constraint pk_user primary key (id))
;

create sequence student_seq;

create sequence teacher_seq;

create sequence user_seq;

alter table student add constraint fk_student_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_student_user_1 on student (user_id);
alter table teacher add constraint fk_teacher_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_teacher_user_2 on teacher (user_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists student;

drop table if exists teacher;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists student_seq;

drop sequence if exists teacher_seq;

drop sequence if exists user_seq;

