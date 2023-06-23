package com.alhasid.sender;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SenderUserDetailsService implements UserDetailsService {
    private final SenderDao senderDao;

    public SenderUserDetailsService(SenderDao senderDao) {
        this.senderDao = senderDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return senderDao.selectUserByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User %s not found".formatted(username)));
    }
}
