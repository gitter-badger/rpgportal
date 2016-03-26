create table rpg_role (
  name varchar(255) not null,
  description varchar(255),
  primary key (name)
);

create table rpg_user (
  id  bigserial not null,
  birthdate date,
  email varchar(255),
  failed_attempts int4 not null,
  firstname varchar(255),
  lastname varchar(255),
  lock_end timestamp,
  locked boolean,
  password varchar(255) not null,
  username varchar(255) not null,
  primary key (id)
);

create table rpg_user_roles (
  user_id int8 not null,
  role varchar(255) not null
);

alter table rpg_user
add constraint UK_fjm7kr76c2vsm9m9re2khm6fn unique (username);

alter table rpg_user
add constraint UK_p0eephtm65fcmpg7nymbbagq2 unique (email);

alter table rpg_user_roles
add constraint FK_oqqdn4mhhdltx2rjjfdp90qhu
foreign key (role)
references rpg_role;

alter table rpg_user_roles
add constraint FK_gtu9hpnl1eix5rbt68jga6wik
foreign key (user_id)
references rpg_user;