INSERT INTO USER(name, email, password ) VALUES('Aluno', 'aluno@email.com', '$2a$10$5LoVcWUbeLM396o1tRN1F.Usg0PimPeVDOpHnHvWwTKquDmTOUmvu');
INSERT INTO USER(name, email, password ) VALUES('ADMIN', 'admin@email.com', '$2a$10$5LoVcWUbeLM396o1tRN1F.Usg0PimPeVDOpHnHvWwTKquDmTOUmvu');

INSERT INTO PROFILE(id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO PROFILE(id, name) VALUES (2, 'ROLE_COOP');

INSERT INTO USER_PROFILE(user_id, profiles_id) VALUES (1,2);
INSERT INTO USER_PROFILE(user_id, profiles_id) VALUES (2,1);

