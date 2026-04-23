CREATE TABLE books (
    isbn                VARCHAR(13)     NOT NULL,
    title               VARCHAR(255)    NOT NULL,
    author              VARCHAR(255)    NOT NULL,
    genre               VARCHAR(20)     NOT NULL,
    publisher           VARCHAR(255)    NOT NULL,
    publication_year    INT             NOT NULL,
    language            VARCHAR(5)      NOT NULL,
    format              VARCHAR(20)     NOT NULL,
    price               NUMERIC(10, 2)  NOT NULL,
    available_copies    INT             NOT NULL,
    CONSTRAINT pk_books              PRIMARY KEY (isbn),
    CONSTRAINT chk_books_genre       CHECK (genre   IN ('SF', 'HISTORICAL', 'CRIME', 'FANTASY', 'CLASSIC', 'BIOGRAPHY', 'POPULAR_SCIENCE')),
    CONSTRAINT chk_books_format      CHECK (format  IN ('HARDCOVER', 'PAPERBACK')),
    CONSTRAINT chk_books_copies      CHECK (available_copies >= 0),
    CONSTRAINT chk_books_price       CHECK (price >= 0),
    CONSTRAINT chk_books_pub_year    CHECK (publication_year > 0)
);

CREATE TABLE reservations (
    id          BIGSERIAL       NOT NULL,
    order_id    VARCHAR(36)     NOT NULL,
    isbn        VARCHAR(13)     NOT NULL,
    quantity    INT             NOT NULL,
    created_at  TIMESTAMPTZ     NOT NULL,
    CONSTRAINT pk_reservations          PRIMARY KEY (id),
    CONSTRAINT uq_reservations_order_id UNIQUE (order_id),
    CONSTRAINT fk_reservations_isbn     FOREIGN KEY (isbn) REFERENCES books (isbn),
    CONSTRAINT chk_reservations_qty     CHECK (quantity > 0)
);

CREATE INDEX idx_reservations_isbn ON reservations (isbn);