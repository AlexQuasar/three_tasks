
-- Table "url"
create table url
(
	id serial not null,
    link varchar not null
);

create unique index url_id_uindex
	on url (id);

create unique index url_link_uindex
    on url (link);

alter table url
	add constraint url_pk
		primary key (id);

-- Table "url_duplicates"
create table url_duplicates
(
    id serial not null,
    link varchar not null
);

create unique index url_duplicates_id_uindex
    on url_duplicates (id);

create unique index url_duplicates_link_uindex
    on url_duplicates (link);

alter table url_duplicates
    add constraint url_duplicates_pk
        primary key (id);