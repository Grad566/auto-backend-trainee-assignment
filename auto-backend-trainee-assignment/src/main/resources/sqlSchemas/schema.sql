DROP TABLE IF EXISTS short_urls;
DROP TABLE IF EXISTS origin_urls;

CREATE TABLE origin_urls (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE short_urls (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    origin_url_id INT NOT NULL,
    FOREIGN KEY (origin_url_id) REFERENCES origin_urls(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

