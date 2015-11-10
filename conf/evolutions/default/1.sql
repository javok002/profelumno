# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table chat (
  id                        bigint not null,
  teacher_id                bigint,
  student_id                bigint,
  constraint pk_chat primary key (id))
;

create table day_range (
  id                        bigint not null,
  teacher_id                bigint not null,
  day_enum                  integer,
  constraint ck_day_range_day_enum check (day_enum in (0,1,2,3,4,5,6)),
  constraint pk_day_range primary key (id))
;

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

create table message (
  id                        bigint not null,
  author_id                 bigint,
  chat_id                   bigint,
  msg                       varchar(255),
  date                      timestamp,
  constraint pk_message primary key (id))
;

create table range (
  id                        bigint not null,
  day_range_id              bigint not null,
  from_hour                 timestamp,
  to_hour                   timestamp,
  constraint pk_range primary key (id))
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
  ranking                   float,
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
create sequence chat_seq;

create sequence day_range_seq;

create sequence lesson_seq;

create sequence message_seq;

create sequence range_seq;

create sequence review_seq;

create sequence student_seq;

create sequence subject_seq;

create sequence teacher_seq;

create sequence user_seq;

alter table chat add constraint fk_chat_teacher_1 foreign key (teacher_id) references teacher (id) on delete restrict on update restrict;
create index ix_chat_teacher_1 on chat (teacher_id);
alter table chat add constraint fk_chat_student_2 foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_chat_student_2 on chat (student_id);
alter table day_range add constraint fk_day_range_teacher_3 foreign key (teacher_id) references teacher (id) on delete restrict on update restrict;
create index ix_day_range_teacher_3 on day_range (teacher_id);
alter table lesson add constraint fk_lesson_subject_4 foreign key (subject_id) references subject (id) on delete restrict on update restrict;
create index ix_lesson_subject_4 on lesson (subject_id);
alter table lesson add constraint fk_lesson_teacher_5 foreign key (teacher_id) references teacher (id) on delete restrict on update restrict;
create index ix_lesson_teacher_5 on lesson (teacher_id);
alter table lesson add constraint fk_lesson_teacherReview_6 foreign key (teacher_review_id) references review (id) on delete restrict on update restrict;
create index ix_lesson_teacherReview_6 on lesson (teacher_review_id);
alter table lesson add constraint fk_lesson_student_7 foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_lesson_student_7 on lesson (student_id);
alter table lesson add constraint fk_lesson_studentReview_8 foreign key (student_review_id) references review (id) on delete restrict on update restrict;
create index ix_lesson_studentReview_8 on lesson (student_review_id);
alter table message add constraint fk_message_author_9 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_message_author_9 on message (author_id);
alter table message add constraint fk_message_chat_10 foreign key (chat_id) references chat (id) on delete restrict on update restrict;
create index ix_message_chat_10 on message (chat_id);
alter table range add constraint fk_range_day_range_11 foreign key (day_range_id) references day_range (id) on delete restrict on update restrict;
create index ix_range_day_range_11 on range (day_range_id);
alter table student add constraint fk_student_user_12 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_student_user_12 on student (user_id);
alter table teacher add constraint fk_teacher_user_13 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_teacher_user_13 on teacher (user_id);



alter table subject_user add constraint fk_subject_user_subject_01 foreign key (subject_id) references subject (id) on delete restrict on update restrict;

alter table subject_user add constraint fk_subject_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists chat;

drop table if exists day_range;

drop table if exists lesson;

drop table if exists message;

drop table if exists range;

drop table if exists review;

drop table if exists student;

drop table if exists subject;

drop table if exists subject_user;

drop table if exists teacher;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists chat_seq;

drop sequence if exists day_range_seq;

drop sequence if exists lesson_seq;

drop sequence if exists message_seq;

drop sequence if exists range_seq;

drop sequence if exists review_seq;

drop sequence if exists student_seq;

drop sequence if exists subject_seq;

drop sequence if exists teacher_seq;

drop sequence if exists user_seq;

