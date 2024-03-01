--creation de la table messages
CREATE TABLE if not exists messages(
	id serial primary key,
	number int not null,
	info varchar(255) not null
);