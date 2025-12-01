package com.promptmanager.promptmanager.repository;

import com.promptmanager.promptmanager.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PromptRepository extends JpaRepository<Prompt, Long> {

    List<Prompt> findByAiModel(String aiModel);
}
