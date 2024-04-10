-- This script only contains the table creation statements and does not fully represent the table in the database. Do not use it as a backup.

-- Sequence and defined type
CREATE SEQUENCE IF NOT EXISTS _user_id_seq;

-- Table Definition
CREATE TABLE "public"."_user" (
                                  "email_verified" bool NOT NULL,
                                  "id" int4 NOT NULL DEFAULT nextval('_user_id_seq'::regclass),
                                  "is_active" bool NOT NULL,
                                  "role" int2 CHECK ((role >= 0) AND (role <= 1)),
                                  "email" varchar,
                                  "firstname" varchar,
                                  "lastname" varchar,
                                  "password" varchar,
                                  PRIMARY KEY ("id")
);

-- This script only contains the table creation statements and does not fully represent the table in the database. Do not use it as a backup.

-- Table Definition
CREATE TABLE "public"."email_verification_token" (
                                                     "user_id" int4,
                                                     "created_at" timestamp,
                                                     "token" varchar NOT NULL,
                                                     CONSTRAINT "fk6ugv95pxetldg10r8k4jho5u0" FOREIGN KEY ("user_id") REFERENCES "public"."_user"("id"),
                                                     PRIMARY KEY ("token")
);