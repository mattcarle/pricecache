drop table if exists price, vendor, instrument;

drop sequence if exists seq_price;
drop sequence if exists seq_vendor;
drop sequence if exists seq_instrument;

create table vendor
(
  vendor_id integer not null,
  name varchar2(100) not null,
  constraint vendor_pk primary key(vendor_id),
  constraint vendor_name_uq unique(name)
);

CREATE SEQUENCE SEQ_VENDOR INCREMENT BY 1 START WITH 1001;

create table instrument
(
instrument_id integer not null,
isin varchar(30) null,
sedol varchar(30) null,
cusip varchar(30) null,
ticker varchar(30) null,
ric varchar(30) null,
bbgid varchar(30) null,
constraint instrument_pk primary key(instrument_id)
);

CREATE SEQUENCE SEQ_INSTRUMENT INCREMENT BY 1 START WITH 1001;

create table price
(
  price_id integer not null,
  create_time datetime not null,
  price numeric(15,8) not null,
  vendor_id integer not null,
  instrument_id integer not null,
  is_latest char(1) not null,
  constraint price_pk primary key(price_id),
  constraint vendor_fk foreign key (vendor_id) references vendor(vendor_id),
  constraint instrument_fk foreign key(instrument_id) references instrument(instrument_id)
);

CREATE SEQUENCE SEQ_PRICE INCREMENT BY 1 START WITH 1001;