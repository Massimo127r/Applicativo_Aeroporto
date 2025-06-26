-- Create the statovolo enum type if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'statovolo') THEN
        CREATE TYPE statovolo AS ENUM ('programmato', 'inRitardo', 'decollato', 'atterrato', 'cancellato');
    END IF;
END$$;

-- Alter the Volo table to use the statovolo enum type
-- First, create a temporary column with the new type
ALTER TABLE Volo ADD COLUMN stato_new statovolo;

-- Update the temporary column with values from the existing stato column
UPDATE Volo SET stato_new = stato::statovolo;

-- Drop the old column and rename the new one
ALTER TABLE Volo DROP COLUMN stato;
ALTER TABLE Volo RENAME COLUMN stato_new TO stato;