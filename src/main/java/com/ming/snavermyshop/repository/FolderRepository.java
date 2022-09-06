package com.ming.snavermyshop.repository;

import com.ming.snavermyshop.model.Folder;
import com.ming.snavermyshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByUser(User user);
    List<Folder> findAllByUserAndNameIn(User user, List<String> names);
}