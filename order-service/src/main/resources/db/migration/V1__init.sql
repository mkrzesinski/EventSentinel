CREATE TABLE customer_orders (
    id          VARCHAR(36)     NOT NULL,
    user_id     BIGINT          NOT NULL,
    isbn        VARCHAR(13)     NOT NULL,
    quantity    INT             NOT NULL,
    status      VARCHAR(20)     NOT NULL,
    created_at  TIMESTAMPTZ     NOT NULL,
    updated_at  TIMESTAMPTZ     NOT NULL,
    CONSTRAINT pk_customer_orders    PRIMARY KEY (id),
    CONSTRAINT chk_orders_status     CHECK (status IN ('PENDING_INVENTORY', 'RESERVED', 'COMPLETED', 'REJECTED')),
    CONSTRAINT chk_orders_quantity   CHECK (quantity > 0)
);

CREATE INDEX idx_customer_orders_user_id ON customer_orders (user_id);
CREATE INDEX idx_customer_orders_status  ON customer_orders (status);