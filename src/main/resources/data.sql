/*SELECT pg_get_serial_sequence('activity', 'id');*/
SELECT setval('activity_id_seq', max(id)) FROM activity;
