-- Dataset generated by ChatGPT
-- These data are automatically load at startup when using an in-memory database (e.g., debug)
-- Use http://localhost:9090/h2-console to access the H2 database web UI (sa, password)

INSERT INTO permits (permit_id, organization_id, user_type, license_plates, transponder_id, start_date, expiry_date, processed)
VALUES
    ('5e7c1b28-16a2-4b45-9c1f-83945bfa74b3', 'a1b2c3d4-e5f6-7g89-h0i1-j2k3l4m5n6o7', 'STUDENT', 'ABC123,XYZ789', '1c2a0d45-16f4-41d6-9e3d-4b6a8e6d7f1a', '2024-01-01', '2024-12-31', true),
    ('d9f5e0c2-7b26-4a34-b9a5-345af0a292cd', 'b2c3d4e5-f6g7-89h0-i1j2-k3l4m5n6o7p8', 'STAFF', 'DEF456,GHI789', '9a6b2d34-5c12-4b4a-a3f6-345bc9f12e7d', '2024-02-01', '2024-06-30', true),
    ('86b7d5a4-c7f9-441f-835e-8cb14bf5ef34', 'c3d4e5f6-g7h8-9i0j-1k2l-3m4n5o6p7q8r', 'FACULTY', 'JKL123,MNO456', '7c9e8d21-a4b3-4e2f-b7c8-d9f21e6c5a3b', '2024-03-15', '2025-03-14', true),
    ('3c5af8f6-ec8e-49f8-bb3b-3d5c9f6e2db2', 'd4e5f6g7-h8i9-0j1k-2l3m-4n5o6p7q8r9s', 'VISITOR', 'PQR789', '8d2f5c6e-a7c1-4f9d-9b3e-6e2db49f3c5a', '2024-12-01', '2024-12-01', true),
    ('7a9d3e1b-5b8c-4f9f-823a-d35fbd21b2e7', 'e5f6g7h8-i9j0-1k2l-3m4n-5o6p7q8r9s0t', 'STUDENT', 'STU234,UVW678', '5b7e9a3f-8c6e-4d1b-a3f5-d9b8c2e7f5a1', '2024-05-01', '2024-08-31', true);