package com.ming.snavermyshop.repository;

import com.ming.snavermyshop.model.ApiUseTime;
import com.ming.snavermyshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiUseTimeRepository extends JpaRepository<ApiUseTime, Long> {
    Optional<ApiUseTime> findByUser(User loginUser);
}