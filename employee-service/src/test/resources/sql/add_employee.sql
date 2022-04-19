INSERT INTO t_empl_employee
(f_full_name, f_hired, f_name_legal, f_iban_byn, f_iban_currency, f_recruitment_date, f_termination_date, f_id)
values ('Сидоров Алексей Ю', 'true', 'company', 'BY43UNBS56732131232620004323', 'BY43UNBS34562136232120107371',
        '12/02/2022', '12/02/2034', 7);
insert into t_empl_employee_details (f_create_date, f_last_update, f_note, f_id)
values ('now()', 'now()', 'text', 7);