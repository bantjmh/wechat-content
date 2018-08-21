package com.loyi.cloud.stone.content.dao;

import com.loyi.cloud.stone.content.entity.DraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by fq on 2018/8/21.
 */
public interface DraftRepository extends JpaRepository<DraftEntity,String>,JpaSpecificationExecutor<DraftEntity>{

}
