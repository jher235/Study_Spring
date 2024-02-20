package org.zerock.w2.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.zerock.w2.dao.MemberDAO;
import org.zerock.w2.domain.MemberVO;
import org.zerock.w2.dto.MemberDTO;
import org.zerock.w2.util.MapperUtil;

@Log4j2
public enum MemberService {
    INSTANCE;

    private MemberDAO memberDAO;
    private ModelMapper modelMapper;

    MemberService(){
        memberDAO = new MemberDAO();
        modelMapper = MapperUtil.INSTANCE.get();
    }

    public MemberDTO login(String mid, String mpw) throws Exception {

        MemberVO vo = memberDAO.getWithPassword(mid,mpw);
        MemberDTO memberDTO = modelMapper.map(vo, MemberDTO.class);

        return memberDTO;

    }

    public void updateUuid(String mid, String uuid) throws Exception {
        memberDAO.updateUuid(mid,uuid);
    }

    public MemberDTO getByUuid(String uuid) throws Exception {

        MemberVO vo = memberDAO.selectUuid(uuid);

        MemberDTO dto = modelMapper.map(vo, MemberDTO.class);

        return dto;
    }

}
