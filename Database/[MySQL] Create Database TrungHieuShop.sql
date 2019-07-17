-- B1: TRUY VẤN DATABASE TRƯỚC
CREATE DATABASE IF NOT EXISTS TrungHieuShop CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci';

-- BẢNG USER
CREATE TABLE User(
	id 				INT(11)  		NOT NULL AUTO_INCREMENT,
	username 		VARCHAR(50)		NOT NULL,
	password 		VARCHAR(100)	NOT NULL,
	email 			VARCHAR(50) 	NOT NULL,
	phone			VARCHAR(20) 	NULL,
	fullname		VARCHAR(50) 	NULL,
	gender			INT(11) 		NULL,
	address			VARCHAR(100) 	NULL,
	birthday		DATETIME		NULL,
	avatar 			VARCHAR(200)	NULL,
	datecreated 	DATETIME		NULL,
	usercreated		VARCHAR(50) 	NULL,
	islock			BOOLEAN			NULL,
	lockreason		VARCHAR(50)		NULL,

	PRIMARY KEY (id)
	
)ENGINE = InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- BẢNG ROLE
CREATE TABLE Role(
	id 				INT(11) 		NOT NULL AUTO_INCREMENT,
	name			VARCHAR(50) 	NOT NULL,

	PRIMARY KEY (id)

)ENGINE = InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- BẢNG USER_ROLE
CREATE TABLE User_Role(
	user_id 		INT(11) 		NOT NULL,
	role_id	 		INT(11) 		NOT NULL,

	CONSTRAINT FK_User_Role_User 			FOREIGN KEY (user_id) 		REFERENCES User(id),
	CONSTRAINT FK_User_Role_Role 			FOREIGN KEY (role_id) 		REFERENCES Role(id)

)ENGINE = InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- BẢNG GROUP_PERMISSION
CREATE TABLE Group_Permission(
	id 				INT(11)			NOT NULL AUTO_INCREMENT,
	name 			VARCHAR(100)	NOT NULL,
	parent_id		INT(11)			NULL,

	PRIMARY KEY (id)

)ENGINE = InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- BẢNG PERMISSION
CREATE TABLE Permission(
	id 				INT(11) 		NOT NULL AUTO_INCREMENT,
	name 			VARCHAR(100)	NOT NULL,
	link			VARCHAR(100)	NOT NULL,
	islock 			BOOLEAN 		NULL,
	group_id		INT(11)			NOT NULL,

	PRIMARY KEY (id),
	CONSTRAINT FK_Permission_Group_Permission	FOREIGN KEY (group_id) 		REFERENCES Group_Permission(id)

)ENGINE = InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- BẢNG ROLE_PERMISSION
CREATE TABLE Role_Permission(
	role_id 		INT(11)			NOT NULL,
	permission_id 	INT(11)			NOT NULL,

	CONSTRAINT FK_Role_Permission_Role			FOREIGN KEY (role_id)		REFERENCES Role(id),
	CONSTRAINT FK_Role_Permission_Permission	FOREIGN KEY	(permission_id)	REFERENCES Permission(id)

)ENGINE = InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- BẢNG ROLE_PERMISSION
CREATE TABLE Persistent_Logins(
	username 		VARCHAR(50) 	NOT NULL,
	series 			VARCHAR(100)	NOT NULL,
	token			VARCHAR(500)	NOT NULL,
	last_used 		TIMESTAMP 		NOT NULL,

	PRIMARY KEY (series)

)ENGINE = InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- BẢNG CATEGORY
CREATE TABLE Category(
	id 				INT(11)			NOT NULL AUTO_INCREMENT,
	name 			VARCHAR(50)		NOT NULL,
	parent_id 		INT(11)			NULL,
	datecreated 	DATETIME		NULL,
	usercreated		VARCHAR(50) 	NULL,

	PRIMARY KEY (id)

)ENGINE = InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- BẢNG MANUFACTURER
CREATE TABLE Manufacturer(
	id 				INT(11)			NOT NULL AUTO_INCREMENT,
	name 			VARCHAR(50)		NOT NULL,
	parent_id 		INT(11)			NULL,
	datecreated 	DATETIME		NULL,
	usercreated		VARCHAR(50) 	NULL,

	PRIMARY KEY (id)

)ENGINE = InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- BẢNG PRODUCT
CREATE TABLE Product(
	id 					INT(11)			NOT NULL AUTO_INCREMENT,
	code				VARCHAR(50)		NULL,
	name 				VARCHAR(50)		NOT NULL,
	price				FLOAT(11)		NOT NULL,
	quantity			INT(11)			NOT NULL,
	image_link			VARCHAR(500)	NULL,
	image_list			VARCHAR(1000)	NULL,
	discount			INT(11) 		NULL,
	content 			VARCHAR(1000)	NULL,
	view				INT(11)			NULL,
	status				INT(11)			NULL,
	datecreated 		DATETIME		NULL,
	usercreated			VARCHAR(50) 	NULL,
	category_id			INT(11)			NOT NULL,
	manufacturer_id		INT(11)			NOT NULL,

	PRIMARY KEY (id),
	CONSTRAINT FK_Product_Category				FOREIGN KEY (category_id)			REFERENCES Category(id),
	CONSTRAINT FK_Product_Manufacturer			FOREIGN KEY (manufacturer_id)		REFERENCES Manufacturer(id)

)ENGINE = InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
