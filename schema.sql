-- Создание таблицы питомцев
CREATE TABLE IF NOT EXISTS pet (
    pet_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    age INT NOT NULL,
    pet_category VARCHAR(50) DEFAULT 'Pet'
);

-- Создание таблицы усыновителей
CREATE TABLE IF NOT EXISTS adopter (
    adopter_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL
);

-- Создание таблицы записей об усыновлении
CREATE TABLE IF NOT EXISTS adoption (
    adopter_id INT NOT NULL,
    pet_id INT NOT NULL,
    adoption_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (adopter_id, pet_id),
    CONSTRAINT fk_adopter FOREIGN KEY (adopter_id) REFERENCES adopter(adopter_id) ON DELETE CASCADE,
    CONSTRAINT fk_pet FOREIGN KEY (pet_id) REFERENCES pet(pet_id) ON DELETE CASCADE
);

-- Создание таблицы приютов (как в вашей базе)
CREATE TABLE IF NOT EXISTS shelter (
    shelter_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);
