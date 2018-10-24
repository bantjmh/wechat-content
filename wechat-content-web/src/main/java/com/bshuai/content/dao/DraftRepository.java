package com.bshuai.content.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.bshuai.content.entity.DraftEntity;

@Repository
public interface DraftRepository extends JpaRepository<DraftEntity,String>,JpaSpecificationExecutor<DraftEntity>{

}
