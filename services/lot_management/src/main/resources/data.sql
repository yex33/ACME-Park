INSERT INTO ENTRY_RECORD (ENTRY_RECORD_ID, USER_TYPE, USER_ID, LICENSE_PLATE, GATE_ID, ENTRY_TIME)
VALUES
    ('entry_0', 0, '0', 'squeeze', 'Lot M', '2024-11-25 08:00:00'),
    ('entry_1', 0, '1', 'a', 'Lot A', '2024-11-25 09:00:00'),
    ('entry_2', 0, '2', 'b', 'Lot B', '2024-11-25 10:00:00'),
    ('entry_3', 0, '3', 'c', 'Lot C', '2024-11-25 11:00:00');

INSERT INTO EXIT_RECORD (EXIT_RECORD_ID, ENTRY_RECORD_ID, USER_TYPE, USER_ID, LICENSE_PLATE, GATE_ID, EXIT_TIME)
VALUES
    ('exit_0', 'entry_0', 0, '0', 'squeeze', 'Lot M', NULL),
    ('exit_1', 'entry_1', 0, '1', 'a', 'Lot A', NULL),
    ('exit_2', 'entry_2', 0, '2', 'b', 'Lot B', NULL),
    ('exit_3', 'entry_3', 0, '3', 'c', 'Lot C', NULL);