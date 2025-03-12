CREATE TABLE products (
                          id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                          guid UUID NOT NULL DEFAULT gen_random_uuid(),
                          name VARCHAR(100) NOT NULL,
                          quantity INT NOT NULL,
                          version INT NOT NULL DEFAULT 0,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP,
                          CONSTRAINT unique_guid_version UNIQUE (guid, version)
);

CREATE INDEX idx_products_guid ON products (guid);
CREATE INDEX idx_products_version ON products (version);
