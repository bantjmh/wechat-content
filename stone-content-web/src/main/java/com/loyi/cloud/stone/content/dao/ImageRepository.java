package com.loyi.cloud.stone.content.dao;

import com.loyi.cloud.stone.content.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by fq on 2018/7/4.
 */
public interface ImageRepository extends JpaRepository<ImageEntity,String>,JpaSpecificationExecutor<ImageEntity> {

    @Query(value = " from ImageEntity where uid = ?1")
    List<ImageEntity> selectByUid(String uid);
}
