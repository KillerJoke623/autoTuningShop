-- Flyway migration script to change Car-Model relationship to ManyToOne

-- 1. Add new column model_model_id for ManyToOne relationship
ALTER TABLE car ADD COLUMN model_model_id BIGINT;

-- 2. Update model_model_id with existing model_car_part_carpart_id values
-- Предполагаем, что model_car_part_carpart_id содержит правильные model_id (model_id from Model table)
UPDATE car
SET model_model_id = model_car_part_carpart_id;

-- 3. Add foreign key constraint to model_model_id
ALTER TABLE car
    ADD CONSTRAINT fk_car_model
        FOREIGN KEY (model_model_id)
            REFERENCES model (model_id);

-- 4. Make model_model_id NOT NULL (optional, if Car-Model is mandatory)
ALTER TABLE car ALTER COLUMN model_model_id SET NOT NULL;

-- 5. Remove old column model_car_part_carpart_id
ALTER TABLE car DROP COLUMN model_car_part_carpart_id;