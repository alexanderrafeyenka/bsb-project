INSERT INTO t_conv_application
(f_convert_from, f_convert_to, f_employee_id, f_name_legal, f_percent_conv, f_status, f_uuid, f_id)
VALUES ('BYN', 'EUR', 1, 'BsbBank', 17.3579, 'New', 'c0c4bb28-af62-11ec-b909-0242ac120002', 37);
INSERT INTO t_conv_application_details(f_create_date, f_last_update, f_note, f_application_id)
VALUES ('now()', 'now()', 'my favourite bank', 37)
