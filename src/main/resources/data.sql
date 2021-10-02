insert into bs.users (login, password, role, rating, recall_count)
values ('admin@mail.com', '$2a$10$QblZzp9RRjO3aw6gGPxfOOHT0DW9rXBBq6QF5laaRZ0jQIzMdLgO.','ADMIN', 0.0, 0);

insert into bs.users (login, password,  role, rating, recall_count)
values ('client@mail.com', '$2a$10$QblZzp9RRjO3aw6gGPxfOOHT0DW9rXBBq6QF5laaRZ0jQIzMdLgO.', 'CLIENT', 0.0, 0);

insert into bs.users (login, password, role, rating, recall_count)
values ('master1@mail.com', '$2a$10$QblZzp9RRjO3aw6gGPxfOOHT0DW9rXBBq6QF5laaRZ0jQIzMdLgO.', 'MASTER', 9.0, 0);

insert into bs.users (login, password, role, rating, recall_count)
values ('master2@mail.com', '$2a$10$QblZzp9RRjO3aw6gGPxfOOHT0DW9rXBBq6QF5laaRZ0jQIzMdLgO.', 'MASTER', 8.0, 0);

insert into bs.users (login, password, role, rating, recall_count)
values ('master3@mail.com', '$2a$10$QblZzp9RRjO3aw6gGPxfOOHT0DW9rXBBq6QF5laaRZ0jQIzMdLgO.', 'MASTER', 9.0, 0);

insert into bs.users (login, password, role, rating, recall_count)
values ('master4@mail.com', '$2a$10$QblZzp9RRjO3aw6gGPxfOOHT0DW9rXBBq6QF5laaRZ0jQIzMdLgO.', 'MASTER', 8.0, 0);

insert into bs.users (login, password, role, rating, recall_count)
values ('master5@mail.com', '$2a$10$QblZzp9RRjO3aw6gGPxfOOHT0DW9rXBBq6QF5laaRZ0jQIzMdLgO.', 'MASTER', 9.0, 0);

insert into bs.users (login, password, role, rating, recall_count)
values ('master6@mail.com', '$2a$10$QblZzp9RRjO3aw6gGPxfOOHT0DW9rXBBq6QF5laaRZ0jQIzMdLgO.', 'MASTER', 8.0, 0);

insert into bs.users (login, password, role, rating, recall_count)
values ('master7@mail.com', '$2a$10$QblZzp9RRjO3aw6gGPxfOOHT0DW9rXBBq6QF5laaRZ0jQIzMdLgO.', 'MASTER', 9.0, 0);

insert into bs.users (login, password, role, rating, recall_count)
values ('master8@mail.com', '$2a$10$QblZzp9RRjO3aw6gGPxfOOHT0DW9rXBBq6QF5laaRZ0jQIzMdLgO.', 'MASTER', 8.0, 0);

insert into bs.users (login, password, role, rating, recall_count)
values ('master9@mail.com', '$2a$10$QblZzp9RRjO3aw6gGPxfOOHT0DW9rXBBq6QF5laaRZ0jQIzMdLgO.', 'MASTER', 8.0, 0);

insert into bs.users (login, password, role, rating, recall_count)
values ('master10@mail.com', '$2a$10$QblZzp9RRjO3aw6gGPxfOOHT0DW9rXBBq6QF5laaRZ0jQIzMdLgO.', 'MASTER', 8.0, 0);

insert into bs.procedures(name, duration)
values ('hair_coloring', Interval '1 hour');

insert into bs.procedures(name, duration)
values ('face_massage', Interval '1 hour');

insert into bs.procedures(name, duration)
values ('eyelash_extension', Interval '30 min');

insert into bs.procedures(name, duration)
values ('nail_staining', Interval '30 min');

insert into bs.users_procedures(user_id, procedure_id)
values (3, 1);

insert into bs.users_procedures(user_id, procedure_id)
values (4, 1);

insert into bs.users_procedures(user_id, procedure_id)
values (5, 1);

insert into bs.users_procedures(user_id, procedure_id)
values (6, 2);

insert into bs.users_procedures(user_id, procedure_id)
values (7, 2);

insert into bs.users_procedures(user_id, procedure_id)
values (8, 2);

insert into bs.users_procedures(user_id, procedure_id)
values (9, 3);

insert into bs.users_procedures(user_id, procedure_id)
values (10, 3);

insert into bs.users_procedures(user_id, procedure_id)
values (11, 3);

insert into bs.users_procedures(user_id, procedure_id)
values (12, 4);

insert into bs.appointments(procedure_id, master_id, client_id, start_time, status)
values (1, 3, 2, '2021-09-27 11:00:00', 'DECLARED');

insert into bs.appointments(procedure_id, master_id, client_id, start_time, status)
values (1, 4, 2, '2021-09-28 13:00:00', 'CONFIRMED');

insert into bs.appointments(procedure_id, master_id, client_id, start_time, status)
values (1, 3, 2, '2021-09-30 15:00:00', 'FINISHED');

insert into bs.appointments(procedure_id, master_id, client_id, start_time, status)
values (1, 3, 2, '2021-09-30 13:00:00', 'PAID');

insert into bs.appointments(procedure_id, master_id, client_id, start_time, status)
values (1, 4, 2, '2021-09-29 15:00:00',  'RATED');


