package com.heartsave.todaktodak_api.diary.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.heartsave.todaktodak_api.common.BaseTestObject;
import com.heartsave.todaktodak_api.diary.entity.DiaryEntity;
import com.heartsave.todaktodak_api.member.entity.MemberEntity;
import com.heartsave.todaktodak_api.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@DataJpaTest
public class DiaryRepositoryTest {

  @Autowired private DiaryRepository diaryRepository;
  @Autowired private MemberRepository memberRepository;
  private MemberEntity member;
  private DiaryEntity diary;

  @BeforeEach
  void setupAll() {
    member = BaseTestObject.createMemberEntity();
    diary = BaseTestObject.createDiaryEntity(member);
    memberRepository.save(member);
    diaryRepository.save(diary);
  }

  @Test
  @DisplayName("특정 날짜에 해당되는 사용자 일기 없음.")
  void notExistDiaryByDateAndMember() {
    LocalDateTime testTime = LocalDateTime.of(2025, 10, 22, 11, 1);
    boolean exist = diaryRepository.existsByDate(member.getId(), testTime);
    assertThat(exist).as("memberID와 날짜에 해당하는 일기가 있습니다.").isFalse();
  }

  @Test
  @DisplayName("특정 날짜에 해당되는 사용자 일기가 있음.")
  void existDiaryByDateAndMember() {
    boolean exist = diaryRepository.existsByDate(member.getId(), diary.getDiaryCreatedAt());
    assertThat(exist).as("memberID와 날짜에 해당하는 일기가 없습니다.").isTrue();
  }

  @Test
  @DisplayName("Audit 기능 활성화 확인.")
  void auditJpaEntity() {
    assertThat(diary.getCreatedBy()).as("Audit이 설정 되어 있지만, DB에는 null이 들어가 있습니다.").isEqualTo(7L);
    assertThat(diary.getCreatedTime()).as("Audit이 설정 되어 있지만, DB에는 null이 들어가 있습니다.").isNotNull();
    log.info("test diary createdBy = {}", diary.getCreatedBy());
    log.info("test diary createdTime = {}", diary.getCreatedTime());
  }

  @EnableJpaAuditing
  @TestConfiguration
  static class TestJpaConfig {
    @Bean
    AuditorAware<Long> auditorAware() {
      return () -> Optional.of(7L);
    }
  }
}