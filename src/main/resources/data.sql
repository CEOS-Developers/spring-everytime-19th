-- INSERT INTO users (USER_ID, USERNAME, PASSWORD, NICKNAME, ACTIVATED)
-- VALUES (1, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);
--
-- INSERT INTO AUTHORITY (AUTHORITY_NAME) VALUES ('ROLE_USER');
-- INSERT INTO AUTHORITY (AUTHORITY_NAME) VALUES ('ROLE_ADMIN');
--
-- INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_NAME) VALUES (1, 'ROLE_USER');
-- INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_NAME) VALUES (1, 'ROLE_ADMIN');

-- Inserting a user
INSERT INTO user (id, username, password, nickname, is_admin, user_last, user_first, email, is_banned, activated)
VALUES (1, 'admin', 'admin', 'admin', true, 'Doe', 'John', 'john.doe@example.com', false, 1);

-- Inserting authorities
INSERT INTO authority (authority_name) VALUES ('ROLE_USER');
INSERT INTO authority (authority_name) VALUES ('ROLE_ADMIN');

-- Associating user with authorities
INSERT INTO user_authority (id, authority_name) VALUES (1, 'ROLE_USER');
INSERT INTO user_authority (id, authority_name) VALUES (1, 'ROLE_ADMIN');
