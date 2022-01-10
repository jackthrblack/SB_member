package com.icia.member;

import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberService ms;
    @Test
    @DisplayName("회원 데이터 생성")
    public void newMembers() {
        IntStream.rangeClosed(1,15).forEach(i -> {
            ms.save(new MemberSaveDTO("email"+i, "pw"+1,"name"+i));
        });
    }

    /*
    * 회원삭제 테스트코드를 만들어 봅시다
    * 회원삭제 시나리오를 작성해보고 코드도 짜보도록...
    회원가입 ->
     */

    @Test
    @Transactional
    @Rollback
    @DisplayName("삭제 테스트")
    public void deleteMember(){
        // given
        MemberSaveDTO memberSaveDTO = new MemberSaveDTO("deleteEamil","deletePassword","deleteName");
        Long memberId = ms.save(memberSaveDTO);
        System.out.println(memberSaveDTO);
        System.out.println(memberId);
        // when
        ms.deleteById(memberId);
        // then
        boolean result = ms.login(new MemberLoginDTO(memberSaveDTO.getMemberEmail(), memberSaveDTO.getMemberPassword()));
        assertThat(result).isEqualTo(false);

        // MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        assertThrows(NoSuchElementException.class, () ->{
            assertThat(ms.findById(memberId)).isNull();
        });
    }


    }



