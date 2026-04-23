-- Seed data covering all three fulfillment decision paths.
-- These ISBNs are documented in Docs/system-logic.md under "Seed ISBNs".
--
-- ISBN 9780451524935 — George Orwell: 1984 (10 copies available)
--   → canWait=any  → COMPLETED
--
-- ISBN 9780141028088 — Arthur Conan Doyle: The Hound of the Baskervilles (0 copies)
--   → canWait=true → RESERVED
--
-- ISBN 9780060935467 — Sun Tzu: The Art of War (0 copies)
--   → canWait=false → REJECTED

INSERT INTO books (isbn, title, author, genre, publisher, publication_year, language, format, price, available_copies)
VALUES
    ('9780451524935', '1984',                          'George Orwell',       'CLASSIC',  'Signet Classic',    1949, 'EN', 'PAPERBACK',  8.99, 10),
    ('9780141028088', 'The Hound of the Baskervilles', 'Arthur Conan Doyle',  'CRIME',    'Penguin Classics',  1902, 'EN', 'PAPERBACK', 10.99,  0),
    ('9780060935467', 'The Art of War',                'Sun Tzu',             'BIOGRAPHY','Harper Perennial',  2003, 'EN', 'PAPERBACK',  7.99,  0);