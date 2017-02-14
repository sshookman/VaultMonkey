DROP TABLE IF EXISTS "test";

CREATE TABLE "test" (
        "id" INTEGER PRIMARY KEY NOT NULL UNIQUE,
        "name" VARCHAR NOT NULL,
		"dubs" DOUBLE NOT NULL,
		"good" BOOLEAN NOT NULL,
		"loooooooong" INTEGER NOT NULL
    );