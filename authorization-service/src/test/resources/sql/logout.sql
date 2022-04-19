INSERT INTO t_auth_user (f_fail_attempts, f_active, f_last_login_date, f_logout_date, f_password, f_usermail,
                         f_user_role_id, f_username, f_id)
values (0, 'true', 'now()', null, '$2a$12$OzQTWrYR88sn0n26H2eyxeJwcOiUT3JMtk.w3jrhFc/dx/qt7XiL.', 'usernametest@bsb.by',
        2, 'testadmin', 22);
INSERT INTO t_auth_user_details (f_first_name, f_registration_date, f_user_id)
VALUES ('Петя', 'now()', 22);
INSERT INTO t_auth_user_session (f_id, f_active, f_session_start, f_session_id)
VALUES (22, 'true', 'now()',
        'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0YWRtaW4iLCJpYXQiOjE2NDg1MDM4MTMsImV4cCI6MjI3OTY1NTgxM30.NC7xJQ3S3ECCNg1MRwuakc3axqi0Mu45gugS0YlXIKBgouIvrtlrsY1WpgbJJ6YCjuq0l9hd5v8ELOMtwyx5Vg')

