-- originalテーブルの作成
DROP TABLE IF EXISTS original;
CREATE TABLE original (
	id serial,
	name text,
	condition_id Integer NOT NULL,
	category_name text,
	brand text,
	price Double PRECISION,
	shipping Integer NOT NULL,
	description text
);
-- categoryテーブルの作成
DROP TABLE IF EXISTS category;
CREATE TABLE category (
	id serial PRIMARY KEY,
	parent_id integer,
	name character varying(255),
	name_all character varying(255),
	FOREIGN KEY (parent_id) REFERENCES category(id) ON DELETE CASCADE
);
-- itemsテーブルの作成
DROP TABLE IF EXISTS items;
CREATE TABLE items (
	id serial primary key,
	name character varying(255),
	condition Integer,
	category Integer,
	brand character varying(255),
	price Double PRECISION,
	stock Integer,
	shipping Integer,
	description text,
	update_time timestamp,
	del_flg integer DEFAULT 0
);
-- usersテーブルの作成
DROP TABLE IF EXISTS users;
CREATE TABLE users (
	id serial PRIMARY KEY,
	name character varying(255),
	mail character varying(255),
	password character varying(255),
	authority Integer
);
-- imagesテーブルの作成
DROP TABLE IF EXISTS images;
CREATE TABLE images (
	id serial PRIMARY KEY,
	item_id Integer NOT NULL,
	path text NOT NULL,
	FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
)