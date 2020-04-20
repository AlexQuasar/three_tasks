create table if not exists url
(
	id serial not null,
    link varchar not null
);

create unique index if not exists url_id_uindex
	on url (id);

alter table url
	add constraint url_pk
		primary key (id);