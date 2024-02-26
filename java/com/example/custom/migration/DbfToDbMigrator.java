package com.example.custom.migration;

import com.example.custom.dto.MessageDto;

public interface DbfToDbMigrator {

    MessageDto migrateAll();

    MessageDto migrateLastUpdate();

    Boolean isActive();

    void deleteAll();
}
