--this script initiates db for h2 db (used in test profile)
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (account_status, email, first_name) values ('NEW', 'brian@domain.com', 'Brian')
insert into user (account_status, email, first_name) values ('CONFIRMED', 'thomas@domain.com', 'Thomas')
insert into user (account_status, email, first_name) values ('REMOVED', 'ehhehehe@domain.com', 'Bogdan')
insert into blog_post values (1, 'Java is simple', 1)
insert into blog_post values (3, 'Cpp is simple', 3)