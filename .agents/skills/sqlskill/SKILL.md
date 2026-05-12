---
name: sqlskill
description: Use when working on SQL schema, queries, migrations, database scripts, or data-model changes for this project. Keywords: SQL, schema, table, index, foreign key, MySQL, database.sql, migration.
---

## SQL Skill

Use this skill for database-oriented changes in this workspace: schema design, SQL scripts, queries, migrations, and service-specific data models.

## Guidance

- Keep SQL simple and explicit.
- Scope changes to the owning service instead of assuming shared tables across services.
- Prefer MySQL-compatible SQL unless the target module documents a different engine.
- Review the relevant service contract or README before changing database structure.
- For the auth service, check [auth-service/database.sql](auth-service/database.sql) before adding or changing tables.

## Good Fits

- Adding or updating tables, indexes, or foreign keys.
- Writing queries for service repositories or reports.
- Adjusting schema to match a SOAP, REST, or gRPC contract change.
- Reviewing or refactoring database scripts in a service module.