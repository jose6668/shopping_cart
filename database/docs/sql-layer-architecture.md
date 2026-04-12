# SQL Layer Architecture

## Architectural Position

This repository is organized by SQL responsibility first and by object family second.

The current structure separates:
- `DDL` for structural evolution
- `DML` for data changes
- `DCL` for security and permissions
- `TCL` for exceptional transactional operations

## Active Deployment Path

Today the active path for `shopping_cart` is:
- `01_ddl/03_tables`
- `01_ddl/09_indexes`
- `03_dcl/00_roles`
- `03_dcl/01_grants`

The remaining folders stay available for future growth without forcing early complexity into the active deployment chain.

## Why The Master Changelog Lives At Root

This component is a database workspace by itself, so keeping `changelog-master.yaml` at the root makes the deployment contract explicit and easier to audit.

## Rollback Strategy

Each active `changeSet` points to a dedicated rollback file under `05_rollbacks/`.

That keeps forward scripts and reverse scripts separated while preserving traceability.

## Practical Rule

Only files included from `changelog-master.yaml` are active in deployment.

The fact that a folder exists does not mean it is part of the current rollout.
