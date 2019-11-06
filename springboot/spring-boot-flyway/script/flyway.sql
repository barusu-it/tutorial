create table hj_parcel.schema_version
(
  version_rank int not null,
  installed_rank int not null,
  version varchar(50) not null
    primary key,
  description varchar(200) not null,
  type varchar(20) not null,
  script varchar(1000) not null,
  checksum int null,
  installed_by varchar(100) not null,
  installed_on timestamp default CURRENT_TIMESTAMP not null,
  execution_time int not null,
  success tinyint(1) not null
)
;

create index schema_version_ir_idx
  on hj_parcel.schema_version (installed_rank)
;

create index schema_version_s_idx
  on hj_parcel.schema_version (success)
;

create index schema_version_vr_idx
  on hj_parcel.schema_version (version_rank)
;

