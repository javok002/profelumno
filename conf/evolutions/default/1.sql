# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table lesson (
  id                        bigint not null,
  date_string               varchar(255),
  date_time                 timestamp,
  duration                  bigint,
  address                   varchar(255),
  comment                   varchar(255),
  price                     float,
  subject_id                bigint,
  lesson_state              integer,
  teacher_id                bigint,
  teacher_review_id         bigint,
  student_id                bigint,
  student_review_id         bigint,
  constraint uq_lesson_teacher_review_id unique (teacher_review_id),
  constraint uq_lesson_student_review_id unique (student_review_id),
  constraint pk_lesson primary key (id))
;

create table review (
  id                        bigint not null,
  comment                   varchar(255),
  stars                     bigint,
  date                      timestamp,
  constraint pk_review primary key (id))
;

create table student (
  id                        bigint not null,
  user_id                   bigint,
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
  description               varchar(255),
  home_classes              boolean,
  ranking                   bigint,
  lessons_dictated          integer,
  price                     double,
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
  last_login                timestamp,
  gender                    varchar(255),
  address                   varchar(255),
  latitude                  varchar(255),
  longitude                 varchar(255),
  city                      varchar(255),
  neighbourhood             varchar(255),
  secure_question           varchar(255),
  secure_answer             varchar(255),
  reviews                   bigint,
  total_stars               bigint,
  profile_picture           blob,
  constraint pk_user primary key (id))
;


create table subject_user (
  subject_id                     bigint not null,
  user_id                        bigint not null,
  constraint pk_subject_user primary key (subject_id, user_id))
;
create sequence lesson_seq;

create sequence review_seq;

create sequence student_seq;

create sequence subject_seq;

create sequence teacher_seq;

create sequence user_seq;

alter table lesson add constraint fk_lesson_subject_1 foreign key (subject_id) references subject (id) on delete restrict on update restrict;
create index ix_lesson_subject_1 on lesson (subject_id);
alter table lesson add constraint fk_lesson_teacher_2 foreign key (teacher_id) references teacher (id) on delete restrict on update restrict;
create index ix_lesson_teacher_2 on lesson (teacher_id);
alter table lesson add constraint fk_lesson_teacherReview_3 foreign key (teacher_review_id) references review (id) on delete restrict on update restrict;
create index ix_lesson_teacherReview_3 on lesson (teacher_review_id);
alter table lesson add constraint fk_lesson_student_4 foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_lesson_student_4 on lesson (student_id);
alter table lesson add constraint fk_lesson_studentReview_5 foreign key (student_review_id) references review (id) on delete restrict on update restrict;
create index ix_lesson_studentReview_5 on lesson (student_review_id);
alter table student add constraint fk_student_user_6 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_student_user_6 on student (user_id);
alter table teacher add constraint fk_teacher_user_7 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_teacher_user_7 on teacher (user_id);



alter table subject_user add constraint fk_subject_user_subject_01 foreign key (subject_id) references subject (id) on delete restrict on update restrict;

alter table subject_user add constraint fk_subject_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists lesson;

drop table if exists review;

drop table if exists student;

drop table if exists subject;

drop table if exists subject_user;

drop table if exists teacher;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists lesson_seq;

drop sequence if exists review_seq;

drop sequence if exists student_seq;

drop sequence if exists subject_seq;

drop sequence if exists teacher_seq;

drop sequence if exists user_seq;

