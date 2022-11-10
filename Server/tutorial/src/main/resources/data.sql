INSERT INTO CATEGORY(id, name) VALUES (1, 'Eurogames');
INSERT INTO CATEGORY(id, name) VALUES (2, 'Ameritrash');
INSERT INTO CATEGORY(id, name) VALUES (3, 'Familiar');

INSERT INTO AUTHOR(id, name, nationality) VALUES (1, 'Alan R. Moon', 'US');
INSERT INTO AUTHOR(id, name, nationality) VALUES (2, 'Vital Lacerda', 'PT');
INSERT INTO AUTHOR(id, name, nationality) VALUES (3, 'Simone Luciani', 'IT');
INSERT INTO AUTHOR(id, name, nationality) VALUES (4, 'Perepau Llistosella', 'ES');
INSERT INTO AUTHOR(id, name, nationality) VALUES (5, 'Michael Kiesling', 'DE');
INSERT INTO AUTHOR(id, name, nationality) VALUES (6, 'Phil Walker-Harding', 'US');

INSERT INTO GAME(id, title, age, category_id, author_id) VALUES (1, 'On Mars', '14', 1, 2);
INSERT INTO GAME(id, title, age, category_id, author_id) VALUES (2, 'Aventureros al tren', '8', 3, 1);
INSERT INTO GAME(id, title, age, category_id, author_id) VALUES (3, '1920: Wall Street', '12', 1, 4);
INSERT INTO GAME(id, title, age, category_id, author_id) VALUES (4, 'Barrage', '14', 1, 3);
INSERT INTO GAME(id, title, age, category_id, author_id) VALUES (5, 'Los viajes de Marco Polo', '12', 1, 3);
INSERT INTO GAME(id, title, age, category_id, author_id) VALUES (6, 'Azul', '8', 3, 5);

INSERT INTO CLIENT(id, name) VALUES (1, 'Cliente 1');
INSERT INTO CLIENT(id, name) VALUES (2, 'Cliente 2');
INSERT INTO CLIENT(id, name) VALUES (3, 'Cliente 3');
INSERT INTO CLIENT(id, name) VALUES (4, 'Cliente 4');
INSERT INTO CLIENT(id, name) VALUES (5, 'Cliente 5');
INSERT INTO CLIENT(id, name) VALUES (6, 'Cliente 6');


INSERT INTO LOAN(id, game_id, client_id, loan_date, return_date) VALUES(1,1,1,TO_DATE('03/11/2022', 'DD/MM/YYYY'),TO_DATE('13/11/2022', 'DD/MM/YYYY'));
INSERT INTO LOAN(id, game_id, client_id, loan_date, return_date) VALUES(2,2,1,TO_DATE('03/11/2022', 'DD/MM/YYYY'),TO_DATE('14/11/2022', 'DD/MM/YYYY'));
INSERT INTO LOAN(id, game_id, client_id, loan_date, return_date) VALUES(3,1,4,TO_DATE('14/11/2022', 'DD/MM/YYYY'),TO_DATE('16/11/2022', 'DD/MM/YYYY'));
INSERT INTO LOAN(id, game_id, client_id, loan_date, return_date) VALUES(4,4,3,TO_DATE('06/11/2022', 'DD/MM/YYYY'),TO_DATE('16/11/2022', 'DD/MM/YYYY'));
INSERT INTO LOAN(id, game_id, client_id, loan_date, return_date) VALUES(5,3,2,TO_DATE('07/11/2022', 'DD/MM/YYYY'),TO_DATE('19/11/2022', 'DD/MM/YYYY'));
INSERT INTO LOAN(id, game_id, client_id, loan_date, return_date) VALUES(6,5,3,TO_DATE('20/11/2022', 'DD/MM/YYYY'),TO_DATE('3/12/2022', 'DD/MM/YYYY'));