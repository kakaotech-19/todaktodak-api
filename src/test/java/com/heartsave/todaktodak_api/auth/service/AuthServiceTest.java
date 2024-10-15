package com.heartsave.todaktodak_api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import com.heartsave.todaktodak_api.auth.dto.LoginIdCheckReq;
import com.heartsave.todaktodak_api.auth.dto.NicknameCheckReq;
import com.heartsave.todaktodak_api.member.entity.Member;
import com.heartsave.todaktodak_api.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
  @Mock private MemberRepository memberRepository;

  @InjectMocks private AuthService authService;

  private final String LOGIN_ID = "TEST_LOGIN";
  private final String NICKNAME = "TEST_NICKNAME";

  private final Member member =
      Member.builder().loginId("TEST_LOGIN").nickname("TEST_NICKNAME").build();

  @BeforeEach
  void setup() {
    // given
    when(memberRepository.save(any(Member.class))).thenReturn(member);
  }

  @Test
  @DisplayName("로그인 아이디 중복 확인")
  void isDuplicatedLoginId() {
    // given
    when(memberRepository.findMemberByLoginId(anyString())).thenReturn(Optional.of(member));

    // when
    memberRepository.save(member);
    LoginIdCheckReq dto = new LoginIdCheckReq(LOGIN_ID);
    boolean isDuplicated = authService.isDuplicatedLoginId(dto);

    // then
    verify(memberRepository, times(1)).findMemberByLoginId(anyString());
    assertThat(isDuplicated).isEqualTo(true);
  }

  @Test
  @DisplayName("닉네임 중복 확인")
  void checkNicknameDuplicationTest() {
    // given
    when(memberRepository.findMemberByNickname(anyString())).thenReturn(Optional.of(member));

    // when
    memberRepository.save(member);
    NicknameCheckReq dto = new NicknameCheckReq(NICKNAME);
    boolean isDuplicated = authService.isDuplicatedNickname(dto);

    // then
    verify(memberRepository, times(1)).findMemberByNickname(anyString());
    assertThat(isDuplicated).isEqualTo(true);
  }
}
