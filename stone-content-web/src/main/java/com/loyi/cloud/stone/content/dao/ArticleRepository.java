package com.loyi.cloud.stone.content.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.loyi.cloud.stone.content.entity.ArticleEntity;

@Repository
public interface ArticleRepository 
extends JpaRepository<ArticleEntity, String>, JpaSpecificationExecutor<ArticleEntity> {

}
