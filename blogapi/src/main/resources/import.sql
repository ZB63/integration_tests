--this script initiates db for h2 db (used in test profile)
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (account_status, email, first_name) values ('NEW', 'brian@domain.com', 'Brian')
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'john@domain.com', 'John')
insert into user (account_status, email, first_name) values ('REMOVED', 'jan@domain.com', 'Jan')
insert into blog_post values (1, 'Some text', 1)
insert into blog_post values (2, 'Some text', 2)