
--roles
INSERT INTO ROLE(ID,ROLE_NAME,ROLE_DESCRIPTION) VALUES(1,'ADMIN','Admin Role');
INSERT INTO ROLE(ID,ROLE_NAME,ROLE_DESCRIPTION) VALUES(2,'EMPLOYEE','Employee Role');
INSERT INTO ROLE(ID,ROLE_NAME,ROLE_DESCRIPTION) VALUES(3,'CUSTOMER','Customer Role');
INSERT INTO ROLE(ID,ROLE_NAME,ROLE_DESCRIPTION) VALUES(4,'MASTER','Master/Dispatcher Role');

--user details
INSERT INTO user_details(id, first_name, last_name, phone, email)
VALUES(1, 'Adam','Adamowicz','+48111222333','adamowicz@email.com');

INSERT INTO user_details(id, first_name, last_name, phone, email)
VALUES(2, 'Adam','Klient','+48123456789','klient1@mail.com');

INSERT INTO user_details(id, first_name, last_name, phone, email)
VALUES(3, 'Master','Master','+48341555999','master@email.com');

--users
INSERT INTO USER_T(USERNAME,PASSWORD,ENABLED,CREDENTIALS_EXPIRED,EXPIRED,LOCKED,DTYPE, DETAILS_ID)
VALUES ('adam_adamowicz','$2a$10$aiBgroa5UbCph3gb4WSiD.njeKqgsjXmHv/pbXiRVAYPMlewWDLD6',true,false,false,false, 'EMPLOYEE', 1);

INSERT INTO USER_T(USERNAME,PASSWORD,ENABLED,CREDENTIALS_EXPIRED,EXPIRED,LOCKED,DTYPE, DETAILS_ID)
VALUES ('adam_klient','$2a$10$aiBgroa5UbCph3gb4WSiD.njeKqgsjXmHv/pbXiRVAYPMlewWDLD6',true,false,false,false,'CUSTOMER', 2);

INSERT INTO USER_T(USERNAME,PASSWORD,ENABLED,CREDENTIALS_EXPIRED,EXPIRED,LOCKED,DTYPE, DETAILS_ID)
VALUES ('master','$2a$10$aiBgroa5UbCph3gb4WSiD.njeKqgsjXmHv/pbXiRVAYPMlewWDLD6',true,false,false,false, 'EMPLOYEE', 3);


INSERT INTO USER_ROLE(USER_ID,ID) VALUES(1,1);
INSERT INTO USER_ROLE(USER_ID,ID) VALUES(2,3);
INSERT INTO USER_ROLE(USER_ID,ID) VALUES(3,4);