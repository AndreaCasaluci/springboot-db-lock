CREATE OR REPLACE FUNCTION track_product_updates()
RETURNS TRIGGER AS $$
BEGIN
INSERT INTO products_history (product_id, guid, name, quantity, version, updated_at)
VALUES (OLD.id, OLD.guid, OLD.name, OLD.quantity, OLD.version, NOW());

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER product_update_trigger
    BEFORE UPDATE ON products
    FOR EACH ROW
    WHEN (OLD.version <> NEW.version)
    EXECUTE FUNCTION track_product_updates();