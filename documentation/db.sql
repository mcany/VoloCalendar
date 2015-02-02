-- Table: "DayStatistics"

-- DROP TABLE "DayStatistics";

CREATE TABLE "DayStatistics"
(
  id character(36) NOT NULL,
  "userId" character(36),
  "weekBeginDate" date NOT NULL,
  date date NOT NULL,
  hour0 smallint NOT NULL DEFAULT 0,
  hour1 smallint NOT NULL DEFAULT 0,
  hour2 smallint NOT NULL DEFAULT 0,
  hour3 smallint NOT NULL DEFAULT 0,
  hour4 smallint NOT NULL DEFAULT 0,
  hour5 smallint NOT NULL DEFAULT 0,
  hour6 smallint NOT NULL DEFAULT 0,
  hour7 smallint NOT NULL DEFAULT 0,
  hour8 smallint NOT NULL DEFAULT 0,
  hour9 smallint NOT NULL DEFAULT 0,
  hour10 smallint NOT NULL DEFAULT 0,
  hour11 smallint NOT NULL DEFAULT 0,
  hour12 smallint NOT NULL DEFAULT 0,
  hour13 smallint NOT NULL DEFAULT 0,
  hour14 smallint NOT NULL DEFAULT 0,
  hour15 smallint NOT NULL DEFAULT 0,
  hour16 smallint NOT NULL DEFAULT 0,
  hour17 smallint NOT NULL DEFAULT 0,
  hour18 smallint NOT NULL DEFAULT 0,
  hour19 smallint NOT NULL DEFAULT 0,
  hour20 smallint NOT NULL DEFAULT 0,
  hour21 smallint NOT NULL DEFAULT 0,
  hour22 smallint NOT NULL DEFAULT 0,
  hour23 smallint NOT NULL DEFAULT 0,
  "weekDayIndex" smallint NOT NULL,
  CONSTRAINT "PK_DayStatistics" PRIMARY KEY (id),
  CONSTRAINT "UserIdPlusDate_Unique" UNIQUE ("userId", date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "DayStatistics"
  OWNER TO postgres;

-- Index: date_index

-- DROP INDEX date_index;

CREATE INDEX date_index
  ON "DayStatistics"
  USING btree
  (date);

-- Index: "userId_index"

-- DROP INDEX "userId_index";

CREATE INDEX "userId_index"
  ON "DayStatistics"
  USING btree
  ("userId" COLLATE pg_catalog."default");

-- Index: "weekBeginDate_index"

-- DROP INDEX "weekBeginDate_index";

CREATE INDEX "weekBeginDate_index"
  ON "DayStatistics"
  USING btree
  ("weekBeginDate");

-- Table: "Store"

-- DROP TABLE "Store";

CREATE TABLE "Store"
(
  id character(36) NOT NULL,
  data character(3000),
  CONSTRAINT "PK_Store" PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE "Store"
OWNER TO postgres;

-- Table: "User"

-- DROP TABLE "User";

CREATE TABLE "User"
(
  id character(36) NOT NULL,
  address character(255),
  admin boolean,
  bic character(255),
  city character(255),
  "contractType" smallint,
  deleted boolean,
  email character(255),
  iban character(255),
  name character(255),
  password character(255),
  plz character(255),
  street character(255),
  "telephoneNumber" character(255),
  "telephoneType" smallint,
  "transportType" smallint,
  CONSTRAINT "User_pkey" PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE "User"
OWNER TO postgres;

-- Index: admin_index

-- DROP INDEX admin_index;

CREATE INDEX admin_index
ON "User"
USING btree
(admin);

-- Index: email_index

-- DROP INDEX email_index;

CREATE INDEX email_index
ON "User"
USING btree
(email COLLATE pg_catalog."default");

-- Index: name_index

-- DROP INDEX name_index;

CREATE INDEX name_index
ON "User"
USING btree
(name COLLATE pg_catalog."default");

-- Index: "telephoneNumber_index"

-- DROP INDEX "telephoneNumber_index";

CREATE INDEX "telephoneNumber_index"
ON "User"
USING btree
("telephoneNumber" COLLATE pg_catalog."default");

INSERT INTO "User"(
  id, address, admin, bic, city, "contractType", deleted, email,
  iban, name, password, plz, street, "telephoneNumber", "telephoneType",
  "transportType")
VALUES ('1', null, true, null, null, null, false, 'eminguliyev1987@gmail.com',
        null, 'Emin Guliyev', '1', null, null, null, null,
        null);
