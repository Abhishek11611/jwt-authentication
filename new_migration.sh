#!/bin/bash

# Usage:
# ./new_migration.sh user create user_table

MODULE=$1
OPERATION=$2
MIGRATION_NAME=$3

if [ -z "$MODULE" ] || [ -z "$OPERATION" ] || [ -z "$MIGRATION_NAME" ]; then
  echo "Module and migration name required ???????"
  echo "Usage: ./new_migration.sh <module> <operation> <migration_name>"
  echo "Example: ./new_migration.sh user create users_tables"
  exit 1
fi

case "$MODULE" in
  user|token)
    ;;
  *)
    echo "Invalid module ??????: $MODULE"
    echo "Allowed modules: user, token"
    exit 1
    ;;
esac

case "$OPERATION" in
  create|alter|drop)
    ;;
  *)
    echo "Invalid operation ?????: $OPERATION"
    echo "Allowed operations: create, alter, drop"
    exit 1
    ;;
esac

TIMESTAMP=$(date +"%Y%m%d%H%M%S")
FILENAME="V${TIMESTAMP}__${OPERATION}_${MIGRATION_NAME}.sql"

MIGRATION_DIR="src/main/resources/db/migration"

mkdir -p "$MIGRATION_DIR"

FILE_PATH="$MIGRATION_DIR/$FILENAME"

touch "$FILE_PATH"

cat <<EOF > "$FILE_PATH"
-- Migration: $MIGRATION_NAME
-- Module: $MODULE
-- Created at: $(date)

EOF

echo "Migration Created Successfully:"
echo "$FILE_PATH"