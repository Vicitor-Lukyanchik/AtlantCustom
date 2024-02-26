package com.example.custom.service.impl;

import com.example.custom.entity.Changelog;
import com.example.custom.repository.ChangelogRepository;
import com.example.custom.service.ChangelogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChangelogServiceImpl implements ChangelogService {

    private final ChangelogRepository changelogRepository;

    @Override
    public List<Changelog> getAllLastChangelogs() {
        return changelogRepository.findAll().stream()
                .sorted(Comparator.comparing(Changelog::getLocalDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Changelog getOrSaveChangelogByName(String name) {
        List<Changelog> changelogs = changelogRepository.findAllByName(name).stream()
                .sorted(Comparator.comparing(Changelog::getLocalDateTime).reversed())
                .collect(Collectors.toList());
        if (changelogs.isEmpty()){
            return changelogRepository.save(Changelog.builder()
                    .name(name).filePath("").localDateTime(LocalDateTime.now())
                    .allCount(0).addedCount(0).build());
        }
        if(changelogs.get(0).getFilePath().isEmpty()){
            changelogs.get(0).setFilePath("Удаление");
        }
        return changelogs.get(0);
    }

    @Override
    @Transactional
    public Changelog saveChangelog(Changelog changelog) {
        return changelogRepository.save(changelog);
    }

    @Override
    @Transactional
    public void deleteAll() {
        changelogRepository.deleteAll();
        changelogRepository.setAutoincrementOnStart();
    }
}
