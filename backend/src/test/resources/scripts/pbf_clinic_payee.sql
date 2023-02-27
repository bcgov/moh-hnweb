--Archived
INSERT INTO pbf.pbf_clinic_payee (pbf_clinic_payee_id, archived, cancel_date, effective_date, payee_number, report_group) VALUES (10000, 'true', DATEADD(MONTH, 6, CURRENT_TIMESTAMP), DATEADD(YEAR, -23, CURRENT_TIMESTAMP), '00053', 'AAAA');
--Effective with no cancel date
INSERT INTO pbf.pbf_clinic_payee (pbf_clinic_payee_id, archived, cancel_date, effective_date, payee_number, report_group) VALUES (10001, 'false', NULL, DATEADD(YEAR, -2, CURRENT_TIMESTAMP) , '00023', 'AAAA');
--Effective with future cancel date
INSERT INTO pbf.pbf_clinic_payee (pbf_clinic_payee_id, archived, cancel_date, effective_date, payee_number, report_group) VALUES (10002, 'false', DATEADD(YEAR, 2, CURRENT_TIMESTAMP), DATEADD(YEAR, -2, CURRENT_TIMESTAMP) , '00033', 'BBBB');
--Effective today
INSERT INTO pbf.pbf_clinic_payee (pbf_clinic_payee_id, archived, cancel_date, effective_date, payee_number, report_group) VALUES (10003, 'false', NULL, CURRENT_TIMESTAMP, '00043', 'BBBB');
--Effective tomorrow
INSERT INTO pbf.pbf_clinic_payee (pbf_clinic_payee_id, archived, cancel_date, effective_date, payee_number, report_group) VALUES (10004, 'false', NULL, DATEADD(DAY, 1, CURRENT_TIMESTAMP), 'T0055', 'CCCC');
--Cancelled today => still active
INSERT INTO pbf.pbf_clinic_payee (pbf_clinic_payee_id, archived, cancel_date, effective_date, payee_number, report_group) VALUES (10005, 'false', CURRENT_TIMESTAMP, DATEADD(YEAR, -2, CURRENT_TIMESTAMP), 'T0053', 'DDDD');
--Cancelled yesterday
INSERT INTO pbf.pbf_clinic_payee (pbf_clinic_payee_id, archived, cancel_date, effective_date, payee_number, report_group) VALUES (10006, 'false', DATEADD(DAY, -1, CURRENT_TIMESTAMP), DATEADD(YEAR, -2, CURRENT_TIMESTAMP), 'X0053', 'DDDD');
