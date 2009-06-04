drop user petstore cascade;
drop user opc cascade;
drop user supplier cascade;
drop user j2ee cascade;
drop user j2ee_ja cascade;
drop user shopper cascade;
drop user j2ee_zh cascade;

CREATE USER petstore IDENTIFIED BY petstore;
GRANT CONNECT, RESOURCE TO petstore;

CREATE USER opc IDENTIFIED BY opc;
GRANT CONNECT, RESOURCE TO opc;

CREATE USER supplier IDENTIFIED BY supplier;
GRANT CONNECT, RESOURCE TO supplier;

CREATE USER j2ee IDENTIFIED BY j2ee;
GRANT CONNECT, RESOURCE TO j2ee;

CREATE USER j2ee_ja IDENTIFIED BY j2ee_ja;
GRANT CONNECT, RESOURCE TO j2ee_ja;

CREATE USER shopper IDENTIFIED BY shopper;
GRANT CONNECT, RESOURCE TO j2ee;

CREATE USER j2ee_zh IDENTIFIED BY j2ee_zh;
GRANT CONNECT, RESOURCE TO j2ee_zh;

