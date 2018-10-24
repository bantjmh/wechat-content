package com.bshuai.content.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bshuai.content.entity.ArticleEntity;

@Repository
public interface ArticleRepository
		extends JpaRepository<ArticleEntity, String>, JpaSpecificationExecutor<ArticleEntity> {

	@Modifying
	@Query(value = "update ArticleEntity set checkStatus =?2 where id = ?1")
	void updateStatus(String id, Integer checkStatus);

}
