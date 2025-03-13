package kr.co.recipick.api.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service 
public class MemberApiServiceImpl implements MemberApiService {
    
    
    @Autowired
    private MemberApiMapper memberApiMapper;

    @Override
    public MemberResponseVO getMemberInfo(String email) {
        
        MemberResponseVO member = memberApiMapper.getMemberInfo(email);
        
        if (member != null) {
            return member;
        }    
        return null;
    }
    
}