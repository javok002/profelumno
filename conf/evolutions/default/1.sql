# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table lesson (
  id                        bigint not null,
  date_time                 timestamp,
  duration                  time,
  address                   varchar(255),
  comment                   varchar(255),
  price                     float,
  teacher_id                bigint,
  student_id                bigint,
  constraint pk_lesson primary key (id))
;

create table student (
  id                        bigint not null,
  user_id                   bigint,
  profile_picture           blob,
  constraint uq_student_user_id unique (user_id),
  constraint pk_student primary key (id))
;

create table subject (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_subject primary key (id))
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
  home_classes              boolean,
  ranking                   integer,
  lessons_dictated          integer,
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


create table subject_user (
  subject_id                     bigint not null,
  user_id                        bigint not null,
  constraint pk_subject_user primary key (subject_id, user_id))
;

create table user_subject (
  user_id                        bigint not null,
  subject_id                     bigint not null,
  constraint pk_user_subject primary key (user_id, subject_id))
;
create sequence lesson_seq;

create sequence student_seq;

create sequence subject_seq;

create sequence teacher_seq;

create sequence user_seq;

alter table lesson add constraint fk_lesson_teacher_1 foreign key (teacher_id) references teacher (id) on delete restrict on update restrict;
create index ix_lesson_teacher_1 on lesson (teacher_id);
alter table lesson add constraint fk_lesson_student_2 foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_lesson_student_2 on lesson (student_id);
alter table student add constraint fk_student_user_3 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_student_user_3 on student (user_id);
alter table teacher add constraint fk_teacher_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_teacher_user_4 on teacher (user_id);



alter table subject_user add constraint fk_subject_user_subject_01 foreign key (subject_id) references subject (id) on delete restrict on update restrict;

alter table subject_user add constraint fk_subject_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_subject add constraint fk_user_subject_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_subject add constraint fk_user_subject_subject_02 foreign key (subject_id) references subject (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists lesson;

drop table if exists student;

drop table if exists subject;

drop table if exists subject_user;

drop table if exists teacher;

drop table if exists user;

drop table if exists user_subject;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists lesson_seq;

drop sequence if exists student_seq;

drop sequence if exists subject_seq;

drop sequence if exists teacher_seq;

drop sequence if exists user_seq;

