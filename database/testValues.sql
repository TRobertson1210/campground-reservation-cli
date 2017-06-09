BEGIN TRANSACTION;

INSERT INTO reservation(site_id, name, from_date, to_date, create_date)
VALUES(618, 'TESTA', '2017-06-01', '2017-06-30', NOW());

INSERT INTO reservation(site_id, name, from_date, to_date, create_date)
VALUES(619, 'TESTB', '2017-06-01', '2017-06-30', NOW());

INSERT INTO reservation(site_id, name, from_date, to_date, create_date)
VALUES(620, 'TESTC', '2017-06-01', '2017-06-30', NOW());

INSERT INTO reservation(site_id, name, from_date, to_date, create_date)
VALUES(621, 'TESTD', '2017-06-01', '2017-06-30', NOW());

INSERT INTO reservation(site_id, name, from_date, to_date, create_date)
VALUES(622, 'TESTE', '2017-06-01', '2017-06-30', NOW());

SELECT *
FROM reservation
JOIN site ON reservation.site_id = site.site_id
WHERE campground_id = 7;

ROLLBACK;

CREATE DATABASE campground_test;