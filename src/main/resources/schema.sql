CREATE TABLE Employee
(
    id_employee     VARCHAR(10)    NOT NULL,
    empl_username   VARCHAR(100)   NOT NULL UNIQUE,
    empl_password   VARCHAR(1000)  NOT NULL,
    empl_surname    VARCHAR(50)    NOT NULL,
    empl_name       VARCHAR(50)    NOT NULL,
    empl_patronymic VARCHAR(50),
    empl_role       VARCHAR(10)    NOT NULL,
    salary          DECIMAL(13, 4) NOT NULL,
    date_of_birth   DATE           NOT NULL,
    date_of_start   DATE           NOT NULL,
    phone_number    VARCHAR(13)    NOT NULL,
    city            VARCHAR(50)    NOT NULL,
    street          VARCHAR(50)    NOT NULL,
    zip_code        VARCHAR(9)     NOT NULL,
    PRIMARY KEY (id_employee)
);

CREATE SEQUENCE category_id_seq START 1;
CREATE TABLE Category
(
    category_number INT PRIMARY KEY DEFAULT nextval('category_id_seq'),
    category_name   VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Customer_Card
(
    card_number     VARCHAR(13) NOT NULL,
    cust_surname    VARCHAR(50) NOT NULL,
    cust_name       VARCHAR(50) NOT NULL,
    cust_patronymic VARCHAR(50),
    phone_number    VARCHAR(13) NOT NULL,
    city            VARCHAR(50),
    street          VARCHAR(50),
    zip_code        VARCHAR(9),
    percent         INT         NOT NULL,
    PRIMARY KEY (card_number)
);

CREATE TABLE "check"
(
    check_number VARCHAR(10)    NOT NULL,
    id_employee  VARCHAR(10)    NOT NULL,
    card_number  VARCHAR(13),
    print_date   TIMESTAMP      NOT NULL,
    sum_total    DECIMAL(13, 4) NOT NULL,
    vat          DECIMAL(13, 4) NOT NULL,
    PRIMARY KEY (check_number),
    FOREIGN KEY (id_employee) REFERENCES Employee (id_employee) ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (card_number) REFERENCES Customer_Card (card_number) ON UPDATE CASCADE ON DELETE NO ACTION
);

CREATE SEQUENCE product_id_seq START 1;
CREATE TABLE Product
(
    id_product      INT PRIMARY KEY DEFAULT nextval('product_id_seq'),
    category_number INT          NOT NULL,
    product_name    VARCHAR(50)  NOT NULL UNIQUE,
    expiration_date DATE,
    characteristics VARCHAR(100) NOT NULL,
    FOREIGN KEY (category_number)
        REFERENCES Category (category_number)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE Store_Product
(
    UPC                 VARCHAR(12)    NOT NULL,
    UPC_prom            VARCHAR(12),
    id_product          INT            NOT NULL,
    selling_price       DECIMAL(13, 4) NOT NULL,
    products_number     INT            NOT NULL,
    promotional_product BOOLEAN        NOT NULL,
    PRIMARY KEY (UPC),
    FOREIGN KEY (UPC_prom)
        REFERENCES Store_Product (UPC)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    FOREIGN KEY (id_product)
        REFERENCES Product (id_product)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);



CREATE TABLE Sale
(
    UPC            VARCHAR(12)    NOT NULL,
    check_number   VARCHAR(10)    NOT NULL,
    product_number INT            NOT NULL,
    selling_price  DECIMAL(13, 4) NOT NULL,
    PRIMARY KEY (UPC, check_number),
    FOREIGN KEY (UPC) REFERENCES Store_Product (UPC) ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (check_number) REFERENCES "check" (check_number) ON UPDATE CASCADE ON DELETE CASCADE
);


