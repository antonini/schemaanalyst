-- 10
-- NNCA
-- Added NOT NULL to column TNUM2 in table TEST12549

CREATE TABLE "TEST12549" (
	"TNUM1"	NUMERIC(5, 0)	CONSTRAINT "CND12549A" NOT NULL,
	"TNUM2"	NUMERIC(5, 0)	CONSTRAINT "CND12549B" UNIQUE	NOT NULL,
	"TNUM3"	NUMERIC(5, 0),
	CONSTRAINT "CND12549C" CHECK ("TNUM3" > 0)
)

