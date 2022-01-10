package com.icia.member.dto;

import com.icia.member.entuty.MemberEntity;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class MemberDetailDTO {
    private  Long memberId;
    private  String memberEmail;
    private  String memberPassword;
    private  String memberName;

    //MemberEntity -> MemberDetailDTO(받는쪽 선언)
    public static MemberDetailDTO toMemberDetailDTO(MemberEntity memberEntity){
        MemberDetailDTO memberDetailDTO = new MemberDetailDTO();
        memberDetailDTO.setMemberId(memberEntity.getId());
        memberDetailDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDetailDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDetailDTO.setMemberName(memberEntity.getMemberName());
        return memberDetailDTO;
    }

}
