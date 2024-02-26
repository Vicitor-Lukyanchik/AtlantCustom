package com.example.custom.service;

import com.example.custom.entity.Changelog;

import java.util.List;

public interface ChangelogService {

    List<Changelog> getAllLastChangelogs();

    Changelog getOrSaveChangelogByName(String name);

    Changelog saveChangelog(Changelog changelog);

    void deleteAll();
}
