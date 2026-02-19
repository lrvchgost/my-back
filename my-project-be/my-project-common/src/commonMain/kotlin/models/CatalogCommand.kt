package models

enum class CatalogCommand {
    NONE,
    CREATE,
    UPDATE,
    DELETE,
    READ,
    SEARCH,
    OPTIMIZE,
}